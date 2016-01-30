package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.ExcelTool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;

/**
 * 用于存储index页记录
 */
public class MappingImportIndexRowVO {
    private int indexNum;
    private String interfaceId;//接口ID
    private String interfaceName;//交易码
    private String serviceName;//服务名称
    private String serviceId;//服务id
    private String operationId;//场景id
    private String operationName;//场景名称
    private String consumerAbs;//调用方
    private String consumerNames;//调用方
    private String providerAbs;//提供方
    private String type;//接口方向
    private String interfaceProId; //接口提供系统id
    private String protocolType;//报文类型
    private String optUser;//处理人
    private String optDate;//处理时间
    private String protocolTran;//报文转换方向
    private String isUsed;//是否已有调用
    private String fileName;//参考文档
    private String module;//模块划分
    private String isPent;//是否穿透
    private String interfaceHeadName;//报文头名称
    private String interfaceState;//交易状态
    private String operationState;//场景状态
    private String isStandard;//是否标准
    private String remark;//备注

    public MappingImportIndexRowVO(){}

    public MappingImportIndexRowVO(int indexNum, MappingIndexHeadIndexVO headRowVO, Row row) {
        this.indexNum = indexNum;
        this.interfaceId = getCell(row, headRowVO.interfaceIdIndex);
        this.interfaceName = getCell(row, headRowVO.interfaceNameIndex);
        String servcieNameStr = getCell(row, headRowVO.serviceNameIndex);//服务名称：服务名称+（服务码）
        if(StringUtils.isNotEmpty(servcieNameStr)){
            servcieNameStr = servcieNameStr.replaceAll("（", "(");
            servcieNameStr = servcieNameStr.replaceAll("）", ")");
            int index1 = servcieNameStr.indexOf("(");
            int index2 = servcieNameStr.indexOf(")");
            if(index1 > 0){
                this.serviceName = servcieNameStr.substring(0, index1);
                if(index2 > 0){
                    this.serviceId = servcieNameStr.substring(index1+1, index2);
                }
            }
        }

        String operationNameid = getCell(row, headRowVO.operationNameIndex);//场景名称：场景名称+（场景码）
        if(StringUtils.isNotEmpty(operationNameid)){
            operationNameid = operationNameid.replaceAll("（", "(");
            operationNameid = operationNameid.replaceAll("）", ")");
            int index1 = operationNameid.indexOf("(");
            int index2 = operationNameid.indexOf(")");
            if(index1 > 0){
                this.operationName = operationNameid.substring(0, index1);
                if(index2 > 0){
                    this.operationId = operationNameid.substring(index1+1, index2);
                }
            }
        }
//        this.operationId = getCell(row, headRowVO.operationIdIndex);
//        this.operationName = getCell(row, headRowVO.operationNameIndex);
        this.consumerAbs = getCell(row, headRowVO.consumerAbsIndex);
        this.consumerNames = getCell(row, headRowVO.consumerNamesIndex);
        this.providerAbs = getCell(row, headRowVO.providerAbsIndex);
        this.interfaceProId = getCell(row, headRowVO.interfaceProIdIndex);
        this.type =  Constants.getInvokeType(getCell(row, headRowVO.typeIndex));
        this.protocolType = getCell(row, headRowVO.protocolIndex);
        this.optUser = getCell(row, headRowVO.optUserIndex);
        this.optDate = getCell(row, headRowVO.optDateIndex);
        this.protocolTran = getCell(row, headRowVO.protocolTranIndex);
        this.isUsed = getCell(row, headRowVO.isUsedIndex);
        this.fileName = getCell(row, headRowVO.fileIndex);
        this.module = getCell(row, headRowVO.moduleIndex);
        this.isPent = getCell(row, headRowVO.isPentIndex);
        this.interfaceHeadName = getCell(row, headRowVO.interfaceHeadIndex);
        this.interfaceState = Constants.getInterfaceStatus(getCell(row, headRowVO.interfaceStateIndex));
        this.operationState =  Constants.Operation.getState(getCell(row, headRowVO.operationStateIndex));
        this.isStandard = getCell(row, headRowVO.isStandardIndex);
        if("是".equals(isStandard)){
            this.isStandard = Constants.INVOKE_TYPE_STANDARD_Y;
        }else{
            this.isStandard = Constants.INVOKE_TYPE_STANDARD_N;

        }
        this.remark = getCell(row, headRowVO.remarkIndex);
    }


    protected String getCell(Row row, int col) {
        return ExcelTool.getInstance().getCellContent(
                row.getCell(col));
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

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }


    public String getConsumerNames() {
        return consumerNames;
    }

    public void setConsumerNames(String consumerNames) {
        this.consumerNames = consumerNames;
    }

    public String getConsumerAbs() {
        return consumerAbs;
    }

    public void setConsumerAbs(String consumerAbs) {
        this.consumerAbs = consumerAbs;
    }

    public String getProviderAbs() {
        return providerAbs;
    }

    public void setProviderAbs(String providerAbs) {
        this.providerAbs = providerAbs;
    }

    public String getInterfaceProId() {
        return interfaceProId;
    }

    public void setInterfaceProId(String interfaceProId) {
        this.interfaceProId = interfaceProId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getProtocolTran() {
        return protocolTran;
    }

    public void setProtocolTran(String protocolTran) {
        this.protocolTran = protocolTran;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIsPent() {
        return isPent;
    }

    public void setIsPent(String isPent) {
        this.isPent = isPent;
    }

    public String getInterfaceHeadName() {
        return interfaceHeadName;
    }

    public void setInterfaceHeadName(String interfaceHeadName) {
        this.interfaceHeadName = interfaceHeadName;
    }

    public String getInterfaceState() {
        return interfaceState;
    }

    public void setInterfaceState(String interfaceState) {
        this.interfaceState = interfaceState;
    }

    public String getOperationState() {
        return operationState;
    }

    public void setOperationState(String operationState) {
        this.operationState = operationState;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }
}
