package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.IdaHIS;
import com.dc.esb.servicegov.service.IdaHISService;
import com.dc.esb.servicegov.service.IdaService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dc.esb.servicegov.dao.impl.IdaHISDAOImpl;

@Service
@Transactional
public class IdaHISServiceImpl extends AbstractBaseService<IdaHIS,String> implements IdaHISService {
	@Autowired
	private IdaHISDAOImpl IdaHISDAOImpl;

	@Override
	public HibernateDAO<IdaHIS, String> getDAO() {
		return IdaHISDAOImpl;
	}
}
