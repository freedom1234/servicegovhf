//package com.dc.esb.servicegov.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//import javax.persistence.Id;
//
///**
// * Created by Administrator on 2015/7/6.
// */
//@Embeddable
//public class SystemProtocolPK implements java.io.Serializable{
//
//    @Id
//    @Column(name = "SYSTEM_ID",insertable = false,updatable = false)
//    private String systemId;
//
//    @Id
//    @Column(name = "PROTOCOL_ID",insertable = false,updatable = false)
//    private String protocolId;
//
//    public String getProtocolId() {
//        return protocolId;
//    }
//
//    public void setProtocolId(String protocolId) {
//        this.protocolId = protocolId;
//    }
//
//    public String getSystemId() {
//        return systemId;
//    }
//
//    public void setSystemId(String systemId) {
//        this.systemId = systemId;
//    }
//
//
//    public SystemProtocolPK() {
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        SystemProtocolPK that = (SystemProtocolPK) o;
//
//        if (protocolId != null ? !protocolId.equals(that.protocolId) : that.protocolId != null) return false;
//        if (systemId != null ? !systemId.equals(that.systemId) : that.systemId != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = systemId != null ? systemId.hashCode() : 0;
//        result = 31 * result + (protocolId != null ? protocolId.hashCode() : 0);
//        return result;
//    }
//}
