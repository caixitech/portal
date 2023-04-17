package org.helloframework.gateway.registry.zk.zkclient;

public interface StateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int connected);

}
