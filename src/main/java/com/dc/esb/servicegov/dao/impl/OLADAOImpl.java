package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.OLA;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OLADAOImpl extends HibernateDAO<OLA, String> {
    private final static String GET_ALL_HQL = "select o from OLA o where o.olaTemplateId = null";
    private final static String GET_TEMPLATE_OLA_HQL = "select o from OLA o where o.olaTemplateId != null ";

    @Override
    public List<OLA> getAll() {
        return find(GET_ALL_HQL);
    }

    private List<OLA> findBy(String hql, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(hql);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(" and ");
            sb.append(entry.getKey());
            sb.append("=");
            sb.append("'"+entry.getValue()+"'");
        }
        return find(sb.toString());
    }

//    @Override
//    public List<OLA> findBy(Map<String, String> params) {
//        return findBy(GET_ALL_HQL, params);
//    }

    public List<OLA> findTemplateBy(Map<String, String> params) {
        return findBy(GET_TEMPLATE_OLA_HQL, params);
    }

    public List<OLA> getAllTemplateOLA() {
        return find(GET_TEMPLATE_OLA_HQL);
    }
}
