package org.helloframework.gateway.config.spring.parser;

import org.helloframework.core.config.HelloSingleBeanDefinitionParser;
import org.helloframework.gateway.config.ApplicationConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lanjian
 */
public class ApplicationBeanDefinitionParser extends HelloSingleBeanDefinitionParser {
    private final static String NAME_PROPERTY = "name";

    @Override
    protected List<Class> getBeanClass(Element element) {
        return Arrays.asList(ApplicationConfig.class);
    }

    public boolean canDouble() {
        return false;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder,Class beanClass) {
        beanAddPropertyValue(element, builder, NAME_PROPERTY);
    }
}
