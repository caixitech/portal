package org.helloframework.codec.json;

public interface ValueFilter {
    Object process(Object object, String name, Object value);
}
