package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SDA_HIS")
public class SDAHis implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUTO_ID")
	private String autoId;
	
	@Column(name = "OPERATION_HIS_ID")
	private String operationHisId;
	
	@Column(name = "SDA_ID")
	private String sdaId;
	
	@Column(name = "STRUCTNAME")
	private String structName;
	
	@Column(name = "STRUCTALIAS")
	private String structAlias;
	
	@Column(name = "METADATAID")
	private String metadataId;
	
	@Column(name = "SEQ")
	private int seq;
	
	@Column(name = "PARENT_ID")
	private String parentId;
	
	@Column(name = "SERVICE_ID")
	private String serviceId;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;
	
	@Column(name = "OPERATION_ID")
	private String operationId;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "REMARK", length = 3072)
	private String remark;
	
	@Column(name = "HEAD_ID")
	private String headId;

	@Column(name = "VERSION")
	private String version;
	
	@Column(name = "type")
	private String type;

	@Column(name = "LENGTH", length = 50)
	private String length;

	@Column(name = "REQUIRED", length = 50)
	private String required;

	public SDAHis(){
		
	}
	
	public SDAHis(SDA sda, String operationHisId) {
		this.autoId = UUID.randomUUID().toString();
		this.operationHisId = operationHisId;
		this.sdaId = sda.getId();
		this.structName = sda.getStructName();
		this.structAlias = sda.getStructAlias();
		this.metadataId = sda.getMetadataId();
		this.seq = sda.getSeq();
		this.parentId = sda.getParentId();
		this.serviceId = sda.getServiceId();
		this.optUser = sda.getOptUser();
		this.optDate = sda.getOptDate();
		this.operationId = sda.getOperationId();
		this.desc = sda.getDesc();
		this.remark = sda.getRemark();
		this.headId = sda.getHeadId();
		this.version = sda.getVersion();
		this.type = sda.getType();
		this.length = sda.getLength();
		this.required = sda.getRequired();
	}



	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getOperationHisId() {
		return operationHisId;
	}

	public void setOperationHisId(String operationHisId) {
		this.operationHisId = operationHisId;
	}

	public String getSdaId() {
		return sdaId;
	}

	public void setSdaId(String sdaId) {
		this.sdaId = sdaId;
	}

	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStructAlias() {
		return structAlias;
	}

	public void setStructAlias(String structAlias) {
		this.structAlias = structAlias;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}
}
