package org.helloframework.gateway.config.spring.parser;

import org.helloframework.core.config.HelloScanBeanDefinitionParser;
import org.helloframework.gateway.common.annotation.ApiPlugin;
import org.helloframework.gateway.config.spring.scan.HelloApiServicePluginsBeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * Created by lanjian
 */
public class PluginsBeanDefinitionParser extends HelloScanBeanDefinitionParser {


    public AnnotationBeanNameGenerator beanNameGenerator() {
        return new HelloApiServicePluginsBeanNameGenerator();
    }

    public Class typeClass() {
        return ApiPlugin.class;
    }

}
