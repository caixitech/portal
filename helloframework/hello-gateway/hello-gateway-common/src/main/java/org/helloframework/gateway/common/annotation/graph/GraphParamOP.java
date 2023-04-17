package org.helloframework.gateway.common.annotation.graph;

import java.util.Collection;

public enum GraphParamOP {
    gt, gte, lt, lte, eq, set, in, nin;

    public String opName(String criteria) {
        return String.format("%s_%s", name(), criteria);
    }

    public void checkArray(Class cls) {
        if (!Collection.class.isAssignableFrom(cls)) {
            throw new RuntimeException("in must array");
        }
    }
}