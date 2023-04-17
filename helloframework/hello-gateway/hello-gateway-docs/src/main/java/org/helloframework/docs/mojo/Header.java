package org.helloframework.docs.mojo;


/**
 * @author yoqu
 * @date 2018/5/21 - 14:02
 */
public class Header {

    private String name;

    private String type = "String";
    private String desc;

    private String remark;

    private boolean required = false;
    private String example;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Header() {
    }

    public Header(String name, String desc, boolean required) {
        this(name, desc, required, null);
    }

    public Header(String name, String desc, boolean required, String example) {
        this.name = name;
        this.type = "String";
        this.desc = desc;
        this.required = required;
        this.example = example;
    }

    public Header(String name, String desc) {
        this(name, desc, true);
    }

    public Header(String name, String type, String desc, String remark, boolean required) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.remark = remark;
        this.required = required;
    }
}
