package com.dc.esb.servicegov.vo;

/**
 * Created by vincentfxz on 15/9/7.
 */
public class InterfaceExVO {
    private String interfaceId;
    private String interfaceName;
    private String providers;
    private String consumers;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getConsumers() {
        return consumers;
    }

    public void setConsumers(String consumers) {
        this.consumers = consumers;
    }
}
