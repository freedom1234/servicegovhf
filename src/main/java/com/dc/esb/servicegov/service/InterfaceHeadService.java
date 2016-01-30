package com.dc.esb.servicegov.service;


import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.List;

public interface InterfaceHeadService extends BaseService<InterfaceHead, String> {
    public void initHDA(InterfaceHead interfaceHead);
    public boolean uniqueValid(String headName);
    public List<List<Ida>> getHeadersByInterfaceId(String interfaceId
    );
}
