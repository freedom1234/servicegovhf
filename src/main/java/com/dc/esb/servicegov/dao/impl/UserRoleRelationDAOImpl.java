package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.UserSystemRelationDAO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.UserRoleRelation;
@Repository
@Transactional
public class UserRoleRelationDAOImpl extends HibernateDAO<UserRoleRelation, String> implements UserSystemRelationDAO {
	private final static String DEL_HQL_RELATION = "delete  from USER_ROLE_RELATION where USER_ID=?";
	public void deleteRelation(String id){
		Session session = getSession();
		//根据userId删除关联表中的相关数据
		SQLQuery delquery = session.createSQLQuery(DEL_HQL_RELATION);
		delquery.setParameter(0, id);
		delquery.executeUpdate();
		}
}
