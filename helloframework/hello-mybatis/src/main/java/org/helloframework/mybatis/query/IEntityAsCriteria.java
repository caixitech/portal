package org.helloframework.mybatis.query;

import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public interface IEntityAsCriteria<T> {

    T between(String p, String pas, Object lo, Object hi);

    T notBetween(String p, String pas, Object lo, Object hi);

    T in(String p, String pas, Object... values);

    T notIn(String p, String pas, Object... values);

    T in(String p, String pas, List values);

    T notIn(String p, String pas, List values);

    T likeEnd(String p, String pas, Object value);

    T likeStart(String p, String pas, Object value);

    T like(String p, String pas, Object value);

    T lt(String p, String pas, Object value);

    T le(String p, String pas, Object value);

    T ge(String p, String pas, Object value);

    T gt(String p, String pas, Object value);

    T eq(String p, String pas, Object value);

    T ne(String p, String pas, Object value);

}

