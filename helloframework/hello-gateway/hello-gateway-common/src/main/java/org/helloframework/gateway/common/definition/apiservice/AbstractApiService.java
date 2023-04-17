package org.helloframework.gateway.common.definition.apiservice;

import org.helloframework.gateway.common.definition.Log;
import org.helloframework.gateway.common.definition.graph.GraphInvoker;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.CollectionUtils;
import org.helloframework.gateway.common.utils.StringUtils;

import java.util.Collection;

/**
 * Created by lanjian
 */
public abstract class AbstractApiService<I, O> extends Log implements ApiService<I, O> {

    protected void checkNull(Object o) {
        if (o == null) {
            throw new ApiException(GateWayCode.DATA_NO_EXITS);
        }
    }

    protected void checkListNull(Collection collection) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ApiException(GateWayCode.DATA_NO_EXITS);
        }
    }

    /**
     * 预留模板代理
     */
    public void handler(I req, O resp, ApiExtend extend) {
        if (StringUtils.isBlank(extend.getPath())) {
            pathHandler(req, resp, extend);
        }
        service(req, resp, extend);
    }

    /**
     * 预留图注册器
     */
    @Override
    public GraphInvoker graphInvoker(String name) {
        return null;
    }

    public void pathHandler(I req, O resp, ApiExtend extend) {
    }

    protected void notNull(Object o, String field) {
        if (o == null) {
            throw new ApiException(new GateWayCode(GateWayCode.FIELD_VALID_ERROR.getCode(), field + "不能为空"));
        }
    }
}
