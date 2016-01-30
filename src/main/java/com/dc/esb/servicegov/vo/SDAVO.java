package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.SDA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SDAVO implements Serializable{
	private static final long serialVersionUID = -8532079950395648627L;
	private SDA value;
    private List<SDAVO> childNode;
    private String xpath;
    
    public void addChild(SDAVO sdaVO){
    	if(null == childNode){
    		childNode = new ArrayList<SDAVO>();
    	}
    	childNode.add(sdaVO);
    }

	public SDA getValue() {
		return value;
	}

	public void setValue(SDA value) {
		this.value = value;
	}

	public List<SDAVO> getChildNode() {
		return childNode;
	}

	public void setChildNode(List<SDAVO> childNode) {
		this.childNode = childNode;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	} 
}
