package org.helloframework.gateway.core.controller;


import org.helloframework.codec.json.JSON;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceHeader;
import org.helloframework.gateway.common.definition.apiservice.ApiServiceRequest;
import org.helloframework.gateway.common.definition.apiservice.plugins.impl.sign.SignUtils;
import org.helloframework.gateway.common.definition.base.Sender;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.DigestUtils;
import org.helloframework.gateway.common.utils.WebToolsUtils;
import org.helloframework.gateway.config.spring.factory.ApiGwConfig;
import org.helloframework.gateway.core.exception.ServiceNoProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping({"/"})
public class GatewayController {
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private Sender sender;

    @Autowired
    private ApiGwConfig gateWayConfig;

//    @Resource
//    private TokenValidater tokenValidater;


    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(
            value = {"/h5", "h5/"},
            method = {RequestMethod.POST, RequestMethod.GET}
    )
    public byte[] h5(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam(value = "callback", required = false) String callback,
                     @RequestParam(value = "appid", required = false) String appid,
                     @RequestParam(value = "version", required = false) String version,
                     @RequestParam(value = "api", required = false) String api,
                     @RequestParam(value = "timestamp", required = false) Long timestamp,
                     @RequestParam(value = "signature", required = false) String signature,
                     @RequestParam(value = "nonce", required = false) String nonce,
                     @RequestParam(value = "model", required = false) String model,
                     @RequestParam(value = "charset", required = false) String charset,
                     @RequestParam(value = "data", required = false) String data) throws IOException {

        log.info("##### " + api + "json:###" + data);
        String source = new String(appid + version + api + timestamp + nonce + DigestUtils.md5(data) + this.gateWayConfig.getAppSecretKeyMap().get(appid));
        String sign = DigestUtils.md5(source);
        if (!sign.equals(signature)) {
            response.setHeader("X-Ca-Code", GateWayCode.GW_CODE.getCode());
            response.setHeader("X-Ca-Message", GateWayCode.GW_CODE.getMsg());
            response.setStatus(403);
            return null;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        byte[] result = this.handle(model, false, false, request, response, appid, version, api, timestamp, signature, nonce, charset, "POST", data.getBytes());
        stopWatch.stop();
        if (result != null) {
            log.info("##### " + api + " -----" + stopWatch.getTotalTimeMillis() + "millis,resp length:" + result.length + ", Nonce " + nonce + "  #####");
        } else {
            log.info("##### " + api + " -----" + stopWatch.getTotalTimeMillis() + "millis, Nonce " + nonce + "  #####");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(callback)) {
            StringBuffer dataNew = new StringBuffer(callback);
            dataNew.append("(");
            dataNew.append(new String(result));
            dataNew.append(")");
            return dataNew.toString().getBytes();
        }
        return result;
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(
            value = {"/gateway", "gateway/"},
            method = {RequestMethod.POST, RequestMethod.GET},
            headers = {"Content-Type=application/json"},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public byte[] gateway(HttpServletRequest request, HttpServletResponse response,
                          @RequestHeader(value = "X-Ca-Appid", required = false) String appid,
                          @RequestHeader(value = "X-Ca-Version", required = false) String version,
                          @RequestHeader(value = "X-Ca-Api", required = false) String api,
                          @RequestHeader(value = "X-Ca-Timestamp", required = false) Long timestamp,
                          @RequestHeader(value = "X-Ca-Signature", required = false) String sign,
                          @RequestHeader(value = "X-Ca-Nonce", required = false) String nonce,
                          @RequestHeader(value = "X-Ca-Charset", required = false) String charset,
                          @RequestHeader(value = "X-Ca-Model", required = false) String model,
                          @RequestBody(required = false) byte[] data) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("##### " + api + "json:###" + new String(data));
        byte[] result = this.handle(model, false, false, request, response, appid, version, api, timestamp, sign, nonce, charset, "POST", data);
        stopWatch.stop();
        if (result != null) {
            log.info("##### " + api + " -----" + stopWatch.getTotalTimeMillis() + "millis,resp length:" + result.length + ", Nonce " + nonce + "  #####");
        } else {
            log.info("##### " + api + " -----" + stopWatch.getTotalTimeMillis() + "millis, Nonce " + nonce + "  #####");
        }
        return result;
    }


    private byte[] handle(String model, boolean replyCheck, boolean signCheck, HttpServletRequest request, HttpServletResponse response, String appid, String version, String api, Long timestamp, String sign, String nonce, String charset, String method, byte[] data) throws IOException {
        if (this.sender == null) {
            throw new IllegalArgumentException("gwSender is null");
        }
        try {
            String requestId = UUID.randomUUID().toString();
            response.setHeader("X-Ca-Request-Id", requestId);
            if (StringUtils.isEmpty(nonce)) {
                nonce = " ";
            }
            //消息体不能为空
            if (data == null || data.length == 0) {
                log.error(String.format("data body is empty,service：%s,version:%s,tc:%s", api, version, nonce));
                response.setHeader("X-Ca-Code", GateWayCode.CODEC_CODE.getCode());
                response.setHeader("X-Ca-Message", GateWayCode.CODEC_CODE.getMsg());
                return null;
            }
            ApiServiceRequest apiServiceRequest = new ApiServiceRequest(model, UUID.randomUUID().toString(), requestId, appid, api, version, nonce, WebToolsUtils.ip(request), data);
            ApiServiceHeader apiServiceHeader = new ApiServiceHeader(replyCheck, signCheck, timestamp, sign, this.gateWayConfig.getAppSecretKeyMap().get(appid), request.getServletPath(), method, SignUtils.extraHeaders(request));
            log.debug("request:{},header:{}", apiServiceRequest.toString(), apiServiceHeader.toString());
            byte[] res = this.sender.send(apiServiceRequest, apiServiceHeader);
            response.setHeader("X-Ca-Code", GateWayCode.SUCC_CODE.getCode());
            response.setHeader("X-Ca-Message", GateWayCode.SUCC_CODE.getMsg());
            return res;
        } catch (ServiceNoProviderException exception) {
            log.error(String.format("service not found ,service：%s,version:%s,tc:%s,%s", api, version, nonce, exception.getMessage()), exception);
            response.setHeader("X-Ca-Code", GateWayCode.NO_PROVIDER_CODE.getCode());
            response.setHeader("X-Ca-Message", GateWayCode.NO_PROVIDER_CODE.getMsg());
            return error(GateWayCode.NO_PROVIDER_CODE.getCode(), GateWayCode.NO_PROVIDER_CODE.getMsg());
        } catch (ApiException exception) {
            log.error(String.format("service：%s,version:%s,tc:%s,code:%s,msg:%s,%s", api, version, nonce, exception.getCode(), exception.getMsg(), exception.getMessage()), exception);
            response.setHeader("X-Ca-Code", exception.getCode());
//            response.setHeader("X-Ca-Message", exception.getMsg());
            response.setHeader("X-Ca-Message", URLEncoder.encode(exception.getMsg(), charset == null ? "UTF-8" : charset));
            return error(exception.getCode(), exception.getMsg());
        } catch (Throwable exception) {
            log.error(String.format("service：%s,version:%s,tc:%s,%s", api, version, nonce, exception.getMessage()), exception);
            response.setHeader("X-Ca-Code", GateWayCode.GW_CODE.getCode());
            response.setHeader("X-Ca-Message", GateWayCode.GW_CODE.getMsg());
            return error(GateWayCode.GW_CODE.getCode(), GateWayCode.GW_CODE.getMsg());
        }
    }

    private byte[] error(String code, String msg) {
        Map map = new HashMap();
        map.put("ret_code", code);
        map.put("ret_msg", msg);
        return JSON.toJSONString(map).getBytes();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/time"},
            method = {RequestMethod.GET}
    )
    public Long time() {
        return System.currentTimeMillis();
    }
}


