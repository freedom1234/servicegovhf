package com.dc.esb.servicegov.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="INTERFACE_HEAD")
public class InterfaceHead {
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

//	@OneToMany(targetEntity=Ida.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//	 @JoinColumns({
//            @JoinColumn(name = "HEAD_ID", referencedColumnName = "HEAD_ID", insertable = false, updatable = false)
//    })
//	
//	private List<Ida> idas;
//

	@OneToMany(mappedBy = "interfaceHead",cascade = CascadeType.ALL)
	private List<InterfaceHeadRelate> headRelates ;

	@OneToMany(mappedBy = "heads",cascade = CascadeType.ALL)
	private List<Ida> idas;


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

//	public List<Ida> getIdas() {
//		return idas;
//	}
//
//	public void setIdas(List<Ida> idas) {
//		this.idas = idas;
//	}


	public List<InterfaceHeadRelate> getHeadRelates() {
		return headRelates;
	}

	public void setHeadRelates(List<InterfaceHeadRelate> headRelates) {
		this.headRelates = headRelates;
	}

	public List<Ida> getIdas() {
		return idas;
	}

	public void setIdas(List<Ida> idas) {
		this.idas = idas;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
