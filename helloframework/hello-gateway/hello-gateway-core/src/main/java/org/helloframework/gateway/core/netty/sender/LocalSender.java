package org.helloframework.gateway.core.netty.sender;

import org.helloframework.gateway.common.definition.apiservice.*;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceReplayCheckPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceSignCheckPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.DefaultApiServiceSignCheckPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.EhCacheApiServiceReplyCheckPlugin;
import org.helloframework.gateway.common.definition.base.Sender;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.config.ConsumerConfig;
import org.helloframework.gateway.core.exception.MessageSendException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by lanjian
 */
public class LocalSender extends ConsumerConfig implements Sender, InitializingBean, ApplicationContextAware {
    private final static ApiServiceContext apiServiceContext = ApiServiceContext.newInstance();
    private final ApiServiceSignCheckPlugin apiServiceSignCheck = new DefaultApiServiceSignCheckPlugin();
    private final ApiServiceReplayCheckPlugin apiServiceReplayCheck = new EhCacheApiServiceReplyCheckPlugin();

    public byte[] send(ApiServiceRequest apiServiceRequest, ApiServiceHeader apiServiceHeader) {
        try {
            if (apiServiceHeader.isEnableReplyCheck()) {
                apiServiceReplayCheck.check(apiServiceHeader, apiServiceRequest);
            }
            if (apiServiceHeader.isEnableSignCheck()) {
                apiServiceSignCheck.check(apiServiceHeader, apiServiceRequest);
            }
            ApiServiceInvoker apiServiceInvoker = apiServiceContext.findApiServiceInvoker(apiServiceRequest.getApi(), apiServiceRequest.getVersion());
            ApiExtend extend = new ApiExtend();
            extend.setClientIp(apiServiceRequest.getClientIp());
            extend.setNonce(apiServiceRequest.getNonce());
            extend.setApi(apiServiceRequest.getApi());
            extend.setVersion(apiServiceRequest.getVersion());
            return apiServiceInvoker.invoke(apiServiceRequest.getData(), extend);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception e) {
            throw new MessageSendException("LocalGWSender", e);
        }
    }

    public void ready(ApiServiceContext apiServiceContext) {
        apiServiceContext.setMode(ApiServiceContext.MODE.local);
        apiServiceContext.setName(getApplicationConfig().getName());
        apiServiceContext.clientReady();
        apiServiceContext.serverReady(null);
    }

    public void shutdown() {
        apiServiceContext.shutdown();
    }

    @Override
    public ApiServiceContext apiServiceContext() {
        return apiServiceContext;
    }


    public void afterPropertiesSet() throws Exception {
        ready(apiServiceContext);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        apiServiceContext.setApplicationContext(applicationContext);
    }
}
