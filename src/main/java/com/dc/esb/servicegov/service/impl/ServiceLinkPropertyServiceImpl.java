package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceLinkPropertyDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ServiceLinkProperty;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/9/23.
 */
@Service
@Transactional
public class ServiceLinkPropertyServiceImpl extends AbstractBaseService<ServiceLinkProperty, String> {

    @Autowired
    private ServiceLinkPropertyDAOImpl serviceLinkPropertyDAO;

    @Override
    public HibernateDAO<ServiceLinkProperty, String> getDAO() {
        return serviceLinkPropertyDAO;
    }
}
