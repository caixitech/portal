package org.helloframework.mongodb;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by lanjian on 14-8-28.
 */
public abstract class MongoQuery {
    protected Query query;

    protected Criteria criteria;

    protected Field field;

    protected Update update;


    protected Update update() {
        if (update == null) {
            update = Update.update("_v", System.currentTimeMillis());
        }
        return update;
    }

    public MongoQuery() {
        criteria = new Criteria();
        query = Query.query(criteria);
        field = query.fields();
    }


    protected void field(String key) {
        field.include(key);
    }

    protected Criteria and(String key) {
        return criteria.and(key);
    }

}
