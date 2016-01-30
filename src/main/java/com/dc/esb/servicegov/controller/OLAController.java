package com.dc.esb.servicegov.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.spec.OAEPParameterSpec;
import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.entity.EnglishWord;
import com.dc.esb.servicegov.entity.OLA;
import com.dc.esb.servicegov.service.impl.OLAServiceImpl;

@Controller
@RequestMapping("/ola")
public class OLAController {
	@Autowired
	private SystemLogServiceImpl systemLogService;
	@Autowired
	private OLAServiceImpl olaServiceImpl;

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.POST, value = "/addList", headers = "Accept=application/json")
	public @ResponseBody
	boolean add(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("ola","批量添加","数量：" + list.size());
		for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            OLA ola=new OLA();
            ola.setOperationId(map.get("operationId"));
            ola.setServiceId(map.get("serviceId"));
            ola.setOlaName(map.get("olaName"));
            ola.setOlaValue(map.get("olaValue"));
            ola.setOlaDesc(map.get("olaDesc"));
            ola.setOlaRemark(map.get("olaRemark"));
            olaServiceImpl.save(ola);
   	 }
		systemLogService.updateResult(operationLog);
		return true;
	}

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.POST, value = "/add/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	   boolean save(@RequestBody List list,@PathVariable(value = "serviceId") String serviceId,
				@PathVariable(value = "operationId") String operationId) {
		OperationLog operationLog = systemLogService.record("ola","批量添加", "服务ID:" + serviceId + "; 场景ID:" +operationId +"; 数量：" + list.size());
   	 for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            OLA ola=new OLA();
            if(map.get("olaId")!=null&&!"".equals(map.get("olaId"))){
            	ola.setOlaId(map.get("olaId"));
            }
            ola.setOperationId(operationId);
            ola.setServiceId(serviceId);
            ola.setOlaName(map.get("olaName"));
            ola.setOlaValue(map.get("olaValue"));
            ola.setOlaDesc(map.get("olaDesc"));
            ola.setOlaRemark(map.get("olaRemark"));
            olaServiceImpl.save(ola);
   	 }
		systemLogService.updateResult(operationLog);
       return true;
   }

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
	public @ResponseBody
	boolean modify(@RequestBody OLA ola) {
		OperationLog operationLog = systemLogService.record("ola","修改","名称：" + ola.getOlaName());

		olaServiceImpl.save(ola);

		systemLogService.updateResult(operationLog);
		return true;
	}

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", headers = "Accept=application/json")
	public @ResponseBody
	boolean delete(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("ola","批量删除","数量：" + list.size());

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            String id = map.get("olaId");
            olaServiceImpl.deleteById(id);
        }

		systemLogService.updateResult(operationLog);
        return true;
 
	}

	@RequiresPermissions({"service-get"})
	@RequestMapping(method = RequestMethod.GET, value = "/getAll/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	List<OLA> getAll(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "operationId") String operationId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		List<OLA> ola = olaServiceImpl.findBy(params);
		return ola;

	}

	@RequiresPermissions({"service-update"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteAll/{serviceId}/{operationId}", headers = "Accept=application/json")
	public @ResponseBody
	boolean deleteBySOId(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "operationId") String operationId) {
		OperationLog operationLog = systemLogService.record("ola","关联删除","服务ID：" + serviceId + "; 场景ID:" + operationId);

		Map<String, String> params = new HashMap<String, String>();
		params.put("serviceId", serviceId);
		params.put("operationId", operationId);
		List<OLA> olas = olaServiceImpl.findBy(params);
		for (OLA ola : olas) {
			olaServiceImpl.delete(ola);
		}

		systemLogService.updateResult(operationLog);
		return true;
	}

	@RequiresPermissions({"service-get"})
	@RequestMapping("/olaPage")
	public ModelAndView olaPage(String operationId, String serviceId, HttpServletRequest req){
		return olaServiceImpl.olaPage(operationId, serviceId, req);
	}

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
