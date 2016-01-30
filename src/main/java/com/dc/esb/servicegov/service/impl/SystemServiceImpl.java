package com.dc.esb.servicegov.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.SystemDAOImpl;
import com.dc.esb.servicegov.service.SystemService;

@Service
@Transactional
public class SystemServiceImpl extends AbstractBaseService<System, String> implements SystemService{

	@Autowired
	private SystemDAOImpl systemDAOImpl;
	@Autowired
	private ServiceInvokeServiceImpl serviceInvokeService;


	@Override
	public HibernateDAO<System, String> getDAO() {
		return systemDAOImpl;
	}

	@Override
	public void insertProtocol(SystemProtocol systemProtocol) {
		systemDAOImpl.exeHql("insert into SystemProtocol(systemId,protocolId) values(?,?)",systemProtocol.getSystemId(),systemProtocol.getProtocolId());
	}

	public void deleteProtocol(SystemProtocol systemProtocol){
		systemDAOImpl.exeHql("delete from SystemProtocol where systemId = ?",systemProtocol.getSystemId());
	}

	public void deleteProtocolBySystemId(String systemId){
		systemDAOImpl.exeHql("delete from SystemProtocol where systemId = ?",systemId);
	}

	public boolean deleteSystemById(String systemId){
		//TZB要求有接口关联的不予删除系统
		System system = systemDAOImpl.get(systemId);
		List<ServiceInvoke> list = system.getServiceInvokes();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getInterfaceId() != null){
				return false;
			}
		}
		//要先删除SYSTEM_PROTOCOL记录
		deleteProtocolBySystemId(systemId);
		systemDAOImpl.delete(systemId);
		return true;
	}

	public List<System> getAllOrderBySystemId(){
		return systemDAOImpl.getAll("systemId",true);
	}

	/**
	 * 是否包含接口
	 * @return
	 */
	public boolean containsInterface(String systemId,String type){
		Map<String, String> params = new HashMap<String, String>();
		params.put("systemId", systemId);
		params.put("type",type);
		List<ServiceInvoke> list =  serviceInvokeService.findBy(params);
		if(list != null && list.size() > 0){
			for(ServiceInvoke si : list){
				if(StringUtils.isNotEmpty(si.getInterfaceId())){
					return true;
				}
			}
		}
		return false;
	}
	public System findUniqueByName(String name){
		String hql = " from System where systemId = ? or systemAb = ? or systemChineseName = ?";
		System system = systemDAOImpl.findUnique(hql, name, name, name);
		return system;
	}

	@Override
	public List<System> getByUserId(String userId) {
		String hql = "select s from System s, UserSystemRelation u where s.systemId = u.systemId and u.userId=? order by s.systemId asc";
		List<System> list = systemDAOImpl.find(hql, userId);
		return list;
	}
}
