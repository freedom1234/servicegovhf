package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.PermissionCategoryDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.PermissionCategory;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangqi on 2015/9/10.
 */
@Service
@Transactional
public class PermissionCategoryServiceImpl extends AbstractBaseService<PermissionCategory,String> {
    @Autowired
    private PermissionCategoryDAOImpl permissionCategoryDAO;
    @Override
    public HibernateDAO<PermissionCategory, String> getDAO() {
        return permissionCategoryDAO;
    }
}
