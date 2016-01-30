package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 错误码
 */
@Entity
@Table(name="ERROR_CODE")
public class ErrorCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id  
	@Column(name="CODE_ID")
	private String codeId;
	
	@Column(name="CODE_NAME")
	private String codeName;
	
	@Column(name="CODE_DESC")
	private String codeDesc;

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	
}
