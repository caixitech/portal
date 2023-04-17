package org.helloframework.gateway.core.netty.sender;

import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceReplayCheckPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceSignCheckPlugin;
import org.helloframework.gateway.common.definition.base.Dispatch;
import org.helloframework.gateway.common.definition.base.Sender;
import org.helloframework.gateway.common.definition.registry.RegistryFactory;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.utils.NamedThreadFactory;
import org.helloframework.gateway.common.utils.NetUtils;
import org.helloframework.gateway.common.utils.URL;
import org.helloframework.gateway.config.ConsumerConfig;
import org.helloframework.gateway.core.exception.MessageSendException;
import org.helloframework.gateway.core.exception.ServiceNoProviderException;
import org.helloframework.gateway.core.netty.client.NettyClientContext;
import org.helloframework.gateway.core.netty.sender.dispatch.DefaultNettyClientDispatch;
import org.helloframework.gateway.registry.zk.ZookeeperRegistryFactory;
import org.helloframework.gateway.registry.zk.zkclient.ZkclientZookeeperTransporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.Executors;


/**
 * Created by lanjian
 */
public class NettySender extends ConsumerConfig implements Sender, InitializingBean, ApplicationContextAware {
    private final static Logger log = LoggerFactory.getLogger(NettySender.class);
    private final static ApiServiceContext apiServiceContext = ApiServiceContext.newInstance();
    private final Dispatch dispatch = new DefaultNettyClientDispatch(apiServiceContext);
    //    @Autowired
    private ApiServiceSignCheckPlugin apiServiceSignCheckPlugin;
    //    @Autowired
    private ApiServiceReplayCheckPlugin apiServiceReplayCheckPlugin;

//    public void setApiServiceSignCheck(ApiServiceSignCheck apiServiceSignCheck) {
//        this.apiServiceSignCheck = apiServiceSignCheck;
//    }
//
//    public void setApiServiceReplayCheck(ApiServiceReplayCheck apiServiceReplayCheck) {
//        this.apiServiceReplayCheck = apiServiceReplayCheck;
//    }

    public void afterPropertiesSet() throws Exception {
        ready(apiServiceContext);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutdowning.....");
            shutdown();
        }));
        this.apiServiceSignCheckPlugin = apiServiceContext.findPlugin(ApiServiceSignCheckPlugin.class);
        this.apiServiceReplayCheckPlugin = apiServiceContext.findPlugin(ApiServiceReplayCheckPlugin.class);
    }

    public void shutdown() {
        apiServiceContext.shutdown();
    }

    @Override
    public ApiServiceContext apiServiceContext() {
        return apiServiceContext;
    }

    public byte[] send(final ApiServiceRequest apiServiceRequest, ApiServiceHeader apiServiceHeader) {
        try {
            if (apiServiceHeader != null) {
                if (apiServiceHeader.isEnableReplyCheck()) {
                    apiServiceReplayCheckPlugin.check(apiServiceHeader, apiServiceRequest);
                }
                if (apiServiceHeader.isEnableSignCheck()) {
                    apiServiceSignCheckPlugin.check(apiServiceHeader, apiServiceRequest);
                }
            }
            return (byte[]) dispatch.dispatch(apiServiceRequest);
        } catch (ApiException e) {
            throw e;
        } catch (ServiceNoProviderException e) {
            throw e;
        } catch (Exception e) {
            throw new MessageSendException("Failed to send message  msgid:" + apiServiceRequest.getId(), e);
        }
    }


    public void ready(final ApiServiceContext apiServiceContext) {
        if (getRegistryConfig() == null) {
            throw new IllegalArgumentException("getRegistryConfig is null");
        }
        final RegistryFactory registryFactory = new ZookeeperRegistryFactory(new ZkclientZookeeperTransporter());
        apiServiceContext.setRegistryFactory(registryFactory);
        final URL url = getRegistryConfig().getUrl();
        apiServiceContext.setName(getApplicationConfig().getName());
        apiServiceContext.setRegistryUrl(url);
        apiServiceContext.setMode(ApiServiceContext.MODE.valueOf(getMode()));
        apiServiceContext.setWorkThreads(getWorkThreads());
        apiServiceContext.setThreads(getThreads());
        apiServiceContext.setHostname(NetUtils.getLocalHost());
        apiServiceContext.setExecutorService(Executors.newFixedThreadPool(getWorkThreads(), new NamedThreadFactory(Constants.CLIENT_WORK_THREAD_NAME, true)));
        apiServiceContext.setClientContext(new NettyClientContext());
        apiServiceContext.setNotifyListener(new SenderNotifyListener(apiServiceContext));
        //client回调
        apiServiceContext.clientReady();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        apiServiceContext.setApplicationContext(applicationContext);
    }
}
