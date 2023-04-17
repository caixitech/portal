package org.helloframework.gateway.common.definition.graph;

import org.helloframework.gateway.common.annotation.graph.GraphOP;

import java.util.HashMap;
import java.util.Map;

public class Graphs {
    //基础对象名称
    private Class cls;
    //图支持的操作
    private Map<GraphOP, Integer> graphs;
    //k为数据库查询字段 key=字段+op
    private Map<String, Integer> graphsOPs;

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public void push(GraphOP graphOP) {
        if (graphs == null) {
            graphs = new HashMap();
        }
        graphs.put(graphOP, 1);
    }

    public void push(String key) {
        if (graphsOPs == null) {
            graphsOPs = new HashMap();
        }
        graphsOPs.put(key, 1);
    }

    public Map<GraphOP, Integer> getGraphs() {
        return graphs;
    }

    public void setGraphs(Map<GraphOP, Integer> graphs) {
        this.graphs = graphs;
    }

    public Map<String, Integer> getGraphsOPs() {
        return graphsOPs;
    }

    public void setGraphsOPs(Map<String, Integer> graphsOPs) {
        this.graphsOPs = graphsOPs;
    }
}
