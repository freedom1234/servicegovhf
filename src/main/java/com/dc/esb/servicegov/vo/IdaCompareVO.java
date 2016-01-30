package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.IdaHIS;
import com.dc.esb.servicegov.entity.SDAHis;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2015/10/21.
 */
public class IdaCompareVO {
    String id;
    String text;
    String _parentId;
    
    String structName1;
    String structAlias1;
    String metadataId1;
    String seq1;
    String  remark1;
    String type1;
    String scale1;
    String   length1;
    String  required1;

    String structName2;
    String structAlias2;
    String metadataId2;
    String seq2;
    String  remark2;
    String type2;
    String scale2;
    String   length2;
    String  required2;

    String color="";

    public IdaCompareVO(IdaHIS his1, IdaHIS his2){
        if(his1 != null){
            this.id = his1.getId();
            this.text = his1.getStructName();
            this._parentId = his1.get_parentId();

            structName1 = his1.getStructName();
            structAlias1 = his1.getStructAlias();
            metadataId1 = his1.getMetadataId();
            seq1 = String.valueOf(his1.getSeq());
            remark1 = his1.getRemark();
            type1 = his1.getType();
            length1 = his1.getLength();
            scale1 = his1.getScale();
            required1 = his1.getRequired();
        }
        if(his2 != null){
            structName2 = his2.getStructName();
            structAlias2= his2.getStructAlias();
            metadataId2 = his2.getMetadataId();
            seq2 =  String.valueOf(his2.getSeq());
            remark2 = his2.getRemark();
            type2 = his2.getType();
            length2 = his2.getLength();
            scale2 = his2.getScale();
            required2 = his2.getRequired();
        }
        if(his1 != null && his2 == null){//添加操作
            this.color = "green";
        }
        if(his1 != null && his2 != null){//修改操作
            if(compareStr(his1.getStructName(), his2.getStructName())
                    &&compareStr(his1.getStructAlias(), his2.getStructAlias())
                    &&compareStr(his1.getMetadataId(), his2.getMetadataId())
                    &&his1.getSeq() == his2.getSeq()
                    &&compareStr(his1.getRemark(), his2.getRemark())
                    &&compareStr(his1.getType(), his2.getType())
                    &&compareStr(his1.getLength(), his2.getLength())
                    &&compareStr(his1.getScale(), his2.getScale())
                    &&compareStr(his1.getRequired(), his2.getRequired())){

            }else{
                this.color = "yellow";
            }
        }
        if(his1 == null && his2 != null){//删除操作
            this.id = his2.getId();
            this.text = "";
            this._parentId = his2.get_parentId();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String _parentId) {
        this._parentId = _parentId;
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

    public String getSeq1() {
        return seq1;
    }

    public void setSeq1(String seq1) {
        this.seq1 = seq1;
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

    public String getScale1() {
        return scale1;
    }

    public void setScale1(String scale1) {
        this.scale1 = scale1;
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

    public String getSeq2() {
        return seq2;
    }

    public void setSeq2(String seq2) {
        this.seq2 = seq2;
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

    public String getScale2() {
        return scale2;
    }

    public void setScale2(String scale2) {
        this.scale2 = scale2;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
