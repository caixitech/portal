package org.helloframework.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;

public class SpringApplicationContext implements ApplicationContextAware {
    private static final Map<String, Object> cache = new HashMap<String, Object>();
    private static ApplicationContext applicationContext = null;

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        Object object = cache.get(name);
        if (object != null) {
            return (T) object;
        }
        if (applicationContext != null) {
            object = applicationContext.getBean(name);
            if (object != null) {
                cache.put(name, object);
            }
        }
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        Object obj = cache.get("class:" + clazz.getName());
        if (obj != null) {
            return (T) obj;
        }
        T object = null;
        if (applicationContext != null) {
            try {
                object = applicationContext.getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                String shortClassName = ClassUtils.getShortName(clazz);
                object = getBean(Introspector.decapitalize(shortClassName));
            }
            if (object != null) {
                cache.put("class:" + clazz.getName(), object);
            }
        }
        return object;
    }
}