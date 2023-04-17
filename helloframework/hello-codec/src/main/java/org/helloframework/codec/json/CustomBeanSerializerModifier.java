package org.helloframework.codec.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomBeanSerializerModifier extends BeanSerializerModifier {
    private boolean isAssignNullArraySerializer = false;
    private boolean isAssignNullNumberSerializer = false;
    private boolean isAssignNullBooleanSerializer = false;
    private boolean isAssignNullStringSerializer = false;
    private boolean isAssignNullMapSerializer = false;
    private ValueFilter valueFilter;

    public CustomBeanSerializerModifier() {
    }

    public CustomBeanSerializerModifier(SerializerFeature... features) {
        if (null == features || features.length == 0)
            return;
        for (SerializerFeature feature : features) {
            if (SerializerFeature.WriteMapNullValue == feature) {
                this.isAssignNullMapSerializer = true;
                continue;
            }
            if (SerializerFeature.WriteNullNumberAsZero == feature) {
                this.isAssignNullNumberSerializer = true;
                continue;
            }
            if (SerializerFeature.WriteNullListAsEmpty == feature) {
                this.isAssignNullArraySerializer = true;
                continue;
            }
            if (SerializerFeature.WriteNullStringAsEmpty == feature) {
                this.isAssignNullStringSerializer = true;
                continue;
            }
            if (SerializerFeature.WriteNullBooleanAsFalse == feature) {
                this.isAssignNullBooleanSerializer = true;
                continue;
            }
        }
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter beanPropertyWriter : beanProperties) {
            if (null != valueFilter) {
                beanPropertyWriter.assignSerializer(new ValueFilterSerializer(beanPropertyWriter.getName(), valueFilter));
            }
            if (isArrayType(beanPropertyWriter) && isAssignNullArraySerializer) {
                beanPropertyWriter.assignNullSerializer(NullArraySerializer.INSTANCE);
                continue;
            }
            if (isNumberType(beanPropertyWriter) && isAssignNullNumberSerializer) {
                beanPropertyWriter.assignNullSerializer(NullNumberSerializer.INSTANCE);
                continue;
            }
            if (isBooleanType(beanPropertyWriter) && isAssignNullBooleanSerializer) {
                beanPropertyWriter.assignNullSerializer(NullBooleanSerializer.INSTANCE);
                continue;
            }
            if (isStringType(beanPropertyWriter) && isAssignNullStringSerializer) {
                beanPropertyWriter.assignNullSerializer(NullStringSerializer.INSTANCE);
                continue;
            }
            if (isMapType(beanPropertyWriter) && isAssignNullMapSerializer) {
                beanPropertyWriter.assignNullSerializer(NullMapSerializer.INSTANCE);
                continue;
            }
        }
        return beanProperties;
    }

    /**
     * 是否是数组
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是string
     */
    private boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是int
     */
    private boolean isNumberType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Number.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是boolean
     */
    private boolean isBooleanType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Boolean.class.equals(clazz);
    }

    /**
     * 是否是map
     */
    private boolean isMapType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Map.class.isAssignableFrom(clazz);
    }

    public void setAssignNullArraySerializer(boolean assignNullArraySerializer) {
        isAssignNullArraySerializer = assignNullArraySerializer;
    }

    public void setAssignNullNumberSerializer(boolean assignNullNumberSerializer) {
        isAssignNullNumberSerializer = assignNullNumberSerializer;
    }

    public void setAssignNullBooleanSerializer(boolean assignNullBooleanSerializer) {
        isAssignNullBooleanSerializer = assignNullBooleanSerializer;
    }

    public void setAssignNullStringSerializer(boolean assignNullStringSerializer) {
        isAssignNullStringSerializer = assignNullStringSerializer;
    }

    public void setAssignNullMapSerializer(boolean assignNullMapSerializer) {
        isAssignNullMapSerializer = assignNullMapSerializer;
    }

    public void setValueFilter(ValueFilter valueFilter) {
        this.valueFilter = valueFilter;
    }
}
