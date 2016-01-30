package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.UserRoleRelation;
import com.dc.esb.servicegov.entity.UserSystemRelation;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jbpm.task.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserSystemRelationDAOImpl extends HibernateDAO<UserSystemRelation, String>{
	public void deleteRelation(String userId){
		String hql = "delete from " + UserSystemRelation.class.getName() + " where userId = ? ";
		this.exeHql(hql, userId);
	}
}
