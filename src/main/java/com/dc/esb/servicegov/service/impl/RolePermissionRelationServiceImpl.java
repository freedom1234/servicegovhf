package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.RolePermissionRelationDAOImpl;
import com.dc.esb.servicegov.entity.RolePermissionRelation;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.BaseService;

/**
 * Created by lenovo on 2015/7/13.
 */
@Service
@Transactional
public class RolePermissionRelationServiceImpl extends AbstractBaseService<RolePermissionRelation, String> {
    @Autowired
    private RolePermissionRelationDAOImpl rolePermissionRelationDAOImpl;

    @Override
    public HibernateDAO<RolePermissionRelation, String> getDAO() {
        return rolePermissionRelationDAOImpl;
    }
}