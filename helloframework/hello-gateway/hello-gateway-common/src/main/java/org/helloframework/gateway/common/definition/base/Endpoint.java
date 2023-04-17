package org.helloframework.gateway.common.definition.base;

public interface Endpoint {
    void bind();

    void shutdown();

    void ready();
}
