package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_ERROR_CODE")
public class SystemErrorCode implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id  
	@Column(name = "SYSTEM_ID")
	private String systemId;
	
	@ManyToOne(targetEntity=System.class)
	@JoinColumn(name = "SYSTEM_ID", referencedColumnName = "SYSTEM_ID", insertable = false, updatable = false)
	private System system;
	
	@Column(name = "CODE_ID")
	private String codeId;

	@ManyToOne(targetEntity=ErrorCode.class)
	@JoinColumn(name = "CODE_ID", referencedColumnName = "CODE_ID", insertable = false, updatable = false)
	private ErrorCode errorCode;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
