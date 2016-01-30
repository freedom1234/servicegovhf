package com.dc.esb.servicegov.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "METADATA_HIS")
public class MetadataHis implements Serializable {
    @Id
    @Column(name = "AUTO_ID")
    private String autoId;
    @Column(name = "VERSION_HIS_ID")
    private String versionHisId;

    @Column(name = "METADATA_ID", length = 50)
    private String metadataId;
    @Column(name = "METADATA_NAME", length = 255)
    private String metadataName;
    @Column(name = "CHINESE_NAME", length = 255)
    private String chineseName;
    @Column(name = "CATEGORY_WORD_ID", length = 50)
    private String categoryWordId;
    @Column(name = "REMARK", length = 1023)
    private String remark;
    @Column(name = "TYPE", length = 30)
    private String type;
    @Column(name = "LENGTH", length = 15)
    private String length;
    @Column(name = "SCALE", length = 10)
    private String scale;
    @Column(name = "ENUM_ID", length = 50)
    private String enumId;
    @Column(name = "METADATA_ALIAS", length = 255)
    private String metadataAlias;
    @Column(name = "BUSS_DEFINE", length = 1023)
    private String bussDefine;
    @Column(name = "BUSS_RULE", length = 1023)
    private String bussRule;
    @Column(name = "DATA_SOURCE", length = 255)
    private String dataSource;
    @Column(name = "TEMPLATE_ID", length = 50)
    private String templateId;
    @Column(name = "STATUS", length = 10)
    private String status;
    @Column(name = "OPT_USER", length = 50)
    private String optUser;
    @Column(name = "OPT_DATE", length = 20)
    private String optDate;
    @Column(name = "AUDIT_USER", length = 50)
    private String auditUser;
    @Column(name = "AUDIT_DATE", length = 20)
    private String auditDate;
    @Column(name = "PROCESS_ID", length = 50)
    private String processId;
    @Column(name = "DATA_FORMULA", length = 20)
    private String dataFormula;
    @Column(name = "BUZZ_CATEGORY", length = 255)
    private String buzzCategory;
    @Column(name = "DATA_CATEGORY", length = 255)
    private String dataCategory;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_WORD_ID",referencedColumnName = "ESGLISG_AB", insertable = false, updatable = false)
    private CategoryWord categoryWord;

    @OneToOne(cascade={CascadeType.REFRESH}, optional=true)
    @JoinColumn(name="VERSION_HIS_ID", insertable = false, updatable = false)
    private VersionHis versionHis;

    public MetadataHis(){}

    public MetadataHis(Metadata metadata){
        this.autoId = UUID.randomUUID().toString();
        this.metadataId = metadata.getMetadataId();
        this.metadataName = metadata.getMetadataName();
        this.chineseName = metadata.getChineseName();
        this.categoryWordId = metadata.getCategoryWordId();
        this.remark = metadata.getRemark();
        this.type = metadata.getType();
        this.length = metadata.getLength();
        this.scale = metadata.getScale();
        this.enumId = metadata.getEnumId();
        this.metadataAlias = metadata.getMetadataAlias();
        this.bussDefine = metadata.getBussDefine();
        this.bussRule = metadata.getBussRule();
        this.dataSource = metadata.getDataSource();
        this.templateId = metadata.getTemplateId();
        this.status = metadata.getStatus();
        this.optUser = metadata.getOptUser();
        this.optDate = metadata.getOptDate();
        this.auditUser = metadata.getAuditUser();
        this.auditDate = metadata.getAuditDate();
        this.processId = metadata.getProcessId();
        this.dataFormula = metadata.getDataFormula();
        this.buzzCategory = metadata.getBuzzCategory();
        this.dataCategory = metadata.getDataCategory();
    }

    public CategoryWord getCategoryWord() {
        return categoryWord;
    }

    public void setCategoryWord(CategoryWord categoryWord) {
        this.categoryWord = categoryWord;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getCategoryWordId() {
        return categoryWordId;
    }

    public void setCategoryWordId(String categoryWordId) {
        this.categoryWordId = categoryWordId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getEnumId() {
        return enumId;
    }

    public void setEnumId(String enumId) {
        this.enumId = enumId;
    }

    public String getMetadataAlias() {
        return metadataAlias;
    }

    public void setMetadataAlias(String metadataAlias) {
        this.metadataAlias = metadataAlias;
    }

    public String getBussDefine() {
        return bussDefine;
    }

    public void setBussDefine(String bussDefine) {
        this.bussDefine = bussDefine;
    }

    public String getBussRule() {
        return bussRule;
    }

    public void setBussRule(String bussRule) {
        this.bussRule = bussRule;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }


    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getDataFormula() {
        return dataFormula;
    }

    public void setDataFormula(String dataFormula) {
        this.dataFormula = dataFormula;
    }


    public String getBuzzCategory() {
        return buzzCategory;
    }

    public void setBuzzCategory(String buzzCategory) {
        this.buzzCategory = buzzCategory;
    }

    public String getDataCategory() {
        return dataCategory;
    }

    public void setDataCategory(String dataCategory) {
        this.dataCategory = dataCategory;
    }

    public String getFormula(){
        String formula = this.type;
        if(StringUtils.isNotEmpty(this.length)){
            if(StringUtils.isNotEmpty(this.scale)){
                formula = formula +"(" + length + "," + this.scale + ")";
            }else{
                formula = formula +"("+length+")";
            }
        }
        return formula;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }
}
