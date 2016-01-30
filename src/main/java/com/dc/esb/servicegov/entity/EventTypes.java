package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EVENTTYPES")
public class EventTypes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INSTANCEID")
	private String instanceId;

	@Column(name = "ELEMENT")
	private String element;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

}
