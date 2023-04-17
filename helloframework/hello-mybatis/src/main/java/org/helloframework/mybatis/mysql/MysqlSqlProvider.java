package org.helloframework.mybatis.mysql;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.helloframework.mybatis.definition.*;
import org.helloframework.mybatis.exception.SQLProviderException;
import org.helloframework.mybatis.plugin.AesUtils;
import org.helloframework.mybatis.query.builder.BaseQuery;
import org.helloframework.mybatis.query.builder.DBBuilder;
import org.helloframework.mybatis.sql.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Author lanjian
 * Email  lanjian
 */
public class MysqlSqlProvider {

    private final static Logger logger = LoggerFactory.getLogger(MysqlSqlProvider.class);

    protected final static Map<Class, String> typeMappings = new HashMap();

    static {
        //TODO 后续补充
        typeMappings.put(Integer.class, "INT|(11)");
        typeMappings.put(Long.class, "BIGINT|(11)");
        typeMappings.put(Float.class, "FLOAT|(10,2)");
        typeMappings.put(String.class, "VARCHAR|(128)");
        typeMappings.put(Double.class, "DOUBLE|(10,2)");
        typeMappings.put(Boolean.class, "TINYINT|(1)");
    }


    private void orderBySql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        List<Order> orders = dbBuilder.getOrders();
        if (orders == null || orders.isEmpty()) {
            return;
        }
//        Collections.reverse(orders);
        for (Order order : orders) {
            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(order.getPropertyName());
            if (entityColumnDefinition == null) {
                throw new SQLProviderException("this PropertyName " + order.getPropertyName() + " not register");
            }
            sql.ORDER_BY(MysqlPartProvider.orderBy(order, entityColumnDefinition));
        }
    }

    private void whereSql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        List<Query> querySet = dbBuilder.getQueries();
        if (querySet == null || querySet.isEmpty()) {
            return;
        }
        for (Query query : querySet) {
            if (Where.clause.contains(query.getWhere())) {// 先判断是否为and 或者 or，该两项的query没有property
                if (Where.AND.equals(query.getWhere())) {
                    sql.AND();
                }
                if (Where.OR.equals(query.getWhere())) {
                    sql.OR();
                }
                if (Where.INOR.equals(query.getWhere())) {
                    sql.INOR();
                }
            } else {
                EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(query.getProperty());
                if (entityColumnDefinition == null) {
                    throw new SQLProviderException("this PropertyName " + query.getProperty() + " not register");
                }
                if (entityColumnDefinition.isAes() && AesUtils.isAesStr(query.getValue())) {
                    query.setValue(AesUtils.encrypt((String) query.getValue()));
                }
                if (Where.kv.contains(query.getWhere())) {
                    sql.WHERE(MysqlPartProvider.where(query, dbBuilder, entityColumnDefinition));
                }
                if (Where.single.contains(query.getWhere())) {
                    sql.WHERE(MysqlPartProvider.whereSingle(query, entityColumnDefinition));
                }
                if (Where.in.contains(query.getWhere())) {
                    sql.WHERE(MysqlPartProvider.whereIn(query, dbBuilder, entityColumnDefinition));
                }
                if (Where.twoV.contains(query.getWhere())) {
                    sql.WHERE(MysqlPartProvider.whereBetween(query, dbBuilder, entityColumnDefinition));
                }
            }
        }
    }

    private void fromSql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (entityDefinition == null) {
            throw new SQLProviderException("entityDefinition not register");
        }
        sql.FROM(MysqlPartProvider.table(entityDefinition));
        if (JoinType.inner.equals(entityDefinition.getJoinType())) {
            sql.INNER_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        } else if (JoinType.left.equals(entityDefinition.getJoinType())) {
            sql.LEFT_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        } else if (JoinType.right.equals(entityDefinition.getJoinType())) {
            sql.RIGHT_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        } else if (JoinType.outer.equals(entityDefinition.getJoinType())) {
            sql.OUTER_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        } else if (JoinType.right_outer.equals(entityDefinition.getJoinType())) {
            sql.RIGHT_OUTER_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        } else if (JoinType.left_outer.equals(entityDefinition.getJoinType())) {
            sql.LEFT_OUTER_JOIN(MysqlPartProvider.joinTable(entityDefinition));
            sql.ON(MysqlPartProvider.on(entityDefinition));
        }

    }

    private void columnSql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        Map<String, EntityColumnDefinition> columnDefinitionMap = entityDefinition.getColumnDefinitions();
        if (columnDefinitionMap == null || columnDefinitionMap.isEmpty()) {
            throw new SQLProviderException("columns is null");
        }
        if (CollectionUtils.isNotEmpty(dbBuilder.getSelect())) {
            List<String> select = dbBuilder.getSelect();
            for (String column : select) {
                EntityColumnDefinition columnDefinition = columnDefinitionMap.get(column);
                if (columnDefinition == null) {
                    throw new SQLProviderException("this java PropertyName " + column + "not register");
                }
                if (columnDefinition.isTransfer()) {
                    sql.SELECT(MysqlPartProvider.column(columnDefinition));
                }
            }
        } else {
            Collection<EntityColumnDefinition> columns = columnDefinitionMap.values();
            for (EntityColumnDefinition column : columns) {
                if (column.isTransfer()) {
                    sql.SELECT(MysqlPartProvider.column(column));
                }
            }
        }
    }

    private void setSql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        List<Update> updates = dbBuilder.getUpdates();
        if (updates == null || updates.isEmpty()) {
            throw new SQLProviderException("no data set");
        }
        for (Update update : updates) {
            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(update.getProperty());
            if (entityColumnDefinition == null) {
                throw new SQLProviderException("this java PropertyName " + update.getProperty() + "not register");
            }
            if (!entityColumnDefinition.isTransfer()) {//忽略字段
                continue;
            }
            if (entityColumnDefinition.isAes() && AesUtils.isAesStr(update.getValue())) {
                update.setValue(AesUtils.encrypt((String) update.getValue()));
            }
            dbBuilder.put(update.getValueAt(), update.getValue());
            sql.SET(MysqlPartProvider.set(update.getValueAt(), entityColumnDefinition));
        }
    }


    private void insertSql(DBBuilder dbBuilder, SQL sql) throws Exception {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        List<Update> updates = dbBuilder.getUpdates();
        if (updates == null || updates.isEmpty()) {
            throw new SQLProviderException("no data set");
        }

        //处理id问题
        if (GenerationType.UUID.equals(entityDefinition.getGetGenerated())) {
            //uuid
            String uuid = UUID.randomUUID().toString();
            PropertyUtils.setProperty(dbBuilder.getInsertTarget(), entityDefinition.getIdJavaColumn(), uuid);
            dbBuilder.put(uuid, uuid);
            sql.VALUES(entityDefinition.getIdColumn(), String.format("#{%s,javaType=java.lang.String}", uuid));
        }
        if (GenerationType.IDENTITY.equals(entityDefinition.getGetGenerated())) {
            //自增在copy实现
        }


        for (Update update : updates) {
            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(update.getProperty());
            if (entityColumnDefinition == null) {
                throw new SQLProviderException("this java PropertyName " + update.getProperty() + " not register");
            }

            if (!entityColumnDefinition.isTransfer()) {//忽略字段
                continue;
            }
            if (!entityColumnDefinition.getId()) {
                if (entityColumnDefinition.isAes() && AesUtils.isAesStr(update.getValue())) {
                    dbBuilder.put(update.getValueAt(), AesUtils.encrypt((String) update.getValue()));
                } else {
                    dbBuilder.put(update.getValueAt(), update.getValue());
                }
                sql.VALUES(entityColumnDefinition.getColumn(), MysqlPartProvider.values(update.getValueAt(), entityColumnDefinition));
            }
        }
    }

    /********************
     * sql gen
     ****************/

    public SQL selectBaseSql(DBBuilder dbBuilder) throws Exception {
        SQL sql = new SQL();
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (StringUtils.isNotBlank(entityDefinition.getSql())) {
            columnSql(dbBuilder, sql);
            sql.SQL(entityDefinition.getSql());
        } else {
            columnSql(dbBuilder, sql);
            fromSql(dbBuilder, sql);
        }
        whereSql(dbBuilder, sql);
        orderBySql(dbBuilder, sql);
        return sql;
    }

    public String list(DBBuilder dbBuilder) {
        try {
            SQL sql = selectBaseSql(dbBuilder);
            String sqlStr = sql.toString() + " LIMIT " + dbBuilder.begin() + "," + dbBuilder.getSize();
            logger.debug("SQLCURDTemple.list.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("list", ex);
            throw new SQLProviderException(ex);
        }
    }

    public String listAll(DBBuilder dbBuilder) {
        try {
            SQL sql = selectBaseSql(dbBuilder);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.list.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("listAll", ex);
            throw new SQLProviderException(ex);
        }
    }

    public String one(DBBuilder dbBuilder) {
        try {
            SQL sql = selectBaseSql(dbBuilder);
            String sqlStr = sql.toString();
            sqlStr += " LIMIT 1";
            logger.debug("SQLCURDTemple.one.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("one", ex);
            throw new SQLProviderException(ex);
        }
    }

    public String sum(DBBuilder dbBuilder) {
        try {
            EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
            if (StringUtils.isNotBlank(entityDefinition.getSql())) {
                throw new SQLProviderException("not support sum");
            }
            String sp = dbBuilder.getSum();
            if (StringUtils.isBlank(sp)) {
                throw new SQLProviderException("sum is null");
            }
            SQL sql = new SQL();
            if (StringUtils.isNotBlank(entityDefinition.getSql())) {
                sql.SELECT("SUM(" + sp + ")");
                sql.SQL(entityDefinition.getSql());
            } else {
                sql.SELECT("SUM(" + sp + ")");
                fromSql(dbBuilder, sql);
            }
            whereSql(dbBuilder, sql);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.count.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("sum", ex);
            throw new SQLProviderException(ex);
        }
    }

    public String count(DBBuilder dbBuilder) {
        try {
            SQL sql = new SQL();
            EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
            if (StringUtils.isNotBlank(entityDefinition.getSql())) {
                sql.SELECT("COUNT(1)");
                sql.SQL(entityDefinition.getSql());
            } else {
                sql.SELECT("COUNT(1)");
                fromSql(dbBuilder, sql);
            }
            whereSql(dbBuilder, sql);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.count.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("count", ex);
            throw new SQLProviderException(ex);
        }
    }


    public String delete(DBBuilder dbBuilder) {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (entityDefinition.isJoin()) {
            throw new SQLProviderException("join entityDefinition not support delete");
        }
        try {
            SQL sql = new SQL();
            sql.DELETE_FROM(entityDefinition.getTable());
            whereSql(dbBuilder, sql);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.delete.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("delete", ex);
            throw new SQLProviderException(ex);
        }

    }

    public String update(DBBuilder dbBuilder) {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (entityDefinition.isJoin()) {
            throw new SQLProviderException("join entityDefinition not support update");
        }
        try {
            SQL sql = new SQL();
            sql.UPDATE(entityDefinition.getTable());
            setSql(dbBuilder, sql);
            whereSql(dbBuilder, sql);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.update.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("update", ex);
            throw new SQLProviderException(ex);
        }
    }

    /**
     * <insert id="add" parameterType="vo.Category">
     * <selectKey resultType="Java.lang.Short" order="BEFORE" keyProperty="id">
     * SELECT SEQ_TEST.NEXTVAL FROM DUAL
     * </selectKey>
     * insert into category (name_zh, parent_id,
     * <p>
     * show_order, delete_status, description
     * )
     * values (#{nameZh,jdbcType=VARCHAR},
     * #{parentId,jdbcType=SMALLINT},
     * #{showOrder,jdbcType=SMALLINT},
     * #{deleteStatus,jdbcType=BIT},
     * #{description,jdbcType=VARCHAR}
     * )
     * </insert>
     *
     * @param dbBuilder
     * @return
     */


    public String insert(DBBuilder dbBuilder) {
        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
        if (entityDefinition.isJoin()) {
            throw new SQLProviderException("join entityDefinition not support insert");
        }
        try {
            SQL sql = new SQL();
            sql.INSERT_INTO(entityDefinition.getTable());
            insertSql(dbBuilder, sql);
            String sqlStr = sql.toString();
            logger.debug("SQLCURDTemple.insert.sql:{}", sqlStr);
            return sqlStr;
        } catch (Exception ex) {
            logger.error("insert", ex);
            throw new SQLProviderException(ex);
        }
    }

    /**
     * CREATE TABLE `user_info` (
     * `id` varchar(30) NOT NULL DEFAULT '',
     * `create_at` double DEFAULT NULL,
     * `update_at` int(11) DEFAULT NULL,
     * `poster` varchar(50) DEFAULT NULL,
     * `nickname` varchar(40) DEFAULT NULL,
     * `email` varchar(40) DEFAULT NULL,
     * `phone` varchar(100) DEFAULT NULL,
     * `password` varchar(50) DEFAULT NULL,
     * `signature` varchar(50) DEFAULT NULL,
     * `birthday` bigint(11) DEFAULT NULL,
     * `address` varchar(11) DEFAULT NULL,
     * `city` varchar(11) DEFAULT NULL,
     * `channel` varchar(11) DEFAULT NULL,
     * `invite_code` varchar(11) DEFAULT NULL,
     * `credit` int(11) DEFAULT NULL,
     * `coins` int(11) DEFAULT NULL,
     * `ip` varchar(11) DEFAULT NULL,
     * `os` varchar(11) DEFAULT NULL,
     * `client_version` varchar(11) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     *
     * @param clazz
     * @return
     */

    public static String createTable(Class clazz) {
        try {
            EntityDefinition entityDefinition = BaseQuery.resolve(clazz);
            String table = entityDefinition.getTable();
            StringBuffer stringBuffer = new StringBuffer("DROP TABLE IF EXISTS `");
            stringBuffer.append(table);
            stringBuffer.append("`; \nCREATE TABLE ");
            stringBuffer.append("`");
            stringBuffer.append(table);
            stringBuffer.append("`");
            stringBuffer.append(" ( \n");

            for (EntityColumnDefinition entityColumnDefinition : entityDefinition.getColumnDefinitions().values()) {
                stringBuffer.append("   `");
                stringBuffer.append(entityColumnDefinition.getColumn());
                stringBuffer.append("`");
                stringBuffer.append(" ");
                String join = typeMappings.get(entityColumnDefinition.getType());
                String joins[] = StringUtils.split(join, "|");
                stringBuffer.append(joins[0]);
                if (StringUtils.isBlank(entityColumnDefinition.getLength())) {
                    stringBuffer.append(joins[1]);
                } else {
                    stringBuffer.append("(");
                    stringBuffer.append(entityColumnDefinition.getLength());
                    stringBuffer.append(")");
                }
                stringBuffer.append(" ");
                if (entityColumnDefinition.getId()) {
                    stringBuffer.append("NOT NULL DEFAULT ''");
                } else {
                    stringBuffer.append("DEFAULT NULL");
                }
                if (StringUtils.isNotBlank(entityColumnDefinition.getDesc())) {
                    stringBuffer.append(" COMMENT ");
                    stringBuffer.append("'");
                    stringBuffer.append(entityColumnDefinition.getDesc());
                    stringBuffer.append("'");
                }
                stringBuffer.append(",\n");
            }
            stringBuffer.append("   PRIMARY KEY (`" + entityDefinition.getIdColumn() + "`)\n)ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n");
            return stringBuffer.toString();
        } catch (Exception ex) {
            if (logger.isInfoEnabled()) {
                logger.error("createTable", ex);
            }
            throw new SQLProviderException(ex);
        }
    }
}
