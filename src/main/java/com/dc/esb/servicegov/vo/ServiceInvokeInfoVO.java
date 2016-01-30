package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.ServiceInvoke;

/**
 * Created by vincentfxz on 15/7/9.
 */
public class ServiceInvokeInfoVO {
    private String interfaceId;
    private String interfaceName;
    private String serviceId;
    private String serviceName;
    private String operationId;
    private String operationName;
    private String invokeType;
    private String nodeType;
    private String bussCategory;
    private String status;
    private String id;

    public ServiceInvokeInfoVO(ServiceInvoke serviceInvoke){
        this.interfaceId = serviceInvoke.getInterfaceId();
        this.serviceId = serviceInvoke.getServiceId();
        this.interfaceId = serviceInvoke.getInterfaceId();
        if(null != serviceInvoke.getInter()){
            this.interfaceName = serviceInvoke.getInter().getInterfaceName();
        }
        this.operationId = serviceInvoke.getOperationId();
        this.id = serviceInvoke.getInvokeId();
        if(serviceInvoke.getType().equals("0")){
            this.invokeType = "提供者";
        }else if(serviceInvoke.getType().equals("1")){
            this.invokeType = "消费者";
        }else{
            this.invokeType = serviceInvoke.getType();
        }
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getBussCategory() {
        return bussCategory;
    }

    public void setBussCategory(String bussCategory) {
        this.bussCategory = bussCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        this.invokeType = invokeType;
    }
}
