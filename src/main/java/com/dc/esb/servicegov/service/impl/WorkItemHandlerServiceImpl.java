package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.WorkItem;
import com.dc.esb.servicegov.entity.WorkItemHandler;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.WorkItemHandlerDAOImpl;

@Service
@Transactional
public class WorkItemHandlerServiceImpl extends AbstractBaseService<WorkItemHandler, String> {

	@Autowired
	private WorkItemHandlerDAOImpl workItemHandlerDAOImpl;

	@Override
	public HibernateDAO<WorkItemHandler, String> getDAO() {
		return workItemHandlerDAOImpl;
	}
}
