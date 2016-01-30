package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.EntityTaskInfoDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.EntityTaskInfo;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/7/7.
 */
@Service
@Transactional
public class EntityTaskInfoServiceImpl extends AbstractBaseService<EntityTaskInfo, String> {
    @Autowired
    private EntityTaskInfoDAOImpl entityTaskInfoDAO;

    @Override
    public HibernateDAO<EntityTaskInfo, String> getDAO() {
        return entityTaskInfoDAO;
    }
}
