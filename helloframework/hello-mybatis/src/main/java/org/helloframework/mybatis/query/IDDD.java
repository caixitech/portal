package org.helloframework.mybatis.query;

import org.helloframework.core.dto.PageData;
import org.helloframework.mybatis.definition.SqlBaseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lanjian on 2016/12/15.
 */
public interface IDDD {
    Integer count();

    List list();

    List list(FieldMapper fieldMapper);

    <T> T one();

    BigDecimal sum();

    PageData page();

    PageData page(FieldMapper fieldMapper);

    Integer save();

    Integer update();

    Integer delete();

    <T extends SqlBaseDao> T dao();


}
