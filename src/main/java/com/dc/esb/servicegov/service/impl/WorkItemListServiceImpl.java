package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.WorkItemList;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.WorkItemListDAOImpl;

@Service
@Transactional
public class WorkItemListServiceImpl extends AbstractBaseService<WorkItemList, String> {

	@Autowired
	private WorkItemListDAOImpl workItemListDAOImpl;

	@Override
	public HibernateDAO<WorkItemList, String> getDAO() {
		return workItemListDAOImpl;
	}
}
