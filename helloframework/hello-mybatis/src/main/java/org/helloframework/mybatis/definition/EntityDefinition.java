package org.helloframework.mybatis.definition;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author lanjian
 * Email  lanjian
 */
public class EntityDefinition {


    private String idColumn;

    private String idJavaColumn;

    private Class idJavaType;

    private String table;

    private String tableAs;

    private JoinType joinType;

    private String joinTable;

    private String joinTableAs;

    private String tableOn;

    private String joinTableOn;

    private String sql;

    private boolean join = false;

    public GenerationType getGenerated;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
        this.join = StringUtils.isNotBlank(sql);
    }

    public GenerationType getGetGenerated() {
        return getGenerated;
    }

    public void setGetGenerated(GenerationType getGenerated) {
        this.getGenerated = getGenerated;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getIdJavaColumn() {
        return idJavaColumn;
    }

    public void setIdJavaColumn(String idJavaColumn) {
        this.idJavaColumn = idJavaColumn;
    }

    public Class getIdJavaType() {
        return idJavaType;
    }

    public void setIdJavaType(Class idJavaType) {
        this.idJavaType = idJavaType;
    }

    public String getTableAs() {
        return tableAs;
    }

    public void setTableAs(String tableAs) {
        this.tableAs = tableAs;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
        this.join = StringUtils.isNotBlank(joinTable);
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public String getJoinTableAs() {
        return joinTableAs;
    }

    public void setJoinTableAs(String joinTableAs) {
        this.joinTableAs = joinTableAs;
    }

    public String getTableOn() {
        return tableOn;
    }

    public void setTableOn(String tableOn) {
        this.tableOn = tableOn;
    }

    public String getJoinTableOn() {
        return joinTableOn;
    }

    public void setJoinTableOn(String joinTableOn) {
        this.joinTableOn = joinTableOn;
    }

    private Map<String, EntityColumnDefinition> columnDefinitions = new HashMap<String, EntityColumnDefinition>();

    public void addEntityColumnDefinition(String p, EntityColumnDefinition entityColumnDefinition) {
        columnDefinitions.put(p, entityColumnDefinition);
    }

    public Map<String, EntityColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(Map<String, EntityColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
