package org.helloframework.codec.json;

import com.fasterxml.jackson.databind.util.StdConverter;

public class Object2StringConverter extends StdConverter<Object, String> {
    @Override
    public String convert(Object value) {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        return JSON.toJSONString(value);
    }
}
