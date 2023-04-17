package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.definition.apiservice.ApiExtend;
import org.helloframework.gateway.common.exception.ApiException;

/**
 * Created by lanjian
 */
public interface ApiServiceResponsePlugin<I, O> extends Plugin {
    O handler(I i, ApiExtend extend) throws ApiException;
}
