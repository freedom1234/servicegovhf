package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INTERFACE_TAG")
public class InterfaceTag implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INTERFACE_ID")
	private String interfaceId;
	
	@Id
	@Column(name = "TAG_ID")
	private String tagId;

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	
}
