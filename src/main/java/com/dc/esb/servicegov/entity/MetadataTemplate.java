package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="METADATA_TEMPLATE")
public class MetadataTemplate {
	@Id
    @Column(name = "TEMPLATE_ID")
	private String templateId;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "LENGTH")
	private String length;
	
	@Column(name = "SCALE")
	private String scale;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	
	
}
