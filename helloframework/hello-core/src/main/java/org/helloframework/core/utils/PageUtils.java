package org.helloframework.core.utils;

public class PageUtils {
    //page从1开始
    public static Integer redisBegin(Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * size;
    }

    //page从1开始
    public static Integer redisEnd(Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        return page * size - 1;
    }
}
