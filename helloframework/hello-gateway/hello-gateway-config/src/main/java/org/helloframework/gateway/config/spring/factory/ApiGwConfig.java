package org.helloframework.gateway.config.spring.factory;

import java.util.Map;

public class ApiGwConfig {

    private Map<String, String> setAppSecretKeyMap;

    private boolean enableReplyCheck;

    private boolean enableSignCheck;

    public boolean isEnableReplyCheck() {
        return enableReplyCheck;
    }

    public void setEnableReplyCheck(boolean enableReplyCheck) {
        this.enableReplyCheck = enableReplyCheck;
    }

    public boolean isEnableSignCheck() {
        return enableSignCheck;
    }

    public void setEnableSignCheck(boolean enableSignCheck) {
        this.enableSignCheck = enableSignCheck;
    }

    public Map<String, String> getAppSecretKeyMap() {
        return setAppSecretKeyMap;
    }

    public void setAppSecretKeyMap(Map<String, String> appSecretKeyMap) {
        this.setAppSecretKeyMap = appSecretKeyMap;
    }
}
