package org.helloframework.gateway.config.spring.parser;

import org.helloframework.core.config.HelloSingleBeanDefinitionParser;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.config.ApplicationConfig;
import org.helloframework.gateway.config.RegistryConfig;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lanjian
 */
public class ConsumerRemoteBeanDefinitionParser extends HelloSingleBeanDefinitionParser {
    private final static String THREADS_PROPERTY = "threads";
    private final static String WORK_THREADS_PROPERTY = "workThreads";
    private final static String MODE_PROPERTY = "mode";
    private final static String REGISTRY_CONFIG_PROPERTY = "registryConfig";
    private final static String APPLICATION_PROPERTY = "applicationConfig";

    @Override
    protected List<Class> getBeanClass(Element element) {
        try {
            return Arrays.asList(Class.forName(Constants.CLIENT_DEFAULT_CLASS));
        } catch (Exception ex) {
            throw new RuntimeException("ServerBeanDefinitionParser", ex);
        }
    }

    public boolean canDouble() {
        return false;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder,Class beanClass) {
        beanAddPropertyValue(element, builder, MODE_PROPERTY, Constants.REMOTE_MODE);
        beanAddPropertyValue(element, builder, THREADS_PROPERTY, Constants.DEFAULT_CPU_THREADS);
        beanAddPropertyValue(element, builder, WORK_THREADS_PROPERTY, Constants.DEFAULT_CPU_THREADS);
        builder.addPropertyValue(MODE_PROPERTY, Constants.REMOTE_MODE);
        builder.addPropertyValue(APPLICATION_PROPERTY, new RuntimeBeanReference(ApplicationConfig.class.getName()));
        builder.addPropertyValue(REGISTRY_CONFIG_PROPERTY, new RuntimeBeanReference(RegistryConfig.class.getName()));
    }
}
