package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WORK_ITEM_HANDLER")
public class WorkItemHandler {
	@Id
    @Column(name = "HANDLER_ID")
	private String handlerId;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "WORK_ITEM__ID")
	private String workItemId;

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	
	
}
