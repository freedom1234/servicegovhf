package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceCategoryDAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceCategoryHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.ServiceCategoryHis;
import com.dc.esb.servicegov.service.ServiceCategoryHisService;

@Service
@Transactional
public class ServiceCategoryHisServiceImpl extends AbstractBaseService<ServiceCategoryHis,String> implements ServiceCategoryHisService{

    @Autowired
    private ServiceCategoryHisDAOImpl serviceCategoryHisDAO;

    @Override
    public HibernateDAO<ServiceCategoryHis, String> getDAO() {
        return serviceCategoryHisDAO;
    }
}
