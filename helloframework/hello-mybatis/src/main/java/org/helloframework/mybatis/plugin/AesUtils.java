package org.helloframework.mybatis.plugin;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class AesUtils {
    private static final Logger log = LoggerFactory.getLogger(AesUtils.class);
    private final static String SECRETKEY = "7cAF36FA";

    public static String encrypt(String target) {
        try {
            return parseByte2HexStr(encryptAES(SECRETKEY, target.getBytes("UTF-8")));
        } catch (Exception ex) {
            log.info("", ex);
        }
        return null;
    }

    public static String decrypt(String target) {
        try {
            if (StringUtils.isEmpty(target)) {
                return target;
            }
            return new String(decryptAES(SECRETKEY, parseHexStr2Byte(target)));
        } catch (Exception ex) {
        }
        return null;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;

    }


    /**
     * 使用指定的key通过AES算法加密指定的content
     *
     * @param key
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] encryptAES(String key, byte[] content) throws Exception {
        byte[] enCodeFormat = generateSecret(key, SALT, 16);
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES" + ECB_PKCS5_PADDING);// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化加密
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final byte[] SALT = new byte[]{5, 45, 89, -34, -76, 53, 97, 58, 30, -71, -98, -60, 123, 45, -78, -39, 63, -38, -69, 48, 59, 74, 58, 44, 39, 89, 82, 74, 37, 42, 89, 75, 86, 27, 2, 57, 34, 8, 43, 75, 89, 3, 79, 35, 77, 29, 8, 2, 8, 02, 02, 82, 39, 83, 7, 93, 6, 79, 84, 8, 97, 9, 83, 5, 73, 9, 87, 3, 43, 53, 89, 7, 06, 54, 73, 79, 89, 6, 80, 56, 8, 6, 7, 97, 8, 90, 23, -05};
    public static final String ECB_PKCS5_PADDING = "/ECB/PKCS5Padding";


    /**
     * 使用指定的key通过AES算法解密指定的content
     *
     * @param key
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] decryptAES(String key, byte[] content) throws Exception {
        byte[] enCodeFormat = generateSecret(key, SALT, 16);
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES" + ECB_PKCS5_PADDING);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化解密
        byte[] result = cipher.doFinal(content);
        return result;
    }

    private static byte[] generateSecret(String password, byte[] salt, int length) throws UnsupportedEncodingException {
        byte[] result = new byte[length];
        byte[] key = password.getBytes(DEFAULT_CHARSET);
        System.arraycopy(key, 0, result, 0, Math.min(result.length, key.length));
        if (key.length < result.length) {
            System.arraycopy(salt, 0, result, key.length, Math.min(salt.length, length - key.length));
        }
        return result;
    }

    public static boolean isAesStr(Object value) {
        if (value instanceof String && value != null && !Objects.equals(value, "") && decrypt((String) value) == null) {
            return true;
        }
        return false;
    }

    public static boolean isNULL(Object value) {
        if (value instanceof String && value != null && !Objects.equals(value, "")) {
            return false;
        }
        return true;
    }
}
