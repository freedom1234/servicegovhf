package com.dc.esb.servicegov.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.entity.BaseLineVersionHisMapping;
import com.dc.esb.servicegov.entity.OperationHis;

@Repository
public class OperationHisDAOImpl extends BaseDAOImpl<OperationHis> {
	public List<OperationHis> getBLOperationHiss(String baseId) {
		String hql = " from " + OperationHis.class.getName() +" oh where oh.versionHis.autoId in (select bvhm.versionHisId from " + BaseLineVersionHisMapping.class.getName() + " bvhm where bvhm.baseLineId=?)";
		List<OperationHis> list = find(hql, baseId);
		return list;
	}
}
