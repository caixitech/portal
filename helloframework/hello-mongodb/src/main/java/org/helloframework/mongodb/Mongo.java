package org.helloframework.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.helloframework.core.utils.BeanUtils;
import org.helloframework.mongodb.exception.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: lanjian
 * Email:lanjian@i5c6c.com
 * Version:1.0
 */
public class Mongo extends MongoQuery {

    public static final String ID = "_id";
    private static final Logger LOGGER = LoggerFactory.getLogger(Mongo.class);
    private String prefix = null;

    private String suffix = null;

    private MongoTemplate mongoOperations;

    //    public static IMongoUpdate nullUpdate() {
//        return new IMongoUpdate() {
//            @Override
//            public void update(Update update) {
//
//            }
//        };
//    }
    private Map<OP, List<Criteria>> map = new HashMap<OP, List<Criteria>>();

    public Mongo(MongoTemplate mongoOperations) {
        super();
        this.mongoOperations = mongoOperations;
    }

    public static Mongo build(MongoTemplate source) {
        return new Mongo(source);
    }

    public Mongo safe() {
        mongoOperations.setWriteConcern(WriteConcern.SAFE);
        return this;
    }

    public Mongo fields(String... fields) {
        for (String field : fields) {
            field(field);
        }
        return this;
    }

    public Mongo sort(MongoSort mongoSort) {
        if (mongoSort != null) {
            mongoSort.sort(this);
        }
        return this;
    }

    public Mongo prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Mongo suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public Mongo buildCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public List geoFind(Double lat, Double lng, Class cls) {
        NearQuery nearQuery = NearQuery.near(lng, lat, Metrics.KILOMETERS);
        nearQuery.query(query);
        nearQuery.spherical(true);
//        nearQuery.maxDistance(10000d);
        nearQuery.num(query.getSkip() + query.getLimit());
        GeoResults geoResults = mongoOperations.geoNear(nearQuery, cls, collectionName(cls));
        List<GeoResult> results = geoResults.getContent();
        List list = new ArrayList();
        for (GeoResult g : results) {
            list.add(g.getContent());
        }
        return list;
    }

    public List geoFind(Double lat, Double lng, Class cls, String collectionName) {
        NearQuery nearQuery = NearQuery.near(lng, lat, Metrics.KILOMETERS);
        nearQuery.query(query);
        nearQuery.spherical(true);
        nearQuery.num(query.getSkip() + query.getLimit());
        GeoResults geoResults = mongoOperations.geoNear(nearQuery, cls, collectionName);
        List<GeoResult> results = geoResults.getContent();
        List list = new ArrayList();
        for (GeoResult g : results) {
            list.add(g.getContent());
        }
        return list;
    }

    public Mongo multiEq(String property, String... values) {
        Criteria[] criterias = new Criteria[values.length];
        int index = 0;
        for (String str : values) {
            criterias[index] = Criteria.where(property).is(str);
            index++;
        }
        criteria.andOperator(criterias);
        return this;
    }

    public Mongo intersects(String key, GeoJson geoJson) {
        criteria.and(key).intersects(geoJson);
        return this;
    }

    public Mongo withinSphere(Circle circle) {
        criteria.withinSphere(circle);
        return this;
    }

    public Mongo within(String key, Shape shape) {
        criteria.and(key).within(shape);
        return this;
    }

    public Mongo within(Shape shape) {
        criteria.within(shape);
        return this;
    }

    public Mongo near(Point point) {
        criteria.near(point);
        return this;
    }

    public Mongo nearSphere(Point point) {
        criteria.nearSphere(point);
        return this;
    }

    public Mongo minDistance(double minDistance) {
        criteria.minDistance(minDistance);
        return this;
    }

    public Mongo maxDistance(double maxDistance) {
        criteria.maxDistance(maxDistance);
        return this;
    }

    public Mongo mySelf(IMyselfQuery myself) {
        myself.query(criteria);
        return this;
    }

    public Mongo eq(String key, Object value) {
        eq(key, value, null);
        return this;
    }

    public Mongo eq(String key, Object value, boolean flag) {
        if (flag) {
            eq(key, value, null);
        }
        return this;
    }

    public Mongo eq(String key, Object value, MongoValid valid) {
        if (valid != null) {
            if (valid.valid(value)) {
                and(key).is(value);
            }
        } else {
            and(key).is(value);
        }
        return this;
    }

    public Mongo fuzzy(String key, Object value) {
        String re = String.valueOf(value);
        re = re.replace("(", "\\(").replace(")", "\\)");
        and(key).regex(re, "i");
        return this;
    }

    public Mongo fuzzy(String key, Object value, boolean flag) {
        if (flag) {
            return fuzzy(key, value);
        }
        return this;
    }

    public Mongo fuzzy(String key, Object value, MongoValid valid) {
        if (valid != null) {
            if (valid.valid(value)) {
                fuzzy(key, value);
            }
        } else {
            fuzzy(key, value);
        }
        return this;
    }

    public Mongo ne(String key, Object value, boolean flag) {
        if (flag) {
            and(key).ne(value);
        }
        return this;
    }

    public Mongo ne(String key, Object value) {
        and(key).ne(value);
        return this;
    }

    public Mongo or(String key, Object... value) {
        push(OP.OR, genOrCriteria(key, value));
        return this;
    }

    public Mongo or(String[] key, Object... value) {
        push(OP.OR, genOrCriteria(key, value));
        return this;
    }

    public Mongo or(String[] key, Object[] value, Operator[] operators) {
        push(OP.OR, genOrCriteria(key, value, operators));
        return this;
    }

    public Mongo nor(String key, Object... value) {
        push(OP.NOR, genOrCriteria(key, value));
        return this;
    }

    public Mongo nor(String[] key, Object... value) {
        push(OP.NOR, genOrCriteria(key, value));
        return this;
    }

    public Mongo nor(String[] key, Object[] value, Operator[] operators) {
        push(OP.NOR, genOrCriteria(key, value, operators));
        return this;
    }

    public Mongo and(String key, Object... value) {
        push(OP.AND, genOrCriteria(key, value));
        return this;
    }

    public Mongo and(String[] key, Object... value) {
        push(OP.AND, genOrCriteria(key, value));
        return this;
    }

    public Mongo and(String[] key, Object[] value, Operator[] operators) {
        push(OP.AND, genOrCriteria(key, value, operators));
        return this;
    }


//    public void init() {
//        Set<OP> ops = map.keySet();
//        for (OP op : ops) {
//            List<Criteria> criterias = map.get(op);
//            if (CollectionUtils.isNotEmpty(criterias)) {
//                op.op(criteria, criterias.toArray(new Criteria[]{}));
//            }
//        }
//    }

    private void push(OP op, List<Criteria> criteriaList) {
        List<Criteria> criterias = map.get(op);
        if (criterias == null) {
            criterias = new ArrayList<Criteria>();
            map.put(op, criterias);
            criterias.addAll(criteriaList);
            op.op(criteria, criterias.toArray(new Criteria[]{}));
        } else {
            throw new RuntimeException("multi or, and, nor operations not support");
        }
//        criterias.addAll(criteriaList);
    }

    private List<Criteria> genOrCriteria(String key, Object[] value) {
        List<Criteria> criterias = new ArrayList<Criteria>();
        for (int i = 0; i < value.length; i++) {
            criterias.add(Criteria.where(key).is(value[i]));
        }
        return criterias;
    }

    private List<Criteria> genOrCriteria(String[] key, Object[] value) {
        List<Criteria> criterias = new ArrayList<Criteria>();
        for (int i = 0; i < value.length; i++) {
            criterias.add(Criteria.where(key[i]).is(value[i]));
        }
        return criterias;
    }

    private List<Criteria> genOrCriteria(String[] key, Object[] value, Operator[] operators) {
        List<Criteria> criterias = new ArrayList<Criteria>();
        for (int i = 0; i < value.length; i++) {
            if (Operator.in.equals(operators[i])) {
                Object[] tmp = (Object[]) value[i];
                criterias.add(Criteria.where(key[i]).in(tmp));
            }
            if (Operator.nin.equals(operators[i])) {
                Object[] tmp = (Object[]) value[i];
                criterias.add(Criteria.where(key[i]).nin(tmp));
            }
            if (Operator.regex.equals(operators[i])) {
                String re = String.valueOf(value[i]);
                re = re.replace("(", "\\(").replace(")", "\\)");
                criterias.add(Criteria.where(key[i]).regex(re, "i"));
            }
            if (Operator.eq.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).is(value[i]));
            }
            if (Operator.gt.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).gt(value[i]));
            }
            if (Operator.lt.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).lt(value[i]));
            }
            if (Operator.gte.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).gte(value[i]));
            }
            if (Operator.lte.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).lte(value[i]));
            }
            if (Operator.ne.equals(operators[i])) {
                criterias.add(Criteria.where(key[i]).ne(value[i]));
            }
        }
        return criterias;
    }

    public Mongo gte(String key, Object value) {
        and(key).gte(value);
        return this;
    }

    public Mongo gt(String key, Object value) {
        and(key).gt(value);
        return this;
    }

    public Mongo lt(String key, Object value) {
        and(key).lt(value);
        return this;
    }

    public Mongo lte(String key, Object value) {
        and(key).lte(value);
        return this;
    }

    public Mongo in(String key, Collection value) {
        and(key).in(value.toArray());
        return this;
    }

    public Mongo in(String key, Collection value, MongoValid valid) {
        if (valid.valid(value)) {
            and(key).in(value.toArray());
        }
        return this;
    }

    public Mongo in(String key, Object... value) {
        and(key).in(value);
        return this;
    }

    public Mongo nin(String key, Collection value) {
        and(key).nin(value.toArray());
        return this;
    }

    public Mongo nin(String key, Collection value, MongoValid valid) {
        if (valid.valid(valid)) {
            and(key).nin(value.toArray());
        }
        return this;
    }

    public Mongo nin(String key, Object... value) {
        and(key).nin(value);
        return this;
    }

    public Mongo size(String key, int size) {
        and(key).size(size);
        return this;
    }

    public Mongo exists(String key, boolean flag) {
        and(key).exists(flag);
        return this;
    }

    public Mongo limit(Integer limit, Integer page, Integer skip) {
        if (page < 1) {
            throw new RuntimeException("page is invalid ...");
        }
        if (skip == null) {
            skip = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        query.limit(limit);
        query.skip(skip + ((page - 1) * limit));
        return this;
    }

    public Mongo limit(int limit, int page) {
        limit(limit, page, 0);
        return this;
    }

    public Mongo skip(int limit, int skip) {
        if (skip < 0) {
            throw new RuntimeException("skip is invalid ...");
        }
        query.limit(limit);
        query.skip(skip);
        return this;
    }

    public Mongo desc(String... properties) {
        query.with(Sort.by(Sort.Direction.DESC, properties));
        return this;
    }

    public Mongo asc(String... properties) {
        query.with(Sort.by(Sort.Direction.ASC, properties));
        return this;
    }

    public Mongo type(String key, int type) {
        and(key).type(type);
        return this;
    }

    public <T> T load(String id, Class<T> clazz) {
        return eq(ID, id).one(clazz);
    }

    private String collectionName(Object obj) {
        Class clazz = obj.getClass();
        return collectionName(clazz);
    }

    private String collectionName(Class clazz) {
        String collectionName = null;
        if (clazz.isAnnotationPresent(MongoCollection.class)) {
            MongoCollection table = (MongoCollection) clazz.getAnnotation(MongoCollection.class);
            collectionName = table.value();
        } else {
            collectionName = clazz.getSimpleName();
        }
        StringBuffer cn = new StringBuffer();
        if (StringUtils.isNotBlank(prefix)) {
            cn.append(prefix);
        }
        cn.append(collectionName);
        if (StringUtils.isNotBlank(suffix)) {
            cn.append(suffix);
        }
        return cn.toString();
    }

    //    public OPResult insert(Object obj, IMongoUpdate update) {
//        return insert(obj, collectionName(obj), update);
//    }
//
//    public OPResult insert(Object obj, String collectionName, IMongoUpdate update) {
//        if (count(collectionName) > 0) {
//            if (update != null) {
//                UpdateResult updateResult = updateFirst(collectionName, update);
//                return new OPResult(MongoOpType.UPDATE, updateResult);
//            } else {
//                return new OPResult(MongoOpType.UPDATE, null);
//            }
//        } else {
//            insert(obj, collectionName);
//            return new OPResult(MongoOpType.INSERT, null);
//        }
//    }
    public OPResult insertAndUpdate(Object obj) {
        return insertAndUpdate(obj, collectionName(obj));
    }

    public OPResult insertAndUpdate(Object obj, String collectionName) {
        if (count(collectionName) > 0) {
            if (update == null) {
                return new OPResult(MongoOpType.UPDATE, null);
            }
            UpdateResult updateResult = updateFirst(collectionName);
            return new OPResult(MongoOpType.UPDATE, updateResult);
        } else {
            insert(obj, collectionName);
            return new OPResult(MongoOpType.INSERT, null);
        }
    }

    public void save(Object obj) {
        mongoOperations.save(obj, collectionName(obj));
    }

    public void save(Object obj, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(obj);
        }
        mongoOperations.save(obj, collectionName);
    }

    public <T> T findAndUpdate(Class clazz) {
        return (T) mongoOperations.findAndModify(query, update, clazz, collectionName(clazz));
    }

//    public UpdateResult upsert(Class clazz, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        return mongoOperations.upsert(query, _update, collectionName(clazz));
//    }
//
//
//    public UpdateResult updateMulti(Class clazz, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        return mongoOperations.updateMulti(query, _update, collectionName(clazz));
//    }
//
//    public UpdateResult updateMulti(String collectionName, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        return mongoOperations.updateMulti(query, _update, collectionName);
//    }
//
//    public UpdateResult updateFirst(Class clazz, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        UpdateResult updateResult = mongoOperations.updateFirst(query, _update, collectionName(clazz));
//        return updateResult;
//    }
//
//    public UpdateResult updateFirst(String collectionName, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        return mongoOperations.updateFirst(query, _update, collectionName);
//    }
//
//    public void findAndModify(Class clazz, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        mongoOperations.findAndModify(query, _update, clazz, collectionName(clazz));
//    }
//
//    public <T> T findAndUpdate(Class clazz, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        return (T) mongoOperations.findAndModify(query, _update, clazz, collectionName(clazz));
//    }
//
//    public <T> T findAndUpdate(Class clazz, boolean upsert, boolean returnNew, IMongoUpdate update) {
//        Update _update = Update.update("_version_", System.currentTimeMillis());
//        update.update(_update);
//        FindAndModifyOptions options = new FindAndModifyOptions();
//        options.upsert(upsert).returnNew(returnNew);
//        return (T) mongoOperations.findAndModify(query, _update, options, clazz, collectionName(clazz));
//    }

    public <T> T findAndUpdate(Class clazz, boolean upsert, boolean returnNew) {
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(upsert).returnNew(returnNew);
        return (T) mongoOperations.findAndModify(query, update, options, clazz, collectionName(clazz));
    }

    public <T> T findAndModify(Class clazz) {
        return (T) mongoOperations.findAndModify(query, update, clazz, collectionName(clazz));
    }

    public <T> T findAndModify(Class clazz, String collectionName) {
        return (T) mongoOperations.findAndModify(query, update, clazz, collectionName);
    }

    public UpdateResult upsert(Class clazz) {
        return mongoOperations.upsert(query, update, collectionName(clazz));
    }

    public UpdateResult updateMulti(Class clazz) {
        return mongoOperations.updateMulti(query, update, collectionName(clazz));
    }

    public UpdateResult updateMulti(String collectionName) {
        return mongoOperations.updateMulti(query, update, collectionName);
    }

    public UpdateResult updateMulti(Class clazz, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(clazz);
        }
        return mongoOperations.updateMulti(query, update, collectionName);
    }

    public UpdateResult updateFirst(Class clazz) {
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, collectionName(clazz));
        return updateResult;
    }

    public UpdateResult updateFirst(String collectionName) {
        return mongoOperations.updateFirst(query, update, collectionName);
    }

    public UpdateResult updateFirst(Class clazz, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(clazz);
        }
        return mongoOperations.updateFirst(query, update, collectionName);
    }

    public DeleteResult remove(Class cls) {
        return mongoOperations.remove(query, collectionName(cls));
    }

    public DeleteResult remove(Class cls, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(cls);
        }
        return mongoOperations.remove(query, collectionName);
    }


    public void remove(String collectionName) {
        mongoOperations.remove(query, collectionName);
    }

    public long count(Class clazz) {
        return mongoOperations.count(query, collectionName(clazz));
    }

    public long count(Class clazz, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(clazz);
        }
        return mongoOperations.count(query, collectionName);
    }

    public <T> T aggregate(Class clazz, Class outClass, List<AggregationOperation> aggregationOperations) {
        List<AggregationOperation> operations = new ArrayList<AggregationOperation>();
        operations.add(Aggregation.match(criteria));
        if (aggregationOperations != null && aggregationOperations.size() > 0) {
            operations.addAll(aggregationOperations);
        }
        Aggregation aggregations = Aggregation.newAggregation(operations);
        AggregationResults<BasicDBObject> results = mongoOperations.aggregate(aggregations, collectionName(clazz), BasicDBObject.class);
        List<BasicDBObject> mappedResults = results.getMappedResults();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < mappedResults.size(); i++) {
            BasicDBObject basicDBObject = mappedResults.get(i);
            Set<String> keys = basicDBObject.keySet();
            for (String key : keys) {
                if (map.containsKey(key)) {
                    throw new RuntimeException("aggregate key double");
                }
                if (!key.equals("_id")) {
                    map.put(key, basicDBObject.get(key));
                }
            }
        }
        return BeanUtils.populate(outClass, map);
    }

    public <T> T aggregate(Class clazz, Class outClass, AggregationOperation... aggregationOperations) {
        return aggregate(clazz, outClass, aggregationOperations != null ? Arrays.asList(aggregationOperations) : null);
    }

    public <T> List<T> aggregateList(Class clazz, Class outClass, AggregationOperation... aggregationOperations) {
        List<AggregationOperation> operations = new ArrayList<AggregationOperation>();
        operations.add(Aggregation.match(criteria));
        if (aggregationOperations != null && aggregationOperations.length > 0) {
            operations.addAll(Arrays.asList(aggregationOperations));
        }
        Aggregation aggregations = Aggregation.newAggregation(operations);
        AggregationResults<BasicDBObject> results = mongoOperations.aggregate(aggregations, collectionName(clazz), BasicDBObject.class);
        List<BasicDBObject> mappedResults = results.getMappedResults();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < mappedResults.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            BasicDBObject basicDBObject = mappedResults.get(i);
            Set<String> keys = basicDBObject.keySet();
            for (String key : keys) {
                map.put(key, basicDBObject.get(key));
            }
            list.add(BeanUtils.populate(outClass, map));
        }
        return list;
    }

    public long count(String collectionName) {
        return mongoOperations.count(query, collectionName);
    }

    public <T> List<T> list(Class<T> clazz) {
        return mongoOperations.find(query, clazz, collectionName(clazz));
    }

    public <T> List<T> list(Class<T> clazz, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(clazz);
        }
        return mongoOperations.find(query, clazz, collectionName);
    }

    public <T> T one(Class<T> clazz) {
        return (T) mongoOperations.findOne(query, clazz, collectionName(clazz));
    }

    public <T> T one(Class<T> clazz, String collection) {
        if (StringUtils.isEmpty(collection)) {
            collection = collectionName(clazz);
        }
        return (T) mongoOperations.findOne(query, clazz, collection);
    }

    public void insert(Object obj) {
        mongoOperations.insert(obj, collectionName(obj));
    }

//    public CommandResult executeCommand(String json) {
//        return mongoOperations.executeCommand(json);
//    }

    public void insert(Object obj, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(obj);
        }
        mongoOperations.insert(obj, collectionName);
    }

    public void insertBatch(List objs, Class clazz) {
        mongoOperations.insert(objs, collectionName(clazz));
    }

    public void insertBatch(List objs, Class clazz, String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = collectionName(clazz);
        }
        mongoOperations.insert(objs, collectionName);
    }

    public void insertBatch(List objs, String collectionName) {
        mongoOperations.insert(objs, collectionName);
    }

    public List all(Class clazz) {
        return mongoOperations.findAll(clazz, collectionName(clazz));
    }

    public List all(Class clazz, String collectionName) {
        return mongoOperations.findAll(clazz, collectionName);
    }

    public Mongo between(String key, Object begin, Object end, Between between) {
        between.between(and(key), begin, end);
        return this;
    }

//    public void ensureIndex(String name, Order order, Class cls) {
//        DBObject dbObject = new BasicDBObject();
//        dbObject.put(name, order.getOrderValue());
//        mongoOperations.getCollection(collectionName(cls)).createIndex(dbObject);
//    }
//
//    public void ensureIndex2D(String name, Class cls) {
//        DBObject dbObject = new BasicDBObject();
//        dbObject.put(name, "2d");
//        mongoOperations.getCollection(collectionName(cls)).createIndex(dbObject);
//    }

    public Mongo set(String key, @Nullable Object value) {
        update().set(key, value);
        return this;
    }

    public Mongo set(String key, @Nullable Object value, boolean flag) {
        if (flag) {
            update().set(key, value);
        }
        return this;
    }

    public Mongo setOnInsert(String key, @Nullable Object value) {
        update().setOnInsert(key, value);
        return this;
    }

    public Mongo setOnInsert(String key, @Nullable Object value, boolean flag) {
        if (flag) {
            update().setOnInsert(key, value);
        }
        return this;
    }

    public Mongo unset(String key) {
        update().unset(key);
        return this;
    }

    public Mongo unset(String key, boolean flag) {
        if (flag) {
            update().unset(key);
        }
        return this;
    }

    public Mongo inc(String key, Number inc) {
        update().inc(key, inc);
        return this;
    }

    public Mongo inc(String key, Number inc, boolean flag) {
        if (flag) {
            update().inc(key, inc);
        }
        return this;
    }

    public Mongo inc(String key) {
        inc(key, 1L);
        return this;
    }

    public Mongo inc(String key, boolean flag) {
        if (flag) {
            this.inc(key, 1L);
        }
        return this;
    }

    public Mongo push(String key, @Nullable Object value) {
        update().push(key, value);
        return this;
    }

    public Mongo push(String key, @Nullable Object value, boolean flag) {
        if (flag) {
            update().push(key, value);
        }
        return this;
    }

    public Mongo addToSet(String key, @Nullable Object value) {
        update().addToSet(key, value);
        return this;
    }

    public Mongo each(String key, @Nullable Collection value) {
        Update.AddToSetBuilder addToSetBuilder = update().addToSet(key);
        addToSetBuilder.each(value.toArray());
        return this;
    }

    public Mongo addToSet(String key, @Nullable Object value, boolean flag) {
        if (flag) {
            update().addToSet(key, value);
        }
        return this;
    }

    public Mongo objectSetAll(Object obj, String... excludes) {
        if (obj != null) {
            List<Field> fieldList = new ArrayList<>();
            Class<?> clazz = obj.getClass();
            while (clazz != null) {
                fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();
            }
            if (CollectionUtils.isNotEmpty(fieldList)) {
                try {
                    for (Field field : fieldList) {
                        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                            continue;
                        }
                        if (ArrayUtils.isNotEmpty(excludes) && ArrayUtils.contains(excludes, field.getName())) {
                            continue;
                        }
                        Object value = PropertyUtils.getProperty(obj, field.getName());
                        if (value != null) {
                            update().set(field.getName(), value);
                        }
                    }
                } catch (Throwable e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("objectSetAll---", e);
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }

    public Mongo objectSet(Object obj, String... includes) {
        for (String field : includes) {
            try {
                Object value = PropertyUtils.getProperty(obj, field);
                if (value != null) {
                    update().set(field, value);
                }
            } catch (Exception e) {
                LOGGER.error("updateSet---", e);
            }
        }
        return this;
    }

    public BigDecimal sum(Class clazz, String field) {
        return sum(collectionName(clazz), field);
    }

    public BigDecimal sum(String collectionName, String field) {
        List<BasicDBObject> pipeline = new ArrayList<>();
        if (!query.getQueryObject().keySet().isEmpty()) {
            BasicDBObject $match = new BasicDBObject();
            $match.put("$match", query.getQueryObject());
            pipeline.add($match);
        }

        pipeline.add(BasicDBObject.parse("{$group:{_id:null,'" + field + "':{$sum:'$" + field + "'}}}"));
        AggregateIterable<Document> results = mongoOperations.getCollection(collectionName).aggregate(pipeline);
        try (MongoCursor<Document> it = results.cursor()) {
            if (it.hasNext()) {
                Document document = it.next();
                Object obj = document.get(field);
                if (obj == null) {
                    return BigDecimal.ZERO;
                }
                if (obj instanceof Double || obj instanceof Float) {
                    return new BigDecimal(((Number) obj).doubleValue());
                } else {
                    return new BigDecimal(((Number) obj).longValue());
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal avg(Class clazz, String field) {
        return avg(collectionName(clazz), field);
    }

    public BigDecimal avg(String collectionName, String field) {
        List<BasicDBObject> pipeline = new ArrayList<>();
        if (!query.getQueryObject().keySet().isEmpty()) {
            BasicDBObject $match = new BasicDBObject();
            $match.put("$match", query.getQueryObject());
            pipeline.add($match);
        }

        pipeline.add(BasicDBObject.parse("{$group:{_id:null,'" + field + "':{avg:'$" + field + "'}}}"));
        AggregateIterable<Document> results = mongoOperations.getCollection(collectionName).aggregate(pipeline);
        try (MongoCursor<Document> it = results.cursor()) {
            if (it.hasNext()) {
                Document document = it.next();
                Object obj = document.get(field);
                if (obj == null) {
                    return BigDecimal.ZERO;
                }
                if (obj instanceof Double || obj instanceof Float) {
                    return new BigDecimal(((Number) obj).doubleValue());
                } else {
                    return new BigDecimal(((Number) obj).longValue());
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private enum OP {
        OR, AND, NOR;

        public void op(Criteria criteria, Criteria[] criterias) {
            switch (this) {
                case OR:
                    criteria.orOperator(criterias);
                    return;
                case AND:
                    criteria.andOperator(criterias);
                    return;
                case NOR:
                    criteria.norOperator(criterias);
                    return;
                default:
                    throw new RuntimeException("not support");
            }
        }
    }


    public enum Order {
        desc(-1), asc(1);

        private int orderValue;

        Order(int orderValue) {
            this.orderValue = orderValue;
        }

        public int getOrderValue() {
            return orderValue;
        }
    }

    public enum Between {
        EQ, NEQ, FEQ, EEQ;

        public void between(Criteria criteria, Object begin, Object end) {
            switch (this) {
                case EQ:
                    criteria.lte(end).gte(begin);
                    break;
                case NEQ:
                    criteria.lt(end).gt(begin);
                    break;
                case FEQ:
                    criteria.lt(end).gte(begin);
                    break;
                case EEQ:
                    criteria.lte(end).gt(begin);
                    break;
                default:
                    throw new QueryException("no Between enum");
            }
        }
    }

    public Mongo filterArray(String key, Object value) {
        update().filterArray(key, value);
        return this;
    }

    public Mongo filterArray(CriteriaDefinition criteriaDefinition) {
        update().filterArray(criteriaDefinition);
        return this;
    }
}