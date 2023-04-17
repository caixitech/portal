package org.helloframework.gateway.core.netty.client;

import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.apiservice.ClientContext;
import org.helloframework.gateway.common.definition.base.*;
import org.helloframework.gateway.common.utils.NamedThreadFactory;
import org.helloframework.gateway.core.netty.sender.dispatch.weight.TreeWeightLoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lanjian
 * nettyclient 节点容器管理
 */
public class NettyClientContext implements ClientContext {

    private final static Logger log = LoggerFactory.getLogger(NettyClient.class);
    //存活节点
    private final Map<ServerInfo, Client> lives = new ConcurrentHashMap<ServerInfo, Client>();
    //死亡节点
    private final Map<ServerInfo, Client> dies = new ConcurrentHashMap<ServerInfo, Client>();
    //fan
    private final Map<String, LoadBalance<ServerInfo>> loadBalances = new ConcurrentHashMap<String, LoadBalance<ServerInfo>>();

    // 定时任务执行器
    private final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("HelloNettyClientContextTimer", true));

    // 失败重试定时器，定时检查是否有请求失败，如有，无限次重试
    private final ScheduledFuture<?> retryFuture;

    public NettyClientContext() {
        this.retryFuture = retryExecutor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                // 检测并连接注册中心
                try {
                    retry();
                } catch (Throwable t) {
                    log.error("Unexpected error occur at failed retry, cause: " + t.getMessage(), t);
                }
            }
        }, Constants.RETRY_CLIENT_CHECK, Constants.RETRY_CLIENT_CHECK, TimeUnit.MILLISECONDS);
    }


    private void retry() {
        log.debug("定时检查非活跃重连 die size {}", dies.size());
        for (Client client : dies.values()) {
            try {
                if (Constants.DIE_FAIL_COUNT < client.fail()) {
                    dies.remove(client.serverInfo());
                    client.shutdown();
                    log.debug("DIE_FAIL_COUNT Remove after die size {}", dies.size());
                } else {
                    client.connect();
                }
            } catch (Throwable t) {
                // 防御性容错
                log.error("Unexpected error occur at failed retry, cause: " + t.getMessage(), t);
            }
        }
    }


    public LoadBalance findLoadBalance(String method) {
        return loadBalances.get(method);
    }

    public void remove(ServerInfo serverInfo) {
        lock.lock();
        Client client = null;
        try {
            client = lives.get(serverInfo);
            if (client != null) {
                lives.remove(serverInfo);
                unRegisterLoadBalance(serverInfo, client.getMethods());
            }
            client = dies.get(serverInfo);
            if (client != null) {
                dies.remove(serverInfo);
                unRegisterLoadBalance(serverInfo, client.getMethods());
            }
        } finally {
            lock.unlock();
            if (client != null) {
                client.shutdown();
            }
        }
    }

    public Client findLiveClient(ServerInfo serverInfo) {
        return lives.get(serverInfo);
    }


    private final static ReentrantLock lock = new ReentrantLock();


    public void register(ServerInfo serverInfo, Client client) {
        lock.lock();
        try {
            lives.put(serverInfo, client);
            dies.remove(serverInfo);
            registerLoadBalance(serverInfo, client.getMethods());
        } finally {
            lock.unlock();
        }
    }


    public void registerClient(ServerInfo serverInfo, ApiServiceContext apiServiceContext, Collection<MethodInfo> methods) {
        lock.lock();
        try {
            Client client = lives.get(serverInfo);
            if (client == null) {
                log.debug("live not found {}", serverInfo);
                client = dies.get(serverInfo);
                if (client == null) {
                    log.debug("dies not found {}", serverInfo);
                    client = new NettyClient(serverInfo, new NettyClientMessageHandler(), apiServiceContext);
                }
                try {
                    client.connect();
                } catch (Throwable t) {
                    dies.put(serverInfo, client);
                }
            }
            client.addAllMethods(methods);
            registerLoadBalance(serverInfo, methods);
        } finally {
            lock.unlock();
        }
    }


    public boolean containsServerInfo(ServerInfo serverInfo) {
        return lives.containsKey(serverInfo);
    }

    private void registerLoadBalance(ServerInfo serverInfo, Collection<MethodInfo> methods) {
        for (MethodInfo methodInfo : methods) {
            LoadBalance loadBalance = loadBalances.get(methodInfo.getName());
            if (loadBalance == null) {
                loadBalance = new TreeWeightLoadBalance<NettyClient>(new ConcurrentHashMap<NettyClient, Integer>());
                loadBalances.put(methodInfo.getName(), loadBalance);
            }
            loadBalance.updateMethod(methodInfo);
            loadBalance.data().put(serverInfo, serverInfo.getWeight());
            loadBalance.init();
        }

    }

    private void unRegisterLoadBalance(ServerInfo serverInfo, Collection<MethodInfo> methods) {
        for (MethodInfo method : methods) {
            LoadBalance loadBalance = loadBalances.get(method.getName());
            if (loadBalance != null) {
                loadBalance.data().remove(serverInfo);
                loadBalance.init();
            }
        }
    }

    private void unRegisterClient(ServerInfo serverInfo) {
        Client client = lives.get(serverInfo);
        if (client != null) {
            MessageHandler messageHandler = client.findMessageHandler();
            if (messageHandler != null) {
                messageHandler.destroy();
            }
            lives.remove(serverInfo);
            dies.put(serverInfo, client);
        }
    }


    public void destroy() {
        try {
            lives.clear();
            dies.clear();
            loadBalances.clear();
            retryFuture.cancel(true);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }


    public void unRegister(ServerInfo serverInfo, Collection<MethodInfo> methods) {
        lock.lock();
        try {
            unRegisterClient(serverInfo);
            unRegisterLoadBalance(serverInfo, methods);
        } finally {
            lock.unlock();
        }
    }
}
