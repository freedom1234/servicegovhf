package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.RoleDAOImpl;
import com.dc.esb.servicegov.dao.impl.UserDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Role;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractBaseService<Role, String>{
    @Autowired
    private RoleDAOImpl roleDAO;

    @Override
    public HibernateDAO<Role, String> getDAO() {
        return roleDAO;
    }


}
