package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OLA")
public class OLA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OLA_ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String olaId;

    @Column(name = "OLA_NAME")
    private String olaName;

    @Column(name = "OLA_VALUE")
    private String olaValue;

    @Column(name = "OLA_DESC")
    private String olaDesc;

    @Column(name = "OLA_REMARK")
    private String olaRemark;

    @Column(name = "OPERATION_ID")
    private String operationId;

    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "OLA_TEMPLATE_ID")
    private String olaTemplateId;

    @Column(name = "VERSION")
    private String version;

    @ManyToOne(targetEntity = Operation.class)
    @JoinColumns({
            @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID", insertable = false, updatable = false),
            @JoinColumn(name = "OPERATION_ID", referencedColumnName = "OPERATION_ID", insertable = false, updatable = false)
    })
    private Operation operation;


    public String getOlaId() {
        return olaId;
    }

    public void setOlaId(String olaId) {
        this.olaId = olaId;
    }

    public String getOlaName() {
        return olaName;
    }

    public void setOlaName(String olaName) {
        this.olaName = olaName;
    }

    public String getOlaValue() {
        return olaValue;
    }

    public void setOlaValue(String olaValue) {
        this.olaValue = olaValue;
    }

    public String getOlaDesc() {
        return olaDesc;
    }

    public void setOlaDesc(String olaDesc) {
        this.olaDesc = olaDesc;
    }

    public String getOlaRemark() {
        return olaRemark;
    }

    public void setOlaRemark(String olaRemark) {
        this.olaRemark = olaRemark;
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

    public String getOlaTemplateId() {
        return olaTemplateId;
    }

    public void setOlaTemplateId(String olaTemplateId) {
        this.olaTemplateId = olaTemplateId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SLA)) {
            return false;
        }
        OLA another = (OLA) obj;
        return ((null == this.serviceId) ? (null == another.getServiceId()) : (this.serviceId.equals(another.getServiceId()))) &&
                ((null == this.operationId) ? (null == another.getOperationId()) : (this.operationId.equals(another.getOperationId()))) &&
                ((null == this.olaName) ? (null == another.getOlaName()) : (this.olaName.equals(another.getOlaName())));
    }
}
