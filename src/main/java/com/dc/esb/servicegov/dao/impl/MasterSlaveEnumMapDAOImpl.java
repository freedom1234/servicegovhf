package com.dc.esb.servicegov.dao.impl;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.MasterSlaveEnumMapDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.EnumElements;
import com.dc.esb.servicegov.entity.MasterSlaveEnumMap;

@Repository
public class MasterSlaveEnumMapDAOImpl extends HibernateDAO<MasterSlaveEnumMap, String> implements MasterSlaveEnumMapDAO{
	
}
