package org.helloframework.gateway.common.definition.apiservice;

import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.beanutils.PropertyUtils;
import org.helloframework.core.annotation.MappedSuperclass;
import org.helloframework.gateway.common.annotation.*;
import org.helloframework.gateway.common.annotation.graph.Graph;
import org.helloframework.gateway.common.annotation.graph.GraphOP;
import org.helloframework.gateway.common.annotation.graph.GraphParam;
import org.helloframework.gateway.common.annotation.graph.GraphParamOP;
import org.helloframework.gateway.common.definition.apiservice.plugins.*;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.JacksonApiServiceCodecPlugin;
import org.helloframework.gateway.common.definition.graph.Graphs;
import org.helloframework.gateway.common.utils.DigestUtils;
import org.helloframework.gateway.common.utils.KeyUtils;
import org.helloframework.gateway.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by lanjian
 */
public class ApiServiceInvoker {
    private final static Logger log = LoggerFactory.getLogger(ApiServiceInvoker.class);
    private String serviceName;
    private ApiServiceDefinition apiServiceDefinition;
    private String version;
    private String slat;
    private Class serviceClass;
    private Class inClass;
    private Class outClass;
    private ApiServiceContext apiServiceContext;
    private Method method;
    private ApiServiceCodecPlugin apiServiceCodecPlugin;
    private ApiServiceCodecPlugin meshCodecPlugin = new JacksonApiServiceCodecPlugin();
    private ApiServiceValidatePlugin apiServiceValidatePlugin;
    private ApiServiceExtendPlugin apiServiceExtendPlugin;
    private ApiServiceRequestPlugin apiServiceRequestPlugin;
    private ApiServiceResponsePlugin apiServiceResponsePlugin;
    private ApiServiceCachePlugin apiServiceCachePlugin;
    private ApiServiceUserValidPlugin apiServiceUserValidPlugin;
    private ApiServiceSensitivePlugin apiServiceSensitivePlugin;
    private ApiServiceProxyPlugin apiServiceProxyPlugin;
    private int timeout;
    private int max;
    private boolean mesh;
    private boolean sync;
    private boolean graph = false;
    private Class[] graphCls;
    private Map<String, Graphs> graphsMap = new HashMap();

    public String getSlat() {
        return slat;
    }

    public boolean isGraph() {
        return graph;
    }

    public Class[] getGraphCls() {
        return graphCls;
    }

    public Map<String, Graphs> getGraphsMap() {
        return graphsMap;
    }


    public ApiServiceInvoker(ApiServiceDefinition apiServiceDefinition, ApiServiceContext apiServiceContext, Method method, Class in, Class out, int timeout, int max, String version, boolean mesh, boolean sync, Class[] graphCls, String slat) {
        this.apiServiceContext = apiServiceContext;
        this.method = method;
        this.apiServiceDefinition = apiServiceDefinition;
        this.serviceClass = AopUtils.isAopProxy(apiServiceDefinition) ? AopUtils.getTargetClass(apiServiceDefinition) : apiServiceDefinition.getClass();
        this.inClass = in;
        this.outClass = out;
        this.timeout = timeout;
        this.version = version;
        this.max = max;
        this.mesh = mesh;
        this.sync = sync;
        this.graphCls = graphCls;
        this.slat = slat;
        if (graphCls != null && graphCls.length > 0) {
            this.graph = true;
        }
        resolve();
    }

    public static String name(String serviceName, String version) {
        return String.format("%s_%s", serviceName, version);
    }

    public boolean isMesh() {
        return mesh;
    }

    public boolean isSync() {
        return sync;
    }

    public ApiServiceDefinition getApiServiceDefinition() {
        return apiServiceDefinition;
    }

    public Method getMethod() {
        return method;
    }

    public long getTimeout() {
        return timeout;
    }

    public int getMax() {
        return max;
    }

    public ApiServiceContext getApiServiceContext() {
        return apiServiceContext;
    }

    public void setApiServiceContext(ApiServiceContext apiServiceContext) {
        this.apiServiceContext = apiServiceContext;
    }

    public String finalName() {
        return name(serviceName, version);
    }

    public byte[] invoke(byte[] data, ApiExtend extend) throws Exception {
        long begin = System.currentTimeMillis();
        final boolean mesh = extend.isMesh();
        try {
            if (apiServiceCodecPlugin == null) {
                throw new RuntimeException("apiServiceCodecPlugin is null");
            }
            if (inClass == null) {
                throw new RuntimeException("in class is null");
            }
            Object in = mesh ? meshCodecPlugin.decode(data, this.inClass) : apiServiceCodecPlugin.decode(data, this.inClass);
            List<ValidateMessage> validateMessages = apiServiceValidatePlugin == null ? new ArrayList() : apiServiceValidatePlugin.validate(in);
            extend.setMessages(validateMessages);
            if (apiServiceExtendPlugin != null) {
                apiServiceExtendPlugin.handler(extend);
            }
            //请求包装
            if (apiServiceRequestPlugin != null) {
                in = apiServiceRequestPlugin.handler(in);
            }
            userValid(in, extend);
            validate(in);
            sensitive(in);
            Object out = cache(extend, in);
            if (apiServiceProxyPlugin != null) {
                out = apiServiceProxyPlugin.handler(in, extend);
            } else if (!mesh && apiServiceResponsePlugin != null) {
                out = apiServiceResponsePlugin.handler(out, extend);
            }
            byte[] message = mesh ? meshCodecPlugin.encode(out, this.outClass) : apiServiceCodecPlugin.encode(out, this.outClass);
            return message;
        } finally {
            long execTime = System.currentTimeMillis() - begin;
            if (execTime > 500) {
                log.info("ApiServiceInvoker 耗时{}ms,api:{},version:{},timeout:{},data length:{}", execTime, serviceName, version, timeout, data.length);
            }
        }
    }

    private void validate(Object in) {
        //验证插件
        if (apiServiceValidatePlugin != null) {
            apiServiceValidatePlugin.validate(in);
        }
    }

    private Object cache(ApiExtend extend, Object in) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        //缓存模块 out为返回数据
        Object out = null;
        if (serviceClass.isAnnotationPresent(ApiCache.class) || method.isAnnotationPresent(ApiCache.class)) {
            ApiCache apiServiceCache = (ApiCache) serviceClass.getAnnotation(ApiCache.class);
            if (apiServiceCache == null) {
                apiServiceCache = method.getAnnotation(ApiCache.class);
            }
            if (apiServiceCachePlugin == null) {
                throw new RuntimeException("apiServiceCachePlugin is null");
            }
            String key = DigestUtils.md5(KeyUtils.key(in));
            out = apiServiceCachePlugin.get(key, this.outClass, apiServiceCache.group());
            if (out == null) {
                out = this.outClass.newInstance();
                invoke0(in, out, extend);
                apiServiceCachePlugin.set(key, out, this.outClass, apiServiceCache.time(), apiServiceCache.group());
            }
        } else {
            out = this.outClass.newInstance();
            invoke0(in, out, extend);
        }
        return out;
    }

    private void userValid(Object in, ApiExtend extend) {
        // 无需检查token
        if (serviceClass.isAnnotationPresent(NoUserAuth.class) || method.isAnnotationPresent(NoUserAuth.class)) {
            return;
        }
        //用户验证插件
//        if (serviceClass.isAnnotationPresent(UserValid.class) || method.isAnnotationPresent(UserValid.class)) {
        if (apiServiceUserValidPlugin == null) {
            throw new RuntimeException("apiServiceUserValidPlugin is null");
        }
        apiServiceUserValidPlugin.valid(in, extend);
//        }
    }

    private void sensitive(Object in) {
        //关键词过滤插件
        if (apiServiceSensitivePlugin != null) {
            if (in.getClass().isAnnotationPresent(Sensitive.class)) {
                List<String> keys = KeyUtils.resolve(in, Sensitive.class);
                for (String key : keys) {
                    try {
                        Object value = PropertyUtils.getProperty(in, key);
                        if (value instanceof String) {
                            apiServiceSensitivePlugin.sensitive((String) value);
                        } else {
                            throw new RuntimeException("value not string");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Property get error:" + key, e);
                    }
                }
            }
        }
    }

    private void invoke0(Object in, Object out, ApiExtend extend) throws InvocationTargetException, IllegalAccessException {
        if (log.isDebugEnabled()) {
            log.debug(String.format("========in data:%s", in.toString()));
        }
        if (graph) {
            extend.setGraphs(graphsMap);
        }
        if (ApiService.class.isAssignableFrom(serviceClass)) {
            ApiService service = (ApiService) apiServiceDefinition;
            if (AbstractApiService.class.isAssignableFrom(serviceClass)) {
                service.handler(in, out, extend);
            } else {
                service.service(in, out, extend);
            }
        } else {
            method.invoke(this.apiServiceDefinition, in, out, extend);
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("========out data:%s", out.toString()));
        }
    }

    private final static List<Field> allFields(Class cls) {
        List<Field> all = new ArrayList<Field>();
        Class tmpClass = cls;
        while (tmpClass != null) {
            all.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            Class superClass = tmpClass.getSuperclass();
            if (superClass != null && superClass.isAnnotationPresent(MappedSuperclass.class)) {
                tmpClass = superClass;
            } else {
                break;
            }
        }
        return all;
    }

    /**
     * 图接口解析
     */
    private void graphResolve() {
        if (graph) {
            for (Class cls : graphCls) {
                //解析domain
                if (cls.isAnnotationPresent(Graph.class)) {
                    Graph graph = (Graph) cls.getAnnotation(Graph.class);
                    Graphs graphs = new Graphs();
                    graphs.setCls(cls);
                    for (GraphOP graphOP : graph.ops()) {
                        graphs.push(graphOP);
                    }
                    List<Field> all = allFields(cls);
                    for (Field field : all) {
                        if (field.isAnnotationPresent(GraphParam.class)) {
                            GraphParam graphParam = field.getAnnotation(GraphParam.class);
                            if (StringUtils.isBlank(graphParam.criteria())) {
                                for (GraphParamOP graphParamOP : graphParam.ops()) {
                                    graphs.push(graphParamOP.opName(field.getName()));
                                }
                            } else {
                                for (GraphParamOP graphParamOP : graphParam.ops()) {
                                    graphs.push(graphParamOP.opName(graphParam.criteria()));
                                }
                            }
                        }
                    }
                    if (graphs.getGraphsOPs().isEmpty()) {
                        throw new RuntimeException(String.format("cls %s empty", cls.getName()));
                    }
                    graphsMap.put(cls.getName(), graphs);
                } else {
                    throw new RuntimeException(String.format("cls %s not graph", cls.getName()));
                }
            }
        }
    }

    /**
     * 动态赋值给apiservice解析器  验证器
     */
    private void resolve() {
        if (apiServiceContext == null) {
            throw new IllegalArgumentException("apiServiceContext is null");
        }
        Api api = (Api) serviceClass.getAnnotation(Api.class);
        if (api == null) {
            throw new RuntimeException("this is not api service");
        }
        //兼容之前版本
//         || AbstractApiService.class.isAssignableFrom(serviceClass)
        if (ApiService.class.isAssignableFrom(serviceClass)) {
            this.serviceName = serviceClass.getName();
        } else {
            this.serviceName = serviceClass.getName() + "." + method.getName();
        }
        //提前缓存pbsuff
        cacheSchema(inClass, outClass);
        this.apiServiceCodecPlugin = apiServiceContext.findPlugin(ApiServiceCodecPlugin.class);
        this.apiServiceResponsePlugin = apiServiceContext.findPlugin(ApiServiceResponsePlugin.class, false);
        this.apiServiceValidatePlugin = apiServiceContext.findPlugin(ApiServiceValidatePlugin.class, false);
        this.apiServiceRequestPlugin = apiServiceContext.findPlugin(ApiServiceRequestPlugin.class, false);
        this.apiServiceCachePlugin = apiServiceContext.findPlugin(ApiServiceCachePlugin.class, false);
        this.apiServiceUserValidPlugin = apiServiceContext.findPlugin(ApiServiceUserValidPlugin.class, false);
        this.apiServiceSensitivePlugin = apiServiceContext.findPlugin(ApiServiceSensitivePlugin.class, false);
        this.apiServiceExtendPlugin = apiServiceContext.findPlugin(ApiServiceExtendPlugin.class, false);
        if (!ApiServiceProxyPlugin.class.equals(api.proxy())) {
            this.apiServiceProxyPlugin = apiServiceContext.findPlugin(api.proxy(), false);
        }
        graphResolve();
    }


    /**
     * 用户提前缓存protobuf
     */
    private synchronized void cacheSchema(Class... clss) {
        for (Class cls : clss) {
            if (cls.isAnnotationPresent(Protobuf.class)) {
                RuntimeSchema.getSchema(cls);
            }
        }
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Class getInClass() {
        return inClass;
    }

    public void setInClass(Class inClass) {
        this.inClass = inClass;
    }

    public Class getOutClass() {
        return outClass;
    }

    public void setOutClass(Class outClass) {
        this.outClass = outClass;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
