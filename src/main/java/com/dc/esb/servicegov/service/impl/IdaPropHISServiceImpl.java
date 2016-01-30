package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.IdaPropHIS;
import com.dc.esb.servicegov.service.IdaHISService;
import com.dc.esb.servicegov.service.IdaPropHISService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.IdaPropHISDAOImpl;

@Service
@Transactional
public class IdaPropHISServiceImpl extends AbstractBaseService<IdaPropHIS, String> implements IdaPropHISService{
	@Autowired
	private IdaPropHISDAOImpl idaPropHISDAOImpl;

	@Override
	public HibernateDAO<IdaPropHIS, String> getDAO() {
		return idaPropHISDAOImpl;
	}
}
