package org.helloframework.gateway.config;

import org.apache.commons.lang3.StringUtils;
import org.helloframework.gateway.common.utils.URL;

/**
 * Created by lanjian
 */
public class RegistryConfig {
    private String address;
    private URL url;

    public URL getUrl() {
        if (url == null && StringUtils.isNotBlank(address)) {
            URL url = URL.valueOf(address);
            setUrl(url);
        }
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RegistryConfig{");
        sb.append("address='").append(address).append('\'');
        sb.append(", url=").append(url);
        sb.append('}');
        return sb.toString();
    }
}
