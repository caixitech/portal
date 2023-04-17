package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class IntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken jsonToken = jsonParser.currentToken();
        if (JsonToken.VALUE_TRUE == jsonToken) {
            return 1;
        } else if (JsonToken.VALUE_FALSE == jsonToken) {
            return 0;
        } else if (JsonToken.VALUE_STRING == jsonToken) {
            String valueAsString = jsonParser.getValueAsString();
            if (StringUtils.isBlank(valueAsString))
                return null;
            return Integer.valueOf(valueAsString);
        } else {
            return jsonParser.getIntValue();
        }
    }
}
