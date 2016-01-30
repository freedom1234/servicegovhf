package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ProcessContextDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ProcessContext;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/9/14.
 */
@Service
@Transactional
public class ProcessContextServiceImpl extends AbstractBaseService<ProcessContext, String> {

    @Autowired
    private ProcessContextDAOImpl processContextDAO;

    @Override
    public HibernateDAO<ProcessContext, String> getDAO() {
        return processContextDAO;
    }
}
