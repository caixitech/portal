package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.exception.ApiException;

import java.util.List;

/**
 * Created by lanjian on 23/08/2017.
 */
public interface ApiServiceValidatePlugin extends Plugin {
    List<ValidateMessage> validate(Object o) throws ApiException;
}
