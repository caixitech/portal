package org.helloframework.gateway.core.netty.server;

import io.netty.channel.Channel;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.*;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.ProtoBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;

/**
 * Created by lanjian
 */
public class ServiceInvoker implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(ServiceInvoker.class);

    static {
        RuntimeSchema.getSchema(ApiServiceRequest.class);
        RuntimeSchema.getSchema(ApiServiceResponse.class);
    }

    private Channel channel;
    private byte[] message;
    private ApiServiceContext apiServiceContext;

    public ServiceInvoker(Channel channel, byte[] message, ApiServiceContext apiServiceContext) {
        this.channel = channel;
        this.message = message;
        this.apiServiceContext = apiServiceContext;
    }

    public void invoke() throws IOException {
        long begin = System.currentTimeMillis();
        ApiServiceRequest apiServiceRequest = ProtoBufUtil.deserialize(message, ApiServiceRequest.class);
        ApiServiceResponse apiServiceResponse = null;
        try {
            if (apiServiceRequest == null) {
                throw new RuntimeException("apiServiceRequest is null");
            }
            apiServiceResponse = invoke(apiServiceRequest);
        } catch (ApiException ex) {
            String fullStackTrace = ExceptionUtils.getStackTrace(ex);
            if (!apiServiceRequest.isSync()) {
                apiServiceResponse = new ApiServiceResponse(apiServiceRequest.getId(), apiServiceRequest.getNonce(), Constants.ERROR_INFO, fullStackTrace, ex.getCode(), ex.getMsg(), ex.getLogLevel(), ex.getDesc());
            }
            log.warn(String.format("ServerInvoker call api:%s,version:%s,error:%s,code:%s,msg:%s,desc:%s", apiServiceRequest.getApi(), apiServiceRequest.getVersion(), ex.getCode(), ex.getMsg(), ex.getDesc(), fullStackTrace));
        } catch (Throwable ex) {
            String fullStackTrace = ExceptionUtils.getStackTrace(ex);
            if (!apiServiceRequest.isSync()) {
                apiServiceResponse = new ApiServiceResponse(apiServiceRequest.getId(), apiServiceRequest.getNonce(), Constants.ERROR_INFO, fullStackTrace, GateWayCode.GW_CODE.getCode(), GateWayCode.GW_CODE.getMsg(), Level.ERROR, null);
            }
            log.error(String.format("ServerInvoker call api:%s,version:%s,error:%s,code:%s,msg:%s", apiServiceRequest.getApi(), apiServiceRequest.getVersion(), GateWayCode.GW_CODE.getCode(), GateWayCode.GW_CODE.getMsg(), fullStackTrace));
        } finally {
            if (!apiServiceRequest.isSync()) {
                if (apiServiceResponse != null) {
                    channel.writeAndFlush(ProtoBufUtil.serialize(apiServiceResponse, ApiServiceResponse.class));
                } else {
                    log.error(String.format("ApiServiceResponse not build api:%s,version:%s", apiServiceRequest.getApi(), apiServiceRequest.getVersion()));
                }
            }
//            long execTime = System.currentTimeMillis() - begin;
//            if (execTime > 500) {
//                log.info("ServiceInvoker 耗时{}ms,api:{},version:{}", execTime, apiServiceRequest.getApi(), apiServiceRequest.getVersion());
//            }
        }
    }

    public ApiServiceResponse invoke(ApiServiceRequest apiServiceRequest) throws Exception {
        ApiServiceInvoker apiServiceInvoker = apiServiceContext.findApiServiceInvoker(apiServiceRequest.getApi(), apiServiceRequest.getVersion());
        if (apiServiceInvoker == null) {
            throw new RuntimeException(String.format("%s not found version:%s", apiServiceRequest.getApi(), apiServiceRequest.getVersion()));
        }
        ApiExtend extend = new ApiExtend();
        extend.setAppid(apiServiceRequest.getAppid());
        extend.setRequestId(apiServiceRequest.getRequestId());
        extend.setNonce(apiServiceRequest.getNonce());
        extend.setClientIp(apiServiceRequest.getClientIp());
        extend.setMesh(apiServiceRequest.isMesh());
        extend.setApi(apiServiceRequest.getApi());
        extend.setPath(apiServiceRequest.getPath());
        extend.setToken(apiServiceRequest.getToken());
        extend.setSource(apiServiceRequest.getSource());
        extend.setTest(apiServiceRequest.getTest());
        byte[] data = apiServiceInvoker.invoke(apiServiceRequest.getData(), extend);
        if (apiServiceRequest.isSync()) {
            return null;
        }
        ApiServiceResponse apiServiceResponse = new ApiServiceResponse(apiServiceRequest.getId(), apiServiceRequest.getNonce(), data);
        return apiServiceResponse;
    }


    public void run() {
        try {
            apiServiceContext.getWorking().incrementAndGet();
            invoke();
        } catch (Throwable ex) {
            log.error("invoke", ex);
        } finally {
            apiServiceContext.getWorking().decrementAndGet();
        }
    }
}
