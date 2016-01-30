package com.dc.esb.servicegov.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.entity.BaseLine;

@Repository
public class BaseLineDAOImpl extends BaseDAOImpl<BaseLine>{
	 public List<BaseLine> getBaseLine(String code, String blDesc) {
	        String hql = " from "+ BaseLine.class.getName() +" where 1=1 ";
	        if (StringUtils.isNotEmpty(code)) {
	            hql += "and code like '%" + code + "%' ";
	        }
	        if (StringUtils.isNotEmpty(blDesc)) {
	            hql += "and blDesc like '%" + blDesc + "%' ";
	        }
	        List<BaseLine> list = find(hql);
	        return list;
	    }
}
