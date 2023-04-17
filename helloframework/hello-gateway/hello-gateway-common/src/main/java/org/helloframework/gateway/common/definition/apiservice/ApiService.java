package org.helloframework.gateway.common.definition.apiservice;

import org.helloframework.gateway.common.definition.graph.GraphInvoker;

/**
 * Created by lanjian
 */
public interface ApiService<I, O> extends ApiServiceDefinition {

    void handler(I req,O resp, ApiExtend extend);

    void service(I req,O resp, ApiExtend extend);

    GraphInvoker graphInvoker(String name);

}
