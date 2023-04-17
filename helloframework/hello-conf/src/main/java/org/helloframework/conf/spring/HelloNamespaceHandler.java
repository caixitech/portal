package org.helloframework.conf.spring;



import org.helloframework.conf.spring.parser.ConfigRedisBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by lanjian
 */
public class HelloNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        this.registerBeanDefinitionParser("conf", new ConfigRedisBeanDefinitionParser());
//        this.registerBeanDefinitionParser("conf_zk", new ConfigZKBeanDefinitionParser());
    }
}