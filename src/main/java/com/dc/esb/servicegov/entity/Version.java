package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "version")
public class Version implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="BASE_ID")
	private String id;

	@Column(name="CODE")
	private String code;
	
	//0：操作版本，1：基线版本
	@Column(name="TYPE")
	private String type;
	//0:生效，1：废弃
	@Column(name="STATE")
	private String state;
	
	@Column(name="V_DESC")
	private String versionDesc;
	
	@Column(name="REMARK")
	private String remark;
	//操作类型，0：新增，1：修改，2：删除,3:发布
	@Column(name="OPT_Type")
	private String optType;
	
	@Column(name="OPT_USER")
	private String optUser;
	
	@Column(name="OPT_DATE")
	private String optDate;
	
	//目标类型, 0:基线, 1:场景， 2：公共代码，3：接口, 4:元数据
	@Column(name="TARGET_TYPE")
	private String targetType;
	
	@Column(name="TARGET_ID")
	private String targetId;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOptUser() {
		return optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}

	public String getOptDate() {
		return optDate;
	}

	public void setOptDate(String optDate) {
		this.optDate = optDate;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	
}
