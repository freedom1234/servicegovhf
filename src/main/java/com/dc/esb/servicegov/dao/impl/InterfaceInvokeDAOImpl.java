package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceInvoke;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.vo.InterfaceInvokeVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangqi on 2015/8/10.
 */
@Repository
public class InterfaceInvokeDAOImpl extends HibernateDAO<InterfaceInvoke, String> {
    public List<InterfaceInvoke> getBySO(String serviceId, String operation){
        String hql = "select  a from " + InterfaceInvoke.class.getName()
                + " as a where (a.consumer.serviceId=? and a.consumer.operationId=? and a.consumer.type=?) " +
                "or (a.provider.serviceId=? and a.provider.operationId=? and a.provider.type=?)";
         List<InterfaceInvoke> interfaceInvokes = this.find(hql, serviceId, operation, Constants.INVOKE_TYPE_CONSUMER, serviceId, operation, Constants.INVOKE_TYPE_PROVIDER);
        return interfaceInvokes;
    }
    public List<Object[]> getVOBySO(String serviceId, String operationId){
        String hql =  " select si.type, si.interfaceId, si.isStandard from " +
//                ServiceInvoke.class.getName() + " as si where si.serviceId= ? and si.operationId=? and si.interfaceId is not null and si.interfaceId != '' group by si.interfaceId, si.type";
                ServiceInvoke.class.getName() + " as si where si.serviceId= ? and si.operationId=? and si.interfaceId is not null group by si.interfaceId, si.type, si.isStandard";
        List<Object[]> list = this.findFree(hql, serviceId, operationId);

        return list;
    }

    public List<InterfaceInvoke> getStandard(String serviceId, String operationId){
        String hql = " from " + InterfaceInvoke.class.getName() + " as ii where ii.provider.interfaceId is null and ii.consumer.interfaceId is null and ii.provider.serviceId=? and ii.provider.operationId=? ";
        List<InterfaceInvoke> list = this.find(hql, serviceId, operationId);
        return list;
    }

    public List<InterfaceInvoke> getByOperation(String serviceId, String operationId){
        String hql = " from " + InterfaceInvoke.class.getName() + " as ii where ii.provider.serviceId=? and ii.provider.operationId=? ";
        List<InterfaceInvoke> list = this.find(hql, serviceId, operationId);
        return list;
    }

    public InterfaceInvoke getByProIdConId(String providerId, String consumerId){
        Map<String, String> params = new HashMap<String, String>();
        params.put("providerInvokeId", providerId);
        params.put("consumerInvokeId", consumerId);
        InterfaceInvoke interfaceInvoke = this.findUniqureBy(params);
        return interfaceInvoke;
    }
}
