package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SDA_PROP")
public class SDAProp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "SDA_ID")
	private String sdaId;
	
	@Column(name = "PROPERTY_ID")
	private String propertyId;
	
	@Column(name = "PROPERTY_VALUE")
	private String propertyValue;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSdaId() {
		return sdaId;
	}

	public void setSdaId(String sdaId) {
		this.sdaId = sdaId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
