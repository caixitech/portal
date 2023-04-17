package org.helloframework.core.utils;

/**
 * Created by lanjian
 */
public class ShortUtils {
    private static final String[] l = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};


    public final static String to62RadixString(long seq) {
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 62);
            sBuilder.append(l[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return sBuilder.toString();
    }

    public static void main(String[] a) {
        System.out.println(ShortUtils.to62RadixString(600907000005783769l));
        //http://100086.cn/s/NEegcIW9oI
        //http://10086.cn/x/y/z/decode(NEegcIW9oI)/a/b/c


        //NEegcIW9oI => http://10086.cn/x/y/z/600907000005783769l/a/b/c

        for (int i = 1000; i <= 10000; i++) {
            System.out.println(ShortUtils.to62RadixString(i));
        }
    }
}