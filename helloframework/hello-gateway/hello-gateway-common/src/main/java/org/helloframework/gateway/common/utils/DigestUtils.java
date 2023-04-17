package org.helloframework.gateway.common.utils;


import org.apache.commons.codec.binary.Base64;
import org.helloframework.gateway.common.exception.DigestException;

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
        byte[] _data = data.getBytes();
        try {
            return new String(Base64.decodeBase64(_data), ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String decodeBase64(String data, String encoding) {
        byte[] _data = data.getBytes();
        try {
            return new String(Base64.decodeBase64(_data), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String encodeBase64(String data) {
        byte[] _data = data.getBytes();
        try {
            return new String(Base64.encodeBase64(_data), ENCODING);
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
        byte[] _data = data.getBytes();
        try {
            return new String(Base64.encodeBase64(_data), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }

    public static String encodeBase64(String data, String byteEncoding, String encoding) {
        try {
            byte[] _data = data.getBytes(byteEncoding);
            return new String(Base64.encodeBase64(_data), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new DigestException("encoding unknow", e);
        }
    }


    public static String md5(String data) {
        String _data = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _data;
    }

    public static String md5(byte[] data) {
        String _data = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _data;
    }

    public static String md5(InputStream data) {
        String _data = null;
        try {
            _data = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _data;
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
