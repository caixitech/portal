package org.helloframework.gateway.common.definition.apiservice;

import org.helloframework.gateway.common.annotation.graph.GraphOP;
import org.helloframework.gateway.common.annotation.graph.GraphParamOP;
import org.helloframework.gateway.common.definition.graph.*;
import org.helloframework.gateway.common.exception.ApiException;
import org.helloframework.gateway.common.exception.GateWayCode;
import org.helloframework.gateway.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lanjian
 */
public abstract class AbstractGraphApiService<I extends GraphRequest, O extends GraphResponse> extends AbstractApiService<I, O> {

    /**
     * 预留模板代理
     */
    public void handler(I req, O resp, ApiExtend extend) {
        service(req, resp, extend);
    }

    public abstract GraphInvoker graphInvoker();

    @Override
    public void service(I req, O resp, ApiExtend extend) {
        try {
            String api = extend.getApi();
            String domain = api.substring(0, api.lastIndexOf("."));
            GraphOP op = op(api);
            Class cls = Class.forName(domain);
            Map<String, Graphs> graph = extend.getGraphs();
            Graphs graphs = graph.get(domain);
            if (graphs == null) {
                throw new ApiException(GateWayCode.GRAPH_ERROR);
            }
            if (!graphs.getGraphs().get(op).equals(1)) {
                //不支持这个图操作
                throw new ApiException(GateWayCode.GRAPH_OP_ERROR);
            }
            GraphInvoker graphInvoker = graphInvoker();
            if (graphInvoker == null) {
                throw new ApiException(GateWayCode.GRAPH_INVOKER_ERROR);
            }
            List<GraphQuery> queries = req.getQuery();
            //这一段代码只是为了验证 不越过边界 指定条件 非全条件暴露
            Map<String, Integer> graphsOPMap = graphs.getGraphsOPs();
            for (GraphQuery query : queries) {
                //默认可以不传op  不传op默认为eq
                if (StringUtils.isBlank(query.getOp())) {
                    query.setOp(GraphParamOP.eq.name());
                }
                //判断是否支持这个字段查询
                GraphParamOP graphParamOP = GraphParamOP.valueOf(query.getOp());
                if (!graphsOPMap.get(graphParamOP.opName(query.getK())).equals(1)) {
                    throw new ApiException(GateWayCode.GRAPH_CRITERIA_ERROR);
                }
            }
            graphInvoker.invoke(req, resp, cls, op);
        } catch (ClassNotFoundException e) {
            throw new ApiException(GateWayCode.GRAPH_CLASS_ERROR);
        }
    }

    public static GraphOP op(String api) {
        String info = api.substring(api.lastIndexOf(".")).toLowerCase();
        if (info.contains(GraphOP.delete.name())) {
            return GraphOP.delete;
        }
        if (info.contains(GraphOP.insert.name())) {
            return GraphOP.insert;
        }
        if (info.contains(GraphOP.update.name())) {
            return GraphOP.update;
        }
        if (info.contains(GraphOP.query.name())) {
            return GraphOP.query;
        }
        throw new ApiException(GateWayCode.GRAPH_OP_NOT_MATCH_ERROR);
    }
}
