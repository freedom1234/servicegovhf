package com.dc.esb.servicegov.vo;

import java.util.ArrayList;
import java.util.List;

import com.dc.esb.servicegov.entity.Service;
import com.dc.esb.servicegov.entity.ServiceCategory;

public class ServiceTreeViewBean {
	private  List<ServiceTreeViewBean> children = new ArrayList<ServiceTreeViewBean>();
	private String id;
	private String text;
	private String state;
	private String type;
	private Service service;
	private ServiceCategory serviceCategory;
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public ServiceCategory getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(ServiceCategory serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<ServiceTreeViewBean> getChildren() {
		return children;
	}
	public void setChildren(List<ServiceTreeViewBean> children) {
		this.children = children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
