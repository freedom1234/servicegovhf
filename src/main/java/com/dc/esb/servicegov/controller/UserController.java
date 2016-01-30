package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRoleRelationServiceImpl userRoleRelationService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private OrgServiceImpl orgService;

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(@RequestBody SGUser user) {
        OperationLog operationLog = systemLogService.record("用户","添加","用户名称：" + user.getName());

        userServiceImpl.save(user);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/assignRoles", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(@RequestBody UserRoleRelation[] userRoleRelations) {
        OperationLog operationLog = systemLogService.record("用户","添加角色关系","");

        for(UserRoleRelation userRoleRelation : userRoleRelations){
            userRoleRelationService.save(userRoleRelation);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

//    @RequiresRoles({"admin"})
@RequiresPermissions({"user-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getAll( @RequestParam("page") int pageNo, @RequestParam("rows") int rowCount) {
        List<UserVO> userVOs = new ArrayList<UserVO>();
        Page page = userServiceImpl.getAll(rowCount);
        page.setPage(pageNo);
        List<SGUser> rows = userServiceImpl.getAll(page);
        for(SGUser user : rows){
            String orgId = user.getOrgId();
            Organization org = orgService.getById(orgId);
            if(null != org){
                user.setOrgId(org.getOrgName());
            }
            List<UserRoleRelation> rs = userRoleRelationService.findBy("userId", user.getId());
            userVOs.add(new UserVO(user, rs));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", userVOs);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUser", headers = "Accept=application/json")
    public
    @ResponseBody
    List<SGUser> getAllUser() {
        return userServiceImpl.getAll();
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/query", headers = "Accept=application/json")
    public
    @ResponseBody
    List<UserVO> getByName( @RequestBody Map<String, String> params) {
        List<UserVO> userVOs = new ArrayList<UserVO>();
        List<SGUser> users = userServiceImpl.findLikeAnyWhere(params);
        for(SGUser user : users){
            List<UserRoleRelation> rs = userRoleRelationService.findBy("userId", user.getId());
            userVOs.add(new UserVO(user, rs));
        }
        return userVOs;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String id) {
        OperationLog operationLog = systemLogService.record("用户", "删除", "用户ID:" + id);

    	userRoleRelationService.deleteRelation(id);
        userServiceImpl.deleteById(id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}", headers = "Accept=application/json")
    public ModelAndView getById(
            @PathVariable String id) {
        SGUser user = userServiceImpl.getById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.setViewName("user/userEdit");

        List<UserRoleRelation> rs = userRoleRelationService.findBy("userId", user.getId());
        String roleId = "";
        for (UserRoleRelation us : rs) {
            roleId += us.getRoleId() + ",";
        }
        roleId = roleId.substring(0, roleId.length() - 1);
        model.addObject("roleId", roleId);
        return model;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify(@RequestBody SGUser SGUser) {
        OperationLog operationLog = systemLogService.record("用户", "修改", "用户名称：" + SGUser.getName());

    	userRoleRelationService.deleteRelation(SGUser.getId());
        userServiceImpl.update(SGUser);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modify2", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify2(@RequestBody SGUser SGUser) {
        OperationLog operationLog = systemLogService.record("用户", "修改", "用户名称：" + SGUser.getName());
        SGUser user = userServiceImpl.findUniqueBy("id", SGUser.getId());
        if(user != null){
            user.setName(SGUser.getName());
            user.setRemark(SGUser.getRemark());
            user.setLastdate(SGUser.getLastdate());
            user.setPassword(SGUser.getPassword());
            user.setUserMobile(SGUser.getUserMobile());
            user.setUserTel(SGUser.getUserTel());

            userServiceImpl.update(user);
        }


        systemLogService.updateResult(operationLog);
        return true;
    }
    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getUserInfo", headers = "Accept=application/json")
    public ModelAndView getUserInfo() {
        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        SGUser SGUser = userServiceImpl.getById(userId);
        ModelAndView model = new ModelAndView();
        model.addObject("user", SGUser);
        model.setViewName("user/userInfo");
        return model;
    }

    @RequiresPermissions({"password-update"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByPW/{id}", headers = "Accept=application/json")
    public ModelAndView getByPW(
            @PathVariable String id) {
        SGUser SGUser = userServiceImpl.getById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("user", SGUser);
        model.setViewName("user/passWord");
        return model;
    }

//    @RequiresRoles({"admin"})
    @RequiresPermissions({"password-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/passWord/{id}/{passWord}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean pwEdit(@PathVariable String id,@PathVariable String passWord) {
        OperationLog operationLog = systemLogService.record("用户", "密码修改", "用户ID：" + id);

        userServiceImpl.passWord(passWord, id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/checkUnique/userId/{userId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean passWord(@PathVariable("userId") String userId) {
        SGUser user = userServiceImpl.getById(userId);
        if(null != user){
            return false;
        }
        return true;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
