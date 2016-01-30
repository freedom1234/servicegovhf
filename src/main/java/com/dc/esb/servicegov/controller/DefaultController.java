package com.dc.esb.servicegov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Controller
@RequestMapping("/home")
public class DefaultController {
    @RequestMapping(method = RequestMethod.GET, value = "/", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView login() {
        return new ModelAndView("index");
    }
}