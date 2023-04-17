package org.helloframework.mybatis.definition;



import org.helloframework.core.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanjian on 2016/12/7.
 */
public class DBSql {

    private final static Map<Class, SqlBaseDao> daoMap = new HashMap<Class, SqlBaseDao>();

    public static void register(SqlBaseDao SqlBaseDao) {
        Class cls = ClassUtils.genericType(0, SqlBaseDao.getClass());
        SqlBaseDao dao = daoMap.get(cls);
        if (dao != null) {
            throw new RuntimeException("dao double register");
        }
        daoMap.put(cls, SqlBaseDao);
    }


    public static <T extends SqlBaseDao> T build(Class cls) {
        SqlBaseDao dao = daoMap.get(cls);
        if (dao == null) {
            throw new RuntimeException("dao not found");
        }
        return (T) dao;
    }
}
