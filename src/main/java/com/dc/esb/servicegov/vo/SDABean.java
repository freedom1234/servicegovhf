package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.SDA;

/**
 * Created by wang on 2015/8/20.
 */
public class SDABean {
    private String sdaId;

    private String structName;

    private String structAlias;

    private String metadataId;

    private int seq = 0;

    private String parentId;

    private String serviceId;

    private String optUser;

    private String optDate;

    private String operationId;

    private String desc;

    private String remark;

    private String headId;

    private String version;

    private String type;

    private String length;

    private String required;

    private String argType;

    public SDABean(SDA sda){
        setSdaId(sda.getId());
        setStructName(sda.getStructName());
        setStructAlias(sda.getStructAlias());
        setMetadataId(sda.getMetadataId());
        setSeq(sda.getSeq());
        setParentId(sda.getParentId());
        setServiceId(sda.getServiceId());
        setOptUser(sda.getOptUser());
        setOptDate(sda.getOptDate());
        setOperationId(sda.getOperationId());
        setDesc(sda.getDesc());
        setRemark(sda.getRemark());
        setHeadId(sda.getHeadId());
        setVersion(sda.getVersion());
        setType(sda.getType());
        setLength(sda.getLength());
        setRequired(sda.getRequired());
        setArgType(sda.getArgType());
    }

    public String getSdaId() {
        return sdaId;
    }

    public void setSdaId(String sdaId) {
        this.sdaId = sdaId;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public String getStructAlias() {
        return structAlias;
    }

    public void setStructAlias(String structAlias) {
        this.structAlias = structAlias;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType = argType;
    }

}
