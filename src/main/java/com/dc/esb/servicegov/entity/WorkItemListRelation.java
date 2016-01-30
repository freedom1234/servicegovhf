package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WORK_ITEM_LIST_RELATION")
public class WorkItemListRelation {
	@Id
    @Column(name = "RELATION_ID")
	private String relationId;
	
	@Column(name = "LIST_ID")
	private String listId;
	
	@Column(name = "WORK_ITEM__ID")
	private String workItemId;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	
	
}
