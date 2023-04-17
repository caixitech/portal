package org.helloframework.codec.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;

import static org.helloframework.codec.json.SerializerFeature.*;

/**
 * 使用jackson封装FastJson使用到的API
 */
public abstract class JSON {

    protected final static JsonMapper DEFAULT_MAPPER = initJsonMapper(JsonInclude.Include.NON_NULL);
    protected final static JsonMapper API_JSON_MAPPER = initJsonMapper(JsonInclude.Include.ALWAYS, WriteNullNumberAsZero, WriteNullListAsEmpty, WriteMapNullValue, WriteNullStringAsEmpty, WriteNullBooleanAsFalse);

    private static JsonMapper initJsonMapper(JsonInclude.Include include, SerializerFeature... features) {
        JsonMapper jsonMapper = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .enable(MapperFeature.USE_STD_BEAN_NAMING)
                .defaultTimeZone(TimeZone.getDefault()).defaultLocale(Locale.getDefault())
                .build();
        jsonMapper.setSerializationInclusion(include);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSONObject.class, new JSONObjectDeserializer());
        module.addDeserializer(JSONArray.class, new JSONArrayDeserializer());
        jsonMapper.registerModule(module);

        if (features != null && features.length > 0) {
            CustomBeanSerializerModifier beanSerializerModifier = new CustomBeanSerializerModifier(features);
            jsonMapper.setSerializerFactory(jsonMapper.getSerializerFactory().withSerializerModifier(beanSerializerModifier));
        }
        return jsonMapper;
    }

    public static JsonMapper getJsonMapper(SerializerFeature... features) {
        if (features != null && features.length > 0) {
            List<SerializerFeature> featureList = Arrays.asList(features);
            if (featureList.contains(WriteNullNumberAsZero) && featureList.contains(WriteNullBooleanAsFalse))
                return API_JSON_MAPPER;
            return initJsonMapper(JsonInclude.Include.ALWAYS, features);
        }
        return DEFAULT_MAPPER;
    }

    private static String writeValueAsString(Object object, boolean prettyFormat, SerializerFeature... features) {
        JsonMapper jsonMapper = getJsonMapper(features);
        try {
            if (prettyFormat)
                return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJSONString(Object object, boolean prettyFormat) {
        return writeValueAsString(object, prettyFormat);
    }

    public static String toJSONString(Object object) {
        return writeValueAsString(object, false);
    }

    public static String toJSONString(Object object, SerializerFeature... features) {
        return writeValueAsString(object, false, features);
    }

    public static JSONObject parseObject(String text) {
        return parseObject(text, JSONObject.class);
    }

    public static <T> T parseObject(String json, Type type) {
        return parseObject(json, DEFAULT_MAPPER.getTypeFactory().constructType(type));
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return parseObject(json, DEFAULT_MAPPER.getTypeFactory().constructType(clazz));
    }

    public static <T> T parseObject(String json, TypeReference<T> valueTypeRef) {
        return parseObject(json, DEFAULT_MAPPER.getTypeFactory().constructType(valueTypeRef));
    }

    public static <T> T parseObject(String text, JavaType valueType) {
        try {
            if (StringUtils.isBlank(text))
                return null;
            return DEFAULT_MAPPER.readValue(text, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray parseArray(String text) {
        try {
            JSONArray jsonArray = new JSONArray();
            if (StringUtils.isBlank(text))
                return jsonArray;
            List<JSONObject> jsonObjects = DEFAULT_MAPPER.readValue(text, new TypeReference<List<JSONObject>>() {
            });
            if (null == jsonObjects || jsonObjects.isEmpty())
                return jsonArray;
            for (JSONObject jsonObject : jsonObjects) {
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(text))
                return new ArrayList<>();
            return DEFAULT_MAPPER.readValue(text, DEFAULT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return DEFAULT_MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return DEFAULT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return DEFAULT_MAPPER.convertValue(fromValue, toValueType);
    }

    public String toJSONString() {
        return toJSONString(this);
    }

    @Override
    public String toString() {
        return toJSONString();
    }
}
