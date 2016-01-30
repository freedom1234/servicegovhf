package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.service.impl.EnglishWordServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.impl.UserServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;


/**
 * Created by vincentfxz on 15/7/2.
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    SystemLogServiceImpl systemLogService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private EnglishWordServiceImpl englishWordService;


    private static final Log log = LogFactory.getLog(LoginController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView login(boolean topFlag) {
        ModelAndView mv = new ModelAndView("/login/login");
        if(!topFlag){
            topFlag = true;
            mv.addObject("topFlag", topFlag);
        }
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public
    @ResponseBody
    ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password ) {

        try {
            username = new String(username.getBytes("iso-8859-1"), "utf-8");
             password = new String(password.getBytes("iso-8859-1"), "utf-8");
            log.info("userName: " + username + "; password: " + password);

            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (UnsupportedEncodingException e) {
            log.error(e, e );
            log.error("用户名或密码编码错误！");
            ModelAndView mv = new ModelAndView("/login/login");
            mv.addObject("errMsg", "输入编码错误！");
            systemLogService.addLoginLog(username,"登入", "失败");
            return mv;
        }catch(Exception e){
            log.error(e, e );
            log.error("用户名或密码错误！");
            ModelAndView mv = new ModelAndView("/login/login");
            mv.addObject("errMsg", "用户名或密码错误！");
            systemLogService.addLoginLog(username, "登入", "失败");
            return mv;
        }
        systemLogService.addLoginLog(username,"登入", "成功");
        return new ModelAndView("index");
    }

    @ExceptionHandler({IncorrectCredentialsException.class})
    public String processUnauthorizedException() {
        return "/login/login";
    }
}
