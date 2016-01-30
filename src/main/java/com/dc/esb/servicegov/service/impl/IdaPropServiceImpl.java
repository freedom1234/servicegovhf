package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.IdaProp;
import com.dc.esb.servicegov.service.IdaPropHISService;
import com.dc.esb.servicegov.service.IdaPropService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.IdaPropDAOImpl;
@Service
@Transactional
public class IdaPropServiceImpl extends AbstractBaseService<IdaProp, String> implements IdaPropService {
	@Autowired
	private IdaPropDAOImpl IdaPropDAOImpl;

	@Override
	public HibernateDAO<IdaProp, String> getDAO() {
		return IdaPropDAOImpl;
	}
}
