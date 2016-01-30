package com.dc.esb.servicegov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/12/31.
 */
@Entity
@Table(name = "METADATA_VERSION_RECORD")
public class MetadataVersion {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "version_no")
    private String versionNo;
    @Column(name = "version_desc")
    private String versionDesc;
    @Column(name = "file_path")
    private String filePath;//数据字典存储路径
    @Column(name = "opt_user")
    private String optUser;
    @Column(name = "opt_date")
    private String optDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }
}
