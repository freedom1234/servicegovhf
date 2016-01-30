package com.dc.esb.servicegov.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dc.esb.servicegov.entity.OLA;
import com.dc.esb.servicegov.entity.OLATemplate;
import com.dc.esb.servicegov.service.impl.OLAServiceImpl;
import com.dc.esb.servicegov.service.impl.OLATemplateServiceImpl;


@Controller
@RequestMapping("/olaTemplate")
public class OLATemplateController {
	@Autowired
	private SystemLogServiceImpl systemLogService;
	@Autowired
	private OLATemplateServiceImpl olaTemplateServiceImpl;
	@Autowired
	private OLAServiceImpl olaServiceImpl;

	@RequestMapping(method = RequestMethod.GET, value = "/getOLA/{templateId}", headers = "Accept=application/json")
	public @ResponseBody
	List<OLA> getOLA(@PathVariable(value = "templateId") String templateId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("olaTemplateId", templateId);
		return olaServiceImpl.getTemplateOLA(params);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
	public @ResponseBody
	List<OLATemplate> getAll() {
		return olaTemplateServiceImpl.getAll();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
	public @ResponseBody
	   boolean save(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("ola模板","保存","ola数量：" + list.size());

  	 for (int i = 0; i < list.size(); i++) {
           LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
           Set<String> keySet = map.keySet();
           OLATemplate olaTemplate=new OLATemplate();
           if(map.get("olaTemplateId")!=null&&!"".equals(map.get("olaTemplateId"))){
        	   olaTemplate.setOlaTemplateId(map.get("olaTemplateId"));
           }
           olaTemplate.setTemplateName(map.get("templateName"));
           olaTemplate.setDesc(map.get("desc"));
           olaTemplateServiceImpl.save(olaTemplate);
  	 }
		systemLogService.updateResult(operationLog);
      return true;
  }


	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", headers = "Accept=application/json")
	public @ResponseBody
	boolean delete(@RequestBody List list) {
		OperationLog operationLog = systemLogService.record("ola模板","删除","ola数量：" + list.size());

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            String id = map.get("olaTemplateId");
            olaTemplateServiceImpl.deleteById(id);
        }

		systemLogService.updateResult(operationLog);
        return true;
 
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addOla/{serviceId}/{operationId}/{olaTemplateId}", headers = "Accept=application/json")
	public @ResponseBody
	   boolean saveOla(@RequestBody List list,@PathVariable(value = "serviceId") String serviceId,
				@PathVariable(value = "operationId") String operationId,@PathVariable(value = "olaTemplateId") String olaTemplateId) {
		OperationLog operationLog = systemLogService.record("ola","服务ID:"+ serviceId + "; 场景ID：" + operationId + "; 按模板保存","模板ID:" + olaTemplateId);
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
            ola.setOlaTemplateId(olaTemplateId);
            olaServiceImpl.save(ola);
   	 }
		systemLogService.updateResult(operationLog);
       return true;
   }

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
   }
}
