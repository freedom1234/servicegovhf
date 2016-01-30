package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WORK_ITEM")
public class WorkItem {
	@Id
    @Column(name = "WORK_ITEM_ID")
	private String WorkItemId;
	
	@Column(name = "RELATION_ID")
	private String relationId;
	
	@Column(name = "INTERFACE_VERSION")
	private String interfaceVersion;
	
	@Column(name = "OPERATION_VERSION")
	private String operationVersion;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "VERSION")
	private String version;

	public String getWorkItemId() {
		return WorkItemId;
	}

	public void setWorkItemId(String workItemId) {
		WorkItemId = workItemId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getOperationVersion() {
		return operationVersion;
	}

	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
