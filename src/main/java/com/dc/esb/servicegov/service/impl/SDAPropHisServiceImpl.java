package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SDAPropHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.SDAPropHis;
import com.dc.esb.servicegov.service.SDAPropHisService;

@Service
@Transactional
public class SDAPropHisServiceImpl extends AbstractBaseService<SDAPropHis,String> implements SDAPropHisService{

    @Autowired
    private SDAPropHisDAOImpl sdaPropHisDAO;
    @Override
    public HibernateDAO<SDAPropHis, String> getDAO() {
        return sdaPropHisDAO;
    }
}
