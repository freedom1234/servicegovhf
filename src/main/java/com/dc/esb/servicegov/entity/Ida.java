package com.dc.esb.servicegov.entity;

import javax.persistence.*;

import com.dc.esb.servicegov.export.IExportableNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "IDA")
@JsonIgnoreProperties("interObj")
public class Ida extends IExportableNode {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;
    @Column(name = "STRUCTNAME")
    private String structName;
    @Column(name = "STRUCTALIAS")
    private String structAlias;

    @Column(name = "METADATA_ID")
    private String metadataId;

    @Column(name = "SEQ")
    private int seq;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SCALE")
    private String scale;

    @Column(name = "LENGTH")
    private String length;

    @Column(name = "REQUIRED")
    private String required;

    @Column(name = "PARENT_ID",updatable=false,insertable=true)
    private String _parentId;

    @Column(name = "INTERFACE_ID")
    private String interfaceId;

    @Column(name = "OPT_USER")
    private String potUser;

    @Column(name = "OPT_DATE")
    private String potDate;

    @Column(name = "HEAD_ID")
    private String headId;

    @Column(name = "VERSION")
    private String version;

    @Column(name = "REMARK",length = 3072)
    private String remark;

    @Column(name = "sdaId")
    private String sdaId;

    @Column(name = "state", columnDefinition="varchar(10) default '0'")
    private String state;//状态，0：普通（可以在页面看到），1：空白（文件导出使用）

    @Column(name = "xpath")
    private String xpath;

    @ManyToOne
    @JoinColumn(name="INTERFACE_ID",referencedColumnName = "INTERFACE_ID",insertable = false,updatable = false)
    private Interface interObj;

    @ManyToOne
    @JoinColumn(name = "HEAD_ID",referencedColumnName = "HEAD_ID",insertable = false,updatable = false)
    private InterfaceHead heads;

    private String attrFlag;//判断是否有附加属性，不存储到数据库
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public String getStructAlias() {
        return structAlias;
    }

    public void setStructAlias(String structAlias) {
        this.structAlias = structAlias;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

	public String getParentId() {
		return get_parentId();
	}

	public void setParentId(String id) {
		set_parentId(id);
	}

	public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getPotUser() {
        return potUser;
    }

    public void setPotUser(String potUser) {
        this.potUser = potUser;
    }

    public String getPotDate() {
        return potDate;
    }

    public void setPotDate(String potDate) {
        this.potDate = potDate;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

//	public InterfaceHead getHead() {
//		return head;
//	}
//
//	public void setHead(InterfaceHead head) {
//		this.head = head;
//	}


    public Interface getInterObj() {
        return interObj;
    }

    public void setInterObj(Interface interObj) {
        this.interObj = interObj;
    }


    public String getRemark() {
        if(remark != null && remark.length() > 2048){
            remark.replaceAll(" ", "");
            remark.replaceAll("\t", "");
            if(remark.length() > 2048){
                remark = remark.substring(0, 2048);
            }
        }
        return remark;
    }

    public void setRemark(String remark) {
        if(remark != null && remark.length() > 2048){
            remark.replaceAll(" ", "");
            remark.replaceAll("\t", "");
            if(remark.length() > 2048){
                remark = remark.substring(0, 2048);
            }
        }
        this.remark = remark;
    }

//    public String getArgType() {
//        return argType;
//    }
//
//    public void setArgType(String argType) {
//        this.argType = argType;
//    }

    public InterfaceHead getHeads() {
        return heads;
    }

    public void setHeads(InterfaceHead heads) {
        this.heads = heads;
    }

    public String getSdaId() {
        return sdaId;
    }

    public void setSdaId(String sdaId) {
        this.sdaId = sdaId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String _parentId) {
        this._parentId = _parentId;
    }

    public String getAttrFlag() {
        return attrFlag;
    }

    public void setAttrFlag(String attrFlag) {
        this.attrFlag = attrFlag;
    }
}
