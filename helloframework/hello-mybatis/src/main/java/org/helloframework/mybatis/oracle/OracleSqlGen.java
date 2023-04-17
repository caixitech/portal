package org.helloframework.mybatis.oracle;//package org.helloframework.mybatis.oracle;
//
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.helloframework.mybatis.definition.*;
//import org.helloframework.mybatis.mysql.MYSQL;
//import org.helloframework.mybatis.query.builder.BaseQuery;
//import org.helloframework.mybatis.query.builder.DBBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Author lanjian
// * Email  lanjian
// */
//public class OracleSqlGen {
//
//    private final static Logger logger = LoggerFactory.getLogger(OracleSqlGen.class);
//
//    protected final static Map<Class, String> typeMappings = new HashMap();
//
//    static {
//        typeMappings.put(Integer.class, "INT|(11)");
//        typeMappings.put(Long.class, "BIGINT|(11)");
//        typeMappings.put(Float.class, "FLOAT|(10,2)");
//        typeMappings.put(String.class, "VARCHAR|(128)");
//        typeMappings.put(Double.class, "DOUBLE|(10,2)");
//        typeMappings.put(Boolean.class, "TINYINT|(1)");
//    }
//
//
//    private void orderBy(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        List<Order> orders = dbBuilder.getOrders();
//        if (orders == null || orders.isEmpty()) {
//            return;
//        }
////        Collections.reverse(orders);
//        for (Order order : orders) {
//            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(order.getPropertyName());
//            if (entityColumnDefinition == null) {
//                throw new RuntimeException("this PropertyName " + order.getPropertyName() + " not register");
//            }
//            sql.ORDER_BY(entityColumnDefinition.orderBy(order));
//        }
//    }
//
//    private void where(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        List<Query> querySet = dbBuilder.getQueries();
//        if (querySet == null || querySet.isEmpty()) {
//            return;
//        }
//        for (Query query : querySet) {
//            if (Where.clause.contains(query.getWhere())) {// 先判断是否为and 或者 or，该两项的query没有property
//                if (Where.AND.equals(query.getWhere())) {
//                    sql.AND();
//                }
//                if (Where.OR.equals(query.getWhere())) {
//                    sql.OR();
//                }
//            } else {
//                EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(query.getProperty());
//                if (entityColumnDefinition == null) {
//                    throw new RuntimeException("this PropertyName " + query.getProperty() + " not register");
//                }
//                if (Where.kv.contains(query.getWhere())) {
//                    sql.WHERE(entityColumnDefinition.where(query, dbBuilder));
//                }
//                if (Where.single.contains(query.getWhere())) {
//                    sql.WHERE(entityColumnDefinition.whereSingle(query));
//                }
//                if (Where.in.contains(query.getWhere())) {
//                    sql.WHERE(entityColumnDefinition.whereIn(query, dbBuilder));
//                }
//                if (Where.twoV.contains(query.getWhere())) {
//                    sql.WHERE(entityColumnDefinition.whereBetween(query, dbBuilder));
//                }
//            }
//        }
//    }
//
//    private void from(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        if (entityDefinition == null) {
//            throw new RuntimeException("entityDefinition not register");
//        }
//        sql.FROM(entityDefinition.table());
//        if (JoinType.inner.equals(entityDefinition.getJoinType())) {
//            sql.INNER_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        } else if (JoinType.left.equals(entityDefinition.getJoinType())) {
//            sql.LEFT_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        } else if (JoinType.right.equals(entityDefinition.getJoinType())) {
//            sql.RIGHT_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        } else if (JoinType.outer.equals(entityDefinition.getJoinType())) {
//            sql.OUTER_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        } else if (JoinType.right_outer.equals(entityDefinition.getJoinType())) {
//            sql.RIGHT_OUTER_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        } else if (JoinType.left_outer.equals(entityDefinition.getJoinType())) {
//            sql.LEFT_OUTER_JOIN(entityDefinition.joinTable());
//            sql.ON(entityDefinition.on());
//        }
//
//    }
//
//    private void column(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        Map<String, EntityColumnDefinition> columnDefinitionMap = entityDefinition.getColumnDefinitions();
//        if (columnDefinitionMap == null || columnDefinitionMap.isEmpty()) {
//            throw new RuntimeException("columns is null");
//        }
//        if (CollectionUtils.isNotEmpty(dbBuilder.getSelect())) {
//            List<String> select = dbBuilder.getSelect();
//            for (String column : select) {
//                EntityColumnDefinition columnDefinition = columnDefinitionMap.get(column);
//                if (columnDefinition == null) {
//                    throw new RuntimeException("this java PropertyName " + column + "not register");
//                }
//                sql.SELECT(columnDefinition.column());
//            }
//        } else {
//            Collection<EntityColumnDefinition> columns = columnDefinitionMap.values();
//            for (EntityColumnDefinition column : columns) {
//                sql.SELECT(column.column());
//            }
//        }
//    }
//
//    private void set(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        List<Update> updates = dbBuilder.getUpdates();
//        if (updates == null || updates.isEmpty()) {
//            throw new RuntimeException("no data set");
//        }
//        for (Update update : updates) {
//            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(update.getProperty());
//            if (entityColumnDefinition == null) {
//                throw new RuntimeException("this java PropertyName " + update.getProperty() + "not register");
//            }
//            dbBuilder.put(update.getValueAt(), update.getValue());
//            sql.SET(entityColumnDefinition.set(update.getValueAt()));
//        }
//    }
//
//    private void insert(DBBuilder dbBuilder, MYSQL sql) throws Exception {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        List<Update> updates = dbBuilder.getUpdates();
//        if (updates == null || updates.isEmpty()) {
//            throw new RuntimeException("no data set");
//        }
//        for (Update update : updates) {
//            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(update.getProperty());
//            if (entityColumnDefinition == null) {
//                throw new RuntimeException("this java PropertyName " + update.getProperty() + " not register");
//            }
//            dbBuilder.put(update.getValueAt(), update.getValue());
//            sql.VALUES(entityColumnDefinition.getColumn(), "#{" + update.getValueAt() + "}");
//        }
//    }
//
//    /********************
//     * sql gen
//     ****************/
//
//
//    public String list(DBBuilder dbBuilder) {
//        try {
//            MYSQL sql = new MYSQL();
//            column(dbBuilder, sql);
//            from(dbBuilder, sql);
//            where(dbBuilder, sql);
//            orderBy(dbBuilder, sql);
//            String sqlStr = sql.toString() + " LIMIT " + dbBuilder.begin() + "," + dbBuilder.getSize();
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.list.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            logger.info("list", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public String one(DBBuilder dbBuilder) {
//        try {
//            MYSQL sql = new MYSQL();
//            column(dbBuilder, sql);
//            from(dbBuilder, sql);
//            where(dbBuilder, sql);
//            orderBy(dbBuilder, sql);
//            String sqlStr = sql.toString();
//            if(dbBuilder.isMustOne()){
//                sqlStr+= " LIMIT 1";
//            }
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.one.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("one", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public String sum(DBBuilder dbBuilder) {
//        try {
//            String sp = dbBuilder.getSum();
//            if (StringUtils.isBlank(sp)) {
//                throw new RuntimeException("sum is null");
//            }
//            MYSQL sql = new MYSQL();
//            sql.SELECT("SUM(" + sp + ")");
//            from(dbBuilder, sql);
//            where(dbBuilder, sql);
//            String sqlStr = sql.toString();
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.count.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("sum", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public String count(DBBuilder dbBuilder) {
//        try {
//            MYSQL sql = new MYSQL();
//            sql.SELECT("COUNT(1)");
//            from(dbBuilder, sql);
//            where(dbBuilder, sql);
//            String sqlStr = sql.toString();
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.count.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            logger.info("count", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public String delete(DBBuilder dbBuilder) {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        if (entityDefinition.isJoin()) {
//            throw new RuntimeException("join entityDefinition not support delete");
//        }
//        try {
//            MYSQL sql = new MYSQL();
//            sql.DELETE_FROM(entityDefinition.getTable());
//            where(dbBuilder, sql);
//            return sql.toString();
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("delete", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//
//    }
//
//    public String update(DBBuilder dbBuilder) {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        if (entityDefinition.isJoin()) {
//            throw new RuntimeException("join entityDefinition not support update");
//        }
//        try {
//            MYSQL sql = new MYSQL();
//            sql.UPDATE(entityDefinition.getTable());
//            set(dbBuilder, sql);
//            where(dbBuilder, sql);
//            String sqlStr = sql.toString();
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.update.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("update", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public String insert(DBBuilder dbBuilder) {
//        EntityDefinition entityDefinition = dbBuilder.findEntityDefinition();
//        if (entityDefinition.isJoin()) {
//            throw new RuntimeException("join entityDefinition not support insert");
//        }
//        try {
//            MYSQL sql = new MYSQL();
//            sql.INSERT_INTO(entityDefinition.getTable());
//            insert(dbBuilder, sql);
//            String sqlStr = sql.toString();
//            if (logger.isInfoEnabled()) {
//                logger.info("MysqlCURDTemple.insert.sql:" + sqlStr);
//            }
//            return sqlStr;
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("insert", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//    }
//
//    /**
//     * CREATE TABLE `user_info` (
//     * `id` varchar(30) NOT NULL DEFAULT '',
//     * `create_at` double DEFAULT NULL,
//     * `update_at` int(11) DEFAULT NULL,
//     * `poster` varchar(50) DEFAULT NULL,
//     * `nickname` varchar(40) DEFAULT NULL,
//     * `email` varchar(40) DEFAULT NULL,
//     * `phone` varchar(100) DEFAULT NULL,
//     * `password` varchar(50) DEFAULT NULL,
//     * `signature` varchar(50) DEFAULT NULL,
//     * `birthday` bigint(11) DEFAULT NULL,
//     * `address` varchar(11) DEFAULT NULL,
//     * `city` varchar(11) DEFAULT NULL,
//     * `channel` varchar(11) DEFAULT NULL,
//     * `invite_code` varchar(11) DEFAULT NULL,
//     * `credit` int(11) DEFAULT NULL,
//     * `coins` int(11) DEFAULT NULL,
//     * `ip` varchar(11) DEFAULT NULL,
//     * `os` varchar(11) DEFAULT NULL,
//     * `client_version` varchar(11) DEFAULT NULL,
//     * PRIMARY KEY (`id`)
//     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
//     *
//     * @param clazz
//     * @return
//     */
//
//    public static String createTable(Class clazz) {
//        try {
//            EntityDefinition entityDefinition = BaseQuery.resolve(clazz);
//            String table = entityDefinition.getTable();
//            StringBuffer stringBuffer = new StringBuffer("DROP TABLE IF EXISTS `");
//            stringBuffer.append(table);
//            stringBuffer.append("`; \nCREATE TABLE ");
//            stringBuffer.append("`");
//            stringBuffer.append(table);
//            stringBuffer.append("`");
//            stringBuffer.append(" ( \n");
//
//            for (EntityColumnDefinition entityColumnDefinition : entityDefinition.getColumnDefinitions().values()) {
//                stringBuffer.append("   `");
//                stringBuffer.append(entityColumnDefinition.getColumn());
//                stringBuffer.append("`");
//                stringBuffer.append(" ");
//                String join=typeMappings.get(entityColumnDefinition.getType());
//                String joins[]=StringUtils.split(join,"|");
//                stringBuffer.append(joins[0]);
//                if(StringUtils.isBlank(entityColumnDefinition.getLength())){
//                    stringBuffer.append(joins[1]);
//                }else{
//                    stringBuffer.append("(");
//                    stringBuffer.append(entityColumnDefinition.getLength());
//                    stringBuffer.append(")");
//                }
//                stringBuffer.append(" ");
//                if (entityColumnDefinition.getId()) {
//                    stringBuffer.append("NOT NULL DEFAULT ''");
//                } else {
//                    stringBuffer.append("DEFAULT NULL");
//                }
//                if (StringUtils.isNotBlank(entityColumnDefinition.getDesc())) {
//                    stringBuffer.append(" COMMENT ");
//                    stringBuffer.append("'");
//                    stringBuffer.append(entityColumnDefinition.getDesc());
//                    stringBuffer.append("'");
//                }
//                stringBuffer.append(",\n");
//            }
//            stringBuffer.append("   PRIMARY KEY (`" + entityDefinition.getIdColumn() + "`)\n)ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n");
//            return stringBuffer.toString();
//        } catch (Exception ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("list", ex);
//            }
//            throw new RuntimeException(ex);
//        }
//    }
//}
