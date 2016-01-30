package com.dc.esb.servicegov.service;

import java.util.HashMap;
import java.util.List;

import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.EnumElementMap;
import com.dc.esb.servicegov.entity.EnumElements;
import com.dc.esb.servicegov.entity.MasterSlaveEnumMap;
import com.dc.esb.servicegov.entity.SGEnum;


public interface EnumService {
	boolean insertEnum(SGEnum anEnum);
	HashMap<String, Object> getAllEnum(String hql,List<SearchCondition> searchConds,int pageNum,int rows);
	SGEnum getByEnumId(String id);
	List<SGEnum> getSlaveByEnumId(String id);
	SGEnum getMasterBySlaveId(String id);
	List<EnumElements> getElementsByEnumId(String id);
	HashMap<String, Object>  getElementByMasterId(String hql,int pageNum,int rows);
	boolean deleteEnumById(String id);
	boolean deleteEnumElementsByIds(List<String> ids);
	boolean updateEnum(SGEnum anEnum);
	List<SGEnum> getEnumByParams(HashMap<String, String> map);
	boolean addElement(EnumElements elements);
	List getElementsMapping(StringBuffer hql);
	HashMap<String, Object> getElementsMapping(String hql,int pageNum,int rows);
	boolean addMasterSlaveEnumMap(MasterSlaveEnumMap enumMap);
	List<EnumElements> getElementsByHql(String hql);
	boolean addEnumElementMap(EnumElementMap elementMap);
	boolean deleteElementsMapping(List<EnumElementMap> mappingList);
	boolean deleteElementsMappingByPK(String masterId,String slaveId);
}
