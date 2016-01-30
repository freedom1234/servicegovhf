package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.VersionHis;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.impl.VersionServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/1.
 */
@Controller
@RequestMapping("/version")
public class VersionController {
    @Autowired
    private VersionServiceImpl versionService;
    @Autowired
    private OperationServiceImpl operationService;

    @RequiresPermissions({"version-get"})
    @RequestMapping("/operationList")
    @ResponseBody
    public Map<String, Object> operationList(HttpServletRequest req) {
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));

        String serviceId = req.getParameter("serviceId");
        if(null == serviceId) serviceId = "";
        String serviceName = req.getParameter("serviceName");
        if(null != serviceName && !"".equals(serviceName)){
            try{
                serviceName = URLDecoder.decode(req.getParameter("serviceName"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            serviceName = "";
        }
        String operationId = req.getParameter("operationId");
        if(null == operationId) operationId = "";
        String operationName = req.getParameter("operationName");
        if(null != operationName && !"".equals(operationName)){
            try{
                operationName = URLDecoder.decode(req.getParameter("operationName"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            operationName = "";
        }
//        Page page = operationService.getAll(rowCount);
        String hql = "from Operation a where a.state=? and a.version.optType !=? and a.service.serviceId like ? and a.service.serviceName like ?";
        hql += " and a.operationId like ? and a.operationName like ?";
        Page page = operationService.getPageBy(hql,rowCount, Constants.Operation.OPT_STATE_PASS, Constants.Version.OPT_TYPE_RELEASE,"%"+serviceId+"%","%"+serviceName+"%",
                                                "%"+operationId+"%","%"+operationName+"%");
        page.setPage(pageNo);

        Map<String, Object> result = new HashMap<String, Object>();
        List<Operation> list = operationService.getReleased(page,serviceId,serviceName,operationId,operationName);
        result.put("total", page.getResultCount());
        result.put("rows", list);
        return result;
    }

    /**
     * @param type 对比类型 0：当前版本VS历史版本， 1：历史版本VS历史版本
     * @param autoId1 如果是当前版本，值为''
     * @param autoId2
     * @return
     */
    @RequiresPermissions({"version-get"})
    @RequestMapping("/getOperationDiff")
    @ResponseBody
    public Map<String, Object> getOperationDiff(String type,String versionId, String autoId1, String autoId2){
        Map<String, Object> result = versionService.getOperationDiff(type, versionId, autoId1, autoId2);
        return result;
    }

    @RequiresPermissions({"version-get"})
    @RequestMapping("/getInterfaceDiff")
    @ResponseBody
    public Map<String, Object> getInterfaceDiff(String type,String versionId, String autoId1, String autoId2){
        Map<String, Object> result = versionService.getInterfaceDiff(type, versionId, autoId1, autoId2);
        return result;
    }

    /**
     * 根据传入的版本号查询相关版本历史
     * @param versionId 版本id
     * @return
     */
    @RequiresPermissions({"version-get"})
    @RequestMapping("/getVersions")
    @ResponseBody
    public List<VersionHis> getVersions(String versionId){
        List<VersionHis> list = versionService.getVersions(versionId);
        return list;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

}
