package org.helloframework.gateway.config.spring.parser;

import org.helloframework.core.config.HelloScanBeanDefinitionParser;
import org.helloframework.gateway.common.annotation.Api;
import org.helloframework.gateway.config.spring.scan.HelloApiServiceBeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * Created by lanjian
 */
public class ApiServicesBeanDefinitionParser extends HelloScanBeanDefinitionParser {
    public AnnotationBeanNameGenerator beanNameGenerator() {
        return new HelloApiServiceBeanNameGenerator();
    }

    public Class typeClass() {
        return Api.class;
    }

}
