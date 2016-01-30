package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.export.util.FileUtil;
import com.dc.esb.servicegov.export.util.ZipUtil;
import com.dc.esb.servicegov.service.impl.SqlGenerateServiceImpl;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.rsimport.impl.*;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by kongxfa on 2016/1/11.
 */
@Controller
@RequestMapping("/sqlGenerate")
public class SqlGenerateController {

    private Log log = LogFactory.getLog(SqlGenerateController.class);

    @Autowired
    private SqlGenerateServiceImpl sqlGenerateServiceImpl;


    //    @RequiresPermissions({"importMetadata-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/import")
    public void importMetadata(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("file") MultipartFile file
    ) throws Exception {
        String msg = "";
        log.info("import fileName is: " + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String path = null;
        //判断上传文件是否为xlsx或xls类型
        if (fileName.toLowerCase().endsWith("xlsx")
                || fileName.toLowerCase().endsWith("xls")) {
            synchronized (ResourceImportController.class) {//同步，防止有多个请求
                Workbook workbook = null;
                try {
                    if (fileName.toLowerCase().endsWith("xls")) {
                        workbook = new HSSFWorkbook(file.getInputStream());
                    } else if (fileName.toLowerCase().endsWith("xlsx")) {
                        workbook = new XSSFWorkbook(file.getInputStream());
                    }
                } catch (IOException e) {
                    log.error(e, e);
                    msg = "文件上传过程中出现错误！";
                }
                if (null != workbook) {
                    try {
                        path = sqlGenerateServiceImpl.parse(workbook);
                    } catch (Exception e) {
                        log.error(e, e);
                        msg = "数据读取过程中出现错误！";
                    }
                }

            }
        } else {
            msg = "请上传EXCEL文件！";
        }

//*************************导出
        if (StringUtils.isNotEmpty(path)) {
            ZipUtil.compressZip(path, path + "/sqlGenerate.zip", "sqlGenerate.zip");
            InputStream in = null;
            OutputStream out = null;
            try {
                File metadata = new File(path + "/sqlGenerate.zip");

                response.setContentType("application/zip");
                response.addHeader("Content-Disposition",
                        "attachment;filename=sqlGenerate.zip");
                in = new BufferedInputStream(new FileInputStream(metadata));
                out = new BufferedOutputStream(response.getOutputStream());
                long fileLength = metadata.length();
                byte[] cache = null;
                if (fileLength > Integer.MAX_VALUE) {
                    cache = new byte[Integer.MAX_VALUE];
                } else {
                    cache = new byte[(int) fileLength];
                }
                int i = 0;
                while ((i = in.read(cache)) > 0) {
                    out.write(cache, 0, i);
                }
                out.flush();
            } catch (Exception e) {
                log.error(e, e);
                printMsg(response, "生成脚本文件出现错误,请检查数据！");
            } finally {
                try {
                    in.close();
                    out.close();
                    FileUtil.deleteDirectory(new File(path));
                } catch (Exception e) {
                    log.error("生成脚本文件，关闭流异常," + e.getMessage(), e);
                }

            }
        }
//        if (StringUtils.isEmpty(msg)) {
//            printMsg(response, msg);
//        }
    }

    public void printMsg(HttpServletResponse response, String message) {
        PrintWriter pw = null;
        try {
            response.setContentType("text/html; charset=utf-8");
            pw = response.getWriter();
            pw.print("<script language='javascript'>alert('" + message + "')</script>");
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
