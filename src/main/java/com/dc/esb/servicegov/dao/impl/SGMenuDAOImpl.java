package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGMenu;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Repository
@Transactional
public class SGMenuDAOImpl extends HibernateDAO<SGMenu,String> {
}
