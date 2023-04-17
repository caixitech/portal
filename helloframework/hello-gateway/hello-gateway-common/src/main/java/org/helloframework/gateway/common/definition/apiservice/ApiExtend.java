package org.helloframework.gateway.common.definition.apiservice;

import io.jsonwebtoken.Jwts;
import org.helloframework.codec.json.JSON;
import org.helloframework.gateway.common.definition.apiservice.plugins.ValidateMessage;
import org.helloframework.gateway.common.definition.graph.Graphs;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lanjian on 02/09/2017.
 */
public class ApiExtend {
    private String appid;
    private String requestId;
    private String nonce;
    private String clientIp;
    private String api;
    private String version;
    private List<ValidateMessage> messages;
    private Map<String, Graphs> graphs;
    private boolean mesh = false;
    private String path;
    private int respType = RespType.MAP;
    private String method = "post";
    private String token;
    private String source;
    private Boolean test;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Map<String, Graphs> getGraphs() {
        return graphs;
    }

    public void setGraphs(Map<String, Graphs> graphs) {
        this.graphs = graphs;
    }

    public boolean isMesh() {
        return mesh;
    }

    public void setMesh(boolean mesh) {
        this.mesh = mesh;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public List<ValidateMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ValidateMessage> messages) {
        this.messages = messages;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRespType() {
        return respType;
    }

    public void setRespType(int respType) {
        this.respType = respType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static String getTokenKey() {
        return tokenKey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public boolean isTest() {
        if (test == null) {
            return false;
        }
        return test;
    }

    @Override
    public String toString() {
        return "ApiExtend{" +
                "appid='" + appid + '\'' +
                ", requestId='" + requestId + '\'' +
                ", nonce='" + nonce + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", api='" + api + '\'' +
                ", version='" + version + '\'' +
                ", messages=" + messages +
                ", graphs=" + graphs +
                ", mesh=" + mesh +
                ", path='" + path + '\'' +
                ", respType=" + respType +
                ", method='" + method + '\'' +
                ", token='" + token + '\'' +
                ", source='" + source + '\'' +
                ", test=" + test +
                '}';
    }

    private static final String tokenKey = "yayakouyu";

    /**
     * 根据token获取用户ID
     *
     * @return
     */
    public Integer getUidIntFromToken() {
        String token = this.getToken();
        if (!StringUtils.hasText(token))
            return null;
        String loginJson = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody().getSubject();
        return JSON.parseObject(loginJson).getInteger("id");
    }

    public String getUidStrFromToken() {
        Integer uid = getUidIntFromToken();
        return null != uid ? String.valueOf(uid) : null;
    }
}
