package com.dc.esb.servicegov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.UserRoleRelationDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.UserRoleRelation;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
@Service
@Transactional
public class UserRoleRelationServiceImpl extends AbstractBaseService<UserRoleRelation, String>{
	 @Autowired
	 private UserRoleRelationDAOImpl userRoleRelationDAO;

	@Override
	public HibernateDAO<UserRoleRelation, String> getDAO() {
		return userRoleRelationDAO;
	}
	 public void deleteRelation(String id){
		 userRoleRelationDAO.deleteRelation(id);
	    }
}
