package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE_RELATION")
public class UserRoleRelation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 11111L;
	@Id
    @Column(name = "ROLE_ID")
    private String roleId;

	@Id
	@Column(name = "USER_ID")
	private String userId;

	@ManyToOne()
	@JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
	private Role role;
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
