package org.helloframework.gateway.config.security;

import java.security.GeneralSecurityException;

public abstract class SignatureAlgorithm {
    /**
     * 对指定内容使用指定的密钥进行签名
     *
     * @param key     密钥
     * @param content 需要签名的内容
     * @return
     */
    abstract public String signature(byte[] key, byte[] content) throws GeneralSecurityException;
}
