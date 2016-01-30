package com.dc.esb.servicegov.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.MetadataService;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.GlobalImport;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dc.esb.servicegov.service.ExcelImportService;
import com.dc.esb.servicegov.util.ExcelTool;

@Controller
@RequestMapping("/excelHelper")
public class ExcelImportController {

    @Autowired
    private SystemLogServiceImpl systemLogService;

    protected Log logger = LogFactory.getLog(getClass());

    @Qualifier("TaizhouExcelImportService")
//    @Qualifier("BaseExcelImportService")
    @Autowired
    ExcelImportService excelImportService;

    @Autowired
    MetadataServiceImpl metadataService;

    @Autowired
    LogInfoServiceImpl logInfoService;

    @Autowired
    InterfaceInvokeServiceImpl interfaceInvokeService;

    @Autowired
    InterfaceServiceImpl interfaceService;
    /**
     * Excel 2003
     */
    private final static String XLS = "xls";
    /**
     * Excel 2007
     */
    private final static String XLSX = "xlsx";

    @RequiresPermissions({"importInterface-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/interfaceimport")
    public void importInterfaceField(@RequestParam("systemId")String systemId, @RequestParam("file") MultipartFile file, HttpServletResponse response){
        OperationLog operationLog = systemLogService.record("接口文档","导入","文件名称："+ file.getOriginalFilename());

        response.setContentType("text/html");
        response.setCharacterEncoding("GB2312");
        Workbook workbook = null;
        String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        InputStream is = null;
        java.io.PrintWriter writer = null;
        StringBuffer msg = new StringBuffer();
        boolean success = true;
        try {
            writer = response.getWriter();
            is = file.getInputStream();
            if (extensionName.toLowerCase().equals(XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (extensionName.toLowerCase().equals(XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                outPutError(writer,systemId);
                return;
            }
            // 读取交易索引Sheet页
            Sheet indexSheet = workbook.getSheet("INDEX");
            if (indexSheet == null) {
                logger.error("缺少INDEX sheet页");
                logInfoService.saveLog("缺少INDEX sheet页", "导入");
                outPutError(writer,systemId);
                return;
            }
            // 从第一行开始读，获取所有接口行
            List list = excelImportService.parseInterfaceIndexSheet(indexSheet);
            List<ExcelImportServiceImpl.IndexDO> indexDOs = (List<ExcelImportServiceImpl.IndexDO>)list.get(0);
            msg.append(list.get(1));

            for (ExcelImportServiceImpl.IndexDO indexDO : indexDOs) {
                //开始解析每一个页面
                String sheetName = indexDO.getSheetName();
                if (sheetName != null && !"".equals(sheetName)) {
                    // 读取每个交易sheet页
                    logger.debug("开始获取" + sheetName + "交易信息=========================");
                    Sheet sheet = workbook.getSheet(sheetName);
                    if(null == sheet){
                        logger.error(sheetName + "导入失败，sheet页不存在");
                        logInfoService.saveLog(sheetName + "导入失败，sheet页不存在", "原始接口导入");
                        msg.append(sheetName + "导入失败，sheet页不存在，");
                        continue;
                    }
                    //获取接口,系统信息
                    Map<String, Object> infoMap = excelImportService.getInterfaceInfo(sheet);
                    //获取接口 输入 参数
                    Map<String, Object> inputMap = excelImportService.getInterfaceInputArg(sheet);
                    //获取接口 输出 参数
                    Map<String, Object> outMap = excelImportService.getInterfaceOutputArg(sheet);

                    if (infoMap == null || inputMap == null || outMap == null) {
                        msg.append(sheetName + "导入失败，");
                        continue;
                    }
                    //判断是否新增
                    if(indexDO.getOptType().equals("0")){
                        Interface tempInter = (Interface) infoMap.get("interface");
                        Interface inter = interfaceService.getById(tempInter.getInterfaceId());
                        if(inter != null){
                            msg.append(sheetName + "导入失败，新增接口已经存在，");
                            logger.error(sheetName + "导入失败，新增接口已经存在");
                            logInfoService.saveLog(sheetName + "导入失败，新增接口已经存在", "原始接口导入");
                            continue;
                        }
                    }
                    logger.info("===========接口[" + sheetName + "],开始导入接口信息=============");
                    long time = java.lang.System.currentTimeMillis();
                    List result = excelImportService.executeInterfaceImport(infoMap, inputMap, outMap,indexDO);
                    /*if ((Boolean)result.get(0)) {
                        logger.info("===========接口[" + sheetName + "],导入失败=============");
                        msg.append(result.get(1).toString());
                        logInfoService.saveLog(result.get(1).toString(), "导入");
                        continue;
                    }*/
                    long useTime = java.lang.System.currentTimeMillis() - time;
                    logger.info("===========接口[" + sheetName + "],导入完成，耗时" + useTime + "ms=============");
                } else {
                    logger.error("交易代码为空。");
                    logInfoService.saveLog("第交易代码为空。", "导入");
                }

            }
            //组织返回
            outPut(writer, msg);
        } catch (IOException e) {
            logger.error("导入出现异常:异常信息：" + e.getMessage());
            logInfoService.saveLog("导入出现异常:异常信息：" + e.getMessage(), "导入");
            writer.println("alert('导入失败，请查看日志!');");
            success = false;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
//        GlobalImport.headMap.clear();//清空本次导入的业务报文头
        writer.println("window.location='/jsp/sysadmin/interface_import.jsp?systemId="+systemId+"'");
        writer.println("</script>");
        writer.flush();
        writer.close();

        systemLogService.updateResult(operationLog);
    }

    @RequiresPermissions({"importInterface-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/interfaceimport/noSystem")
    public void importInterfaceField(@RequestParam("file") MultipartFile file, HttpServletResponse response){
        OperationLog operationLog = systemLogService.record("接口文档","导入","文件名称："+ file.getOriginalFilename());

        response.setContentType("text/html");
        response.setCharacterEncoding("GB2312");
        Workbook workbook = null;
        String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        InputStream is = null;
        java.io.PrintWriter writer = null;
        StringBuffer msg = new StringBuffer();
        boolean success = true;
        try {
            writer = response.getWriter();
            is = file.getInputStream();
            if (extensionName.toLowerCase().equals(XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (extensionName.toLowerCase().equals(XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                outPutError(writer,"noSystem");
                return;
            }
            // 读取交易索引Sheet页
            Sheet indexSheet = workbook.getSheet("INDEX");
            if (indexSheet == null) {
                logger.error("缺少INDEX sheet页");
                logInfoService.saveLog("缺少INDEX sheet页", "导入");
                outPutError(writer,"noSystem");
                return;
            }
            // 从第一行开始读，获取所有接口行
            List list = excelImportService.parseInterfaceIndexSheet(indexSheet);
            List<ExcelImportServiceImpl.IndexDO> indexDOs = (List<ExcelImportServiceImpl.IndexDO>)list.get(0);
            msg.append(list.get(1));

            for (ExcelImportServiceImpl.IndexDO indexDO : indexDOs) {
                //开始解析每一个页面
                String sheetName = indexDO.getSheetName();
                if (sheetName != null && !"".equals(sheetName)) {
                    // 读取每个交易sheet页
                    logger.debug("开始获取" + sheetName + "交易信息=========================");
                    Sheet sheet = workbook.getSheet(sheetName);
                    if(null == sheet){
                        logger.error(sheetName + "导入失败，sheet页不存在");
                        logInfoService.saveLog(sheetName + "导入失败，sheet页不存在", "原始接口导入");
                        msg.append(sheetName + "导入失败，sheet页不存在，");
                        continue;
                    }
                    //获取接口,系统信息
                    Map<String, Object> infoMap = excelImportService.getInterfaceInfo(sheet);
                    //获取接口 输入 参数
                    Map<String, Object> inputMap = excelImportService.getInterfaceInputArg(sheet);
                    //获取接口 输出 参数
                    Map<String, Object> outMap = excelImportService.getInterfaceOutputArg(sheet);

                    if (infoMap == null || inputMap == null || outMap == null) {
                        logger.error(sheetName + "导入失败，");
                        logInfoService.saveLog(sheetName + "导入失败，", "原始接口导入");
                        msg.append(sheetName + "导入失败，");
                        continue;
                    }
                    //判断是否新增
                    if(indexDO.getOptType().equals("0")){
                        Interface tempInter = (Interface) infoMap.get("interface");
                        Interface inter = interfaceService.getById(tempInter.getInterfaceId());
                        if(inter != null){
                            msg.append(sheetName + "导入失败，新增接口已经存在，");
                            logger.error(sheetName + "导入失败，新增接口已经存在");
                            logInfoService.saveLog(sheetName + "导入失败，新增接口已经存在", "原始接口导入");
                            continue;
                        }
                    }

                    logger.info("===========接口[" + sheetName + "],开始导入接口信息=============");
                    long time = java.lang.System.currentTimeMillis();
                    List result = excelImportService.executeInterfaceImport(infoMap, inputMap, outMap,indexDO);
                   /* if ((Boolean)result.get(0)) {
                        logger.info("===========接口[" + sheetName + "],导入失败=============");
                        msg.append(result.get(1).toString());
                        logInfoService.saveLog(result.get(1).toString(), "导入");
                        continue;
                    }*/
                    long useTime = java.lang.System.currentTimeMillis() - time;
                    logger.info("===========接口[" + sheetName + "],导入完成，耗时" + useTime + "ms=============");
                } else {
                    logger.error("交易代码为空。");
                    logInfoService.saveLog("第交易代码为空。", "导入");
                }

            }
            //组织返回
            outPut(writer, msg);
        } catch (IOException e) {
            logger.error("导入出现异常:异常信息：" + e.getMessage());
            logInfoService.saveLog("导入出现异常:异常信息：" + e.getMessage(), "导入");
            writer.println("alert('导入失败，请查看日志!');");
            success = false;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
//        GlobalImport.headMap.clear();//清空本次导入的业务报文头
        writer.println("window.location='/jsp/sysadmin/interface_import.jsp'");
        writer.println("</script>");
        writer.flush();
        writer.close();

        systemLogService.updateResult(operationLog);
    }

    @RequiresPermissions({"importExcel-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/fieldimport")
    public void importFieldMapping(@RequestParam("file")
                                   MultipartFile file, Model model, HttpServletResponse response, @RequestParam("select")
                                   String operateFlag) {
        OperationLog operationLog = systemLogService.record("字段映射文档","导入","文件名称："+ file.getOriginalFilename());

        response.setContentType("text/html");
        response.setCharacterEncoding("GB2312");
        logger.info("覆盖标识: " + operateFlag);
        if ("Y".equals(operateFlag)) {
            GlobalImport.operateFlag = true;
        } else {
            GlobalImport.operateFlag = false;
        }
        Workbook workbook = null;
        String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        InputStream is = null;
        java.io.PrintWriter writer = null;
        StringBuffer msg = new StringBuffer();
        try {
            writer = response.getWriter();
            is = file.getInputStream();
            if (extensionName.toLowerCase().equals(XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (extensionName.toLowerCase().equals(XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                outPutError(writer,null);
                return;
            }
            // 读取交易索引Sheet页
            Sheet indexSheet = workbook.getSheet("INDEX");
            if (indexSheet == null) {
                logger.error("缺少INDEX sheet页");
                logInfoService.saveLog("缺少INDEX sheet页", "导入");
                outPutError(writer,null);
                return;
            }
            //TODO parse的时候不存在系统
            // 从第一行开始读，获取所有交易行
            List list = excelImportService.parseIndexSheet(indexSheet);
            List<ExcelImportServiceImpl.IndexDO> indexDOs = (List<ExcelImportServiceImpl.IndexDO>)list.get(0);
            List<OperationPK> operationPKs = new ArrayList<OperationPK>();//场景主键列表，用来定位某个场景是否已经被发布过
            msg.append(list.get(1));
            for (ExcelImportServiceImpl.IndexDO indexDO : indexDOs) {
                //TODO 提供和消费系统都要判断
                boolean consumerExists = excelImportService.existSystem(indexDO.getConsumerSystemId());
                if (!consumerExists) {
                    logger.error("交易[" + indexDO.getSheetName() + "]," + indexDO.getConsumerSystem() + "系统不存在");
                    logInfoService.saveLog("交易[" + indexDO.getSheetName() + "]," + indexDO.getConsumerSystem() + "系统不存在", "导入");
                    msg.append("交易[" + indexDO.getSheetName() + "]," + indexDO.getConsumerSystem() + "系统不存在，");
                    continue;
                }
                boolean providerExists = excelImportService.existSystem(indexDO.getProviderSystemId());
                if (!providerExists) {
                    logger.error("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "系统不存在");
                    logInfoService.saveLog("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "系统不存在", "导入");
                    msg.append("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "系统不存在，");
                    continue;
                }
                /*//判断系统是否存在
                boolean exists = excelImportService.existSystem(indexDO.getSystemId());
                //系统不存在该行跳过，继续下一行解析
                if (!exists) {
                    logger.error("交易[" + indexDO.getSheetName() + "]," + indexDO.getSystemAb() + "系统不存在");
                    logInfoService.saveLog("交易[" + indexDO.getSheetName() + "]," + indexDO.getSystemAb() + "系统不存在", "导入");
                    msg.append("交易[" + indexDO.getSheetName() + "]," + indexDO.getSystemAb() + "系统不存在，");
                    continue;
                }*/
                //开始解析每一个页面
                String sheetName = indexDO.getSheetName();
                //------------------------------------
                //TODO 标准接口导入（没ida）
                if(null != sheetName && !"".equals(sheetName) && indexDO.getIsStandard().equals(Constants.INVOKE_TYPE_STANDARD_Y)){
                    // 读取每个交易sheet页
                    logger.debug("开始获取" + sheetName + "交易信息=========================");
                    Sheet sheet = workbook.getSheet(sheetName);
                    if(null == sheet){
                        logger.error("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "页签不存在");
                        logInfoService.saveLog("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "页签不存在", "导入");
                        msg.append("交易[" + indexDO.getSheetName() + "]," + indexDO.getProviderSystem() + "页签不存在，");
                        continue;
                    }
                    //交易、服务、场景信息
                    Map<String, Object> infoMap = excelImportService.getServiceInfo(sheet,indexDO);
                    infoMap.put("operationPKs", operationPKs);
                    //获取 服务 输入 参数
                    Map<String, Object> inputMap = excelImportService.getStandardInputArg(sheet);
                    //获取接口、服务 输出 参数
                    Map<String, Object> outMap = excelImportService.getStandardOutputArg(sheet);
                    //获取接口头
                    Map<String, Object> headMap = excelImportService.getInterfaceHead(indexDO, workbook);
                    //获取公共信息
                    Map<String, String> publicMap = excelImportService.getPublicHead(indexDO);
                    if (infoMap == null || inputMap == null || outMap == null) {
                        msg.append(sheetName + "导入失败，");
                        continue;
                    }
                    logger.info("===========交易[" + sheetName + "],开始导入字段映射信息=============");
                    long time = java.lang.System.currentTimeMillis();
                    List result = excelImportService.executeStandardImport(infoMap, inputMap, outMap, publicMap, headMap);

                    if (!(Boolean)result.get(0)) {
                        logger.info("===========交易[" + sheetName + "],导入失败=============");
                        continue;
                    }
                    //TODO 插入标准接口映射
                    indexDO.getSystemId();
                    //IS_STANDARD  SERVICE_ID,operation_id systemId,type
                    String type = indexDO.getInterfacePoint();
                    String operationId = indexDO.getOperationId();
                    String cusumerSystem = indexDO.getConsumerSystem();
                    String cusumerSystemId = indexDO.getConsumerSystemId();
                    String providerSystem = indexDO.getProviderSystem();
                    String providerSystemId = indexDO.getProviderSystemId();
                    String invokeSystemId = "";
                    String isStandard = Constants.INVOKE_TYPE_STANDARD_Y;
                    String serviceId = indexDO.getServiceId();
                    if("Provider".equalsIgnoreCase(type)){
                        type = "1";
                        invokeSystemId = cusumerSystemId;
                    }else{
                        type = "0";
                        invokeSystemId = providerSystemId;
                    }
                    ServiceInvoke invoke = excelImportService.addServiceInvoke(invokeSystemId,serviceId,operationId,type,isStandard);
                    //增加调用关系
                    ServiceInvoke provider_invoke = (ServiceInvoke)result.get(1);
                    if(null != invoke){
                        String providerInvokeId = "";
                        String consumerInvokeId = "";
                        if(provider_invoke.getType().equals("0")){
                            providerInvokeId = provider_invoke.getInvokeId();
                            consumerInvokeId = invoke.getInvokeId();
                        }else{
                            consumerInvokeId = provider_invoke.getInvokeId();
                            providerInvokeId = invoke.getInvokeId();
                        }
                        //判断是否存在调用关系
                        Map map = new HashMap();
                        map.put("providerInvokeId",providerInvokeId);
                        map.put("consumerInvokeId",consumerInvokeId);
                        List<InterfaceInvoke> invokeList = interfaceInvokeService.findBy(map);
                        if(invokeList.size()==0){
                            InterfaceInvoke interfaceInvoke = new InterfaceInvoke();
                            interfaceInvoke.setConsumerInvokeId(consumerInvokeId);
                            interfaceInvoke.setProviderInvokeId(providerInvokeId);
                            interfaceInvokeService.insert(interfaceInvoke);
                        }

                    }

                    long useTime = java.lang.System.currentTimeMillis() - time;
                    logger.info("===========交易[" + sheetName + "],导入完成，耗时" + useTime + "ms=============");
                }else if (sheetName != null && !"".equals(sheetName)) {
                    // 读取每个交易sheet页
                    logger.debug("开始获取" + sheetName + "交易信息=========================");
                    Sheet sheet = workbook.getSheet(sheetName);
                    if(sheet == null){
                        int i = 0;
                        i = 1;
                        logger.info("===========交易[" + sheetName + "],导入失败=============");
                        logInfoService.saveLog(sheetName + "sheet页，导入失败", "导入");
                        continue;
                    }
                    //获取交易、服务、场景信息
                    Map<String, Object> infoMap = excelImportService.getInterfaceAndServiceInfo(sheet,indexDO);
                    if(infoMap == null){
                        continue;
                    }
                    infoMap.put("operationPKs", operationPKs);
                    //获取接口、服务 输入 参数
                    Map<String, Object> inputMap = excelImportService.getInputArg(sheet);
                    //获取接口、服务 输出 参数
                    Map<String, Object> outMap = excelImportService.getOutputArg(sheet);
                    //获取接口头
                    Map<String, Object> headMap = excelImportService.getInterfaceHead(indexDO, workbook);
                    //获取公共信息
                    Map<String, String> publicMap = excelImportService.getPublicHead(indexDO);
                    if (infoMap == null || inputMap == null || outMap == null) {
                        msg.append(sheetName + "导入失败，");
                        continue;
                    }
                    logger.info("===========交易[" + sheetName + "],开始导入字段映射信息=============");
                    long time = java.lang.System.currentTimeMillis();
                    List result = excelImportService.executeImport(infoMap, inputMap, outMap, publicMap, headMap);

                    if (!result.get(0).equals("true")) {
                        logger.info("===========交易[" + sheetName + "],导入失败=============");
                        continue;
                    }
                    //TODO 插入标准接口映射
                    indexDO.getSystemId();
                    //IS_STANDARD  SERVICE_ID,operation_id systemId,type
                    String type = indexDO.getInterfacePoint();
                    String operationId = indexDO.getOperationId();
                    String cusumerSystem = indexDO.getConsumerSystem();
                    String cusumerSystemId = indexDO.getConsumerSystemId();
                    String providerSystem = indexDO.getProviderSystem();
                    String providerSystemId = indexDO.getProviderSystemId();
                    String invokeSystemId = "";
                    String _isStandard = Constants.INVOKE_TYPE_STANDARD_Y;
                    String serviceId = indexDO.getServiceId();
                    //TODO 消费方是不是不用插入标准提供方
                    if("Provider".equalsIgnoreCase(type)){
                        type = Constants.INVOKE_TYPE_CONSUMER;
                        invokeSystemId = cusumerSystemId;
                    }else{
                        type = Constants.INVOKE_TYPE_PROVIDER;;
                        invokeSystemId = providerSystemId;
                    }
                    ServiceInvoke invoke = excelImportService.addServiceInvoke(invokeSystemId,serviceId,operationId,type,_isStandard);
                    //增加调用关系
                    ServiceInvoke provider_invoke = (ServiceInvoke)result.get(1);
                    if(null != invoke){
                        String providerInvokeId = "";
                        String consumerInvokeId = "";
                        if(provider_invoke.getType().equals("0")){
                            providerInvokeId = provider_invoke.getInvokeId();
                            consumerInvokeId = invoke.getInvokeId();
                        }else{
                            consumerInvokeId = provider_invoke.getInvokeId();
                            providerInvokeId = invoke.getInvokeId();
                        }
                        //判断是否存在调用关系
                        Map map = new HashMap();
                        map.put("providerInvokeId",providerInvokeId);
                        map.put("consumerInvokeId",consumerInvokeId);
                        List<InterfaceInvoke> invokeList = interfaceInvokeService.findBy(map);
                        if(null == invokeList || invokeList.size()==0){
                            InterfaceInvoke interfaceInvoke = new InterfaceInvoke();
                            interfaceInvoke.setConsumerInvokeId(consumerInvokeId);
                            interfaceInvoke.setProviderInvokeId(providerInvokeId);
                            interfaceInvokeService.insert(interfaceInvoke);
                        }

                    }

                    long useTime = java.lang.System.currentTimeMillis() - time;
                    logger.info("===========交易[" + sheetName + "],导入完成，耗时" + useTime + "ms=============");
                } else {
                    logger.error("交易代码为空。");
                    logInfoService.saveLog("第交易代码为空。", "导入");
                }

            }
            //组织返回
            outPut(writer, msg);
        } catch (IOException e) {
            logger.error("导入出现异常:异常信息：" + e.getMessage());
            logInfoService.saveLog("导入出现异常:异常信息：" + e.getMessage(), "导入");
            writer.println("alert('导入失败，请查看日志!');");
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }
        GlobalImport.headMap.clear();//清空本次导入的业务报文头
        writer.println("window.location='/jsp/sysadmin/fieldmapping_import.jsp'");
        writer.println("</script>");
        writer.flush();
        writer.close();

        systemLogService.updateResult(operationLog);
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

    public void outPutError(java.io.PrintWriter writer,String systemId) {
        writer.println("<script language=\"javascript\">");
        writer.println("alert('缺少INDEX sheet页!');");
        if(null == systemId){
            writer.println("window.location='/jsp/sysadmin/fieldmapping_import.jsp'");
        }else if(systemId.equals("noSystem")){
            writer.println("window.location='/jsp/sysadmin/interface_import.jsp'");
        } else{
            writer.println("window.location='/jsp/interface/interface_list.jsp?systemId="+systemId+"'");
        }
        writer.println("</script>");
        writer.flush();
        writer.close();
    }

    public void outPut(java.io.PrintWriter writer, StringBuffer msg) {
        writer.println("<script language=\"javascript\">");
        if (msg.length() == 0) {
            writer.println("alert('导入成功!');");
        } else {
            writer.println("alert('" + msg.toString() + " 请查看日志！');");
        }
    }
}
