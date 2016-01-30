package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by vincentfxz on 15/9/7.
 */
@Entity
@Table(name = "SERVICE_LINK_NODE")
public class ServiceLinkNode {

    @Id
    @Column(name = "INVOKE_ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column(name = "SERVICE_INVOKE_ID")
    private String serviceInvokeId;

    @Column(name = "BUSS_CATEGORY")
    private String bussCategory;

    @Column(name="ESB_ACCESS_PATTERN")
    private String esbAccessPattern;

    @Column(name="CONDITION")
    private String condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceInvokeId() {
        return serviceInvokeId;
    }

    public void setServiceInvokeId(String serviceInvokeId) {
        this.serviceInvokeId = serviceInvokeId;
    }

    public String getEsbAccessPattern() {
        return esbAccessPattern;
    }

    public void setEsbAccessPattern(String esbAccessPattern) {
        this.esbAccessPattern = esbAccessPattern;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getBussCategory() {
        return bussCategory;
    }

    public void setBussCategory(String bussCategory) {
        this.bussCategory = bussCategory;
    }
}
