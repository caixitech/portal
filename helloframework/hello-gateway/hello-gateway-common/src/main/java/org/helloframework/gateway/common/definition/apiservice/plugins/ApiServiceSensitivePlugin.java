package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.exception.ApiException;

public interface ApiServiceSensitivePlugin extends Plugin {
    void sensitive(String value) throws ApiException;
}
