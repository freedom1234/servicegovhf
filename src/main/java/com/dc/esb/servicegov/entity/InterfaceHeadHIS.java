package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="INTERFACE_HEAD_HIS")
public class InterfaceHeadHIS {
	@Id
	@Column(name = "HEAD_ID")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	private String headId;

	@Column(name = "HEAD_NAME")
	private String headName;

	@Column(name = "HEAD_DESC")
	private String headDesc;

	@Column(name = "HEAD_REMARK")
	private String headRemark;

	@Column(name= "SYSTEM_ID")
	private String systemId;


	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getHeadDesc() {
		return headDesc;
	}

	public void setHeadDesc(String headDesc) {
		this.headDesc = headDesc;
	}

	public String getHeadRemark() {
		return headRemark;
	}

	public void setHeadRemark(String headRemark) {
		this.headRemark = headRemark;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	
}
