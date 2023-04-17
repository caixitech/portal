package org.helloframework.baseapi.invoker;

import org.helloframework.gateway.common.annotation.graph.GraphOP;
import org.helloframework.gateway.common.annotation.graph.GraphParamOP;
import org.helloframework.gateway.common.definition.graph.GraphInvoker;
import org.helloframework.gateway.common.definition.graph.GraphQuery;
import org.helloframework.gateway.common.definition.graph.GraphRequest;
import org.helloframework.gateway.common.definition.graph.GraphResponse;
import org.helloframework.mongodb.Mongo;
import org.springframework.data.mongodb.core.MongoTemplate;

public class HelloMongodbGraphInvoker implements GraphInvoker {

    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void invoke(GraphRequest req, GraphResponse resp, Class cls, GraphOP op) {
        if (mongoTemplate == null) {
            throw new RuntimeException("mongoTemplate is null");
        }
        Mongo mongo = Mongo.build(mongoTemplate);
        for (GraphQuery graphsOP : req.getQuery()) {
            op(mongo, graphsOP);
        }
        if (GraphOP.update.equals(op)) {
            mongo.updateMulti(cls);
        }
        if (GraphOP.query.equals(op)) {
            if (req.getLimit() <= 1) {
                Object o = mongo.one(cls);
                resp.setData(o);
            } else {
                Object o = mongo.limit(req.getLimit(), req.getPage(), req.getSkip()).list(cls);
                resp.setData(o);
            }
        }
        if (GraphOP.delete.equals(op)) {
            mongo.remove(cls);
        }
        if (GraphOP.insert.equals(op)) {
            mongo.upsert(cls);
        }
    }

    private void op(Mongo mongo, GraphQuery graphQuery) {
        GraphParamOP op = GraphParamOP.valueOf(graphQuery.getOp());
        switch (op) {
            case eq:
                mongo.eq(graphQuery.getK(), graphQuery.getV());
                return;
            case gt:
                mongo.gt(graphQuery.getK(), graphQuery.getV());
                return;
            case lt:
                mongo.lt(graphQuery.getK(), graphQuery.getV());
                return;
            case gte:
                mongo.gte(graphQuery.getK(), graphQuery.getV());
                return;
            case lte:
                mongo.lte(graphQuery.getK(), graphQuery.getV());
                return;
            case set:
                mongo.set(graphQuery.getK(), graphQuery.getV());
                return;
            case in:
                op.checkArray(graphQuery.getV().getClass());
                mongo.in(graphQuery.getK(), graphQuery.getV());
                return;
            case nin:
                op.checkArray(graphQuery.getV().getClass());
                mongo.nin(graphQuery.getK(), graphQuery.getV());
                return;
            default:
                throw new RuntimeException("mongo op error");
        }
    }


}