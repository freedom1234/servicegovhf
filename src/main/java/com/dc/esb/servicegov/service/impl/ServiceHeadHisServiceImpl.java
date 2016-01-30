package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceHeadHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ServiceHeadHis;
import com.dc.esb.servicegov.service.ServiceHeadHisService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceHeadHisServiceImpl extends AbstractBaseService<ServiceHeadHis,String> implements ServiceHeadHisService{

    @Autowired
    private ServiceHeadHisDAOImpl serviceHeadHisDAO;

    @Override
    public HibernateDAO<ServiceHeadHis, String> getDAO() {
        return serviceHeadHisDAO;
    }
}
