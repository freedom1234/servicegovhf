package com.dc.esb.servicegov.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.VersionHis;
@Repository
public class VersionHisDAOImpl  extends HibernateDAO<VersionHis, String>{
	public List<VersionHis> hisVersionList(String keyValue){
		String hql = " from VersionHis";
		if(StringUtils.isNotEmpty(keyValue)){
			hql += " where code like '%"+keyValue+"%' or versionDesc like '%"+keyValue+"%' or remark like '%"+keyValue+"%'";
		}
		return find(hql);
	}
	
	public void updateVerionHis(String type, String[] versionHisIds){
		  //更新版本
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("versionHisIds", versionHisIds);
        batchExecute(" update VersionHis set type=(:type) where autoId in(:versionHisIds)", params);
        
	}
}
