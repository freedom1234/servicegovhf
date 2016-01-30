package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.service.impl.IdaServiceImpl;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.List;
import java.util.Map;

public interface IdaService  extends BaseService<Ida, String> {
    public void deletes(String [] ids);

    public void saveOrUpdate(Ida[] idas);

    public boolean updateMetadataId(String metadataId, String id);

    public boolean deleteList(List<Ida> list);

    public List<IdaServiceImpl.IdaMappingBean> findIdaMappingBy(Map<String,String> map,String orderByProperties,String serviceId, String operationId);

    public List findIdas(Map<String,String> reqMap, String orderStr);
    public boolean moveDown(String id);
    public boolean moveUp(String id);
    public boolean uniqueValid(String structName,String headId);
}
