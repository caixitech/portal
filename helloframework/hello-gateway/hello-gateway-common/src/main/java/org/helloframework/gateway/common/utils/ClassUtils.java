package org.helloframework.gateway.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lanjian
 */
public class ClassUtils {
    public static Class findenericType(Integer index, Class clazz) {
        try {
            ParameterizedType genType = (ParameterizedType) clazz.getGenericSuperclass();
            Type[] params = genType.getActualTypeArguments();
            return (Class) params[index];
        } catch (Exception ex) {
            throw new RuntimeException("not found");
        }
    }
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    public static ArrayList<Class> getAllClassByInterface(Class clazz){
//        ArrayList<Class> list = new ArrayList();
//        //判断是否是一个接口
//        if (clazz.isInterface()) {
//            try {
//                ArrayList<Class> allClass = getAllClass(clazz.getPackage().getName());
//                /**
//                 * 循环判断路径下的所有类是否实现了指定的接口
//                 * 并且排除接口类自己
//                 */
//                for (int i = 0; i < allClass.size(); i++) {
//                    /**
//                     * 判断是不是同一个接口
//                     * 该方法的解析，请参考博客：
//                     * http://blog.csdn.net/u010156024/article/details/44875195
//                     */
//                    if (clazz.isAssignableFrom(allClass.get(i))) {
//                        if (!clazz.equals(allClass.get(i))) {//自身并不加进去
//                            list.add(allClass.get(i));
//                        }else {
//
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("出现异常");
//            }
//        }else {
//            //如果不是接口不作处理
//        }
//        return list;
//    }


}
