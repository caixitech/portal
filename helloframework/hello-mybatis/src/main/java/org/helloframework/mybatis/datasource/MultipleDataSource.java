package org.helloframework.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Author lanjian
 * Email  lanjian
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();
    private static final Logger logger = LoggerFactory.getLogger(MultipleDataSource.class);
    private static String defaultReadKey;
    private static String defaultWriteKey;


    public static String getDefaultReadKey() {
        if (defaultReadKey == null) {
            throw new IllegalArgumentException("defaultReadKey not null");
        }
        return defaultReadKey;
    }

    public void setDefaultReadKey(String defaultReadKey) {
        MultipleDataSource.defaultReadKey = defaultReadKey;
    }

    @Override
    public void afterPropertiesSet() {
        //TODO 初始化targetsources
        super.afterPropertiesSet();
    }

    public static String getDefaultWriteKey() {
        if (defaultWriteKey == null) {
            throw new IllegalArgumentException("defaultWriteKey not null");
        }
        return defaultWriteKey;
    }

    public void setDefaultWriteKey(String defaultWriteKey) {
        MultipleDataSource.defaultWriteKey = defaultWriteKey;
    }

    public static void changeDataSourceKey(String dataSource) {
        if (logger.isDebugEnabled()) {
            logger.debug("changeDataSourceKey {} 数据库", dataSource);
        }
        dataSourceKey.set(dataSource);
    }


    @Override
    protected Object determineCurrentLookupKey() {
        String key = dataSourceKey.get();
        if (logger.isDebugEnabled()) {
            logger.debug("determineCurrentLookupKey {} 数据库", key);
        }
        return key;
    }
}
