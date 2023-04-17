package org.helloframework.gateway.config;

/**
 * Created by lanjian
 */
public class ProviderConfig {
    private ApplicationConfig applicationConfig;
    private Integer max;
    private Integer port;
    private String protocol;
    private Integer threads;
    private Integer workThreads;
    private String localHost;
    private String mode;
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private RegistryConfig registryConfig;

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }


    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public Integer getThreads() {
        return threads;
    }

    public Integer getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(Integer workThreads) {
        this.workThreads = workThreads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public String toString() {
        return "ProviderConfig{" +
                "applicationConfig=" + applicationConfig +
                ", max=" + max +
                ", port=" + port +
                ", protocol='" + protocol + '\'' +
                ", threads=" + threads +
                ", workThreads=" + workThreads +
                ", localHost='" + localHost + '\'' +
                ", mode='" + mode + '\'' +
                ", weight=" + weight +
                ", registryConfig=" + registryConfig +
                '}';
    }
}
