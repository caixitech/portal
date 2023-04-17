package org.helloframework.gateway.config.spring.scan;

import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.common.definition.apiservice.plugins.Plugin;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * Created by lanjian
 */
public class HelloApiServicePluginsBeanNameGenerator extends AnnotationBeanNameGenerator {
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            String beanClassName = beanDefinition.getBeanClassName();
            Class bClass = Class.forName(beanClassName);
            if (bClass.isAnnotationPresent(ApiPlugin.class) && Plugin.class.isAssignableFrom(bClass)) {
                return bClass.getName();
            } else {
                throw new RuntimeException(String.format("%s is not ApiServiceUtils", bClass));
            }
        } catch (Exception ex) {
            throw new RuntimeException("HelloBeanNameGenerator", ex);
        }
    }
}
