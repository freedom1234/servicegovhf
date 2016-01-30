package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OPERATION_TAG")
public class OperationTag implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TAG_ID")
	private String tagId;

	@Id
	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Id
	@Column(name = "OPERATION_ID")
	private String operationId;


	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}


	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
