package org.helloframework.mybatis.query.builder;

import org.apache.commons.lang3.StringUtils;
import org.helloframework.core.annotation.MappedSuperclass;
import org.helloframework.mybatis.annotations.Column;
import org.helloframework.mybatis.annotations.Id;
import org.helloframework.mybatis.annotations.Table;
import org.helloframework.mybatis.definition.EntityColumnDefinition;
import org.helloframework.mybatis.definition.EntityDefinition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public abstract class BaseQuery extends HashMap {

    private final static HashMap<Class, EntityDefinition> entityDefinitions = new HashMap<Class, EntityDefinition>();


    private final static EntityDefinition table(Class clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            EntityDefinition entityDefinition = new EntityDefinition();
            Table table = (Table) clazz.getAnnotation(Table.class);
            entityDefinition.setTable(table.table());
            entityDefinition.setTableAs(table.tableAs());
            entityDefinition.setJoinTable(table.joinTable());
            entityDefinition.setJoinTableAs(table.joinTableAs());
            entityDefinition.setJoinTableOn(table.joinTableOn());
            entityDefinition.setTableOn(table.tableOn());
            entityDefinition.setJoinType(table.joinType());
            entityDefinition.setSql(table.sql());
            return entityDefinition;
        }
        throw new RuntimeException("not  table");
    }

    private final static void registerColumns(List<Field> fields, EntityDefinition entityDefinition) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                EntityColumnDefinition columnDefinition = new EntityColumnDefinition();
                if (StringUtils.isBlank(column.column())) {
                    columnDefinition.setColumn(field.getName());
                } else {
                    columnDefinition.setColumn(column.column());
                }
                columnDefinition.setJavaColumn(field.getName());
                columnDefinition.setType(field.getType());
                columnDefinition.setTableAs(column.tableAs());
                columnDefinition.setLength(column.length());
                columnDefinition.setNullable(column.nullable());
                columnDefinition.setDesc(column.desc());
                columnDefinition.setTransfer(column.transfer());
                columnDefinition.setAes(column.ase());
                if (field.isAnnotationPresent(Id.class)) {
                    Id id = field.getAnnotation(Id.class);
                    columnDefinition.setId(true);
                    columnDefinition.setGenerated(id.generated());
                    entityDefinition.setGetGenerated(id.generated());
                    entityDefinition.setIdColumn(columnDefinition.getColumn());
                    entityDefinition.setIdJavaColumn(columnDefinition.getJavaColumn());
                    entityDefinition.setIdJavaType(columnDefinition.getType());
                }
                entityDefinition.addEntityColumnDefinition(field.getName(), columnDefinition);
            }
        }
    }

    protected final static List<Field> allFields(Class cls) {
        List<Field> all = new ArrayList<Field>();
        Class tmpClass = cls;
        while (tmpClass != null) {
            all.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            Class superClass = tmpClass.getSuperclass();
            if (superClass != null && superClass.isAnnotationPresent(MappedSuperclass.class)) {
                tmpClass = superClass;
            } else {
                break;
            }
        }
        return all;
    }

    public final static EntityDefinition resolve(Class clazz) {
        EntityDefinition entityDefinition = table(clazz);
        registerColumns(allFields(clazz), entityDefinition);
        return entityDefinition;
    }

    private Class clazz = null;

    public BaseQuery(Class clazz) {
        if (clazz == null) {
            throw new RuntimeException("class is null");
        }
        synchronized (this) {
            if (!entityDefinitions.containsKey(clazz)) {
                entityDefinitions.put(clazz, resolve(clazz));
            }
        }
        this.clazz = clazz;
    }

    public EntityDefinition findEntityDefinition() {
        EntityDefinition entityDefinition = entityDefinitions.get(clazz);
        if (entityDefinition == null) {
            entityDefinition = resolve(clazz);
            entityDefinitions.put(clazz, entityDefinition);
        }
        return entityDefinition;
    }


    public Class getClazz() {
        return clazz;
    }
}
