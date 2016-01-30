package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dc.esb.servicegov.entity.ErrorCode;
import com.dc.esb.servicegov.service.ErrorCodeService;

/**
 * TODO no DAO
 *
 */
@Service
@Transactional
public class ErrorCodeServiceImpl extends AbstractBaseService<ErrorCode,String> implements ErrorCodeService {

    @Override
    public HibernateDAO<ErrorCode, String> getDAO() {
        return null;
    }
}
