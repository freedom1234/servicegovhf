package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ProcessContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/9/14.
 */
@Repository
@Transactional
public class ProcessContextDAOImpl extends HibernateDAO<ProcessContext, String>{

}
