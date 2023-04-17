package org.helloframework.mybatis.definition;

/**
 * Created by macintosh
 */
public class Between {
    private Object s;
    private Object e;

    public Between(Object s, Object e) {
        this.s = s;
        this.e = e;
    }

    public Object getS() {
        return s;
    }

    public void setS(Object s) {
        this.s = s;
    }

    public Object getE() {
        return e;
    }

    public void setE(Object e) {
        this.e = e;
    }
}
