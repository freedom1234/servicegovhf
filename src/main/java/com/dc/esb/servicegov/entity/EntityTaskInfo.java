package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by vincentfxz on 15/7/7.
 */
@Entity
@Table(name="ENTITY_TASK_INFO")
public class EntityTaskInfo {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;
    @Column(name="ENTITY_TYPE")
    private String entityType;
    @Column(name="ENTITY_ID")
    private String entityId;
    @Column(name="PROCESS_ID")
    private String processId;
    @Column(name="TASK_ID")
    private String taskId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
