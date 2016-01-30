/*
 * 文件名： SearchCondition.java
 * 
 * 创建日期： 2009-10-13
 *
 * Copyright(C) 2009, by Along.
 *
 * 原始作者: <a href="mailto:HL_Qu@hotmail.com">Along</a>
 *
 */
package com.dc.esb.servicegov.dao.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 查询条件的基类
 *
 * @author <a href="mailto:HL_Qu@hotmail.com">Along</a>
 * @version $Revision: 1.1 $
 * @since 2009-10-13
 */
public class SearchCondition {

    /**
     * 查询的字段
     */
    protected String field;

    /**
     * 查询字段对应的值
     */
    protected Object fieldValue;

    public SearchCondition() {
        super();
    }

    public SearchCondition(String field, Object fieldValue) {
        super();
        this.field = field;
        this.fieldValue = fieldValue;
    }

    /**
     * 获得要查询的字段名
     *
     * @return 返回要查询的字段名。
     */
    public String getField() {
        return field;
    }

    /**
     * 设置要查询的字段名
     *
     * @param field 要设置的查询的字段名。
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * 获得要查询的字段值
     *
     * @return 返回要查询的字段值。
     */
    public Object getFieldValue() {
        return fieldValue;
    }

    /**
     * 设置要查询的字段值
     *
     * @param fieldValue 要设置的查询的字段值。
     */
    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String toString() {
        ToStringBuilder strBuilder = new ToStringBuilder(this);
        strBuilder.append(super.toString());

        strBuilder.append("field", this.getField());
        strBuilder.append("fieldValue", this.getFieldValue());

        return strBuilder.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof SearchCondition))
            return false;

        EqualsBuilder eq = new EqualsBuilder();
        SearchCondition that = (SearchCondition) obj;

        return eq.append(this.getField(), that.getField()).append(
                this.getFieldValue(), that.getFieldValue()).isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37);
        hashCodeBuilder.append(this.getField()).append(this.getFieldValue());

        return hashCodeBuilder.toHashCode();
    }

}

