package org.helloframework.gateway.common.definition;

import org.helloframework.gateway.common.definition.apiservice.*;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceCodecPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.JacksonApiServiceCodecPlugin;
import org.helloframework.gateway.common.definition.base.Sender;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class MeshSender extends Log {

    @Autowired
    private Sender sender;

    private ApiServiceCodecPlugin meshCodecPlugin = new JacksonApiServiceCodecPlugin();


    public <T> T send(MeshRequest meshRequest, Class respCls) {
        if (sender == null) {
            throw new RuntimeException("mesh not open");
        }
        ApiServiceContext apiServiceContext = sender.apiServiceContext();
        if (apiServiceContext == null) {
            throw new RuntimeException("apiServiceContext not open");
        }
        try {
            ApiServiceInvoker apiServiceDefinition = apiServiceContext.findApiServiceInvoker(meshRequest.getApi(), meshRequest.getVersion());
            ApiExtend extend = meshRequest.getExtend();
            checkNull(extend);
            Object data = meshRequest.getData();
            checkNull(data);
            extend.setMesh(true);
            extend.setApi(meshRequest.getApi());
            extend.setVersion(meshRequest.getVersion());
            if (apiServiceDefinition == null) {
                //走tcp服务
                ApiServiceRequest apiServiceRequest = new ApiServiceRequest(meshRequest.getModel(), UUID.randomUUID().toString(), extend.getRequestId(), extend.getAppid(), meshRequest.getApi(), meshRequest.getVersion(), extend.getNonce(), extend.getClientIp(), meshCodecPlugin.encode(data, data.getClass()));
                apiServiceRequest.setMesh(true);
                debug(String.format("request:%s", apiServiceRequest.toString()));
                byte[] respData = this.sender.send(apiServiceRequest, null);
                return (T) meshCodecPlugin.decode(respData, respCls);
            } else {
                //走内部服务
                byte[] respData = apiServiceDefinition.invoke(meshCodecPlugin.encode(meshRequest.getData(), meshRequest.getData().getClass()), extend);
                return (T) meshCodecPlugin.decode(respData, respCls);
            }
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            error("request", ex);
            throw new ApiException(GateWayCode.MESH_ERROR_CODE);
        }
    }

    protected void checkNull(Object o) {
        if (o == null) {
            throw new ApiException(GateWayCode.DATA_NO_EXITS);
        }
    }
}
