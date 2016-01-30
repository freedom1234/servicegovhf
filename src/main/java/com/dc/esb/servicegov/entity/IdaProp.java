package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="IDA_PROP")
public class  IdaProp{
	@Id
    @Column(name = "ID")
	private String id;
	
	@Column(name = "IDA_ID")
	private String idaId;
	
	@Column(name = "PROPERTY_ID")
	private String propertyId;
	
	@Column(name = "PROPERTY_VALUE")
	private String propertyValue;
	
	@Column(name = "REMARK")
	private String remark;
	
	 @Column(name = "OPT_USER")
	 private String potUser;
	
	 @Column(name = "OPT_DATE")
	 private String potDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdaId() {
		return idaId;
	}

	public void setIdaId(String idaId) {
		this.idaId = idaId;
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

	public String getPotUser() {
		return potUser;
	}

	public void setPotUser(String potUser) {
		this.potUser = potUser;
	}

	public String getPotDate() {
		return potDate;
	}

	public void setPotDate(String potDate) {
		this.potDate = potDate;
	}

	 
}
