package org.helloframework.gateway.common.definition.apiservice.plugins;

import org.helloframework.gateway.common.definition.apiservice.ApiExtend;
import org.helloframework.gateway.common.exception.ApiException;

/**
 * @author qianwang5
 * @date 2020/8/12 8:33 上午
 */
public interface ApiServiceProxyPlugin<I, O> extends Plugin {
    O handler(I i, ApiExtend extend);

    O handler0(I i, ApiExtend extend) throws ApiException;
}
