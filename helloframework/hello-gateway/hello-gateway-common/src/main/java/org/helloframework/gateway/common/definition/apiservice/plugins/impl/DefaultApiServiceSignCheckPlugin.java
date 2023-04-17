package org.helloframework.gateway.common.definition.apiservice.plugins.impl;

import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.definition.apiservice.plugins.ApiServiceSignCheckPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.sign.SignUtils;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;


/**
 * Created by lanjian
 */
public class DefaultApiServiceSignCheckPlugin implements ApiServiceSignCheckPlugin {


    public void check(ApiServiceHeader apiServiceHeader, ApiServiceRequest apiServiceRequest) throws ApiException {
        boolean verifySuccess = SignUtils.verify(apiServiceHeader.getMethod(),
                apiServiceHeader.getAppSecret(),
                apiServiceHeader.getHeaders(), apiServiceHeader.getPath(),
                null, null);
        if (!verifySuccess) {
            throw new ApiException(GateWayCode.SIGN_CODE);
        }
    }
}
