package org.helloframework.http.plugin;

import org.helloframework.http.common.HttpInfo;

import java.util.Map;

public interface HttpPlugin {
    void plugin(Map<String, HttpInfo> httpInfos);
}
