package org.helloframework.mybatis.oracle;//
//package org.helloframework.mybatis.oracle;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.support.SqlSessionDaoSupport;
//import org.helloframework.core.dto.PageData;
//import org.helloframework.mybatis.definition.DBSql;
//import org.helloframework.mybatis.definition.SqlBaseDao;
//import org.helloframework.mybatis.query.builder.DBBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * Author lanjian
// * Email  lanjian
// */
//public abstract class AbstractOracleDao<T> extends SqlSessionDaoSupport implements SqlBaseDao<T> {
//    @Autowired
//    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
//        super.setSqlSessionTemplate(sqlSessionTemplate);
//    }
//
//    protected abstract Class mapperClass();
//
//    public String namespace() {
//        if (mapperClass() == null) {
//            throw new RuntimeException("not impl");
//        }
//        return mapperClass().getName();
//    }
//
//    @Override
//    protected void checkDaoConfig() {
//        super.checkDaoConfig();
//        DBSql.register(this);
//    }
//
//    public Integer update(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return k.update(dbBuilder);
//    }
//
//    public Integer insert(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return k.insert(dbBuilder);
//    }
//
//    public Integer count(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return k.count(dbBuilder);
//    }
//
//    public List<T> list(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return k.list(dbBuilder);
//    }
//
//    public T one(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return (T) k.one(dbBuilder);
//    }
//
//    @Override
//    public BigDecimal sum(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return  k.sum(dbBuilder);
//    }
//
//    public PageData page(DBBuilder dbBuilder) {
//        PageData pageData = new PageData();
//        pageData.setCount(count(dbBuilder));
//        pageData.setList(list(dbBuilder));
//        pageData.setPageNumber(dbBuilder.getPage());
//        pageData.setPageSize(dbBuilder.getSize());
//        return pageData;
//    }
//
//    @Override
//    public Integer delete(DBBuilder dbBuilder) {
//        OracleMapper k = (OracleMapper) getSqlSession().getMapper(mapperClass());
//        return  k.delete(dbBuilder);
//    }
//}
