package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "service_head_sda")
public class ServiceHeadSda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SDA_ID")
	private String sdaId;

	@Column(name = "STRUCTNAME")
	private String structName;

	@Column(name = "STRUCTALIAS")
	private String structAlias;

	@Column(name = "METADATAID")
	private String metadataId;

	@Column(name = "SEQ")
	private int seq = 0;

	@Column(name = "PARENT_ID")
	private String _parentId;

	@Column(name = "DESCRIPTION")
	private String desc;

	@Column(name = "REMARK",length=2048)
	private String remark;

	@Column(name = "HEAD_ID")
	private String headId;

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

	public String getSdaId() {
		return sdaId;
	}

	public void setSdaId(String sdaId) {
		this.sdaId = sdaId;
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

	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
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

//	public SDA getParent() {
//		return parent;
//	}
//
//	public void setParent(SDA parent) {
//		this.parent = parent;
//	}

}
