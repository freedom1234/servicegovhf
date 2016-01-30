package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SG_ROLE")
public class Role implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 33333333333333L;
	@Id
    @Column(name = "ROLE_ID")
    private String id;
    @Column(name = "ROLE_NAME")
    private String name;
    @Column(name = "ROLE_REMARK")
    private String remark;
    
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
