package org.helloframework.codegen.definition;

import java.util.List;

/**
 * Created by macintosh
 */
public class TableInfo {
    private String tableName;
    private String pojoName;
    private List<ColumnInfo> columnInfos;

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", columnInfos=" + columnInfos +
                '}';
    }
}
