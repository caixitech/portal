package org.helloframework.gateway.registry.zk.zkclient;


import org.helloframework.gateway.common.utils.URL;

public interface ZookeeperTransporter {


    ZookeeperClient connect(URL url);

}
