package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GENERATOR")
public class Generator {
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "IMPLEMENTS")
	private String implementsClazz;
	
	@Column(name = "DESCRIPTION")
	private String desc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImplementsClazz() {
		return implementsClazz;
	}

	public void setImplementsClazz(String implementsClazz) {
		this.implementsClazz = implementsClazz;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
