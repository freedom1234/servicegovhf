package com.dc.esb.servicegov.dao.impl;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.EnumDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGEnum;

@Repository
public class EnumDAOImpl extends HibernateDAO<SGEnum, String> implements EnumDAO{
	
}
