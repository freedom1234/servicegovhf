package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.LogInfo;
import com.dc.esb.servicegov.service.impl.LogInfoServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangqi on 2015/8/3.
 */

@Controller
@RequestMapping("/log")
public class LogController {

    protected Log logger = LogFactory.getLog(getClass());
    @Autowired
    LogInfoServiceImpl logInfoService;

    @RequiresPermissions({"importlog-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getAll(HttpServletRequest req) {
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = logInfoService.getAll(rowCount);
        page.setPage(pageNo);
        page.setOrderBy("time");
        page.setOrder("desc");
        List<LogInfo> rows = logInfoService.getAll(page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"importlog-delete"})
    @RequestMapping(method = RequestMethod.POST,value = "/delete", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@RequestParam("logInfos") String loginIds){
        String[] ids = loginIds.split("\\,");
        if(ids != null && ids.length > 0){
            for(String logId : ids){
                logInfoService.deleteById(logId);
            }
        }
        return true;
    }

    @RequiresPermissions({"importlog-delete"})
    @RequestMapping(method = RequestMethod.POST,value = "/deleteAll", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteAll(){
        logInfoService.deleteAll();
        return true;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
//        ModelAndView mv = new ModelAndView("403");
//        return mv;
//        return "forward:/jsp/403.jsp";
        return "403";
    }
}
