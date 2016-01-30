package com.dc.esb.servicegov.service.impl;

import java.net.URLDecoder;
import java.util.*;

import com.dc.esb.servicegov.dao.impl.InterfaceDAOImpl;
import com.dc.esb.servicegov.dao.impl.InterfaceInvokeDAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceInvokeDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.jsonObj.ServiceInvokeJson;
import com.dc.esb.servicegov.service.ServiceInvokeService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;

import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.vo.RelationVO;
import com.dc.esb.servicegov.vo.ServiceInvokeInfoVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class ServiceInvokeServiceImpl extends AbstractBaseService<ServiceInvoke, String> implements ServiceInvokeService{

	@Autowired
	private ServiceInvokeDAOImpl serviceInvokeDAOImpl;
	@Autowired
	private InterfaceInvokeDAOImpl interfaceInvokeDAO;
	@Autowired
	private InterfaceDAOImpl interfaceDAO;

	@Override
	public HibernateDAO<ServiceInvoke, String> getDAO() {
		return serviceInvokeDAOImpl;
	}
	@Override
	public void save(ServiceInvoke entity){
		serviceInvokeDAOImpl.save(entity);
	}
	public List<?> getBLInvoke(String baseId) {
        return serviceInvokeDAOImpl.getBLInvoke(baseId);
    }
	
	public ServiceInvoke getUniqueSI(String invokeId){
		return serviceInvokeDAOImpl.findUniqueBy("invokeId", invokeId);
	}

	public void updateBySO(String serviceId, String operationId){
		serviceInvokeDAOImpl.updateBySO(serviceId, operationId);
	}
	//场景新建或者修改后关联的方法
	public void updateAfterOPAdd(String invokeId, String serviceId, String operationId, String type){
		ServiceInvoke si = serviceInvokeDAOImpl.findUniqueBy("invokeId", invokeId);
		if(si != null && StringUtils.isEmpty(si.getServiceId()) && StringUtils.isEmpty(si.getOperationId())){
			  si.setServiceId(serviceId);
              si.setOperationId(operationId);
              si.setType(type); 
              serviceInvokeDAOImpl.save(si);
              return;
		}
		//新建条件：serviceId或者operationId为不为空而且serviceId、operationId、type至少有一个不同
		if(si == null || !StringUtils.isEmpty(si.getServiceId()) || !StringUtils.isEmpty(si.getOperationId())){
			if(si == null || !serviceId.equals(si.getServiceId())
					|| !operationId.equals(si.getOperationId())
					|| !type.equals(si.getType())){
				ServiceInvoke entity = new ServiceInvoke();
				entity.setServiceId(serviceId);
				entity.setOperationId(operationId);
				entity.setType(type);
				entity.setSystemId(si.getSystemId());
				entity.setInterfaceId(si.getInterfaceId());
				
				serviceInvokeDAOImpl.save(entity);
				return;
			}
		}
		
	}
	public void updateProtocolId(String hql,String ...args ){
		serviceInvokeDAOImpl.exeHql(hql,args);

	}
	public void deleteEntity(List<ServiceInvoke> serviceInvokeList){
		for (ServiceInvoke serviceInvoke : serviceInvokeList) {
			//先删除interfaceInvoke数据
			List<InterfaceInvoke> list = interfaceInvokeDAO.findBy("providerInvokeId", serviceInvoke.getInvokeId());
			list.addAll(interfaceInvokeDAO.findBy("consumerInvokeId", serviceInvoke.getInvokeId()));
			interfaceInvokeDAO.delete(list);
			//删serviceInvoke
			delete(serviceInvoke);
		}
	}
	public List<?> findJsonBySO(String serviceId, String operationId){
		return serviceInvokeDAOImpl.findJsonBySO(serviceId, operationId);
	}

	public List getInvokerRelationByServiceId(String serviceId) {
		String hql = " select new com.dc.esb.servicegov.vo.RelationVO" +
				"(a.system.systemId, b.system.systemAb, a.system.systemAb, a.serviceId, a.operationId, a.interfaceId)"+
				" from ServiceInvoke as a, ServiceInvoke as b " +
				" where a.type=? and b.type=? and a.serviceId=b.serviceId and a.serviceId=? and a.operationId=b.operationId";

		List<?> list = super.find(hql, Constants.INVOKE_TYPE_PROVIDER, Constants.INVOKE_TYPE_CONSUMER, serviceId);

		return list;

	}

	public List<ServiceInvokeJson> getDistinctInterBy(String hql,Page page){
		List<ServiceInvoke> list = this.findBy(hql, page);
		List<ServiceInvokeJson> voList = new ArrayList<ServiceInvokeJson>();

		for(int i = 0; i < list.size(); i++){
			ServiceInvoke si = list.get(i);

			if(si != null){
				for(int j = i+1; j < list.size(); j++){
					ServiceInvoke sj = list.get(j);
					if(sj != null){
						if(si.getSystemId().equals(sj.getSystemId()) ){
							if(StringUtils.isNotEmpty(si.getInterfaceId())&& StringUtils.isNotEmpty(sj.getInterfaceId())&& si.getInterfaceId().equals(sj.getInterfaceId())){
								if(i != j){
									list.set(j, null);
								}
							}
						}
						if(si.getInterfaceId() == null && sj.getInterfaceId() == null){
							list.set(j, null);
						}
					}
				}
			}

		}
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) != null){
				ServiceInvoke si = list.get(i);
				ServiceInvokeJson svo = new ServiceInvokeJson(list.get(i));
				//将标准接口放在第一位
				if(null == si.getIsStandard()){
					voList.add(svo);
					continue;
				}
//				if(si.getInterfaceId() == null){
				if(si.getIsStandard().equals("0")){
					svo.setRemark("标准接口");
				}
				voList.add(svo);
			}
		}
		Collections.sort(voList, new Comparator<ServiceInvokeJson>() {
			@Override
			public int compare(ServiceInvokeJson o1, ServiceInvokeJson o2) {
				return (""+o1.getIsStandard()).compareTo(""+o2.getIsStandard());
			}
		});
		return voList;
	}

	public List<ServiceInvokeJson> getDistinctInter(String systemId,String type, String text) throws  Throwable{
		String hql = " from " + ServiceInvoke.class.getName() +" as si where si.systemId='"+systemId+"' and type = '"+type	+"'";
		if(StringUtils.isNotEmpty(text)){
			text = URLDecoder.decode(text, "utf-8");
			hql += " and( si.interfaceId like '%" + text + "%' or si.inter.interfaceName like '%" + text + "%') ";
		}
		List<ServiceInvoke> list = this.find(hql);
		List<ServiceInvokeJson> voList = new ArrayList<ServiceInvokeJson>();

		for(int i = 0; i < list.size(); i++){
			ServiceInvoke si = list.get(i);

			if(si != null){
				for(int j = i+1; j < list.size(); j++){
					ServiceInvoke sj = list.get(j);
					if(sj != null){
						if(si.getSystemId().equals(sj.getSystemId()) ){
							if(StringUtils.isNotEmpty(si.getInterfaceId())&& StringUtils.isNotEmpty(sj.getInterfaceId())&& si.getInterfaceId().equals(sj.getInterfaceId())){
								if(i != j){
									list.set(j, null);
								}
							}
						}
						if(si.getInterfaceId() == null && sj.getInterfaceId() == null){
							list.set(j, null);
						}
					}
				}
			}

		}
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) != null){
				ServiceInvoke si = list.get(i);
				ServiceInvokeJson svo = new ServiceInvokeJson(list.get(i));
				//将标准接口放在第一位
				if(null == si.getIsStandard()) continue;
//				if(si.getInterfaceId() == null){
				if(si.getIsStandard().equals("0")){
					svo.setRemark("标准接口");
				}
				voList.add(svo);
			}
		}
		Collections.sort(voList, new Comparator<ServiceInvokeJson>() {
			@Override
			public int compare(ServiceInvokeJson o1, ServiceInvokeJson o2) {
				return (""+o1.getIsStandard()).compareTo(""+o2.getIsStandard());
			}
		});
		return voList;
	}

	public boolean deleteByOperationId(String OperationId,String serviceId){
		//TODO 还要加service_id
		Map map = new HashMap();
		map.put("operationId",OperationId);
		map.put("serviceId",serviceId);

		List<ServiceInvoke> list = serviceInvokeDAOImpl.findBy(map);

		for (ServiceInvoke invoke : list){
			//删除interface_invoke表。（调用关系）
			map = new HashMap();
			map.put("providerInvokeId",invoke.getInvokeId());
			List<InterfaceInvoke> list2 = interfaceInvokeDAO.findBy(map);
			map = new HashMap();
			map.put("consumerInvokeId",invoke.getInvokeId());
			List<InterfaceInvoke> list3 = interfaceInvokeDAO.findBy(map);
			if(list2.size()>0){
				interfaceInvokeDAO.delete(list2);
			}
			if(list3.size()>0){
				interfaceInvokeDAO.delete(list3);
			}
			serviceInvokeDAOImpl.delete(invoke);
		}
		return true;
//		String hql = "delete from ServiceInvoke t where t.operationId = '"+OperationId+"' and t.serviceId = '"+serviceId+"'";
//		return serviceInvokeDAOImpl.exeHql(hql);
	}
	public List<ServiceInvoke> getByOperationAndType(Operation operation, String type){
		return serviceInvokeDAOImpl.getByOperationAndType(operation,type);
	}

	public ServiceInvoke genderServiceInvoke(LinkedHashMap<String, Object> map){
		String systemId = map.get("systemId").toString();
		String interfaceId = map.get("interfaceId").toString();
		String type = map.get("type").toString();
		String isStandard = map.get("isStandard").toString();
		String serviceId = map.get("serviceId").toString();
		String operationId = map.get("operationId").toString();

		Map<String, String> params = new HashMap<String, String>();
		params.put("systemId", systemId);
		params.put("interfaceId", interfaceId);
		params.put("type", type);
		params.put("isStandard", isStandard);
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		ServiceInvoke c = serviceInvokeDAOImpl.findUniqureBy(params);
		if(null != c){
			return c;
		}
		c = new ServiceInvoke();
		c.setSystemId(systemId);
		if(StringUtils.isNotEmpty(interfaceId)){
			String hql = "from ServiceInvoke s where s.interfaceId = ? and s.systemId = ? and s.serviceId is null and s.operationId is null and s.protocolId is not null";
			ServiceInvoke serviceInvoke = serviceInvokeDAOImpl.findUnique(hql, interfaceId, systemId);
			if(null != serviceInvoke){
				c.setProtocolId(serviceInvoke.getProtocolId());
			}
			c.setInterfaceId(interfaceId);
		}
		c.setType(type);
		c.setIsStandard(isStandard);
		c.setOperationId(operationId);
		c.setServiceId(serviceId);
		serviceInvokeDAOImpl.save(c);
		return c;
	}

	public boolean containOperation(String interfaceId){
		String hql = "select count(*) from ServiceInvoke si where si.interfaceId = ? and si.operationId is not null and si.serviceId is not null";
		long count = serviceInvokeDAOImpl.findUnique(hql, interfaceId);
		if(count > 0){
			return true;
		}
		return false;
	}

	public List<ServiceInvoke> findByServiceOperationId(String serviceId,String OperationId){
		Map map = new HashMap();
		map.put("operationId",OperationId);
		map.put("serviceId",serviceId);
		List<ServiceInvoke> list = serviceInvokeDAOImpl.findBy(map);
		return list;
	}
}
