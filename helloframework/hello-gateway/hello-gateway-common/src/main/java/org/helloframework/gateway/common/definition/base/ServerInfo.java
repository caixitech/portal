package org.helloframework.gateway.common.definition.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.helloframework.gateway.common.utils.StringUtils;

/**
 * Created by lanjian
 */
public class ServerInfo {
    private String host;
    private Integer port;
    private Integer weight;

    public ServerInfo(String host, Integer port, Integer weight) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("host is null");
        }
        if (port == null || port <= 0) {
            throw new IllegalArgumentException("port must >0");
        }
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String buildString() {
        return host + ":" + port;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServerInfo that = (ServerInfo) o;

        return new EqualsBuilder()
                .append(host, that.host)
                .append(port, that.port)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(host)
                .append(port)
                .toHashCode();
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("(%s:%s)", getHost(), getPort());
    }
}
