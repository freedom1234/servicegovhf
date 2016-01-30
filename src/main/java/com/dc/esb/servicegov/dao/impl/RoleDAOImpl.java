package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Role;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Repository
@Transactional
public class RoleDAOImpl extends HibernateDAO<Role, String> {
}
