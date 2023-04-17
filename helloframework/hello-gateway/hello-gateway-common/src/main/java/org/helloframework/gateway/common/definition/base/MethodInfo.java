package org.helloframework.gateway.common.definition.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by lanjian on 08/09/2017.
 */
public class MethodInfo {
    //这里的name是name_version
    private String name;
    private boolean mesh = true;
    private boolean sync = false;
    private boolean graph = false;
    private int timeout;
    private int max;

    public MethodInfo(String name, int timeout, int max, boolean mesh, boolean sync,boolean graph) {
        this.name = name;
        this.timeout = timeout;
        this.max = max;
        this.mesh = mesh;
        this.sync = sync;
        this.graph=graph;
    }

    public boolean isMesh() {
        return mesh;
    }

    public void setMesh(boolean mesh) {
        this.mesh = mesh;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInfo that = (MethodInfo) o;
        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", timeout=" + timeout +
                ", max=" + max +
                '}';
    }
}
