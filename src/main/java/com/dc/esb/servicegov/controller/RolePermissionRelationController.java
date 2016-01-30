package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.RolePermissionRelation;
import com.dc.esb.servicegov.service.impl.RolePermissionRelationServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2015/7/13.
 */

@Controller
@RequestMapping("/rprelation")
public class RolePermissionRelationController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private RolePermissionRelationServiceImpl rolePermissionRelationService;
    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/save", headers = "Accept=application/json")
    public @ResponseBody
    boolean add(@RequestBody RolePermissionRelation[] rprs) {
        OperationLog operationLog = systemLogService.record("权限","角色权限添加","数量:" + rprs.length );

        for(RolePermissionRelation  rpr : rprs){
            RolePermissionRelation rolePermissionRelation = new RolePermissionRelation();
            rolePermissionRelation.setPermissionId(rpr.getPermissionId());
            rolePermissionRelation.setRoleId(rpr.getRoleId());
            rolePermissionRelationService.save(rolePermissionRelation);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<RolePermissionRelation> rolePermissionRelation(@PathVariable(value = "id") String roleId){
        HashMap<String,String> param = new HashMap<String, String>();
        param.put("roleId",roleId);
        List<RolePermissionRelation> temp = rolePermissionRelationService.findBy(param);
        return temp;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
    public @ResponseBody
    boolean modify(@RequestBody RolePermissionRelation[] rprs) {
        OperationLog operationLog = systemLogService.record("权限","角色权限修改","数量:" + rprs.length );

        for(RolePermissionRelation  rpr : rprs){
            RolePermissionRelation rolePermissionRelation = new RolePermissionRelation();
            rolePermissionRelation.setPermissionId(rpr.getPermissionId());
            rolePermissionRelation.setRoleId(rpr.getRoleId());
            rolePermissionRelationService.save(rolePermissionRelation);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String id) {
        OperationLog operationLog = systemLogService.record("权限","角色权限删除","角色ID:" + id );

        HashMap<String,String> param = new HashMap<String, String>();
        param.put("roleId",id);
        List<RolePermissionRelation> temp = rolePermissionRelationService.findBy(param);
        for(RolePermissionRelation u : temp){
            rolePermissionRelationService.delete(u);
        }
        return true;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
