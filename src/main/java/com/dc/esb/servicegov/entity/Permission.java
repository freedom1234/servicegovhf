package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * Created by vincentfxz on 15/7/9.
 */
@Entity
@Table(name="PERMISSION")
public class Permission {
    @Id
    @Column(name="ID")
    private String Id;
    @Column(name="NAME")
    private String name;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="TEMP")
    private String temp;
    @Column(name="CHINESE_NAME")
    private String chineseName;
    @Column(name="CHINESE_DESCRIPTION")
    private String chineseDescription;
    @Column(name="CATEGORY_ID")
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getChineseDescription() {
        return chineseDescription;
    }

    public void setChineseDescription(String chineseDescription) {
        this.chineseDescription = chineseDescription;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
}
