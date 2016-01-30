package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SystemProtocolDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SystemProtocol;
import com.dc.esb.servicegov.service.SystemProtocolService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/7/6.
 */
@Service
@Transactional
public class SystemProtocolServiceImpl extends AbstractBaseService<SystemProtocol, String> implements SystemProtocolService {
@Autowired
private SystemProtocolDAOImpl systemProtocolDAOImpl;


@Override
public HibernateDAO<SystemProtocol, String> getDAO() {
        return systemProtocolDAOImpl;
        }

        @Override
        public void deleteSystemProtocol(String systemId) {
                String hql = "delete from SystemProtocol where systemId = ?";
                systemProtocolDAOImpl.exeHql(hql,systemId);
        }
}
