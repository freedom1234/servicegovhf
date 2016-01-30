package com.dc.esb.servicegov.vo;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ConfigVO {
    private String serviceId;
    private String serviceName;
    private String operationId;
    private String operationName;

    private String providerServiceInvokeId;
    private String consumerServiceInvokeId;

    private String providerName;
    private String consumerName;
    private String proIsStandard;
    private String conIsStandard;
    private String proInterfaceName;
    private String conInterfaceName;

    private String proGeneratorId;
    private String conGeneratorId;
    private String proGeneratorName;
    private String conGeneratorName;
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

    public String getProIsStandard() {
        return proIsStandard;
    }

    public void setProIsStandard(String proIsStandard) {
        this.proIsStandard = proIsStandard;
    }

    public String getConIsStandard() {
        return conIsStandard;
    }

    public void setConIsStandard(String conIsStandard) {
        this.conIsStandard = conIsStandard;
    }


    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProInterfaceName() {
        return proInterfaceName;
    }

    public void setProInterfaceName(String proInterfaceName) {
        this.proInterfaceName = proInterfaceName;
    }

    public String getConInterfaceName() {
        return conInterfaceName;
    }

    public void setConInterfaceName(String conInterfaceName) {
        this.conInterfaceName = conInterfaceName;
    }

    public String getProGeneratorId() {
        return proGeneratorId;
    }

    public void setProGeneratorId(String proGeneratorId) {
        this.proGeneratorId = proGeneratorId;
    }

    public String getConGeneratorId() {
        return conGeneratorId;
    }

    public void setConGeneratorId(String conGeneratorId) {
        this.conGeneratorId = conGeneratorId;
    }

    public String getProGeneratorName() {
        return proGeneratorName;
    }

    public void setProGeneratorName(String proGeneratorName) {
        this.proGeneratorName = proGeneratorName;
    }

    public String getConGeneratorName() {
        return conGeneratorName;
    }

    public void setConGeneratorName(String conGeneratorName) {
        this.conGeneratorName = conGeneratorName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getProviderServiceInvokeId() {
        return providerServiceInvokeId;
    }

    public void setProviderServiceInvokeId(String providerServiceInvokeId) {
        this.providerServiceInvokeId = providerServiceInvokeId;
    }

    public String getConsumerServiceInvokeId() {
        return consumerServiceInvokeId;
    }

    public void setConsumerServiceInvokeId(String consumerServiceInvokeId) {
        this.consumerServiceInvokeId = consumerServiceInvokeId;
    }
}
