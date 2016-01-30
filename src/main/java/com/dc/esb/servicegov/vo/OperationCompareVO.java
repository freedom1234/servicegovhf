package com.dc.esb.servicegov.vo;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2015/9/21.
 */
public class OperationCompareVO {
    String field1;
    String value1;
    String field2;
    String value2;
    String color ="";
    public OperationCompareVO(String str1, String value1){
        this.field1 = str1;
        this.value1 = value1;
    }
    public OperationCompareVO(String str1,String value1, String str2, String value2){
        this.field1 = str1;
        this.field1 = str2;
        if(StringUtils.isNotEmpty(value1) && StringUtils.isEmpty(value2)){
            this.color="yellow";
        }
        if(StringUtils.isEmpty(value1) && StringUtils.isNotEmpty(value2)){
            this.color="yellow";
        }
        if(StringUtils.isNotEmpty(value1) && StringUtils.isNotEmpty(value2)){
            if(str1.equals(str2)){
                this.color="yellow";
            }
        }
    }
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }



    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
