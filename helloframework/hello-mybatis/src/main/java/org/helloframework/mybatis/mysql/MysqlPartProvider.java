package org.helloframework.mybatis.mysql;

import org.apache.commons.lang3.StringUtils;
import org.helloframework.core.utils.ShortUtils;
import org.helloframework.mybatis.definition.*;
import org.helloframework.mybatis.query.builder.DBBuilder;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by lanjian on 31/08/2017.
 */
public class MysqlPartProvider {
    public static String table(EntityDefinition entityDefinition) {
        if (StringUtils.isBlank(entityDefinition.getTableAs())) {
            return entityDefinition.getTable();
        }
        return entityDefinition.getTable() + " as " + entityDefinition.getTableAs();
    }

    public static String joinTable(EntityDefinition entityDefinition) {
        if (StringUtils.isBlank(entityDefinition.getJoinTable())) {
            throw new RuntimeException("join table is null");
        }
        return entityDefinition.getJoinTable() + " as " + entityDefinition.getJoinTableAs();
    }

    public static String on(EntityDefinition entityDefinition) {
        if (StringUtils.isBlank(entityDefinition.getTableOn()) || StringUtils.isBlank(entityDefinition.getJoinTableOn())) {
            throw new RuntimeException("on must not null");
        }
        return entityDefinition.getTableAs() + "." + entityDefinition.getTableOn() + "=" + entityDefinition.getJoinTableAs() + "." + entityDefinition.getJoinTableOn();
    }

    public static String column(EntityColumnDefinition columnDefinition) {
        if (StringUtils.isBlank(columnDefinition.getTableAs())) {
            return String.format("`%s` as `%s`", columnDefinition.getColumn(), columnDefinition.getJavaColumn());
        } else {
            return String.format("%s.`%s`  as  `%s`", columnDefinition.getTableAs(), columnDefinition.getColumn(), columnDefinition.getJavaColumn());
        }
    }

    public static String columnNoAs(EntityColumnDefinition columnDefinition) {
        if (StringUtils.isBlank(columnDefinition.getTableAs())) {
            return String.format("`%s`", columnDefinition.getColumn());
        } else {
            return String.format("%s.`%s`", columnDefinition.getTableAs(), columnDefinition.getColumn());
        }
    }


    private static void inSql(StringBuffer stringBuffer, DBBuilder dbBuilder, Query query) {
        Collection list = (Collection) query.getValue();
        stringBuffer.append("(");
        Iterator iterator = list.iterator();
        if (iterator.hasNext()) {
            int i = 0;
            Object first = iterator.next();
            String valueAt = query.getValueAt() + ShortUtils.to62RadixString(1000 + i);
            stringBuffer.append("#{").append(valueAt).append("}");
            dbBuilder.put(valueAt, first);
            while (iterator.hasNext()) {
                i++;
                stringBuffer.append(",");
                Object o = iterator.next();
                valueAt = query.getValueAt() + ShortUtils.to62RadixString(1000 + i);
                stringBuffer.append("#{").append(valueAt).append("}");
                dbBuilder.put(valueAt, o);
            }
        }
        stringBuffer.append(")");
    }

    public static String whereIn(Query query, DBBuilder dbBuilder, EntityColumnDefinition columnDefinition) {
        StringBuffer stringBuffer = new StringBuffer(columnNoAs(columnDefinition) + query.getWhere().getDesc());
        inSql(stringBuffer, dbBuilder, query);
        return stringBuffer.toString();
    }

    public static String whereBetween(Query query, DBBuilder dbBuilder, EntityColumnDefinition columnDefinition) {
        Object value = query.getValue();
        if (value instanceof Between) {
            Between bV = (Between) value;
            String sKey = query.getValueAt() + ShortUtils.to62RadixString(1000);
            String eKey = query.getValueAt() + ShortUtils.to62RadixString(1001);
            StringBuffer stringBuffer = new StringBuffer("(");
            stringBuffer.append(columnNoAs(columnDefinition));
            stringBuffer.append(query.getWhere().getDesc());
            stringBuffer.append("#{");
            stringBuffer.append(sKey);
            stringBuffer.append("}");
            stringBuffer.append(" AND ");
            stringBuffer.append("#{");
            stringBuffer.append(eKey);
            stringBuffer.append("}");
            stringBuffer.append(")");
            dbBuilder.put(sKey, bV.getS());
            dbBuilder.put(eKey, bV.getE());
            return stringBuffer.toString();
        } else {
            throw new RuntimeException("this query not between");
        }
    }

    public static String where(Query query, DBBuilder dbBuilder, EntityColumnDefinition columnDefinition) {
        //当做map处理
        dbBuilder.put(query.getValueAt(), query.getValue());
        return columnNoAs(columnDefinition) + query.getWhere().getDesc() + values(query.getValueAt(), columnDefinition);
    }

    public static String whereSingle(Query query, EntityColumnDefinition columnDefinition) {
        return columnNoAs(columnDefinition) + query.getWhere().getDesc();
    }

    public static String set(String valueAt, EntityColumnDefinition columnDefinition) {
        return columnDefinition.getColumn() + "=" + values(valueAt, columnDefinition);
    }

    public static String values(String valueAt, EntityColumnDefinition columnDefinition) {
        return String.format("#{%s,javaType= %s}", valueAt, columnDefinition.getType().getName());

    }

    public static String orderBy(Order order, EntityColumnDefinition columnDefinition) {
        return columnNoAs(columnDefinition) + " " + order.getOrderBy().name();
    }


}
