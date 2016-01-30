package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.OrgDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Organization;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Service
@Transactional
public class OrgServiceImpl extends AbstractBaseService<Organization, String>{
    @Autowired
    private OrgDAOImpl orgDAO;

    @Override
    public HibernateDAO<Organization, String> getDAO() {
        return orgDAO;
    }
    
  
}
