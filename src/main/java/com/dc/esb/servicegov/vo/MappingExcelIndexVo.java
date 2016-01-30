package com.dc.esb.servicegov.vo;

import java.util.ArrayList;
import java.util.List;

public class MappingExcelIndexVo {
	private String interfaceId;
	private String interfaceName;
	private String serviceId;
	private String serviceName;
	private String operationId;
	private String operationName;
	private String consumerSysAb;
	private String providerSysAb;
	private String type;
	private String msgType;
	private String serviceRemark;
	private String operationRemark;
	private String providerSysId;
	private List<String> msgConvert = new ArrayList<String>();
	
	public List<String> getMsgConvert() {
		return msgConvert;
	}
	public void setMsgConvert(List<String> msgConvert) {
		this.msgConvert = msgConvert;
	}
	public String getOperationRemark() {
		return operationRemark;
	}
	public void setOperationRemark(String operationRemark) {
		this.operationRemark = operationRemark;
	}
	public String getServiceRemark() {
		return serviceRemark;
	}
	public void setServiceRemark(String serviceRemark) {
		this.serviceRemark = serviceRemark;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	public String getConsumerSysAb() {
		return consumerSysAb;
	}
	public void setConsumerSysAb(String consumerSysAb) {
		this.consumerSysAb = consumerSysAb;
	}
	public String getProviderSysAb() {
		return providerSysAb;
	}
	public void setProviderSysAb(String providerSysAb) {
		this.providerSysAb = providerSysAb;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getProviderSysId() {
		return providerSysId;
	}
	public void setProviderSysId(String providerSysId) {
		this.providerSysId = providerSysId;
	}
	
	

}
