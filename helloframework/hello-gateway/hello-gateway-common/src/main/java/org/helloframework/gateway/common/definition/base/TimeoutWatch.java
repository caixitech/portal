package org.helloframework.gateway.common.definition.base;


import org.helloframework.gateway.common.exception.ConnectedException;

/**
 * Created by lanjian on 08/09/2017.
 */
public interface TimeoutWatch {
    void reconnect() throws ConnectedException;
}
