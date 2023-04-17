package org.helloframework.gateway.config;

/**
 * Created by lanjian
 */
public class ConsumerConfig {
    private ApplicationConfig applicationConfig;
    private Integer threads;
    private Integer workThreads;
    private String mode;

    public Integer getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(Integer workThreads) {
        this.workThreads = workThreads;
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

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public String toString() {
        return "ConsumerConfig{" +
                "applicationConfig=" + applicationConfig +
                ", threads=" + threads +
                ", workThreads=" + workThreads +
                ", mode='" + mode + '\'' +
                ", registryConfig=" + registryConfig +
                '}';
    }
}
