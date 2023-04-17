package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.exception.ApiException;

/**
 * Created by lanjian
 */
public interface ApiServiceRequestPlugin<I, O> extends Plugin {
    O handler(I i) throws ApiException;
}
