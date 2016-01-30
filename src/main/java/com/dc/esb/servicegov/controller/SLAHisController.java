package com.dc.esb.servicegov.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.entity.SLAHis;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dc.esb.servicegov.entity.OLAHis;
import com.dc.esb.servicegov.service.impl.SLAHisServiceImpl;

@Controller
@RequestMapping("slaHis")
public class SLAHisController {
	@Autowired
	SLAHisServiceImpl slaHisService;
	

	//根据serviceId，operationId获取sda树
	@RequestMapping("/getByOperation")
	@ResponseBody
	public  Map<String, Object>  getByOperation(String autoId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("autoId", autoId);
		List<SLAHis> rows = slaHisService.findBy(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rows.size());
		result.put("rows", rows);
		return result;
		
	}

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
