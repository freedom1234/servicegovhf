package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="base_line")
public class BaseLine implements Serializable{
	private static final long serialVersionUID = -6048321328134031286L;

	@Id
	@Column(name="BASE_ID")
	private String baseId;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="BL_DESC")
	private String blDesc;
	
	@Column(name="OPT_USER")
	private String optUser;
	
	@Column(name="OPT_DATE")
	private String optDate;
	
	@Column(name="VERSION_ID")
	private String versionId;


	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getBlDesc() {
		return blDesc;
	}

	public void setBlDesc(String blDesc) {
		this.blDesc = blDesc;
	}

	public String getOptUser() {
		return optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}

	public String getOptDate() {
		return optDate;
	}

	public void setOptDate(String optDate) {
		this.optDate = optDate;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	
}
