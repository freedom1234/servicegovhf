package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.CorrelationPropertyInfo;
import com.dc.esb.servicegov.service.CorrelationPropertyInfoService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO 没有DAO
 */

@Service
@Transactional
public class CorrelationPropertyInfoServiceImpl extends AbstractBaseService<CorrelationPropertyInfo,String> implements CorrelationPropertyInfoService{

    @Override
    public HibernateDAO<CorrelationPropertyInfo, String> getDAO() {
        return null;
    }
}
