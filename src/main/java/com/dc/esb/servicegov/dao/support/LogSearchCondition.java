/*
 * 文件名： LogSearchCondition.java
 * 
 * 创建日期： 2006-9-27
 *
 * Copyright(C) 2006, by Along.
 *
 * 原始作者: <a href="mailto:HL_Qu@hotmail.com">Along</a>
 *
 */
package com.dc.esb.servicegov.dao.support;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.sql.Date;

/**
 * 日志查询条件对应的Java类
 *
 * @author <a href="mailto:HL_Qu@hotmail.com">Along</a>
 * @version $Revision: 1.1 $
 * @since 2006-9-27
 */
public class LogSearchCondition extends SearchCondition {

    /**
     * 记录的开始时间
     */
    private Date logDateBegin;

    /**
     * 记录的结束时间
     */
    private Date logDateEnd;

    /**
     * 比较运算：大于、等于、小于
     */
    private String searchCompare;

    /**
     * 自应用启动时间后的毫秒数
     */
    private Long searchElapsedTime;

    /**
     * 获得日志记录的开始时间
     *
     * @return 返回日志记录的开始时间n。
     */
    public Date getLogDateBegin() {
        return logDateBegin;
    }

    /**
     * 设置日志记录的开始时间
     *
     * @param logDateBegin 要设置的日志记录的开始时间。
     */
    public void setLogDateBegin(Date logDateBegin) {
        this.logDateBegin = logDateBegin;
    }

    /**
     * 获得日志记录的结束时间
     *
     * @return 返回日志记录的结束时间d。
     */
    public Date getLogDateEnd() {
        return logDateEnd;
    }

    /**
     * 设置日志记录的结束时间
     *
     * @param logDateEnd 要设置的日志记录的结束时间。
     */
    public void setLogDateEnd(Date logDateEnd) {
        this.logDateEnd = logDateEnd;
    }

    /**
     * 获得比较运算操作符
     *
     * @return 返回比较运算操作符。
     */
    public String getSearchCompare() {
        return searchCompare;
    }

    /**
     * 设置比较运算操作符
     *
     * @param searchCompare 要设置的比较运算操作符。
     */
    public void setSearchCompare(String searchCompare) {
        this.searchCompare = searchCompare;
    }

    /**
     * 获得自应用启动时间后的毫秒数
     *
     * @return 返回自应用启动时间后的毫秒数。
     */
    public Long getSearchElapsedTime() {
        return searchElapsedTime;
    }

    /**
     * 设置自应用启动时间后的毫秒数
     *
     * @param searchElapsedTime 要设置的自应用启动时间后的毫秒数。
     */
    public void setSearchElapsedTime(Long searchElapsedTime) {
        this.searchElapsedTime = searchElapsedTime;
    }

    public String toString() {

        ToStringBuilder strBuilder = new ToStringBuilder(this);
        strBuilder.append(super.toString());

        strBuilder.append("logDateBegin", this.getLogDateBegin());
        strBuilder.append("logDateEnd", this.getLogDateEnd());
        strBuilder.append("searchCompare", this.getSearchCompare());
        strBuilder.append("searchElapsedTime", this.getSearchElapsedTime());

        return strBuilder.toString();
    }
}
