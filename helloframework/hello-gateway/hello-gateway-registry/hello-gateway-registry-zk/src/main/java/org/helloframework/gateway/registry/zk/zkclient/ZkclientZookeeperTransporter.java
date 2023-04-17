package org.helloframework.gateway.registry.zk.zkclient;


import org.helloframework.gateway.common.utils.URL;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter {

    public ZookeeperClient connect(URL url) {
        return new ZkclientZookeeperClient(url);
    }

}
