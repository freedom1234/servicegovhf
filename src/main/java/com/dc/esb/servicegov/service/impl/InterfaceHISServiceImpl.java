package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceHIS;
import com.dc.esb.servicegov.service.InterfaceHISService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.InterfaceHISDAOImpl;

import java.util.List;

@Service
@Transactional
public class InterfaceHISServiceImpl extends AbstractBaseService<InterfaceHIS, String>{
	@Autowired
	private InterfaceHISDAOImpl interfaceHISDAOImpl;

	/**
	 * 根据接口id查询接口发布历史
	 * @param interfaceId
	 * @return
	 */
	public List<InterfaceHIS> getHis(String interfaceId){
		String hql = " from " + InterfaceHIS.class.getName() + " as ih where ih.interfaceId = ? order by optDate desc";
		List<InterfaceHIS> list = interfaceHISDAOImpl.find(hql, interfaceId);
		return list;
	}
	@Override
	public HibernateDAO<InterfaceHIS, String> getDAO() {
		return interfaceHISDAOImpl;
	}
}
