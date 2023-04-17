package org.helloframework.gateway.config.spring.parser;

import org.helloframework.core.config.HelloSingleBeanDefinitionParser;
import org.helloframework.gateway.config.RegistryConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lanjian
 */
public class RegistryBeanDefinitionParser extends HelloSingleBeanDefinitionParser {
    private final static String ADDRESS_PROPERTY = "address";


    @Override
    protected List<Class> getBeanClass(Element element) {
        return Arrays.asList(RegistryConfig.class);
    }

    public boolean canDouble() {
        return false;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, Class cls) {
        beanAddPropertyValue(element, builder, ADDRESS_PROPERTY);
    }
}
