package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SLAHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.SLAHis;
import com.dc.esb.servicegov.service.SLAHisService;

@Service
@Transactional
public class SLAHisServiceImpl extends AbstractBaseService<SLAHis,String> implements SLAHisService{

    @Autowired
    private SLAHisDAOImpl slaHisDAO;

    @Override
    public HibernateDAO<SLAHis, String> getDAO() {
        return slaHisDAO;
    }
    @Override
    public void save(SLAHis entity){
    	slaHisDAO.save(entity);
    }
}
