package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.IdaAttributeDAO;
import com.dc.esb.servicegov.dao.SDAAttributeDAO;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.IdaAttribute;
import com.dc.esb.servicegov.entity.SDAAttribute;
import org.springframework.stereotype.Repository;

@Repository
public class IdaAttrbuteDAOImpl extends HibernateDAO<IdaAttribute, String> implements IdaAttributeDAO {
	
}
