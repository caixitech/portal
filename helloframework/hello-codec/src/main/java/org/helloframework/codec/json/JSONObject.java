package org.helloframework.codec.json;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public final class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private final Map<String, Object> map;

    public JSONObject() {
        this(DEFAULT_INITIAL_CAPACITY, false);
    }

    public JSONObject(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONObject(boolean ordered) {
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public JSONObject(int initialCapacity) {
        this(initialCapacity, false);
    }

    public JSONObject(int initialCapacity, boolean ordered) {
        if (ordered) {
            map = new LinkedHashMap<String, Object>(initialCapacity);
        } else {
            map = new HashMap<String, Object>(initialCapacity);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        boolean result = map.containsKey(key);
        if (!result) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                result = map.containsKey(key.toString());
            }
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        Object val = map.get(key);

        if (val == null) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                val = map.get(key.toString());
            }
        }

        return val;
    }

    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);

        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return super.convertValue(value, JSONObject.class);
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);

        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        if (value instanceof String) {
            return super.parseArray(String.valueOf(value));
        }

        return super.convertValue(value, JSONArray.class);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        return super.convertValue(obj, clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        return super.convertValue(value, Boolean.class);
    }

    public byte[] getBytes(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        return super.convertValue(value, new TypeReference<byte[]>() {
        });
    }

    public boolean getBooleanValue(String key) {
        Object value = get(key);
        if (value == null)
            return false;
        return super.convertValue(value, boolean.class);
    }

    public Byte getByte(String key) {
        Object value = get(key);
        return super.convertValue(value, Byte.class);
    }

    public byte getByteValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0;
        return super.convertValue(value, byte.class);
    }

    public Short getShort(String key) {
        Object value = get(key);
        return super.convertValue(value, Short.class);
    }

    public short getShortValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0;
        return super.convertValue(value, short.class);
    }

    public Integer getInteger(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, Integer.class);
    }

    public int getIntValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0;
        return super.convertValue(value, int.class);
    }

    public Long getLong(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, Long.class);
    }

    public long getLongValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0L;
        return super.convertValue(value, long.class);
    }

    public Float getFloat(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, Float.class);
    }

    public float getFloatValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0F;
        return super.convertValue(value, float.class);
    }

    public Double getDouble(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, Double.class);
    }

    public double getDoubleValue(String key) {
        Object value = get(key);
        if (value == null)
            return 0D;

        return super.convertValue(value, double.class);
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, BigDecimal.class);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, BigInteger.class);
    }

    public String getString(String key) {
        Object value = get(key);
        if (value == null)
            return null;
        return value.toString();
    }

    public Date getDate(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, Date.class);
    }

    public java.sql.Date getSqlDate(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, java.sql.Date.class);
    }

    public java.sql.Timestamp getTimestamp(String key) {
        Object value = get(key);
        if (null == value)
            return null;
        return super.convertValue(value, java.sql.Timestamp.class);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public JSONObject fluentPut(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    public JSONObject fluentPutAll(Map<? extends String, ?> m) {
        map.putAll(m);
        return this;
    }

    public void clear() {
        map.clear();
    }

    public JSONObject fluentClear() {
        map.clear();
        return this;
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public JSONObject fluentRemove(Object key) {
        map.remove(key);
        return this;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object clone() {
        return new JSONObject(map instanceof LinkedHashMap //
                ? new LinkedHashMap<String, Object>(map) //
                : new HashMap<String, Object>(map)
        );
    }

    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public Map<String, Object> getInnerMap() {
        return this.map;
    }

    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONObject.class || clazz == JSON.class) {
            return (T) this;
        }

        if (clazz == Object.class) {
            return (T) this;
        }
        return super.convertValue(this.map, clazz);
    }


}
