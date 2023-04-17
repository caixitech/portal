package org.helloframework.gateway.registry.zk.zkclient;

import java.util.List;

public interface ChildListener {

    void childChanged(String path, List<String> children);

}
