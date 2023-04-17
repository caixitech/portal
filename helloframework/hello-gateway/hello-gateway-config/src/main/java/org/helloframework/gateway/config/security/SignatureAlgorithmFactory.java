package org.helloframework.gateway.config.security;

public final class SignatureAlgorithmFactory {
    /**
     * 算法名：HMAC-SHA1
     */
    public static final String ALGORITHM_HMAC_SHA1 = "hmac-sha1";

    /**
     * 获取签名算法
     *
     * @param name 算法名，全小写
     * @return
     */
    public static SignatureAlgorithm getAlgorithm(String name) {
        SignatureAlgorithm sa = null;
        if (ALGORITHM_HMAC_SHA1.equals(name)) {
            sa = new HmacSha1Algorithm();
        }
        return sa;
    }
}
