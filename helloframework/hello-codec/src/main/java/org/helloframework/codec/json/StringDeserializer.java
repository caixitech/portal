package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (JsonToken.START_OBJECT == currentToken || JsonToken.START_ARRAY == currentToken)
            return jsonParser.getCodec().readTree(jsonParser).toString();
        return jsonParser.getValueAsString();
    }
}
