package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by vincentfxz on 15/9/14.
 */
@Entity
@Table(name = "SG_PROCESS_CONTEXT")
public class ProcessContext {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;
    @Column(name = "KEY")
    private String key;
    @Column(name = "VALUE")
    private String value;
    @Column(name="REMARK")
    private String remark;
    @Column(name = "PROCESS_ID")
    private String processId;
    @Column(name="NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;
    @Column(name="OPT_DATE")
    private String optDate;
    @Column(name="OPT_USER")
    private String optUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }
}
