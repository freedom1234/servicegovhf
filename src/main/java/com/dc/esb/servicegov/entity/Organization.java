package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SG_ORG")
public class Organization implements Serializable {
    private static final long serialVersionUID = -1190402568959982370L;
    @Id
    @Column(name = "ORG_ID")
    private String orgId;
    @Column(name = "ORG_NAME")
    private String orgName;
    @Column(name = "ORG_AB")
    private String orgAB;
    @Column(name = "ORG_STATUS")
    private String orgStatus;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAB() {
        return orgAB;
    }

    public void setOrgAB(String orgAB) {
        this.orgAB = orgAB;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }
}
