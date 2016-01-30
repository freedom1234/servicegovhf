package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENUM_ELEMENTS")
public class EnumElements implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ELEMENT_ID",length=50)
	@GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
	private String elementId;
	
	@Column(name = "ENUM_ID",length=50)
	private String enumId;
	
	@Column(name = "ELEMENT_NAME",length=50)
	private String elementName;
	
	@Column(name = "REMARK",length=1023)
	private String remark;
	
	@Column(name = "BUSS_DEFINE",length=255)
	private String bussDefine;
	
	@Column(name = "OPT_USER",length=50)
	private String optUser;
	
	@Column(name = "OPT_DATE",length=50)
	private String optDate;

	@Column(name = "PROCESS_ID")
	private String processId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBussDefine() {
		return bussDefine;
	}

	public void setBussDefine(String bussDefine) {
		this.bussDefine = bussDefine;
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

	public String getEnumId() {
		return enumId;
	}

	public void setEnumId(String enumId) {
		this.enumId = enumId;
	}
}
