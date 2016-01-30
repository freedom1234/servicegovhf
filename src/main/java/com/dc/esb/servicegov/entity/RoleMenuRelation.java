package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Entity
@Table(name = "ROLE_MENU_RELATION")
public class RoleMenuRelation implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String Id;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "SG_MENU_ID")
    private String sgMenuId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSgMenuId() {
        return sgMenuId;
    }

    public void setSgMenuId(String sgMenuId) {
        this.sgMenuId = sgMenuId;
    }
}
