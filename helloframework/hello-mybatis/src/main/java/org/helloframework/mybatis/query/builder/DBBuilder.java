package org.helloframework.mybatis.query.builder;

import org.apache.commons.beanutils.PropertyUtils;
import org.helloframework.core.dto.PageData;
import org.helloframework.core.utils.ShortUtils;
import org.helloframework.mybatis.cache.CacheCall;
import org.helloframework.mybatis.datasource.MultipleDataSource;
import org.helloframework.mybatis.definition.*;
import org.helloframework.mybatis.query.FieldMapper;
import org.helloframework.mybatis.query.IDDD;
import org.helloframework.mybatis.query.IEntityCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Author lanjian
 * Email  lanjian
 */
public class DBBuilder extends BaseQuery implements IEntityCriteria<DBBuilder>, IDDD {

    private final static Logger logger = LoggerFactory.getLogger(DBBuilder.class);

    public final static DBBuilder build(Class clazz) {
        return new DBBuilder(clazz);
    }

    protected DBBuilder(Class clazz) {
        super(clazz);
    }

    public Integer begin() {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * size;
    }

    private final static Integer DSIZE = 100;

    private Integer size = DSIZE;

    private Integer page = 1;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    private int i = 0;

    private String sum = null;

    private CacheCall cacheCall = null;

    public CacheCall getCacheCall() {
        return cacheCall;
    }


    public String getSum() {
        return sum;
    }

    public DBBuilder sum(String p) {
        this.sum = p;
        return this;
    }

    public List<String> getSelect() {
        return select;
    }

    private boolean changeDatasource = false;

    public DBBuilder changeDatasource(String sourceKey) {
        MultipleDataSource.changeDataSourceKey(sourceKey);
        this.changeDatasource = true;
        return this;
    }

    public DBBuilder cache(CacheCall cacheCall) {
        if (this.cacheCall != null) {
            throw new RuntimeException("cache call is double");
        }
        this.cacheCall = cacheCall;
        return this;
    }

    public DBBuilder page(Integer page) {
        if (page == null) {
            this.page = 1;
        }
        if (page <= 1) {
            this.page = 1;
        } else {
            this.page = page;
        }
        return this;
    }

    /**
     * 使用语法支持 还是执行多条sql
     *
     * @return
     */
    public DBBuilder duplicate() {
        return this;
    }

    public DBBuilder size(Integer size) {
        if (size == null) {
            this.size = DSIZE;
            return this;
        }
        if (size <= 0) {
            this.size = DSIZE;
        } else {
            this.size = size;
        }
        return this;
    }

    private List<Query> queries = null;

    private List<Order> orders = null;

    private List<String> select = null;

    private List<Update> updates = null;


    private Object insertTarget;

    public DBBuilder set(Object object) {
        if (!getClazz().equals(object.getClass())) {
            throw new RuntimeException("不支持该类");
        }
        this.insertTarget = object;
        EntityDefinition entityDefinition = findEntityDefinition();
        List<Field> all = allFields(getClazz());
        for (Field field : all) {
            EntityColumnDefinition entityColumnDefinition = entityDefinition.getColumnDefinitions().get(field.getName());
            if (entityColumnDefinition != null) {
                try {
                    Object obj = PropertyUtils.getProperty(object, entityColumnDefinition.getJavaColumn());
                    if (obj != null) {
                        addSet(field.getName(), obj);
                    }
                } catch (Exception e) {
                    logger.error("set object", e);
                }
            }
        }
        return this;
    }


    public List<Query> getQueries() {
        return queries;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    private void addOrder(Order order) {
        synchronized (this) {
            if (orders == null) {
                orders = new ArrayList<Order>();
            }
            orders.add(order);
        }
    }

    public DBBuilder set(String property, Object value) {
        addSet(property, value);
        return this;
    }

    private void addSet(String property, Object value) {
        synchronized (this) {
            if (updates == null) {
                updates = new ArrayList<Update>();
            }
            Update update = new Update(value, property, ShortUtils.to62RadixString(i));
            updates.add(update);
            i++;
        }
    }


    private void addQuery(Query query) {
        synchronized (this) {
            if (queries == null) {
                queries = new ArrayList<Query>();
            }
            query.setValueAt(ShortUtils.to62RadixString(i));
            queries.add(query);
            i++;
        }
    }

    public DBBuilder select(String... ps) {
        synchronized (this) {
            if (select == null) {
                select = new ArrayList<String>();
            }
            for (String p : ps) {
                if (!select.contains(p)) {
                    select.add(p);
                }
            }
        }
        return this;
    }

    /**
     * 不等于  <>
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder ne(String p, Object value) {
        addQuery(Where.NE.where(p, p, value));
        return this;
    }

    public DBBuilder or() {
        addQuery(new Query(Where.OR));
        return this;
    }

    public DBBuilder inor() {
        addQuery(new Query(Where.INOR));
        return this;
    }

    public DBBuilder and() {
        addQuery(new Query(Where.AND));
        return this;
    }

    /**
     * 等于  =
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder eq(String p, Object value) {
        addQuery(Where.EQ.where(p, p, value));
        return this;
    }

    /**
     * 大于  >
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder gt(String p, Object value) {
        addQuery(Where.GT.where(p, p, value));
        return this;
    }

    /**
     * 大于等于 >=
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder ge(String p, Object value) {
        addQuery(Where.GE.where(p, p, value));
        return this;
    }

    /**
     * 小于 <
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder lt(String p, Object value) {
        addQuery(Where.LT.where(p, p, value));
        return this;
    }

    /**
     * 小于等于 <=
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder le(String p, Object value) {
        addQuery(Where.LE.where(p, p, value));
        return this;
    }

    /**
     * like %value%
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder likeOnly(String p, Object value) {
        addQuery(Where.LIKE.where(p, p, value));
        return this;
    }

    /**
     * like %value%
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder like(String p, Object value) {
        addQuery(Where.LIKE.where(p, p, "%" + value + "%"));
        return this;
    }

    /**
     * like %value
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder likeStart(String p, Object value) {
        addQuery(Where.LIKE.where(p, p, "%" + value));
        return this;
    }

    /**
     * like value%
     *
     * @param p
     * @param value
     * @return
     */
    public DBBuilder likeEnd(String p, Object value) {
        addQuery(Where.LIKE.where(p, p, value + "%"));
        return this;
    }

    /**
     * is not null
     *
     * @param p
     * @return
     */
    public DBBuilder isNotNull(String p) {
        addQuery(Where.NOTNULL.whereProperty(p, p));
        return this;
    }

    /**
     * is null
     *
     * @param p
     * @return
     */
    public DBBuilder isNull(String p) {
        addQuery(Where.ISNULL.whereProperty(p, p));
        return this;
    }

    /**
     * is not Empty
     *
     * @param p
     * @return
     */
    public DBBuilder isNotEmpty(String p) {
        addQuery(Where.NOTEMPTY.whereProperty(p, p));
        return this;
    }

    /**
     * is Empty
     *
     * @param p
     * @return
     */
    public DBBuilder isEmpty(String p) {
        addQuery(Where.ISEMPTY.whereProperty(p, p));
        return this;
    }


    @Override
    public DBBuilder in(String p, Object... values) {
        in(p, false, values);
        return this;
    }

    @Override
    public DBBuilder notIn(String p, Object... values) {
        notIn(p, false, values);
        return this;
    }

    @Override
    public DBBuilder in(String p, Collection values) {
        in(p, false, values);
        return this;
    }

    @Override
    public DBBuilder notIn(String p, Collection values) {
        notIn(p, false, values);
        return this;
    }

    @Override
    public DBBuilder in(String p, boolean ignore, Object... values) {
        in(p, ignore, (values == null || values.length == 0) ? null : Arrays.asList(values));
        return this;
    }

    @Override
    public DBBuilder notIn(String p, boolean ignore, Object... values) {
        notIn(p, ignore, (values == null || values.length == 0) ? null : Arrays.asList(values));
        return this;
    }

    @Override
    public DBBuilder in(String p, boolean ignore, Collection values) {
        if (values == null || values.isEmpty()) {
            if (ignore) {
                return this;
            } else {
                throw new RuntimeException("values is null");
            }
        }
        addQuery(Where.IN.where(p, p, values));
        return this;
    }

    @Override
    public DBBuilder notIn(String p, boolean ignore, Collection values) {
        if (values == null || values.isEmpty()) {
            if (ignore) {
                return this;
            } else {
                throw new RuntimeException("values is null");
            }
        }
        addQuery(Where.NOTIN.where(p, p, values));
        return this;
    }

    /**
     * @param p
     * @return
     */
    public DBBuilder asc(String p) {
        addOrder(OrderBy.ASC.order(p));
        return this;
    }


    /**
     * @param p
     * @return
     */
    public DBBuilder desc(String p) {
        addOrder(OrderBy.DESC.order(p));
        return this;
    }

//    }

    @Override
    public DBBuilder between(String p, Object lo, Object hi) {
        addQuery(Where.BETWEEN.where(p, p, new Between(lo, hi)));
        return this;
    }

    @Override
    public DBBuilder notBetween(String p, Object lo, Object hi) {
        addQuery(Where.NOTBETWEEN.where(p, p, new Between(lo, hi)));
        return this;
    }

    private void defaultRead() {
        if (!changeDatasource) {
            MultipleDataSource.changeDataSourceKey(MultipleDataSource.getDefaultReadKey());
        }
    }

    private void defaultWrite() {
        if (!changeDatasource) {
            MultipleDataSource.changeDataSourceKey(MultipleDataSource.getDefaultWriteKey());
        }
    }

    public Integer count() {
        defaultRead();
        return DBSql.build(getClazz()).count(this);
    }

    public List list() {
        defaultRead();
        return DBSql.build(getClazz()).list(this);
    }

    public List listAll() {
        defaultRead();
        return DBSql.build(getClazz()).listAll(this);
    }

    @Override
    public List list(FieldMapper fieldMapper) {
        List list = new ArrayList();
        for (Object o : list()) {
            list.add(fieldMapper.mapper(o));
        }
        return list;
    }

    @Override
    public PageData page(FieldMapper fieldMapper) {
        PageData pageData = new PageData();
        pageData.setCount(count());
        pageData.setList(list(fieldMapper));
        pageData.setPageNumber(getPage());
        pageData.setPageSize(getSize());
        return pageData;
    }

    public <T> T one() {
        defaultRead();
        return (T) DBSql.build(getClazz()).one(this);
    }

    public BigDecimal sum() {
        defaultRead();
        return DBSql.build(getClazz()).sum(this);
    }

    public PageData page() {
        PageData pageData = new PageData();
        pageData.setCount(count());
        pageData.setList(list());
        pageData.setPageNumber(getPage());
        pageData.setPageSize(getSize());
        return pageData;
    }

    public Object getInsertTarget() {
        return insertTarget;
    }

    public Integer save() {
        defaultWrite();
        return DBSql.build(getClazz()).insert(this);
    }

    public Integer update() {
        defaultWrite();
        return DBSql.build(getClazz()).update(this);
    }

    public Integer delete() {
        defaultWrite();
        return DBSql.build(getClazz()).delete(this);
    }

    public <T extends SqlBaseDao> T dao() {
        return DBSql.build(getClazz());
    }


    public final static String identity_key = "identity_sense_mybatis_62f98591c4ed2947b1d66676ae020acc";

    public void copyIdentity() {
        EntityDefinition entityDefinition = findEntityDefinition();
        if (GenerationType.IDENTITY.equals(entityDefinition.getGetGenerated())) {
            //自增在copy实现
            try {
                Object o = get(identity_key);
                if (o != null) {
                    if (o instanceof Long || o instanceof Integer) {
                        if (Integer.class.equals(entityDefinition.getIdJavaType())) {
                            PropertyUtils.setProperty(insertTarget, entityDefinition.getIdJavaColumn(), Integer.valueOf(String.valueOf(o)));
                        } else if (Long.class.equals(entityDefinition.getIdJavaType())) {
                            PropertyUtils.setProperty(insertTarget, entityDefinition.getIdJavaColumn(), Long.valueOf(String.valueOf(o)));
                        } else if (String.class.equals(entityDefinition.getIdJavaType())) {
                            PropertyUtils.setProperty(insertTarget, entityDefinition.getIdJavaColumn(), String.valueOf(o));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("copyId", e);
            }
        }

    }
}
