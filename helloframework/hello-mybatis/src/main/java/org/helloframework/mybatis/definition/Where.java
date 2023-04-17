package org.helloframework.mybatis.definition;


import org.helloframework.mybatis.exception.QueryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public enum Where {
    EQ(" = "), GT(" > "), GE(" >= "), LT(" < "), LE(" <= "), LIKE(" LIKE "), NE(" <> "), NOTIN(" NOT IN "), IN(" IN "), BETWEEN(" BETWEEN "), NOTBETWEEN(" NOT BETWEEN "), NOTNULL(" IS NOT NULL "), ISNULL(" IS NULL "), NOTEMPTY(" IS NOT EMPTY "), ISEMPTY("IS EMPTY"), AND(" AND "),
    OR(" OR "), INOR(" OR ");

    /**
     * OBC专用
     *
     * @param p
     * @param value
     * @return
     */

    public final static List<Where> kv = new ArrayList<Where>();
    public final static List<Where> single = new ArrayList<Where>();
    public final static List<Where> twoV = new ArrayList<Where>();
    public final static List<Where> in = new ArrayList<Where>();
    public final static List<Where> clause = new ArrayList<Where>();

    static {
        kv.add(Where.EQ);
        kv.add(Where.GT);
        kv.add(Where.GE);
        kv.add(Where.LT);
        kv.add(Where.LE);
        kv.add(Where.LIKE);
        kv.add(Where.NE);
        in.add(Where.NOTIN);
        in.add(Where.IN);
        twoV.add(Where.BETWEEN);
        twoV.add(Where.NOTBETWEEN);
        single.add(Where.NOTNULL);
        single.add(Where.ISNULL);
        single.add(Where.NOTEMPTY);
        single.add(Where.ISEMPTY);
        clause.add(Where.AND);
        clause.add(Where.OR);
        clause.add(Where.INOR);
    }


    public Query where(String p, String vat, Object value) {
//        if (value == null) {
//            throw new RuntimeException("value is null");
//        }
        switch (this) {
            case EQ:
                return Restrictions.eq(p, value, vat);
            case GT:
                return Restrictions.gt(p, value, vat);
            case GE:
                return Restrictions.ge(p, value, vat);
            case LT:
                return Restrictions.lt(p, value, vat);
            case LE:
                return Restrictions.le(p, value, vat);
            case NE:
                return Restrictions.ne(p, value, vat);
            case LIKE:
                return Restrictions.like(p, value, vat);
            case IN:
                return Restrictions.in(p, value, vat);
            case NOTIN:
                return Restrictions.notIn(p, value, vat);
            case BETWEEN:
                return Restrictions.between(p, value, vat);
            case NOTBETWEEN:
                return Restrictions.notBetween(p, value, vat);
            default:
                throw new QueryException("enum-where is null.pls choose one!");
        }
    }

    public Query whereProperty(String p, String vat) {
        switch (this) {
            case NOTEMPTY:
                return Restrictions.notEmpty(p, vat);
            case ISEMPTY:
                return Restrictions.isEmpty(p, vat);
            case ISNULL:
                return Restrictions.isNull(p, vat);
            case NOTNULL:
                return Restrictions.notNull(p, vat);
            default:
                throw new QueryException("enum-where is null.pls choose one!");

        }
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;

    Where(String desc) {
        this.desc = desc;
    }
}
