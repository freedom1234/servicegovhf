package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SGEnumDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGEnum;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/6/24.
 */
@Service
@Transactional
public class SGEnumServiceImpl extends AbstractBaseService<SGEnum,String>{

    @Autowired
    private SGEnumDAOImpl sgEnumDAO;

    @Override
    public HibernateDAO<SGEnum, String> getDAO() {
        return sgEnumDAO;
    }
}
