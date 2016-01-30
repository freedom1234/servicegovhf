package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by wang on 2015/8/18.
 */
@Entity
@Table(name="operation_log_type")
public class OperationLogType {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @Column(name = "ID")
    private  String id;
    @Column(name = "CLASS_NAME")
    private String className;
    @Column(name = "METHOD_NAME")
    private String methodName;
    @Column(name = "CHINESE_NAME")
    private String chineseName;
    @Column(name = "OPERATION_NAME")
    private String operationName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
