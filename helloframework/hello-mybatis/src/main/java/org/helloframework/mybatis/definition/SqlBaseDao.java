package org.helloframework.mybatis.definition;


import org.helloframework.core.dto.PageData;
import org.helloframework.mybatis.query.builder.DBBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public interface SqlBaseDao<T> {

    Integer update(DBBuilder dbBuilder);

    Integer insert(DBBuilder dbBuilder);

    Integer delete(DBBuilder dbBuilder);

    Integer count(DBBuilder dbBuilder);

    List<T> list(DBBuilder dbBuilder);

    List<T> listAll(DBBuilder dbBuilder);

    T one(DBBuilder dbBuilder);

    BigDecimal sum(DBBuilder dbBuilder);

    PageData page(DBBuilder dbBuilder);

}

