package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.List;

public interface ServiceInvokeService extends BaseService<ServiceInvoke, String> {
    public void updateProtocolId(String hql,String ...args );
    public void deleteEntity(List<ServiceInvoke> list);
}
