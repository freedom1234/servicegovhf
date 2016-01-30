package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.OLAHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.OLAHis;
import com.dc.esb.servicegov.service.OLAHisService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 没有DAO
 */
@Service
@Transactional
public class OLAHisServiceImpl extends AbstractBaseService<OLAHis,String> implements OLAHisService{
    @Autowired
    private OLAHisDAOImpl daoImpl;
    public List<OLAHis> getByOperation(String autoId){
        String hql = " from OLAHis where operationHisId = ?";
        return daoImpl.find(hql, autoId);
    }

    @Override
    public HibernateDAO<OLAHis, String> getDAO() {
        return daoImpl;
    }
    @Override
    public void save(OLAHis entity){
    	daoImpl.save(entity);
    }
}
