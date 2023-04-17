package org.helloframework.gateway.common.definition.graph;

public class GraphQuery {
    private String op;
    //支持操作符 参考GraphParamOP
    private String k;
    private Object v;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
