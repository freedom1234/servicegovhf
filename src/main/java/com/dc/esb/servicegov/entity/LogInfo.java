package com.dc.esb.servicegov.entity;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "SG_LOG")
public class LogInfo implements Serializable{
	private static final long serialVersionUID = -1207333465639890164L;
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="LOG_ID")
	private String id;

	@Column(name="LOG_USER_ID")
	private String userId;

	@Column(name="LOG_TIME")
	private String time;

	@Column(name="LOG_DETAIL", length=2048)
	private String detail;

	@Column(name="LOG_TYPE")
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		if(detail.length() > 255){
			detail = detail.substring(0, 254);
		}
		this.detail = detail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
