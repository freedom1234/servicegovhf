package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.WorkItemListRelation;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.WorkItemListRelationDAOImpl;

@Service
@Transactional
public class WorkItemListRelationServiceImpl extends AbstractBaseService<WorkItemListRelation, String>{

	@Autowired
	private WorkItemListRelationDAOImpl workItemListRelationDAOImpl;

	@Override
	public HibernateDAO<WorkItemListRelation, String> getDAO() {
		return workItemListRelationDAOImpl;
	}
}
