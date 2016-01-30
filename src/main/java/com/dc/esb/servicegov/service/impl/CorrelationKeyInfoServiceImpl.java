package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.CorrelationKeyInfo;
import com.dc.esb.servicegov.service.CorrelationKeyInfoService;

/**
 * TODO 没有DAO
 */
@Service
@Transactional
public class CorrelationKeyInfoServiceImpl extends AbstractBaseService<CorrelationKeyInfo,String> implements CorrelationKeyInfoService{

    @Override
    public HibernateDAO<CorrelationKeyInfo, String> getDAO() {
        return null;
    }
}
