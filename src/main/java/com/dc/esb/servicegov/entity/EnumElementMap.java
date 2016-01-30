package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ENUM_ELEMENT_MAP")
public class EnumElementMap implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MASTER_ELEMENT_ID",length=50)
	private String masterElementId;
	
	@Id
	@Column(name = "SLAVE_ELEMENT_ID",length=50)
	private String slaveElementId;
	
	@Column(name = "MAPPING_RELATION",length=50)
	private String mappingRelation;
	
	@Column(name = "DIRECTION",length=50)
	private String direction;

	public String getMasterElementId() {
		return masterElementId;
	}

	public String getMappingRelation() {
		return mappingRelation;
	}

	public void setMappingRelation(String mappingRelation) {
		this.mappingRelation = mappingRelation;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setMasterElementId(String masterElementId) {
		this.masterElementId = masterElementId;
	}

	public String getSlaveElementId() {
		return slaveElementId;
	}

	public void setSlaveElementId(String slaveElementId) {
		this.slaveElementId = slaveElementId;
	}
	
}
