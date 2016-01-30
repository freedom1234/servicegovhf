package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.entity.Ida;

import java.util.List;

@Repository
public class IdaDAOImpl extends HibernateDAO<Ida, String> {

    public List<Ida> findByParentIdOrder(String parentId){
        if(StringUtils.isNotEmpty(parentId)){
            String hql = " from Ida where _parentId = ? order by seq asc";
            List<Ida> result = this.find(hql, parentId);
            return result;
        }
        return null;
    }
    public List<Ida> findHeadOrder(String headId, String structName){
        if(StringUtils.isNotEmpty(headId)){
            String hql = "from Ida a where a._parentId in (select b.id from Ida b where b.headId = ? and b.structName=?)  order by a.seq asc";
            List<Ida> result = this.find(hql, headId, structName);
            return result;
        }
        return null;
    }
}