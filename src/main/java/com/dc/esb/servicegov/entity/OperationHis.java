package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.dc.esb.servicegov.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OPERATION_HIS")
public class OperationHis implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUTO_ID")
	private String autoId;
	
	@Column(name = "OPERATION_ID")
	private String operationId;
	
	@Column(name = "SERVICE_ID")
	private String serviceId;
	
	@Column(name = "OPERATION_NAME")
	private String operationName;
	
	@Column(name = "OPERATION_DESC", length = 2048)
	private String operationDesc;
	
	@Column(name = "OPERATION_REMARK")
	private String operationRemark;
	
	@Column(name = "VERSION_HIS_ID")
	private String versionHisId;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;
	
	@Column(name = "HEAD_ID")
	private String headId;
	
	@ManyToOne()
    @JoinColumn(name="SERVICE_ID", insertable = false, updatable = false)
	private Service service;

	@OneToOne(cascade={CascadeType.REFRESH}, optional=true)
    @JoinColumn(name="VERSION_HIS_ID", insertable = false, updatable = false)
	private VersionHis versionHis;


	public OperationHis(){
		
	}
	
	public OperationHis(Operation operation){
		this.autoId = UUID.randomUUID().toString();
		this.headId = operation.getHeadId();
		this.operationDesc = operation.getOperationDesc();
		this.operationId = operation.getOperationId();
		this.operationName = operation.getOperationName();
		this.operationRemark = operation.getOperationRemark();
		this.optDate = DateUtils.format(new Date());
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		this.optUser = userName;
		this.serviceId = operation.getServiceId();
		this.state = operation.getState();
	}
	


	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}

	public String getOperationRemark() {
		return operationRemark;
	}

	public void setOperationRemark(String operationRemark) {
		this.operationRemark = operationRemark;
	}


	public String getVersionHisId() {
		return versionHisId;
	}

	public void setVersionHisId(String versionHisId) {
		this.versionHisId = versionHisId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public VersionHis getVersionHis() {
		return versionHis;
	}

	public void setVersionHis(VersionHis versionHis) {
		this.versionHis = versionHis;
	}


	
}
