package org.helloframework.core.utils;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

/**
 * @author lanjian
 */
public class UUIDUtils {


    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp;
        temp = str.replaceAll("-", "");
        return temp;
    }

    // 获得指定数量的UUID
    public static String[] uuids(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = uuid();
        }
        return ss;
    }

    public static String sn(String uuid) {
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, 12);
    }

    public static String sn(String uuid, Integer num) {
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, num);
    }


    public static String sn() {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, 12);
    }

    public static String sn(int num) {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, num);
    }

    public static String snByDay(int num) {
        return String.format("%s%s", DateUtils.dateToString(new Date(), "yyyyMMdd"), sn(num));
    }
}
