package org.helloframework.baseapi.invoker;

import org.helloframework.gateway.common.annotation.graph.GraphOP;
import org.helloframework.gateway.common.annotation.graph.GraphParamOP;
import org.helloframework.gateway.common.definition.graph.GraphInvoker;
import org.helloframework.gateway.common.definition.graph.GraphQuery;
import org.helloframework.gateway.common.definition.graph.GraphRequest;
import org.helloframework.gateway.common.definition.graph.GraphResponse;
import org.helloframework.mybatis.query.builder.DBBuilder;
public class HelloMybatisGraphInvoker implements GraphInvoker {

    @Override
    public void invoke(GraphRequest req, GraphResponse resp, Class cls, GraphOP op) {
        DBBuilder dbBuilder = DBBuilder.build(cls);
        for (GraphQuery graphsOP : req.getQuery()) {
            op(dbBuilder, graphsOP);
        }
        if (GraphOP.query.equals(op)) {
            dbBuilder.update();
        }
        if (GraphOP.update.equals(op)) {
            if (req.getLimit() > 1) {
                Object o = dbBuilder.one();
                resp.setData(o);
            } else {
                dbBuilder.page(req.getPage()).size(req.getLimit());
                resp.setData(dbBuilder.list());
            }
        }
        if (GraphOP.delete.equals(op)) {
            dbBuilder.delete();
        }
        if (GraphOP.insert.equals(op)) {
            dbBuilder.save();
        }
    }

    private void op(DBBuilder dbBuilder, GraphQuery graphQuery) {
        GraphParamOP op = GraphParamOP.valueOf(graphQuery.getOp());
        switch (op) {
            case eq:
                dbBuilder.eq(graphQuery.getK(), graphQuery.getV());
                return;
            case gt:
                dbBuilder.gt(graphQuery.getK(), graphQuery.getV());
                return;
            case lt:
                dbBuilder.lt(graphQuery.getK(), graphQuery.getV());
                return;
            case gte:
                dbBuilder.ge(graphQuery.getK(), graphQuery.getV());
                return;
            case lte:
                dbBuilder.le(graphQuery.getK(), graphQuery.getV());
                return;
            case set:
                dbBuilder.set(graphQuery.getK(), graphQuery.getV());
                return;
            case in:
                op.checkArray(graphQuery.getV().getClass());
                dbBuilder.in(graphQuery.getK(), graphQuery.getV());
                return;
            case nin:
                op.checkArray(graphQuery.getV().getClass());
                dbBuilder.notIn(graphQuery.getK(), graphQuery.getV());
                return;
            default:
                throw new RuntimeException("DBBuilder op error");
        }
    }
}
