package org.helloframework.core.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * Created by lanjian
 */
public abstract class HelloScanBeanDefinitionParser implements BeanDefinitionParser {
    public abstract AnnotationBeanNameGenerator beanNameGenerator();

    public abstract Class typeClass();

    protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
        return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters,
                readerContext.getEnvironment(), readerContext.getResourceLoader());
    }

    public TypeFilter excludeFilter(Element element) {
        final String exclude = element.getAttribute("exclude");
        if (StringUtils.isEmpty(exclude)) {
            return null;
        }
        return new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String[] excludeNames = StringUtils.tokenizeToStringArray(exclude, ",;\t\n");
                for (int i = 0; i < excludeNames.length; i++) {
                    String className = metadataReader.getClassMetadata().getClassName();
                    String packageName = className.substring(className.lastIndexOf(".") + 1);
                    if (packageName.equalsIgnoreCase(excludeNames[i])) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String basePackage = element.getAttribute("package");
        if (StringUtils.isEmpty(basePackage)) {
            return null;
        }
        basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
        String[] basePackages = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");
        XmlReaderContext xmlReaderContext = parserContext.getReaderContext();
        ClassPathBeanDefinitionScanner scanner = createScanner(xmlReaderContext, true);
        scanner.setBeanNameGenerator(beanNameGenerator());
        scanner.resetFilters(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(typeClass()));
        TypeFilter excludeFilter = excludeFilter(element);
        if (excludeFilter != null) {
            scanner.addExcludeFilter(excludeFilter);
        }
        scanner.scan(basePackages);
        return null;
    }
}
