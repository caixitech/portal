package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.definition.apiservice.ApiExtend;
import org.helloframework.gateway.common.exception.ApiException;

/**
 * Created by lanjian
 */
public interface ApiServiceExtendPlugin extends Plugin {
    void handler(ApiExtend extend) throws ApiException;
}
