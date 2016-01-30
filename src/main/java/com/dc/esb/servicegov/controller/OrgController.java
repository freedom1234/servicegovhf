package com.dc.esb.servicegov.controller;

import java.util.List;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.dc.esb.servicegov.entity.Organization;
import com.dc.esb.servicegov.service.impl.OrgServiceImpl;

@Controller
@RequestMapping("/org")
public class OrgController {
	@Autowired
	private SystemLogServiceImpl systemLogService;
	@Autowired
	private OrgServiceImpl orgServiceImpl;
	@RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
	public @ResponseBody
	boolean add(@RequestBody Organization org) {
		OperationLog operationLog = systemLogService.record("组织","添加","组织名称：" + org.getOrgName());
		orgServiceImpl.save(org);
		systemLogService.updateResult(operationLog);
		return true;
	}
	
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Organization> getAll() {
        List<Organization> org = orgServiceImpl.getAll();
        return org;
    }
    
	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
