package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.SystemProtocol;
import com.dc.esb.servicegov.service.support.BaseService;

/**
 * Created by Administrator on 2015/7/6.
 */
public interface SystemProtocolService  extends BaseService<SystemProtocol,String>{
    public void deleteSystemProtocol(String systemId);
}

