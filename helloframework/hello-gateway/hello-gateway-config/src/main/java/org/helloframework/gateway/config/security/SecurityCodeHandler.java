package org.helloframework.gateway.config.security;

/**
 * Created by lanjian
 */
public interface SecurityCodeHandler {
    String generateSecurityCode(byte[] content);

    boolean securityCodeCheck(String securityCode);

}
