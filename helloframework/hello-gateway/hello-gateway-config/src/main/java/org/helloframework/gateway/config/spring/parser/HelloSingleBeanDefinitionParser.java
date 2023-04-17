//package org.helloframework.gateway.config.spring.parser;
//
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinitionHolder;
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
//import org.springframework.beans.factory.xml.BeanDefinitionParser;
//import org.springframework.beans.factory.xml.ParserContext;
//import org.springframework.util.StringUtils;
//import org.w3c.dom.Element;
//
//import java.util.List;
//
///**
// * Created by lanjian
// */
//public abstract class HelloSingleBeanDefinitionParser implements BeanDefinitionParser {
//
//    private static final String BEAN_ID = "id";
//
//    public abstract boolean canDouble();
//
//    public BeanDefinition parse(Element element, ParserContext parserContext) {
//        List<Class> clss = getBeanClass(element);
//        if (clss == null || clss.isEmpty()) {
//            return null;
//        }
//        for (Class cls : clss) {
//            String beanName = element.getAttribute(BEAN_ID);
//            if (!StringUtils.hasText(beanName)) {
//                beanName = cls.getName();
//                if (!canDouble()) {
//                    if (parserContext.getRegistry().containsBeanDefinition(beanName)) {
//                        throw new RuntimeException("can not double");
//                    }
//                }
//            }
//            BeanDefinition beanDefinition = parseInternal(element, parserContext, cls);
//            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
//            BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
//        }
//        return null;
//    }
//
//    public BeanDefinition parseInternal(Element element, ParserContext parserContext, Class beanClass) {
//        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//        if (beanClass != null) {
//            builder.getRawBeanDefinition().setBeanClass(beanClass);
//        }
//        builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
//        if (parserContext.isNested()) {
//            builder.setScope(parserContext.getContainingBeanDefinition().getScope());
//        }
//        if (parserContext.isDefaultLazyInit()) {
//            builder.setLazyInit(true);
//        }
//        doParse(element, parserContext, builder, beanClass);
//        return builder.getBeanDefinition();
//    }
//
//    protected abstract List<Class> getBeanClass(Element element);
//
//    protected abstract void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, Class cls);
//
//    protected void beanAddPropertyValue(Element element, BeanDefinitionBuilder bean, String key, Object defaultValue) {
//        String value = element.getAttribute(key);
//        if (StringUtils.hasText(value)) {
//            bean.addPropertyValue(key, value);
//        } else {
//            if (defaultValue != null) {
//                bean.addPropertyValue(key, defaultValue);
//            } else {
//                throw new RuntimeException("key " + key + " must set");
//            }
//        }
//    }
//
//    protected void beanAddPropertyValue(Element element, BeanDefinitionBuilder bean, String key) {
//        beanAddPropertyValue(element, bean, key, null);
//    }
//}
