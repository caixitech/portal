package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.definition.apiservice.ApiExtend;

/**
 * Created by lanjian
 */
public interface ApiServiceUserValidPlugin<T> extends Plugin {
    void valid(T o, ApiExtend extend);
}
