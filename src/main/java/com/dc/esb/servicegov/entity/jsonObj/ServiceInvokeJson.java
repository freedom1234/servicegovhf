package com.dc.esb.servicegov.entity.jsonObj;

import com.dc.esb.servicegov.entity.ServiceInvoke;

/**
 * lw
 */
public class ServiceInvokeJson {
    private String invokeId;

    private String systemId;

    private String isStandard;

    private String serviceId;

    private String operationId;

    private String interfaceId;

    private String type;

    private String desc;

    private String remark;

    private String interfaceName;

    private String systemChineseName;

    public ServiceInvokeJson(){

    }

    public ServiceInvokeJson(String invokeId, String systemId, String isStandard, String serviceId, String operationId, String interfaceId, String type, String desc, String remark, String interfaceName, String systemChineseName) {
        this.invokeId = invokeId;
        this.systemId = systemId;
        this.isStandard = isStandard;
        this.serviceId = serviceId;
        this.operationId = operationId;
        this.interfaceId = interfaceId;
        this.type = type;
        this.desc = desc;
        this.remark = remark;
        this.interfaceName = interfaceName;
        this.systemChineseName = systemChineseName;
    }

    public ServiceInvokeJson(ServiceInvoke si) {
        this.invokeId = si.getInvokeId();
        this.systemId = si.getSystemId();
        this.isStandard = si.getIsStandard();
        this.serviceId = si.getServiceId();
        this.operationId = si.getOperationId();
        this.interfaceId = si.getInterfaceId();
        this.type = si.getType();
        this.desc = si.getDesc();
        this.remark = si.getRemark();
        if(si.getInter() != null){
            this.interfaceName = si.getInter().getInterfaceName();
        }
        else{
            this.interfaceName = "";
        }
        if(si.getSystem() != null){
            this.systemChineseName = si.getSystem().getSystemChineseName();
        }else{
            this.systemChineseName = "";
        }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
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

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getSystemChineseName() {
        return systemChineseName;
    }

    public void setSystemChineseName(String systemChineseName) {
        this.systemChineseName = systemChineseName;
    }
}
