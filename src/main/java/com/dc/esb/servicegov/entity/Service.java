package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SERVICE")
public class Service implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Column(name = "SERVICE_NAME")
	private String serviceName;
	
	@Column(name = "CATEGORY_ID")
	private String categoryId;

	@Column(name = "DESCRIPTION",length=2048)
	private String desc;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "VERSION")
	private String version;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;

	@Column(name = "PROCESS_ID")
	private String processId;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}
