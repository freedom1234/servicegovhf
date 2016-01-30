package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.ExcelExportInterfaceImpl;
import com.dc.esb.servicegov.service.impl.ExcelExportServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.OperationPKVO;
import com.dc.esb.servicegov.vo.ReleaseListVO;
import com.dc.esb.servicegov.vo.ReuseRateListVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/7/14.
 */
@Controller
@RequestMapping("/excelExporter")
public class ExcelExportController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    private static Log log = LogFactory.getLog(ExcelExportController.class);
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }
    @Autowired
    private ExcelExportServiceImpl excelExportServiceImpl;

    @Autowired
    private ExcelExportInterfaceImpl excelExportInterfaceImpl;

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

    /**
     * 根据服务分类或者服务导出字段映射
     **/
    @RequiresPermissions({"excelExport-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportService", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportService(HttpServletRequest request, HttpServletResponse response,
                          String id, String type) {
        OperationLog operationLog = systemLogService.record("字段映射文档","导出EXCEL","根据分类或者服务导出，具体参数：id"+id+", 类型:"+type);

        String fileName = type+"_"+id + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderExcel(id, type);
        boolean result = export(request, response, fileName, workbook);

        systemLogService.updateResult(operationLog);
        return result;
    }
    /*根据场景列表导出字段映射*/
    @RequiresPermissions({"excelExport-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportOperation", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportOperation(HttpServletRequest request, HttpServletResponse response, OperationPKVO pkvo) {
        OperationLog operationLog = systemLogService.record("字段映射文档","导出EXCEL","根据服务场景列表导出");

        String fileName = "operation_" + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderExcelByOperation(pkvo);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;
    }
    /**
     * 根据系统导出字段映射文档
     **/
    @RequiresPermissions({"excelExport-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportSystem", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportService(HttpServletRequest request, HttpServletResponse response, String systemId) {
        OperationLog operationLog = systemLogService.record("字段映射文档","导出EXCEL","根据系统ID，具体参数：系统编码["+ systemId + "]");

        String fileName = systemId + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderExcelBySystemId(systemId);
        boolean result = export(request, response, fileName, workbook);

        systemLogService.updateResult(operationLog);
        return result;
    }

    /**
     * 根据服务分类或者服务导出服务视图
     */
//    @RequiresPermissions({"excelExport-get"})
    @RequiresPermissions({"viewExcelExport-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportServiceView", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportServiceView(HttpServletRequest request, HttpServletResponse response,
            String type, String categoryId) {
        OperationLog operationLog = systemLogService.record("服务视图","导出EXCEL","分类ID:"+categoryId);

        String fileName = "serviceview_"+categoryId + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderServiceView(type, categoryId);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;
    }

    @RequiresPermissions({"exportInterface-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportInterface", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportInterface(HttpServletRequest request, HttpServletResponse response,
                          String ids, String type,String systemId) {
        OperationLog operationLog = systemLogService.record("接口","导出EXCEL","ID列表："+ ids + "; 系统ID:" + systemId +"; 类型:" + type);

        String fileName = type + new Date().getTime();
        String[] interfaceIds = ids.split(",");
        HSSFWorkbook workbook = excelExportInterfaceImpl.genderExcel(interfaceIds, type, systemId);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;
    }

    /**
     * 导出复用率统计execl
     * */
    @RequiresPermissions({"exportStatistics-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportSystemReuserate", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportSystemReuserate(HttpServletRequest request, HttpServletResponse response,
                            ReuseRateListVO listVO) {
        OperationLog operationLog = systemLogService.record("系统复用率统计","导出EXCEL","导出条数：" + listVO.getList().size());

        String fileName = "reuse_rate_" + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderSystemRuserate(listVO);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;
    }
    /**
     * 导出服务复用率统计execl
     * */
    @RequiresPermissions({"exportStatistics-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportServiceReuserate", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportServiceReuserate(HttpServletRequest request, HttpServletResponse response,
                            TreeNode root) {
        OperationLog operationLog = systemLogService.record("服务复用率统计","导出EXCEL","");

        String fileName ="reuse_rate_" + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderServiceRuserate(root);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;
    }
    /**
     * 导出发布状况统计
     * */
    @RequiresPermissions({"exportStatistics-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportRelease/state", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportReleaseSate(HttpServletRequest request, HttpServletResponse response,
                          ReleaseListVO listVO) {
        OperationLog operationLog = systemLogService.record("发布情况统计","导出EXCEL","");

        String fileName ="release_" + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderReleaseState(listVO);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;

    }
    /**
     * 导出发布数量统计
     * */
    @RequiresPermissions({"exportStatistics-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/exportRelease/count", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean exportReleaseCount(HttpServletRequest request, HttpServletResponse response,
                            ReleaseListVO listVO) {
        OperationLog operationLog = systemLogService.record("发布次数统计","导出EXCEL","");

        String fileName ="release_" + new Date().getTime();
        HSSFWorkbook workbook = excelExportServiceImpl.genderReleaseCount(listVO);

        boolean result = export(request, response, fileName, workbook);
        systemLogService.updateResult(operationLog);

        return result;

    }

    public boolean  export(HttpServletRequest request, HttpServletResponse response, String fileName, HSSFWorkbook workbook ){
        String codedFileName = null;
        OutputStream fOut = null;
        try
        {
            // 进行转码，使其支持中文文件名
            response.setContentType("application/vnd.ms-excel");
            codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // response.addHeader("Content-Disposition", "attachment;   filename=" + codedFileName + ".xls");
            // 产生工作簿对象
//            HSSFWorkbook workbook = excelExportServiceImpl.genderRelease(listVO);
            fOut = response.getOutputStream();
            if(workbook != null){
                workbook.write(fOut);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(fOut != null){
                    fOut.flush();
                    fOut.close();
                }
            }
            catch (IOException e)
            {
                log.error("IO异常");
            }
        }
        return true;

    }

}
