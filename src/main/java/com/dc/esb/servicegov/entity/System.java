package com.dc.esb.servicegov.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM")
public class System implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SYSTEM_ID")
	private String systemId;
	
	@Column(name = "SYSTEM_AB")
	private String systemAb;
	
	@Column(name = "SYSTEM_CHINESE_NAME")
	private String systemChineseName;
	
	@Column(name = "FEATURE_DESC")
	private String featureDesc;
	
	@Column(name = "WORK_RANGE")
	private String workRange;
	
	@Column(name = "PRINCIPAL1")
	private String principal1;
	
	@Column(name = "PRINCIPAL2")
	private String principal2;
	
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;

	@Column(name = "SYSTEM_DESC")
	private String systemDesc;

	@Column(name = "PRINCIPAL_DETAIL1")
	private String principalDetail1;

	@Column(name = "PRINCIPAL_DETAIL2")
	private String principalDetail2;

	private String protocolName;

	public static String[] simpleFields(){
		String[] names = {"systemId", "systemChineseName"};
		return names;
	}

	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,mappedBy = "system")
	//@JoinColumn(name = "SYSTEM_ID",referencedColumnName = "SYSTEM_ID",insertable=true,updatable = true)
	private List<SystemProtocol> systemProtocols;
	
	@OneToMany(mappedBy="system",cascade = CascadeType.ALL)
	private List<ServiceInvoke> serviceInvokes;
	
	
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemAb() {
		return systemAb;
	}

	public void setSystemAb(String systemAb) {
		this.systemAb = systemAb;
	}

	public String getSystemChineseName() {
		return systemChineseName;
	}

	public void setSystemChineseName(String systemChineseName) {
		this.systemChineseName = systemChineseName;
	}

	public String getFeatureDesc() {
		return featureDesc;
	}

	public void setFeatureDesc(String featureDesc) {
		this.featureDesc = featureDesc;
	}

	public String getWorkRange() {
		return workRange;
	}

	public void setWorkRange(String workRange) {
		this.workRange = workRange;
	}

	public String getPrincipal1() {
		return principal1;
	}

	public void setPrincipal1(String principal1) {
		this.principal1 = principal1;
	}

	public String getPrincipal2() {
		return principal2;
	}

	public void setPrincipal2(String principal2) {
		this.principal2 = principal2;
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

	public List<ServiceInvoke> getServiceInvokes() {
		return serviceInvokes;
	}

	public void setServiceInvokes(List<ServiceInvoke> serviceInvoke) {
		this.serviceInvokes = serviceInvoke;
	}

	public List<SystemProtocol> getSystemProtocols() {
		return systemProtocols;
	}

	public void setSystemProtocols(List<SystemProtocol> systemProtocols) {
		this.systemProtocols = systemProtocols;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getSystemDesc() {
		return systemDesc;
	}

	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}

	public String getPrincipalDetail1() {
		return principalDetail1;
	}

	public void setPrincipalDetail1(String principalDetail1) {
		this.principalDetail1 = principalDetail1;
	}

	public String getPrincipalDetail2() {
		return principalDetail2;
	}

	public void setPrincipalDetail2(String principalDetail2) {
		this.principalDetail2 = principalDetail2;
	}
}
