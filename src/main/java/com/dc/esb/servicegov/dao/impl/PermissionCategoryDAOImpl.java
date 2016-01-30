package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.PermissionCategory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangqi on 2015/9/10.
 */
@Repository
@Transactional
public class PermissionCategoryDAOImpl extends HibernateDAO<PermissionCategory,String> {
}
