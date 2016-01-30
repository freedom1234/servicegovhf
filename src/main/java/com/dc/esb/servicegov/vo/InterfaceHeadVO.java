package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.entity.ServiceInvoke;

/**
 * Created by Administrator on 2015/7/31.
 */
public class InterfaceHeadVO {
    private InterfaceHead interfaceHead;
    private InterfaceHeadRelate interfaceHeadRelate;
    private ServiceInvoke serviceInvoke;

    public InterfaceHeadVO(){}
    public InterfaceHeadVO(InterfaceHead interfaceHead, InterfaceHeadRelate interfaceHeadRelate, ServiceInvoke serviceInvoke) {
        this.interfaceHead = interfaceHead;
        this.interfaceHeadRelate = interfaceHeadRelate;
        this.serviceInvoke = serviceInvoke;
    }

    public InterfaceHead getInterfaceHead() {
        return interfaceHead;
    }

    public void setInterfaceHead(InterfaceHead interfaceHead) {
        this.interfaceHead = interfaceHead;
    }

    public InterfaceHeadRelate getInterfaceHeadRelate() {
        return interfaceHeadRelate;
    }

    public void setInterfaceHeadRelate(InterfaceHeadRelate interfaceHeadRelate) {
        this.interfaceHeadRelate = interfaceHeadRelate;
    }

    public ServiceInvoke getServiceInvoke() {
        return serviceInvoke;
    }

    public void setServiceInvoke(ServiceInvoke serviceInvoke) {
        this.serviceInvoke = serviceInvoke;
    }

}
