package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.ResourceExportServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.vo.ReleaseListVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by wang on 2015/9/10.
 */
@Controller
@RequestMapping("/resourceExporter")
public class ResourceExportController {
    private static Log log = LogFactory.getLog(ResourceExportController.class);

    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    ResourceExportServiceImpl resourceExportService;

    /**
     * 导出元数据
     * */
    @RequiresPermissions({"exportMetadataExcel-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public
    @ResponseBody
    boolean exportDictionary(HttpServletRequest request, HttpServletResponse response,
                               ReleaseListVO listVO) {
        OperationLog operationLog = systemLogService.record("数据字典","导出","");

        String fileName ="data_dictionary_" + new Date().getTime();
        HSSFWorkbook workbook = resourceExportService.genderResourceExcel();
        boolean result = export(request, response,fileName, workbook);

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
