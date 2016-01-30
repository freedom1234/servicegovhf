package com.dc.esb.servicegov.dao.impl;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.Operation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.entity.SDA;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SDADAOImpl extends BaseDAOImpl<SDA> {
    public SDA findRootByOperation(Operation operation){
        String hql = " from " + SDA.class.getName() + " as s where s.serviceId=? and s.operationId=? and s.parentId is null";
        SDA sda = this.findUnique(hql, operation.getServiceId(), operation.getOperationId());
        return  sda;
    }
    public List<SDA> findByParentId(String parentId){
        String hql = " from SDA where parentId = ? order by seq asc";
        List<SDA> result = this.find(hql, parentId);
        return result;
    }
    /**
     * 根据xpath查找sda
     * @param serviceId
     * @param operationId
     * @param xpath
     * @return
     */
    public SDA findByXpath(String serviceId, String operationId, String xpath){
        if(StringUtils.isNotEmpty(xpath)){
            String hql = " from SDA where serviceId = ? and operationId = ? and xpath =?";
            List<SDA> sdas = this.find(hql, serviceId, operationId, xpath);
            if(sdas.size() > 0){
                return sdas.get(0);
            }
        }
        return null;
    }
    public List<SDA> findByHead(String headId, String structName){
        if(StringUtils.isNotEmpty(headId)){
            String hql = "from SDA a where a.parentId in (select b.id from SDA b where b.headId = ? and b.structName = ?)";
            List<SDA> sdas = this.find(hql, headId, structName);
            return sdas;
        }
        return new ArrayList<SDA>();
    }
    public SDA findByXpath(String headId, String xpath){
        if(StringUtils.isNotEmpty(xpath)){
            String hql = " from SDA where headId = ? and xpath =?";
            List<SDA> sdas = this.find(hql, headId, xpath);
            if(sdas.size() > 0){
                return sdas.get(0);
            }
        }
        return null;
    }
}
