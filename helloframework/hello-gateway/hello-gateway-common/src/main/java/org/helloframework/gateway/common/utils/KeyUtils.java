package org.helloframework.gateway.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.helloframework.gateway.common.annotation.CacheKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KeyUtils {

    public static String key(Object o) {
        TreeMap keyMap = new TreeMap();
        List<String> keys = resolve(o, CacheKey.class);
        if (keys.isEmpty()) {
            throw new RuntimeException("keys is null");
        }
        for (String key : keys) {
            try {
                Object value = PropertyUtils.getProperty(o, key);
                keyMap.put(key, value);
            } catch (Exception e) {
                throw new RuntimeException("Property get error:" + key, e);
            }
        }
        return urlParamsByMap(keyMap);
    }

    public static String urlParamsByMap(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        return sb.toString();
    }

    public static List<String> resolve(Object o) {
        List<String> keys = new ArrayList<String>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            keys.add(field.getName());
        }
        return keys;
    }

    public static List<String> resolve(Object o, Class cls) {
        List<String> keys = new ArrayList<String>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(cls)) {
                keys.add(field.getName());
            }
        }
        return keys;
    }

}
