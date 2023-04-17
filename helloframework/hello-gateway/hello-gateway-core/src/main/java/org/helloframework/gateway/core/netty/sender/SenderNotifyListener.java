package org.helloframework.gateway.core.netty.sender;

import org.helloframework.gateway.common.definition.Constants;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceContext;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceInvoker;
import org.helloframework.gateway.common.definition.base.MethodInfo;
import org.helloframework.gateway.common.definition.base.ServerInfo;
import org.helloframework.gateway.common.definition.registry.NotifyListener;
import org.helloframework.gateway.common.utils.StringUtils;
import org.helloframework.gateway.common.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lanjian on 2017/11/30.
 */
public class SenderNotifyListener implements NotifyListener {
    private final static Logger log = LoggerFactory.getLogger(SenderNotifyListener.class);
    private ApiServiceContext apiServiceContext;

    public SenderNotifyListener(ApiServiceContext apiServiceContext) {
        this.apiServiceContext = apiServiceContext;
    }

    public synchronized void notify(List<URL> urls) {

        try {
            List<URL> providers = new ArrayList();
            for (URL source : urls) {
                log.debug("notify=====" + source);
                if (source.toFullString().startsWith(Constants.NAME)) {
                    if (Constants.PROVIDERS_CATEGORY.equals(source.getParameter(Constants.CATEGORY_KEY))) {
                        providers.add(source);
                    }
                }
            }
            //处理client 链接问题
            for (URL url : providers) {
                String path = url.getPath();
                if (ApiServiceContext.MODE.mesh.equals(apiServiceContext.getMode())) {
                    if (!path.equals(apiServiceContext.getName())) {
                        initClient(url);
                    }
                }
                if (ApiServiceContext.MODE.remote.equals(apiServiceContext.getMode())) {
                    initClient(url);
                }
            }
        } catch (Throwable t) {
            //防御性容错，dubbo zk bug  如异常后  一直会处于通知失败
            log.error("notify  fail", t);
        }
    }

    private void initClient(URL url) {
        String methodParameter = url.getParameter(Constants.METHODS_KEY);
        if (!StringUtils.isBlank(methodParameter)) {
            Set<MethodInfo> methodInfos = new HashSet<MethodInfo>();
            String[] methods = methodParameter.split(Constants.METHODS_SEPARATOR);
            for (String methodStr : methods) {
                //这里的服务名称是什么
                String[] info = methodStr.split("_");
                String method = ApiServiceInvoker.name(info[0], info[1]);
                //name_version_mesh_sync_max_timeout
                MethodInfo methodInfo = new MethodInfo(method,
                        Integer.valueOf(info[5]),
                        Integer.valueOf(info[4]),
                        Boolean.valueOf(info[2]),
                        Boolean.valueOf(info[3]),
                        Boolean.valueOf(info[6]));
                methodInfos.add(methodInfo);
            }
            ServerInfo serverInfo = new ServerInfo(url.getHost(), url.getPort(), url.getParameter(Constants.WEIGHT_KEY, Constants.WEIGHT));
            apiServiceContext.registryConsumer(serverInfo, url);
            apiServiceContext.getClientContext().registerClient(serverInfo, apiServiceContext, methodInfos);
        }
    }
}
