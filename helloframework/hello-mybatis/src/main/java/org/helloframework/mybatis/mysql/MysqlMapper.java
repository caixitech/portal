package org.helloframework.mybatis.mysql;

import org.apache.ibatis.annotations.*;
import org.helloframework.mybatis.query.builder.DBBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public interface MysqlMapper<T> {

    @SelectProvider(type = MysqlSqlProvider.class, method = "one")
    T one(DBBuilder dbBuilder);

    @SelectProvider(type = MysqlSqlProvider.class, method = "list")
    List<T> list(DBBuilder dbBuilder);

    @SelectProvider(type = MysqlSqlProvider.class, method = "listAll")
    List<T> listAll(DBBuilder dbBuilder);

    @SelectProvider(type = MysqlSqlProvider.class, method = "count")
    Integer count(DBBuilder dbBuilder);

    @SelectProvider(type = MysqlSqlProvider.class, method = "sum")
    BigDecimal sum(DBBuilder dbBuilder);

    @UpdateProvider(type = MysqlSqlProvider.class, method = "update")
    Integer update(DBBuilder dbBuilder);

    @Options(useGeneratedKeys = true, keyProperty = DBBuilder.identity_key)
    @InsertProvider(type = MysqlSqlProvider.class, method = "insert")
    Integer insertIdentity(DBBuilder dbBuilder);

    //@Options(useGeneratedKeys = true, keyProperty = DBBuilder.identity_key)
    @InsertProvider(type = MysqlSqlProvider.class, method = "insert")
    Integer insert(DBBuilder dbBuilder);

    @DeleteProvider(type = MysqlSqlProvider.class, method = "delete")
    Integer delete(DBBuilder dbBuilder);

}
