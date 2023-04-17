package org.helloframework.mybatis.definition;

/**
 * Author lanjian
 * Email  lanjian
 */
public class Restrictions {


    public static Query eq(String p, Object v, String vat) {
        return new Query(v, p, vat, Where.EQ);
    }

    public static Query gt(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.GT);
    }

    public static Query ge(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.GE);
    }

    public static Query le(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.LE);
    }

    public static Query lt(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.LT);
    }

    public static Query like(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.LIKE);
    }

    public static Query ne(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.NE);
    }

    public static Query notEmpty(String p, String vat) {
        return new Query(null, p, vat, Where.NOTEMPTY);
    }

    public static Query isEmpty(String p, String vat) {
        return new Query(null, p, vat, Where.ISEMPTY);
    }

    public static Query isNull(String p, String vat) {
        return new Query(null, p, vat, Where.ISNULL);
    }

    public static Query notNull(String p, String vat) {
        return new Query(null, p, vat, Where.NOTNULL);
    }

    public static Query notIn(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.NOTIN);
    }

    public static Query in(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.IN);
    }

    public static Query notBetween(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.NOTBETWEEN);
    }

    public static Query between(String p, Object value, String vat) {
        return new Query(value, p, vat, Where.BETWEEN);
    }
}
