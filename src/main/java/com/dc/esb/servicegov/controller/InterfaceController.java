package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.*;
import com.dc.esb.servicegov.service.impl.ProcessContextServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.impl.TagServiceImpl;
import com.dc.esb.servicegov.service.impl.VersionServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.JSONUtil;
import com.dc.esb.servicegov.util.TreeNode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/interface")
public class InterfaceController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    private static final Log log = LogFactory.getLog(InterfaceController.class);
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ServiceInvokeService serviceInvokeService;
    @Autowired
    private IdaService idaService;
    @Autowired
    private InterfaceHeadService interfaceHeadService;
    @Autowired
    private InterfaceHeadRelateService interfaceHeadRelateService;
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private ProcessContextServiceImpl processContextService;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private VersionServiceImpl versionServiceImpl;

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/{systemIds}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getLeftTree(@PathVariable(value = "systemIds") String systemIds) {
        List<TreeNode> resList = new ArrayList<TreeNode>();
        TreeNode root = new TreeNode();
        root.setId("root");
        root.setText("系统");
        root.setClick("system");
        List<com.dc.esb.servicegov.entity.System> systems = new ArrayList<System>();
        if ("all".equals(systemIds)) {
            //增加排序
            systems = systemService.getAllOrderBySystemId();
        } else if (null != systemIds) {
            systemIds = systemIds.trim();
            if (!systemIds.equalsIgnoreCase("")) {
                String[] systemArr = systemIds.split(",");
                for (String systemId : systemArr) {
                    System s = systemService.findUniqueBy("systemId", systemId);
                    if (systems.indexOf(s) < 0) {
                        systems.add(s);
                    }
                }
            }
        }
        List<TreeNode> rootList = interfaceService.getLeftTreeBySystems(systems);

        for (TreeNode node : rootList) {
            if (null != node.getChildren() && node.getChildren().size() > 0) {
                node.setState("closed");
            }
        }
        root.setChildren(rootList);
        resList.add(root);
        return resList;
    }
    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftLazyTree", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getLeftLazyTree() {
        List<TreeNode> resList = new ArrayList<TreeNode>();
        TreeNode root = new TreeNode();
        root.setId("root");
        root.setText("系统");
        root.setClick("system");
//        List<com.dc.esb.servicegov.entity.System> systems = systemService.getAllOrderBySystemId();
        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        List<com.dc.esb.servicegov.entity.System> systems = systemService.getByUserId(userId);//根据用户在usersystemrelation中查找
        if(0 == systems.size()){
            systems = systemService.getAllOrderBySystemId();//如果在用户系统关系表中没有数据，默认为没有设置过，拥有所有系统的权限
        }
        List<TreeNode> children = new ArrayList<TreeNode>();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("id","systemId");
        fields.put("text", "systemChineseName");
        for(int i = 0; i < systems.size(); i++){
            System system = systems.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(system.getSystemId());
            treeNode.setText(system.getSystemChineseName());
            treeNode.setState("closed");
            treeNode.setClick("system");
            children.add(treeNode);
        }

        root.setChildren(children);
        resList.add(root);
        return resList;
    }
    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/subtree/system/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getSystemSubLeftTree(@PathVariable(value = "systemId") String systemId) {
        return interfaceService.getSingleSystemTreeNode(systemId);
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/subInterfaceTree/system/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getInterfaceSubLeftTree(@PathVariable(value = "systemId") String systemId) {
        System system = systemService.getById(systemId);
        return interfaceService.getInterfaceTreeChildren(system);
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/subHeadTree/system/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getHeadSubLeftTree(@PathVariable(value = "systemId") String systemId) {
        System system = systemService.getById(systemId);
        return interfaceService.getHeadTreeChildren(system);
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/subProtocolTree/system/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getProtocolSubLeftTree(@PathVariable(value = "systemId") String systemId) {
        System system = systemService.getById(systemId);
        return interfaceService.getProtocolTreeChildren(system);
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getLeftTree/subFileTree/system/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<TreeNode> getFileSubLeftTree(@PathVariable(value = "systemId") String systemId) {
        System system = systemService.getById(systemId);
        return interfaceService.getFileTreeChildren(system);
    }

//    @RequiresPermissions({"system-add"})
    @RequiresPermissions({"interface-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean save(@RequestBody
                 Interface inter, HttpServletRequest request) {
        OperationLog operationLog = systemLogService.record("接口","保存","接口名称：" + inter.getInterfaceName());

        //新增操作
        boolean add = "add".equals(request.getParameter("type"));
        if (!add) {
            String hql = "update ServiceInvoke set protocolId = ? where interfaceId = ?";
            serviceInvokeService.updateProtocolId(hql, inter.getServiceInvoke().get(0).getProtocolId(), inter.getInterfaceId());
            //修改接口关系表不更新
            inter.setServiceInvoke(null);
        }
        interfaceService.save(inter, add);
        if (add) {
            //添加报文，自动生成固定报文头<root><request><response>
            //root
            Ida ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(null);
            ida.setStructName("root");
            ida.setStructAlias("根节点");
            ida.setState(Constants.IDA_STATE_COMMON);
            ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
            idaService.save(ida);
            String parentId = ida.getId();

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(parentId);
            ida.setStructName("request");
            ida.setStructAlias("请求报文体");
            ida.setSeq(0);
            ida.setState(Constants.IDA_STATE_COMMON);
            ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
            idaService.save(ida);

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(parentId);
            ida.setSeq(1);
            ida.setStructName("response");
            ida.setStructAlias("响应报文体");
            ida.setState(Constants.IDA_STATE_COMMON);
            ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
            idaService.save(ida);
        }

        systemLogService.updateResult(operationLog);
        return true;

    }

    @RequiresPermissions({"system-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add/{processId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean saveWithProcess(@RequestBody
                            Interface inter, @PathVariable("processId") String processId, HttpServletRequest request) {
        OperationLog operationLog = systemLogService.record("接口","添加（任务）","接口名称:" + inter.getInterfaceName());
        //新增操作
        String optUser = SecurityUtils.getSubject().getPrincipal().toString();
        String optDate = DateUtils.format(new Date());
        boolean add = "add".equals(request.getParameter("type"));
        if (!add) {
            String hql = "update ServiceInvoke set protocolId = ? where interfaceId = ?";
            serviceInvokeService.updateProtocolId(hql, inter.getServiceInvoke().get(0).getProtocolId(), inter.getInterfaceId());
            //修改接口关系表不更新
            inter.setServiceInvoke(null);
        }
        inter.setOptUser(optUser);
        inter.setOptDate(optDate);
        interfaceService.save(inter);
        if (add) {
            //添加报文，自动生成固定报文头<root><request><response>
            //root
            Ida ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(null);
            ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
            ida.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
            ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
            idaService.save(ida);
            String parentId = ida.getId();

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(parentId);
            ida.setSeq(0);
            ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
            ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
            ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
            idaService.save(ida);

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(parentId);
            ida.setSeq(1);
            ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
            ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
            ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
            idaService.save(ida);
        }


        com.dc.esb.servicegov.entity.ProcessContext processContext = new com.dc.esb.servicegov.entity.ProcessContext();
        processContext.setName("接口定义");
        processContext.setProcessId(processId);
        processContext.setKey("interface");
        processContext.setValue(inter.getInterfaceId());
        processContext.setType("result");
        processContext.setRemark("添加接口[" + inter.getInterfaceId() + "(" + inter.getInterfaceName() + ")" + "]");
        processContext.setOptDate(optDate);
        processContext.setOptUser(optUser);
        processContextService.save(processContext);

        systemLogService.updateResult(operationLog);
        return true;

    }

//    @RequiresPermissions({"system-delete"})
    @RequiresPermissions({"interface-delete"})
    @RequestMapping(method = RequestMethod.GET, value = "/delete/{interfaceId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable
                   String interfaceId) {
        OperationLog operationLog = systemLogService.record("接口","删除","接口ID：" + interfaceId);
        //TODO 删除接口要删除serviceInvoke（外键）
        List<ServiceInvoke> serviceInvokes = serviceInvokeService.findBy("interfaceId", interfaceId);
        serviceInvokeService.deleteEntity(serviceInvokes);
        //TODO 删除接口要删除ida
        interfaceService.deleteById(interfaceId);
        Map map = new HashMap();
        map.put("interfaceId", interfaceId);
        List<Ida> list = idaService.findBy(map);
        idaService.deleteList(list);

        systemLogService.updateResult(operationLog);
        return true;
    }

//    @RequiresPermissions({"system-delete"})
    @RequiresPermissions({"interface-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/delete2", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete2(@RequestBody
                   String interfaceId) {
        OperationLog operationLog = systemLogService.record("接口","删除","接口ID：" + interfaceId);
        //去掉''
        interfaceId = interfaceId.substring(1,interfaceId.length()-1);
        //TODO 删除接口要删除serviceInvoke（外键）
        List<ServiceInvoke> serviceInvokes = serviceInvokeService.findBy("interfaceId", interfaceId);
        serviceInvokeService.deleteEntity(serviceInvokes);
        //TODO 删除接口要删除ida
        interfaceService.deleteById(interfaceId);
        Map map = new HashMap();
        map.put("interfaceId", interfaceId);
        List<Ida> list = idaService.findBy(map);
        idaService.deleteList(list);

        systemLogService.updateResult(operationLog);
        return true;
    }

//    @RequiresPermissions({"system-update"})
    @RequiresPermissions({"interface-update"})
    @RequestMapping(method = RequestMethod.GET, value = "/edit/{interfaceId}", headers = "Accept=application/json")
    public ModelAndView getInterface(@PathVariable
                                     String interfaceId) {

        Interface inter = interfaceService.getById(interfaceId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("inter", inter);
        modelAndView.setViewName("interface/interface_edit");
        return modelAndView;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getInterById/{interfaceId}", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getInterById(@PathVariable
                                     String interfaceId) {
        //String hql = "SELECT u.interfaceId,u.interfaceName,u.ecode,u.remark,u.status,u.version,u.optUser,u.optDate FROM Interface u WHERE interfaceId = ?";
        Interface inter = interfaceService.getById(interfaceId);
        Map<String, Object> map = new HashMap<String, Object>();
        if(null != inter){
            //TODO 为什么要重新建一个interface？
            Interface resInter = new Interface();
            resInter.setInterfaceId(inter.getInterfaceId());
            resInter.setInterfaceName(inter.getInterfaceName());
            resInter.setEcode(inter.getEcode());
            resInter.setDesc(inter.getDesc());
            resInter.setRemark(inter.getRemark());
            resInter.setOptDate(inter.getOptDate());
            resInter.setOptUser(inter.getOptUser());
            resInter.setStatus(inter.getStatus());
            resInter.setVersionId(inter.getVersionId());
            resInter.setVersion(inter.getVersion());
            List<InterfaceHeadRelate> heads = inter.getHeadRelates();
            String headName = "";
            for (InterfaceHeadRelate head : heads) {
                if (!"".equals(headName)) {
                    headName += ",";
                }
                headName += head.getInterfaceHead().getHeadName();
            }
            resInter.setHeadName(headName);


            List<Interface> inters = new ArrayList<Interface>();
            inters.add(resInter);
            map.put("total", 1);
            map.put("rows", inters);
        }

        return map;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/getInterface/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getInterfaceBySystemId(@PathVariable String systemId, HttpServletRequest req) {

        String starpage = req.getParameter("page");

        String rows = req.getParameter("rows");

        String ecode = req.getParameter("ecode");
        String interfaceName = req.getParameter("interfaceName");
        String desc = req.getParameter("desc");
        try {
            if (null != desc)
                desc = URLDecoder.decode(desc, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String interfaceTag = req.getParameter("interfaceTag");
        if(interfaceTag==null){
            interfaceTag = "";
        }
        try{
            if (null != interfaceTag)
                interfaceTag = URLDecoder.decode(interfaceTag,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        String interfaceIds = "";
        if(!interfaceTag.equals("")){
            interfaceIds = tagService.findInterfaceIdsByTag(interfaceTag);
        }
        String status = req.getParameter("status");
        String protocolId = req.getParameter("protocolId");
        String headId = req.getParameter("headId");


        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();

        StringBuffer hql = new StringBuffer("SELECT distinct t1 FROM Interface t1,ServiceInvoke t2 where t1.interfaceId=t2.interfaceId ");
        hql.append("and t2.systemId=? ");

        SearchCondition searchCond = new SearchCondition();

        searchCond.setField("systemId");
        searchCond.setFieldValue(systemId);
        searchConds.add(searchCond);
        if (ecode != null && !"".equals(ecode)) {
            searchCond = new SearchCondition();
            hql.append(" and t1.ecode like ?");
            searchCond.setField("ecode");
            searchCond.setFieldValue("%" + ecode + "%");
            searchConds.add(searchCond);
        }
        if (interfaceName != null && !"".equals(interfaceName)) {
            searchCond = new SearchCondition();
            hql.append(" and t1.interfaceName like ?");
            searchCond = new SearchCondition();
            searchCond.setField("interfaceName");
            searchCond.setFieldValue("%" + interfaceName + "%");
            searchConds.add(searchCond);
        }
        if (desc != null && !"".equals(desc)) {
            searchCond = new SearchCondition();
            hql.append(" and t1.desc like ?");
            searchCond = new SearchCondition();
            searchCond.setField("desc");
            searchCond.setFieldValue("%" + desc + "%");
            searchConds.add(searchCond);
        }
        if (status != null && !"".equals(status)) {
            searchCond = new SearchCondition();
            hql.append(" and t1.status=?");
            searchCond = new SearchCondition();
            searchCond.setField("status");
            searchCond.setFieldValue(status);
            searchConds.add(searchCond);
        }
        if (protocolId != null && !"".equals(protocolId)) {
            searchCond = new SearchCondition();
            hql.append(" and t2.protocolId=?");
            searchCond = new SearchCondition();
            searchCond.setField("protocolId");
            searchCond.setFieldValue(protocolId);
            searchConds.add(searchCond);
        }
        if (headId != null && !"".equals(headId)) {
            searchCond = new SearchCondition();
            hql.append(" and exists (select 1 from InterfaceHeadRelate t3 WHERE t3.interfaceId = t1.interfaceId and t3.headId = ?)");
            searchCond = new SearchCondition();
            searchCond.setField("headId");
            searchCond.setFieldValue(headId);
            searchConds.add(searchCond);
        }
        if(!interfaceTag.equals("")){
            if(!interfaceIds.equals("")){
                hql.append(" and t1.interfaceId in ("+interfaceIds+")");
            }else{
                hql.append(" and 1=2");
            }
        }



        Page page = interfaceService.findPage(hql.toString(), Integer.parseInt(rows), searchConds);
        page.setPage(Integer.parseInt(starpage));

        hql.append(" order by t1.interfaceId ");

        List<Interface> inters = interfaceService.findBy(hql.toString(), page, searchConds);
        for (Interface i : inters) {

            List<InterfaceHeadRelate> heads = i.getHeadRelates();
            String headName = "";
            if (heads != null) {
                for (InterfaceHeadRelate head : heads) {
                    if (!"".equals(headName)) {
                        headName += ",";
                    }
                    headName += head.getInterfaceHead().getHeadName();
                }
            }
            if (i.getServiceInvoke() != null && i.getServiceInvoke().size() > 0) {
                ServiceInvoke invoke = i.getServiceInvoke().get(0);
                if (invoke.getProtocolId() != null && !"".equals(invoke.getProtocolId())) {
                    Protocol p = protocolService.getById(invoke.getProtocolId());
                    if (p != null) {
                        i.setProtocolName(p.getProtocolName());
                    }
                }
            }
            i.setHeadName(headName);
            //避免转化json错误，设置ServiceInvoke=null;
            i.setServiceInvoke(null);
            i.setHeadRelates(null);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getResultCount());
        map.put("rows", inters);
        return map;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getHeadBySystemId/{systemId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Map<String, Object>> getHeadBySystemId(@PathVariable String systemId,HttpServletRequest request) {
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        List<InterfaceHead> heads = interfaceHeadService.findBy("systemId",systemId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getParameter("query") != null && !"".equals(request.getParameter("query"))) {
            map.put("id", "");
            map.put("text", "全部");
            resList.add(map);
        } else {
            map.put("id", "");
            map.put("text", "不关联");
            resList.add(map);
        }

        for (InterfaceHead head : heads) {
            map = new HashMap<String, Object>();
            map.put("id", head.getHeadId());
            map.put("text", head.getHeadName());
            resList.add(map);
        }
        return resList;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getHeadAll", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Map<String, Object>> getHeadAll(HttpServletRequest request) {
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        List<InterfaceHead> heads = interfaceHeadService.getAll();
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getParameter("query") != null && !"".equals(request.getParameter("query"))) {
            map.put("id", "");
            map.put("text", "全部");
            resList.add(map);
        } else {
            map.put("id", "");
            map.put("text", "不关联");
            resList.add(map);
        }

        for (InterfaceHead head : heads) {
            map = new HashMap<String, Object>();
            map.put("id", head.getHeadId());
            map.put("text", head.getHeadName());
            resList.add(map);
        }
        return resList;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getChecked/{interfaceId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<String> getChecked(@PathVariable String interfaceId) {
        List<String> resList = new ArrayList<String>();
//		Interface head =  interfaceService.getById(interfaceId);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("interfaceId", interfaceId);
        List<InterfaceHeadRelate> heads = interfaceHeadRelateService.findBy(paramMap);
        for (InterfaceHeadRelate h : heads) {
            resList.add(h.getHeadId());
        }
        return resList;
    }

//    @RequiresPermissions({"system-get"})
    @RequiresPermissions({"interface-headRelation"})
    @RequestMapping(method = RequestMethod.GET, value = "/headRelate/{interfaceId}/{headIds}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean headRelate(@PathVariable String interfaceId, @PathVariable String headIds) {
        OperationLog operationLog = systemLogService.record("接口","关联报文头","");
        String logParam = "接口ID：" + interfaceId + "; 报文头:";
        if(StringUtils.isNotEmpty(headIds)){
            String[] hIds = headIds.split("\\,");
            for(int i = 0; i < hIds.length; i++){
                InterfaceHead head = interfaceHeadService.findUniqueBy("headId", hIds[i]);
                if(head != null){
                    logParam += head.getHeadName() + ",";
                }

            }
        }

        Interface entity = interfaceService.findUniqueBy("interfaceId", interfaceId);
        if(entity != null){
            versionServiceImpl.editVersion(entity.getVersionId());
        }

        if (headIds.equals("none")) {
            interfaceHeadRelateService.deleteRelate(interfaceId);
            operationLog.setParams("接口ID：" + interfaceId + ", 取消报文头关联");
            return true;
        }
        if (interfaceId != null && headIds != null) {
            interfaceHeadRelateService.relateSave(interfaceId, headIds);
        }

        operationLog.setParams(logParam.substring(0, logParam.length() - 2));
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"system-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/check/{interfaceId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean check(@PathVariable String interfaceId) {
        Interface inter = interfaceService.findUniqueBy("interfaceId", interfaceId);
        if (inter != null) {
            return true;
        }
        return false;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

    @RequestMapping("/getInterfaceJson")
    @ResponseBody
    public Object getInterfaceJson(String systemId) {
        List<Interface> rows = interfaceService.getBySystemId(systemId);
        return JSONUtil.getInterface().convert(rows, Interface.simpleFields());
    }

//    @RequiresPermissions({"system-update"})
    @RequiresPermissions({"interface-release"})
    @RequestMapping(method = RequestMethod.GET, value = "/release", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean release(String interfaceIds, String versionDesc) {
        OperationLog operationLog = systemLogService.record("接口","发布","接口id："+ interfaceIds + "; 版本描述" + versionDesc);

        boolean result = interfaceService.releaseBatch(interfaceIds, versionDesc);

        systemLogService.updateResult(operationLog);
        return result;
    }
}
