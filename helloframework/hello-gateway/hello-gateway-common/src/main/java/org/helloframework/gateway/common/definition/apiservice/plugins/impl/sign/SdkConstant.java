package org.helloframework.gateway.common.definition.apiservice.plugins.impl.sign;

import java.nio.charset.Charset;

/**
 * Created by fred on 2016/12/19.
 */
public class SdkConstant {
    //签名Header
    public static final String CLOUDAPI_X_CA_SIGNATURE = "X-Ca-Signature";
    //所有参与签名的Header
    public static final String CLOUDAPI_X_CA_SIGNATURE_HEADERS = "X-Ca-Signature-Headers";
    //请求时间戳
    public static final String CLOUDAPI_X_CA_TIMESTAMP = "X-Ca-Timestamp";
    //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
    public static final String CLOUDAPI_X_CA_NONCE = "X-Ca-Nonce";
    //APP KEY
    public static final String CLOUDAPI_X_CA_KEY = "X-Ca-Key";
    //签名版本号
    public static final String CLOUDAPI_X_CA_VERSION = "CA_VERSION";

    //编码UTF-8
    public static final Charset CLOUDAPI_ENCODING = Charset.forName("UTF-8");

    //Header头的编码
    public static final Charset CLOUDAPI_HEADER_ENCODING = Charset.forName("ISO-8859-1");

    //UserAgent
    public static final String CLOUDAPI_USER_AGENT = "ALIYUN-ANDROID-DEMO";
    //换行符
    public static final String CLOUDAPI_LF = "\n";

    //参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
    public static final String CLOUDAPI_CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "X-Ca-";
    //签名版本号
    public static final String CLOUDAPI_CA_VERSION_VALUE = "1";

    public static final String COMMA = ",";

    public static final String DEFAULT_CLOUDAPI_X_CA_SIGNATURE_HEADERS =
            "X-Ca-Version,X-Ca-Appid,X-Ca-Timestamp, X-Ca-Nonce,  X-Ca-Api";
}
