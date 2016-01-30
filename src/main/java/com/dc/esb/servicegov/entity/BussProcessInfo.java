package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BUSS_PROCESS_INFO")
public class BussProcessInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "PROCESS_BYTE_ARRAY")
	private String processByteArray;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessByteArray() {
		return processByteArray;
	}

	public void setProcessByteArray(String processByteArray) {
		this.processByteArray = processByteArray;
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
