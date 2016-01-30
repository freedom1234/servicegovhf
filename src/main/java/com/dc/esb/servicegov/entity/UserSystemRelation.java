package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/15.
 */
@Entity
@Table(name = "USER_SYSTEM_RELATION")
public class UserSystemRelation implements Serializable {
    private static final long serialVersionUID = 11111L;
    @Id
    @Column(name = "USER_ID")
    private String userId;
    @Id
    @Column(name = "SYSTEM_ID")
    private String systemId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
