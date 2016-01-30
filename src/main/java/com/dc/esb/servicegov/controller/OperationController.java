package com.dc.esb.servicegov.controller;

import static com.dc.esb.servicegov.service.support.Constants.STATE_PASS;
import static com.dc.esb.servicegov.service.support.Constants.STATE_UNPASS;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.InterfaceInvokeVO;
import com.dc.esb.servicegov.vo.InterfaceInvokeVO2;
import com.dc.esb.servicegov.vo.OperationExpVO;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.JSONUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dc.esb.servicegov.service.support.Constants.STATE_PASS;
import static com.dc.esb.servicegov.service.support.Constants.STATE_UNPASS;

@Controller
@RequestMapping("/operation")
public class OperationController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    private OperationServiceImpl operationServiceImpl;
    @Autowired
    private ServiceServiceImpl serviceService;
    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeService;
    @Autowired
    private SystemServiceImpl systemService;
    @Autowired
    private ExcelExportServiceImpl excelExportService;
    @Autowired
    private ProcessContextServiceImpl processContextService;
    @Autowired
    private VersionServiceImpl versionService;
    @Autowired
    private InterfaceInvokeServiceImpl interfaceInvokeService;

    /**
     * 获取所有的服务场景
     *
     * @return
     */
    @RequiresPermissions({"operation-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getAll() {

        Map<String, Object> result = new HashMap<String, Object>();
        List<Operation> rows = operationServiceImpl.getAll();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    /**
     * 根据服务获取场景列表
     *
     * @param serviceId
     * @return
     */
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/getOperationByServiceId/{serviceId}")
    @ResponseBody
    public Map<String, Object> getOperationByServiceId(@PathVariable(value = "serviceId") String serviceId) {
        Map<String, Object> result = new HashMap<String, Object>();
//        List<Operation> rows = operationServiceImpl.findBy("serviceId", serviceId);
        List<OperationServiceImpl.OperationBean> rows = operationServiceImpl.findOperationBy(serviceId);
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/getByServiceId")
    @ResponseBody
    public List<Operation> getByServiceId(String serviceId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Operation> rows;
        if(StringUtils.isNotEmpty(serviceId)){
            rows = operationServiceImpl.findBy("serviceId", serviceId);
        }else {
            rows = operationServiceImpl.getAll();
        }
        return rows;
    }

    /**
     * 获取服务{serviceId}的待审核场景列表
     *
     * @param serviceId
     * @return
     */
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/getAudits/{serviceId}")
    @ResponseBody
    public Map<String, Object> getAudits(
            @PathVariable(value = "serviceId") String serviceId) {
        List<Operation> rows = operationServiceImpl.getUnAuditOperationByServiceId(serviceId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    /**
     * 这是一个页面跳转控制,根据服务id跳转到场景添加页面
     *
     * @param req
     * @param serviceId
     * @return TODO 建议不要通过Controller控制页面跳转
     */
    @RequiresPermissions({"operation-add"})
    @RequestMapping("/addPage/{serviceId}")
    public ModelAndView addPage(HttpServletRequest req, @PathVariable(value = "serviceId") String serviceId) {
        ModelAndView mv = new ModelAndView("service/operation/add");
        Service service = serviceService.getUniqueByServiceId(serviceId);
        if (service != null) {
            mv.addObject("service", service);
            List<com.dc.esb.servicegov.entity.System> systemList = systemService.getAll();
            mv.addObject("systemList", JSONUtil.getInterface().convert(systemList, com.dc.esb.servicegov.entity.System.simpleFields()));
        }
        return mv;
    }

    /**
     * 场景号唯一性验证
     *
     * @param operationId
     * @param serviceId
     * @return
     */
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String operationId, String serviceId) {
        return operationServiceImpl.uniqueValid(serviceId, operationId);
    }

//    @RequiresPermissions({"service-add"})
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(Operation operation) {
        OperationLog operationLog = systemLogService.record("服务场景","添加","服务ID:" + operation.getServiceId() + "; 场景ID:" + operation.getOperationId() + "; 场景名称:" + operation.getOperationName());
//        if(Integer.parseInt(operation.getOperationId()) < 10){
//            operation.setOperationId("0"+Integer.parseInt(operation.getOperationId()));
//        }
        Map<String,String> map = new HashMap<String, String>();
        map.put("operationId", operation.getOperationId());
        map.put("serviceId",operation.getServiceId());
        List<Operation> list = operationServiceImpl.findBy(map);
        if(list.size() > 0){
            return false;
        }else{
            operationServiceImpl.addOperation(operation);

            systemLogService.updateResult(operationLog);
            return true;
        }
    }
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addInvokeMapping", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addInvokeMapping(@RequestBody List<LinkedHashMap> serviceInvokes) {

        return operationServiceImpl.addInvokeMapping(serviceInvokes);
    }
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.GET, value = "/getInvokeMapping", headers = "Accept=application/json")
    public
    @ResponseBody
    List getInvokeMapping(String serviceId, String operationId) {
        //处理标准数据
        List<InterfaceInvokeVO> standardVOs = excelExportService.getStandardVOList(serviceId, operationId);
        //处理非标准数据
        //TODO 调用关系没有判断
        List<InterfaceInvokeVO> interfaceInvokeVOs = excelExportService.getVOList(serviceId, operationId);
        //去掉没用调用方的记录
        int size = interfaceInvokeVOs.size();
        List<InterfaceInvokeVO> temp = new ArrayList<InterfaceInvokeVO>();
        for (int i = 0; i < size; i++) {
            if(interfaceInvokeVOs.get(i).getConsumerIds().equals("")){
                temp.add(interfaceInvokeVOs.get(i));
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            interfaceInvokeVOs.remove(temp.get(i));
        }
        List<InterfaceInvokeVO> result = new ArrayList<InterfaceInvokeVO>();
        for(int i = 0; i < standardVOs.size(); i++){
            InterfaceInvokeVO iiv = standardVOs.get(i);
            if(iiv != null){
                result.add(iiv);
            }
        }
        for(int i = 0; i < interfaceInvokeVOs.size(); i++){
            InterfaceInvokeVO iiv = interfaceInvokeVOs.get(i);
            if(iiv != null){
                result.add(iiv);
            }
        }
        return result;
    }
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.GET, value = "/getInvokeMapping2", headers = "Accept=application/json")
    public
    @ResponseBody
    List getInvokeMapping2(String serviceId, String operationId) {
        List<InterfaceInvoke> result = interfaceInvokeService.getBySOId(serviceId, operationId);
        if(result != null){
            List<InterfaceInvokeVO2> voList = new ArrayList<InterfaceInvokeVO2>();
            for(InterfaceInvoke interfaceInvoke : result){
                voList.add(new InterfaceInvokeVO2(interfaceInvoke));
            }
            return  voList;
        }
        return null;
    }

    /**
     * TODO 场景基本信息保存后，保存相关的接口映射关系。
     *
     * @param req
     * @param serviceId
     * @param operationId
     * @param consumerStr
     * @param providerStr
     * @return
     */
    @RequiresPermissions({"operation-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/afterAdd", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean afterAdd(HttpServletRequest req, String serviceId, String operationId, String consumerStr, String providerStr) {
//        if(Integer.parseInt(operationId) < 10){
//            operationId = "0"+Integer.parseInt(operationId);
//        }
        return operationServiceImpl.addInvoke(req, serviceId, operationId, consumerStr, providerStr);
    }

    /**
     * 修改场景
     *
     * @param req
     * @param operation
     * @return
     */
//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"operation-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/edit", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean edit(HttpServletRequest req, Operation operation) {
        OperationLog operationLog = systemLogService.record("服务场景","修改","服务ID:" + operation.getServiceId() + "; 场景ID:" + operation.getOperationId() + "; 场景名称:" + operation.getOperationName());
        //只有服务定义和修订状态能修改
        if(!operation.getState().equals(Constants.Operation.OPT_STATE_UNAUDIT) && !operation.getState().equals(Constants.Operation.OPT_STATE_REVISE)){
            return false;
        }
        boolean result = operationServiceImpl.editOperation(req, operation);

        systemLogService.updateResult(operationLog);
        return result;
    }

    /**
     * 根据服务id，场景id跳转到场景编辑页面
     *
     * @param req
     * @param operationId
     * @param serviceId
     * @return
     */
//    @RequiresPermissions({"operation-update"})
    @RequestMapping("/editPage")
    public ModelAndView editPage(HttpServletRequest req, String operationId, String serviceId) {
        ModelAndView mv = new ModelAndView("service/operation/edit");
        //根据operationId查询operation
        Operation operation = operationServiceImpl.getOperation(serviceId, operationId);
        if (operation != null) {
            mv.addObject("operation", operation);
            //根据operation查询service信息
            Service service = serviceService.getById(operation.getServiceId());
            if (service != null) {
                mv.addObject("service", service);
            }
            List<com.dc.esb.servicegov.entity.System> systems = systemService.getAll();

            mv.addObject("systemList", JSONUtil.getInterface().convert(systems, com.dc.esb.servicegov.entity.System.simpleFields()));

            Map<String, String> params = new HashMap<String, String>();
            params.put("serviceId", serviceId);
            params.put("operationId", operationId);

            params.put("type", Constants.INVOKE_TYPE_CONSUMER);
            List<ServiceInvoke> consumerInvokes = serviceInvokeService.findBy(params);
           // mv.addObject("consumerList", JSONUtil.getInterface().convert(consumerInvokes, ServiceInvoke.simpleFields()));

            params.put("type", Constants.INVOKE_TYPE_PROVIDER);
            List<ServiceInvoke> providerInvokes = serviceInvokeService.findBy(params);
            //mv.addObject("providerList", JSONUtil.getInterface().convert(providerInvokes, ServiceInvoke.simpleFields()));

            List<ServiceInvoke> invokeList = new ArrayList<ServiceInvoke>();
            invokeList.addAll(consumerInvokes);
            invokeList.addAll(providerInvokes);
            mv.addObject("invokeList", JSONUtil.getInterface().convert(invokeList, ServiceInvoke.simpleFields()));
        }
        return mv;
    }

    @RequiresPermissions({"operation-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/deletes", headers = "Accept=application/json")
    @ResponseBody
    public boolean deletes(@RequestBody OperationPK[] operationPks) {
        OperationLog operationLog = systemLogService.record("服务场景","批量删除","数量：" + operationPks.length);
        String logParam = "";
        //上线和发布的场景不能删除
        for (int i = 0; i < operationPks.length; i++) {
            Operation operation = operationServiceImpl.getById(operationPks[i]);
            if(Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED.equals(operation.getState()) || Constants.Operation.LIFE_CYCLE_STATE_ONLINE.equals(operation.getState())){
                return false;
            }
            logParam += ", [服务ID：" + operation.getServiceId() + ", 场景ID：" + operation.getOperationId() + ", 场景名称:" + operation.getOperationName() + "]";
        }

        operationServiceImpl.deleteOperations(operationPks);

        operationLog.setParams(logParam.substring(1, logParam.length() -1 ));
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"operation-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getServiseById/{value}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Operation> getServiseById(@PathVariable String value) {
        String name = "serviceId";
        Map<String, String> params = new HashMap<String, String>();
        List<Operation> info = operationServiceImpl.findBy(params);
        return info;
    }

//    @RequiresPermissions({"service-get"})
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/detailPage")
    public ModelAndView detailPage(HttpServletRequest req, String operationId, String serviceId) {
        return operationServiceImpl.detailPage(req, operationId, serviceId);
    }

    @RequiresPermissions({"version-add"})
    @RequestMapping("/release")
    public ModelAndView release(HttpServletRequest req, String operationId, String serviceId, String versionDesc) {
        OperationLog operationLog = systemLogService.record("服务场景","发布","服务ID:" + serviceId + "; 场景ID:" + operationId + "; 版本描述:" + versionDesc);

        operationServiceImpl.release(operationId, serviceId, versionDesc);
        ModelAndView result = detailPage(req, operationId, serviceId);

        systemLogService.updateResult(operationLog);
        return result;
    }

    @RequiresPermissions({"version-add"})
    @RequestMapping("/releaseBatch")
    @ResponseBody
    public boolean releaseBatch(@RequestBody Operation[] operations) {
        OperationLog operationLog = systemLogService.record("服务场景","版本发布","发布数量：" + operations.length);

        boolean result = operationServiceImpl.releaseBatch(operations);

        systemLogService.updateResult(operationLog);
        return result;
    }

    @RequiresPermissions({"operation-update"})
    @RequestMapping("/auditPage")
    public ModelAndView auditPage(HttpServletRequest req, String serviceId) {
        ModelAndView mv = new ModelAndView("service/operation/audit");
        //根据serviceId查询service信息
        Service service = serviceService.getUniqueByServiceId(serviceId);
        if (service != null) {
            mv.addObject("service", service);
        }
        return mv;
    }

    @RequiresPermissions({"version-check"})
    @RequestMapping(method = RequestMethod.POST, value = "/auditSave", headers = "Accept=application/json")
    @ResponseBody
    public boolean auditSave(String state , String auditRemark, @RequestBody String[] operationIds) throws  Throwable{
        OperationLog operationLog = systemLogService.record("服务场景","审核","");

        String logParam = operationServiceImpl.auditOperation(state, auditRemark, operationIds);

        operationLog.setParams("审核结果:" + Constants.Operation.getStateName(state) + "; 审核备注：" + auditRemark + ";场景：" + logParam );
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"version-check"})
    @RequestMapping(method = RequestMethod.POST, value = "/auditSave/{processId}", headers = "Accept=application/json")
    @ResponseBody
    public boolean auditSaveWithProcess(String state , String auditRemark, @RequestBody String[] operationIds, @PathVariable("processId") String processId) throws  Throwable{
        OperationLog operationLog = systemLogService.record("服务场景","审核(任务)","场景数量：" + operationIds.length + "； 审核结果:" + Constants.Operation.getStateName(state) + "; 审核备注：" + auditRemark);

        String optUser = SecurityUtils.getSubject().getPrincipal().toString();
        String optDate = DateUtils.format(new Date());
        String logParam = operationServiceImpl.auditOperation(state, auditRemark, operationIds);
        for(String serviceOperationIdPair : operationIds){
            String[] per = serviceOperationIdPair.split(",");
            String operationId = per[0];
            String serviceId = per[1];
            com.dc.esb.servicegov.entity.ProcessContext processContext = new com.dc.esb.servicegov.entity.ProcessContext();
            processContext.setName("服务审核");
            processContext.setProcessId(processId);
            processContext.setKey("operation");
            processContext.setValue(serviceId + operationId);
            processContext.setType("result");
            processContext.setRemark("添加审核场景[" + serviceId + operationId + "]");
            processContext.setOptDate(optDate);
            processContext.setOptUser(optUser);
            processContextService.save(processContext);
        }
        systemLogService.updateResult(operationLog);

        operationLog.setParams("审核结果:" + Constants.Operation.getStateName(state) + "; 审核备注：" + auditRemark + ";场景：" + logParam );
        systemLogService.updateResult(operationLog);
        return true;
    }


    // 根据系统id查询该系统过是有接口
    @RequiresPermissions({"operation-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/judgeInterface", headers = "Accept=application/json")
    @ResponseBody
    public boolean judgeInterface(String systemId,String type) {
        boolean result = systemService.containsInterface(systemId, type);
        return result;
    }

    @RequiresPermissions({"operation-get"})
    @RequestMapping("/interfacePage")
    public ModelAndView interfacePage(String operationId, String serviceId, HttpServletRequest req) {
        ModelAndView mv = new ModelAndView("service/operation/interfacePage");
        // 根据serviceId获取service信息
        if (StringUtils.isNotEmpty(serviceId)) {
            Service service = serviceService.getById(serviceId);
            if (service != null) {
                mv.addObject("service", service);
            }
            if (StringUtils.isNotEmpty(operationId)) {
                // 根据serviceId,operationId获取operation信息
                Operation operation = operationServiceImpl.getOperation(serviceId, operationId);
                if (operation != null) {
                    mv.addObject("operation", operation);
                    List<?> systemList = systemService.getAll();
                    mv.addObject("systemList", systemList);
                }
            }
        }
        return mv;
	}
    /**
     * 判断元数据是否被服务场景引用
     * @param metadataId
     * @return
     */
//    @RequiresPermissions({"service-get"})
    @RequestMapping("/judgeByMetadataId/{metadataId}")
    @ResponseBody
    public boolean judgeByMetadataId(@PathVariable(value = "metadataId") String metadataId){
        return operationServiceImpl.judgeByMetadataId(metadataId);
    }
    /**
     * 根据元数据ID查询场景列表
     * @param metadataId
     * @return
     */
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/getByMetadataId/{metadataId}")
    @ResponseBody
    public List<TreeNode> getByMetadataId(@PathVariable(value = "metadataId") String metadataId){
        return operationServiceImpl.getTreeByMetadataId(metadataId);
    }
    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

//    @RequiresPermissions({"service-get"})
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/query")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest req) throws  Throwable{
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(operationServiceImpl.queryCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
        List<OperationExpVO> rows = operationServiceImpl.queryByCondition(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }


    /**
     * 提交审核，变为待审核状态
     * @param list
     * @return
     * @throws Throwable
     */
//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"operation-commit"})
    @RequestMapping(method = RequestMethod.POST, value = "/submitToAudit", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean submitToAudit(@RequestBody List list) throws  Throwable{
        OperationLog operationLog = systemLogService.record("服务场景", "提交审核", "");
        String logParam = "";
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)list.get(i);
            String serviceId = map.get("serviceId").toString();
            String operationId = map.get("operationId").toString();
            Map<String,String> params = new HashMap<String, String>();
            params.put("serviceId",serviceId);
            params.put("operationId",operationId);
            Operation operation = operationServiceImpl.findUniqueBy(params);
            if(operation.getState().equals(Constants.Operation.OPT_STATE_UNAUDIT) || operation.getState().equals(Constants.Operation.OPT_STATE_REVISE)){
                operation.setState(Constants.Operation.OPT_STATE_REQUIRE_UNAUDIT);
                operationServiceImpl.save(operation);
            }

            logParam += ", [服务ID：" + operation.getServiceId() + ", 场景ID：" + operation.getOperationId() + ", 场景名称:" + operation.getOperationName() + "]";
        }

        operationLog.setParams(logParam.substring(1, logParam.length() - 1));
        systemLogService.updateResult(operationLog);
        return true;
    }

    /**
     * 修订，变为修订状态  只有审核通过，已上线，已发布状态的服务才能进行修订
     * @param list
     * @return
     * @throws Throwable
     */
//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"operation-revise"})
    @RequestMapping(method = RequestMethod.POST, value = "/revise", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean revise(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("服务场景","修订","");
        String logParam = "场景：";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)list.get(i);
            String serviceId = map.get("serviceId").toString();
            String operationId = map.get("operationId").toString();
            Map<String,String> params = new HashMap<String, String>();
            params.put("serviceId",serviceId);
            params.put("operationId",operationId);
            Operation operation = operationServiceImpl.findUniqueBy(params);
            boolean canRevise = operationServiceImpl.judgeCanRevise(operation);
            if(canRevise){
                operation.setState(Constants.Operation.OPT_STATE_REVISE);
                operationServiceImpl.save(operation);
                //将版本状态改为‘修改’
                Version version = operation.getVersion();
                if(version != null){
                    version.setOptType(Constants.Version.OPT_TYPE_EDIT);
                    versionService.save(version);
                }

            }else {
                return false;
            }

            logParam += "[服务ID:" + serviceId + ", 场景ID:" + operationId + "],";
        }

        operationLog.setParams(logParam.substring(0, logParam.length() -2 ));
        systemLogService.updateResult(operationLog);
        return true;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/drop", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean drop(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("服务场景","废弃","");
        String logParam = "场景：";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)list.get(i);
            String serviceId = map.get("serviceId").toString();
            String operationId = map.get("operationId").toString();
            Map<String,String> params = new HashMap<String, String>();
            params.put("serviceId",serviceId);
            params.put("operationId",operationId);
            Operation operation = operationServiceImpl.findUniqueBy(params);
            boolean canRevise = true;
            if(canRevise){
                operation.setState(Constants.Operation.OPT_STATE_ABANDONED);
                operationServiceImpl.save(operation);
                //将版本状态改为‘废弃’
//                Version version = operation.getVersion();
//                if(version != null){
//                    version.setOptType(Constants.Version.STATE_DROPPED);
//                    versionService.save(version);
//                }
            }else {
                return false;
            }

            logParam += "[服务ID:" + serviceId + ", 场景ID:" + operationId + "],";
        }

        operationLog.setParams(logParam.substring(0, logParam.length() -2 ));
        systemLogService.updateResult(operationLog);
        return true;
    }
    /**
     * 下线，变为下线状态  只有已上线状态的服务才能进行下线
     * @param list
     * @return
     * @throws Throwable
     */
//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"operation-revise"})
    @RequestMapping(method = RequestMethod.POST, value = "/quit", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean quit(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("服务场景","下线","");
        String logParam = "场景：";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)list.get(i);
            String serviceId = map.get("serviceId").toString();
            String operationId = map.get("operationId").toString();
            Map<String,String> params = new HashMap<String, String>();
            params.put("serviceId",serviceId);
            params.put("operationId",operationId);
            Operation operation = operationServiceImpl.findUniqueBy(params);
            boolean canQuit = operationServiceImpl.judgeCanQuit(operation);
            if(canQuit){
                operation.setState(Constants.Operation.OPT_STATE_QUIT);
                operationServiceImpl.save(operation);
            }else {
                return false;
            }

            logParam += "[服务ID:" + serviceId + ", 场景ID:" + operationId + "],";
        }

        operationLog.setParams(logParam.substring(0, logParam.length() -2 ));
        systemLogService.updateResult(operationLog);
        return true;
    }

    /**
     * 根据接口ID查询场景列表
     * @param interfaceId
     * @return
     */
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/getByInterfaceId/{interfaceId}")
    @ResponseBody
    public List<TreeNode> getByInterfaceId(@PathVariable(value = "interfaceId") String interfaceId){
        return operationServiceImpl.getByInterfaceId(interfaceId);
    }
    @RequiresPermissions({"operation-get"})
    @RequestMapping("/updateable")
    @ResponseBody
    public boolean updateable(String serviceId, String operationId){
        Map<String,String> params = new HashMap<String, String>();
        params.put("serviceId",serviceId);
        params.put("operationId",operationId);
        Operation operation = operationServiceImpl.findUniqueBy(params);
        //只有服务定义和修订状态能修改
        if(operation.getState().equals(Constants.Operation.OPT_STATE_UNAUDIT) || operation.getState().equals(Constants.Operation.OPT_STATE_REVISE)){
            return true;
        }
        return false;
    }
}
