package com.dc.esb.servicegov.controller;

import java.net.URLDecoder;
import java.util.*;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.jsonObj.ServiceInvokeJson;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.JSONUtil;
import com.dc.esb.servicegov.vo.ServiceInvokeInfoVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dc.esb.servicegov.vo.ServiceInvokeViewBean;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vincentfxz on 15/7/8.
 */
@Controller
@RequestMapping("/serviceLink")
public class ServiceInvokeController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeService;
    @Autowired
    private InterfaceInvokeServiceImpl interfaceInvokeService;
    @Autowired
    private OperationServiceImpl operationService;
    @Autowired
    private InterfaceServiceImpl interfaceService;

    @RequiresPermissions({"service-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/list", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceInvokeViewBean> getAllTreeBean() {
        List<ServiceInvokeViewBean>  serviceInvokeViewBeans = new ArrayList<ServiceInvokeViewBean>();
        List<ServiceInvoke> serviceInvokes =  serviceInvokeService.getAll();
        for(ServiceInvoke serviceInvoke : serviceInvokes){
            ServiceInvokeViewBean serviceInvokeViewBean = new ServiceInvokeViewBean(serviceInvoke);
            serviceInvokeViewBeans.add(serviceInvokeViewBean);
        }
        return serviceInvokeViewBeans;
    }

//根据系统id查询接口列表

    /**
     * @param systemId
     * @return
     */
    @RequiresPermissions({"service-get"})
    @RequestMapping("/getInterface")
    @ResponseBody
    public Map<String, Object> getInterface(String systemId, String type, String text,HttpServletRequest req) throws  Throwable{
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        String hql = " from " + ServiceInvoke.class.getName() +" as si where si.systemId='"+systemId+"' and type = '"+type	+"'";
        if(StringUtils.isNotEmpty(text)){
            text = URLDecoder.decode(text, "utf-8");
            hql += " and( si.interfaceId like '%" + text + "%' or si.inter.interfaceName like '%" + text + "%') ";
        }

        Page page = serviceInvokeService.getPageBy(hql,rowCount);
        page.setPage(pageNo);
        List<ServiceInvokeJson> rows = serviceInvokeService.getDistinctInterBy(hql, page);
//        List<ServiceInvokeJson> rows = serviceInvokeService.getDistinctInter(systemId, type,text);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping("/getInterface2")
    @ResponseBody
    public Map<String, Object> getInterface2(String systemId, String type, String text,HttpServletRequest req) throws  Throwable{//根据系统id查询接口
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        List<ServiceInvokeJson>  rows = new ArrayList<ServiceInvokeJson>();

        String hql = "select distinct t1 from Interface t1, ServiceInvoke t2 where t1.interfaceId=t2.interfaceId and t2.systemId = ? and type = ?";
        if(StringUtils.isNotEmpty(text)){
            text = URLDecoder.decode(text, "utf-8");
            hql += " and( t1.interfaceId like '%" + text + "%' or t1.interfaceName like '%" + text + "%') ";
        }

        Page page = interfaceService.getPageBy(hql,rowCount, systemId, type);
        page.setPage(pageNo);
        List<Interface> inters = interfaceService.find(hql, systemId, type);
        if(inters != null && inters.size() > 0){
            for(int i = 0; i < inters.size(); i++){
                Interface inter = inters.get(i);
                String hql2 = " from ServiceInvoke where interfaceId = ? and systemId = ? and type = ? ";
                List<ServiceInvoke> serviceInvokes = serviceInvokeService.find(hql2, inter.getInterfaceId(), systemId, type);
                if(serviceInvokes != null && serviceInvokes.size() > 0){
                    ServiceInvokeJson serviceInvokeJson = new ServiceInvokeJson(serviceInvokes.get(0));
                    serviceInvokeJson.setInvokeId(UUID.randomUUID().toString());
                    rows.add(serviceInvokeJson);
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    /**
     * @param serviceId
     * @param operationId
     * @param systemId
     * @return
     */
    @RequiresPermissions({"service-get"})
    @RequestMapping("/getInterfaceByOSS")
    @ResponseBody
    public Map<String, Object> getInterfaceByOSS(String serviceId, String operationId, String systemId) {
        List<?> rows = serviceInvokeService.findJsonBySO(serviceId, operationId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"invoke-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/deleteInvoke", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteInvoke(@RequestBody LinkedHashMap map) {
        OperationLog operationLog = systemLogService.record("映射关系","删除","");
        String serviceId = map.get("serviceId").toString();
        String operationId = map.get("operationId").toString();
        String[] consumers = map.get("consumers").toString().replaceAll("，", ",").split(",");
        String[] providers = map.get("providers").toString().replaceAll("，",",").split(",");
        String interfaceId ="";
        if(null != map.get("interfaceId")){
            interfaceId = map.get("interfaceId").toString();
        }

        String[] consumerIds = map.get("consumerIds").toString().replaceAll("，", ",").split(",");
        String[] providerIds = map.get("providerIds").toString().replaceAll("，",",").split(",");

        for (int i = 0; i < providerIds.length; i++) {
            Map<String,String> params = new HashMap<String, String>();
            params.put("serviceId",serviceId);
            params.put("systemId",providerIds[i]);
            params.put("operationId",operationId);
            params.put("type",Constants.INVOKE_TYPE_PROVIDER);
            List<ServiceInvoke> providerInvokes = serviceInvokeService.findBy(params);
            ServiceInvoke provider = null;
            //map里面interfaceId=null怎么查
            if("".equals(interfaceId)){
                for (ServiceInvoke p : providerInvokes){
                    if(null == p.getInterfaceId()){
                        provider =p;
                    }
                }
            }else {
                for (ServiceInvoke p : providerInvokes){
                    if(null != p.getInterfaceId() && p.getInterfaceId().equals(interfaceId)){
                        provider =p;
                    }
                }
            }
            if(null == provider) return false;
            for (int j = 0; j < consumerIds.length; j++) {
                Map<String,String> params2 = new HashMap<String, String>();
                params2.put("serviceId",serviceId);
                params2.put("systemId",consumerIds[j]);
                params2.put("operationId",operationId);
                params2.put("type",Constants.INVOKE_TYPE_CONSUMER);
                ServiceInvoke consumer = serviceInvokeService.findUniqueBy(params2);
                if(null == consumer) return false;

                //删除interfaceInvoke
                Map<String,String> map2 = new HashMap<String, String>();
                map2.put("providerInvokeId",provider.getInvokeId());
                map2.put("consumerInvokeId",consumer.getInvokeId());
                InterfaceInvoke invoke = interfaceInvokeService.findUniqueBy(map2);
                if(null == invoke) return false;
                interfaceInvokeService.delete(invoke);
                //不能直接删除serviceInvoke ，看看是否有相同的没场景信息的接口
                //删除消费方接口
                if(consumer.getInterfaceId() == null){
                    //标准的消费方直接删除
                    try{
                        serviceInvokeService.delete(consumer);
                    }catch (Exception e){
                        //约束
                        e.printStackTrace();
                    }

                }else{
                    Map<String,String> params3 = new HashMap<String, String>();
                    params3.put("interfaceId",consumer.getInterfaceId());
                    params3.put("serviceId",serviceId);
                    params3.put("type",Constants.INVOKE_TYPE_CONSUMER);
                    List<ServiceInvoke> tempConsumers = serviceInvokeService.findBy(params3);
                    if(tempConsumers.size()>1){
                        //超过一条删除
                        serviceInvokeService.delete(consumer);
                    }else{
                        consumer.setServiceId(null);
                        consumer.setOperationId(null);
                    }
                }
                //删除提供方接口
                if(consumer.getInterfaceId() == null){
                    serviceInvokeService.delete(provider);
                }else{
                    Map<String,String> params3 = new HashMap<String, String>();
                    params3.put("interfaceId",provider.getInterfaceId());
                    params3.put("serviceId",serviceId);
                    params3.put("type",Constants.INVOKE_TYPE_PROVIDER);
                    List<ServiceInvoke> tempProvider = serviceInvokeService.findBy(params3);
                    if(tempProvider.size()>1){
                        //超过一条删除
                        serviceInvokeService.delete(consumer);
                    }else{
                        provider.setServiceId(null);
                        provider.setOperationId(null);
                    }
                }
            }
        }

        operationLog.setParams("服务ID:" + serviceId + "; 场景ID:" + operationId);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"invoke-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/deleteInvoke2", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteInvoke2(String id) {
        OperationLog operationLog = systemLogService.record("映射关系", "删除", "");
        String serviceId = "";
        String operationId = "";

        InterfaceInvoke interfaceInvoke = interfaceInvokeService.findUniqueBy("id", id);
        if(interfaceInvoke != null){
            ServiceInvoke consumer = interfaceInvoke.getConsumer();
            ServiceInvoke provider = interfaceInvoke.getProvider();
            interfaceInvokeService.delete(interfaceInvoke);

            String hql = " from InterfaceInvoke where consumerInvokeId = ?";
            List<ServiceInvoke> cList = serviceInvokeService.find(hql, consumer.getInvokeId());
            if(null == cList || cList.size() == 0){
                serviceInvokeService.delete(consumer);
            }

            String hql2 = " from InterfaceInvoke where providerInvokeId = ?";
            List<ServiceInvoke> pList = serviceInvokeService.find(hql2, provider.getInvokeId());
            if(null == pList || pList.size() == 0){
                serviceInvokeService.delete(provider);
            }
        }

        operationLog.setParams("服务ID:" + serviceId + "; 场景ID:" + operationId);
        systemLogService.updateResult(operationLog);

        return true;
    }

//    @RequiresPermissions({"service-update"})
    @RequiresPermissions({"invoke-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addServiceLink", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addServiceLink(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("映射关系","添加","");
        String logParam = "";

        String serviceId ="";
        String operationId = "";

        List consumers = (ArrayList)list.get(0);
        List providers = (ArrayList)list.get(1);
        List<ServiceInvoke> temp = new ArrayList<ServiceInvoke>();
        Operation operation = null;
        for (int i = 0; i < providers.size(); i++) {
            for (int j = 0; j < consumers.size(); j++) {
                LinkedHashMap<String, Object> mapConsumer = (LinkedHashMap)consumers.get(j);
                LinkedHashMap<String, Object> mapProvider = (LinkedHashMap)providers.get(i);
//                Long l = (Long)mapConsumer.get("invokeId");
//                String invokeId = "" + l;
                String invokeId = "" + mapConsumer.get("invokeId").toString();
                String systemId = mapConsumer.get("systemId").toString();
                String systemChineseName = mapConsumer.get("systemChineseName").toString();
                String interfaceId = mapConsumer.get("interfaceId").toString();
                String interfaceName = mapConsumer.get("interfaceName").toString();
                String type = mapConsumer.get("type").toString();
                String isStandard = mapConsumer.get("isStandard").toString();
                serviceId =mapConsumer.get("serviceId").toString();
                operationId =mapConsumer.get("operationId").toString();
                operation = operationService.getOperation(serviceId,operationId);
                ServiceInvoke c;
                ServiceInvoke c2 = serviceInvokeService.getById(invokeId);
                if(null != c2){
                    c = c2;
                }else{
                    //是否已经存在标准消费方
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("isStandard",isStandard);
                    params.put("operationId",operationId);
                    params.put("serviceId",serviceId);
                    params.put("systemId",systemId);
                    List<ServiceInvoke> list1 = serviceInvokeService.findBy(params);
                    if(list1.size()>0){
                        c = list1.get(0);
                    }else{
                        c = new ServiceInvoke();
                        c.setInvokeId(invokeId);
                        c.setSystemId(systemId);
                        if(StringUtils.isNotEmpty(interfaceId)){
                            c.setInterfaceId(interfaceId);
                        }
                        c.setType(type);
                        c.setIsStandard(isStandard);
                        c.setServiceId(serviceId);
                        c.setOperationId(operationId);
                        serviceInvokeService.insert(c);
                    }
                }
                String invokeId2 = mapProvider.get("invokeId").toString();
                String systemId2 = mapProvider.get("systemId").toString();
                String systemChineseName2 = mapProvider.get("systemChineseName").toString();
                String interfaceId2 = mapProvider.get("interfaceId").toString();
                /*String interfaceName2 = "";
                if(null != mapProvider.get("interfaceName")){
                    interfaceName2 = mapProvider.get("interfaceName").toString();
                }*/
                String type2 = mapProvider.get("type").toString();
                String isStandard2 = mapProvider.get("isStandard").toString();
                //是否已经存在提供方
                Map<String,String> params = new HashMap<String, String>();
                params.put("operationId",operationId);
                params.put("serviceId",serviceId);
                params.put("systemId",systemId2);
                params.put("interfaceId",interfaceId2);
                params.put("isStandard",isStandard2);

                List<ServiceInvoke> list1 = serviceInvokeService.findBy(params);
                ServiceInvoke p;
                if(list1.size()>0){
                    p = list1.get(0);
                }else{

                    //TODO 怎么判断是否标准
                    p = new ServiceInvoke();
                    p.setIsStandard(isStandard2);
                    if(StringUtils.isNotEmpty(interfaceId2)){
                        p.setInterfaceId(interfaceId2);
                    }
                    p.setOperationId(operationId);
                    p.setServiceId(serviceId);
                    p.setSystemId(systemId2);
                    p.setType(Constants.INVOKE_TYPE_PROVIDER);
//                    //判断是否本次重复
//                    int index =  temp.indexOf(p);
//                    if(index >= 0){
//                        p = temp.get(index);
//                    }else{
//                        temp.add(p);
//                        serviceInvokeService.insert(p);
//                    }
                    serviceInvokeService.insert(p);

                }
                //查看是否存在调用关系
                Map<String,String> map = new HashMap<String, String>();
                map.put("providerInvokeId",p.getInvokeId());
                map.put("consumerInvokeId",c.getInvokeId());
                List<InterfaceInvoke> list3 = interfaceInvokeService.findBy(map);
                if(list3.size()>0) continue;
                //配置调用关系
                InterfaceInvoke interfaceInvoke = new InterfaceInvoke();
                interfaceInvoke.setProviderInvokeId(p.getInvokeId());
                interfaceInvoke.setConsumerInvokeId(c.getInvokeId());
                interfaceInvokeService.insert(interfaceInvoke);
            }
        }
        if(null != operation){
            operationService.editOperation(null, operation);
        }

        logParam += "服务ID：" + serviceId + ", 场景ID:" + operationId + ", 消费者提供者关系：数量:" + list.size();
        operationLog.setParams(logParam);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"invoke-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addServiceLink2", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addServiceLink2(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("映射关系", "添加", "");
        String logParam = "";

        String serviceId ="";
        String operationId = "";

        List consumers = (ArrayList)list.get(0);
        List providers = (ArrayList)list.get(1);
        List<ServiceInvoke> temp = new ArrayList<ServiceInvoke>();
        Operation operation = null;

        for (int i = 0; i < providers.size(); i++) {
            LinkedHashMap<String, Object> mapProvider = (LinkedHashMap) providers.get(i);
            ServiceInvoke p = serviceInvokeService.genderServiceInvoke(mapProvider);
            operation = operationService.getOperation(p.getServiceId(), p.getOperationId());
            serviceId = p.getServiceId();
            operationId = p.getOperationId();

            for (int j = 0; j < consumers.size(); j++) {

                LinkedHashMap<String, Object> mapConsumer = (LinkedHashMap) consumers.get(j);
                ServiceInvoke c = serviceInvokeService.genderServiceInvoke(mapConsumer);

                InterfaceInvoke interfaceInvoke = new InterfaceInvoke();
                interfaceInvoke.setProviderInvokeId(p.getInvokeId());
                interfaceInvoke.setConsumerInvokeId(c.getInvokeId());
                interfaceInvokeService.insert(interfaceInvoke);
            }
        }
        if(null != operation){
            operationService.editReleate(serviceId, operationId);
        }

        logParam += "服务ID：" + serviceId + ", 场景ID:" + operationId + ", 消费者提供者关系：数量:" + list.size();
        operationLog.setParams(logParam);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping("/contionOperation")
    @ResponseBody
    public boolean contionOperation(String interfaceId) {
        boolean result = serviceInvokeService.containOperation(interfaceId);
        return result;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
