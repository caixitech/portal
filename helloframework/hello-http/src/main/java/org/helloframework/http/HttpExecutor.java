package org.helloframework.http;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.helloframework.codec.json.JSON;
import org.helloframework.core.annotation.MappedSuperclass;
import org.helloframework.http.annotation.Http;
import org.helloframework.http.annotation.HttpParam;
import org.helloframework.http.common.HttpFormat;
import org.helloframework.http.common.HttpInfo;
import org.helloframework.http.common.HttpMethod;
import org.helloframework.http.common.HttpParamType;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

//TODO 大体思路如下  细化待定
public class HttpExecutor {

    public static <T> T exec(Object obj) {
        Class cls = obj.getClass();
        if (cls.isAnnotationPresent(Http.class)) {
            try {
                //用户判断启动哪个
                Http http = (Http) cls.getAnnotation(Http.class);
                List<Field> all = allFields(cls);
                //构建一个map用来装数据
                Map<String, HttpInfo> httpInfos = new HashMap();
                Map<String, Object> urls = new HashMap();
                for (Field field : all) {
                    if (field.isAnnotationPresent(HttpParam.class)) {
                        HttpParam httpParam = field.getAnnotation(HttpParam.class);
                        String column = httpParam.name() == null ? field.getName() : httpParam.name();
                        Object value = PropertyUtils.getProperty(obj, column);
                        if (HttpParamType.URL.equals(httpParam.param())) {
                            urls.put(column, value);
                        } else {
                            httpInfos.put(column, new HttpInfo(httpParam.param(), column, value, httpParam.format()));
                        }
                    }
                }
                //处理url
                String url = appendUrl(http.url(), urls);
                Request request = request(http, url);
                if (!httpInfos.isEmpty()) {
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                    for (HttpInfo httpInfo : httpInfos.values()) {
                        if (HttpParamType.BODY.equals(httpInfo.getHttpParamType())) {
                            if (HttpFormat.NONE.equals(httpInfo.getFormat())) {
                                multipartEntityBuilder.addBinaryBody(httpInfo.getName(), httpInfo.getValue().toString().getBytes());
                            }
                            if (HttpFormat.JSON.equals(httpInfo.getFormat())) {
                                multipartEntityBuilder.addBinaryBody(httpInfo.getName(), JSON.toJSONString(httpInfo.getValue()).getBytes());
                            }
                            if (HttpFormat.FILE.equals(httpInfo.getFormat())) {
                                multipartEntityBuilder.addBinaryBody(httpInfo.getName(), (File) httpInfo.getValue());
                            }
                        }
                        if (HttpParamType.BODY_NONAME.equals(httpInfo.getHttpParamType())) {
                            if (HttpFormat.NONE.equals(httpInfo.getFormat())) {
                                request.bodyByteArray(httpInfo.getValue().toString().getBytes());
                            }
                            if (HttpFormat.JSON.equals(httpInfo.getFormat())) {
                                request.bodyByteArray(JSON.toJSONString(httpInfo.getValue()).getBytes());
                            }
                            if (HttpFormat.FILE.equals(httpInfo.getFormat())) {
//                                request.bodyByteArray((File) httpInfo.getValue());
                            }
                        }
                        if (HttpParamType.HEADER.equals(httpInfo.getHttpParamType())) {
                            request.setHeader(httpInfo.getName(), httpInfo.getValue().toString());
                        }
                        if (HttpParamType.FORM.equals(httpInfo.getHttpParamType())) {
                            if (HttpFormat.NONE.equals(httpInfo.getFormat())) {
                                multipartEntityBuilder.addTextBody(httpInfo.getName(), httpInfo.getValue().toString());
                            }
                            if (HttpFormat.FILE.equals(httpInfo.getFormat())) {
                                multipartEntityBuilder.addBinaryBody(httpInfo.getName(), (File) httpInfo.getValue());
                            }
                        }
                    }
                    request.body(multipartEntityBuilder.build());
                }
                //TODO 加密在这里处理
                String json = request.execute().returnContent().asString();
            } catch (Exception e) {
                throw new RuntimeException("exec error", e);
            }
        } else {
            throw new RuntimeException("this obj not http");
        }
        return null;
    }

    private final static String appendUrl(String url, Map<String, Object> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        for (String key : data.keySet()) {
            param.append(key + "=" + data.get(key).toString() + "&");
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (newUrl.indexOf("?") >= 0) {
            newUrl += "&" + paramStr;
        } else {
            newUrl += "?" + paramStr;
        }
        return newUrl;
    }

    private final static Request request(Http http, String url) {
        if (HttpMethod.GET.equals(http.method())) {
            return Request.Get(url);
        }
        if (HttpMethod.POST.equals(http.method())) {
            return Request.Post(url);
        }
        throw new RuntimeException("no method");
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
}

