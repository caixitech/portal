package org.helloframework.gateway.common.definition.graph;

import org.helloframework.gateway.common.annotation.graph.GraphOP;

public interface GraphInvoker<I extends GraphRequest, O extends GraphResponse> {

      void invoke(I req, O resp,Class domain, GraphOP op);
}
