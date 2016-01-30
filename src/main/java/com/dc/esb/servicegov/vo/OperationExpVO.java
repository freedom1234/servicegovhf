package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.Operation;

/**
 * Created by wang on 2015/8/11.
 */
public class OperationExpVO {
    private String serviceId;
    private String serviceName;
    private String serviceDesc;
    private String operationId;
    private String operationName;
    private String operationDesc;
    private String consumers;
    private String providers;
    private String version;
    private String versionRemark;
    private String optDate;
    private String optUser;
    private String optState;
    public OperationExpVO(){}
    public OperationExpVO(Operation o) {
        this.serviceId = o.getServiceId();
        this.serviceName = o.getService().getServiceName();
        this.serviceDesc = o.getService().getDesc();
        this.operationId = o.getOperationId();
        this.operationName = o.getOperationName();
        this.operationDesc = o.getOperationDesc();
        if(o.getVersion() != null){
            this.version = o.getVersion().getCode();
            this.versionRemark = o.getVersion().getRemark();
        }
        this.optDate = o.getOptDate();
        this.optUser = o.getOptUser();
        this.optState = o.getState();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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
        this.operationDesc = operationDesc;
    }

    public String getConsumers() {
        return consumers;
    }

    public void setConsumers(String consumers) {
        this.consumers = consumers;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public String getOptState() {
        return optState;
    }

    public void setOptState(String optState) {
        this.optState = optState;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getVersionRemark() {
        return versionRemark;
    }

    public void setVersionRemark(String versionRemark) {
        this.versionRemark = versionRemark;
    }
}
