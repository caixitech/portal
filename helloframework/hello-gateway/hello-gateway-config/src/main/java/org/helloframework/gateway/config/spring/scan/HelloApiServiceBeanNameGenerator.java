package org.helloframework.gateway.config.spring.scan;

import org.helloframework.gateway.common.annotation.Api;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceDefinition;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceInvoker;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * Created by lanjian
 */
public class HelloApiServiceBeanNameGenerator extends AnnotationBeanNameGenerator {
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            String beanClassName = beanDefinition.getBeanClassName();
            Class bClass = Class.forName(beanClassName);
            if (bClass.isAnnotationPresent(Api.class) && ApiServiceDefinition.class.isAssignableFrom(bClass)) {
                Api apiService = (Api) bClass.getAnnotation(Api.class);
                return ApiServiceInvoker.name(beanClassName, apiService.version());
            } else {
                throw new RuntimeException(String.format("%s is not ApiService", bClass));
            }
        } catch (Exception ex) {
            throw new RuntimeException("HelloBeanNameGenerator", ex);
        }
    }
}
