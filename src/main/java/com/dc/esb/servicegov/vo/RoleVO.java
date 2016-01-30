package com.dc.esb.servicegov.vo;


import com.dc.esb.servicegov.entity.Role;

/**
 * Created by vincentfxz on 15/7/27.
 */
public class RoleVO {

    private String id;
    private String name;
    private String remark;

    public RoleVO(String id, String name,String remark){
        this.id = id;
        this.name = name;
        this.remark = remark;
    }

    public RoleVO(Role role){
        this.id = role.getId();
        this.name = role.getName();
        this.remark = role.getRemark();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
