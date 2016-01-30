package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.BussProcessInfo;
import com.dc.esb.servicegov.service.BussProcessInfoService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO DAO 没有添加
 */
@Service
@Transactional
public class BussProcessInfoServiceImpl extends AbstractBaseService<BussProcessInfo,String> implements BussProcessInfoService {


    @Override
    public HibernateDAO<BussProcessInfo, String> getDAO() {
        return null;
    }
}
