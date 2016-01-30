package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.SDAHis;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2015/9/21.
 */
public class SDACompareVO {
    String id;
    String text;
    String _parentId;


    String structName1;
    String structAlias1;
    String metadataId1;
    String seq1;
    String  parentId1;
    String desc1;
    String  remark1;
    String type1;
    String   length1;
    String  required1;

    String structName2;
    String structAlias2;
    String metadataId2;
    String seq2;
    String  parentId2;
    String desc2;
    String  remark2;
    String type2;
    String   length2;
    String  required2;

    String color="";

    public SDACompareVO(SDAHis sdaHis1, SDAHis sdaHis2){
        if(sdaHis1 != null){
            this.id = sdaHis1.getSdaId();
            this.text = sdaHis1.getStructName();
            this._parentId = sdaHis1.getParentId();

            structName1 = sdaHis1.getStructName();
            structAlias1 = sdaHis1.getStructAlias();
            metadataId1 = sdaHis1.getMetadataId();
            seq1 = String.valueOf(sdaHis1.getSeq());
            parentId1 = sdaHis1.getParentId();
            desc1 = sdaHis1.getDesc();
            remark1 = sdaHis1.getRemark();
            type1 = sdaHis1.getType();
            length1 = sdaHis1.getLength();
            required1 = sdaHis1.getRequired();
        }
        if(sdaHis2 != null){
            structName2 = sdaHis2.getStructName();
            structAlias2= sdaHis2.getStructAlias();
            metadataId2 = sdaHis2.getMetadataId();
            seq2 =  String.valueOf(sdaHis2.getSeq());
            parentId2 = sdaHis2.getParentId();
            desc2 = sdaHis2.getDesc();
            remark2 = sdaHis2.getRemark();
            type2 = sdaHis2.getType();
            length2 = sdaHis2.getLength();
            required2 = sdaHis2.getRequired();
        }
        if(sdaHis1 != null && sdaHis2 == null){//添加操作
            this.color = "green";
        }
        if(sdaHis1 != null && sdaHis2 != null){//修改操作
            if(compareStr(sdaHis1.getStructName(), sdaHis2.getStructName())
                    &&compareStr(sdaHis1.getStructAlias(), sdaHis2.getStructAlias())
                    &&compareStr(sdaHis1.getMetadataId(), sdaHis2.getMetadataId())
                    &&sdaHis1.getSeq() == sdaHis2.getSeq()
                    &&compareStr(sdaHis1.getParentId(), sdaHis2.getParentId())
                    &&compareStr(sdaHis1.getDesc(), sdaHis2.getDesc())
                    &&compareStr(sdaHis1.getRemark(), sdaHis2.getRemark())
                    &&compareStr(sdaHis1.getType(), sdaHis2.getType())
                    &&compareStr(sdaHis1.getLength(), sdaHis2.getLength())
                    &&compareStr(sdaHis1.getRequired(), sdaHis2.getRequired())){

            }else{
                this.color = "yellow";
            }
        }
        if(sdaHis1 == null && sdaHis2 != null){//删除操作
            this.id = sdaHis2.getSdaId();
            this.text = "";
            this._parentId = sdaHis2.getParentId();
            this.color = "red";
        }
    }

    public boolean compareStr(String s1, String s2){
        if(StringUtils.isNotEmpty(s1) && StringUtils.isNotEmpty(s2)){
            if(!s1.equals(s2)){
                return  false;
            }
        }
        if(StringUtils.isNotEmpty(s1) && StringUtils.isEmpty(s2)){
            return false;
        }
        if(StringUtils.isEmpty(s1) && StringUtils.isNotEmpty(s2)){
            return false;
        }
        return true;
    }

    public String getStructName1() {
        return structName1;
    }

    public void setStructName1(String structName1) {
        this.structName1 = structName1;
    }

    public String getStructAlias1() {
        return structAlias1;
    }

    public void setStructAlias1(String structAlias1) {
        this.structAlias1 = structAlias1;
    }

    public String getMetadataId1() {
        return metadataId1;
    }

    public void setMetadataId1(String metadataId1) {
        this.metadataId1 = metadataId1;
    }



    public String getParentId1() {
        return parentId1;
    }

    public void setParentId1(String parentId1) {
        this.parentId1 = parentId1;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getLength1() {
        return length1;
    }

    public void setLength1(String length1) {
        this.length1 = length1;
    }

    public String getRequired1() {
        return required1;
    }

    public void setRequired1(String required1) {
        this.required1 = required1;
    }

    public String getStructName2() {
        return structName2;
    }

    public void setStructName2(String structName2) {
        this.structName2 = structName2;
    }

    public String getStructAlias2() {
        return structAlias2;
    }

    public void setStructAlias2(String structAlias2) {
        this.structAlias2 = structAlias2;
    }

    public String getMetadataId2() {
        return metadataId2;
    }

    public void setMetadataId2(String metadataId2) {
        this.metadataId2 = metadataId2;
    }

    public String getSeq1() {
        return seq1;
    }

    public void setSeq1(String seq1) {
        this.seq1 = seq1;
    }

    public String getSeq2() {
        return seq2;
    }

    public void setSeq2(String seq2) {
        this.seq2 = seq2;
    }

    public String getParentId2() {
        return parentId2;
    }

    public void setParentId2(String parentId2) {
        this.parentId2 = parentId2;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getLength2() {
        return length2;
    }

    public void setLength2(String length2) {
        this.length2 = length2;
    }

    public String getRequired2() {
        return required2;
    }

    public void setRequired2(String required2) {
        this.required2 = required2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String id) {
        _parentId = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
