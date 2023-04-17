package org.helloframework.gateway.common.definition.apiservice;

import org.helloframework.gateway.common.definition.base.Client;
import org.helloframework.gateway.common.definition.base.LoadBalance;
import org.helloframework.gateway.common.definition.base.MethodInfo;
import org.helloframework.gateway.common.definition.base.ServerInfo;

import java.util.Collection;

/**
 * Created by lanjian on 2017/11/28.
 */
public interface ClientContext {
    void unRegister(ServerInfo serverInfo, Collection<MethodInfo> methods);

    boolean containsServerInfo(ServerInfo serverInfo);

    void register(ServerInfo serverInfo, Client client);

    void registerClient(ServerInfo serverInfo, ApiServiceContext apiServiceContext, Collection<MethodInfo> methods);

    Client findLiveClient(ServerInfo serverInfo);

    LoadBalance findLoadBalance(String method);

    void remove(ServerInfo serverInfo);

    void destroy();

}
