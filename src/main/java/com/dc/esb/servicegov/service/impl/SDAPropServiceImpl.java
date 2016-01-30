package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SDAPropDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.SDAProp;
import com.dc.esb.servicegov.service.SDAPropService;

@Service
@Transactional
public class SDAPropServiceImpl extends AbstractBaseService<SDAProp,String> implements SDAPropService{

    @Autowired
    private SDAPropDAOImpl sdaPropDAO;

    @Override
    public HibernateDAO<SDAProp, String> getDAO() {
        return null;
    }
}
