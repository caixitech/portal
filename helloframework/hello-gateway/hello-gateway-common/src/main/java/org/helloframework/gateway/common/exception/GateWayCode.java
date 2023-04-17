package org.helloframework.gateway.common.exception;

/**
 * Created by lanjian on 2017/11/9.
 */
public class GateWayCode {
    private String msg;
    private String code;
    public final static GateWayCode SUCC_CODE = new GateWayCode("0000", "SUCC");

    //网关异常
    public final static GateWayCode GW_CODE = new GateWayCode("9001", "API_SERVICE_INNER_INVALID");
    //数据编解码异常
    public final static GateWayCode CODEC_CODE = new GateWayCode("9002", "DATA_SERIALIZATION_INVALID");
    //签名异常
    public final static GateWayCode SIGN_CODE = new GateWayCode("9003", "SIGN_CHECK_INVALID");
    //包重放
    public final static GateWayCode REPLAY_CODE = new GateWayCode("9004", "REQUEST_NONCE_INVALID");
    //限流异常
    public final static GateWayCode REQUEST_MAX_CODE = new GateWayCode("9005", "REQUEST_MAX");
    //超时异常
    public final static GateWayCode TIME_OUT_CODE = new GateWayCode("9006", "REQUEST_TIMEOUT");
    //网关下线
    public final static GateWayCode GW_OFFLINE_CODE = new GateWayCode("9007", "GW_OFFLINE_CODE");

    public final static GateWayCode NO_PROVIDER_CODE = new GateWayCode("9008", "NO_PROVIDER_CODE");

    public final static GateWayCode FIELD_VALID_ERROR = new GateWayCode("9009", "FIELD_VALID_ERROR");
    public final static GateWayCode MESH_ERROR_CODE = new GateWayCode("9010", "MESH_ERROR");
    public final static GateWayCode DATA_NO_EXITS = new GateWayCode("4004", "DATA_NO_EXITS");
    public final static GateWayCode HTTP_PROXY_ERROR_CODE = new GateWayCode("9011", "HTTP_PROXY_ERROR_CODE");


    public final static GateWayCode GRAPH_ERROR = new GateWayCode("9012", "GRAPH NOT FOUND");
    public final static GateWayCode GRAPH_INVOKER_ERROR = new GateWayCode("9013", "GRAPH INVOKER NOT FOUND");
    public final static GateWayCode GRAPH_CLASS_ERROR = new GateWayCode("9014", "GRAPH CLASS NOT FOUND");
    public final static GateWayCode GRAPH_CRITERIA_ERROR = new GateWayCode("9015", "GRAPH CRITERIA NOT FOUND");
    public final static GateWayCode GRAPH_OP_ERROR = new GateWayCode("9016", "GRAPH OP NOT FOUND");
    public final static GateWayCode GRAPH_OP_NOT_MATCH_ERROR = new GateWayCode("9017", "GRAPH OP NOT MATCH");
    public final static GateWayCode SMS_SEND_ERROR = new GateWayCode("9020", "SMS_SEND_ERROR");
    public final static GateWayCode RSA_ERROR = new GateWayCode("9013", "SECRET_ERROR");


    public GateWayCode(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
