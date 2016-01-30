package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by vincentfxz on 15/7/24.
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView logout() {
        OperationLog operationLog = systemLogService.record("登出","注销","");
        SecurityUtils.getSubject().logout();
        ModelAndView mv = new ModelAndView("/login/login");
        systemLogService.updateResult(operationLog);
        return mv;
    }
}
