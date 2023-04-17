
package org.helloframework.core.utils;


import org.apache.commons.codec.binary.Base64;
import org.helloframework.core.exception.DigestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * .
 * Creator: lanjian
 */
public class DigestUtils {
    public final static String ENCODING = "UTF-8";

    public static String decodeBase64(String data) {
        byte[] _date = data.getBytes();
        try {
            return new String(Base64.decodeBase64(_date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String decodeBase64(String data, String encoding) {
        byte[] _date = data.getBytes();
        try {
            return new String(Base64.decodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String encodeBase64(String data) {
        byte[] _date = data.getBytes();
        try {
            return new String(Base64.encodeBase64(_date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static byte[] encodeBase64ToByte(byte[] data) {
        try {
            return Base64.encodeBase64(data);
        } catch (Exception e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String encodeBase64(byte[] date) {
        try {
            return new String(Base64.encodeBase64(date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static byte[] decodeBase64(byte[] date) {
        return Base64.decodeBase64(date);
    }


    public static String encodeBase64(String data, String encoding) {
        byte[] _date = data.getBytes();
        try {
            return new String(Base64.encodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String encodeBase64(String data, String byteEncoding, String encoding) {
        try {
            byte[] _date = data.getBytes(byteEncoding);
            return new String(Base64.encodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }


    public static String md5(String data) {
        String _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _date;
    }

    public static String md5(byte[] data) {
        String _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _date;
    }

    public static String md5(InputStream data) {
        String _date = null;
        try {
            _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _date;
    }


//    public static String sha1(String data) {
//        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(data);
//    }


    public static String urlEncode(String data, String encoding) {
        try {
            return URLEncoder.encode(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String urlDecode(String data, String encoding) {
        try {
            return URLDecoder.decode(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

}
