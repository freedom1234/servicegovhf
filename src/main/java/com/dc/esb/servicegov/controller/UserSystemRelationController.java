package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.UserSystemRelationService;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemServiceImpl;
import com.dc.esb.servicegov.service.impl.UserSystemRelationServiceImpl;
import com.dc.esb.servicegov.vo.SystemVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
@Controller
@RequestMapping("/userSystemRelation")
public class UserSystemRelationController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    private UserSystemRelationServiceImpl userSystemRelationService;
    @Autowired
    private SystemServiceImpl systemService;
    /**
     * 根据用户id获取系统，用于用户管理中系统分配
     * @param userId
     * @return
     */
    @RequiresPermissions({"system-get"})
    @RequestMapping(value = "/getByUserId")
    public @ResponseBody
    List<SystemVO> getByUserId(String userId){
        List<com.dc.esb.servicegov.entity.System> list = systemService.getByUserId(userId);
        String hql = " from System order by systemId asc";
        List<System> all = systemService.find(hql);
        List<SystemVO> result = new ArrayList<SystemVO>();

        for(System system : all){
            SystemVO systemVO = new SystemVO();
            systemVO.setSystemId(system.getSystemId());
            systemVO.setSystemChineseName(system.getSystemChineseName());
            systemVO.setSystemAb(system.getSystemAb());
            if( 0 == list.size()){//如果没有设置用户-系统关系，默认拥有所有系统的权限
                systemVO.setChecked(true);
            }else{
                if(list.contains(system)){
                    systemVO.setChecked(true);
                }else{
                    systemVO.setChecked(false);
                }
            }
            result.add(systemVO);
        }
        return result;
    }

    /**
     * 保存用户-系统关系
     * @param userId 用户id
     * @param systemIdsStr 系统id拼接成的字符串，用逗号隔开
     * @return
     */
    @RequiresRoles({"admin"})
    @RequestMapping("/saveUserSystem")
    public @ResponseBody boolean saveUserSystem(String userId, String systemIdsStr){
        OperationLog operationLog = systemLogService.record("用户管理", "系统分配", "为用户["+userId+"]重新分配了系统["+systemIdsStr+"]");

        boolean result = userSystemRelationService.saveUserSystem(userId, systemIdsStr);

        systemLogService.updateResult(operationLog);
        return result;
    }
}
