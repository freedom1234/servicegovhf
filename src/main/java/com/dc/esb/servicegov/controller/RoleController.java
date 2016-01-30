package com.dc.esb.servicegov.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dc.esb.servicegov.entity.Role;
import com.dc.esb.servicegov.entity.UserRoleRelation;
import com.dc.esb.servicegov.service.impl.RoleServiceImpl;
import com.dc.esb.servicegov.service.impl.UserRoleRelationServiceImpl;
import com.dc.esb.servicegov.vo.RoleVO;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

	@Autowired
	private RoleServiceImpl roleServiceImpl;
    @Autowired
    private UserRoleRelationServiceImpl userRoleRelationService;
    @RequiresRoles({"admin"})
	@RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
	public @ResponseBody
	boolean add(@RequestBody Role role) {
        OperationLog operationLog = systemLogService.record("角色","保存","名称：" + role.getName());
		roleServiceImpl.save(role);
        systemLogService.updateResult(operationLog);
		return true;
	}

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public @ResponseBody
    Map<String,Object> getAll(String operationId,HttpServletRequest req) {
        String starpage = req.getParameter("page");
        String rows = req.getParameter("rows");
        StringBuffer hql = new StringBuffer("select role from Role role");
        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();
        Page page = roleServiceImpl.findPage(hql.toString(), Integer.parseInt(rows), searchConds);
        page.setPage(Integer.parseInt(starpage));
        List<Role> role = roleServiceImpl.findBy(hql.toString(),page,searchConds);
        Map<String,Object> resMap = new HashMap<String,Object>();
        resMap.put("total", page.getResultCount());
        resMap.put("rows", role);
        return resMap;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView getById(@PathVariable String id) {
        Role role= roleServiceImpl.getById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("role",role);
        model.setViewName("role/roleEdit");
        return model;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String id) {
        OperationLog operationLog = systemLogService.record("角色","删除","角色ID:" + id);

        roleServiceImpl.deleteById(id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/query", headers = "Accept=application/json")
    public
    @ResponseBody
    List<RoleVO> getByName( @RequestBody Map<String, String> params) {
        List<RoleVO> roleVOs = new ArrayList<RoleVO>();
        List<Role> roles = roleServiceImpl.findLikeAnyWhere(params);
        for(Role role : roles){
        	roleVOs.add(new RoleVO(role));
        }
        return roleVOs;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/checkUnique/roleId/{roleId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean passWord(@PathVariable("roleId") String roleId) {
        Role role = roleServiceImpl.getById(roleId);
        if(null != role){
            return false;
        }
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/checkRoleNameUnique/name/{name}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean checkRoleNameUnique(@PathVariable("name") String name) {
        List<Role> roles = roleServiceImpl.findBy("name",name);
        if(roles.size() > 0){
            return false;
        }
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getRelation/{id}", headers = "Accept=application/json")
    public @ResponseBody
    boolean getRelation(@PathVariable String id) {
    	List<UserRoleRelation> urr= userRoleRelationService.findBy("roleId", id);
    	if(urr.size()==0){
    		 return false;
    	} 
        return true;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
