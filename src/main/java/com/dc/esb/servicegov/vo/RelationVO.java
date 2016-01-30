package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.ServiceInvoke;

import java.util.ArrayList;
import java.util.List;

public class RelationVO {

    private String recordId;
    private String providerSystemId;
    private String consumerSystemAb;
    private String providerSystemAb;
    private String serviceId;
    private String operationId;
    private String type;
    private String interfaceId;
    private String functionType;
    private String passbySys;
    private String msgType;
    private String inMsgType;
    private String outMsgType;
    private List<String> msgConvert = new ArrayList<String>();

    public RelationVO(ServiceInvoke consumer, ServiceInvoke provider){
        this.recordId = consumer.getInvokeId() + "_" + provider.getInvokeId();
        this.providerSystemId = provider.getSystemId();
        this.consumerSystemAb = consumer.getSystem().getSystemAb();
        this.providerSystemAb = provider.getSystem().getSystemAb();
        this.serviceId = consumer.getServiceId();
        this.operationId = consumer.getOperationId();
        this.interfaceId = consumer.getInterfaceId();
    }

    public RelationVO(ServiceInvoke sr) {
        this.recordId = sr.getInvokeId();
//        this.providerSystemId = sr.getProviderSystemId();
//        this.consumerSystemAb = sr.getConsumerSystemAb();
        this.serviceId = sr.getServiceId();
        this.operationId = sr.getOperationId();
        this.type = sr.getType();
        this.interfaceId = sr.getInterfaceId();
//        this.functionType = sr.getFunctionType();
//        this.passbySys = sr.getPassbySys();
    }

//    public RelationVO(SvcAsmRelateView sr) {
//        this.inMsgType = sr.getConsumeMsgType();
//        this.outMsgType = sr.getProvideMsgType();
//        String o = this.inMsgType + "-" + this.outMsgType;
//        if (!this.msgConvert.contains(o)) {
//            this.msgConvert.add(o);
//        }
//        this.providerSystemAb = sr.getPrdSysAB();
//        this.providerSystemId = sr.getPrdSysID();
//        this.consumerSystemAb = sr.getCsmSysAB();
//        this.serviceId = sr.getServiceId();
//        this.operationId = sr.getOperationId();
//        this.interfaceId = sr.getInterfaceId();
//        if (null == sr.getPassbySysAB()) {
//            this.passbySys = "";
//        } else {
//            this.passbySys = sr.getPassbySysAB();
//        }
//        this.type = sr.getDirection();
//        this.msgType = sr.getProvideMsgType();
//    }

    public RelationVO(String providerSystemId, String consumerSystemAb, String providerSystemAb, String serviceId, String operationId, String interfaceId) {
        this.providerSystemId = providerSystemId;
        this.consumerSystemAb = consumerSystemAb;
        this.providerSystemAb = providerSystemAb;
        this.serviceId = serviceId;
        this.operationId = operationId;
        this.interfaceId = interfaceId;
    }

    public List<String> getMsgConvert() {
        return msgConvert;
    }

    public void setMsgConvert(List<String> msgConvert) {
        this.msgConvert = msgConvert;
    }

    public String getInMsgType() {
        return inMsgType;
    }

    public void setInMsgType(String inMsgType) {
        this.inMsgType = inMsgType;
    }

    public String getOutMsgType() {
        return outMsgType;
    }

    public void setOutMsgType(String outMsgType) {
        this.outMsgType = outMsgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getProviderSystemId() {
        return providerSystemId;
    }

    public void setProviderSystemId(String providerSystemId) {
        this.providerSystemId = providerSystemId;
    }

    public String getProviderSystemAb() {
        return providerSystemAb;
    }

    public void setProviderSystemAb(String providerSystemAb) {
        this.providerSystemAb = providerSystemAb;
    }

    public String getConsumerSystemAb() {
        return consumerSystemAb;
    }

    public void setConsumerSystemAb(String consumerSystemAb) {
        this.consumerSystemAb = consumerSystemAb;
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

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getPassbySys() {
        return passbySys;
    }

    public void setPassbySys(String passbySys) {
        this.passbySys = passbySys;
    }

}
