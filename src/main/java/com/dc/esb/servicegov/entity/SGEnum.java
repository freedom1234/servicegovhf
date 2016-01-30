package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENUM")
public class SGEnum implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID",length=50)
	@GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
	private String id;
	
	@Column(name = "NAME")
	private String name; 
	
	@Column(name = "IS_STANDARD",length=10)
	private String isStandard;
	
	@Column(name = "IS_MASTER",length=10)
	private String isMaster; 
	
	@Column(name = "DATA_SOURCE")
	private String dataSource; 
	
	@Column(name = "STATUS",length=10)
	private String status; 
	
//	@Column(name = "VERSION",length=10,columnDefinition="default 1.0.0")
	@Column(name = "VERSION",length=10)
	private String version; 
	
	@Column(name = "REMARK",length=1023)
	private String remark; 
	
	@Column(name = "OPT_USER",length=50)
	private String optUser; 
	
	@Column(name = "OPT_DATE")
	private String optDate;

	@Column(name = "PROCESS_ID")
	private String processId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public String getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(String isMaster) {
		this.isMaster = isMaster;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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
