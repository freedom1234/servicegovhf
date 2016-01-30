package com.dc.esb.servicegov.dao.impl;

import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.jsonObj.ServiceInvokeJson;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.lang.StringUtils;
import org.dom4j.io.SAXEventRecorder;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.support.HibernateDAO;

@Repository
public class ServiceInvokeDAOImpl  extends HibernateDAO<ServiceInvoke, String> {
	public List<?> getBLInvoke(String baseId) {
        String hql = "select new "+ ServiceInvokeJson.class.getName()+"(invoke) from "+ ServiceInvoke.class.getName() + " as invoke where invoke.invokeId in  (select si.invokeId from " +ServiceInvoke.class.getName() + " as si, "
               +OperationHis.class.getName()+ " as oh where oh.versionHis.autoId in (select bvhm.versionHisId from " + BaseLineVersionHisMapping.class.getName() + " as bvhm where bvhm.baseLineId=?)" +
                "	and si.serviceId = oh.serviceId and si.operationId = oh.operationId)";
        List<?> list = find(hql, baseId);
        return list;
    }

    public void updateBySO(String serviceId, String operationId){
        String hql = " update " + ServiceInvoke.class.getName() + " set serviceId = null, operationId=null where serviceId = ? and operationId = ?";
        super.exeHql(hql, serviceId, operationId  );
    }

    /*
     * @param serviceId
     * @param operationId
     * @return
     */
    public List<?> findJsonBySO(String serviceId, String operationId){
//        String hql = " select new com.dc.esb.servicegov.entity.jsonObj.ServiceInvokeJson("+
//                " s.invokeId, s.systemId, s.isStandard, s.serviceId, s.operationId, s.interfaceId, s.type, s.desc, s.remark,"+
//                " s.interfaceId, s.system.systemChineseName)"+
//                " from "+ ServiceInvoke.class.getName()+" as s " +
//                "where s.serviceId=? and s.operationId=? ";
        String hql = "select new "+ ServiceInvokeJson.class.getName()+"(s) from "+ ServiceInvoke.class.getName()+" as s where s.serviceId=? and s.operationId=? ";
        List<?> list = super.find(hql, serviceId, operationId);
        return list;
    }
    /**
     * 根据消费者查找提供者，或者根据提供者查找消费者
     */
    @Deprecated
    public ServiceInvoke getByOtherType(String invokeId){
        ServiceInvoke serviceInvoke = this.findUniqueBy("invokeId", invokeId);
        if(StringUtils.isNotEmpty(serviceInvoke.getServiceId()) && StringUtils.isNotEmpty(serviceInvoke.getOperationId()) ){
            String type = serviceInvoke.getType();
            if(StringUtils.isNotEmpty(type)){
                if(type.equals(Constants.INVOKE_TYPE_CONSUMER)){
                    type = Constants.INVOKE_TYPE_PROVIDER;
                }
                else{
                    type = Constants.INVOKE_TYPE_CONSUMER;
                }
                String hql = " from " + ServiceInvoke.class.getName() + " as si where si.type = ? and si.serviceId = ? and si.operationId = ?";
                List<ServiceInvoke> list = this.find(hql, type, serviceInvoke.getServiceId(), serviceInvoke.getOperationId());
                if(list.size() > 0){
                    return list.get(0);
                }
            }
        }
        return null;
    }
    public List<ServiceInvoke> getByOtherType(ServiceInvoke si){
        String hql = " select si from " + ServiceInvoke.class.getName() + " as si, " +
                InterfaceInvoke.class.getName() + " as ii where si.invokeId = ii.invokeId ";
        String extend = Constants.INVOKE_TYPE_CONSUMER.equals(si.getType())? " and ii.providerId = ? " :" and ii.consumerId = ?";
        List<ServiceInvoke> list = this.find(hql+extend, si.getInvokeId());
        return list;
    }
    /**
     * 根据二级服务分类id
     */
    public List<ServiceInvoke> getByServiceCagegoryId2(String categoryId){
        String hql = "select si from " + ServiceInvoke.class.getName() + " as si, " + Service.class.getName()
                + " as s where si.serviceId = s.serviceId  and s.categoryId = ?  order by si.serviceId, si.operationId asc";
        List<ServiceInvoke> list = this.find(hql, categoryId);
        return list;
    }
    /**
     * 根据一级服务分类Id
     */
    public List<ServiceInvoke> getByServiceCagegoryId1(String categoryId){
        String hql = "select si from " + ServiceInvoke.class.getName() + " as si, " + Service.class.getName()
                + " as s, " + ServiceCategory.class.getName() + " sc where si.serviceId = s.serviceId  and s.categoryId = sc.categoryId and sc.parentId = ?  order by si.serviceId, si.operationId asc";
        List<ServiceInvoke> list = this.find(hql , categoryId);
        return list;
    }
    /**
     * 根据所有服务分类
     */
    public List<ServiceInvoke> getByServiceCagegoryId0(){
        String hql = "select si from " + ServiceInvoke.class.getName() + " as si, " + Service.class.getName()
                + " as s, " + ServiceCategory.class.getName() + " sc where si.serviceId = s.serviceId  and s.categoryId = sc.categoryId order by si.serviceId, si.operationId asc";
        List<ServiceInvoke> list = this.find(hql);
        return list;
    }

    public List<ServiceInvoke> getByOperationAndType(Operation operation, String type){
        String hql = " from "+ ServiceInvoke.class.getName() + " as si where si.serviceId = ? and si.operationId = ? and si.type = ?";
        List<ServiceInvoke> list = this.find(hql, operation.getServiceId(), operation.getOperationId(), type);
        return list;
    }

    public List<ServiceInvoke> getByOperationPK(OperationPK pk){
        String hql = " from "+ ServiceInvoke.class.getName() + " as si where si.serviceId = ? and si.operationId = ?";
        List<ServiceInvoke> list = this.find(hql, pk.getServiceId(), pk.getOperationId());
        return list;
    }
}