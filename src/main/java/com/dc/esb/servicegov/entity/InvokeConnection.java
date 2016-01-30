package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by vincentfxz on 15/7/13.
 */
@Entity
@Table(name = "INVOKE_CONNECTION")
public class InvokeConnection {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(name = "SOURCE_ID", length = 50)
    private String sourceId;
    @Column(name = "SOURCE_TYPE", length = 50)
    private String sourceType;
    @Column(name = "TARGET_ID", length = 50)
    private String targetId;
    @Column(name="TARGET_TYPE", length=50)
    private String targetType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
