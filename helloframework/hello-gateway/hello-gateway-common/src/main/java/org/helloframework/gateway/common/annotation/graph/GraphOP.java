package org.helloframework.gateway.common.annotation.graph;

//只支持查询和更新
public enum GraphOP {
    query, update, delete, insert;

    public String finalName(String domain, String version, String name) {
        String finalName = name == null ? "" : name;
        switch (this) {
            case query:
                return String.format("%s.%s%s_%s", domain, finalName, "query", version);
            case update:
                return String.format("%s.%s%s_%s", domain, finalName, "update", version);
            case delete:
                return String.format("%s.%s%s_%s", domain, finalName, "delete", version);
            case insert:
                return String.format("%s.%s%s_%s", domain, finalName, "insert", version);
            default:
                throw new RuntimeException("GraphOP Error");
        }
    }
}
