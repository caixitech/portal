package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.exception.ApiException;

/**
 * Created by lanjian
 */
public interface ApiServiceReplayCheckPlugin extends Plugin {
    void check(ApiServiceHeader apiServiceHeader, ApiServiceRequest apiServiceRequest) throws ApiException;

}
