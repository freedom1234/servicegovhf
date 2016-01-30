package com.dc.esb.servicegov.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGUser;
import com.dc.esb.servicegov.entity.UserRoleRelation;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Repository
@Transactional
public class UserDAOImpl extends HibernateDAO<SGUser, String> {
	private final static String UPDATE_HQL = "update SG_USER set USER_PASSWORD=? where USER_ID=?";
	public void passWord(String str1,String str2){
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(UPDATE_HQL);
		query.setParameter(0,str1);
		query.setParameter(1,str2);
		query.executeUpdate();	
	}
}
