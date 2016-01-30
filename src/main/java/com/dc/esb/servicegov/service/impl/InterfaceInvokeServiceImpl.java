package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.InterfaceInvokeDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceInvoke;
import com.dc.esb.servicegov.service.InterfaceInvokeService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.vo.InterfaceInvokeVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqi on 2015/8/10.
 */
@Service
@Transactional
public class InterfaceInvokeServiceImpl extends AbstractBaseService<InterfaceInvoke, String> implements InterfaceInvokeService{

    @Autowired
    private InterfaceInvokeDAOImpl interfaceInvokeDAOImpl;
    @Override
    public HibernateDAO<InterfaceInvoke, String> getDAO() {
        return interfaceInvokeDAOImpl;
    }

    public List<InterfaceInvokeVO> getVOList(List<InterfaceInvoke> interfaceInvokeList){
        List<InterfaceInvokeVO> voList = new ArrayList<InterfaceInvokeVO>();
        for(InterfaceInvoke interfaceInvoke : interfaceInvokeList){
            for(InterfaceInvokeVO interfaceInvokeVO : voList){
                if(StringUtils.isNotEmpty(interfaceInvokeVO.getConsumers()) && interfaceInvokeVO.getConsumers().equals(interfaceInvoke.getConsumer()) ){

                }
            }
        }
        return null;
    }

    public List<InterfaceInvoke> getBySOId(String serviceId, String operationId){
        String hql = " from " + InterfaceInvoke.class.getName() + " i where i.provider.serviceId = ? and i.provider.operationId = ?";
        List<InterfaceInvoke> list = interfaceInvokeDAOImpl.find(hql, serviceId, operationId);
        return list;
    }

}
