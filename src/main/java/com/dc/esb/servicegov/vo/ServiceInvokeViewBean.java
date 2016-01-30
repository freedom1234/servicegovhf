package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.ServiceInvoke;

import javax.persistence.Column;

/**
 * Created by vincentfxz on 15/7/8.
 */
public class ServiceInvokeViewBean {
    private String invokeId;
    private String systemId;
    private String isStandard;
    private String serviceId;
    private String operationId;
    private String interfaceId;
    private String type;
    private String desc;
    private String remark;
    private String protocolId;
    private String interfaceName;
    private String systemName;

    public ServiceInvokeViewBean(ServiceInvoke si){
        this.invokeId = si.getInvokeId();
        this.systemId = si.getSystemId();
        this.isStandard = si.getIsStandard();
        this.serviceId = si.getServiceId();
        this.operationId = si.getOperationId();
        this.interfaceId = si.getInterfaceId();
        this.type = si.getType();
        this.desc = si.getDesc();
        this.remark = si.getRemark();
        this.protocolId = si.getProtocolId();
        if(null != si.getInter()){
            this.interfaceName = si.getInter().getInterfaceName();
        }
        this.systemName = si.getSystem().getSystemAb();
    }


    public String getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(String invokeId) {
        this.invokeId = invokeId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
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

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
