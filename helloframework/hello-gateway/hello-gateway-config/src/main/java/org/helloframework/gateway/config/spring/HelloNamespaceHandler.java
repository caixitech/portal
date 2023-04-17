package org.helloframework.gateway.config.spring;


import org.helloframework.gateway.config.spring.parser.*;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by lanjian
 */
public class HelloNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        this.registerBeanDefinitionParser("application", new ApplicationBeanDefinitionParser());
        this.registerBeanDefinitionParser("provider", new ProviderBeanDefinitionParser());
        this.registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());
        this.registerBeanDefinitionParser("services", new ApiServicesBeanDefinitionParser());
        this.registerBeanDefinitionParser("plugins", new PluginsBeanDefinitionParser());
        this.registerBeanDefinitionParser("consumer_local", new ConsumerLocalBeanDefinitionParser());
        this.registerBeanDefinitionParser("consumer_remote", new ConsumerRemoteBeanDefinitionParser());
        this.registerBeanDefinitionParser("consumer_mesh", new ConsumerMeshBeanDefinitionParser());
    }
}