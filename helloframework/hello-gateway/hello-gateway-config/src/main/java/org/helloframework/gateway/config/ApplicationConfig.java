package org.helloframework.gateway.config;

/**
 * Created by lanjian
 */
public class ApplicationConfig {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ApplicationConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
