package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="MSG_TEMPLATE")
public class MsgTemplate {
	@Id
    @Column(name = "TEMPLATE_ID")
	private String templateId;
	
	 @Column(name = "TEMPLATE_NAME")
	 private String templateName;
	 
	 @Column(name = "TEMPLATE_CONTENT")
	 private String templateContent;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	 
	 
}
