package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SAL_HIS")
public class SLAHis implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "AUTO_ID")
	private String autoId;
	
	
	@Column(name = "OPERATION_HIS_ID")
	private String operationHisId;

	@Column(name = "SLA_ID")
	private String slaId;
	
	@Column(name = "SLA_NAME")
	private String slaName;
	
	@Column(name = "SLA_VALUE")
	private String slaValue;
	
	@Column(name = "SLA_DESC")
	private String slaDesc;
	
	@Column(name = "SLA_REMARK")
	private String slaRemark;
	
	@Column(name = "OPERATION_ID")
	private String operationId;
	
	@Column(name = "SERVICE_ID")
	private String serviceId;
	
	@Column(name = "SLA_TEMPLATE_ID")
	private String slaTemplateId;

	@Column(name = "VERSION")
	private String version;
	
	public SLAHis(){
		
	}
	
	public SLAHis(SLA sla, String operationHisId) {
		this.autoId = UUID.randomUUID().toString();
		this.operationHisId = operationHisId;
		this.slaId = sla.getSlaId();
		this.slaName = sla.getSlaName();
		this.slaValue = sla.getSlaValue();
		this.slaDesc = sla.getSlaDesc();
		this.slaRemark = sla.getSlaRemark();
		this.operationId = sla.getOperationId();
		this.serviceId = sla.getServiceId();
		this.slaTemplateId = sla.getSlaTemplateId();
		this.version = sla.getVersion();
	}


	public String getOperationHisId() {
		return operationHisId;
	}

	public void setOperationHisId(String operationHisId) {
		this.operationHisId = operationHisId;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getSlaId() {
		return slaId;
	}

	public void setSlaId(String slaId) {
		this.slaId = slaId;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public String getSlaValue() {
		return slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public String getSlaDesc() {
		return slaDesc;
	}

	public void setSlaDesc(String slaDesc) {
		this.slaDesc = slaDesc;
	}

	public String getSlaRemark() {
		return slaRemark;
	}

	public void setSlaRemark(String slaRemark) {
		this.slaRemark = slaRemark;
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

	public String getSlaTemplateId() {
		return slaTemplateId;
	}

	public void setSlaTemplateId(String slaTemplateId) {
		this.slaTemplateId = slaTemplateId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
