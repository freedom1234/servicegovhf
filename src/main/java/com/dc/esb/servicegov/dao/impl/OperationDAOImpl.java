package com.dc.esb.servicegov.dao.impl;

import java.util.*;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.OperationPK;
import com.dc.esb.servicegov.entity.ServiceCategory;
import com.dc.esb.servicegov.entity.Version;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Repository;

import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.service.support.Constants;
import org.springframework.util.Assert;

@Repository
public class OperationDAOImpl extends HibernateDAO<Operation, OperationPK> {
	public Operation getBySO(String serviceId, String operationId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		Operation operation = this.findUniqureBy(params);
		return operation;
	}

	public List<Operation> getReleased(Page page,String serviceId,String serviceName,String operationId,String operationName) {
		String hql = "select a from Operation a where a.state=? and a.version.optType !=? and a.service.serviceId like ? and a.service.serviceName like ?";
		hql += " and a.operationId like ? and a.operationName like ? order by a.optDate desc";
		List<Operation> list = findBy(hql.toString(), page,Constants.Operation.OPT_STATE_PASS, Constants.Version.OPT_TYPE_RELEASE,"%"+serviceId+"%","%"+serviceName+"%","%"+operationId+"%","%"+operationName+"%");
		return list;
	}

	@Override
	public void save(Operation entity){
		entity.setOptDate(DateUtils.format(new Date()));
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		entity.setOptUser(userName);
		super.save(entity);
	}
	public long getByMetadataIdCount(String metadataId){
		String hql = "select count(*) from Operation as o, SDA s where o.operationId = s.operationId and o.serviceId = s.serviceId and s.metadataId = ? ";
		Long count = this.findUnique(hql, metadataId);
		return count;
	}
	public List<Operation> getByMetadataId(String metadataId){
		String hql = "select o from Operation as o, SDA s where o.operationId = s.operationId and o.serviceId = s.serviceId and s.metadataId = ? ";
		List<Operation> list = this.find(hql, metadataId);
		return list;
	}

	public List<Operation> getByCategoryId(String categoryId){
		String hql = " from " + Operation.class.getName() + " op where op.service.categoryId = ?)";
		List<Operation> opList = this.find(hql, categoryId);
		return opList;
	}

//	@Override
//	public void delete(OperationPK id) {
//		Assert.notNull(id, "id不能为空");
//		String hql = " update "+ Operation.class.getName() + " set state=? , deleted=?" ;
//		super.batchExecute(hql,Constants.Operation.OPT_STATE_UNAUDIT, Constants.DELTED_TRUE);
//	}
}
