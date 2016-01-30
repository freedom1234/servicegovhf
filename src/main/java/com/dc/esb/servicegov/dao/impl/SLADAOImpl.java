package com.dc.esb.servicegov.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SLA;

@Repository
public class SLADAOImpl extends HibernateDAO<SLA, String> {
	
	private final static String GET_ALL_HQL = "select s from SLA s where s.slaTemplateId = null"; 
	private final static String GET_TEMPLATE_SLA_HQL = "select s from SLA s where s.slaTemplateId != null ";
	
	@Override
	public List<SLA> getAll(){
		return find(GET_ALL_HQL);
	}
	
	private List<SLA> findBy(String hql, Map<String, String> params){
		StringBuilder sb = new StringBuilder(hql);
		for(Map.Entry<String, String> entry : params.entrySet()){
			sb.append(" and ");
			sb.append(entry.getKey());
			sb.append("=");
			sb.append("'" +entry.getValue() + "'");
		}
		return find(sb.toString());
	}
	
	@Override
	public List<SLA> findBy(Map<String, String> params) {
		return findBy(GET_ALL_HQL,params);
	}
	
	public List<SLA> findTemplateBy(Map<String, String> params){
		return findBy(GET_TEMPLATE_SLA_HQL, params);
	}

	public List<SLA> getAllTemplateSLA(){
		return find(GET_TEMPLATE_SLA_HQL);
	}

	public List<SLA> getAllTemplateSLABy(Map<String, String> params){
		return findBy(GET_TEMPLATE_SLA_HQL,params);
	}
}
