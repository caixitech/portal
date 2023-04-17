package org.helloframework.mybatis.plugin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.helloframework.mybatis.annotations.Column;
import org.helloframework.mybatis.annotations.Table;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Component
public class AesOutPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }
        if (result instanceof ArrayList) {
            ArrayList resultList = (ArrayList) result;
            if (CollectionUtils.isNotEmpty(resultList) && needToDecrypt(resultList.get(0))) {
                Field[] fields = resultList.get(0).getClass().getDeclaredFields();
                for (int i = 0; i < resultList.size(); i++) {
                    Object obj = resultList.get(i);
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Column column = field.getAnnotation(Column.class);
                        if (!Objects.isNull(column) && column.ase() && !AesUtils.isNULL(field.get(obj))) {
                            field.set(obj, AesUtils.decrypt((String) field.get(obj)));
                        }
                    }
                }
            }
        } else {
            if (needToDecrypt(result)) {
                AesUtils.decrypt((String) result);
            }
        }
        return result;
    }

    public boolean needToDecrypt(Object object) {
        if (object == null) {
            return false;
        }
        Class<?> objectClass = object.getClass();
        Table table = AnnotationUtils.findAnnotation(objectClass, Table.class);
        if (Objects.nonNull(table) && table.ase()) {
            return true;
        }
        return false;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
