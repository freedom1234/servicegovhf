package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Entity
@Table(name="SG_MENU")
public class SGMenu implements Serializable {
    @Id
    @Column(name="ID")
    private String Id;
    @Column(name="SG_MENU_CATEGORY_ID")
    private String sgMenuCategoryId;
    @Column(name="NAME")
    private String name;
    @Column(name="PERMISSION_ID")
    private String permissionId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSgMenuCategoryId() {
        return sgMenuCategoryId;
    }

    public void setSgMenuCategoryId(String sgMenuCategoryId) {
        this.sgMenuCategoryId = sgMenuCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
