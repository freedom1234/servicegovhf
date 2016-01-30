package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CORRELATIONPROPERTYINFO")
public class CorrelationPropertyInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROPERTYID")
	private String propertyId;
	
	@Column(name = "NAME")
	private String name ;
	 
	@Column(name = "VALUE")
	private String value ;
	
	@Column(name = "OPTLOCK")
	private String optLock;
	
	@Column(name = "CORRELATIONKEYID")
	private String correlationKeyId;

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOptLock() {
		return optLock;
	}

	public void setOptLock(String optLock) {
		this.optLock = optLock;
	}

	public String getCorrelationKeyId() {
		return correlationKeyId;
	}

	public void setCorrelationKeyId(String correlationKeyId) {
		this.correlationKeyId = correlationKeyId;
	}
	
	
}
