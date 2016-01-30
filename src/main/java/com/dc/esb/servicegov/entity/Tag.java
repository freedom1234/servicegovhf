package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="TAG")
public class Tag implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TAG_ID")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	private String tagId;
	
	@Column(name = "TAG_NAME")
	private String tagName;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "PARENT_TAG_ID")
	private String parentTagId;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(String parentTagId) {
		this.parentTagId = parentTagId;
	}
	
	
}


