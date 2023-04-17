package org.helloframework.gateway.registry.zk;


import org.helloframework.gateway.common.definition.registry.Registry;
import org.helloframework.gateway.common.utils.URL;
import org.helloframework.gateway.registry.zk.support.AbstractRegistryFactory;
import org.helloframework.gateway.registry.zk.zkclient.ZookeeperTransporter;

/**
 * ZookeeperRegistryFactory.
 */
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {


    private ZookeeperTransporter zookeeperTransporter;


    public ZookeeperRegistryFactory(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    public Registry createRegistry(URL url) {
        return new ZookeeperRegistry(url, zookeeperTransporter);
    }

}