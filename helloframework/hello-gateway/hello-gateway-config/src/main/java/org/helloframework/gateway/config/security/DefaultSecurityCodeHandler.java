package org.helloframework.gateway.config.security;

import java.util.Arrays;

/**
 * Created by lanjian
 */
public class DefaultSecurityCodeHandler implements SecurityCodeHandler {
    private final static int ENCRYPT_KEY_LENGTH = 16;
    private final static int ENCRYPT_CHECK_INFO_LENGTH = 32;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private String keyStorePath;
    private String alias;
    private String password;


    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String generateSecurityCode(byte[] content) {
        try {
            byte[] decrypt = SecurityHelper.decryptByPrivateKey(content, keyStorePath, alias, password);
            int start = 0;
            int end = ENCRYPT_KEY_LENGTH;
            byte[] aesPassword = Arrays.copyOfRange(decrypt, start, end);
            String aesPasswordString = new String(aesPassword, DEFAULT_CHARSET);
            end += 1;
            start = end;
            end += ENCRYPT_KEY_LENGTH;
            String appCodeString = new String(Arrays.copyOfRange(decrypt, start, end), DEFAULT_CHARSET);
            start = end;
            end += ENCRYPT_CHECK_INFO_LENGTH;
            byte[] check = Arrays.copyOfRange(decrypt, start, end);
            start = end;
            end = decrypt.length;
            byte[] encrypt = Arrays.copyOfRange(decrypt, start, end);
            byte[] thisCheck = SecurityHelper.decryptAES(aesPasswordString, encrypt);
            boolean success = Arrays.equals(check, thisCheck);
            if (success) {
                //生成防伪码
                SecurityCode securityCode = new SecurityCode(aesPasswordString, appCodeString, System.currentTimeMillis());
                return securityCode.generate();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public boolean securityCodeCheck(String securityCodeStr) {
        SecurityCode securityCode = SecurityCode.from(securityCodeStr);
        return securityCode.valid();
    }
}
