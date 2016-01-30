package com.dc.esb.servicegov.controller;

import java.util.List;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dc.esb.servicegov.service.impl.SDAHisServiceImpl;
import com.dc.esb.servicegov.util.TreeNode;

@Controller
@RequestMapping("sdaHis")
public class SDAHisController {
	@Autowired
	SDAHisServiceImpl serviceImpl;
	
	//根据serviceId，operationId获取sda树
	@RequestMapping("/sdaHisTree")
	@ResponseBody
	public List<TreeNode> getSDATree(String autoId){
		return serviceImpl.genderSDATree(autoId);
	}

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
