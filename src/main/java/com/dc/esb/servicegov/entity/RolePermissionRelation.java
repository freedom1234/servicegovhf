package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lenovo on 2015/7/13.
 */
@Entity
@Table(name = "ROLE_PERMISSION_RELATION")
public class RolePermissionRelation implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String Id;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "PERMISSION_ID")
    private String permissionId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
