package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SLA_TEMPLATE")
public class SLATemplate implements Serializable {

	private static final long serialVersionUID = 1L;
   
	@Id
	@Column(name = "SLA_TEMPLATE_ID")
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String slaTemplateId;
	
	@Column(name="DESCRIPTION")
	private String desc;
	
	@Column(name="TEMPLATE_NAME")
	private String templateName;


	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

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
