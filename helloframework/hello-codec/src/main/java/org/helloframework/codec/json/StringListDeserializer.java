package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StringListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.START_ARRAY) {
            return jsonParser.getCodec().readValue(jsonParser, new TypeReference<List<String>>() {
            });
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            String valueAsString = jsonParser.getValueAsString();
            if (valueAsString.startsWith(JsonToken.START_ARRAY.asString()) && valueAsString.endsWith(JsonToken.END_ARRAY.asString())) {
                return JSON.parseArray(valueAsString, String.class);
            }
            return Arrays.asList(valueAsString);
        }
        return Arrays.asList(jsonParser.getCodec().readTree(jsonParser).toString());
    }
}
