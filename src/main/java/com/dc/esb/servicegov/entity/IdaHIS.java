package com.dc.esb.servicegov.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="IDA_HIS")
public class IdaHIS {
	@Id
	@Column(name = "AUTO_ID")
	private String autoId;

	@Column(name = "ID")
	private String id;
	@Column(name = "STRUCTNAME")
	private String structName;
	@Column(name = "STRUCTALIAS",length = 500)
	private String structAlias;

	@Column(name = "METADATA_ID")
	private String metadataId;

	@Column(name = "SEQ")
	private int seq;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "SCALE")
	private String scale;

	@Column(name = "LENGTH")
	private String length;

	@Column(name = "REQUIRED")
	private String required;

	@Column(name = "PARENT_ID",updatable=false,insertable=true)
	private String _parentId;

	@Column(name = "INTERFACE_ID")
	private String interfaceId;

	@Column(name = "INTERFACE_HIS_ID")
	private String interfaceHisId;

	@Column(name = "OPT_USER")
	private String potUser;

	@Column(name = "OPT_DATE")
	private String potDate;

	@Column(name = "HEAD_ID")
	private String headId;

	@Column(name = "VERSION")
	private String version;

	@Column(name = "REMARK",length = 3072)
	private String remark;

	@Column(name = "sdaId")
	private String sdaId;

	@Column(name = "state")
	private String state;//状态，0：普通（可以在页面看到），1：空白（文件导出使用）

	public IdaHIS(){

	}
	public IdaHIS(Ida ida, String interfaceHisId) {
		this.autoId = UUID.randomUUID().toString();
		this.id = ida.getId();
		this.structName = ida.getStructName();
		this.structAlias = ida.getStructAlias();
		this.metadataId = ida.getMetadataId();
		this.seq = ida.getSeq();
		this.type = ida.getType();
		this.scale = ida.getScale();
		this.length = ida.getLength();
		this.required = ida.getRequired();
		this._parentId = ida.getParentId();
		this.interfaceId = ida.getInterfaceId();
		this.potUser = ida.getPotUser();
		this.potDate = ida.getPotDate();
		this.headId = ida.getHeadId();
		this.version = ida.getVersion();
		this.remark = ida.getRemark();
//		this.interObj = ida.getInterObj();
		this.heads = ida.getHeads();
		this.sdaId = ida.getSdaId();
		this.state = ida.getState();
		this.interfaceHisId = interfaceHisId;
	}

	//    @Column(name = "ARG_TYPE")
	//参数类型 输出还是输入参数，导入时判断，有可能输入和输出参数名相同
//    private String argType;

//	@ManyToOne
//	@JoinColumn(name="INTERFACE_ID",referencedColumnName = "INTERFACE_ID",insertable = false,updatable = false)
//	private Interface interObj;

	@ManyToOne
	@JoinColumn(name = "HEAD_ID",referencedColumnName = "HEAD_ID",insertable = false,updatable = false)
	private InterfaceHead heads;

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

	public String getStructAlias() {
		return structAlias;
	}

	public void setStructAlias(String structAlias) {
		this.structAlias = structAlias;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
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

	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String id) {
		_parentId = id;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getPotUser() {
		return potUser;
	}

	public void setPotUser(String potUser) {
		this.potUser = potUser;
	}

	public String getPotDate() {
		return potDate;
	}

	public void setPotDate(String potDate) {
		this.potDate = potDate;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public InterfaceHead getHeads() {
		return heads;
	}

	public void setHeads(InterfaceHead heads) {
		this.heads = heads;
	}

	public String getInterfaceHisId() {
		return interfaceHisId;
	}

	public void setInterfaceHisId(String interfaceHisId) {
		this.interfaceHisId = interfaceHisId;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getSdaId() {
		return sdaId;
	}

	public void setSdaId(String sdaId) {
		this.sdaId = sdaId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
