package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class LongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken jsonToken = jsonParser.currentToken();
        if (JsonToken.VALUE_TRUE == jsonToken) {
            return 1L;
        } else if (JsonToken.VALUE_FALSE == jsonToken) {
            return 0L;
        } else if (JsonToken.VALUE_STRING == jsonToken) {
            String valueAsString = jsonParser.getValueAsString();
            if (StringUtils.isBlank(valueAsString))
                return null;
            ImmutablePair<Boolean, Date> parseDate = parseDate(valueAsString);
            if (parseDate.getLeft()) {
                return parseDate.getRight().getTime();
            } else {
                return Long.parseLong(valueAsString);
            }
        } else {
            return jsonParser.getLongValue();
        }
    }

    private static final String[] date_patters = {"yyyy-MM-dd'T'HH:mm:ss.SSSX", "yyyy-MM-dd'T'HH:mm:ss.SSS", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd HH:mm:ss.SSSX", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"};

    private ImmutablePair<Boolean, Date> parseDate(String dateStr) {
        try {
            Date parseDate = DateUtils.parseDate(dateStr, date_patters);
            return new ImmutablePair(true, parseDate);
        } catch (ParseException e) {
            return new ImmutablePair(false, null);
        }
    }
}
