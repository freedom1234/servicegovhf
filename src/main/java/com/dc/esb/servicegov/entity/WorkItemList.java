package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WORK_ITEM_LIST")
public class WorkItemList {
	@Id
	@Column(name = "LIST_ID")
	private String listId;
	
	@Column(name = "VERSION")
	private String version;
	
	@Column(name = "DESCRIPTION")
	private String desc;

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
