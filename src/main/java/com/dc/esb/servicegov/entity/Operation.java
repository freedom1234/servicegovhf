package com.dc.esb.servicegov.entity;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "OPERATION")
@IdClass(OperationPK.class)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OPERATION_ID")
    private String operationId;
    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "OPERATION_NAME")
    private String operationName;

    @Column(name = "OPERATION_DESC", length=2048)
    private String operationDesc;

    @Column(name = "OPERATION_REMARK")
    private String operationRemark;

    @Column(name = "VERSION_ID")
    private String versionId;

    @Column(name = "STATE", length = 20)
    private String state;

    @Column(name = "LIFE_CYCLE_STATE", length = 20)
    private String lifeCyscleState;

    @Column(name = "OPT_USER", length = 100)
    private String optUser;

    @Column(name = "OPT_DATE", length = 50)
    private String optDate;

    @Column(name = "RANGE_AREA")
    private String range;

    @Column(name = "HEAD_ID")
    private String headId;
    @Column(name = "DELETED", length = 10)
    private String deleted;
    @ManyToOne(targetEntity = Service.class)
    @JoinColumns({
            @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID", insertable = false, updatable = false)
    })
    private Service service;

    @ManyToOne()
    @JoinColumn(name = "VERSION_ID", insertable = false, updatable = false)
    private Version version;

    @Column(name = "PROCESS_ID", length = 50)
    private String processId;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        if(StringUtils.isNotEmpty(operationDesc)){
            if(operationDesc.length() > 1024){
                this.operationDesc = operationDesc.substring(0, 1023);
                return;
            }
        }
        this.operationDesc = operationDesc;
    }

    public String getOperationRemark() {
        return operationRemark;
    }

    public void setOperationRemark(String operationRemark) {
        this.operationRemark = operationRemark;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getLifeCyscleState() {
        return lifeCyscleState;
    }

    public void setLifeCyscleState(String lifeCyscleState) {
        this.lifeCyscleState = lifeCyscleState;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
