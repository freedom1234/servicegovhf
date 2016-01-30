package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.ExcelImportService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.hibernate.NonUniqueObjectException;
import org.jboss.seam.annotations.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("BaseExcelImportService")
@Transactional
public class ExcelImportServiceImpl extends AbstractBaseService implements ExcelImportService {
    protected Log logger = LogFactory.getLog(getClass());

    protected static final String INDEX_SHEET_NAME = "交易代码";
    protected static final String INDEX_SERVICE_ID = "服务名称";
    protected static final String INDEX_OPERATION_ID = "服务操作ID";
    protected static final String INDEX_CONSUMER = "调用方";
    protected static final String INDEX_PROVIDER = "提供方";
    protected static final String INDEX_INTERFACE_POINT = "接口方向";
    protected static final String INDEX_INTERFACE_HEAD = "业务报文头编号";
    protected static final String INDEX_INTERFACE_STATUS = "交易状态";
    protected static final String INDEX_OPERATION_STATE = "场景状态";
    protected static final String INDEX_ISSTANDARD = "是否标准";

    protected static int INDEX_SHEET_NAME_COL = 0;
    protected static int INDEX_SERVICE_ID_COL = 2;
    protected static int INDEX_OPERATION_ID_COL = 3;
    protected static int INDEX_CONSUMER_COL = 5;
    protected static int INDEX_PROVIDER_COL = 6;
    protected static int INDEX_INTERFACE_POINT_COL = 7;
    protected static int INDEX_INTERFACE_HEAD_COL = 18;
    protected static int INDEX_INTERFACE_STATUS_COL = 19;
    protected static int INDEX_OPERATION_STATE_COL = 20;
    protected static int INDEX_ISSTANDARD_COL = 21;

    protected static final int INTERFACE_INDEX_SHEET_NAME_COL = 0;
    protected static final int INTERFACE_SYSTEM_NAME_COL = 1;
    protected static final int INTERFACE_STATUS_COL = 2;
    protected static final int INTERFACE_POINT_COL = 3;
    protected static final int INTERFACE_ADD_OR_MODIFY_COL = 4;



    @Autowired
    InterfaceDAOImpl interfaceDao;
    @Autowired
    IdaDAOImpl idaDao;
    @Autowired
    ServiceDAOImpl serviceDao;
    @Autowired
    SDADAOImpl sdaDAO;
    @Autowired
    SystemDAOImpl systemDao;
    @Autowired
    ServiceInvokeDAOImpl serviceInvokeDAO;
    @Autowired
    ServiceCategoryDAOImpl serviceCategoryDAO;
    @Autowired
    OperationDAOImpl operationDAO;
    @Autowired
    InterfaceHeadDAOImpl interfaceHeadDAO;
    @Autowired
    InterfaceHeadRelateDAOImpl interfaceHeadRelateDAO;
    @Autowired
    LogInfoDAOImpl logInfoDAO;
    @Autowired
    protected VersionServiceImpl versionService;
    @Autowired
    protected MetadataServiceImpl metadataService;
    @Autowired
    protected OperationServiceImpl operationService;
    @Autowired
    protected OperationHisDAOImpl operationHisDAO;
    @Autowired
    protected LogInfoServiceImpl logInfoService;
    @Autowired
    protected  SDAServiceImpl sdaService;
    protected String initVersion = "1.0.0";
    protected static int readline = 0;

    /**
     * 执行入库
     * @param infoMap
     * @param inputMap
     * @param outMap
     * @param publicMap
     * @param headMap
     * @return
     */
    @Override
    public List executeStandardImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap, Map<String, String> publicMap, Map<String, Object> headMap) {
        return null;
    }

    public List executeInterfaceImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap){
        return null;
    }

    /**
     * 执行入库
     * @param infoMap
     * @param inputMap
     * @param outMap
     * @param publicMap
     * @param headMap
     * @return
     */
    @Override
    public List executeImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap, Map<String, String> publicMap, Map<String, Object> headMap) {
        com.dc.esb.servicegov.entity.Service service = (com.dc.esb.servicegov.entity.Service) infoMap.get("service");
        Operation operation = (Operation) infoMap.get("operation");
        Interface inter = (Interface) infoMap.get("interface");

        List<Ida> idainput = (List<Ida>) inputMap.get("idas");
        List<Ida> idaoutput = (List<Ida>) outMap.get("idas");
        List<SDA> sdainput = (List<SDA>) inputMap.get("sdas");
        List<SDA> sdaoutput = (List<SDA>) outMap.get("sdas");
        List<OperationPK>  operationPKs = (List<OperationPK>)infoMap.get("operationPKs");

        ServiceInvoke provider_invoke = null;
        List list = new ArrayList();
        //导入服务定义相关信息
        logger.info("导入服务定义信息...");
        if (insertService(service)) {
            //导入服务场景相关信息
            logger.info("导入服务场景信息...");
            boolean existsOper = insertOperation(service, operation, operationPKs);
            //维护调用关系
            //接口提供方 service_invoke 中 type=0
            String providerSystem = publicMap.get("providerSystem");
            //接口消费方 service_invoke 中 type=1
            String cusumerSystem = publicMap.get("cusumerSystem");
            //接口方向
            String interfacepoint = publicMap.get("interfacepoint");
            //TODO excel传入的是简称，已经转化为id
            HashMap<String,String> param = new HashMap<String, String>();
            param.put("systemAb",cusumerSystem);
            System system = systemDao.findUniqureBy(param);
            String systemId = system.getSystemId();
            String type = "1";
            if ("Provider".equalsIgnoreCase(interfacepoint)) {
                param = new HashMap<String, String>();
                param.put("systemAb",providerSystem);
                system = systemDao.findUniqureBy(param);
                systemId = system.getSystemId();
                type = "0";
            }
            //获取调用关系
            //TODO 加个interfaceID作为区别
            provider_invoke = serviceInvokeProviderQuery(service, operation, systemId, interfacepoint,inter.getInterfaceId());
            //获取消费关系
            //ServiceInvoke cusumer_invoke = serviceInvokeCusumerQuery(service, operation, cusumerSystem);
            //导入接口相关信息
            logger.info("导入接口定义信息...");
            List list1 = insertInterface(inter, service, operation, provider_invoke, systemId, type);
            boolean exists = (Boolean)list1.get(0);
            provider_invoke = (ServiceInvoke)list1.get(1);
            insertIDA(exists, inter, idainput, idaoutput);
            insertSDA(false, existsOper, operation, service, sdainput, sdaoutput);
            //处理业务报文头
            if (headMap != null && headMap.size()>0) {
                insertInterfaceHead(exists, inter, headMap,systemId);
            }
            list.add("true");
            list.add(provider_invoke);
        } else {
            list.add("false");
            return list;
        }
        return list;
    }

    public List parseInterfaceIndexSheet(Sheet indexSheet) {
        return null;
    }

    /**
     * 解析Index页,获取IndexDO对象
     * @param indexSheet
     * @return
     */
    @Override
    public List parseIndexSheet(Sheet indexSheet) {
        initIndexColnum(indexSheet);
        List<IndexDO> indexDOs = new ArrayList<IndexDO>();
        int endRow = indexSheet.getLastRowNum();
        StringBuffer msg = new StringBuffer();
        for (int i = 1; i <= endRow; i++) {
            Row row = indexSheet.getRow(i);
            // 读取每一行第一列，获取每个交易sheet名称
            String sheetName = getCell(row, INDEX_SHEET_NAME_COL);
            //接口消费方
            String consumerSystem = getCell(row, INDEX_CONSUMER_COL);
            HashMap<String,String> param = new HashMap<String, String>();
            param.put("systemAb",consumerSystem);
            System system = systemDao.findUniqureBy(param);

            if (null == system) {
                logger.error("" + consumerSystem + "系统不存在");
                logInfoService.saveLog("" + consumerSystem + "系统不存在", "导入");
                msg.append("" + consumerSystem + "系统不存在");
                continue;
            }
            String consumerSystemId = system.getSystemId();
            //接口提供方
            String providerSystem = getCell(row, INDEX_PROVIDER_COL);
            param = new HashMap<String, String>();
            param.put("systemAb",providerSystem);
            system = systemDao.findUniqureBy(param);
            if (null == system) {
                logger.error("" + providerSystem + "系统不存在");
                logInfoService.saveLog("" + providerSystem + "系统不存在", "导入");
                msg.append("" + providerSystem + "系统不存在");
                continue;
            }
            String providerSystemId = system.getSystemId();
            //接口方向
            String interfacePoint = getCell(row, INDEX_INTERFACE_POINT_COL);
            String interfaceHead = getCell(row, INDEX_INTERFACE_HEAD_COL);
            String operationId = getCell(row, INDEX_OPERATION_ID_COL);
            String interfaceStatus = getCell(row, INDEX_INTERFACE_STATUS_COL);
            if ("投产".equals(interfaceStatus)){
                interfaceStatus = Constants.INTERFACE_STATUS_TC;
            }else if ("废弃".equals(interfaceStatus)){
                interfaceStatus = Constants.INTERFACE_STATUS_FQ;
            }else{
                interfaceStatus = "";
            }
            //0.服务定义 1：审核通过，2：审核不通过, 3:已发布 4:已上线 5 已下线
            String operationState = getCell(row, INDEX_OPERATION_STATE_COL);
            if("服务定义".equals(operationState)){
                operationState = Constants.Operation.OPT_STATE_UNAUDIT;
            }else if("审核通过".equals(operationState)){
                operationState = Constants.Operation.OPT_STATE_PASS;
            }else if("审核不通过".equals(operationState)){
                operationState = Constants.Operation.OPT_STATE_UNPASS;
            }else if("已发布".equals(operationState)){
                operationState = Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED;
            }else if("已上线".equals(operationState)){
                operationState = Constants.Operation.LIFE_CYCLE_STATE_ONLINE;
            }else if("已下线".equals(operationState)){
                operationState = Constants.Operation.LIFE_CYCLE_STATE_DISCHARGE;
            }else {
                operationState = "";
            }
            String isStandard = getCell(row,INDEX_ISSTANDARD_COL);
            if("是".equals(isStandard)){
                isStandard = Constants.INVOKE_TYPE_STANDARD_Y;
            }else{
                isStandard = Constants.INVOKE_TYPE_STANDARD_N;
            }
            String temp = getCell(row,INDEX_SERVICE_ID_COL).replaceAll("（","(").replaceAll("）",")");
            String serviceId = temp.split("[()]+")[1];
            String systemId = consumerSystemId;
            String systemAb = consumerSystem;
            if ("Provider".equalsIgnoreCase(interfacePoint)) {
                systemId = providerSystemId;
                systemAb = providerSystem;
            }
            IndexDO indexDO = new IndexDO();
            indexDO.setConsumerSystem(consumerSystem);
            indexDO.setConsumerSystemId(consumerSystemId);
            indexDO.setSheetName(sheetName);
            indexDO.setInterfaceHead(interfaceHead);
            indexDO.setProviderSystem(providerSystem);
            indexDO.setProviderSystemId(providerSystemId);
            indexDO.setSystemId(systemId);
            indexDO.setInterfacePoint(interfacePoint);
            indexDO.setOperationId(operationId);
            indexDO.setServiceId(serviceId);
            indexDO.setSystemAb(systemAb);
            indexDO.setInterfaceStatus(interfaceStatus);
            indexDO.setOperationState(operationState);
            indexDO.setIsStandard(isStandard);
            indexDOs.add(indexDO);
        }
        List list = new ArrayList();
        list.add(indexDOs);
        list.add(msg);
        return list;
    }

    @Override
    public Map<String, String> getPublicHead(IndexDO indexDO){
        Map<String, String> publicMap = new HashMap<String, String>();
        publicMap.put("providerSystem", indexDO.getProviderSystem());
        publicMap.put("cusumerSystem", indexDO.getConsumerSystem());
        publicMap.put("interfacepoint", indexDO.getInterfacePoint());
        return publicMap;
    }

    @Override
    public Map<String, Object> getInterfaceHead(IndexDO indexDO, Workbook workbook){
        Map<String, Object> headMap = new HashMap<String, Object>();
        String interfaceHead = indexDO.getInterfaceHead();
        if (interfaceHead != null && !"".equals(interfaceHead)) {
            if (GlobalImport.headMap.get(interfaceHead) == null) {

                Sheet headSheet = workbook.getSheet(interfaceHead);
                if (headSheet == null) {
                    logger.info("交易[" + indexDO.getSheetName() + "]，没找到业务报文头[" + interfaceHead + "]");
                } else {
                    headMap = getInterfaceHead(headSheet);
                    if (headMap != null) {
                        headMap.put("headName", interfaceHead);
                    }
                }
            } else {
                headMap = new HashMap<String, Object>();
                headMap.put("headName", interfaceHead);
            }
        }
        return headMap;
    }

    public  Map<String, Object> getStandardInputArg(Sheet sheet) {
        return null;
    }

    public Map<String, Object> getInterfaceInputArg(Sheet sheet){
        return null;
    }

    public  Map<String, Object> getInputArg(Sheet sheet) {
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<Ida> idas = new ArrayList<Ida>();
        List<SDA> sdas = new ArrayList<SDA>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();

        int order = 0;
        for (int j = start; j <= end; j++) {
            Ida ida = new Ida();
            SDA sda = new SDA();
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null) continue;
            Cell cellObj = sheetRow.getCell(0);

            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                if ("输出".equals(cell)) {
                    readline = j++;
                    break;
                }
                ida.setStructName(isNull(cell));
            }
            cellObj = sheetRow.getCell(1);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructAlias(isNull(cell));
            }
            cellObj = sheetRow.getCell(2);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(3);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setLength(isNull(cell));
            }

            cellObj = sheetRow.getCell(4);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRequired(isNull(cell));
            }

            cellObj = sheetRow.getCell(5);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                ida.setRemark(remark);
            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setMetadataId(isNull(cell));
                sda.setMetadataId(isNull(cell));
                sda.setStructName(isNull(cell));

            }
            ida.setSeq(order);

            cellObj = sheetRow.getCell(8);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setStructAlias(isNull(cell));
            }

            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(10);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setLength(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(13);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                sda.setRemark(remark);
            }

            if (ida.getMetadataId() != null && !"".equals(ida.getMetadataId())  && !"end".equalsIgnoreCase(sda.getRemark())) {
                Metadata metadata = metadataService.findUniqueBy("metadataId", sda.getMetadataId());
                if (metadata == null) {
                    logger.error(sheet.getSheetName() + "页,元数据[" + ida.getMetadataId() + "]未配置，导入失败...");
                    msg.append(ida.getMetadataId()).append(",");
                    flag = false;
                }
            }
            sda.setSeq(order);

            idas.add(ida);
            sdas.add(sda);
            order++;
        }
        if (!flag) {
            logInfoService.saveLog(sheet.getSheetName() + "页,元数据[" + msg.toString() + "]未配置，导入失败...", "导入(输入)");
            return null;
        }
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("idas", idas);
        resMap.put("sdas", sdas);
        return resMap;
    }

    public Map<String, Object> getStandardOutputArg(Sheet sheet) {
        return null;
    }

    public Map<String, Object> getInterfaceOutputArg(Sheet sheet){
        return null;
    }
    public Map<String, Object> getOutputArg(Sheet sheet) {
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<Ida> idas = new ArrayList<Ida>();
        List<SDA> sdas = new ArrayList<SDA>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();
        int order = 0;
        for (int j = start; j <= end; j++) {
            Ida ida = new Ida();
            SDA sda = new SDA();
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null) continue;
            Cell cellObj = sheetRow.getCell(0);

            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                if ("输出".equals(cell)) {
                    continue;
                }
                ida.setStructName(isNull(cell));
            }
            cellObj = sheetRow.getCell(1);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructAlias(isNull(cell));
            }
            cellObj = sheetRow.getCell(2);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(3);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setLength(isNull(cell));
            }

            cellObj = sheetRow.getCell(4);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(5);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                ida.setRemark(remark);
            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setMetadataId(isNull(cell));
                sda.setStructName(isNull(cell));
                sda.setMetadataId(isNull(cell));
            }
            ida.setSeq(order);

            cellObj = sheetRow.getCell(8);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setStructAlias(isNull(cell));
            }

            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(10);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setLength(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(13);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                sda.setRemark(remark);
            }

            if (ida.getMetadataId() != null && !"".equals(ida.getMetadataId()) && !"end".equalsIgnoreCase(sda.getRemark())) {
                Metadata metadata = metadataService.findUniqueBy("metadataId", sda.getMetadataId());
                if (metadata == null) {
                    logger.error(sheet.getSheetName() + "页,元数据[" + ida.getMetadataId() + "]未配置，导入失败...");
                    msg.append(ida.getMetadataId()).append(",");
                    flag = false;
                }
            }
//e

            sda.setSeq(order);

            idas.add(ida);
            sdas.add(sda);
            order++;

        }

        if (!flag) {
            logInfoService.saveLog(sheet.getSheetName() + "页,元数据[" + msg.toString() + "]未配置，导入失败...", "导入(输出)");
            return null;
        }

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("idas", idas);
        resMap.put("sdas", sdas);

        return resMap;
    }

    public Map<String, Object> getServiceInfo(Sheet tranSheet,IndexDO indexDO) {

        return null;
    }

    /**
     * 获取接口信息
     * @param tranSheet
     * @return
     */
    public Map<String, Object> getInterfaceInfo(Sheet tranSheet){
        return null;
    }
    /**
     * 获取交易、服务、场景信息
     *
     * @return
     */
    public Map<String, Object> getInterfaceAndServiceInfo(Sheet tranSheet) {
        boolean flag = true;
        // 读取每个sheet页交易信息与服务信息
        int start = tranSheet.getFirstRowNum();
        int end = tranSheet.getLastRowNum();
        Interface inter = new Interface();
        inter.setInterfaceId(tranSheet.getSheetName());
        com.dc.esb.servicegov.entity.Service service = new com.dc.esb.servicegov.entity.Service();
        Operation oper = new Operation();
        for (int j = start; j <= end; j++) {
            Row sheetRow = tranSheet.getRow(j);
            if(sheetRow == null){
                continue;
            }
            String tranCode = "";
            String tranName = "";
            String tranDesc = "";
            String serviceName = "";
            String serviceId = "";
            String operId = "";
            String operName = "";
            String serviceDesc = "";
            String operDesc = "";
            int cellStart = sheetRow.getFirstCellNum();
            int cellEnd = sheetRow.getLastCellNum();

            for (int k = cellStart; k < cellEnd; k++) {

                Cell cellObj = sheetRow.getCell(k);
                if (cellObj != null) {

                    String cell = ExcelTool.getInstance().getCellContent(
                            cellObj);
                    if ("交易码".equals(cell) && k==0) {
                        //TODO 类型报错
                        sheetRow.getCell(k + 1).setCellType(Cell.CELL_TYPE_STRING);
                        tranCode = sheetRow.getCell(k + 1).getStringCellValue();
                        if (tranCode == null || "".equals(tranCode)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，交易码为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，交易码为空", "导入");
                            flag = false;
                        }
                        inter.setEcode(tranCode);
                    } else if ("服务名称".equals(cell)) {
                        serviceName = sheetRow.getCell(k + 1)
                                .getStringCellValue();
                        if (serviceName == null || "".equals(serviceName)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，服务名称为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，服务名称为空", "导入");
                            flag = false;
                        }

                        try {
                            String[] req = getContext(serviceName);
                            serviceName = req[0];
                            serviceId = req[1];
                            service.setServiceName(serviceName);
                            service.setServiceId(serviceId);
                        } catch (Exception e) {
                            logger.error("服务名称格式不正确，格式应为为：服务名称(服务ID)");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，服务名称格式不正确，格式应为为：服务名称(服务ID)", "导入");
                            flag = false;
                        }

                        break;
                    } else if ("交易名称".equals(cell) && k==0) {
                        tranName = sheetRow.getCell(k + 1).getStringCellValue();
                        /*if (tranName == null || "".equals(tranName)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，交易名称为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，交易名称为空", "导入");
                            flag = false;
                        }*/
                        inter.setInterfaceName(tranName);
                    } else if ("接口功能描述".equals(cell) && k==0) {
                        tranDesc = sheetRow.getCell(k + 1).getStringCellValue();
                        inter.setDesc(tranDesc);
                    } else if ("服务操作名称".equals(cell)) {
                        operName = sheetRow.getCell(k + 1).getStringCellValue();
                        if (operName == null || "".equals(operName)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，服务操作名称为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，服务操作名称为空", "导入");
                            flag = false;
                        }

                        try {
                            String[] req = getContext(operName);
                            operName = req[0];
                            operId = req[1];
                            oper.setOperationId(operId);
                            oper.setOperationName(operName);
                        } catch (Exception e) {
                            logger.error("服务操作名称格式不正确，格式应为为：服务操作名称(操作ID)");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，服务操作名称格式不正确，格式应为为：服务操作名称(操作ID)", "导入");
                            flag = false;
                        }
                        break;
                    } else if ("服务描述".equals(cell)) {
                        serviceDesc = sheetRow.getCell(k + 1)
                                .getStringCellValue();
//                        if (serviceDesc == null || "".equals(serviceDesc)) {
//                            logger.error(tranSheet.getSheetName()
//                                    + "sheet页，服务描述为空");
//                            logInfoService.saveLog(tranSheet.getSheetName()
//                                    + "sheet页，服务描述为空", "导入");
//                            flag = false;
//                        }
                        service.setDesc(serviceDesc);
                        break;
                    } else if ("服务操作描述".equals(cell)) {
                        operDesc = sheetRow.getCell(k + 1).getStringCellValue();
//                        if (operDesc == null || "".equals(operDesc)) {
//                            logger.error(tranSheet.getSheetName()
//                                    + "sheet页，服务操作描述为空");
//                            logInfoService.saveLog(tranSheet.getSheetName()
//                                    + "sheet页，服务操作描述为空", "导入");
//                            flag = false;
//                        }
                        oper.setOperationDesc(operDesc);
                        break;
                    } else if ("原始接口".equals(cell)) {
                        // 将表头跳过,获取接口字段信息
                        readline = j += 3;
                        break;
                    }
                }
            }
        }

        //信息不正确返回空
        if (!flag) {
            return null;
        }
        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("interface", inter);
        resMap.put("service", service);
        resMap.put("operation", oper);

        return resMap;
    }

    public Map<String, Object> getInterfaceAndServiceInfo(Sheet tranSheet,IndexDO indexDO) {
        return null;
    }
    protected Map<String, Object> getInterfaceHead(Sheet sheet) {
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        Map<String, Object> resMap = new HashMap<String, Object>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = sheet.getFirstRowNum();
        int end = sheet.getLastRowNum();
        int inputIndex = 0;
        int outIndex = 0;
        for (int i = 0; i <= end; i++) {
            Row sheetRow = sheet.getRow(i);
            if(sheetRow == null) continue;
            Cell cellObj = sheetRow.getCell(0);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                if ("输入".equals(cell)) {
                    inputIndex = i + 1;
                } else if ("输出".equals(cell)) {
                    outIndex = i + 1;
                    break;
                }
            }
        }
        //int order = 0;
        List<Ida> input = new ArrayList<Ida>();

        String tempHeadId = UUID.randomUUID().toString();//临时替代headId,等保存head后，按此更新sda
        Map<String, SDA> sdas = sdaService.genderSDAAuto(tempHeadId);
        resMap.put("inputTempHeadId", tempHeadId);
        List<SDA> inputArraySdas = new ArrayList<SDA>();//sda数组类型的列表
        inputArraySdas.add(sdas.get("request"));
        for (int i = inputIndex; i < outIndex - 1; i++) {
            Ida ida = new Ida();
            Row sheetRow = sheet.getRow(i);
            if(sheetRow == null) continue;

            SDA sda = genderSDA(sheetRow, inputArraySdas, tempHeadId, i);
            if(sda != null){
                ida.setSdaId(sda.getId());
                ida.setXpath(sda.getXpath());
            }

            Cell cellObj = sheetRow.getCell(0);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructName(isNull(cell));
                if ("".equals(isNull(cell))) {
                    continue;
                }
            }

            cellObj = sheetRow.getCell(1);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructAlias(isNull(cell));
            }

            cellObj = sheetRow.getCell(2);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(3);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setLength(isNull(cell));
            }
            cellObj = sheetRow.getCell(4);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRequired(isNull(cell));
            }

            cellObj = sheetRow.getCell(5);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRemark(isNull(cell));
            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setMetadataId(isNull(cell));
                if (cell != null && !"".equals(cell)) {
                    Metadata metadata = metadataService.findUniqueBy("metadataId", cell);
                    if (metadata == null) {
                        logger.error(sheet.getSheetName() + "页,元数据[" + cell + "]未配置，导入失败...");
                        msg.append(cell).append(",");
                        flag = false;
                    }
                }
            }
            input.add(ida);
            ida.setSeq(i);
            //order++;
        }

        //order = 0;
        List<Ida> output = new ArrayList<Ida>();

        String outTempHeadId = UUID.randomUUID().toString();//临时替代headId,等保存head后，按此更新sda
        resMap.put("outTempHeadId", outTempHeadId);
        List<SDA> outArraySdas = new ArrayList<SDA>();//sda数组类型的列表
        outArraySdas.add(sdas.get("response"));
        for (int j = outIndex; j <= end; j++) {
            Ida ida = new Ida();
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null) continue;

            SDA sda = genderSDA(sheetRow, outArraySdas, outTempHeadId, j);
            if(sda != null){
                ida.setSdaId(sda.getId());
                ida.setXpath(sda.getXpath());
            }

            Cell cellObj = sheetRow.getCell(0);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructName(isNull(cell));
                if ("".equals(isNull(cell))) {
                    continue;
                }
            }

            cellObj = sheetRow.getCell(1);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setStructAlias(isNull(cell));
            }

            cellObj = sheetRow.getCell(2);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setType(isNull(cell));
            }

            cellObj = sheetRow.getCell(3);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setLength(isNull(cell));
            }
            cellObj = sheetRow.getCell(4);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRequired(isNull(cell));
            }

            cellObj = sheetRow.getCell(5);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setRemark(isNull(cell));
            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                ida.setMetadataId(isNull(cell));
                if (cell != null && !"".equals(cell)) {
                    Metadata metadata = metadataService.findUniqueBy("metadataId", cell);
                    if (metadata == null) {

                        logger.error(sheet.getSheetName() + "页,元数据[" + cell + "]未配置，导入失败...");
                        //logInfoService.saveLog(sheet.getSheetName()+"页,元数据["+cell+"]未配置，导入失败...","导入");
                        msg.append(cell).append(",");
                        flag = false;
                        //return null;
                    }
                }
            }
            output.add(ida);
            ida.setSeq(j);
            //order++;
        }

        if (!flag) {
            logInfoService.saveLog(sheet.getSheetName() + "页,元数据[" + msg.toString() + "]未配置，导入失败...", "导入报文头");
            return null;
        }
        resMap.put("sdas", sdas);
        resMap.put("input", input);
        resMap.put("output", output);

        return resMap;
    }


    public static class IndexDO {
        private String sheetName;
        private String consumerSystem;
        private String consumerSystemId;
        private String providerSystem;
        private String providerSystemId;
        private String interfacePoint;
        private String interfaceHead;
        private String systemId;
        private String operationId;
        private String serviceId;
        private String systemAb;
        private String interfaceStatus;
        private String invokeType;
        private String operationState;
        private String isStandard;
        //原始接口导入，新增：0，修改：1
        private String optType;

        public String getOptType() {
            return optType;
        }

        public void setOptType(String optType) {
            this.optType = optType;
        }

        public String getInvokeType() {
            return invokeType;
        }

        public void setInvokeType(String invokeType) {
            this.invokeType = invokeType;
        }

        public String getIsStandard() {
            return isStandard;
        }

        public void setIsStandard(String isStandard) {
            this.isStandard = isStandard;
        }

        public String getInterfaceStatus() {
            return interfaceStatus;
        }

        public void setInterfaceStatus(String interfaceStatus) {
            this.interfaceStatus = interfaceStatus;
        }

        public String getOperationState() {
            return operationState;
        }

        public void setOperationState(String operationState) {
            this.operationState = operationState;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getProviderSystem() {
            return providerSystem;
        }

        public void setProviderSystem(String providerSystem) {
            this.providerSystem = providerSystem;
        }

        public String getInterfacePoint() {
            return interfacePoint;
        }

        public void setInterfacePoint(String interfacePoint) {
            this.interfacePoint = interfacePoint;
        }

        public String getInterfaceHead() {
            return interfaceHead;
        }

        public void setInterfaceHead(String interfaceHead) {
            this.interfaceHead = interfaceHead;
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public String getConsumerSystem() {
            return consumerSystem;
        }

        public void setConsumerSystem(String consumerSystem) {
            this.consumerSystem = consumerSystem;
        }

        public String getConsumerSystemId() {
            return consumerSystemId;
        }

        public void setConsumerSystemId(String consumerSystemId) {
            this.consumerSystemId = consumerSystemId;
        }

        public String getProviderSystemId() {
            return providerSystemId;
        }

        public void setProviderSystemId(String providerSystemId) {
            this.providerSystemId = providerSystemId;
        }

        public String getOperationId() {
            return operationId;
        }

        public void setOperationId(String operationId) {
            this.operationId = operationId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getSystemAb() {
            return systemAb;
        }

        public void setSystemAb(String systemAb) {
            this.systemAb = systemAb;
        }

    }

    public void insertInterfaceHead(boolean exists, Interface inter, Map<String, Object> headMap,String systemId) {

        //如果接口存在，且不覆盖 直接返回
        if (exists && !GlobalImport.operateFlag) {
            return;
        }

        String headName = headMap.get("headName").toString();
        //如果本次导入已导入该报文头，直接建立关系
        InterfaceHead interfaceHead = GlobalImport.headMap.get(headName);
        if (interfaceHead != null) {
            InterfaceHeadRelate relate = new InterfaceHeadRelate();
            relate.setHeadId(interfaceHead.getHeadId());
            relate.setInterfaceId(inter.getInterfaceId());
            //同一个session插入相同主键
            try {
                interfaceHeadRelateDAO.save(relate);
            }catch (NonUniqueObjectException e){
//                e.printStackTrace();
                return ;
            }
//            interfaceHeadRelateDAO.save(relate);
            return;
        }

        Map<String,String> map = new HashMap<String, String>();
        map.put("headName",headName);
        map.put("systemId",systemId);
        InterfaceHead headDB = interfaceHeadDAO.findUniqureBy(map);

        if (headDB != null) {
            if (GlobalImport.operateFlag) {
                //TODO null != headDB是更新不是删除吧？删除后以前的Interface_Head_Relate也没了
                //删除老的Ida
                String hql = "delete from Ida t where t.headId = '"+headDB.getHeadId()+"' ";
                idaDao.exeHql(hql);
                List<SDA> sdaList = sdaDAO.findBy("headId", headDB.getHeadId());
                for(SDA sda : sdaList){
                    sdaDAO.getSession().delete(sda);
                }
//                String hql2 = " delete from " + SDA.class.getName()+ " where headId=?";
//                sdaDAO.exeHql(hql2, headDB.getHeadId());
//                interfaceHeadDAO.delete(headDB.getHeadId());
            } else {
                if (exists) {
                    return;
                }
                //接口不存在，建立关系
                InterfaceHeadRelate relate = new InterfaceHeadRelate();
                relate.setHeadId(headDB.getHeadId());
                relate.setInterfaceId(inter.getInterfaceId());
                interfaceHeadRelateDAO.save(relate);
                return;

            }
        }else{
            headDB = new InterfaceHead();
        }
        headDB.setSystemId(systemId);
        headDB.setHeadName(headName);
        headDB.setHeadDesc(headName);
        interfaceHeadDAO.save(headDB);

        InterfaceHeadRelate relate = new InterfaceHeadRelate();
        relate.setHeadId(headDB.getHeadId());
        relate.setInterfaceId(inter.getInterfaceId());
        interfaceHeadRelateDAO.save(relate);

        String idaheadId = headDB.getHeadId();

        Map<String, SDA> sdas = (Map<String, SDA>)headMap.get("sdas");
        //添加IDA
        Ida ida = new Ida();
        String rootId = "", requestId = "", responseId = "";
        ida.setHeadId(idaheadId);
        ida.setParentId(null);
        ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
        ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
        SDA sdaRoot = sdas.get(Constants.ElementAttributes.REQUEST_NAME);
        ida.setSdaId(sdaRoot.getId());
        ida.setState(Constants.IDA_STATE_COMMON);
        ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
        idaDao.save(ida);
        rootId = ida.getId();

        ida = new Ida();
        ida.setHeadId(idaheadId);
        ida.setParentId(rootId);
        ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
        ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
        ida.setSeq(0);
        SDA reqRoot = sdas.get(Constants.ElementAttributes.REQUEST_NAME);
        ida.setSdaId(reqRoot.getId());
        ida.setState(Constants.IDA_STATE_COMMON);
        ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
        idaDao.save(ida);
        requestId = ida.getId();

        ida = new Ida();
        ida.setHeadId(idaheadId);
        ida.setParentId(rootId);
        ida.setSeq(1);
        ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
        ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
        SDA resRoot = sdas.get(Constants.ElementAttributes.RESPONSE_NAME);
        ida.setSdaId(resRoot.getId());
        ida.setState(Constants.IDA_STATE_COMMON);
        ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
        idaDao.save(ida);
        responseId = ida.getId();

        List<Ida> input = (List<Ida>) headMap.get("input");
        List<Ida> output = (List<Ida>) headMap.get("output");
        for (int i = 0; i < input.size(); i++) {
            ida = input.get(i);
            ida.setParentId(requestId);
            ida.setHeadId(idaheadId);
            //ida.setArgType("headinput");
            idaDao.save(ida);
        }

        for (int i = 0; i < output.size(); i++) {
            ida = output.get(i);
            ida.setParentId(responseId);
            ida.setHeadId(idaheadId);
            //ida.setArgType("headoutput");
            idaDao.save(ida);
        }
        String inputTempHeadId = (String)headMap.get("inputTempHeadId");
        updateSDAByTempHeadId(inputTempHeadId, headDB.getHeadId());
        String outTempHeadId = (String)headMap.get("outTempHeadId");
        updateSDAByTempHeadId(outTempHeadId, headDB.getHeadId());
        //将本次导入的报文头缓存到map,导入有可能是同一个报文头
        GlobalImport.headMap.put(headName, headDB);
    }

    protected void insertIDA(boolean exists, Interface inter, List<Ida> idainput, List<Ida> idaoutput) {
        //添加报文，自动生成固定报文头<root><request><response>
        //root
        Ida ida = new Ida();
        String rootId = "", requestId = "", responseId = "";
        //覆盖
        if (GlobalImport.operateFlag) {
            //先删除
            String hql = "delete from Ida where interfaceId=?";
            sdaDAO.exeHql(hql, inter.getInterfaceId());
        } else {
            //不覆盖，直接return
            if (exists) {
                return;
            }
        }

        if (!exists || (exists && GlobalImport.operateFlag)) {
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(null);
            ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
            ida.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
            ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
            ida.setState(Constants.IDA_STATE_COMMON);
            idaDao.save(ida);
            rootId = ida.getId();

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(rootId);
            ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
            ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
            ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
            ida.setSeq(0);
            ida.setState(Constants.IDA_STATE_COMMON);
            idaDao.save(ida);
            requestId = ida.getId();

            ida = new Ida();
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(rootId);
            ida.setSeq(1);
            ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
            ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
            ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
            ida.setState(Constants.IDA_STATE_COMMON);
            idaDao.save(ida);
            responseId = ida.getId();
        }

        String parentId = null;
        for (int i = 0; i < idainput.size(); i++) {
            ida = idainput.get(i);
            //判断ida是否存在
//			Map<String,String> paramMap = new HashMap<String, String>();
//			paramMap.put("interfaceId",inter.getInterfaceId());
//			paramMap.put("structName",ida.getStructName());
//			if(ida.getMetadataId()!=null && !"".equals(ida.getMetadataId())) {
//				paramMap.put("metadataId", ida.getMetadataId());
//			}
//			paramMap.put("argType","input");//输入参数
//			Ida idadb = idaDao.findUniqureBy(paramMap);
//			if(idadb!=null){
//				ida.setId(idadb.getId());
//				ida.setParentId(idadb.getParentId());
//				continue;
//			}
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(requestId);
            if (parentId != null) {
                ida.setParentId(parentId);
            }
            ida.setState(Constants.IDA_STATE_COMMON);
            //包含bug，当节点end后，下一节点 不在request 或 response下 就会出现问题，
            if ("end".equalsIgnoreCase(ida.getRemark()) || "不映射".equalsIgnoreCase(ida.getRemark()) || ida.getStructName() == null || "".equals(ida.getStructName())) {
                if ("end".equalsIgnoreCase(ida.getRemark())) {
                    parentId = null;
                }
//                continue;
                ida.setState(Constants.IDA_STATE_DISABLE);
            }
            //ida.setArgType("input");
            idaDao.save(ida);
            //包含子节点
            if (ida.getRemark() != null && ida.getRemark().trim().indexOf("start") == 0) {
                parentId = ida.getId();
            }
        }

        parentId = null;
        for (int i = 0; i < idaoutput.size(); i++) {
            ida = idaoutput.get(i);
            //判断ida是否存在
//			Map<String,String> paramMap = new HashMap<String, String>();
//			paramMap.put("interfaceId",inter.getInterfaceId());
//			paramMap.put("structName",ida.getStructName());
//			if(ida.getMetadataId()!=null && !"".equals(ida.getMetadataId())) {
//				paramMap.put("metadataId", ida.getMetadataId());
//			}
//			paramMap.put("argType","output");
//			Ida idadb = idaDao.findUniqureBy(paramMap);
//			if(idadb!=null){
//				ida.setId(idadb.getId());
//				ida.setParentId(idadb.getParentId());
//				continue;
//
//			}
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(responseId);
            if (parentId != null) {
                ida.setParentId(parentId);
            }
            ida.setState(Constants.IDA_STATE_COMMON);
            if ("end".equalsIgnoreCase(ida.getRemark()) || "不映射".equalsIgnoreCase(ida.getRemark()) || ida.getStructName() == null || "".equals(ida.getStructName())) {
                if ("end".equalsIgnoreCase(ida.getRemark())) {
                    parentId = null;
                }
//                continue;
                ida.setState(Constants.IDA_STATE_DISABLE);
            }
            //ida.setArgType("output");
            idaDao.save(ida);
            //包含子节点
            if (ida.getRemark() != null && ida.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                parentId = ida.getId();
            }
        }

    }

    protected void insertSDA(boolean isStandard, boolean existsOper, Operation operation, com.dc.esb.servicegov.entity.Service service, List<SDA> sdainput, List<SDA> sdaoutput) {
        SDA sda = new SDA();
        String rootId = "", requestId = "", responseId = "";

        //覆盖
        if (GlobalImport.operateFlag) {
            //先删除
            String hql = "delete from SDA where operationId=? and serviceId=?";
            sdaDAO.exeHql(hql, operation.getOperationId(), service.getServiceId());
        } else {
            if (existsOper) {
                return;
            }
        }
        if (!existsOper || (existsOper && GlobalImport.operateFlag)) {
            sda.setId(UUID.randomUUID().toString());
            sda.setOperationId(operation.getOperationId());
            sda.setParentId(null);
            sda.setStructName(Constants.ElementAttributes.ROOT_NAME);
            sda.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
            sda.setServiceId(service.getServiceId());
            sda.setXpath(Constants.ElementAttributes.ROOT_XPATH);
            sdaDAO.save(sda);
            rootId = sda.getId();

            sda = new SDA();
            sda.setId(UUID.randomUUID().toString());
            sda.setOperationId(operation.getOperationId());
            sda.setParentId(rootId);
            sda.setStructName(Constants.ElementAttributes.REQUEST_NAME);
            sda.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
            sda.setServiceId(service.getServiceId());
            sda.setSeq(0);
            sda.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
            sdaDAO.save(sda);
            requestId = sda.getId();

            sda = new SDA();
            sda.setId(UUID.randomUUID().toString());
            sda.setOperationId(operation.getOperationId());
            sda.setParentId(rootId);
            sda.setSeq(1);
            sda.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
            sda.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
            sda.setServiceId(service.getServiceId());
            sda.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
            sdaDAO.save(sda);
            responseId = sda.getId();
        }


        String parentId = null;
        String parentPath = "/request";
        for (int i = 0; i < sdainput.size(); i++) {
            sda = sdainput.get(i);

            //判断ida是否存在
//			Map<String,String> paramMap = new HashMap<String, String>();
//			paramMap.put("operationId",operation.getOperationId());
//			paramMap.put("structName",sda.getStructName());
//			if(sda.getMetadataId()!=null && !"".equals(sda.getMetadataId())) {
//				paramMap.put("metadataId", sda.getMetadataId());
//			}
//			paramMap.put("serviceId",service.getServiceId());
//			paramMap.put("argType","input");
//			SDA sdadb = sdaDAO.findUniqureBy(paramMap);
//			if(sdadb!=null){
//				sda.setId(sdadb.getId());
//				sda.setParentId(sdadb.getParentId());
//				continue;
//			}
//            sda.setId(UUID.randomUUID().toString());
            sda.setOperationId(operation.getOperationId());
            sda.setParentId(requestId);
            sda.setServiceId(service.getServiceId());
            sda.setXpath(parentPath + "/" + sda.getMetadataId());
            if (parentId != null) {
                sda.setParentId(parentId);
            }
            if ("end".equalsIgnoreCase(sda.getRemark()) || "不映射".equalsIgnoreCase(sda.getRemark()) || sda.getStructName() == null || "".equals(sda.getStructName())) {
                if ("end".equalsIgnoreCase(sda.getRemark())) {
                    parentId = null;
                    parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
                    sda.setXpath(parentPath+"/");
                }
//                continue;
                sda.setServiceId(null);
                sda.setOperationId(null);
                sda.setParentId(null);
            }
            //sda.setArgType("input");
            sdaDAO.save(sda);
            if(!isStandard){
                Ida ida = idaDao.findUniqueBy("sdaId", sda.getId());
                ida.setXpath(sda.getXpath());
                idaDao.save(ida);
            }
            //包含子节点
            if (sda.getRemark() != null  &&  sda.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                parentId = sda.getId();
                parentPath = parentPath + "/" + sda.getMetadataId();
            }
        }

        parentId = null;
        parentPath = "/response";
        for (int i = 0; i < sdaoutput.size(); i++) {
            sda = sdaoutput.get(i);
//            sda.setId(UUID.randomUUID().toString());
            sda.setOperationId(operation.getOperationId());
            sda.setParentId(responseId);
            sda.setServiceId(service.getServiceId());
            sda.setXpath(parentPath + "/" + sda.getMetadataId());
            if (parentId != null) {
                sda.setParentId(parentId);
            }
            if ("end".equalsIgnoreCase(sda.getRemark()) || "不映射".equalsIgnoreCase(sda.getRemark()) || sda.getStructName() == null || "".equals(sda.getStructName())) {
                if ("end".equalsIgnoreCase(sda.getRemark())) {
                    parentId = null;
                    parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
                    sda.setXpath(parentPath+"/");
                }
//                continue;
                sda.setServiceId(null);
                sda.setOperationId(null);
                sda.setParentId(null);
            }
            //sda.setArgType("output");
            sdaDAO.save(sda);
            if(!isStandard){
                Ida ida = idaDao.findUniqueBy("sdaId", sda.getId());
                ida.setXpath(sda.getXpath());
                idaDao.save(ida);
            }
            //包含子节点
            if ( sda.getRemark() != null && sda.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                parentId = sda.getId();
                parentPath = parentPath + "/" + sda.getMetadataId();
            }
        }
    }

    protected List insertStrandardInvoke(Interface inter,com.dc.esb.servicegov.entity.Service service, Operation operation, ServiceInvoke provider_invoke, String providerSystem, String type) {
        return null;
    }

    protected List insertInterface(Interface inter, com.dc.esb.servicegov.entity.Service service, Operation operation, ServiceInvoke provider_invoke, String providerSystem, String type) {
        Map<String, String> paramMap = new HashMap<String, String>();
        boolean exists = false;
        List list = new ArrayList();
        //已存在提供系统关系
        if (provider_invoke != null) {
            String interfaceId = provider_invoke.getInterfaceId();
            if (interfaceId != null && !"".equals(interfaceId)) {
                Interface interfaceDB = interfaceDao.getEntity(interfaceId);
                //覆盖
                if (GlobalImport.operateFlag) {
                    interfaceDB.setInterfaceName(inter.getInterfaceName());
                    interfaceDB.setEcode(inter.getEcode());
                    interfaceDB.setStatus(inter.getStatus());
                }
                String versionId = interfaceDB.getVersionId();/**接口版本管理，如果未存在新增版本信息，否则编辑**/
                if (versionId == null || "".equals(versionId)) {
                    versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
                    interfaceDB.setVersionId(versionId);
                } else {
                    versionService.editVersion(versionId);
                }
                interfaceDB.setOptDate(DateUtils.format(new Date()));
                interfaceDB.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
                interfaceDao.save(interfaceDB);
                inter.setInterfaceId(interfaceDB.getInterfaceId());
                exists = true;
            } else {
                String versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
                inter.setVersionId(versionId);
                inter.setInterfaceId(inter.getEcode());
                inter.setOptDate(DateUtils.format(new Date()));
                inter.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
                interfaceDao.save(inter);
                provider_invoke.setInterfaceId(inter.getInterfaceId());

                serviceInvokeDAO.save(provider_invoke);

            }
        } else {
            String versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
            inter.setVersionId(versionId);
            inter.setInterfaceId(inter.getInterfaceId());
            inter.setEcode(inter.getEcode());
            inter.setOptDate(DateUtils.format(new Date()));
            //建立调用关系
            interfaceDao.save(inter);
            provider_invoke = new ServiceInvoke();
            //TODO 已经改为id格式
//			paramMap.put("systemAb", providerSystem);
//			System system = systemDao.findUniqureBy(paramMap);
            provider_invoke.setSystemId(providerSystem);
            provider_invoke.setServiceId(service.getServiceId());
            provider_invoke.setOperationId(operation.getOperationId());
            provider_invoke.setType(type);
            //添加协议==================
            // provider_invoke.setProtocolId("");
            provider_invoke.setInterfaceId(inter.getInterfaceId());
            //非标准
            provider_invoke.setIsStandard(Constants.INVOKE_TYPE_STANDARD_N);
            serviceInvokeDAO.save(provider_invoke);
        }
        list.add(exists);
        list.add(provider_invoke);
        return list;
    }


    //提供系统调用关系
    protected ServiceInvoke serviceInvokeProviderQuery(com.dc.esb.servicegov.entity.Service service, Operation operation, String systemId, String interfacepoint,String interfaceId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        //查询提供系统 关系
        paramMap.put("serviceId", service.getServiceId());
        paramMap.put("operationId", operation.getOperationId());
        paramMap.put("systemId", systemId);
        //TODO 是否要改动，service_invoke里应该也是interfaceId唯一的吧？
        if(null != interfaceId){
            paramMap.put("interfaceId",interfaceId);
        }
        String type = "1";
        if ("Provider".equalsIgnoreCase(interfacepoint)) {
            type = "0";
        }
        paramMap.put("type", type);

        ServiceInvoke provider_invoke = serviceInvokeDAO.findUniqureBy(paramMap);
        return provider_invoke;
    }

    //消费系统调用关系
    protected ServiceInvoke serviceInvokeCusumerQuery(com.dc.esb.servicegov.entity.Service service, Operation operation, String cusumerSystem) {
        Map<String, String> paramMap = new HashMap<String, String>();
        //查询提供系统 关系
        paramMap.put("serviceId", service.getServiceId());
        paramMap.put("operationId", operation.getOperationId());
        paramMap.put("systemId", cusumerSystem);
        paramMap.put("type", "1");

        ServiceInvoke provider_invoke = serviceInvokeDAO.findUniqureBy(paramMap);
        return provider_invoke;
    }

    protected boolean insertOperation(com.dc.esb.servicegov.entity.Service service, Operation operation, List<OperationPK> operationPKs) {

        boolean exists = false;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("operationId", operation.getOperationId());
        paramMap.put("serviceId", service.getServiceId());

        Operation operationDB = operationDAO.findUniqureBy(paramMap);
        OperationPK pk = new OperationPK(service.getServiceId(), operation.getOperationId());
        if (operationDB != null) {
            //覆盖
            if (GlobalImport.operateFlag) {
                operationDB.setOperationName(operation.getOperationName());
                operationDB.setOperationDesc(operation.getOperationDesc());
                if(!operationPKs.contains(pk)){
                    operationPKs.add(pk);//第一次发现一个场景
                    /*如果已经存在了场景，导入数据为发布或者上线状态，在当前基础上发布一次*/
                    if(Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED.equals(operation.getState()) || Constants.Operation.LIFE_CYCLE_STATE_ONLINE.equals(operation.getState())){
                        String state = operation.getState();//保存当前状态
                        operationDB.setState(Constants.Operation.OPT_STATE_PASS);//为了发布将状态修改为审核通过
                        operationDAO.save(operationDB);
                        operationService.release(operationDB.getOperationId(), operationDB.getServiceId(), "导入发布");
                        operationDB.setState(state);
                        operationDAO.save(operationDB);
                    }
                }
                operationDB.setState(operation.getState());
                operationDAO.save(operationDB);
            }
            exists = true;
        } else {
            if(!operationPKs.contains(pk)) {
                operationPKs.add(pk);
            }
            String versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_OPERATION, operation.getOperationId(), Constants.Version.TYPE_ELSE);
            operation.setServiceId(service.getServiceId());
            operation.setVersionId(versionId);
//            operation.setState(AuditUtil.submit);
            operationDAO.save(operation);
            /*如果系统中不存在当前场景，发布该场景新发布I一次*/
            if(Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED.equals(operation.getState()) || Constants.Operation.LIFE_CYCLE_STATE_ONLINE.equals(operation.getState())){
                String state = operation.getState();//保存当前状态
                operation.setState(Constants.Operation.OPT_STATE_PASS);//为了发布将状态修改为审核通过
                operationDAO.save(operation);
                operationService.release(operation.getOperationId(), operation.getServiceId(), "导入发布");
                operation.setState(state);
                operationDAO.save(operation);
            }
        }


        return exists;

    }

    protected boolean insertService(com.dc.esb.servicegov.entity.Service service) {
        String serviceId = service.getServiceId();
        String categoryId = serviceId.substring(0, 5);
        String parentId = serviceId.substring(0, 4);

        //检查服务类别是否存在
        ServiceCategory serviceCategory = serviceCategoryDAO.getEntity(categoryId);
        if (serviceCategory != null) {
            if (!parentId.equals(serviceCategory.getParentId())) {
                logger.error("服务小类别不存在");
                LogInfo logInfo = new LogInfo();
                logInfo.setDetail("服务小类别不存在");
                logInfo.setType("导入");

                ;
                logInfo.setTime(Utils.getTime());
                logInfoDAO.save(logInfo);

                return false;
            }
        } else {
            logger.error("服务类别不存在");
            LogInfo logInfo = new LogInfo();
            logInfo.setDetail("服务类别不存在");
            logInfo.setType("导入");

            ;
            logInfo.setTime(Utils.getTime());
            logInfoDAO.save(logInfo);
            return false;
        }

        //检查服务是否存在
        com.dc.esb.servicegov.entity.Service serviceDB = serviceDao.getEntity(serviceId);
        if (serviceDB != null) {
            //覆盖
            if (GlobalImport.operateFlag) {
                serviceDB.setServiceName(service.getServiceName());
                serviceDB.setDesc(service.getDesc());

                String version = serviceDB.getVersion();
                if (version == null || "".equals(version)) {
                    version = initVersion;
                }
                if (AuditUtil.passed.equals(serviceDB.getState())) {
                    //serviceDB.setVersion(Utils.modifyversionno(version));
                } else {
                    serviceDB.setVersion(version);
                }
            }
            serviceDao.save(serviceDB);
        } else {
            //不存在 新增服务
            service.setCategoryId(categoryId);
            service.setVersion(initVersion);
            service.setState(AuditUtil.submit);

            serviceDao.save(service);
        }
        return true;
    }

    protected String isNull(String text) {
        if (text == null) {
            return "";
        }
        text = text.replaceAll("（", "(").replaceAll("）", ")");
        return text;
    }

    @Override
    public boolean existSystem(String systemId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("systemId", systemId);

        System system = systemDao.findUniqureBy(paramMap);//systemDao.getEntity(systemId);
        if (system != null) {
            return true;
        }
        return false;

    }

    protected String getCell(Row row, int col) {
        return ExcelTool.getInstance().getCellContent(
                row.getCell(col));
    }

    protected String[] getContext(String text) {

        text = text.replaceAll("（", "(").replaceAll("）", ")");
        String[] str = text.split("[()]+");
        return str;
    }

    @Override
    public HibernateDAO getDAO() {
        return null;
    }
    @Override
    public ServiceInvoke addServiceInvoke(String invokeSystemId,String serviceId,String operationId,String type,String isStandard){
        //先检查是否有映射记录

        Map<String, String> paramMap = new HashMap<String, String>();
        //查询提供系统 关系
        paramMap.put("serviceId", serviceId);
        paramMap.put("operationId", operationId);
        paramMap.put("systemId", invokeSystemId);
        paramMap.put("type", type);
        paramMap.put("isStandard", isStandard);
        ServiceInvoke invoke = serviceInvokeDAO.findUniqureBy(paramMap);
        if(null != invoke){
            return invoke;
        }
        invoke = new ServiceInvoke();
        invoke.setServiceId(serviceId);
        invoke.setOperationId(operationId);
        invoke.setSystemId(invokeSystemId);
        invoke.setType(type);
        invoke.setIsStandard(isStandard);
        serviceInvokeDAO.insert(invoke);
        return invoke;
    }

    public List executeInterfaceImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap ,IndexDO indexDO){
        return null;
    }

    /**
     * @param sheetRow 单行
     * @param inputArraySdas 缓冲数组
     * @param tempHeadId 临时报头id
     * @return
     */
    public SDA genderSDA(Row sheetRow, List<SDA> inputArraySdas, String tempHeadId, int i){
        SDA sda = new SDA();
        sda.setSeq(i);
        sda.setId(UUID.randomUUID().toString());
        ExcelTool tools = ExcelTool.getInstance();
        Cell cellObj = sheetRow.getCell(7);
        String cell = tools.getCellContent(cellObj);
        if (cell != null && !"".equals(cell)) {
            Metadata metadata = metadataService.findUniqueBy("metadataId", cell);
            if(metadata != null){
                sda.setHeadId(tempHeadId);
                sda.setMetadataId(metadata.getMetadataId());//元数据
                sda.setStructName(metadata.getMetadataId());//英文名称
                cellObj = sheetRow.getCell(8);//sda中文名称
                if (cellObj != null) {
                    cell = tools.getCellContent(cellObj);
                    sda.setStructAlias(cell);
                }
                cellObj = sheetRow.getCell(10);//约束条件
                if (cellObj != null) {
                    cell = tools.getCellContent(cellObj);
                    sda.setConstraint(cell);
                }
                cellObj = sheetRow.getCell(11);//是否必输
                if (cellObj != null) {
                    cell = tools.getCellContent(cellObj);
                    sda.setRequired(cell);
                }
                cellObj = sheetRow.getCell(12);//备注
                if (cellObj != null) {
                    cell = tools.getCellContent(cellObj);
                    sda.setRemark(cell);
                }
                if(inputArraySdas.size() > 0){//父节点
                    SDA parent = inputArraySdas.get(inputArraySdas.size() -1);
                    sda.setParentId(parent.getId());
                    sda.setXpath(parent.getXpath()+"/"+metadata.getMetadataId());
                }
                cellObj = sheetRow.getCell(9);//数据类型/长度
                if (cellObj != null) {
                    cell = tools.getCellContent(cellObj);
                    if(StringUtils.isNotEmpty(cell)){
                        if(cell.equalsIgnoreCase("array") || cell.equalsIgnoreCase("struct")){//数组或结构体
                            if(sda.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                                sda.setType(cell);//数据类型
                                inputArraySdas.add(sda);
                            }
                            if(sda.getRemark().equalsIgnoreCase("end")){
                                sda.setType(cell);//数据类型
                                inputArraySdas.remove(inputArraySdas.size() -1);
                                sda.setHeadId(null);
                            }
                        }
                        else{//其他类型
                            String type ="";
                            String length = "";
                            if(cell.contains("(") && cell.contains(")")){
                                type = cell.substring(0, cell.indexOf("("));
                                length = cell.substring(cell.indexOf("(")+1, cell.indexOf(")"));
                            }else{
                                type = cell;
                            }
                            sda.setType(type);
                            sda.setLength(length);
                        }
                    }
                }
                sdaDAO.save(sda);

            }
        }
        return sda;
    }
    public void updateSDAByTempHeadId(String tempHeadId, String headId){
        String hql = " update " + SDA.class.getName() + " set headId=? where headId=?";
        sdaDAO.exeHql(hql, headId, tempHeadId);
    }

    /**
     * 初始化index页字段序号
     * @param sheet
     */
    public void initIndexColnum(Sheet sheet){
        if(sheet != null){
            Row row = sheet.getRow(0);
            for(int i = 0; i < row.getLastCellNum(); i++){//遍历第1行所有单元格
                String content = getCell(row, i);
                if(INDEX_SHEET_NAME.equals(content)){
                    INDEX_SHEET_NAME_COL = i;
                }
                if(INDEX_SERVICE_ID.equals(content)){
                    INDEX_SERVICE_ID_COL = i;
                }
                if(INDEX_CONSUMER.equals(content)){
                    INDEX_CONSUMER_COL = i;
                }
                if(INDEX_PROVIDER.equals(content)){
                    INDEX_PROVIDER_COL = i;
                }
                if(INDEX_INTERFACE_POINT.equals(content)){
                    INDEX_INTERFACE_POINT_COL = i;
                }
                if(INDEX_INTERFACE_HEAD.equals(content)){
                    INDEX_INTERFACE_HEAD_COL = i;
                }
                if(INDEX_INTERFACE_STATUS.equals(content)){
                    INDEX_INTERFACE_STATUS_COL = i;
                }
                if(INDEX_OPERATION_STATE.equals(content)){
                    INDEX_OPERATION_STATE_COL = i;
                }
                if(INDEX_ISSTANDARD.equals(content)){
                    INDEX_ISSTANDARD_COL = i;
                }
            }
        }
    }
}
