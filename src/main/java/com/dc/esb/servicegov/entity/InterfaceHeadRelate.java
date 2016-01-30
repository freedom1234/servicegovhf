package com.dc.esb.servicegov.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/9.
 */
@Entity
@Table(name = "INTERFACE_HEAD_RELATE")
public class InterfaceHeadRelate implements Serializable{

    @Id
    @Column(name = "INTERFACE_ID")
    private String interfaceId;

    @Id
    @Column(name = "HEAD_ID")
    private String headId;

    public String getInterfaceId() {
        return interfaceId;
    }

    @ManyToOne
    @JoinColumn(name = "INTERFACE_ID",referencedColumnName = "INTERFACE_ID",insertable = false,updatable = false)
    private Interface relateInters;

    @ManyToOne
    @JoinColumn(name = "HEAD_ID",referencedColumnName = "HEAD_ID",insertable = false,updatable = false)
    private InterfaceHead interfaceHead;

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public Interface getRelateInters() {
        return relateInters;
    }

    public void setRelateInters(Interface relateInters) {
        this.relateInters = relateInters;
    }

    public InterfaceHead getInterfaceHead() {
        return interfaceHead;
    }

    public void setInterfaceHead(InterfaceHead interfaceHead) {
        this.interfaceHead = interfaceHead;
    }
}
