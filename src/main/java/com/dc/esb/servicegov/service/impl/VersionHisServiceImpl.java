package com.dc.esb.servicegov.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.impl.BaselineVersionHisMappingDAOImpl;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.BaseLineVersionHisMapping;
import com.dc.esb.servicegov.entity.OperationHis;
import com.dc.esb.servicegov.service.OperationHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.VersionHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.VersionHis;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
@Service
@Transactional
public class VersionHisServiceImpl  extends AbstractBaseService<VersionHis, String>{
	@Autowired
	VersionHisDAOImpl hisDaoImpl;
	@Autowired
	BaselineVersionHisMappingDAOImpl baselineVersionHisMappingDAO;
	@Autowired
	OperationHisService operationHisService;

	@Override
	public void save(VersionHis entity){
		hisDaoImpl.save(entity);
	}
	
	public List<VersionHis> hisVersionList(String keyValue){
		return hisDaoImpl.hisVersionList(keyValue);
	}
	
	public void updateVerionHis(String type, String[] versionHisIds){
		hisDaoImpl.updateVerionHis(type, versionHisIds);
	}

	public List<VersionHisBean> findVersionBeanBy(String hql,Page page){
		//TODO 关联(基线版本号根据BaseLineVersionHisMapping查找基线版本号 code）
		ArrayList<Object[]> list = (ArrayList)hisDaoImpl.findBy(hql,page);
		List<VersionHisBean> beanList = new ArrayList<VersionHisBean>();
		for(Object[] objects : list){
//			Map<String,String> map = new HashMap<String, String>();
//			map.put("versionHisId",vh.getAutoId());
			VersionHis vh = (VersionHis)objects[1];
			OperationHis operationHis = (OperationHis)objects[0];
			List<BaseLineVersionHisMapping> mapping = baselineVersionHisMappingDAO.findBy("versionHisId",vh.getAutoId());
			String baseLineNum = "";
			for (int i = 0; i < mapping.size(); i++) {
				if(i==0){
					baseLineNum = mapping.get(i).getBaseLine().getCode();
				}else{
					baseLineNum += "," + mapping.get(i).getBaseLine().getCode();
				}
			}
			//场景信息
			String operationHisId = vh.getTargetId();
//			OperationHis operationHis = operationHisService.getById(operationHisId);
			String serviceName = operationHis.getService().getServiceName();
			String serviceId = operationHis.getService().getServiceId();
			String operationName = operationHis.getOperationName();
			String operationId = operationHis.getOperationId();
			String operationDesc = operationHis.getOperationDesc();

			beanList.add(new VersionHisBean(vh, baseLineNum,serviceName,serviceId,operationName,operationId,operationDesc));
		}
		return beanList;
//		return hisDaoImpl.findBy(hql,page);
	}

	public static class VersionHisBean{
		private String autoId;

		private String id;

		private String code;

		// 0：操作版本，1：基线版本
		private String type;
		// 0:生效，1：废弃
		private String state;

		private String versionDesc;

		private String remark;
		//操作类型，0：新增，1：修改，2：删除
		private String optType;

		private String optUser;

		private String optDate;

		// 目标类型, 1:场景， 2：公共代码。。。
		private String targetType;

		private String targetId;

		private String baseLineNum;//基线版本

		private String serviceName;
		private String serviceId;
		private String operationName;
		private String operationId;
		private String operationDesc;

		public VersionHisBean(VersionHis vh,String baseLineNum,String serviceName,String serviceId,String operationName,String operationId,String operationDesc){
			setAutoId(vh.getAutoId());
			setId(vh.getId());
			setCode(vh.getCode());
			setType(vh.getType());
			setState(vh.getState());
			setVersionDesc(vh.getVersionDesc());
			setRemark(vh.getRemark());
			setOptType(vh.getOptType());
			setOptUser(vh.getOptUser());
			setOptDate(vh.getOptDate());
			setTargetType(vh.getTargetType());
			setTargetId(vh.getTargetId());
			setBaseLineNum(baseLineNum);
			setServiceName(serviceName);
			setServiceId(serviceId);
			setOperationName(operationName);
			setOperationId(operationId);
			setOperationDesc(operationDesc);
		}

		public String getAutoId() {
			return autoId;
		}

		public void setAutoId(String autoId) {
			this.autoId = autoId;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getVersionDesc() {
			return versionDesc;
		}

		public void setVersionDesc(String versionDesc) {
			this.versionDesc = versionDesc;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getOptType() {
			return optType;
		}

		public void setOptType(String optType) {
			this.optType = optType;
		}

		public String getOptUser() {
			return optUser;
		}

		public void setOptUser(String optUser) {
			this.optUser = optUser;
		}

		public String getOptDate() {
			return optDate;
		}

		public void setOptDate(String optDate) {
			this.optDate = optDate;
		}

		public String getTargetType() {
			return targetType;
		}

		public void setTargetType(String targetType) {
			this.targetType = targetType;
		}

		public String getTargetId() {
			return targetId;
		}

		public void setTargetId(String targetId) {
			this.targetId = targetId;
		}

		public String getBaseLineNum() {
			return baseLineNum;
		}

		public void setBaseLineNum(String baseLineNum) {
			this.baseLineNum = baseLineNum;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public String getOperationName() {
			return operationName;
		}

		public void setOperationName(String operationName) {
			this.operationName = operationName;
		}

		public String getOperationId() {
			return operationId;
		}

		public void setOperationId(String operationId) {
			this.operationId = operationId;
		}

		public String getOperationDesc() {
			return operationDesc;
		}

		public void setOperationDesc(String operationDesc) {
			this.operationDesc = operationDesc;
		}
	}


	@Override
	public HibernateDAO<VersionHis, String> getDAO() {
		return hisDaoImpl;
	}

	public VersionHis getPreVersion(String autoId){
		VersionHis  his = hisDaoImpl.findUniqueBy("autoId", autoId);
		String hql = " from " + VersionHis.class.getName() + " where code < ? and id = ?  order by code desc";
		List<VersionHis> list = hisDaoImpl.find(hql, his.getCode(), his.getId());
		if(list.size() > 0){
			return list.get(0);
		}
		return new VersionHis();
	}
}
