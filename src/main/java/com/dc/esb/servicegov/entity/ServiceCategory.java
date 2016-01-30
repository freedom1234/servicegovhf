package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SERVICE_CATEGORY")
public class ServiceCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CATEGORY_ID")
//	@GeneratedValue(generator="system-uuid")
//    @GenericGenerator(name="system-uuid",strategy="uuid")
	private String categoryId;
	
	@Column(name = "CATEGORY_NAME")
	private String categoryName;
	
	@Column(name = "PARENT_ID")
	private String parentId;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "REMARK")
	private String remark;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
