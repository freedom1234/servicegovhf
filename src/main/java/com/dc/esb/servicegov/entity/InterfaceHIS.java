package com.dc.esb.servicegov.entity;

import com.dc.esb.servicegov.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="INTERFACE_HIS")
public class InterfaceHIS {
	@Id
    @Column(name = "AUTO_ID")
    private String autoId;

	@Column(name = "INTERFACE_ID")
	private String interfaceId;


	@Column(name = "INTERFACE_NAME")
	private String interfaceName;

	@Column(name = "ECODE")
	private String ecode;

	@Column(name = "DESCRIPTION",length = 1000)
	private String desc;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "STATUS")
	private String status;

	@Column(name="VERSION_ID")
	private String versionId;

	@Column(name="VERSION_HIS_ID")
	private String versionHisId;

	@Column(name = "OPT_USER")
	private String optUser;

	@Column(name = "OPT_DATE")
	private String optDate;

	private String headName;

	private String protocolName;

	@OneToOne(cascade={CascadeType.REFRESH}, optional=true)
	@JoinColumn(name="VERSION_HIS_ID", insertable = false, updatable = false)
	private VersionHis versionHis;


	public InterfaceHIS(){
	}

	public InterfaceHIS(Interface inter) {
		this.autoId = UUID.randomUUID().toString();
		this.interfaceId = inter.getInterfaceId();
		this.interfaceName = inter.getInterfaceName();
		this.ecode = inter.getEcode();
		this.desc = inter.getDesc();
		this.remark = inter.getRemark();
		this.status = inter.getStatus();
		this.versionId = inter.getVersionId();
		this.optUser = SecurityUtils.getSubject().getPrincipal().toString();
		this.optDate = DateUtils.format(new Date());
		this.headName = inter.getHeadName();
		this.protocolName = inter.getHeadName();
	}

	public static String[] simpleFields(){
		String[] fields = {"interfaceId", "interfaceName"};
		return fields;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getEcode() {
		return ecode;
	}

	public void setEcode(String ecode) {
		this.ecode = ecode;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getVersionHisId() {
		return versionHisId;
	}

	public void setVersionHisId(String versionHisId) {
		this.versionHisId = versionHisId;
	}

	public VersionHis getVersionHis() {
		return versionHis;
	}

	public void setVersionHis(VersionHis versionHis) {
		this.versionHis = versionHis;
	}

}
