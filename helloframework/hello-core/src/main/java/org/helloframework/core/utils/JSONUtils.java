package org.helloframework.core.utils;


import org.apache.commons.collections.CollectionUtils;
import org.helloframework.codec.json.JSON;
import org.helloframework.codec.json.SerializerFeature;
import org.helloframework.codec.json.ValueFilter;

import java.util.List;

/**
 * creator: lanjian
 */
public class JSONUtils {

    public static <T> T parseObject(String json, Class clazz) {
        return (T) JSON.parseObject(json, clazz);
    }

    public static <T> T parseArray(String json, Class clazz) {
        return (T) JSON.parseArray(json, clazz);
    }

    public static <T> T parseArrayNoQ(String json, Class clazz) {
        return (T) JSON.parseArray("[" + json + "]", clazz);
    }


    private static ValueFilter filter = new ValueFilter() {
        public Object process(Object obj, String s, Object v) {
            if (v == null || "null".equals(v)) {
                return "";
            }
            return v;
        }
    };

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

    public static String toJSONString(List list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return JSON.toJSONString(list);
        }
        return null;
    }
}
