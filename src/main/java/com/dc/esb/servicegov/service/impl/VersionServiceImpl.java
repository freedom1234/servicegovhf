package com.dc.esb.servicegov.service.impl;

import java.util.*;

import com.dc.esb.servicegov.dao.IdaDAO;
import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;

import com.dc.esb.servicegov.vo.IdaCompareVO;
import com.dc.esb.servicegov.vo.OperationCompareVO;
import com.dc.esb.servicegov.vo.SDACompareVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.service.VersionService;
import com.dc.esb.servicegov.util.DateUtils;
@Service
@Transactional
public class VersionServiceImpl extends AbstractBaseService<Version, String> implements	VersionService {
	@Autowired
	VersionDAOImpl daoImpl;
	@Autowired
	VersionHisServiceImpl versionHisServiceImpl;
	@Autowired
	OperationDAOImpl operationDao;
	@Autowired
	OperationHisDAOImpl operationHisDao;
	@Autowired
	SDADAOImpl sdaDao;
	@Autowired
	SDAHisDAOImpl sdaHisDao;
	@Autowired
	InterfaceDAOImpl interfaceDAO;
	@Autowired
	InterfaceHISDAOImpl interfaceHISDAO;
	@Autowired
	IdaDAOImpl idaDAO;
	@Autowired
	IdaHISDAOImpl idaHISDAO;

	
	private final String initalVersion = "1.0.0";

	@Override
	public HibernateDAO<Version, String> getDAO() {
		return daoImpl;
	}

	public String addVersion(String targetType, String targetId, String type) {
		Version nv = new Version();
		nv.setId(UUID.randomUUID().toString());
		nv.setCode(initalVersion);
		nv.setType(type);
		nv.setState(Constants.Version.STATE_USING);
		nv.setOptType(Constants.Version.OPT_TYPE_ADD);
		nv.setOptDate(DateUtils.format(new Date()));
		//nv.setOptUser(optUser);
		nv.setTargetId(targetId);
		nv.setTargetType(targetType);
		daoImpl.save(nv);

		return nv.getId();
	}

	public boolean editVersion(String id) {
		if(StringUtils.isEmpty(id))
			return false;
		Version entity = daoImpl.findUniqueBy("id", id);
		if(entity != null){
			entity.setCode(editVersionCode(entity.getCode()));
			entity.setOptDate(DateUtils.format(new Date()));
			entity.setOptType(Constants.Version.OPT_TYPE_EDIT);
			entity.setOptUser(entity.getOptUser());
			daoImpl.save(entity);
		}
		return true;
	}

	public String editVersionCode(String versionCode) {
		if (StringUtils.isNotEmpty(versionCode)) {
			String[] s = versionCode.split("\\.");
			int s2 = Integer.parseInt(s[2]);
			s[2] = String.valueOf(s2 + 1);
			return s[0] + "." + s[1] + "." + s[2];
		}
		return initalVersion;
	}
	public String deleteVersion(String versionId, String targetId){
		Version ov = daoImpl.findUniqueBy("id", versionId);
		
		VersionHis nv = new VersionHis(ov, targetId);
		nv.setOptType(Constants.Version.OPT_TYPE_DELETE);
		versionHisServiceImpl.save(nv);
		
		return nv.getAutoId();
	}
	
	//发布时产生一个历史版本
	public String releaseVersion(String versionId, String targetId, String versionDesc) {
		Version ov = daoImpl.findUniqueBy("id", versionId);
		ov.setVersionDesc(versionDesc);
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		VersionHis nv = new VersionHis(ov, targetId);
		nv.setOptUser(userName);
		nv.setOptDate(DateUtils.format(new Date()));
		versionHisServiceImpl.save(nv);
		
		ov.setCode(releaseVersionCode(ov.getCode()));
		ov.setOptType(Constants.Version.OPT_TYPE_RELEASE);
		ov.setOptDate(DateUtils.format(new Date()));
		ov.setOptUser(userName);
		daoImpl.save(ov);
		
		return nv.getAutoId();
	}

	// 发布时调用方法，每次中间一位+1,最后一位置零
	public String releaseVersionCode(String versionCode) {//格式:1.0.0
		if (StringUtils.isNotEmpty(versionCode)) {
			String[] s = versionCode.split("\\.");
			int s1 = Integer.parseInt(s[1]);
			s[1] = String.valueOf(s1 + 1);
			s[2] = "0";
			return s[0] + "." + s[1] + "." + s[2];
		}
		return initalVersion;
	}

	/**
	 * 获取场景对比数据
	 */
	public Map<String, Object> getOperationDiff(String type,String versionId, String autoId1, String autoId2){
		Map<String, Object> result = new HashMap<String, Object>();
		OperationHis operationHis1 = null;
		OperationHis operationHis2 = null;
		List<SDAHis> sdaHisList1 = null;
		List<SDAHis> sdaHisList2 = null;
		if(Constants.Version.COMPARE_TYPE0.equals(type)){//当前版本与历史版本对比
			Operation operation = operationDao.findUniqueBy("versionId", versionId);
			operationHis1 =  new OperationHis(operation);
			String sdaHql = " from " + SDA.class.getName() + " where serviceId=? and operationId=?";
			List<SDA> sdaList = sdaDao.find(sdaHql, operation.getServiceId(), operation.getOperationId());
			sdaHisList1 = new ArrayList<SDAHis>();
			for(int i = 0; i < sdaList.size(); i++){
				sdaHisList1.add(new SDAHis(sdaList.get(i), operationHis1.getAutoId()));
			}
			VersionHis version2 = versionHisServiceImpl.findUniqueBy("autoId", autoId2);;
			if(version2 != null){
				operationHis2 = operationHisDao.findUniqueBy("versionHisId", version2.getAutoId());
				sdaHisList2 = sdaHisDao.findBy("operationHisId", operationHis2.getAutoId());
			}
		}
		else if(Constants.Version.COMPARE_TYPE1.equals(type)){//历史版本对比
			operationHis1 = operationHisDao.findUniqueBy("versionHisId", autoId1);
			sdaHisList1 = sdaHisDao.findBy("operationHisId", operationHis1.getAutoId());
			operationHis2 = operationHisDao.findUniqueBy("versionHisId", autoId2);
			sdaHisList2 = sdaHisDao.findBy("operationHisId", operationHis2.getAutoId());
		}
		else if(Constants.Version.COMPARE_TYPE2.equals(type)){//历史版本与当前版本对比
			VersionHis version1 = versionHisServiceImpl.findUniqueBy("autoId", autoId1);;
			if(version1 != null){
				operationHis1 = operationHisDao.findUniqueBy("versionHisId", version1.getAutoId());
				sdaHisList1 = sdaHisDao.findBy("operationHisId", operationHis1.getAutoId());
			}
			Operation operation = operationDao.findUniqueBy("versionId", versionId);
			operationHis2 =  new OperationHis(operation);
			String sdaHql = " from " + SDA.class.getName() + " where serviceId=? and operationId=?";
			List<SDA> sdaList = sdaDao.find(sdaHql, operation.getServiceId(), operation.getOperationId());
			sdaHisList2 = new ArrayList<SDAHis>();
			for(int i = 0; i < sdaList.size(); i++){
				sdaHisList2.add(new SDAHis(sdaList.get(i), operationHis1.getAutoId()));
			}
		}
		List<OperationCompareVO> operationHisList =  genderOperationData(operationHis1, operationHis2);
		List<SDACompareVO> sdaHisList = genderSDAData(sdaHisList1, sdaHisList2);
//		result.put( "operationHisList", operationHisList);
//		result.put("sdaHisList", sdaHisList);
		result.put("total", sdaHisList.size());
		result.put("rows", sdaHisList);
		return result;
	}
	//构造场景对比数据，以功能描述，备注，状态改变为标准
	public List<OperationCompareVO> genderOperationData(OperationHis oper1, OperationHis oper2){
		List<OperationCompareVO> list = new ArrayList<OperationCompareVO>();
		OperationCompareVO vo1 = null;
		OperationCompareVO vo2 = null;
		OperationCompareVO vo3 = null;
		OperationCompareVO vo4 = null;
		if(oper2 == null){
			vo1 = new OperationCompareVO("场景名称",oper1.getOperationName());
			vo2 = new OperationCompareVO("描述",oper1.getOperationDesc());
			vo3 = new OperationCompareVO("备注",oper1.getOperationRemark());
			vo4 = new OperationCompareVO("状态",oper1.getState());
		}else{
			vo1 = new OperationCompareVO("场景名称",oper1.getOperationName(),"场景名称", oper2.getOperationName());
			vo2 = new OperationCompareVO("描述", oper1.getOperationDesc(), "描述",oper2.getOperationDesc());
			vo3 = new OperationCompareVO("备注", oper1.getOperationRemark(), "备注", oper2.getOperationRemark());
			vo4 = new OperationCompareVO("状态", oper1.getState(), "状态", oper2.getState());
		}

		list.add(vo1);
		list.add(vo2);
		list.add(vo3);
		list.add(vo4);
		return list;
	}
	// 构建sda对比数据
	public List<SDACompareVO> genderSDAData(List<SDAHis> sdaHisList1, List<SDAHis> sdaHisList2){
		//匹配两个数组中相同id的sdaHIS
		List<SDACompareVO> list = new ArrayList<SDACompareVO>();
		if(sdaHisList2 != null){
			for(int i = 0; i < sdaHisList1.size(); i++){
				SDAHis s1 = sdaHisList1.get(i);
				if(s1 != null){
					for(int j = 0; j < sdaHisList2.size(); j ++){
						SDAHis s2 = sdaHisList2.get(j);
						if(s2 != null){
							if(s2.getSdaId().equals(s1.getSdaId())){
								SDACompareVO vo = new SDACompareVO(s1, s2);
								list.add(vo);
								sdaHisList1.set(i, null);
								sdaHisList2.set(j, null);
							}
						}

					}
				}
			}
			//处理未匹配数据
			for(int j = 0; j < sdaHisList2.size(); j++) {
				SDAHis s2 = sdaHisList2.get(j);
				if (s2 != null) {
					SDACompareVO vo = new SDACompareVO(null, s2);
					list.add(vo);
					sdaHisList2.set(j, null);
				}
			}
		}
		//处理未匹配数据
		for(int i = 0; i < sdaHisList1.size(); i++) {
			SDAHis s1 = sdaHisList1.get(i);
			if (s1 != null) {
				SDACompareVO vo = new SDACompareVO(s1, null);
				list.add(vo);
				sdaHisList1.set(i, null);
			}
		}

		return  list;
	}
	//拼接版本列表
	public List<VersionHis> getVersions(String versionId){
		Version version = daoImpl.findUniqueBy("id", versionId);
		VersionHis versionHis = new VersionHis(version, null);
		versionHis.setAutoId("");
		List<VersionHis> hisList = new ArrayList<VersionHis>();
		hisList.add(versionHis);
		String hql = " from " + VersionHis.class.getName() + " where id=? order by code desc";
		List<VersionHis> hisList2 = versionHisServiceImpl.find(hql, versionId);
		hisList.addAll(hisList2);
		return hisList;
	}

	/**
	 * 获取接口对比数据
	 */
	public Map<String, Object> getInterfaceDiff(String type,String versionId, String autoId1, String autoId2){
		Map<String, Object> result = new HashMap<String, Object>();
		InterfaceHIS interHis1 = null;
		InterfaceHIS interHis2 = null;
		List<IdaHIS> idaHisList1 = null;
		List<IdaHIS> idaHisList2 = null;
		if(Constants.Version.COMPARE_TYPE0.equals(type)){//当前版本与历史版本对比
			Interface inter = interfaceDAO.findUniqueBy("versionId", versionId);
			interHis1 =  new InterfaceHIS(inter);
			String idaHql = " from " + Ida.class.getName() + " where interfaceId = ?";
			List<Ida> idaList = idaDAO.find(idaHql, inter.getInterfaceId());
			idaHisList1 = new ArrayList<IdaHIS>();
			for(int i = 0; i < idaList.size(); i++){
				idaHisList1.add(new IdaHIS(idaList.get(i),interHis1.getAutoId()));
			}
			VersionHis version2 = versionHisServiceImpl.findUniqueBy("autoId", autoId2);;
			if(version2 != null){
				interHis2 = interfaceHISDAO.findUniqueBy("versionHisId", version2.getAutoId());
				idaHisList2 = idaHISDAO.findBy("interfaceHisId", interHis2.getAutoId());
			}
		}
		else if(Constants.Version.COMPARE_TYPE1.equals(type)){//历史版本对比
			interHis1 = interfaceHISDAO.findUniqueBy("versionHisId", autoId1);
			idaHisList1 = idaHISDAO.findBy("interfaceHisId", interHis1.getAutoId());
			interHis2 = interfaceHISDAO.findUniqueBy("versionHisId", autoId2);
			idaHisList2 = idaHISDAO.findBy("interfaceHisId", interHis2.getAutoId());
		}
		else if(Constants.Version.COMPARE_TYPE2.equals(type)){//历史版本与当前版本对比
			VersionHis version1 = versionHisServiceImpl.findUniqueBy("autoId", autoId1);;
			if(version1 != null){
				interHis1 = interfaceHISDAO.findUniqueBy("versionHisId", version1.getAutoId());
				idaHisList1 = idaHISDAO.findBy("interfaceHisId", interHis1.getAutoId());
			}
			Interface inter = interfaceDAO.findUniqueBy("versionId", versionId);
			interHis2 =  new InterfaceHIS(inter);
			String idaHql = " from " + Ida.class.getName() + " where interfaceId=?";
			List<Ida> idaList = idaDAO.find(idaHql, inter.getInterfaceId());
			idaHisList2 = new ArrayList<IdaHIS>();
			for(int i = 0; i < idaList.size(); i++){
				idaHisList2.add(new IdaHIS(idaList.get(i), interHis1.getAutoId()));
			}
		}
		List<IdaCompareVO> idaHisList = genderIdaData(idaHisList1, idaHisList2);
//		result.put( "operationHisList", operationHisList);
//		result.put("sdaHisList", sdaHisList);
		result.put("total", idaHisList.size());
		result.put("rows", idaHisList);
		return result;
	}

	// 构建sda对比数据
	public List<IdaCompareVO> genderIdaData(List<IdaHIS> hisList1, List<IdaHIS> hisList2){
		//匹配两个数组中相同id的sdaHIS
		List<IdaCompareVO> list = new ArrayList<IdaCompareVO>();
		if(hisList1 != null){
			for(int i = 0; i < hisList1.size(); i++){
				IdaHIS s1 = hisList1.get(i);
				if(s1 != null){
					for(int j = 0; j < hisList2.size(); j ++){
						IdaHIS s2 = hisList2.get(j);
						if(s2 != null){
							if(s2.getId().equals(s1.getId())){
								IdaCompareVO vo = new IdaCompareVO(s1, s2);
								list.add(vo);
								hisList1.set(i, null);
								hisList2.set(j, null);
							}
						}

					}
				}
			}
			//处理未匹配数据
			for(int j = 0; j < hisList2.size(); j++) {
				IdaHIS s2 = hisList2.get(j);
				if (s2 != null) {
					IdaCompareVO vo = new IdaCompareVO(null, s2);
					list.add(vo);
					hisList2.set(j, null);
				}
			}
		}
		//处理未匹配数据
		for(int i = 0; i < hisList1.size(); i++) {
			IdaHIS s1 = hisList1.get(i);
			if (s1 != null) {
				IdaCompareVO vo = new IdaCompareVO(s1, null);
				list.add(vo);
				hisList1.set(i, null);
			}
		}

		return  list;
	}
}