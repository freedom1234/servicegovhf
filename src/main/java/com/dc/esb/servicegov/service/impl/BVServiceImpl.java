package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.BVMappingDAOImpl;
import com.dc.esb.servicegov.entity.BaseLineVersionHisMapping;

@Service
@Transactional
public class BVServiceImpl extends AbstractBaseService<BaseLineVersionHisMapping, String> {
	@Autowired
	private BVMappingDAOImpl daoImpl;

	@Override
	public HibernateDAO<BaseLineVersionHisMapping, String> getDAO() {
		return daoImpl;
	}
}
