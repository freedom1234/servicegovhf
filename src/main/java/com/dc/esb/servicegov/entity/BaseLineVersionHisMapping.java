package com.dc.esb.servicegov.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="baseline_version_mapping")
public class BaseLineVersionHisMapping implements Serializable{
	private static final long serialVersionUID = 4585709087576606095L;
	@Id
	@Column(name="baseline_id")
	private String baseLineId;
	@Id
	@Column(name="versionhis_id")
	private String versionHisId;
	@ManyToOne()           
    @JoinColumn(name="baseline_id", insertable = false, updatable = false)
	private BaseLine baseLine;
	
	@ManyToOne()           
    @JoinColumn(name="versionhis_id", insertable = false, updatable = false)
	private VersionHis versionHis;
	
	
	public String getBaseLineId() {
		return baseLineId;
	}
	public void setBaseLineId(String baseLineId) {
		this.baseLineId = baseLineId;
	}
	public String getVersionHisId() {
		return versionHisId;
	}
	public void setVersionHisId(String versionHisId) {
		this.versionHisId = versionHisId;
	}
	public BaseLine getBaseLine() {
		return baseLine;
	}
	public void setBaseLine(BaseLine baseLine) {
		this.baseLine = baseLine;
	}
	public VersionHis getVersionHis() {
		return versionHis;
	}
	public void setVersionHis(VersionHis versionHis) {
		this.versionHis = versionHis;
	}
	
	
}
