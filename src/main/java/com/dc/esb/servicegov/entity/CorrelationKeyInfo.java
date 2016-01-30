package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CORRLATIONKEYINFO")
public class CorrelationKeyInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "KEYID")
	private String keyId;
	
	@Column(name = "NAME")
	private String name ;
	
	@Column(name = "PROCESSINSTANCEID")
	private String processInstanceId;
	
	@Column(name = "OPTLOCK")
	private String optLock;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getOptLock() {
		return optLock;
	}

	public void setOptLock(String optLock) {
		this.optLock = optLock;
	}
}
