package org.helloframework.gateway.config.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 签名的帮助类
 *
 * @author sswang
 */
public class SecurityHelper {
    /**
     * Java密钥库(Java Key Store，JKS)KEY_STORE
     */
    public static final String KEY_STORE = "JKS";

    public static final String X509 = "X.509";

    public static final byte[] SALT = new byte[]{5, 45, 89, -34, -76, 53, 97, 58, 30, -71, -98, -60, 123, 45, -78, -39, 63, -38, -69, 48, 59, 74, 58, 44, 39, 89, 82, 74, 37, 42, 89, 75, 86, 27, 2, 57, 34, 8, 43, 75, 89, 3, 79, 35, 77, 29, 8, 2, 8, 02, 02, 82, 39, 83, 7, 93, 6, 79, 84, 8, 97, 9, 83, 5, 73, 9, 87, 3, 43, 53, 89, 7, 06, 54, 73, 79, 89, 6, 80, 56, 8, 6, 7, 97, 8, 90, 23, -05};
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 244;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;
    public static final String ECB_PKCS1_PADDING = "/ECB/PKCS1Padding";
    public static final String ECB_PKCS5_PADDING = "/ECB/PKCS5Padding";

    /**
     * 校验签名是否正确
     *
     * @param args            参与签名的参数列表
     * @param signature       客户端提供的签名值
     * @param consumerSecret  客户端secret
     * @param signatureMethod 签名算法
     * @return 签名值是否匹配
     */
    public static boolean checkSignature(SortedMap<String, String> args, String signature, String consumerSecret, String signatureMethod) throws Exception {

        // 获取签名算法
        SignatureAlgorithm algorithm = SignatureAlgorithmFactory.getAlgorithm(signatureMethod.toLowerCase());
        if (algorithm == null) {
            throw new Exception("未找到指定的签名算法类, signatureMethod: " + signatureMethod);
        }
        String signatureString = buildSortedArgsString(args);
        // 进行签名处理
        try {
            String signatureResult = algorithm.signature(consumerSecret.getBytes("UTF-8"), signatureString.getBytes("UTF-8"));
            boolean result = signatureResult.equals(signature);
            if (!result) {
                throw new Exception("计算的签名值为: " + signatureResult + "，应用传递的签名值为: " + signature);
            }
            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 生成签名值，返回null 则表明签名过程中产生错误
     *
     * @param consumerSecret
     * @param signatureMethod
     * @param timestamp       Unix时间戳，从1970-01-01到现在的秒数
     * @param version
     * @param isSigArgs
     * @param args
     * @return
     */
    public static String signature(byte[] consumerSecret, String signatureMethod, long timestamp, String nonce, String version, boolean isSigArgs, Map<String, String> args) throws Exception {
        SignatureAlgorithm algorithm = SignatureAlgorithmFactory.getAlgorithm(signatureMethod.toLowerCase());
        if (algorithm == null) {
            throw new Exception("未找到指定的签名算法类, signatureMethod: " + signatureMethod);
        }

        SortedMap<String, String> argsToSignature = buildSignatureMap(nonce, signatureMethod, timestamp, version, isSigArgs, args);
        String argsString = buildSortedArgsString(argsToSignature);
        // 进行签名处理
        try {
            String signatureResult = algorithm.signature(consumerSecret, argsString.getBytes("UTF-8"));
            return signatureResult;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String signature(String consumerSecret, String signatureMethod, long timestamp, String nonce, String version, boolean isSigArgs, Map<String, String> args) throws Exception {
        return signature(consumerSecret.getBytes("UTF-8"), signatureMethod, timestamp, nonce, version, isSigArgs, args);
    }

    /**
     * 生成签名值 timestamp获取当前机器系统时间 返回null 则表明签名过程中产生错误
     *
     * @param consumerSecret
     * @param isSigArgs
     * @param args
     * @return
     */
    public static String signature(String consumerSecret, long timestamp, String nonce, boolean isSigArgs, Map<String, String> args) throws Exception {
        String version = "1.0";
        return signature(consumerSecret, SignatureAlgorithmFactory.ALGORITHM_HMAC_SHA1, timestamp, nonce, version, isSigArgs, args);
    }

    /**
     * 生成签名值, 使用HMAC-SHA1签名算法，timestamp获取当前机器系统时间，业务参数不参与鉴权 返回null 则表明签名过程中产生错误
     *
     * @param consumerSecret
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String signature(String consumerSecret, long timestamp, String nonce) throws Exception {
        return signature(consumerSecret, timestamp, nonce, false, null);
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

    /**
     * 通过用户密钥生成指定长度密钥
     *
     * @param password
     * @param salt
     * @param length
     * @return
     */
    private static byte[] generateSecret(String password, byte[] salt, int length) throws UnsupportedEncodingException {
        byte[] result = new byte[length];
        byte[] key = password.getBytes(DEFAULT_CHARSET);
        System.arraycopy(key, 0, result, 0, Math.min(result.length, key.length));
        if (key.length < result.length) {
            System.arraycopy(salt, 0, result, key.length, Math.min(salt.length, length - key.length));
        }
        return result;
    }

    public final static int ENCRYPT_KEY_LENGTH = 16;
    public final static int ENCRYPT_CHECKINFO_LENGTH = 32;
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 由KeyStore获得私钥
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String alias, String password) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, password);
        PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
        return key;
    }

    /**
     * 由Certificate获得公钥
     *
     * @param certificateStream
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(InputStream certificateStream) throws Exception {
        Certificate certificate = getCertificate(certificateStream);
        PublicKey key = certificate.getPublicKey();
        return key;
    }

    /**
     * 获得Certificate
     *
     * @param certificateStream
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(InputStream certificateStream) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);

        Certificate certificate = certificateFactory.generateCertificate(certificateStream);

        return certificate;
    }

    /**
     * 获得Certificate
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath, String alias, String password) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, password);
        Certificate certificate = ks.getCertificate(alias);

        return certificate;
    }

    /**
     * 获得KeyStore
     *
     * @param keyStorePath
     * @param password
     * @return
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance(KEY_STORE);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) throws Exception {
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm() + ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        int length = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] buf;
        // 对数据分段加密
        while (length - offset > 0) {
            if (length - offset > MAX_ENCRYPT_BLOCK) {
                buf = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                buf = cipher.doFinal(data, offset, length - offset);
            }
            out.write(buf, 0, buf.length);
            offset += MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }

    /**
     * 私钥解密
     *
     * @param data
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) throws Exception {
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm() + ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int length = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] buf;
        // 对数据分段解密
        while (length - offset > 0) {
            if (length - offset > MAX_DECRYPT_BLOCK) {
                buf = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
            } else {
                buf = cipher.doFinal(data, offset, length - offset);
            }
            out.write(buf, 0, buf.length);
            offset += MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }

    /**
     * 公钥加密
     *
     * @param data
     * @param certificateStream
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, InputStream certificateStream) throws Exception {

        // 取得公钥
        PublicKey publicKey = getPublicKey(certificateStream);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm() + ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int length = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] buf;
        // 对数据分段加密
        while (length - offset > 0) {
            if (length - offset > MAX_ENCRYPT_BLOCK) {
                buf = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                buf = cipher.doFinal(data, offset, length - offset);
            }
            out.write(buf, 0, buf.length);
            offset += MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param certificateStream
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, InputStream certificateStream)
            throws Exception {
        // 取得公钥
        PublicKey publicKey = getPublicKey(certificateStream);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm() + ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int length = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] buf;
        // 对数据分段解密
        while (length - offset > 0) {
            if (length - offset > MAX_DECRYPT_BLOCK) {
                buf = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
            } else {
                buf = cipher.doFinal(data, offset, length - offset);
            }
            out.write(buf, 0, buf.length);
            offset += MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }

    /**
     * 验证Certificate
     *
     * @param certificateStream
     * @return
     */
    public static boolean verifyCertificate(InputStream certificateStream) {
        return verifyCertificate(new Date(), certificateStream);
    }

    /**
     * 验证Certificate是否过期或无效
     *
     * @param date
     * @param certificateStream
     * @return
     */
    public static boolean verifyCertificate(Date date, InputStream certificateStream) {
        boolean status = true;
        try {
            // 取得证书
            Certificate certificate = getCertificate(certificateStream);
            // 验证证书是否过期或无效
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 验证证书是否过期或无效
     *
     * @param date
     * @param certificate
     * @return
     */
    private static boolean verifyCertificate(Date date, Certificate certificate) {
        boolean status = true;
        try {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 将参与校验的参数值进行排序生成SortedMap
     *
     * @param nonce
     * @param signatureMethod
     * @param timestamp
     * @param version
     * @param isCheckArgs
     * @param args
     * @return
     */
    public static SortedMap<String, String> buildSignatureMap(String nonce, String signatureMethod, long timestamp, String version, boolean isCheckArgs, Map<String, String> args) {
        SortedMap<String, String> argToSignatureMap;
        //
        argToSignatureMap = new TreeMap();
        argToSignatureMap.put(AUTHENTICATION_NONCE, nonce);
        argToSignatureMap.put(AUTHENTICATION_SIGNATURE_METHOD, signatureMethod);
        argToSignatureMap.put(AUTHENTICATION_TIMESTAMP, Long.toString(timestamp));
        argToSignatureMap.put(AUTHENTICATION_VERSION, version);
        // 如果校验业务参数，则将业务参数加入待签名验证参数列表
        if (isCheckArgs) {
            argToSignatureMap.putAll(args);
        }
        return argToSignatureMap;
    }

    /**
     * 请求唯一标识，32位不重复字符串
     */
    public final static String AUTHENTICATION_NONCE = "nonce";
    /**
     * 签名方法，默认为 hmac-sha1
     */
    public final static String AUTHENTICATION_SIGNATURE_METHOD = "sign_method";
    /**
     * 请求的Unix时间戳，从1970-01-01到现在的秒数
     */
    public final static String AUTHENTICATION_TIMESTAMP = "time";
    /**
     * 签名的版本号默认为1.0
     */
    public final static String AUTHENTICATION_VERSION = "ver";

    /**
     * 将SortedMap生成需要参与鉴权的参数字符串
     *
     * @param args
     * @return
     */
    private static String buildSortedArgsString(SortedMap<String, String> args) {
        // 构造需要进行签名计算的字符串
        StringBuilder stringToBeSignature = new StringBuilder();
        for (String key : args.keySet()) {
            stringToBeSignature.append(key);
            stringToBeSignature.append("=");
            stringToBeSignature.append(args.get(key));
            stringToBeSignature.append("&");
        }
        stringToBeSignature = stringToBeSignature.replace(stringToBeSignature.length() - 1, stringToBeSignature.length(), "");
        String signatureString = stringToBeSignature.toString().trim();
        return signatureString;
    }
}
