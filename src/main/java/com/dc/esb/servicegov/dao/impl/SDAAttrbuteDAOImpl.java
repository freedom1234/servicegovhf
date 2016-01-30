package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.EnumDAO;
import com.dc.esb.servicegov.dao.SDAAttributeDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SDAAttribute;
import com.dc.esb.servicegov.entity.SGEnum;
import org.springframework.stereotype.Repository;

@Repository
public class SDAAttrbuteDAOImpl extends HibernateDAO<SDAAttribute, String> implements SDAAttributeDAO{
	
}
