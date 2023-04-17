package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken jsonToken = jsonParser.currentToken();
        if (JsonToken.VALUE_TRUE == jsonToken) {
            return true;
        } else if (JsonToken.VALUE_FALSE == jsonToken) {
            return false;
        } else if (JsonToken.VALUE_NUMBER_INT == jsonToken) {
            int intValue = jsonParser.getIntValue();
            return intValue == 0 ? false : true;
        } else if (JsonToken.VALUE_STRING == jsonToken) {
            String valueAsString = jsonParser.getValueAsString();
            if (StringUtils.isBlank(valueAsString))
                return null;
            if (TRUE.equalsIgnoreCase(valueAsString))
                return true;
            if (FALSE.equalsIgnoreCase(valueAsString))
                return false;
            boolean isNumeric = StringUtils.isNumeric(valueAsString);
            if (isNumeric) {
                int intValue = Integer.valueOf(valueAsString);
                return intValue == 0 ? false : true;
            }
            throw new JsonParseException(jsonParser, String.format("Current token (%s) not of boolean type", jsonToken));
        } else {
            return jsonParser.getBooleanValue();
        }
    }
}
