package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.ServiceInvoke;

/**
 * Created by vincentfxz on 15/9/7.
 */
public class ServiceLinkNodeVO {
    /**
     * 对应的SERVICE_INVOKE表的ID
     */
    private String invokeId;
    /**
     * 接口ID
     */
    private String interfaceId;
    /**
     *   交易名称
     */
    private String ecode;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 场景ID
     */
    private String operationId;
    /**
     * 场景名称
     */
    private String operationName;
    /**
     * 调用类型，消费方或者提供方
     */
    private String invokeType;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 交易属性标识
     */
    private String location;
    /**
     * 节点业务分类
     */
    private String bussCategory;
    /**
     * 节点状态
     */
    private String status;

    private String id;
    /**
     * ESB标识
     */
    private String esbAccessPattern;
    /**
     * 条件位
     */
    private String condition;
    /**
     * 条件信息
     */
    private String connectionDesc;


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

    public ServiceLinkNodeVO(ServiceInvoke serviceInvoke){
        this.invokeId = serviceInvoke.getInvokeId();
        this.interfaceId = serviceInvoke.getInterfaceId();
        this.serviceId = serviceInvoke.getServiceId();
        this.interfaceId = serviceInvoke.getInterfaceId();
        if(null != serviceInvoke.getInter()){
            this.interfaceName = serviceInvoke.getInter().getInterfaceName();
            this.ecode = serviceInvoke.getInter().getEcode();
        }
        this.operationId = serviceInvoke.getOperationId();
        this.id = serviceInvoke.getInvokeId();
        if("0".equals(serviceInvoke.getType())){
            this.invokeType = "提供者";
        }else if("1".equals(serviceInvoke.getType())){
            this.invokeType = "消费者";
        }else{
            this.invokeType = serviceInvoke.getType();
        }
    }

    public String getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(String invokeId) {
        this.invokeId = invokeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConnectionDesc() {
        return connectionDesc;
    }

    public void setConnectionDesc(String connectionDesc) {
        this.connectionDesc = connectionDesc;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }
}
