package com.dc.esb.servicegov.vo;

/**
 * Created by wang on 2015/8/14.
 */
public class ReleaseVO {
    private String systemId;//系统名称
    private String systemChineseName;//系统名称
    private String type;//类型1：消费者，0：提供者
    private String operationNum;//关联场景数
    private String serviceNum;//关联服务数
    private String operationReleaseNum;//发布场景数
    private String serviceReleaseNum;//发布服务数

    private String useNum;//调用数
    private String reuseRate;//复用率
    private String sum;//消费者或者提供者调用总数


    public String getType() {
        return type;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemChineseName() {
        return systemChineseName;
    }

    public void setSystemChineseName(String systemChineseName) {
        this.systemChineseName = systemChineseName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperationNum() {
        return operationNum;
    }

    public void setOperationNum(String operationNum) {
        this.operationNum = operationNum;
    }

    public String getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(String serviceNum) {
        this.serviceNum = serviceNum;
    }

    public String getUseNum() {
        return useNum;
    }

    public void setUseNum(String useNum) {
        this.useNum = useNum;
    }

    public String getReuseRate() {
        return reuseRate;
    }

    public void setReuseRate(String reuseRate) {
        this.reuseRate = reuseRate;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getOperationReleaseNum() {
        return operationReleaseNum;
    }

    public void setOperationReleaseNum(String operationReleaseNum) {
        this.operationReleaseNum = operationReleaseNum;
    }

    public String getServiceReleaseNum() {
        return serviceReleaseNum;
    }

    public void setServiceReleaseNum(String serviceReleaseNum) {
        this.serviceReleaseNum = serviceReleaseNum;
    }
}
