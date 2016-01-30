package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDAOImpl<T> extends HibernateDAO<T,String> {

}
