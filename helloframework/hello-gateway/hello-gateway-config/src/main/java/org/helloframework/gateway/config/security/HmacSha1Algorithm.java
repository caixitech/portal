package org.helloframework.gateway.config.security;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * HMAC-SHA1签名算法
 *
 * @author sswang
 */
public class HmacSha1Algorithm extends SignatureAlgorithm {
    public final static String ENCODING = "UTF-8";
    /**
     * 算法内部名
     */
    private static final String ALGORITHM_NAME = "HmacSHA1";

    @Override
    public String signature(byte[] key, byte[] content) throws GeneralSecurityException {
        String sign = null;
        // 初始化算法
        SecretKey sKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Mac mac = Mac.getInstance(ALGORITHM_NAME);
        mac.init(sKey);
        // 执行签名
        byte[] buf = mac.doFinal(content);
        // 将结果用Base64编码
        try {
            sign = new String(Base64.encodeBase64(buf), ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return sign;
    }

}
