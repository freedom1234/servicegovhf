package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_CATEGORY_HIS")
public class ServiceCategoryHis implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CATEGORY_ID")
	private String categoryId;
	
	@Column(name = "CATEGORY_NAME")
	private String categoryName;
	
	@Column(name = "PARENT_ID")
	private String parentId;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "VERSION")
	private String version;

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
