package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_TAG")
public class ServiceTag implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Id
	@Column(name = "TAG_ID")
	private String tagId;


	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
