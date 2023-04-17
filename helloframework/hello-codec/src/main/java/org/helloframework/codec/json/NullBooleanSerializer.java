package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class NullBooleanSerializer extends JsonSerializer<Object> {
    public static final NullBooleanSerializer INSTANCE = new NullBooleanSerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeBoolean(false);
    }

}
