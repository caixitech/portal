package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.common.definition.apiservice.ApiExtend;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceExtendPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.ValidateMessage;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.CollectionUtils;

import java.util.List;


/**
 * Created by lanjian
 */
@ApiPlugin
public class ApiServiceExtendValidPlugin implements ApiServiceExtendPlugin {

    @Override
    public void handler(ApiExtend extend) throws ApiException {
        List<ValidateMessage> messages = extend.getMessages();
        if (CollectionUtils.isNotEmpty(messages)) {
            //处理字段异常
            ApiException apiException = new ApiException(GateWayCode.FIELD_VALID_ERROR);
            apiException.setMsg(String.format("%s %s", messages.get(0).getProperty(), messages.get(0).getMessage()));
            throw apiException;
        }
    }
}
