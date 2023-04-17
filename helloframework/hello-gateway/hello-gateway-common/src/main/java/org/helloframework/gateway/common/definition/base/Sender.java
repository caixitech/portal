package org.helloframework.gateway.common.definition.base;

import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;

/**
 * Created by lanjian
 */
public interface Sender {
    byte[] send(ApiServiceRequest apiServiceRequest, ApiServiceHeader apiServiceHeader);

    void ready(ApiServiceContext apiServiceContext);

    void shutdown();

    ApiServiceContext apiServiceContext();
}
