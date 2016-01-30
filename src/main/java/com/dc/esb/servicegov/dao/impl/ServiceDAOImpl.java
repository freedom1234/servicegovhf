package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SDA;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.ServiceDAO;
import com.dc.esb.servicegov.entity.Service;

@Repository
public class ServiceDAOImpl extends HibernateDAO<Service, String> {
	
}
