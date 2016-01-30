package com.dc.esb.servicegov.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.VersionHis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.dao.impl.OperationHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.OperationHis;
import com.dc.esb.servicegov.service.OperationHisService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;

/**
 * TODO 没有dao
 */
@Service
@Transactional
public class OperationHisServiceImpl extends
		AbstractBaseService<OperationHis, String> implements
		OperationHisService {
	@Autowired
	private OperationHisDAOImpl operationHisDAOImpl;
	@Autowired
	private OperationServiceImpl operationService;
	@Autowired
	private ServiceServiceImpl serviceService;

	public ModelAndView hisPage(HttpServletRequest req, String operationId,
			String serviceId) {
		if (StringUtils.isNotEmpty(serviceId)) {
			try {
				serviceId = URLDecoder.decode(serviceId, "utf-8");
				if (StringUtils.isNotEmpty(operationId)) {
					operationId = URLDecoder.decode(operationId, "utf-8");
				}
				ModelAndView mv = new ModelAndView(
						"service/operationHis/hisPage");
				// 根据serviceId查询service信息
				com.dc.esb.servicegov.entity.Service service = serviceService
						.getById(serviceId);
				if (service != null) {
					mv.addObject("service", service);
				}
				// 根据operationId查询operation
				Operation operation = operationService.getOperation(serviceId,
						operationId);
				if (operation != null) {
					mv.addObject("operation", operation);
				}
				return mv;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	public OperationHis getByAutoId(String autoId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("autoId", autoId);
		OperationHis entity = operationHisDAOImpl.findUniqureBy(params);
		return entity;

	}

	// 根据operationId获取operation
	public List<OperationHis> getByOS(String operationId, String serviceId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("operationId", operationId);
		params.put("serviceId", serviceId);
		List<OperationHis> list = operationHisDAOImpl.findBy(params);
		return list;
	}

	@Override
	public HibernateDAO<OperationHis, String> getDAO() {
		return operationHisDAOImpl;
	}

	@Override
	public void save(OperationHis operationHis) {
		operationHisDAOImpl.save(operationHis);
	}

	// 查询已发布的场景列表
	public List<?> operationHisList() {
		List<?> list = operationHisDAOImpl.findBy("state",
				Constants.Operation.OPT_STATE_PASS);
		return list;
	}

	public List<?> getBLOperationHiss(String baseId) {
		return operationHisDAOImpl.getBLOperationHiss(baseId);
	}

	public List findBy(String hql,Page page){
		return operationHisDAOImpl.findBy(hql, page);
	}

	public boolean hisJudge(String serviceId, String operationId){
		List<OperationHis> list = getByOS(operationId, serviceId);//不用count函数，因为不同数据库返回的数据类型不同
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}
}
