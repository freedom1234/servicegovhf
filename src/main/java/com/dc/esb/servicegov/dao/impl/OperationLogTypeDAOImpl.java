package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.OperationLogType;
import org.springframework.stereotype.Repository;

/**
 * Created by wang on 2015/8/18.
 */
@Repository
public class OperationLogTypeDAOImpl extends HibernateDAO<OperationLogType, String> {
}
