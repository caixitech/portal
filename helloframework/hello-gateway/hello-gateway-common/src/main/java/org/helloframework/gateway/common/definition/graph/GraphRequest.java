package org.helloframework.gateway.common.definition.graph;

import org.helloframework.docs.annotation.DocApiFiled;

import java.util.List;

public class GraphRequest {
    @DocApiFiled(desc = "获取条目",example = "10")
    private Integer limit;
    @DocApiFiled(desc = "页码，默认1取第一页",example = "1")
    private Integer page;
    @DocApiFiled(desc = "跳过，和page参数互斥，冲突时以skip为主",example = "0")
    private Integer skip;
    @DocApiFiled(desc = "查询条件",type = "List<GraphQuery>")
    private List<GraphQuery> query;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public List<GraphQuery> getQuery() {
        return query;
    }

    public void setQuery(List<GraphQuery> query) {
        this.query = query;
    }
}
