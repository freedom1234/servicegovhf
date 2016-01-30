package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.Map;

public interface InterfaceHeadRelateService  extends BaseService<InterfaceHeadRelate, String> {
	public void relateSave(String interfaceId,String headIds);
	public void deleteRelate(String interfaceId);

}
