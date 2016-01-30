package com.dc.esb.servicegov.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.entity.SLA;
import com.dc.esb.servicegov.service.impl.SLAServiceImpl;

@Controller
@RequestMapping("/sla")
public class SLAController {
	@Autowired
	private SystemLogServiceImpl systemLogService;

	@Autowired
	private SLAServiceImpl slaServiceImpl;
	@Autowired
	private ServiceServiceImpl serviceService;
	@Autowired
	OperationServiceImpl operationService;

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.POST, value = "/addList", headers = "Accept=application/json")
	public @ResponseBody
	boolean add(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("SLA","添加","");
		String logParam = "元素：";
		for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            SLA sla=new SLA();
            sla.setOperationId(map.get("operationId"));
            sla.setServiceId(map.get("serviceId"));
            sla.setSlaName(map.get("slaName"));
            sla.setSlaValue(map.get("slaValue"));
            sla.setSlaDesc(map.get("slaDesc"));
            sla.setSlaRemark(map.get("slaRemark"));
            slaServiceImpl.save(sla);
			logParam += sla.getSlaName() + ",";
   		 }
		operationLog.setParams(logParam.substring(0, logParam.length() -2 ));
		systemLogService.updateResult(operationLog);
		return true;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sla-add"})
	@RequestMapping(method = RequestMethod.POST, value = "/add/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	   boolean save(@RequestBody List list,@PathVariable(value = "serviceId") String serviceId,
				@PathVariable(value = "operationId") String operationId) {
		OperationLog operationLog = systemLogService.record("SLA","服务场景SLA添加","服务ID:" + serviceId + "; 场景ID:" + operationId + "; SLA数量:" + list.size());
   	 for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            SLA sla=new SLA();
            if(map.get("slaId")!=null&&!"".equals(map.get("slaId"))){
            	sla.setSlaId(map.get("slaId"));
            }
            sla.setOperationId(operationId);
            sla.setServiceId(serviceId);
            sla.setSlaName(map.get("slaName"));
            sla.setSlaValue(map.get("slaValue"));
            sla.setSlaDesc(map.get("slaDesc"));
            sla.setSlaRemark(map.get("slaRemark"));
            slaServiceImpl.save(sla);
   	 }
		systemLogService.updateResult(operationLog);
       return true;
   }

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sla-update"})
	@RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
	public @ResponseBody
	boolean modify(@RequestBody SLA sla) {
		OperationLog operationLog = systemLogService.record("SLA","修改","名称：" + sla.getSlaName());

		slaServiceImpl.save(sla);

		systemLogService.updateResult(operationLog);
		return true;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sla-delete"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", headers = "Accept=application/json")
	public @ResponseBody
	boolean delete(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("SLA","删除元素","");
		String logParam = "";
		String serviceId = "";
		String operaionId = "";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            String id = map.get("slaId");
			SLA sla = slaServiceImpl.findUniqueBy("slaId", id);
			serviceId = sla.getServiceId();
			operaionId = sla.getOperationId();
            slaServiceImpl.deleteById(id);
        }

		logParam = "服务ID：" + serviceId + ", 场景ID:" + operaionId + ", 数量：" + list.size();
		operationLog.setParams(logParam);
		systemLogService.updateResult(operationLog);
        return true;
 
	}

//	@RequiresPermissions({"service-get"})
	@RequiresPermissions({"sla-get"})
	@RequestMapping(method = RequestMethod.GET, value = "/getAll/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	List<SLA> getAll(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "operationId") String operationId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		List<SLA> sla = slaServiceImpl.findBy(params);
		return sla;

	}

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteAll/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	boolean deleteBySOId(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "operationId") String operationId) {
		OperationLog operationLog = systemLogService.record("SLA","删除服务场景SLA元素","服务ID:" + serviceId + "; 场景ID:" + operationId);

		Map<String, String> params = new HashMap<String, String>();
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		List<SLA> slas = slaServiceImpl.findBy(params);
		for (SLA sla : slas) {
			slaServiceImpl.delete(sla);
		}

		systemLogService.updateResult(operationLog);
		return true;
	}

	@RequiresPermissions({"service-get"})
	@RequestMapping("/slaPage")
	public ModelAndView olaPage(String operationId, String serviceId, HttpServletRequest req){

		ModelAndView mv = new ModelAndView("service/sla/slaPage");
		// 根据serviceId获取service信息
		if (StringUtils.isNotEmpty(serviceId)) {
			com.dc.esb.servicegov.entity.Service service = serviceService.getById(serviceId);
			if (service != null) {
				mv.addObject("service", service);
			}
			if (StringUtils.isNotEmpty(operationId)) {
				// 根据serviceId,operationId获取operation信息
				Operation operation = operationService.getOperation(
						serviceId, operationId);
				if (operation != null) {
					mv.addObject("operation", operation);
				}
			}
		}
		return mv;
	}

	/**
	 * slaName唯一性验证
	 *
	 * @param slaName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
	public
	@ResponseBody
	boolean uniqueValid(String slaName) {
		return slaServiceImpl.uniqueValid(slaName);
	}
	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
