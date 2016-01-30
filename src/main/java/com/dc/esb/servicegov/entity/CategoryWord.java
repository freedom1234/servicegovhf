package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="CATEGORY_WORD")
public class CategoryWord implements Serializable{
	private static final long serialVersionUID = -6048321328134021256L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	private String id;
	
    @Column(name = "ENGLISH_WORD")
	private String englishWord;
	
	
    @Column(name = "CHINESE_WORD")
	private String chineseWord;

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

	@Column(name = "ESGLISG_AB")
	private String esglisgAb;
	
	@Column(name = "REMARK")
	private String remark;
	 
	@Column(name = "OPT_USER")
	private String optUser;
	
	@Column(name = "OPT_DATE")
	private String optDate;

	public String getEnglishWord() {
		return englishWord;
	}

	public void setEnglishWord(String englishWord) {
		this.englishWord = englishWord;
	}

	public String getChineseWord() {
		return chineseWord;
	}

	public void setChineseWord(String chineseWord) {
		this.chineseWord = chineseWord;
	}

	public String getEsglisgAb() {
		return esglisgAb;
	}

	public void setEsglisgAb(String esglisgAb) {
		this.esglisgAb = esglisgAb;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
