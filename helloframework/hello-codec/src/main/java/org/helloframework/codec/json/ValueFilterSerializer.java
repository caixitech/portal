package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ValueFilterSerializer extends JsonSerializer<Object> {
    private String name;
    private ValueFilter valueFilter;

    public ValueFilterSerializer(String name, ValueFilter valueFilter) {
        this.name = name;
        this.valueFilter = valueFilter;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(valueFilter.process(gen.currentValue(), name, value));
    }
}
