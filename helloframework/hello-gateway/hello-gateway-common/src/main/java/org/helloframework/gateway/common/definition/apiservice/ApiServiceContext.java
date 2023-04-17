package org.helloframework.gateway.common.definition.apiservice;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.helloframework.gateway.common.annotation.Api;
import org.helloframework.gateway.common.annotation.ApiMethod;
import org.helloframework.gateway.common.annotation.graph.GraphOP;
import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.plugins.Plugin;
import org.helloframework.gateway.common.definition.base.ServerInfo;
import org.helloframework.gateway.common.definition.graph.Graphs;
import org.helloframework.gateway.common.definition.registry.NotifyListener;
import org.helloframework.gateway.common.definition.registry.Registry;
import org.helloframework.gateway.common.definition.registry.RegistryFactory;
import org.helloframework.gateway.common.utils.ClassUtils;
import org.helloframework.gateway.common.utils.NetUtils;
import org.helloframework.gateway.common.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by lanjian
 */
public class ApiServiceContext {

    private static final Logger log = LoggerFactory.getLogger(ApiServiceContext.class);
    private static final ConcurrentMap<String, ApiServiceInvoker> apiServices = new ConcurrentHashMap<String, ApiServiceInvoker>();
    private final static ReentrantLock reentrantLock = new ReentrantLock();
    private ClientContext clientContext;
    private ExecutorService executorService = null;
    private AtomicInteger working = new AtomicInteger(0);
    private URL registryUrl;
    private String name;
    private Integer weight;
    private RegistryFactory registryFactory;
    private Registry registry;
    private String hostname;
    private Integer port;
    private Integer threads;
    private Integer max = Constants.MAX;
    private Integer workThreads;
    private String protocol;
    private NotifyListener notifyListener;
    private boolean shutdown = false;
    private MODE mode = MODE.remote;
    private ApplicationContext applicationContext;


    private ApiServiceContext() {

    }

    /**
     * 全局单例  apiservice上下文  全局数据存储
     *
     * @return
     */
    public synchronized static ApiServiceContext newInstance() {
        return new ApiServiceContext();
    }


    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

//    public void setTimeout(Integer timeout) {
//        this.timeout = timeout;
//    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setNotifyListener(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(Integer workThreads) {
        this.workThreads = workThreads;
    }

    public AtomicInteger getWorking() {
        return working;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public ClientContext getClientContext() {
        if (clientContext == null) {
            throw new RuntimeException("clientContext not register");
        }
        return clientContext;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public void shutdown() {
        setShutdown(true);
        //等待处理事务完成
        while (working.get() != 0) {
            log.info("there also " + working.get() + " threads running. program will exit after there running over.");
            try {
                Thread.sleep(500);
            } catch (Throwable e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
        registryClose();
        if (clientContext != null) {
            clientContext.destroy();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setRegistryUrl(URL registryUrl) {
        this.registryUrl = registryUrl;
    }

    public void setRegistryFactory(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    private static final Map<Class, Plugin> plugins = new ConcurrentHashMap();

    public <T> T findPlugin(Class cls, boolean flag) {
        if (Plugin.class.isAssignableFrom(cls)) {
            Plugin plugin = plugins.get(cls);
            if (plugin == null) {
                try {
                    plugin = (Plugin) applicationContext.getBean(cls);
                    if (plugin != null) {
                        plugins.put(cls, plugin);
                    }
                } catch (Throwable t) {
                    //防御容错
                }
            }
            if (flag) {
                if (plugin == null) {
                    throw new RuntimeException("cls " + cls.getName() + " is null");
                }
            }
            return (T) plugin;
        } else {
            throw new RuntimeException("cls " + cls.getName() + " error");
        }
    }

    public <T> T findPlugin(Class cls) {
        return findPlugin(cls, true);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    private void registerInvokers(ApiServiceDefinition apiServiceDefinition) {

        Class apiServiceCls = AopUtils.isAopProxy(apiServiceDefinition) ? AopUtils.getTargetClass(apiServiceDefinition) : apiServiceDefinition.getClass();

        if (!ApiServiceDefinition.class.isAssignableFrom(apiServiceCls)) {
            throw new RuntimeException("not ApiServiceDefinition");
        }
        if (ApiService.class.isAssignableFrom(apiServiceCls)) {
            Api apiService = (Api) apiServiceCls.getAnnotation(Api.class);
            if (apiService == null) {
                throw new RuntimeException("this is not api service");
            }
            Class inClass = ClassUtils.findenericType(0, apiServiceCls);
            Class outClass = ClassUtils.findenericType(1, apiServiceCls);
            Method method = MethodUtils.getMatchingMethod(apiServiceCls, Constants.TMP_METHOD_SERVICE, inClass, outClass, ApiExtend.class);
            if (method == null) {
                throw new RuntimeException(String.format("%s not find method", apiServiceCls.getName()));
            }
            registerInvoker(apiServiceDefinition, method, inClass, outClass, apiService.timeout(), max, apiService.version(), apiService.mesh(), apiService.sync(), apiService.graph(), apiService.slat());
            return;
        } else {
            List<Method> methods = MethodUtils.getMethodsListWithAnnotation(apiServiceCls, ApiMethod.class);
            for (Method method : methods) {
                ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
                registerInvoker(apiServiceDefinition, method, method.getParameterTypes()[0], method.getParameterTypes()[1], apiMethod.timeout(), max, apiMethod.version(), apiMethod.mesh(), apiMethod.sync(), null, null);
            }
            return;
        }
    }

    private void registerInvoker(ApiServiceDefinition apiService, Method method, Class in, Class out, int timeout, int max, String version, boolean mesh, boolean sync, Class[] graph, String slat) {
        //所有网关都是 in  out机制 先验证一下
        if (MethodUtils.getMatchingMethod(apiService.getClass(), method.getName(), in, out, ApiExtend.class) == null) {
            throw new RuntimeException("method is is not match apiServiceMethod");
        }
        ApiServiceInvoker apiServiceInvoker = new ApiServiceInvoker(apiService, this, method, in, out, timeout, max, version, mesh, sync, graph, slat);
        //如果是图对象
        if (apiServiceInvoker.isGraph()) {
            Collection<Graphs> graphss = apiServiceInvoker.getGraphsMap().values();
            for (Graphs graphs : graphss) {
                String domain = graphs.getCls().getName();
                Set<GraphOP> graphOPS = graphs.getGraphs().keySet();
                for (GraphOP graphOP : graphOPS) {
                    apiServices.put(graphOP.finalName(domain, apiServiceInvoker.getVersion(), apiServiceInvoker.getSlat()), apiServiceInvoker);
                }
            }
        } else {
            apiServices.put(apiServiceInvoker.finalName(), apiServiceInvoker);
        }
    }


    public void registerAllApiService() {
        Map<String, ApiServiceDefinition> maps = applicationContext.getBeansOfType(ApiServiceDefinition.class);
        if (maps.isEmpty()) {
            return;
        }
        Collection<ApiServiceDefinition> apiServiceDefinitions = maps.values();
        long startTime = System.currentTimeMillis();
        for (ApiServiceDefinition apiServiceDefinition : apiServiceDefinitions) {
//            apiServiceDefinition.setApiServiceContext(this);
            registerInvokers(apiServiceDefinition);
        }
        log.debug("finish register all service spend time: {}", System.currentTimeMillis() - startTime);
        if (!MODE.local.equals(mode)) {
            if (registry == null) {
                throw new IllegalArgumentException("registry is null");
            }
            //全局注册一个url
            URL url = apiServiceInvokerToUrl();
            if (url == null) {
                return;
            }
            registry.register(url);
        }
    }

    private void registryReady() {
        if (MODE.local.equals(mode)) {
            return;
        }
        if (registryUrl == null) {
            throw new IllegalArgumentException("registryUrl is null");
        }
        if (registryFactory == null) {
            throw new IllegalArgumentException("registryFactory is null");
        }
        registry = registryFactory.open(registryUrl);
        if (notifyListener != null) {
            subscribe(notifyListener);
        }
    }


    private void registryClose() {
        if (MODE.local.equals(mode)) {
            return;
        }
        if (registry != null) {
            URL url = apiServiceInvokerToUrl();
            if (url == null) {
                return;
            }
            registry.unregister(url);
            //移除掉使用者
            url = url.addParameter(Constants.CATEGORY_KEY, Constants.CONSUMERS_CATEGORY).setHost(this.hostname).setPort(0).removeParameter(Constants.WEIGHT_KEY).removeParameter(Constants.TIMEOUT_KEY).removeParameter(Constants.MAX_KEY);
            registry.unregister(url);
            registry.destroy();
        } else {
            log.warn("registry is null");
        }

    }

    private URL apiServiceInvokerToUrl() {
        try {
            StringBuffer services = new StringBuffer();
            for (String name : this.apiServices.keySet()) {
                ApiServiceInvoker serviceInvoker = this.apiServices.get(name);
                // name=service_version
                // 这里放每一个服务的具体名字 _ 拼接 name_version_mesh_sync_max_timeout_graph
                String info = String.format("%s_%s_%s_%s_%s_%s",
                        name,
                        serviceInvoker.isMesh(),
                        serviceInvoker.isSync(),
                        serviceInvoker.getMax(),
                        serviceInvoker.getTimeout(),
                        serviceInvoker.isGraph());
                services.append(info);
                services.append(Constants.METHODS_SEPARATOR);
            }
            String finalServices = services.substring(0, services.length() - 1);
            URL url = new URL(Constants.NAME, hostname, port);
            url = url.setPath(name).setServiceName(name)
                    .addParameter(Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY)
                    .addParameter(Constants.WEIGHT_KEY, weight)
                    .addParameter(Constants.TIME_VERSION_KEY, System.currentTimeMillis())
                    .addParameter(Constants.METHODS_KEY, finalServices);
            return url;
        } catch (Throwable t) {
            return null;
        }
    }


//    private URL apiServiceInvokerToUrl(ApiServiceInvoker apiServiceInvoker) {
//        URL url = new URL(Constants.NAME, hostname, port);
//        url = url.setServiceInterface(apiServiceInvoker.getServiceClass().getName())
//                .setPath(apiServiceInvoker.getServiceName())
//                .addParameter(Constants.TIMEOUT_KEY, apiServiceInvoker.getTimeout())
//                .addParameter(Constants.MAX_KEY, apiServiceInvoker.getMax())
//                .addParameter(Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY)
//                .addParameter(Constants.VERSION_KEY, apiServiceInvoker.getVersion())
//                .addParameter(Constants.IN_CLASS_KEY, apiServiceInvoker.getIn().getName())
//                .addParameter(Constants.OUT_CLASS_KEY, apiServiceInvoker.getOut().getName())
//                .addParameter(Constants.WEIGHT_KEY, Constants.WEIGHT)
//                .addParameter(Constants.TIME_VERSION_KEY, apiServiceInvoker.getTimeVersion());
//        return url;
//    }

    public void serverReady(Ready ready) {
        reentrantLock.lock();
        try {
            //自定义服务
            if (ready != null) {
                ready.ready();
            }
            //开启注册中心
            registryReady();
            //注册所有服务
            registerAllApiService();
        } catch (Throwable ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new RuntimeException(ex);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void clientReady() {
        reentrantLock.lock();
        try {
            //开启注册中心
            registryReady();
        } catch (Throwable ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new RuntimeException(ex);
        } finally {
            reentrantLock.unlock();
        }
    }

    private void subscribe(final NotifyListener listener) {
        if (MODE.local.equals(mode)) {
            return;
        }
        registry.subscribe(new URL(Constants.NAME, NetUtils.ANYHOST, 0).setServiceName("*"), listener);
    }

    public void registryConsumer(ServerInfo serverInfo, URL url) {
        if (MODE.local.equals(mode)) {
            return;
        }
        /**
         * 注册调用者
         */
        if (!getClientContext().containsServerInfo(serverInfo)) {
            //注册使用者
            url = url.addParameter(Constants.CATEGORY_KEY, Constants.CONSUMERS_CATEGORY).setHost(this.hostname).setPort(0).removeParameter(Constants.WEIGHT_KEY).removeParameter(Constants.TIMEOUT_KEY).removeParameter(Constants.MAX_KEY);
            registry.register(url);
        }
    }

    public ApiServiceInvoker findApiServiceInvoker(String service, String version) {
        reentrantLock.lock();
        try {
            String serviceName = ApiServiceInvoker.name(service, version);
            ApiServiceInvoker apiServiceInvoker = apiServices.get(serviceName);
//            if (apiServiceInvoker == null) {
//                throw new RuntimeException("serviceName not found");
//            }
            return apiServiceInvoker;
        } catch (Exception ex) {
            throw ex;
        } finally {
            reentrantLock.unlock();
        }
    }

    public enum MODE {
        local, remote, mesh;
    }

}
