package org.helloframework.gateway.core.netty.sender.dispatch;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceInvoker;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.definition.base.*;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.FuseException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.ProtoBufUtil;
import org.helloframework.gateway.core.exception.DispatchException;
import org.helloframework.gateway.core.exception.ServiceNoProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by lanjian
 */
public class DefaultNettyClientDispatch implements Dispatch {
    private final static Logger log = LoggerFactory.getLogger(DefaultNettyClientDispatch.class);
    private final static CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache(Constants.FUSE_CACHE_NAME, CacheConfigurationBuilder.
            newCacheConfigurationBuilder(String.class, Integer.class, ResourcePoolsBuilder.heap(100))
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(Constants.FUSE_LIVE_TIMEOUT, TimeUnit.MILLISECONDS)))).build();
    private static Cache<String, Integer> fuseCache = null;

    static {
        cacheManager.init();
        fuseCache = cacheManager.getCache(Constants.FUSE_CACHE_NAME, String.class, Integer.class);
    }

    private ApiServiceContext apiServiceContext;

    private Fuse fuse = serverInfo -> {
        int i = 1;
        String key = serverInfo.buildString();
        if (fuseCache.containsKey(key)) {
            i = fuseCache.get(key) + 1;
        }
        fuseCache.put(key, i);
        if (Constants.FUSE_COUNT < i) {
            fuseCache.remove(key);
            throw new FuseException(key + " 达到熔断次数！");
        }
    };

    public DefaultNettyClientDispatch(ApiServiceContext apiServiceContext) {
        this.apiServiceContext = apiServiceContext;
    }


    public ClientWarp randomClient(ApiServiceRequest apiServiceRequest) {
        String method = ApiServiceInvoker.name(apiServiceRequest.getApi(), apiServiceRequest.getVersion());
        LoadBalance<ServerInfo> loadBalance = apiServiceContext.getClientContext().findLoadBalance(method);
        if (loadBalance == null) {
            throw new ServiceNoProviderException(String.format("%s 服务没有提供者,版本:%s", apiServiceRequest.getApi(), apiServiceRequest.getVersion()));
        }
        ServerInfo serverInfo = loadBalance.random();
        Client client = apiServiceContext.getClientContext().findLiveClient(serverInfo);
        //简易的重试 TODO 计数做熔断 在负载里面将无效节点移除
        if (client == null || client.isConnected()) {
            loadBalance.data().remove(serverInfo);
            loadBalance.init();
            log.info(String.format("负载删除节点是：%s 服务是：%s ", serverInfo, apiServiceRequest.getApi()));
            return randomClient(apiServiceRequest);
        }
        return new ClientWarp(client, loadBalance.timeout(), serverInfo, loadBalance.max(), loadBalance.working());
    }

    public Object send(ApiServiceRequest apiServiceRequest, ClientWarp clientWarp, MessageHandler messageHandler) throws IOException, InterruptedException {
        Client client = clientWarp.getClient();
        if (apiServiceRequest.isSync()) {
            client.send(ProtoBufUtil.serialize(apiServiceRequest, ApiServiceRequest.class));
            return null;
        }
        MessageCountDownLatch messageCountDownLatch = messageHandler.registerMessageCountDownLatch(apiServiceRequest.getId());
        client.send(ProtoBufUtil.serialize(apiServiceRequest, ApiServiceRequest.class));
        boolean success = messageCountDownLatch.await(clientWarp.getTimeout(), TimeUnit.MILLISECONDS);
        if (!success) {
            log.error(String.format("Failed to send message time out msgid:%s  api:%s version:%s", apiServiceRequest.getId(), apiServiceRequest.getApi(), apiServiceRequest.getVersion()));
            throw new ApiException(GateWayCode.TIME_OUT_CODE);
        }
        if (messageCountDownLatch.getException() != null) {
            throw messageCountDownLatch.getException();
        }
        Object message = messageCountDownLatch.getMessage();
        return message;
    }

    public Object dispatch(ApiServiceRequest apiServiceRequest) {
        MessageHandler messageHandler = null;
        ClientWarp clientWarp = null;
        MethodInfo methodInfo = null;
//        MessageCountDownLatch messageCountDownLatch = null;
        String method = ApiServiceInvoker.name(apiServiceRequest.getApi(), apiServiceRequest.getVersion());
        try {
            //这里是递归获取 获取不到走的异常
            clientWarp = randomClient(apiServiceRequest);
            //限流 靠异常阻断业务
            clientWarp.checkMax();
            Client client = clientWarp.getClient();
            messageHandler = client.findMessageHandler();
            clientWarp.getWorking().incrementAndGet();
            //找到 method
            methodInfo = client.findMethod(method);
            if (methodInfo == null) {
                throw new ServiceNoProviderException("methodInfo is not found");
            }
            //这里处理异步
            apiServiceRequest.setSync(methodInfo.isSync());
            return send(apiServiceRequest, clientWarp, messageHandler);
        } catch (ApiException ex) {
            throw ex;
        } catch (ServiceNoProviderException ex) {
            throw ex;
        } catch (Throwable t) {
            try {
                if (clientWarp != null) {
                    //处理熔断计数 熔断到达次数 执行异常
                    fuse.fuse(clientWarp.getServerInfo());
                }
            } catch (FuseException e) {
                if (clientWarp != null) {
                    log.debug("达到熔断要求(" + clientWarp.getServerInfo().buildString() + ")下线");
                    clientWarp.getClient().shutdown();
                }
            }
            throw new DispatchException(apiServiceRequest.getApi(), t);
        } finally {
            //一定要清除msgid
            if (messageHandler != null && !apiServiceRequest.isSync()) {
                messageHandler.removeMessageCountDownLatch(apiServiceRequest.getId());
            }
//            if (messageCountDownLatch != null) {
//                messageCountDownLatch.destroy();
//            }
            if (clientWarp != null) {
                clientWarp.getWorking().decrementAndGet();
            }
//            stopWatch.stop();
            log.debug("dispatch finally,api:{},version:{}", apiServiceRequest.getApi(), apiServiceRequest.getVersion());
        }
    }
}
