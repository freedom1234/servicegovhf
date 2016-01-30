package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.export.impl.MetadataConfigGenerator;
import com.dc.esb.servicegov.service.impl.MetadataHisServiceImpl;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.util.DateUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    private static final Log log = LogFactory.getLog(MetadataController.class);
    @Autowired
    private MetadataServiceImpl metadataService;
    @Autowired
    private SDAServiceImpl sdaService;

    @Autowired
    private MetadataHisServiceImpl metadataHisService;

    @Autowired
    private MetadataConfigGenerator metadataConfigGenerator;

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getAll() {
        return metadataService.getAllMetadata();
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByMetadataId/{metadataId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByMetadataId(@PathVariable(value = "metadataId") String metadataId) {
        return metadataService.getByMetadataId(metadataId);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByMetadataName/{metadataName}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByMetadataName(@PathVariable(value = "metadataName") String metadataName) {
        return metadataService.getByMetadataName(metadataName);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByChineseName/{chineseName}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByChineseName(@PathVariable(value = "chineseName") String chineseName) {
        return metadataService.getByChineseName(chineseName);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByCategoryWordId/{categoryWordId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByCategoryWordId(@PathVariable(value = "categoryWordId") String categoryWordId) {
        return metadataService.getByCategoryWordId(categoryWordId);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByRemark/{remark}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByRemark(@PathVariable(value = "remark") String remark) {
        return metadataService.getByRemark(remark);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByType/{type}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByType(@PathVariable(value = "type") String type) {
        return metadataService.getByType(type);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByLength/{length}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByLength(@PathVariable(value = "length") String length) {
        return metadataService.getByLength(length);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByScale/{scale}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByScale(@PathVariable(value = "scale") String scale) {
        return metadataService.getByScale(scale);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByEnumId/{enumId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByEnumId(@PathVariable(value = "enumId") String enumId) {
        return metadataService.getByEnumId(enumId);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByMetadataAlias/{metadataAlias}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByMetadataAlias(@PathVariable(value = "metadataAlias") String metadataAlias) {
        return metadataService.getByMetadataAlias(metadataAlias);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByBussDefine/{bussDefine}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByBussDefine(@PathVariable(value = "bussDefine") String bussDefine) {
        return metadataService.getByBussDefine(bussDefine);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByBussRule/{bussRule}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByBussRule(@PathVariable(value = "bussRule") String bussRule) {
        return metadataService.getByBussRule(bussRule);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByDataSource/{dataSource}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByDataSource(@PathVariable(value = "dataSource") String dataSource) {
        return metadataService.getByDataSource(dataSource);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByTemplateId/{templateId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByTemplateId(@PathVariable(value = "templateId") String templateId) {
        return metadataService.getByTemplateId(templateId);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByStatus/{status}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByStatus(@PathVariable(value = "status") String status) {
        return metadataService.getByStatus(status);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByVersion/{version}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByVersion(@PathVariable(value = "version") String version) {
        return metadataService.getByVersion(version);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByPotUser/{potUser}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByPotUser(@PathVariable(value = "potUser") String potUser) {
        return metadataService.getByPotUser(potUser);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByPotDate/{potDate}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByPotDate(@PathVariable(value = "potDate") String potDate) {
        return metadataService.getByPotDate(potDate);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByAuditUser/{auditUser}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByAuditUser(@PathVariable(value = "auditUser") String auditUser) {
        return metadataService.getByAuditUser(auditUser);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByAuditDate/{auditDate}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Metadata> getByAuditDate(@PathVariable(value = "auditDate") String auditDate) {
        return metadataService.getByAuditDate(auditDate);
    }

    @RequiresPermissions({"metadata-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(Metadata metadata) {
        OperationLog operationLog = systemLogService.record("元数据","新增","元数据名称：" + metadata.getChineseName() + "; 英文名称：" + metadata.getMetadataId());

        Metadata metadataExsit = metadataService.findUniqueBy("metadataId", metadata.getMetadataId());//查询过时数据，将过时元素移至历史表，删除过时元素
        if(null != metadataExsit){
            MetadataHis metadataHis = new MetadataHis(metadataExsit);
            metadataHisService.addMetadataHis(metadataHis);
            metadataService.delete(metadataExsit);
        }
        metadataService.addMetadata(metadata);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"metadata-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify/{oldMetadataId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify(Metadata metadata,@PathVariable String oldMetadataId, boolean updateSDAFlag) {
        OperationLog operationLog = systemLogService.record("元数据","修改","元数据名称：" + metadata.getChineseName() + "; 英文名称：" + metadata.getMetadataId());

        //TZB要求元数据能修改
        if(!metadata.getMetadataId().equals(oldMetadataId)){
            Metadata newMetadata = metadataService.getById(metadata.getMetadataId());
            if(null != newMetadata){
                return false;
            }
            //删除老数据
            metadataService.deleteById(oldMetadataId);
        }
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        metadata.setOptUser(userName);
        metadata.setOptDate(DateUtils.format(new Date()));
        metadataService.modifyMetadata(metadata);
        if(updateSDAFlag){
            sdaService.updateMetadataRelate(metadata);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"metadata-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify(Metadata metadata) {
        OperationLog operationLog = systemLogService.record("元数据","修改","元数据名称：" + metadata.getChineseName() + "; 英文名称：" + metadata.getMetadataId());

        metadataService.modifyMetadata(metadata);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"metadata-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{metadataId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String metadataId) {
        OperationLog operationLog = systemLogService.record("元数据","删除","元数据ID" + metadataId);

        metadataService.deleteMetadata(metadataId);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"metadata-delete"})
    @RequestMapping("/deletes")
    @ResponseBody
    public boolean deletes(String metadataIds) {
        OperationLog operationLog = systemLogService.record("元数据","批量删除","元数据ID：" + metadataIds);

        boolean result = metadataService.deleteMetadatas(metadataIds);
        systemLogService.updateResult(operationLog);
        return result;

    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam("page") int pageNo, @RequestParam("rows") int rowCount) {
        Page page = metadataService.getAll(rowCount);
        page.setPage(pageNo);
        List<Metadata> rows = metadataService.getAll(page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/query", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest req) {
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(metadataService.queryCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
//        List<Metadata> rows = metadataService.queryByCondition(req.getParameterMap(), page);
        //关联categoryWord表，显示chineseWord
        List<MetadataServiceImpl.MetadataBean> rows = metadataService.queryByCondition2(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping("/query/processId/{processId}")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest request, @PathVariable("processId") String processId) {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("processId", processId);
        List<Metadata> rows = metadataService.findBy(params);

        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"metadata-update"})
    @RequestMapping(method = RequestMethod.GET, value = "/editPage", headers = "Accept=application/json")
    public ModelAndView editPage(String metadataId) {
        ModelAndView mv = new ModelAndView("../assets/metadata/edit");
        List<Metadata> list = metadataService.getByMetadataId(metadataId);
        if (list != null && list.size() > 0) {
            Metadata entity = list.get(0);
            mv.addObject("entity", entity);
        }

        return mv;
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String metadataId) {
        return metadataService.uniqueValid(metadataId);
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueChineseNameValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueChineseNameValid(String chineseName) {
        try {
            chineseName = URLDecoder.decode(URLDecoder.decode(chineseName, "utf-8"),"utf-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return metadataService.uniqueChineseNameValid(chineseName);
    }

    //获取类别词接口
    @RequiresPermissions({"metadata-get"})
    @RequestMapping("/categoryWord")
    @ResponseBody
    public List<CategoryWord> categoryWord() {

        List<CategoryWord> rows = metadataService.categoryWord();

        return rows;
    }

    /**
     * TODO 这个是干什么的
     *
     * @param serviceId
     * @return
     */
    @RequiresPermissions({"metadata-get"})
    @RequestMapping("/servicePage")
    public ModelAndView servicePage(String serviceId) {
        ModelAndView mv = new ModelAndView("service/serviceIndex");
        return mv;
    }

    @RequiresPermissions({"metadata-get"})
    @RequestMapping("/audit/process/{processId}")
    public
    @ResponseBody
    boolean auditMetadata(@PathVariable("processId") String processId) {
        OperationLog operationLog = systemLogService.record("元数据","审核（任务）","");
        Map<String, String> params = new HashMap<String, String>();
        params.put("processId", processId);
        List<Metadata> metadatas = metadataService.findBy(params);
        for (Metadata metadata : metadatas) {
            metadata.setStatus("正式");
            metadataService.save(metadata);
        }
        systemLogService.updateResult(operationLog);
        return true;
    }


//    @RequiresPermissions({"metadata-get"})
    @RequiresPermissions({"exportXML-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public
    @ResponseBody
    boolean exportMetadata(HttpServletRequest request, HttpServletResponse response) {
        OperationLog operationLog = systemLogService.record("元数据","导出XML","");
        log.info(SecurityUtils.getSubject().getPrincipal());
        //生成本地文件
        File file = metadataConfigGenerator.generate();
        //读取本地文件内容
        int length = (int) file.length();
        DataInputStream dataIn = null;
        OutputStream out = null;
        try {
            dataIn = new DataInputStream(new FileInputStream(file));
            byte[] buffer = new byte[length];
            dataIn.readFully(buffer);
            //修改HTTP头
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition",
                    "attachment;filename="
                            + new String(file.getName().getBytes(
                            "gbk"), "iso-8859-1"));
            //把本地文件的内容写入Http输出流
            out = response.getOutputStream();
            out.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != dataIn) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }
        }
        //删除本地文件
        file.delete();
        systemLogService.updateResult(operationLog);
        return true;
    }
    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
