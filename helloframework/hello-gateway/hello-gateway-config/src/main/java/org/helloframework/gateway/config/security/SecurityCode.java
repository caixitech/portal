package org.helloframework.gateway.config.security;

import org.helloframework.codec.json.JSON;
import org.helloframework.gateway.common.utils.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lanjian
 */
public class SecurityCode {
    private static final String chartset = "utf-8";
    private static final String aesPwd = "Q*rNd@%zwa1Oo4u0";
    private String password;
    private String key;
    private Long time;


    public SecurityCode(String password, String key, Long time) {
        this.password = password;
        this.key = key;
        this.time = time;
    }

    /**
     * 暂时先用日期时间实现,后续改造发号器
     *
     * @return
     */
    private static String dynamicPwd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        //每1天换一次密码
        String pwd = DigestUtils.md5(aesPwd + date);
        pwd = pwd.substring(0, 16);
        return pwd;
    }


    public String generate() {
        try {
            String json = JSON.toJSONString(this);
            byte[] _content = SecurityHelper.encryptAES(dynamicPwd(), json.getBytes(chartset));
            return DigestUtils.encodeBase64(_content);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static SecurityCode from(String securityCode) {
        try {
            byte[] content = securityCode.getBytes(chartset);
            byte[] content_ = SecurityHelper.decryptAES(dynamicPwd(), DigestUtils.decodeBase64(content));
            return JSON.parseObject(new String(content_, chartset), SecurityCode.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 60秒有效
     *
     * @return
     */
    public boolean valid() {
        Long now = System.currentTimeMillis();
        Long timeDiff = now - time;
        if (timeDiff < 60 * 1000) {
            return true;
        }
        return false;
    }
}
