package org.helloframework.conf.spring.parser;

import org.helloframework.conf.spring.handler.RedisProperties;
import org.helloframework.core.config.HelloSingleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

public class ConfigRedisBeanDefinitionParser extends HelloSingleBeanDefinitionParser {
    private final static String GROUP_PROPERTY = "group";
    private final static String KEYS_PROPERTY = "keys";
    private final static String REDIS_PROPERTY = "redis";

    @Override
    public boolean canDouble() {
        return false;
    }

    @Override
    protected List<Class> getBeanClass(Element element) {
        return Arrays.asList(RedisProperties.class);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, Class cls) {
        beanAddPropertyValue(element, builder, GROUP_PROPERTY);
        beanAddPropertyValue(element, builder, KEYS_PROPERTY);
        beanAddPropertyValue(element, builder, REDIS_PROPERTY, "");
    }
}
