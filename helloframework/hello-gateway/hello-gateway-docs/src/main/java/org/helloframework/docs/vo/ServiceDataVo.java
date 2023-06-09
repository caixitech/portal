package org.helloframework.docs.vo;

import org.springframework.core.ResolvableType;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class ServiceDataVo {
    private String name;
    private String type;
    private String desc;
    private String required;
    private String remark;
    private String link;
    //    @JSONField(serialize = false)
    private ResolvableType ref;

    public ResolvableType getRef() {
        return ref;
    }

    public void setRef(ResolvableType ref) {
        this.ref = ref;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
