package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.EnumServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/enum")
public class EnumController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private EnumServiceImpl enumService;

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addEnum", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addEnum(@RequestBody SGEnum anEnum) {
        OperationLog operationLog = systemLogService.record("公共代码","添加","代码名称"+ anEnum.getName());

        anEnum.setOptDate(DateUtils.format(new Date()));
        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        anEnum.setOptUser(userId);
        enumService.insertEnum(anEnum);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addSlaveEnum/{masterId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addSlaveEnum(@RequestBody SGEnum anEnum, @PathVariable(value = "masterId") String masterId) {
        OperationLog operationLog = systemLogService.record("公共代码","添加从代码","代码名称"+ anEnum.getName());

        anEnum.setOptDate(DateUtils.format(new Date()));
        anEnum.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
        enumService.insertEnum(anEnum);
        String slaveId = anEnum.getId();
        MasterSlaveEnumMap mapping = new MasterSlaveEnumMap();
        mapping.setMasterId(masterId);
        mapping.setSlaveId(slaveId);
        enumService.addMasterSlaveEnumMap(mapping);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/searchEnum", headers = "Accept=application/json")
    public
    @ResponseBody
    List<SGEnum> searchEnum(@RequestBody HashMap<String, String> map) {
        List<SGEnum> list = enumService.getEnumByParams(map);
        return list;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    HashMap<String, Object> getAll(HttpServletRequest req) {
        int pageNum = Integer.parseInt(req.getParameter("page"));
        int rows = Integer.parseInt(req.getParameter("rows"));
        String name = req.getParameter("name");
        String isStandard = req.getParameter("isStandard");
        String isMaster = req.getParameter("isMaster");
        String dataSource = req.getParameter("dataSource");
        String status = req.getParameter("status");
        String version = req.getParameter("version");
        String remark = req.getParameter("remark");

        StringBuffer hql = new StringBuffer("select sge from SGEnum sge");
        hql.append(" where 1=1 and isMaster='1'");
        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();
        if (name != null && !name.equals("")) {
            hql.append(" and name like ?");
            searchConds.add(new SearchCondition("name", "%" + name + "%"));
        }
        if (isStandard != null && !isStandard.equals("")) {
            hql.append(" and isStandard like ?");
            searchConds.add(new SearchCondition("isStandard", "%" + isStandard + "%"));
        }
        if (isMaster != null && !isMaster.equals("")) {
            hql.append(" and isMaster like ?");
            searchConds.add(new SearchCondition("isMaster", "%" + isMaster + "%"));
        }
        if (dataSource != null && !dataSource.equals("")) {
            hql.append(" and dataSource like ?");
            searchConds.add(new SearchCondition("dataSource", "%" + dataSource + "%"));
        }
        if (status != null && !status.equals("")) {
            hql.append(" and status like ?");
            searchConds.add(new SearchCondition("status", "%" + status + "%"));
        }
        if (version != null && !version.equals("")) {
            hql.append(" and version like ?");
            searchConds.add(new SearchCondition("version", "%" + version + "%"));
        }
        if (remark != null && !remark.equals("")) {
            hql.append(" and remark like ?");
            searchConds.add(new SearchCondition("remark", "%" + remark + "%"));
        }
        return enumService.getAllEnum(hql.toString(), searchConds, pageNum, rows);


    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getMasterElements/{masterId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<EnumElements> getMasterElements(@PathVariable(value = "masterId") String masterId) {
        return enumService.getElementsByEnumId(masterId);
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByEnumId/{id}/{isMaster}/{aSwitch}", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView getByEnumId(@PathVariable(value = "id") String id, @PathVariable(value = "isMaster") String isMaster
            , @PathVariable(value = "aSwitch") String aSwitch) {
        SGEnum master = enumService.getByEnumId(id);
        List<SGEnum> slave = enumService.getSlaveByEnumId(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("master", master);
        modelAndView.addObject("slave", slave);
        //公共代码
        if (aSwitch.equals("1")) {
            modelAndView.setViewName("SGEnum/maintainGGEnum");
        } else if (isMaster.equals("1")) {
            modelAndView.setViewName("SGEnum/maintainEnum");
        } else if (isMaster.equals("0")) {
            modelAndView.setViewName("SGEnum/maintainSlaveEnum");
        }
        return modelAndView;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/checkByEnumId/{id}/{isMaster}/{aSwitch}/{processId}", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView checkByEnumId(@PathVariable(value = "id") String id, @PathVariable(value = "isMaster") String isMaster
            , @PathVariable(value = "aSwitch") String aSwitch, @PathVariable(value = "processId") String processId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("processId", processId);
        params.put("id", id);
        SGEnum master = enumService.findBy(params).get(0);
        List<SGEnum> slave = enumService.checkSlaveByEnumId(id, processId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("master", master);
        modelAndView.addObject("slave", slave);
        //公共代码
        if (aSwitch.equals("1")) {
            modelAndView.setViewName("SGEnum/taskCheck/maintainGGEnum");
        } else if (isMaster.equals("1")) {
            modelAndView.setViewName("SGEnum/taskCheck/maintainEnum");
        } else if (isMaster.equals("0")) {
            modelAndView.setViewName("SGEnum/taskCheck/maintainSlaveEnum");
        }
        return modelAndView;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getElementMapping/{masterId}/{slaveId}", headers = "Accept=application/json")
    public
    @ResponseBody
    HashMap<String, Object> getElementMapping(@PathVariable(value = "masterId") String masterId, @PathVariable(value = "slaveId") String slaveId,HttpServletRequest req) {
        int pageNum = Integer.parseInt(req.getParameter("page"));
        int rows = Integer.parseInt(req.getParameter("rows"));
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT m.REMARK as remark, m.element_name as masterName,s.element_name slaveName,m.element_id as masterId,s.element_id as slaveId");
//        sql.append("SELECT m.REMARK as masterName,s.REMARK slaveName,m.element_id as masterId,s.element_id as slaveId");
        sql.append(",mp.direction as direction,mp.mapping_relation as mappingRelation");
        sql.append(" FROM ENUM_ELEMENTS m left join");
        sql.append(" (select * from ENUM_ELEMENT_MAP t1 where t1.slave_element_id");
        sql.append(" in(select t2.element_id from ENUM_ELEMENTS t2 where t2.enum_id='" + slaveId + "')) mp");
        sql.append(" ON m.element_id = mp.master_element_id");
        sql.append(" left join ENUM_ELEMENTS s");
        sql.append(" ON mp.slave_element_id = s.element_id");
        sql.append(" WHERE m.enum_id='" + masterId + "'");

//        List list = enumService.getElementsMapping(sql);
//        List list = enumService.getElementsMappingList(sql.toString(), pageNum, rows);
        return enumService.getElementsMapping(sql.toString(), pageNum, rows);
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getElementMappingSToM/{masterId}/{slaveId}", headers = "Accept=application/json")
    public
    @ResponseBody
    HashMap<String, Object> getElementMappingSToM(@PathVariable(value = "masterId") String masterId, @PathVariable(value = "slaveId") String slaveId,HttpServletRequest req) {
        int pageNum = Integer.parseInt(req.getParameter("page"));
        int rows = Integer.parseInt(req.getParameter("rows"));
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT m.REMARK as remark, m.element_name as masterName,s.element_name slaveName,m.element_id as masterId,s.element_id as slaveId");
//        sql.append("SELECT m.REMARK as masterName,s.REMARK slaveName,m.element_id as masterId,s.element_id as slaveId");
        sql.append(",mp.direction as direction,mp.mapping_relation as mappingRelation");
        sql.append(" FROM ENUM_ELEMENTS s left join");
        sql.append(" (select * from ENUM_ELEMENT_MAP t1 where t1.master_element_id");
        sql.append(" in(select t2.element_id from ENUM_ELEMENTS t2 where t2.enum_id='" + masterId + "')) mp");
        sql.append(" ON s.element_id = mp.slave_element_id");
        sql.append(" left join ENUM_ELEMENTS m");
        sql.append(" ON mp.master_element_id = m.element_id");
        sql.append(" WHERE s.enum_id='" + slaveId + "'");

//        List list = enumService.getElementsMapping(sql);
//        List list = enumService.getElementsMappingList(sql.toString(),pageNum,rows);
        return enumService.getElementsMapping(sql.toString(),pageNum,rows);
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getMappingBySlaveId/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    ModelAndView getMappingBySlaveId(@PathVariable(value = "id") String id) {
        SGEnum slave = enumService.getByEnumId(id);
        SGEnum master = enumService.getMasterBySlaveId(slave.getId());
        List<EnumElements> masterElements = enumService.getElementsByEnumId(master.getId());
        List<EnumElements> slaveElements = enumService.getElementsByEnumId(slave.getId());
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT m.element_name as masterName,s.element_name slaveName");
        sql.append(" FROM ENUM_ELEMENTS m left join ENUM_ELEMENT_MAP mp");
        sql.append(" ON m.element_id = mp.master_element_id");
        sql.append(" left join ENUM_ELEMENTS s");
        sql.append(" ON mp.slave_element_id = s.element_id");
        sql.append(" WHERE m.enum_id='" + master.getId().toString() + "'");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("master", master);
        modelAndView.addObject("slave", slave);
        modelAndView.addObject("masterElements", masterElements);
        modelAndView.addObject("slaveElements", slaveElements);
//		modelAndView.addObject("mappingData", hashMap);
        modelAndView.setViewName("SGEnum/elementMapping");
        return modelAndView;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getSlaveByMasterId/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<SGEnum> getSlaveByMasterId(@PathVariable(value = "id") String id) {
        List<SGEnum> slave = enumService.getSlaveByEnumId(id);
        return slave;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/checkSlaveByMasterId/{id}/{processId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<SGEnum> checkSlaveByMasterId(@PathVariable(value = "id") String id, @PathVariable(value = "processId") String processId) {
        List<SGEnum> slave = enumService.checkSlaveByEnumId(id, processId);
        return slave;
    }

    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getElementByMasterId/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    HashMap<String, Object> getElementByMasterId(@PathVariable(value = "id") String id,HttpServletRequest req) {
        int pageNum = Integer.parseInt(req.getParameter("page"));
        int rows = Integer.parseInt(req.getParameter("rows"));
        String hql = "select a from EnumElements a where a.enumId = '"+id+"'";

        return enumService.getElementByMasterId(hql,pageNum,rows);
    }

    @RequiresPermissions({"enum-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteEnum/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteEnumById(@PathVariable(value = "id") String id) {
        OperationLog operationLog = systemLogService.record("公共代码","删除","代码："+ id);
        SGEnum entity = enumService.findUniqueBy("id", id);
        if(entity != null){
            operationLog.setParams("代码：" + entity.getName());
        }

        boolean result = enumService.deleteEnumById(id);

        systemLogService.updateResult(operationLog);
        return result;

    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/saveEnum", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean saveEnum(@RequestBody SGEnum anEnum) {
        OperationLog operationLog = systemLogService.record("公共代码","修改", "代码："+ anEnum.getName());

        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        anEnum.setOptUser(userId);
        anEnum.setOptDate(DateUtils.format(new Date()));
        boolean result = enumService.updateEnum(anEnum);

        systemLogService.updateResult(operationLog);
        return result;
    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/setElementMapping", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean setElementMapping(@RequestBody EnumElementMap elementMap) {
        OperationLog operationLog = systemLogService.record("公共代码","保存主从关系", "");
        String logParams = "";
        if(elementMap != null){
            if(StringUtils.isNotEmpty(elementMap.getMasterElementId())){
                SGEnum master = enumService.getByEnumId(elementMap.getMasterElementId());
                if(master != null){
                    logParams += "主代码:" + master.getName();
                }
            }
            if(StringUtils.isNotEmpty(elementMap.getSlaveElementId())){
                SGEnum slave = enumService.getByEnumId(elementMap.getSlaveElementId());
                if(slave != null){
                    logParams += "　从代码:" + slave.getName();
                }
            }
        }
        enumService.addEnumElementMap(elementMap);

        operationLog.setParams(logParams);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/saveElementMapping", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean saveElementMapping(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("公共代码","批量保存主从关系","");
        String logParams1 = "";
        String logParams2 = "";
        String logParams3 = "";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            EnumElementMap elementMap = new EnumElementMap();
            elementMap.setDirection(map.get("DIRECTION"));
            elementMap.setMappingRelation(map.get("MAPPINGRELATION"));
            elementMap.setMasterElementId(map.get("MASTERID"));
            //因为前端combobox值，所以SLAVENAME
            if (null == map.get("SLAVENAME") || "".equals(map.get("SLAVENAME"))) continue;
            elementMap.setSlaveElementId(map.get("SLAVENAME"));
            if (map.get("SLAVEID") != null) {
                //删掉旧mapping
                enumService.deleteElementsMappingByPK(map.get("MASTERID"), map.get("SLAVEID"));
            }
            enumService.addEnumElementMap(elementMap);

            logParams1 += "," + map.get("REMARK");

            logParams2 += "," + map.get("MASTERNAME");

            logParams3 += "," + map.get("SLAVENAME");
        }

        operationLog.setParams("主代码中文名称：" + logParams1.substring(1, logParams1.length()) + " 主代码枚举名称:" +  logParams2.substring(1, logParams2.length())
                + " 从代码枚举名称：" +  logParams3.substring(1, logParams3.length()));
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/saveElementMappingSToM", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean saveElementMappingSToM(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("枚举映射","保存枚举关系","");
        String logParams1 = "";
        String logParams2 = "";
        String logParams3 = "";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            EnumElementMap elementMap = new EnumElementMap();
            elementMap.setDirection(map.get("DIRECTION"));
            elementMap.setMappingRelation(map.get("MAPPINGRELATION"));
            elementMap.setMasterElementId(map.get("MASTERNAME"));
            //因为前端combobox值，所以SLAVENAME
            if (null == map.get("MASTERNAME") || "".equals(map.get("MASTERNAME"))) continue;
            elementMap.setSlaveElementId(map.get("SLAVEID"));
            if (map.get("SLAVEID") != null) {
                //删掉旧mapping
                enumService.deleteElementsMappingByPK(map.get("MASTERID"), map.get("SLAVEID"));
            }
            enumService.addEnumElementMap(elementMap);

            logParams1 += "," + map.get("REMARK");

            logParams2 += "," + map.get("MASTERNAME");

            logParams3 += "," + map.get("SLAVENAME");
        }
        operationLog.setParams("主代码中文名称：" + logParams1.substring(1, logParams1.length()) + " 主代码枚举名称:" +  logParams2.substring(1, logParams2.length())
                + " 从代码枚举名称：" +  logParams3.substring(1, logParams3.length()));
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addElement", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addElement(@RequestBody EnumElements elements) {
        OperationLog operationLog = systemLogService.record("公共代码","枚举映射保存","名称：" + elements.getElementName());

        elements.setOptDate(DateUtils.format(new Date()));
        elements.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
        boolean result = enumService.addElement(elements);

        systemLogService.updateResult(operationLog);
        return result;
    }

    @RequiresPermissions({"enum-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteEnumElements", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteEnumElements(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("公共代码","枚举映射批量删除","");
        String logParams = "";

        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            ids.add(map.get("elementId"));
            List<EnumElements> entitys = enumService.getElementsByEnumId(map.get("elementId"));
            if(entitys != null && entitys.size() > 0){
                logParams += "," + entitys.get(0).getElementName();
            }
        }
        enumService.deleteEnumElementsByIds(ids);

        operationLog.setParams("名称：" + logParams.substring(1, logParams.length()-1));
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"enum-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteElementsMapping", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteElementsMapping(@RequestBody List list) {
        OperationLog operationLog = systemLogService.record("枚举映射","删除枚举关系","");
        String logParams1 = "";
        String logParams2 = "";
        String logParams3 = "";

        List<String> ids = new ArrayList<String>();
        List<EnumElementMap> mappingList = new ArrayList<EnumElementMap>();
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            EnumElementMap elementMap = new EnumElementMap();
            elementMap.setMasterElementId(map.get("MASTERID"));
            elementMap.setSlaveElementId(map.get("SLAVEID"));
            mappingList.add(elementMap);

            logParams1 += "," + map.get("REMARK");

            logParams2 += "," + map.get("MASTERNAME");

            logParams3 += "," + map.get("SLAVENAME");

        }
        enumService.deleteElementsMapping(mappingList);

        operationLog.setParams("主代码中文名称：" + logParams1.substring(1, logParams1.length()) + " 主代码枚举名称:" +  logParams2.substring(1, logParams2.length())
                + " 从代码枚举名称：" +  logParams3.substring(1, logParams3.length()));
        systemLogService.updateResult(operationLog);
        return true;
    }

    //	url:"/enum/query/processId/${processId}",
    @RequiresPermissions({"enum-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/query/processId/{processId}")
    public
    @ResponseBody
    Map<String, Object> query(HttpServletRequest request, @PathVariable("processId") String processId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("processId", processId);
        params.put("isMaster", "1");
        List<SGEnum> rows = enumService.findBy(params);

        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    /**
     * 主代码name唯一性验证
     *
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String name) {
        return enumService.uniqueValid("name",name);
    }

    /**
     * 主代码name唯一性验证
     *
     * @param remark
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueChineseName", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueChineseName(String remark) {
        try {
            remark = URLDecoder.decode(URLDecoder.decode(remark, "utf-8"), "utf-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enumService.uniqueValid("remark",remark);
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
