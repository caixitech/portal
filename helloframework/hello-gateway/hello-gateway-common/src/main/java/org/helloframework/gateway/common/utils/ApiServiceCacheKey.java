package org.helloframework.gateway.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiServiceCacheKey {
    private final static Map<Class, Map> keys = new ConcurrentHashMap();
}
