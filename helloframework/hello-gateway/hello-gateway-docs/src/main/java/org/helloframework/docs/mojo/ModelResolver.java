package org.helloframework.docs.mojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.helloframework.docs.annotation.DocApiFiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.swagger.v3.core.util.RefUtils.constructRef;

public class ModelResolver {
    public static final Logger log = LoggerFactory.getLogger(ModelResolver.class);
    final static ModelConverterContextImpl context;
    private final static io.swagger.v3.core.jackson.ModelResolver modelResolver;
    static ObjectMapper mapper;

    static {
        mapper = mapper();
        modelResolver = new io.swagger.v3.core.jackson.ModelResolver(mapper);
        context = new ModelConverterContextImpl(modelResolver);
    }


    protected Map<JavaType, String> _resolvedTypeNames = new ConcurrentHashMap<>();

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    protected String _typeName(JavaType type, BeanDescription beanDesc) {
        return _findTypeName(type, beanDesc);
        /*String name = _resolvedTypeNames.get(type);
        if (name != null) {
            return name;
        }
        name = _findTypeName(type, beanDesc);
        _resolvedTypeNames.put(type, name);
        return name;*/
    }

    private Schema<?> clone(Schema<?> property) {
        if (property == null) {
            return property;
        }
        try {
            String cloneName = property.getName();
            property = Json.mapper().readValue(Json.pretty(property), Schema.class);
            property.setName(cloneName);
        } catch (IOException e) {
            System.out.println("Could not clone property, e");
        }
        return property;
    }

    @SuppressWarnings("rawtypes")
    public Schema<?> schema(Class<?> clazz) {
        Schema<?> rootSchema = new ObjectSchema();
        JavaType type = mapper.constructType(clazz);
        final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(type);
        String name;
        if (!ReflectionUtils.isSystemType(type)) {
            name = _typeName(type, beanDesc);
        } else {
            name = type.getTypeName();
        }
        rootSchema.setName(name);
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        Map<String, Schema> modelProps = new LinkedHashMap<>();
        for (BeanPropertyDefinition propDef : properties) {
            Schema<?> property;
            String propName = propDef.getName();
            AnnotatedMember member = propDef.getPrimaryMember();
            if (member == null) {
                final BeanDescription deserBeanDesc = mapper.getDeserializationConfig().introspect(type);
                List<BeanPropertyDefinition> deserProperties = deserBeanDesc.findProperties();
                for (BeanPropertyDefinition prop : deserProperties) {
                    if (StringUtils.isNotBlank(prop.getInternalName()) && prop.getInternalName().equals(propDef.getInternalName())) {
                        member = prop.getPrimaryMember();
                        break;
                    }
                }
            }
            if (member == null || member.getAnnotation(DocApiFiled.class) == null) {
                continue;
            }
            DocApiFiled docApiFiled = member.getAnnotation(DocApiFiled.class);
            //fastjson别名支持
//            JSONField jsonField = member.getAnnotation(JSONField.class);
//            if (jsonField != null) {
//                if (!jsonField.serialize()) {//禁止序列化的字段跳过
//                    continue;
//                }
//                if (StringUtils.isNotEmpty(jsonField.name())) {
//                    propName = jsonField.name();
//                }
//            }
            JavaType propType = member.getType();
            AnnotatedType aType = new AnnotatedType()
                    .type(propType)
                    .name(propName)
                    .parent(rootSchema)
                    .resolveAsRef(false)
                    .jsonViewAnnotation(null)
                    .skipSchemaName(true)
                    .schemaProperty(true)
                    .propertyName(propName);
            property = clone(modelResolver.resolve(aType, context, null));
            if (property != null) {
                if (docApiFiled.required()) {
                    rootSchema.addRequiredItem(propName);
                }
                if (property.getType().equals("array")) {
                    ArraySchema arraySchema = (ArraySchema) property;
                    String ref = arraySchema.getItems().get$ref();
                    if (StringUtils.isNotBlank(ref)) {//转大写开头
                        if (aType.getType() instanceof CollectionType) {
                            Class<?> contentClazz = ((CollectionType) aType.getType()).getContentType().getRawClass();
                            String newRef = constructRef(contentClazz.getSimpleName());
                            if (!Objects.equals(newRef, ref)) {
                                log.error("error, ref:{},clazz ref:{}", ref, newRef);
                                arraySchema.getItems().set$ref(newRef);
                            }
                        } else {
                            int index = ref.lastIndexOf("/");
                            String refName = ref.substring(index + 1);
                            arraySchema.getItems().set$ref(ref.replace(refName, StringUtils.capitalize(refName)));
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(docApiFiled.link())) {
                        property.set$ref(constructRef(docApiFiled.link()));
                    } else {
                        //引用类型处理
                        final BeanDescription propBeanDesc = mapper.getSerializationConfig().introspect(propType);
                        if (property != null && !propType.isContainerType()) {
                            if ("object".equals(property.getType())) {
                                // create a reference for the property
                                String pName = _typeName(propType, propBeanDesc);
                                if (context.getDefinedModels().containsKey(pName)) {
                                    property = new Schema().$ref(constructRef(pName));
                                }
                            } else if (property.get$ref() != null) {
                                property = new Schema().$ref(StringUtils.isNotEmpty(property.get$ref()) ? property.get$ref() : property.getName());
                            }
                        }
                    }
                }
                property.setDescription(docApiFiled.remark());
                property.setTitle(docApiFiled.desc());
                if (StringUtils.isNotEmpty(docApiFiled.example())) {
                    property.setExample(docApiFiled.example());
                }
                property.setName(propName);
                modelProps.put(property.getName(), property);
            }
        }
        rootSchema.setProperties(modelProps);
        context.defineModel(rootSchema.getName(), rootSchema);
        return rootSchema;
    }

    private String _findTypeName(JavaType type, BeanDescription beanDesc) {
        // First, handle container types; they require recursion
        if (type.isArrayType()) {
            return "Array";
        }

        if (type.isMapLikeType() && ReflectionUtils.isSystemType(type)) {
            return "Map";
        }

        if (type.isContainerType() && ReflectionUtils.isSystemType(type)) {
            if (Set.class.isAssignableFrom(type.getRawClass())) {
                return "Set";
            }
            return "List";
        }
        if (beanDesc == null) {
            beanDesc = mapper.getSerializationConfig().introspectClassAnnotations(type);
        }

        PropertyName rootName = mapper.getSerializationConfig().getAnnotationIntrospector().findRootName(beanDesc.getClassInfo());
        if (rootName != null && rootName.hasSimpleName()) {
            return rootName.getSimpleName();
        }
        return TypeNameResolver.std.nameForType(type);
    }


}
