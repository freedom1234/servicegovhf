package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.Interface;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by wang on 2015/8/12.
 */
public class InterfaceInvokeVO {
    private String serviceId;
    private String operationId;

    private String consumers;
    private String providers;

    private String type;
    private String interfaceId;
    private String isStandard;
    private String ecode;
    private String interfaceName;

    private String consumerIds;
    private String providerIds;

    private String consumerNames;
    private String providerNames;
    private String consumerInterfaceId;

    public InterfaceInvokeVO(){}

    public InterfaceInvokeVO(String serviceId, String operationId, String type, Interface inter) {
        this.serviceId = serviceId;
        this.operationId = operationId;
        this.type = type;
        this.interfaceId = inter.getInterfaceId();
        this.ecode = inter.getEcode();
        this.interfaceName = inter.getInterfaceName();
    }

    public String getConsumerNames() {
        return consumerNames;
    }

    public void setConsumerNames(String consumerNames) {
        this.consumerNames = consumerNames;
    }

    public String getProviderNames() {
        return providerNames;
    }

    public void setProviderNames(String providerNames) {
        this.providerNames = providerNames;
    }

    public String getConsumerInterfaceId() {
        return consumerInterfaceId;
    }

    public void setConsumerInterfaceId(String consumerInterfaceId) {
        this.consumerInterfaceId = consumerInterfaceId;
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

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getConsumerIds() {
        return consumerIds;
    }

    public void setConsumerIds(String consumerIds) {
        this.consumerIds = consumerIds;
    }

    public String getProviderIds() {
        return providerIds;
    }

    public void setProviderIds(String providerIds) {
        this.providerIds = providerIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null && obj instanceof  InterfaceInvokeVO){
            InterfaceInvokeVO vo = (InterfaceInvokeVO)obj;
            if(vo.getServiceId().equals(serviceId) && vo.getOperationId().equals(operationId)){
                if(vo.getType().equals(type)){
                    if(StringUtils.isNotEmpty(vo.getInterfaceId()) && vo.getInterfaceId().equals(interfaceId)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
