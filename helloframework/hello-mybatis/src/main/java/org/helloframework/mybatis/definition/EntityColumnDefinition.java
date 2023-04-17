package org.helloframework.mybatis.definition;

/**
 * Author lanjian
 * Email  lanjian
 * 在这个类支持方言
 */
public class EntityColumnDefinition {
    private String column;
    private String javaColumn;
    private Class type;
    private String tableAs;
    private String length;
    private Boolean id = false;
    private String desc;
    private boolean nullable = true;
    private GenerationType generated;
    private boolean transfer;
    private boolean aes;

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public GenerationType getGenerated() {
        return generated;
    }

    public void setGenerated(GenerationType generated) {
        this.generated = generated;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Boolean getId() {
        return id;
    }

    public void setId(Boolean id) {
        this.id = id;
    }

    public String getJavaColumn() {
        return javaColumn;
    }

    public void setJavaColumn(String javaColumn) {
        this.javaColumn = javaColumn;
    }

    public String getTableAs() {
        return tableAs;
    }

    public void setTableAs(String tableAs) {
        this.tableAs = tableAs;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isAes() {
        return aes;
    }

    public void setAes(boolean aes) {
        this.aes = aes;
    }
}
