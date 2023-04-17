package org.helloframework.mybatis.mysql;

import org.apache.commons.lang3.StringUtils;
import org.helloframework.core.dto.PageData;
import org.helloframework.mybatis.cache.CacheCall;
import org.helloframework.mybatis.definition.DBSql;
import org.helloframework.mybatis.definition.EntityDefinition;
import org.helloframework.mybatis.definition.GenerationType;
import org.helloframework.mybatis.definition.SqlBaseDao;
import org.helloframework.mybatis.query.builder.DBBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public abstract class AbstractMySqlDao<T> extends SqlSessionDaoSupport implements ApplicationContextAware, SqlBaseDao<T> {

    public String template() {
        return null;
    }

    protected abstract Class mapperClass();

    public String namespace() {
        if (mapperClass() == null) {
            throw new RuntimeException("not impl");
        }
        return mapperClass().getName();
    }

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    interface Call {
        <T> T call();
    }

    @Override
    protected void checkDaoConfig() {
        String template = template();
        if (StringUtils.isNotBlank(template)) {
            SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) applicationContext.getBean(template);
            setSqlSessionTemplate(sqlSessionTemplate);
        } else {
            SqlSessionTemplate sqlSessionTemplate = applicationContext.getBean(SqlSessionTemplate.class);
            setSqlSessionTemplate(sqlSessionTemplate);
        }
        DBSql.register(this);
        super.checkDaoConfig();
    }

    public Integer update(DBBuilder dbBuilder) {
        MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return k.update(dbBuilder);
    }

    public Integer insert(DBBuilder dbBuilder) {
        MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (GenerationType.IDENTITY.equals(entityDefinition.getGetGenerated())) {
            Integer c = k.insertIdentity(dbBuilder);
            dbBuilder.copyIdentity();
            return c;
        }
        return k.insert(dbBuilder);
    }

    private <T> T handler(DBBuilder dbBuilder, Call call, String op) {
        CacheCall cacheCall = dbBuilder.getCacheCall();
        if (cacheCall != null) {
            String key = op + "_" + cacheCall.key();
            Object obj = cacheCall.get(key);
            if (obj == null) {
                obj = call.call();
                cacheCall.set(key, obj);
            }
            return (T) obj;
        }
        return call.call();
    }


    public Integer count(final DBBuilder dbBuilder) {
        final MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return handler(dbBuilder, new Call() {
            @Override
            public <T> T call() {
                return (T) k.count(dbBuilder);
            }
        }, "count");
    }


    public List<T> list(final DBBuilder dbBuilder) {
        final MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return handler(dbBuilder, new Call() {
            @Override
            public <T> T call() {
                return (T) k.list(dbBuilder);
            }
        }, "list");
    }

    public List<T> listAll(final DBBuilder dbBuilder) {
        final MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return handler(dbBuilder, new Call() {
            @Override
            public <T> T call() {
                return (T) k.listAll(dbBuilder);
            }
        }, "listAll");
    }

    public T one(final DBBuilder dbBuilder) {
        final MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return handler(dbBuilder, new Call() {
            @Override
            public <T> T call() {
                return (T) k.one(dbBuilder);
            }
        }, "one");
    }

    @Override
    public BigDecimal sum(final DBBuilder dbBuilder) {
        final MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return handler(dbBuilder, new Call() {
            @Override
            public <T> T call() {
                return (T) k.sum(dbBuilder);
            }
        }, "sum");
    }

    public PageData page(DBBuilder dbBuilder) {
        PageData pageData = new PageData();
        pageData.setCount(count(dbBuilder));
        pageData.setList(list(dbBuilder));
        pageData.setPageNumber(dbBuilder.getPage());
        pageData.setPageSize(dbBuilder.getSize());
        return pageData;
    }

    @Override
    public Integer delete(DBBuilder dbBuilder) {
        MysqlMapper k = (MysqlMapper) getSqlSession().getMapper(mapperClass());
        return k.delete(dbBuilder);
    }
}
