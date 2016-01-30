package com.dc.esb.servicegov.util;

import java.util.List;

/**
 * Created by vincentfxz on 15/9/21.
 * 交易链路 节点的VO
 *
 */
public class GraphNode {
    private String id;
    private String name;
    private String type;
    private String left;
    private String top;
    private List<GraphColumn> columns;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public List<GraphColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<GraphColumn> columns) {
        this.columns = columns;
    }


}
