package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.Interface;
import com.dc.esb.servicegov.entity.InterfaceInvoke;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import org.apache.commons.lang.StringUtils;

/**
 * Created by wang on 2015/8/12.
 */
public class InterfaceInvokeVO2 {
    private String id;

    private String conId;
    private String conName;
    private String conInterId;
    private String conInterName;
    private String conType;
    private String conIsStandard;

    private String proId;
    private String proName;
    private String proInterId;
    private String proInterName;
    private String proType;
    private String proIsStandard;

    public InterfaceInvokeVO2(){}

    public InterfaceInvokeVO2(InterfaceInvoke interfaceInvoke){
        if(interfaceInvoke != null){
            this.id = interfaceInvoke.getId();
            ServiceInvoke consumer = interfaceInvoke.getConsumer();
            ServiceInvoke provider = interfaceInvoke.getProvider();

            if(consumer != null){
                this.conId = consumer.getSystemId();
                if(consumer.getSystem() != null){
                    this.conName = consumer.getSystem().getSystemChineseName();
                }
                this.conInterId = consumer.getInterfaceId();
                if(consumer.getInter() != null){
                    this.conInterName = consumer.getInter().getInterfaceName();
                }
                this.conType = consumer.getType();
                this.conIsStandard = consumer.getIsStandard();
            }

            if(provider != null){
                this.proId = provider.getSystemId();
                if(provider.getSystem() != null){
                    this.proName = provider.getSystem().getSystemChineseName();
                }
                this.proInterId = provider.getInterfaceId();
                if(provider.getInter() != null){
                    this.proInterName = provider.getInter().getInterfaceName();
                }
                this.proType = provider.getType();
                this.proIsStandard = provider.getIsStandard();
            }
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConInterId() {
        return conInterId;
    }

    public void setConInterId(String conInterId) {
        this.conInterId = conInterId;
    }

    public String getConInterName() {
        return conInterName;
    }

    public void setConInterName(String conInterName) {
        this.conInterName = conInterName;
    }

    public String getConType() {
        return conType;
    }

    public void setConType(String conType) {
        this.conType = conType;
    }

    public String getConIsStandard() {
        return conIsStandard;
    }

    public void setConIsStandard(String conIsStandard) {
        this.conIsStandard = conIsStandard;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProInterId() {
        return proInterId;
    }

    public void setProInterId(String proInterId) {
        this.proInterId = proInterId;
    }

    public String getProInterName() {
        return proInterName;
    }

    public void setProInterName(String proInterName) {
        this.proInterName = proInterName;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProIsStandard() {
        return proIsStandard;
    }

    public void setProIsStandard(String proIsStandard) {
        this.proIsStandard = proIsStandard;
    }
}
