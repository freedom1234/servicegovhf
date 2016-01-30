package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.ServiceHead;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceHeadServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/12.
 */
@Controller
@RequestMapping("/serviceHead")
public class ServiceHeadController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    private ServiceHeadServiceImpl serviceHeadService;

    @RequiresPermissions({"serviceHead-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/query", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> query(HttpServletRequest req){
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(serviceHeadService.queryCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
//        List<Metadata> rows = metadataService.queryByCondition(req.getParameterMap(), page);
        //关联categoryWord表，显示chineseWord
        List<ServiceHead> rows = serviceHeadService.queryByCondition(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }
//    @RequiresPermissions({"serviceHead-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/queryAll", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceHead> queryAll(String type){
        List<ServiceHead> result = serviceHeadService.getAll();
        return result;
    }

    /**
     * @param headIds id组成的字符串，中间用逗号‘，’隔开
     * @return
     */
    @RequiresPermissions({"serviceHead-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/queryByHeadIds", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceHead> queryByHeadIds(String headIds){
        List<ServiceHead> result = serviceHeadService.getByIdStr(headIds);
        return result;
    }

    @RequiresPermissions({"serviceHead-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String headName){
        boolean result = serviceHeadService.uniqueName(headName);
        return result;
    }

    @RequiresPermissions({"serviceHead-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(ServiceHead serviceHead){
        OperationLog operationLog = systemLogService.record("服务报文头", "添加", "报文头名称：" + serviceHead.getHeadName());

        serviceHeadService.add(serviceHead);


        systemLogService.updateResult(operationLog);
        return true;
    }
    @RequiresPermissions({"serviceHead-update"})
    @RequestMapping(method = RequestMethod.GET, value = "/editPage", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView editPage(String headId){
        ModelAndView mv = new ModelAndView("service/service_head/service_head_edit");
        if(StringUtils.isNotEmpty(headId)){
            ServiceHead entity = serviceHeadService.findUniqueBy("headId", headId);
            mv.addObject("entity", entity);
        }
        return mv;
    }
    @RequiresPermissions({"serviceHead-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/edit", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean edit(ServiceHead serviceHead){
        OperationLog operationLog = systemLogService.record("服务报文头", "修改", "报文头名称：" + serviceHead.getHeadName());

        serviceHeadService.update(serviceHead);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"serviceHead-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/delete", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(String headId){
        OperationLog operationLog = systemLogService.record("服务报文头", "删除", "");
        String logParam = "名称：";
        if(StringUtils.isNotEmpty(headId)){
            ServiceHead entity = serviceHeadService.findUniqueBy("headId", headId);
            logParam  += entity.getHeadName();
            serviceHeadService.delete(entity);
        }
        operationLog.setParams(logParam);
        systemLogService.updateResult(operationLog);
        return true;
    }
}
