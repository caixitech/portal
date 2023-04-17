package org.helloframework.mybatis.query;

import java.util.Collection;

/**
 * Author lanjian
 * Email  lanjian
 */
public interface IEntityCriteria<T> {

    T between(String p, Object lo, Object hi);

    T notBetween(String p, Object lo, Object hi);

    T in(String p, Object... values);

    T in(String p, boolean ignore, Object... values);

    T notIn(String p, Object... values);

    T notIn(String p, boolean ignore, Object... values);

    T in(String p, Collection values);

    T in(String p, boolean ignore, Collection values);

    T notIn(String p, Collection values);

    T notIn(String p, boolean ignore, Collection values);

    T asc(String p);

    T desc(String p);

    T isNull(String p);

    T isNotNull(String p);

    T isEmpty(String p);

    T isNotEmpty(String p);

    T likeEnd(String p, Object value);

    T likeStart(String p, Object value);

    T like(String p, Object value);

    T lt(String p, Object value);

    T le(String p, Object value);

    T ge(String p, Object value);

    T gt(String p, Object value);

    T eq(String p, Object value);

    T ne(String p, Object value);

}

