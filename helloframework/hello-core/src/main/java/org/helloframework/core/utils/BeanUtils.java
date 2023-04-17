package org.helloframework.core.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yoqu
 * @date 2018/5/14 - 14:06
 */
public class BeanUtils {
    public static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    public static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    /**
     * 对象拷贝,
     *
     * @param source 原对象
     * @param target 赋值的对象
     */
    private static void copyProperties(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier = null;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    public static <T> T copyProperties(Class targetCls, Object... sources) {
        try {
            Object target = targetCls.newInstance();
            for (Object source : sources) {
                if (source == null) {
                    return null;
                }
                copyProperties(source, target);
            }
            return (T) target;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    /**
     * map -> bean
     *
     * @param target     类对象
     * @param properties map对象
     */
    public static <T> T populate(Class target, Map properties) {
        return populate(target, properties, false);
    }

    /**
     * map -> bean
     *
     * @param bean       类对象
     * @param properties map对象
     */
    public static void populate(Object bean, Map properties) {
        populate(bean, properties, false);
    }

    /**
     * map -> bean
     *
     * @param target       类对象
     * @param properties   map对象
     * @param isFirstUpper map中首字母是否大写
     */
    public static <T> T populate(Class target, Map properties,
                                 boolean isFirstUpper) {
        try {
            if ((target == null) || (properties == null)) {
                return null;
            }
            Object bean = target.newInstance();
            populate(bean, properties, isFirstUpper);
            return (T) bean;
        } catch (Exception e) {
            //抛出异常会影响整个数据
//            throw new RuntimeException(e);
            logger.error("populate对象转换异常", e);
            return null;
        }
    }

    /**
     * map -> bean
     *
     * @param bean         类对象
     * @param properties   map对象
     * @param isFirstUpper map中首字母是否大写
     */
    private static void populate(Object bean, Map properties,
                                 boolean isFirstUpper) {
        if ((bean == null) || (properties == null)) {
            return;
        }
        for (Object o : properties.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            try {
                String name = (String) entry.getKey();
                if (name == null) {
                    continue;
                }
                if (isFirstUpper) {
                    char pref = name.charAt(0);
                    name = name.replaceFirst(String.valueOf(pref),
                            String.valueOf(Character.toLowerCase(pref)));
                }
                if (entry.getValue() != null) {
                    BeanUtilsBean beanUtil = BeanUtilsBean.getInstance();
                    beanUtil.setProperty(bean, name, entry.getValue());
                }
            } catch (Exception ex) {
                logger.error("map对象属性转换异常,错误属性:{},错误值:{}", ((Map.Entry) o).getKey(), ((Map.Entry) o).getValue());
                //抛出异常会影响整个数据
                //                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * object -> map
     *
     * @param obj 待转换为map的对象
     * @return 返回新生成的map
     */
    public static Map<String, Object> describe(Object obj) {
        if (Map.class.isAssignableFrom(obj.getClass())) {
            return (Map<String, Object>) obj;
        }
        return describe(obj, false);
    }

    /**
     * object -> map
     *
     * @param obj 待转换为map的对象
     * @return 返回新生成的map
     */
    public static Map<String, Object> describe(Object obj, boolean isFirstUpper) {
        Map<String, Object> map = new HashMap<>();
        PropertyDescriptor[] descriptors = PropertyUtils
                .getPropertyDescriptors(obj);
        for (int i = 0; i < descriptors.length; ++i) {
            String name = descriptors[i].getName();
            if (descriptors[i].getReadMethod() != null)
                try {
                    if (!"class".equals(name)) {
                        Object value = descriptors[i].getReadMethod().invoke(obj);
                        if (isFirstUpper) {
                            char pref = name.charAt(0);
                            name = name.replaceFirst(String.valueOf(pref),
                                    String.valueOf(Character.toUpperCase(pref)));
                        }
                        if (value != null) {
                            map.put(name, value);
                        } /*else {
                            map.put(name, null);
                        }*/
                    }
                } catch (Exception ex) {

                }
        }
        return map;
    }


}
