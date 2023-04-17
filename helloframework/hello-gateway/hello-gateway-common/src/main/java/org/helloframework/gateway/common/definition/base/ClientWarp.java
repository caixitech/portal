package org.helloframework.gateway.common.definition.base;

import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lanjian on 2017/11/28.
 */
public class ClientWarp {
    private final static Logger log = LoggerFactory.getLogger(ClientWarp.class);
    private Client client;
    private int timeout;
    private ServerInfo serverInfo;
    private int max;
    private AtomicInteger working;


    public ClientWarp(Client client, int timeout, ServerInfo serverInfo, Integer max, AtomicInteger working) {
        this.client = client;
        this.timeout = timeout;
        this.serverInfo = serverInfo;
        this.max = max;
        this.working = working;
    }

    public Client getClient() {
        return client;
    }

    public int getTimeout() {
        return timeout;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public int getMax() {
        return max;
    }

    public AtomicInteger getWorking() {
        return working;
    }


    public void checkMax() {
        log.debug(serverInfo + "now woring num " + working.get());
        if (working.get() > max) {
            throw new ApiException(GateWayCode.REQUEST_MAX_CODE);
        }
    }

}
