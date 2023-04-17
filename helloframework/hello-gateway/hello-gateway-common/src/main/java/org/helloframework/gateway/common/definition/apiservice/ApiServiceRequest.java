package org.helloframework.gateway.common.definition.apiservice;


import io.protostuff.Tag;

import java.util.Arrays;

/**
 * Created by lanjian
 */
public class ApiServiceRequest {

    @Tag(1)
    private String id;
    @Tag(2)
    private String appid;
    @Tag(3)
    private String api;
    @Tag(4)
    private String version;
    @Tag(5)
    private String nonce;
    @Tag(6)
    private String clientIp;
    @Tag(7)
    private byte[] data;
    @Tag(8)
    private String requestId;
    @Tag(9)
    private boolean sync = false;
    @Tag(10)
    private boolean mesh = false;
    @Tag(11)
    private String model;
    @Tag(12)
    private String path;
    @Tag(13)
    private String token;
    @Tag(14)
    private Boolean test;
    @Tag(15)
    private String source;

    public ApiServiceRequest(String model, String id, String requestId, String appid, String api, String version, String nonce, String path, String token, String source, Boolean test, String clientIp, byte[] data) {
        this.id = id;
        this.appid = appid;
        this.api = api;
        this.version = version;
        this.nonce = nonce;
        this.data = data;
        this.clientIp = clientIp;
        this.requestId = requestId;
        this.path = path;
        this.token = token;
        this.source = source;
        this.test = test;
    }


    public ApiServiceRequest(String model, String id, String requestId, String appid, String api, String version, String nonce, String clientIp, byte[] data) {
        this.id = id;
        this.appid = appid;
        this.api = api;
        this.version = version;
        this.nonce = nonce;
        this.data = data;
        this.clientIp = clientIp;
        this.requestId = requestId;
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isMesh() {
        return mesh;
    }

    public void setMesh(boolean mesh) {
        this.mesh = mesh;
    }

    public ApiServiceRequest() {

    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ApiServiceRequest{" +
                "id='" + id + '\'' +
                ", appid='" + appid + '\'' +
                ", api='" + api + '\'' +
                ", version='" + version + '\'' +
                ", nonce='" + nonce + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", data=" + Arrays.toString(data) +
                ", requestId='" + requestId + '\'' +
                ", sync=" + sync +
                ", mesh=" + mesh +
                ", model='" + model + '\'' +
                ", path='" + path + '\'' +
                ", token='" + token + '\'' +
                ", test=" + test +
                ", source='" + source + '\'' +
                '}';
    }
}
