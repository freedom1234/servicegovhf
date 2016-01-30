package com.dc.esb.servicegov.export.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/15.
 */
public class ExportBean implements Serializable {

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 场景ID
     */
    private String operationId;

    /**
     * 提供方系统ID
     */
    private String providerSystemId;


    /**
     * 提供方接口ID
     */
    private String providerInterfaceId;

    /**
     * 提供方是否标准接口
     */
    private boolean providerIsStandard;


    /**
     * 消费方系统ID
     */
    private String consumerSystemId;


    /**
     * 消费方接口ID
     */
    private String consumerInterfaceId;

    /**
     * 消费方是否标准接口
     */
    private boolean consumerIsStandard;

    public ExportBean() {
    }

    public ExportBean(String serviceId, String operationId, String providerSystemId, String providerInterfaceId, boolean providerIsStandard, String consumerSystemId, String consumerInterfaceId, boolean consumerIsStandard) {
        this.serviceId = serviceId;
        this.operationId = operationId;
        this.providerSystemId = providerSystemId;
        this.providerInterfaceId = providerInterfaceId;
        this.providerIsStandard = providerIsStandard;
        this.consumerSystemId = consumerSystemId;
        this.consumerInterfaceId = consumerInterfaceId;
        this.consumerIsStandard = consumerIsStandard;
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

    public String getProviderSystemId() {
        return providerSystemId;
    }

    public void setProviderSystemId(String providerSystemId) {
        this.providerSystemId = providerSystemId;
    }

    public String getProviderInterfaceId() {
        return providerInterfaceId;
    }

    public void setProviderInterfaceId(String providerInterfaceId) {
        this.providerInterfaceId = providerInterfaceId;
    }

    public boolean isProviderIsStandard() {
        return providerIsStandard;
    }

    public void setProviderIsStandard(boolean providerIsStandard) {
        this.providerIsStandard = providerIsStandard;
    }

    public String getConsumerSystemId() {
        return consumerSystemId;
    }

    public void setConsumerSystemId(String consumerSystemId) {
        this.consumerSystemId = consumerSystemId;
    }

    public String getConsumerInterfaceId() {
        return consumerInterfaceId;
    }

    public void setConsumerInterfaceId(String consumerInterfaceId) {
        this.consumerInterfaceId = consumerInterfaceId;
    }

    public boolean isConsumerIsStandard() {
        return consumerIsStandard;
    }

    public void setConsumerIsStandard(boolean consumerIsStandard) {
        this.consumerIsStandard = consumerIsStandard;
    }
}
