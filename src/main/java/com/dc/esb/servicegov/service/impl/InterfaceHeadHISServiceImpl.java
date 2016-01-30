package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.entity.InterfaceHeadHIS;
import com.dc.esb.servicegov.service.InterfaceHeadHISService;
import com.dc.esb.servicegov.service.InterfaceHeadService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dc.esb.servicegov.dao.impl.InterfaceHeadHISDAOImpl;
@Service
@Transactional
public class InterfaceHeadHISServiceImpl extends AbstractBaseService<InterfaceHeadHIS, String> implements InterfaceHeadHISService{
	@Autowired
	private InterfaceHeadHISDAOImpl interfaceHeadHISDAOImpl;

	@Override
	public HibernateDAO<InterfaceHeadHIS, String> getDAO() {
		return interfaceHeadHISDAOImpl;
	}
}
