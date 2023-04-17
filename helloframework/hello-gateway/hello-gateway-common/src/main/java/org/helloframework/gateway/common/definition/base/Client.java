package org.helloframework.gateway.common.definition.base;


import java.util.Collection;

/**
 * Created by lanjian
 */
public interface Client {
    void connect();

    void send(final Object message);

//    void reconnect();

    void shutdown();

    void addAllMethods(Collection<MethodInfo> methods);

    Collection<MethodInfo> getMethods();

    MethodInfo findMethod(String name);

    MessageHandler findMessageHandler();

    boolean isConnected();

    int fail();

    ServerInfo serverInfo();


}
