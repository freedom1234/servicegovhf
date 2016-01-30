package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.LogInfoServiceImpl;
import com.dc.esb.servicegov.service.impl.MappingFileImportSeviceImpl;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.vo.MappingImportIndexRowVO;
import com.dc.esb.servicegov.vo.MappingIndexHeadIndexVO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 映射文档导入处理
 */
@Controller
@RequestMapping("/mappingImport")
public class MappingFileImportController {
    protected Log logger = LogFactory.getLog(getClass());
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    private LogInfoServiceImpl logInfoService;
    @Autowired
    private MappingFileImportSeviceImpl mappingFileImportSevice;
    @Autowired
    private OperationServiceImpl operationService;



    @RequiresPermissions({"importExcel-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/fieldImport")
    public ModelAndView importFieldMapping(@RequestParam("file")
                                   MultipartFile file, Model model, HttpServletResponse response, @RequestParam("select")
                                   String operateFlag) {
        OperationLog operationLog = systemLogService.record("字段映射文档", "导入", "文件名称：" + file.getOriginalFilename());

        ModelAndView mv = new ModelAndView("sysadmin/fieldmapping_import");//导入完成后还是跳转到导入页面，页面显示结果信息，刷新导入日志
        String msg = importMappingFile(file, operateFlag);
        mappingFileImportSevice.setMsg(null);
        mv.addObject("message", msg);

        systemLogService.updateResult(operationLog);
        return mv;

    }

    /**
     * @param file 导入文件
     * @param operateFlag 覆盖标志
     * @return 处理结果
     */
    public String importMappingFile(MultipartFile file, String operateFlag){
        if(null == file){
            return "导入文件不能为空!";
        }
        boolean flag =  "Y".equalsIgnoreCase(operateFlag) ? true : false;
        mappingFileImportSevice.setOperateFlag(flag);
        String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook = null;
        InputStream is = null;
        try {
            is = file.getInputStream();
            if (extensionName.toLowerCase().equals(mappingFileImportSevice.XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (extensionName.toLowerCase().equals(mappingFileImportSevice.XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                mappingFileImportSevice.logMsg("导入文档格式不正确!");
                return  mappingFileImportSevice.getMsg();
            }
            Sheet indexSheet = workbook.getSheet(mappingFileImportSevice.INDEX_SHEET_NAME);//读取index页
            if(null != indexSheet){
                Row row = indexSheet.getRow(0);
                MappingIndexHeadIndexVO headRowVO = new MappingIndexHeadIndexVO(row);//根据index页头列名称获取顺序
                for(int i =1; i <= indexSheet.getLastRowNum(); i++){//提取index每条记录
                    MappingImportIndexRowVO indexVO = new MappingImportIndexRowVO(i, headRowVO, indexSheet.getRow(i));//index页一条记录
                    logger.info("===========开始导入INDEX页第" + indexVO.getIndexNum() + "条记录,接口代码[" + indexVO.getInterfaceId() + "]=============");
                    long time = java.lang.System.currentTimeMillis();
                    try {
                        if (mappingFileImportSevice.imoportIndexRecord(workbook, indexVO)) {
                            //根据版本信息发布版本
                            if (Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED.equalsIgnoreCase(indexVO.getOperationState()) || Constants.Operation.LIFE_CYCLE_STATE_ONLINE.equalsIgnoreCase(indexVO.getOperationState())) {
                                operationService.release(indexVO.getOperationId(), indexVO.getServiceId(), "导入发布");
                            }
                            long useTime = java.lang.System.currentTimeMillis() - time;
                            logger.info("===========INDEX页第" + indexVO.getIndexNum() + "条记录导入完成，,接口代码[" + indexVO.getInterfaceId() + "]，耗时" + useTime + "ms=============");
                        }
                    }catch (Exception e){
                        mappingFileImportSevice.logMsg("index页第" + indexVO.getIndexNum() + "条记录导入失败,导入时发生异常！异常信息："+e.getMessage());
                        logger.error(e, e);
                        continue;
                    }
                }
            }else{
               mappingFileImportSevice.logMsg("缺少INDEX页!");
                return  mappingFileImportSevice.getMsg();
            }
            Sheet indexExSheet = workbook.getSheet(mappingFileImportSevice.INDEX_EX_SHEET_NAME);//读取index_ex页
            if(null != indexExSheet){
                Row row = indexExSheet.getRow(0);
                MappingIndexHeadIndexVO headRowVO = new MappingIndexHeadIndexVO(row);//根据index页头列名称获取顺序
                for(int i =1; i <= indexExSheet.getLastRowNum(); i++){//提取index每条记录
                    MappingImportIndexRowVO indexExVO = new MappingImportIndexRowVO(i, headRowVO, indexExSheet.getRow(i));//一条记录
                    logger.info("===========开始导入INDEX_EX页第" + indexExVO.getIndexNum() + "条记录=============");
                    long time = java.lang.System.currentTimeMillis();
                    try {
                        if (mappingFileImportSevice.importIndexExRecord(workbook, indexExVO)) {
                            long useTime = java.lang.System.currentTimeMillis() - time;
                            logger.info("===========INDEX_EX页第" + indexExVO.getIndexNum() + "条记录导入完成，耗时" + useTime + "ms=============");
                        }
                    }catch (Exception e){
                        mappingFileImportSevice.logMsg("index_ex页第" + indexExVO.getIndexNum() + "条记录导入失败,导入时发生异常！");
                        logger.error(e, e);
                        continue;
                    }
                }
            }
            if(StringUtils.isNotEmpty( mappingFileImportSevice.getMsg())){
                mappingFileImportSevice.setMsg("导入完成，但导入过程中有异常，详情见日志！");
            }else{
                mappingFileImportSevice.setMsg("导入成功!");
            }
        }catch (IOException e){
           mappingFileImportSevice.logMsg("读取文档内容时发生IO错误，请检查文档格式！异常信息：" + e.getMessage());
            logger.error(e, e);
        }catch (Exception e){
           mappingFileImportSevice.logMsg("导入出现异常，导入失败！异常信息：" + e.getMessage()+"======"+e.getCause()+"====="+e.getStackTrace());
            logger.error(e, e);
        }
        finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
            mappingFileImportSevice.getInterfaceHeads().clear();
            return mappingFileImportSevice.getMsg();
        }

    }
}
