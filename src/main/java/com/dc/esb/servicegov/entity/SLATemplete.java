package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SLA_TEMPLATE")
public class SLATemplete implements Serializable {

	private static final long serialVersionUID = 1L;
   
	@Id
	@Column(name = "SLA_TEMPLATE_ID")
	private String slaTemplateId;
	
	@Column(name="DESCRIPTION")
	private String desc;

	public String getSlaTemplateId() {
		return slaTemplateId;
	}

	public void setSlaTemplateId(String slaTemplateId) {
		this.slaTemplateId = slaTemplateId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
