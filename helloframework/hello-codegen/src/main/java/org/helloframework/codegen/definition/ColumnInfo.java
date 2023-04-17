package org.helloframework.codegen.definition;

/**
 * Created by macintosh
 */
public class ColumnInfo {
    private String columnName;
    private String columnType;
    private String javaColumnName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaColumnName() {
        return javaColumnName;
    }

    public void setJavaColumnName(String javaColumnName) {
        this.javaColumnName = javaColumnName;
    }

    public ColumnInfo(String columnName, String columnType, String javaColumnName) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.javaColumnName = javaColumnName;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", javaColumnName='" + javaColumnName + '\'' +
                '}';
    }
}
