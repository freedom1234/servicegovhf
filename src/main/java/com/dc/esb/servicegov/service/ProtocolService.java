package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.Protocol;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.List;

public interface ProtocolService extends BaseService<Protocol, String> {
    public List<Protocol> findBy(String hql, List<SearchCondition> searchConds);
}
