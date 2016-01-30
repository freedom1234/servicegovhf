package com.dc.esb.servicegov.dao.impl;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.EnumElementsDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.EnumElements;

@Repository
public class EnumElementsDAOImpl extends HibernateDAO<EnumElements, String> implements EnumElementsDAO{

}
