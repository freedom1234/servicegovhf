package com.dc.esb.servicegov.dao.impl;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.EnumElementMapDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.EnumElementMap;

@Repository
public class EnumElementMapDAOImpl extends HibernateDAO<EnumElementMap, String> implements EnumElementMapDAO{

}
