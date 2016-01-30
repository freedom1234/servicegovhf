package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiangqi on 2015/9/10.
 */
@Entity
@Table(name="PERMISSION_CATEGORY")
public class PermissionCategory {
    @Id
    @Column(name="ID")
    private String Id;
    @Column(name="TEMP")
    private String temp;
    @Column(name="CHINESE_NAME")
    private String chineseName;
    @Column(name="PARENT_ID")
    private String parentId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
