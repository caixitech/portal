package org.helloframework.gateway.common.definition.apiservice;


public class MeshRequest {
    private String api;
    private String version;
    private String model;
    private Object data;
    private ApiExtend extend;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public static MeshRequest create(String api, Object data, ApiExtend extend) {
        return create(api, null, "1.0", data, extend);
    }
    public static MeshRequest create(String api, String model, Object data, ApiExtend extend) {
        return create(api, model, "1.0", data, extend);
    }

    public static MeshRequest create(String api, String model, String version, Object data, ApiExtend extend) {
        MeshRequest request = new MeshRequest();
        request.setVersion(version);
        request.setExtend(extend);
        request.setApi(api);
        request.setData(data);
        request.setModel(model);
        return request;
    }


    public ApiExtend getExtend() {
        return extend;
    }

    public void setExtend(ApiExtend extend) {
        this.extend = extend;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
