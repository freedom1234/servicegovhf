package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Permission;
import com.dc.esb.servicegov.entity.PermissionCategory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/7/13.
 */
@Repository
@Transactional
public class PermissionDAOImpl extends HibernateDAO<Permission,String> {


}
