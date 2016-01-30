package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONTEXTMAPPINGINFO")
public class ContextMappingInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MAPPINGID")
	private String mappingId;
	
	@Column(name = "CONTEXTID")
	private String contextId;
	
	@Column(name = "KESSIONID")
	private String kessionId;
	
	@Column(name = "OWERID")
	private String owerId;
	
	@Column(name = "OPTLOCK")
	private String optLock;

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getKessionId() {
		return kessionId;
	}

	public void setKessionId(String kessionId) {
		this.kessionId = kessionId;
	}

	public String getOwerId() {
		return owerId;
	}

	public void setOwerId(String owerId) {
		this.owerId = owerId;
	}

	public String getOptLock() {
		return optLock;
	}

	public void setOptLock(String optLock) {
		this.optLock = optLock;
	}
	
}
