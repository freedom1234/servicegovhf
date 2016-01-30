package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.InterfaceHeadDAOImpl;
import com.dc.esb.servicegov.dao.impl.InterfaceHeadRelateDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.service.InterfaceHeadRelateService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.StringTokenizer;

/**
 * Created by Administrator on 2015/7/9.
 */
@Service
@Transactional
public class InterfaceHeadRelateServiceImpl extends AbstractBaseService<InterfaceHeadRelate,String> implements InterfaceHeadRelateService {
    @Autowired
    InterfaceHeadRelateDAOImpl interfaceHeadRelateDAO;

    @Override
    public HibernateDAO<InterfaceHeadRelate, String> getDAO() {
        return interfaceHeadRelateDAO;
    }

    @Override
    public void deleteRelate(String interfaceId){
        String hql = "delete from InterfaceHeadRelate where interfaceId=?";
        interfaceHeadRelateDAO.exeHql(hql,interfaceId);
    }
    @Override
    public void relateSave(String interfaceId,String headIds) {
        String hql = "delete from InterfaceHeadRelate where interfaceId=?";
        interfaceHeadRelateDAO.exeHql(hql,interfaceId);

        StringTokenizer tokenizer = new StringTokenizer(headIds,",");
        while (tokenizer.hasMoreElements()){
            String headId = tokenizer.nextElement().toString();
            InterfaceHeadRelate relate = new InterfaceHeadRelate();
            relate.setInterfaceId(interfaceId);
            relate.setHeadId(headId);
            interfaceHeadRelateDAO.save(relate);
        }


    }
}
