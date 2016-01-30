package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.ServiceHis;
import com.dc.esb.servicegov.service.ServiceHisService;

@Service
@Transactional
public class ServiceHisServiceImpl extends AbstractBaseService<ServiceHis,String> implements ServiceHisService{

    @Autowired
    private ServiceHisDAOImpl serviceHisDAO;

    @Override
    public HibernateDAO<ServiceHis, String> getDAO() {
        return serviceHisDAO;
    }
}
