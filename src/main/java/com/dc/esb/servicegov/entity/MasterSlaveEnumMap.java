package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MASTER_SLAVE_ENUM_MAP")
public class MasterSlaveEnumMap implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "MASTER_ID",length=50)
	private String masterId;
	
	@Id
	@Column(name = "SLAVE_ID",length=50)
	private String slaveId;

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}
}
