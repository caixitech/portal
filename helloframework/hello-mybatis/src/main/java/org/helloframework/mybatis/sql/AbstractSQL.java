package org.helloframework.mybatis.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public abstract class AbstractSQL<T> {

    private static final String AND = ") \nAND (";
    private static final String OR = ") \nOR (";
    private static final String INOR = " \nOR ";

    private SQLStatement sql = new SQLStatement();

    public abstract T getSelf();

    public T UPDATE(String table) {
        sql().statementType = SQLStatement.StatementType.UPDATE;
        sql().tables().add(table);
        return getSelf();
    }

    public T ON(String ons) {
        sql().ons().add(ons);
        return getSelf();
    }

    public T SET(String sets) {
        sql().sets().add(sets);
        return getSelf();
    }

    public T INSERT_INTO(String tableName) {
        sql().statementType = SQLStatement.StatementType.INSERT;
        sql().tables().add(tableName);
        return getSelf();
    }

    public T VALUES(String columns, String values) {
        sql().columns().add(columns);
        sql().values().add(values);
        return getSelf();
    }


    public T SQL(String sql) {
        sql().statementType = SQLStatement.StatementType.SQL;
        sql().sql().add(sql);
        return getSelf();
    }

    public T SELECT(String columns) {
        sql().statementType = SQLStatement.StatementType.SELECT;
        sql().select().add(columns);
        return getSelf();
    }

    public T SELECT_DISTINCT(String columns) {
        sql().distinct = true;
        SELECT(columns);
        return getSelf();
    }

    public T DELETE_FROM(String table) {
        sql().statementType = SQLStatement.StatementType.DELETE;
        sql().tables().add(table);
        return getSelf();
    }

    public T FROM(String table) {
        sql().tables().add(table);
        return getSelf();
    }

    public T JOIN(String join) {
        sql().join().add(join);
        return getSelf();
    }

    public T INNER_JOIN(String join) {
        sql().innerJoin().add(join);
        return getSelf();
    }

    public T LEFT_OUTER_JOIN(String join) {
        sql().leftOuterJoin().add(join);
        return getSelf();
    }

    public T RIGHT_OUTER_JOIN(String join) {
        sql().rightOuterJoin().add(join);
        return getSelf();
    }

    public T LEFT_JOIN(String join) {
        sql().leftJoin().add(join);
        return getSelf();
    }

    public T RIGHT_JOIN(String join) {
        sql().rightJoin().add(join);
        return getSelf();
    }

    public T OUTER_JOIN(String join) {
        sql().outerJoin().add(join);
        return getSelf();
    }

    public T WHERE(String conditions) {
        sql().where().add(conditions);
        sql().lastList(sql.where());
        return getSelf();
    }

    public T OR() {
        sql().lastList().add(OR);
        return getSelf();
    }

    public T INOR() {
        sql().lastList().add(INOR);
        return getSelf();
    }

    public T AND() {
        sql().lastList().add(AND);
        return getSelf();
    }

    public T GROUP_BY(String columns) {
        sql().groupBy().add(columns);
        return getSelf();
    }

    public T HAVING(String conditions) {
        sql().having().add(conditions);
        sql().lastList(sql.having());
        return getSelf();
    }

    public T ORDER_BY(String columns) {
        sql().orderBy().add(columns);
        return getSelf();
    }

    private SQLStatement sql() {
        return sql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sql().sql(sb);
        return sb.toString();
    }

    private static class SafeAppendable {
        private final Appendable a;
        private boolean empty = true;

        public SafeAppendable(Appendable a) {
            super();
            this.a = a;
        }

        public SafeAppendable append(CharSequence s) {
            try {
                if (empty && s.length() > 0) {
                    empty = false;
                }
                a.append(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public boolean isEmpty() {
            return empty;
        }

    }


    private static class SQLBase {

        private List<String> ons = new ArrayList();

        protected List<String> ons() {
            return ons;
        }

        private List<String> sets = new ArrayList();

        protected List<String> sets() {
            if (sets == null) {
                sets = new ArrayList();
            }
            return sets;
        }

        private List<String> select = new ArrayList();

        protected List<String> select() {
            if (select == null) {
                select = new ArrayList();
            }
            return select;
        }

        private List<String> tables = null;

        protected List<String> tables() {
            if (tables == null) {
                tables = new ArrayList();
            }
            return tables;
        }

        private List<String> sql = new ArrayList();

        protected List<String> sql() {
            return sql;
        }

        private List<String> join = new ArrayList();

        protected List<String> join() {
            return join;
        }

        private List<String> innerJoin = new ArrayList();

        protected List<String> innerJoin() {
            return innerJoin;
        }

        private List<String> outerJoin = new ArrayList();

        protected List<String> outerJoin() {
            return outerJoin;
        }

        private List<String> leftJoin = new ArrayList();

        protected List<String> leftJoin() {
            return leftJoin;
        }

        private List<String> rightJoin = new ArrayList();

        protected List<String> rightJoin() {
            return rightJoin;
        }

        private List<String> leftOuterJoin = new ArrayList();

        protected List<String> leftOuterJoin() {
            return leftOuterJoin;
        }

        private List<String> rightOuterJoin = new ArrayList();

        protected List<String> rightOuterJoin() {
            return rightOuterJoin;
        }

        private List<String> where = new ArrayList();

        protected List<String> where() {
            return where;
        }

        private List<String> having = new ArrayList();

        protected List<String> having() {
            return having;
        }

        private List<String> groupBy = new ArrayList();

        protected List<String> groupBy() {
            return groupBy;
        }

        private List<String> orderBy = new ArrayList();

        protected List<String> orderBy() {
            return orderBy;
        }

        private List<String> lastList = null;

        protected void lastList(List<String> lastList) {
            this.lastList = lastList;
        }

        protected List<String> lastList() {
            if (lastList == null) {
                throw new RuntimeException("must first where or having");
            }
            return lastList;
        }


        private List<String> columns = new ArrayList();

        protected List<String> columns() {
            return columns;
        }

        private List<String> values = new ArrayList();

        protected List<String> values() {
            return values;
        }
    }

    private static class SQLStatement extends SQLBase {
        public enum StatementType {
            DELETE, INSERT, SELECT, UPDATE, SQL;
        }

        private SQLStatement.StatementType statementType;
        private boolean distinct;

        public SQLStatement() {

        }

        private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close,
                               String conjunction) {
            if (!parts.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                if (keyword != null && !"".equals(keyword)) {
                    builder.append(keyword);
                    builder.append(" ");
                }
                builder.append(open);
                String last = "________";
                for (int i = 0, n = parts.size(); i < n; i++) {
                    String part = parts.get(i);
                    if (i > 0 && !part.equals(AND) && !part.equals(OR) && !part.equals(INOR) && !last.equals(INOR)&& !last.equals(AND) && !last.equals(OR)) {
                        builder.append(conjunction);
                    }
                    builder.append(part);
                    last = part;
                }
                builder.append(close);
            }
        }

        private String selectSQL(SafeAppendable builder) {

            if (!StatementType.SQL.equals(statementType)) {
                if (distinct) {
                    sqlClause(builder, "SELECT DISTINCT", select(), "", "", ", ");
                } else {
                    sqlClause(builder, "SELECT", select(), "", "", ", ");
                }
                sqlClause(builder, "FROM", tables(), "", "", ", ");
                sqlClause(builder, "JOIN", join(), "", "", "\nJOIN ");
                sqlClause(builder, "INNER JOIN", innerJoin(), "", "", "\nINNER JOIN ");
                sqlClause(builder, "OUTER JOIN", outerJoin(), "", "", "\nOUTER JOIN ");
                sqlClause(builder, "LEFT  JOIN", leftJoin(), "", "", "\nLEFT  JOIN ");
                sqlClause(builder, "RIGHT JOIN", rightJoin(), "", "", "\nRIGHT  JOIN ");
                sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin(), "", "", "\nLEFT OUTER JOIN ");
                sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin(), "", "", "\nRIGHT OUTER JOIN ");
                sqlClause(builder, "ON", ons(), "(", ")", " AND ");
            } else {
                sqlClause(builder, "", sql(), "", "", ",");
            }
            sqlClause(builder, "WHERE", where(), "(", ")", " AND ");
            sqlClause(builder, "GROUP BY", groupBy(), "", "", ", ");
            sqlClause(builder, "HAVING", having(), "(", ")", " AND ");
            sqlClause(builder, "ORDER BY", orderBy(), "", "", ", ");
            return builder.toString();
        }

        private String insertSQL(SafeAppendable builder) {
            sqlClause(builder, "INSERT INTO", tables(), "", "", "");
            sqlClause(builder, "", columns(), "(", ")", ", ");
            sqlClause(builder, "VALUES", values(), "(", ")", ", ");
            return builder.toString();
        }

        private String deleteSQL(SafeAppendable builder) {
            sqlClause(builder, "DELETE FROM", tables(), "", "", "");
            sqlClause(builder, "WHERE", where(), "(", ")", " AND ");
            return builder.toString();
        }

        private String updateSQL(SafeAppendable builder) {

            sqlClause(builder, "UPDATE", tables(), "", "", "");
            sqlClause(builder, "SET", sets(), "", "", ", ");
            sqlClause(builder, "WHERE", where(), "(", ")", " AND ");
            return builder.toString();
        }

        public String sql(Appendable a) {
            SafeAppendable builder = new SafeAppendable(a);
            if (statementType == null) {
                return null;
            }

            String answer;

            switch (statementType) {
                case DELETE:
                    answer = deleteSQL(builder);
                    break;

                case INSERT:
                    answer = insertSQL(builder);
                    break;

                case SELECT:
                    answer = selectSQL(builder);
                    break;

                case UPDATE:
                    answer = updateSQL(builder);
                    break;
                case SQL:
                    answer = selectSQL(builder);
                default:
                    answer = null;
            }

            return answer;
        }

    }


    public static void main(String[] a) {
//        MYSQL sql = new MYSQL();
//        sql.SQL("select * from aaa");
//        sql.SELECT("a");
//        sql.SELECT("b");
//        sql.FROM("cc");
//        sql.WHERE("aa =a").OR();
//        sql.WHERE("bb =b").OR();
//        sql.WHERE("cc =c").OR();
//        sql.WHERE("bb =b");
//        sql.WHERE("cc =c").AND().WHERE("aaa=22").OR().WHERE("aaa=a");
//        System.out.println(sql.toString());

    }
}