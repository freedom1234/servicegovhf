package com.dc.esb.servicegov.entity;

import javax.persistence.*;

/**
 * 服务场景与服务头的关系表，服务头与场景是一对多的关系
 */
@Entity
@Table(name = "SERVICE_HEAD_RELATE")
public class ServiceHeadRelate {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "service_head_id")
    private String serviceHeadId;
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "operation_id")
    private String operationId;

    @ManyToOne()
    @JoinColumn(name = "service_head_id", insertable = false, updatable = false)
    private ServiceHead serviceHead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceHeadId() {
        return serviceHeadId;
    }

    public void setServiceHeadId(String serviceHeadId) {
        this.serviceHeadId = serviceHeadId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public ServiceHead getServiceHead() {
        return serviceHead;
    }

    public void setServiceHead(ServiceHead serviceHead) {
        this.serviceHead = serviceHead;
    }
}
