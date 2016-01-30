package com.dc.esb.servicegov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dc.esb.servicegov.service.impl.UserRoleRelationServiceImpl;

@Controller
@RequestMapping("/relation")
public class UserRoleRelationController {
	@Autowired
	private UserRoleRelationServiceImpl userRoleRelationServiceImpl;
}
