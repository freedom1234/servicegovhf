package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.Protocol;
import com.dc.esb.servicegov.entity.SystemProtocol;
import com.dc.esb.servicegov.service.ProtocolService;
import com.dc.esb.servicegov.service.SystemProtocolService;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2015/7/3.
 */

@Controller
@RequestMapping("/protocol")
public class ProtocolController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private SystemProtocolService systemProtocolService;

    @RequiresPermissions({"protocol-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/getAll", headers = "Accept=application/json")
    public @ResponseBody
    Map<String,Object> getAll(HttpServletRequest req) {

        String starpage = req.getParameter("page");
        String rows = req.getParameter("rows");

        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();

        StringBuffer hql = new StringBuffer("FROM Protocol t1 WHERE 1=1");

        String protocolName = req.getParameter("protocolName");
        String encoding = req.getParameter("encoding");
        String msgType = req.getParameter("msgType");
        String remark = req.getParameter("remark");
        if(protocolName!=null&&!"".equals(protocolName)){
            hql.append(" AND t1.protocolName like ?");
            SearchCondition search = new SearchCondition();
            search.setFieldValue("%"+protocolName+"%");
            searchConds.add(search);
        }
        if(encoding!=null&&!"".equals(encoding)){
            hql.append(" AND t1.encoding like ?");
            SearchCondition search = new SearchCondition();
            search.setFieldValue("%" +encoding+ "%");
            searchConds.add(search);
        }
        if(msgType!=null&&!"".equals(msgType)){
            hql.append(" AND t1.msgType like ?");
            SearchCondition search = new SearchCondition();
            search.setFieldValue("%"+msgType+"%");
            searchConds.add(search);
        }
        if(remark!=null&&!"".equals(remark)){
            hql.append(" AND t1.remark like ?");
            SearchCondition search = new SearchCondition();
            search.setFieldValue("%"+remark+"%");
            searchConds.add(search);
        }

        SearchCondition searchCond = new SearchCondition();

        Page page = protocolService.findPage(hql.toString(), Integer.parseInt(rows), searchConds);
        page.setPage(Integer.parseInt(starpage));

        List<Protocol> protocols = protocolService.findBy(hql.toString(),page,searchConds);
        //List<Protocol> resList = new ArrayList<Protocol>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", page.getResultCount());
        map.put("rows", protocols);
        return map;
    }


    @RequiresPermissions({"protocol-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/get/{protocolId}", headers = "Accept=application/json")
    public @ResponseBody
    Map<String,Object> getById(@PathVariable String protocolId) {
        Protocol protocol = protocolService.getById(protocolId);
        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(protocol);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", 1);
        map.put("rows", protocols);
        return map;
    }

    @RequiresPermissions({"protocol-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public @ResponseBody
        Protocol add(@RequestBody Protocol protocol) {
        OperationLog operationLog = systemLogService.record("协议","添加","协议名称：" + protocol.getProtocolName());

        if(StringUtils.isEmpty(protocol.getGeneratorId())){
            protocol.setGeneratorId(null);
        }
        if(protocol.getMsgTemplateId()==null || "".equals(protocol.getMsgTemplateId())) {
            String msgtemplateId = UUID.randomUUID().toString();
            protocol.setMsgTemplateId(msgtemplateId);
            protocol.getMsgTemplate().setTemplateId(msgtemplateId);
            protocol.getMsgTemplate().setTemplateName(protocol.getProtocolName() + "协议模板");
        }
        protocolService.save(protocol);

        systemLogService.updateResult(operationLog);
        return protocol;
    }

    @RequiresPermissions({"protocol-delete"})
    @RequestMapping(method = RequestMethod.GET, value = "/delete/{protocolId}", headers = "Accept=application/json")
    public @ResponseBody
    boolean delete (@PathVariable String protocolId) {
        OperationLog operationLog = systemLogService.record("协议","删除","协议ID：" + protocolId);

        List<SystemProtocol> systemProtocols = systemProtocolService.findBy("protocolId", protocolId);
        for(SystemProtocol systemProtocol : systemProtocols){
            systemProtocolService.delete(systemProtocol);
        }
        protocolService.deleteById(protocolId);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"protocol-update"})
    @RequestMapping(method = RequestMethod.GET, value = "/edit/{protocolId}", headers = "Accept=application/json")
    public ModelAndView getSystem(@PathVariable
                                  String protocolId) {

      Protocol protocol = protocolService.getById(protocolId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("protocol", protocol);
        modelAndView.setViewName("sysadmin/protocol_edit");
        return modelAndView;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
