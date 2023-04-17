package org.helloframework.testcase.common;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.helloframework.testcase.config.RequestType;
import org.helloframework.testcase.config.TestCaseConfig;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @Description
 * @ClassName TestCase
 * @Author lanjian
 * @date 2020.01.14 17:39
 */

public class TestCase {
    private Map<String, Object> params = new HashMap<String, Object>();
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Map<String, Object> cookies = new HashMap<String, Object>();
    private Object body;
    private String serverUrl;
    private String path;
    private RequestConfig requestConfig;
    private SignHandler signHandler;
    private ContentType contentType;
    private RequestType requestType;
    private Then then = base;

    private static Then base = new Then() {
        @Override
        public ValidatableResponse then(ValidatableResponse response) {
            return response;
        }
    };

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Object> cookies) {
        this.cookies = cookies;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public SignHandler getSignHandler() {
        return signHandler;
    }

    public void setSignHandler(SignHandler signHandler) {
        this.signHandler = signHandler;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    private TestCase() {

    }

    private static boolean mapIsNotEmpty(Map map) {
        return (map != null && !map.isEmpty());
    }

    public static TestCase build() {
        TestCase runCase = new TestCase();
        return runCase;
    }

    public static TestCase build(TestCaseConfig testCaseConfig) {
        TestCase runCase = new TestCase();
        if (mapIsNotEmpty(testCaseConfig.getHeaders())) {
            runCase.headers.putAll(testCaseConfig.getHeaders());
        }
        if (mapIsNotEmpty(testCaseConfig.getCookies())) {
            runCase.cookies.putAll(testCaseConfig.getCookies());
        }
        if (mapIsNotEmpty(testCaseConfig.getParams())) {
            runCase.params.putAll(testCaseConfig.getParams());
        }
        runCase.serverUrl = testCaseConfig.getServerUrl();
        runCase.path = testCaseConfig.getPath();
        runCase.contentType = testCaseConfig.getContentType();
        runCase.signHandler = testCaseConfig.getSignHandler();
        runCase.requestConfig = testCaseConfig.getRequestConfig();
        runCase.then = testCaseConfig.getThen();
        return runCase;

    }

    public TestCase serverUrl(String url) {
        this.serverUrl = url;
        return this;
    }

    public TestCase then(Then then) {
        if (then == null) {
            this.then = base;
        }
        this.then = then;
        return this;
    }


    public TestCase sign(SignHandler signHandler) {
        this.signHandler = signHandler;
        return this;
    }

    public TestCase path(String path) {
        this.path = path;
        return this;
    }

    public TestCase contentype(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public TestCase headers(Map<String, Object> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public TestCase header(String key, Object v) {
        this.headers.put(key, v);
        return this;
    }

    public TestCase cookie(String key, Object v) {
        this.cookies.put(key, v);
        return this;
    }

    public TestCase param(String key, Object v) {
        this.params.put(key, v);
        return this;
    }

    public TestCase cookies(Map<String, Object> cookies) {
        this.cookies.putAll(cookies);
        return this;
    }


    public TestCase params(Map<String, Object> params) {
        this.params.putAll(params);
        return this;
    }


    public TestCase body(Object body) {
        this.body = body;
        return this;
    }

    private RequestSpecification request(RequestType requestType) {
        if (serverUrl == null && path == null) {
            throw new RuntimeException("serverUrl || path 为空");
        }
        //httpclinet配置
        if (requestConfig != null) {
            RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(new HttpClientConfig.HttpClientFactory() {
                public HttpClient createHttpClient() {
                    return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                }
            }));
        }
        this.requestType = requestType;
        RequestSpecification request = given().baseUri(serverUrl);
        if (mapIsNotEmpty(params)) {
            request = request.params(params);
        }
        if (mapIsNotEmpty(cookies)) {
            request = request.cookies(cookies);
        }
        if (mapIsNotEmpty(headers)) {
            request = request.headers(headers);
        }
        if (body != null) {
            request = request.body(body);
        }
        if (contentType != null) {
            request = request.contentType(contentType);
        }
        if (signHandler != null) {
            signHandler.sign(request, this);
        }
        return request;
    }


    public Response get() {
        Response response = request(RequestType.GET).when().get(path);
        return response;
    }

    public Response post() {
        Response response = request(RequestType.POST).when().post(path);
        return response;
    }

    public ValidatableResponse vGet() {
        Response response = request(RequestType.GET).when().get(path);
        return then.then(response.then());
    }

    public ValidatableResponse vPost() {
        Response response = request(RequestType.POST).when().post(path);
        return then.then(response.then());
    }
}
