package com.dc.esb.servicegov.export.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public class MetadataNode {

    private String nodeId ;

    private String nodeName;

    private String chineseName;

    private String type;

    private String metadataId ;

    private List<MetadataNode> childreans;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public List<MetadataNode> getChildreans() {
        return childreans;
    }

    public void setChildreans(List<MetadataNode> childreans) {
        this.childreans = childreans;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
