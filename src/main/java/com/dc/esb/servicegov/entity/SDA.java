package com.dc.esb.servicegov.entity;

import com.dc.esb.servicegov.export.IExportableNode;

import java.io.Serializable;
import java.lang.*;

import javax.persistence.*;


@Entity
@Table(name = "SDA")
public class SDA extends IExportableNode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SDA_ID")
	private String id;

	@Column(name = "STRUCTNAME")
	private String structName;

	@Column(name = "STRUCTALIAS")
	private String structAlias;

	@Column(name = "METADATAID")
	private String metadataId;

	@Column(name = "SEQ")
	private int seq = 0;

	@Column(name = "PARENT_ID")
	private String parentId;

	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Column(name = "OPT_USER", length = 100)
	private String optUser;

	@Column(name = "OPT_DATE", length = 50)
	private String optDate;

	@Column(name = "OPERATION_ID")
	private String operationId;

	@Column(name = "DESCRIPTION")
	private String desc;

	@Column(name = "REMARK",length=3072)
	private String remark;

	@Column(name = "HEAD_ID")
	private String headId;//用于关联接口报文头

	@Column(name = "SERVICE_HEAD_ID")
	private String serviceHeadId;//用于关联服务报文头

	@Column(name = "VERSION")
	private String version;

	@Column(name = "TYPE", length = 50)
	private String type;

	@Column(name = "LENGTH", length = 50)
	private String length;

	@Column(name = "REQUIRED", length = 50)
	private String required;

	@Deprecated
	@Column(name = "ARG_TYPE", length = 50)
	//参数类型 输出还是输入参数，导入时判断，有可能输入和输出参数名相同
	private String argType;

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	@Column(name = "CONSTRAINT_ALIAS")

	private String constraint;//约束条件 如：SYS_HEAD  APP_HEAD

	@Column(name = "xpath")
	private String xpath;

//	@ManyToOne()
//	@JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
//	private SDA parent;

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getStructAlias() {
		return structAlias;
	}

	public void setStructAlias(String structAlias) {
		this.structAlias = structAlias;
	}

	public String getRemark() {
		if(remark != null && remark.length() > 2048){
			remark.replaceAll(" ", "");
			remark.replaceAll("\t", "");
			if(remark.length() > 2048){
				remark = remark.substring(0, 2048);
			}
		}
		return remark;
	}

	public void setRemark(String remark) {
		if(remark != null && remark.length() > 2048){
			remark.replaceAll(" ", "");
			remark.replaceAll("\t", "");
			if(remark.length() > 2048){
				remark = remark.substring(0, 2048);
			}
		}
		this.remark = remark;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getArgType() {
		return argType;
	}

	public void setArgType(String argType) {
		this.argType = argType;
	}

	public String getServiceHeadId() {
		return serviceHeadId;
	}

	public void setServiceHeadId(String serviceHeadId) {
		this.serviceHeadId = serviceHeadId;
	}
	//	public SDA getParent() {
//		return parent;
//	}
//
//	public void setParent(SDA parent) {
//		this.parent = parent;
//	}

}
