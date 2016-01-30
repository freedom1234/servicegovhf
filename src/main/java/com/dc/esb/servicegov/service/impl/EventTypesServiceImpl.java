package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.EventTypes;
import com.dc.esb.servicegov.service.EventTypesService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO no DAO
 */
@Service
@Transactional
public class EventTypesServiceImpl extends AbstractBaseService<EventTypes,String> implements EventTypesService{

    @Override
    public HibernateDAO<EventTypes, String> getDAO() {
        return null;
    }
}
