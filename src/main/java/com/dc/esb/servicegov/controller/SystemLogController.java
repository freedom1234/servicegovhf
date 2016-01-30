package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.OperationLogType;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.util.PackageUtil;
import com.dc.esb.servicegov.vo.ReuseRateVO;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/18.
 */
@Controller
@RequestMapping("/systemLog")
public class SystemLogController {
    @Autowired
    SystemLogServiceImpl systemLogService;
    @RequiresPermissions({"systemLog-get"})
    @RequestMapping("/importCommLogType")
    @ResponseBody
    public void importCommLogType(){
        String packageName = "com.dc.esb.servicegov.service.impl";
        List<String> classNames = PackageUtil.getClassName(packageName, false);
        for(String im : classNames){
            String[] names = im.split("\\.");
            String name = names[names.length -1];
            String[] methods = {"save", "insert", "update", "delete", "deleteById", "deleteAll"};
            String[] methodNames = {"保存", "新增", "更新", "删除", "删除byId", "删除所有"};
            for(int i=0; i< 6; i++){
                OperationLogType operationLogType = new OperationLogType();
                operationLogType.setClassName(im);
                operationLogType.setChineseName(name);
                operationLogType.setMethodName(methods[i]);
                operationLogType.setOperationName(methodNames[i]);
                systemLogService.insertLogType(operationLogType);
            }
        }
    }
    @RequiresPermissions({"systemLog-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/query", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest req){
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(systemLogService.getLogCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
        List<OperationLog> rows = systemLogService.getLogs(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
