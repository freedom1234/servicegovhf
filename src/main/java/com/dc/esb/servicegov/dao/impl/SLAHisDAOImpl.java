package com.dc.esb.servicegov.dao.impl;

import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.entity.SLAHis;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.OLA;

@Repository
public class SLAHisDAOImpl extends HibernateDAO<SLAHis, String> {
	private final static String GET_ALL_HQL = "select o from SLAHis o where o.olaTemplateId = null";
	private final static String GET_TEMPLATE_OLA_HQL = "select o from SLAHis o where o.olaTemplateId != null ";
	
	@Override
	public List<SLAHis> getAll(){
		return find(GET_ALL_HQL);
	}
	
	private List<SLAHis> findBy(String hql, Map<String, String> params){
		StringBuilder sb = new StringBuilder(hql);
		for(Map.Entry<String, String> entry : params.entrySet()){
			sb.append(" and ");
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
		}
		return find(sb.toString());
	}
	
	@Override
	public List<SLAHis> findBy(Map<String, String> params) {
		return findBy(GET_ALL_HQL,params);
	}
	
	public List<SLAHis> findTemplateBy(Map<String, String> params){
		return findBy(GET_TEMPLATE_OLA_HQL, params);
	}
	
	public List<SLAHis> getAllTemplateOLA(){
		return find(GET_TEMPLATE_OLA_HQL);
	}
}
