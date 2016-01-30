package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.Service;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.excel.support.CellStyleSupport;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.Counter;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.dc.esb.servicegov.excel.support.Constants.MAPPING_FILE_TYPE;

/**
 * Created by Administrator on 2015/7/21.
 */
@org.springframework.stereotype.Service
@Transactional
public class ExcelExportServiceImpl extends AbstractBaseService {
    protected Log logger = LogFactory.getLog(getClass());

    private HSSFCellStyle commonStyle;
    private HSSFCellStyle arrayStyle;
    private HSSFCellStyle splitStyle;

    private static final String serviceType = "service";
    private static final String serviceCategoryType0 = "root";
    private static final String serviceCategoryType1 = "serviceCategory1";
    private static final String serviceCategoryType2 = "serviceCategory2";

    @Autowired
    private ServiceInvokeDAOImpl siDao;
    @Autowired
    private SDADAOImpl sdaDao;
    @Autowired
    private IdaDAOImpl idaDao;
    @Autowired
    private MetadataDAOImpl metadataDao;
    @Autowired
    private ServiceDAOImpl serviceDao;
    @Autowired
    private OperationDAOImpl operationDAO;
    @Autowired
    private ServiceCategoryDAOImpl serviceCategoryDao;
    @Autowired
    private SystemDAOImpl systemDAOImpl;
    @Autowired
    private InterfaceInvokeDAOImpl interfaceInvokeDAO;
    @Autowired
    private InterfaceDAOImpl interfaceDAO;
    @Autowired
    private StatisticsServiceImpl statisticsService;
    @Autowired
    private InterfaceHeadServiceImpl interfaceHeadService;
    @Autowired
    private ProtocolDAOImpl protocolDAO;
    /**
     * TODO根据参数id和类型，返回excel文件
     */
    public HSSFWorkbook genderExcel(String id, String type) {
        if (MAPPING_FILE_TYPE.equals(type)) {
//			return new MappingGeneraterTask();
        } else if (serviceType.equals(type)) {
            return genderServiceExcel(id);
        } else if (serviceCategoryType0.equals(type)) {
            return genderServiceCagegoryRootExcel();
        } else if (serviceCategoryType1.equals(type)) {
            return genderServiceCagegory1Excel(id);
        } else if (serviceCategoryType2.equals(type)) {
            return genderServiceCagegory2Excel(id);
        } else {
            String errorMsg = "暂时不支持类型为[" + type + "]的文档导出！";
            logger.error(errorMsg);
        }
        return null;
    }

    /**
     * TODO 读取模板
     *
     * @return
     */
    public HSSFWorkbook getTempalteWb(String templatePath) {
        File file = new File(templatePath);
        HSSFWorkbook wb = null;
        BufferedInputStream in = null;
        InputStream is;
        try {
            is = new FileInputStream(file);
            wb = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 根据“服务类”生成excel
     */
    public HSSFWorkbook genderServiceCagegoryRootExcel() {
        List<ServiceInvoke> siList = siDao.getByServiceCagegoryId0();
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }

    /**
     * 根据服务类别生成excel
     */
    public HSSFWorkbook genderServiceCagegory1Excel(String serviceCategoryId) {
        List<ServiceInvoke> siList = siDao.getByServiceCagegoryId1(serviceCategoryId);
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }

    public HSSFWorkbook genderServiceCagegory2Excel(String serviceCategoryId) {
        List<ServiceInvoke> siList = siDao.getByServiceCagegoryId2(serviceCategoryId);
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }

    /**
     * TODO根据服务id生成excel
     *
     * @param serviceId
     * @return
     */
    public HSSFWorkbook genderServiceExcel(String serviceId) {
        String hql = " from " + ServiceInvoke.class.getName() + " where serviceId=? order by operationId asc";
        List<ServiceInvoke> siList = siDao.find(hql, serviceId);
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }
    public HSSFWorkbook genderExcelByOperation(OperationPKVO pkvo) {
        List<ServiceInvoke> siList = new ArrayList<ServiceInvoke>();
        List<OperationPK> pks = pkvo.getPks();
        for (int i = 0; i < pks.size(); i++) {
            List<ServiceInvoke> opSiList = siDao.getByOperationPK(pks.get(i));
            for (ServiceInvoke si : opSiList) {
                siList.add(si);
            }
        }
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }
    //根据系统id生成excel
    public HSSFWorkbook genderExcelBySystemId(String systemId) {
        String hql = " from ServiceInvoke where systemId = ? and operationId is not null and serviceId is not null";
        List<ServiceInvoke> siList = siDao.find(hql, systemId);
        HSSFWorkbook workbook = fillExcel(siList);
        return workbook;
    }

    public HSSFWorkbook fillExcel(List<ServiceInvoke> siList) {
        if (siList.size() > 0) {
            HSSFWorkbook workbook = getTempalteWb(Constants.EXCEL_TEMPLATE_SERVICE);
            commonStyle = CellStyleSupport.leftStyle(workbook);
            arrayStyle = CellStyleSupport.arrayStyle(workbook);
            fillIndex(workbook, siList);
            fillHeads(workbook, siList);
            fillProtocol(workbook, siList);
            fillMapings(workbook, siList);
            return workbook;
        }
        return null;
    }

    /**
     * 填充index和index_exd页
     * @param siList
     */
    public boolean fillIndex(HSSFWorkbook workbook, List<ServiceInvoke> siList) {
        HSSFSheet sheet = workbook.getSheet("INDEX");
        int sheetRowNum = 1;
        HSSFSheet exdSheet = workbook.getSheet("INDEX_EXD");
        int exdSheetRow = 1;
        for(int i =0; i < siList.size(); i++){
            ServiceInvoke serviceInvoke = siList.get(i);
            if(!Constants.INVOKE_TYPE_STANDARD_U.equals(serviceInvoke.getIsStandard())){//不是未知
                String hql = " from InterfaceInvoke where providerInvokeId = ? or consumerInvokeId = ?";
                List<InterfaceInvoke> interfaceInvokes = interfaceInvokeDAO.find(hql, serviceInvoke.getInvokeId(), serviceInvoke.getInvokeId());
                List<ServiceInvoke> consumers = new ArrayList<ServiceInvoke>();
                List<ServiceInvoke> providers = new ArrayList<ServiceInvoke>();
                for(InterfaceInvoke interfaceInvoke : interfaceInvokes){
                    ServiceInvoke consumer = interfaceInvoke.getConsumer();
                    ServiceInvoke provider = interfaceInvoke.getProvider();
                    if(null !=consumer){
                        if(!consumers.contains(consumer)){
                            consumers.add(consumer);
                        }
                    }
                    if(null !=provider){
                        if(!providers.contains(provider)){
                            providers.add(provider);
                        }
                    }
                }
                if(StringUtils.isNotEmpty(serviceInvoke.getInterfaceId())){
                    fillIndexRow(sheet, sheetRowNum, serviceInvoke, consumers, providers);
                    sheetRowNum++;
                }else{
                    fillIndexRow(exdSheet, exdSheetRow, serviceInvoke, consumers, providers);
                    exdSheetRow++;
                }
            }else{
                continue;
            }
        }
        return true;
    }
    public void fillIndexRow(HSSFSheet sheet, int rowNum, ServiceInvoke serviceInvoke, List<ServiceInvoke> consumers, List<ServiceInvoke> providers){
        HSSFRow row = sheet.createRow(rowNum);
        Operation operation = operationDAO.getBySO(serviceInvoke.getServiceId(), serviceInvoke.getOperationId());
        setCellValue(row.createCell(0), commonStyle, serviceInvoke.getInterfaceId());//接口代码
        Hyperlink hyperlink = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
        // "#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
        hyperlink.setAddress("#" + serviceInvoke.getInterfaceId() + "!A1");
        row.getCell(0).setHyperlink(hyperlink);
        Interface inter = interfaceDAO.findUniqueBy("interfaceId", serviceInvoke.getInterfaceId());
        if(null != inter){
            setCellValue(row.createCell(1), commonStyle, inter.getEcode());//交易名称
            setCellValue(row.createCell(2), commonStyle, inter.getInterfaceName());//交易名称
            String interStatus = "";
            if( Constants.INTERFACE_STATUS_TC.equals(inter.getStatus())){
                interStatus = "投产";
            }
            if( Constants.INTERFACE_STATUS_FQ.equals(inter.getStatus())){
                interStatus = "废弃";
            }
            setCellValue(row.createCell(20), commonStyle, interStatus);//接口状态
        }else{
            setCellValue(row.createCell(1), commonStyle, null);//交易名称
            setCellValue(row.createCell(2), commonStyle, null);//交易名称
            setCellValue(row.createCell(20), commonStyle, null);//接口状态
        }
        if(null != operation){
            setCellValue(row.createCell(3), commonStyle, operation.getService().getServiceName() + "(" + operation.getServiceId() + ")");//服务名称
            setCellValue(row.createCell(4), commonStyle, operation.getOperationId());//场景id
            setCellValue(row.createCell(5), commonStyle, operation.getOperationName());//场景名称

            String operaStatus = Constants.Operation.getStateName(operation.getState());
            setCellValue(row.createCell(21), commonStyle, operaStatus);//场景状态
        }

        //用systemAb
//                setCellValue(row.createCell(5), commonStyle, vo.getConsumers());//调用方
        setCellValue(row.createCell(6), commonStyle, joinServiceInvokeSystemName(consumers, "systemAb"));//调用方
//                setCellValue(row.createCell(6), commonStyle, vo.getProviders());//提供者
        setCellValue(row.createCell(7), commonStyle, joinServiceInvokeSystemName(providers, "systemAb"));//提供者
        String systemAb = Constants.INVOKE_TYPE_CONSUMER.equals(serviceInvoke.getType())? "consumer" : "provider";
        setCellValue(row.createCell(8), commonStyle, systemAb);//接口方向
        setCellValue(row.createCell(9), commonStyle, joinServiceInvokeSystemName(providers, "systemId"));//接口提供系统ID

        setCellValue(row.createCell(10), commonStyle, "");//报文名称
        setCellValue(row.createCell(11), commonStyle, "");//处理人
        setCellValue(row.createCell(12), commonStyle, "");//更新时间
        setCellValue(row.createCell(13), commonStyle, "");//报文转换方向
        setCellValue(row.createCell(14), commonStyle, "");//是否已有调用
        setCellValue(row.createCell(15), commonStyle, joinServiceInvokeSystemName(consumers, "systemChineseName"));//调用方系统名称
        setCellValue(row.createCell(16), commonStyle, "");//参考文档
        setCellValue(row.createCell(17), commonStyle, "");//模块划分
        setCellValue(row.createCell(18), commonStyle, "");//是否穿透
        setCellValue(row.createCell(19), commonStyle, interfaceHeadService.getHeadNames(serviceInvoke.getInterfaceId()));//业务报文头

        String isStandard = "";
        if ( Constants.INVOKE_TYPE_STANDARD_Y.equals(serviceInvoke.getIsStandard())){
            isStandard = "是";
        }
        if ( Constants.INVOKE_TYPE_STANDARD_N.equals(serviceInvoke.getIsStandard())){
            isStandard = "否";
        }
        setCellValue(row.createCell(22), commonStyle, isStandard);//是否标准
        String protocolName = "";
        if(StringUtils.isNotEmpty(serviceInvoke.getProtocolId())){
            Protocol protocol = protocolDAO.findUniqueBy("protocolId", serviceInvoke.getProtocolId());
            if(null != protocol){
                protocolName = protocol.getProtocolName();

            }
        }
        setCellValue(row.createCell(23), commonStyle, protocolName);//关联协议
    }
    /**
     * 循环填充mapping
     *
     * @param workbook
     * @param siList
     * @return
     */
    public void fillMapings(HSSFWorkbook workbook, List<ServiceInvoke> siList) {
        HSSFSheet mappingSheet = workbook.getSheet("MAPPING");
        int poolSize = siList.size() > 10 ? 10 : siList.size();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < siList.size(); i++) {
            ServiceInvoke si = siList.get(i);
            //TODO taizhou 标准没有interfaceId
            if (null == si.getInterfaceId()) continue;
            if(null != workbook.getSheet(si.getInterfaceId())) continue;//如果已经有同名sheet
            HSSFSheet sheet = workbook.cloneSheet(workbook.getSheetIndex(mappingSheet));//复制模板中mapping页
            workbook.setSheetName(workbook.getSheetIndex(sheet), si.getInterfaceId());//修改sheet名称
//            MappingSheetTask msTask = new MappingSheetTask(sheet, si, this);
//            pool.execute(msTask);
            fillMapping(sheet, si);
        }
        pool.shutdown();
        while (true) { //判断多线程是否结束
            try {
                if (pool.isTerminated()) {
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("===========创建mapping页进程错误！");
            }
        }
        workbook.removeSheetAt(workbook.getSheetIndex(mappingSheet));//删除mapping页
    }

    /**
     *填充mapping页
     */
    public boolean fillMapping(HSSFSheet sheet, ServiceInvoke si) {
        try {
            HSSFRow row0 = sheet.getRow(0);
            HSSFCell splitCell = row0.getCell(6);
            splitStyle = splitCell.getCellStyle();
            HSSFRow row1 = sheet.getRow(1);
            HSSFRow row2 = sheet.getRow(2);
            HSSFRow row3 = sheet.getRow(3);
            if (si.getInter() != null) {
                setCellValue(row0.createCell(1), commonStyle, si.getInter().getEcode());//交易码
                setCellValue(row1.createCell(1), commonStyle, si.getInter().getInterfaceName());//交易名称
            }
            if (si.getServiceId() != null) {
                Service service = serviceDao.findUniqueBy("serviceId", si.getServiceId());
                setCellValue(row0.createCell(8), commonStyle, service.getServiceName() + "(" + service.getServiceId() +")");//服务名称
                setCellValue(row2.createCell(8), commonStyle, service.getDesc());//服务描述
                if (si.getOperationId() != null) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("serviceId", si.getServiceId());
                    params.put("operationId", si.getOperationId());
                    Operation operation = operationDAO.findUniqureBy(params);
                    setCellValue(row1.createCell(8), commonStyle, operation.getOperationName()+"("+ operation.getOperationId() +")");//服务操作名称
                    setCellValue(row3.createCell(8), commonStyle, operation.getOperationDesc());//服务操作描述
                }
            }

            Counter counter = new Counter(6);
            List<SDA> reqListSDA = getSDAByParentName(si.getServiceId(), si.getOperationId(), "request");
            List<SDA> resListSDA = getSDAByParentName(si.getServiceId(), si.getOperationId(), "response");
            if(Constants.INVOKE_TYPE_STANDARD_Y.equals(si.getIsStandard())){//如果是标准接口，不输出ida
                for(SDA sda : reqListSDA){
                    fillStandarNode(sheet, counter, sda);
                }
                counter.increment();//分隔行
                for(SDA sda : resListSDA){
                    fillStandarNode(sheet, counter, sda);
                }
            }else{
                List<Ida> reqListIda = getIdaByParentName(si.getInterfaceId(), "request");
                List<Ida> resListIda = getIdaByParentName(si.getInterfaceId(), "response");
                for(Ida ida : reqListIda){
                    fillUnstandardNode(sheet, counter, si.getServiceId(), si.getOperationId(), ida);
                    reqListSDA.remove(sdaDao.findByXpath(si.getServiceId(), si.getOperationId(), ida.getXpath()));//从对应的sda数组中移除对应元素
                }
                if(reqListSDA.size() > 0){
                    for(SDA sda : reqListSDA){
                        fillStandarNode(sheet, counter, sda);//只插入sda
                    }
                }
                counter.increment();//分隔行
                for(Ida ida : resListIda){
                    fillUnstandardNode(sheet, counter, si.getServiceId(), si.getOperationId(), ida);
                    resListSDA.remove(sdaDao.findByXpath(si.getServiceId(), si.getOperationId(), ida.getXpath()));//从对应的sda数组中移除对应元素
                }
                if(resListSDA.size() > 0){
                    for(SDA sda : resListSDA){
                        fillStandarNode(sheet, counter, sda);//只插入sda
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("===========填充[" + sheet.getSheetName() + "]页失败===========");
            return false;
        }
        return true;
    }

    /**
     * 标准接口插入sda
     * @param sheet
     * @param counter
     * @param sda
     */
    public void fillStandarNode(HSSFSheet sheet, Counter counter, SDA sda){
        addRow(sheet, counter);
        fillSDA(sheet, counter.getCount(), sda);
        List<SDA> children = sdaDao.findByParentId(sda.getId());
        if(null != children && 0 < children.size()){
            for(SDA child :children){
                addRow(sheet, counter);
                fillSDA(sheet, counter.getCount(), child);
            }
            fillArrayEndRow(sheet, counter, null, sda);
        }
    }

    /**
     * 非标时插入ida，sda行
     * @param sheet
     * @param counter
     * @param serviceId
     * @param operationId
     * @param ida
     */
    public void fillUnstandardNode(HSSFSheet sheet, Counter counter, String serviceId, String operationId, Ida ida){
        addRow(sheet, counter);
        fillIda(sheet, counter.getCount(), ida);
        SDA sda = sdaDao.findByXpath(serviceId, operationId, ida.getXpath());
        fillSDA(sheet, counter.getCount(), sda);
        List<Ida> children = idaDao.findByParentIdOrder(ida.getId());
        if(null != children && 0 < children.size()){
            for(Ida child :children){
                fillUnstandardNode(sheet, counter, serviceId, operationId, child);
            }
            fillArrayEndRow(sheet, counter, ida, sda);
        }
    }
    /**
     * 数组结束，插入一条end记录
     * @param sheet
     * @param counter
     * @param ida
     * @param sda
     */
    public void fillArrayEndRow(HSSFSheet sheet, Counter counter, Ida ida, SDA sda){
        addRow(sheet, counter);
        Ida endIda = new Ida();
        if(null != ida){
            endIda.setStructName(ida.getStructName());
            endIda.setStructAlias(ida.getStructAlias());
            endIda.setType(ida.getType());
            endIda.setLength(ida.getLength());
            endIda.setRequired(ida.getRequired());
            if(StringUtils.isNotEmpty(ida.getRemark()) && ida.getRemark().toLowerCase().contains("start")){
                endIda.setRemark("END");
            }
        }
        fillIda(sheet, counter.getCount(), endIda);
        SDA endSda = new SDA();
        if(null != sda){
            endSda.setStructAlias(sda.getStructAlias());
            endSda.setStructName(sda.getStructName());
            endSda.setMetadataId(sda.getMetadataId());
            endSda.setType(sda.getType());
            endSda.setRequired(sda.getRequired());
            endSda.setConstraint(sda.getConstraint());
            if(StringUtils.isNotEmpty(sda.getRemark()) && sda.getRemark().toLowerCase().contains("start")){
                endSda.setRemark("END");
            }
        }
        fillSDA(sheet, counter.getCount(), endSda);
    }

    public void fillSDA(HSSFSheet sheet, int index, SDA sda) {
        if(sda == null){
            sda = new SDA();
        }
        HSSFRow row = sheet.getRow(index);
        HSSFCellStyle style = "array".equalsIgnoreCase(sda.getType()) || "struct".equalsIgnoreCase(sda.getType()) ? arrayStyle : commonStyle;
        setCellValue(row.createCell(7), style, sda.getStructName()); //英文名称
        setCellValue(row.createCell(8), style,sda.getStructAlias());//中文名称

        setCellValue(row.createCell(9), style, sda.getType());//数据类型/长度
//        setCellValue(row.createCell(10), commonStyle, sda.getLength()); //长度
        setCellValue(row.createCell(10), style, sda.getConstraint());//约束条件
        setCellValue(row.createCell(11), style, sda.getRequired());//是否必输
        setCellValue(row.createCell(12), style, sda.getRemark());//备注
    }

    public void fillIda(HSSFSheet sheet,int index, Ida ida) {
        if(null == ida){
            ida = new Ida();
        }
        HSSFRow row = sheet.getRow(index);
        String[]  values = {ida.getStructName(), ida.getStructAlias(), ida.getType(), ida.getLength(), ida.getRequired(), ida.getRemark()};
        if("array".equalsIgnoreCase(ida.getType()) || "struct".equalsIgnoreCase(ida.getType())){
            setRowValue(row, arrayStyle, values);
        }else{
            setRowValue(row, commonStyle, values);
        }

    }

    public List<Ida> getIdaByParentName(String interfaceId, String parentName) {
        String hql = " from " + Ida.class.getName() + " as i where i._parentId in(" +
                " select i2.id from " + Ida.class.getName() + " as i2 where i2.interfaceId = ? and structName = ?" +
                ") order by i.seq asc";
        List<Ida> list = idaDao.find(hql, interfaceId, parentName);
        return list;
    }

    /**
     * 循环填充head
     */
    public void fillHeads(HSSFWorkbook workbook, List<ServiceInvoke> siList) {
        HSSFSheet headSheet = workbook.getSheet("HEAD");
        List<InterfaceHead> heads = getInterfaceHeads(siList);

        int poolSize = heads.size() > 10 ? 10 : heads.size();
        if(poolSize == 0 ) return;
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < heads.size(); i++) {
            InterfaceHead interfaceHead = heads.get(i);
            HSSFSheet sheet = workbook.cloneSheet(workbook.getSheetIndex(headSheet));//复制模板中head页
            workbook.setSheetName(workbook.getSheetIndex(sheet), interfaceHead.getHeadName());//修改sheet名称
//            MappingSheetTask msTask = new MappingSheetTask(sheet, si, this);
//            pool.execute(msTask);
            fillHead(sheet, interfaceHead);
        }
        pool.shutdown();
        while (true) { //判断多线程是否结束
            try {
                if (pool.isTerminated()) {
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("===========创建head页进程错误！");
            }
        }
        workbook.removeSheetAt(workbook.getSheetIndex(headSheet));//删除mapping页

    }
    public List<InterfaceHead> getInterfaceHeads(List<ServiceInvoke> siList){
        List<String> interfaceIds = new ArrayList<String>();
        if(siList != null){
            for(int i = 0; i < siList.size(); i++){
                if(siList.get(i) != null){
                    ServiceInvoke si = siList.get(i);
                    if(StringUtils.isNotEmpty(si.getInterfaceId())){
                        if(!interfaceIds.contains(si.getInterfaceId())){
                            interfaceIds.add(si.getInterfaceId());
                        }
                    }
                }
            }
        }
        if(interfaceIds.size() == 0){
            return new ArrayList<InterfaceHead>();
        }
        List<InterfaceHead> heads = interfaceHeadService.getByInterfaceIds(interfaceIds);
        return heads;
    }
    /**
     * 填充head页
     */
    public void fillHead(HSSFSheet sheet, InterfaceHead head) {
        HSSFRow row0 = sheet.getRow(0);
        HSSFCell splitCell = row0.getCell(6);
        splitStyle = splitCell.getCellStyle();
        Counter counter = new Counter(4);

        List<Ida> reqListIda = idaDao.findHeadOrder(head.getHeadId(), Constants.ElementAttributes.REQUEST_NAME);
        List<Ida> resListIda = idaDao.findHeadOrder(head.getHeadId(), Constants.ElementAttributes.RESPONSE_NAME);
        List<SDA> reqListSDA = sdaDao.findByHead(head.getHeadId(), Constants.ElementAttributes.REQUEST_NAME);
        List<SDA> resListSDA = sdaDao.findByHead(head.getHeadId(), Constants.ElementAttributes.RESPONSE_NAME);
        for(Ida ida : reqListIda){
            fillHeadNode(sheet, counter, head.getHeadId(), ida);
            reqListSDA.remove(sdaDao.findByXpath(head.getHeadId(), ida.getXpath()));//从对应的sda数组中移除对应元素
        }
        if(reqListSDA.size() > 0){
            for(SDA sda : reqListSDA){
                fillStandarNode(sheet, counter, sda);//只插入sda
            }
        }
        counter.increment();//分隔行
        for(Ida ida : resListIda){
            fillHeadNode(sheet, counter,head.getHeadId(), ida);
            resListSDA.remove(sdaDao.findByXpath(head.getHeadId(), ida.getXpath()));//从对应的sda数组中移除对应元素
        }
        if(resListSDA.size() > 0){
            for(SDA sda : resListSDA){
                fillStandarNode(sheet, counter, sda);//只插入sda
            }
        }
    }
    public void fillHeadNode(HSSFSheet sheet, Counter counter, String headId, Ida ida){
        addRow(sheet, counter);
        fillIda(sheet, counter.getCount(), ida);
        SDA sda = sdaDao.findByXpath(headId, ida.getXpath());
        fillSDA(sheet, counter.getCount(), sda);
        List<Ida> children = idaDao.findByParentIdOrder(ida.getId());
        if(null != children && 0 < children.size()){
            for(Ida child :children){
                fillHeadNode(sheet, counter, headId, child);
            }
            fillArrayEndRow(sheet, counter, ida, sda);
        }
    }
    /**
     * @param serviceId
     * @param operationId
     * @param parentName
     * @return
     */
    public List<SDA> getSDAByParentName(String serviceId, String operationId, String parentName) {
        String hql = " from " + SDA.class.getName() + " as s where s.parentId in( " +
                "select s2.id from " + SDA.class.getName() + " as s2 where s2.serviceId=? and s2.operationId=? and s2.structName=? " +
                ") order by s.seq asc";
        List<SDA> list = sdaDao.find(hql, serviceId, operationId, parentName);
        return list;
    }

    /**
     * 填充协议页
     * @param workbook
     * @param siList
     */
    public void fillProtocol(HSSFWorkbook workbook, List<ServiceInvoke> siList){
        HSSFSheet sheet = workbook.getSheet("PROTOCOL");
        if(null != sheet){
            List<Protocol> protocols = new ArrayList<Protocol>();
            Counter counter = new Counter(0);
            for(int i = 0; i < siList.size(); i++){
                ServiceInvoke serviceInvoke = siList.get(i);
                if(StringUtils.isNotEmpty(serviceInvoke.getProtocolId())){
                    Protocol protocol = protocolDAO.findUniqueBy("protocolId", serviceInvoke.getProtocolId());
                    if(null != protocol){
                        if(!protocols.contains(protocol)){
                            System system = serviceInvoke.getSystem();
                            fillProtocolRow(sheet, counter, protocol, system);
                            protocols.add(protocol);
                        }
                    }
                }
            }
        }
    }
    public void fillProtocolRow(HSSFSheet sheet , Counter counter, Protocol protocol, System system){
        sheet.createRow(sheet.getLastRowNum() + 1);
        counter.increment();
        sheet.shiftRows(counter.getCount(), sheet.getLastRowNum(), 1, true, false); //插入一行
        String generatorName = "";
        if(null != protocol.getGenerator()){
            generatorName = protocol.getGenerator().getName();
        }
        String[] values ={system.getSystemChineseName(), protocol.getProtocolName(), protocol.getCommuProtocol(), protocol.getIsEncrypt(), protocol.getIsSync(), protocol.getIsLongCon(),
                protocol.getEncoding(), protocol.getMsgType(), protocol.getTimeout(), protocol.getSuccCode(), protocol.getErrorCode(), generatorName};
        HSSFRow row = sheet.getRow(counter.getCount());
        setRowValue(row, commonStyle, values);
    }
    /**
     * 导出服务视图
     * @param categoryId
     * @return
     */
    public HSSFWorkbook genderServiceView(String type, String categoryId) {
        try {
            HSSFWorkbook wb = getTempalteWb(Constants.EXCEL_TEMPLATE_SERVICE_VIEW);
            HSSFCellStyle cellStyle = CellStyleSupport.commonStyle(wb);
            if (type.equals(serviceCategoryType0)) {//根节点
                String hql = " from " + ServiceCategory.class.getName() + " where parentId is null";
                List<ServiceCategory> list = serviceCategoryDao.find(hql);
                if (list.size() > 0) {
                    for (ServiceCategory sc : list) {
                        fillView(wb, sc.getCategoryId(), cellStyle, serviceCategoryType1);
                    }
                }
            } else {
                //填充view
                fillView(wb, categoryId, cellStyle, type);
            }
            //填充system页
            fillSystem(wb.getSheet("APP-ID"), cellStyle);
            //删除view页
            wb.removeSheetAt(wb.getSheetIndex(wb.getSheet("VIEW")));
            return wb;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }

    public void fillView(HSSFWorkbook wb, String categoryId, HSSFCellStyle cellStyle, String type) {
        //根据categoryId查询
        ServiceCategory sc = serviceCategoryDao.findUniqueBy("categoryId", categoryId);

        HSSFSheet view = wb.getSheet("VIEW");
        HSSFSheet sheet = wb.cloneSheet(wb.getSheetIndex(view)); //复制index页
        wb.setSheetName(wb.getSheetIndex(sheet), sc.getCategoryName());
        Counter counter = new Counter(1);
        if(type.equals(serviceCategoryType1)){//大类
            //查询子分类
            List<ServiceCategory> scList = serviceCategoryDao.findBy("parentId", categoryId);
            String[] values0 = {sc.getCategoryName(), " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
            if (scList.size() == 0) {
                HSSFRow row = sheet.createRow(counter.getCount());
                setRowValue(row, cellStyle, values0);
                return;
            }
            for (ServiceCategory child : scList) {
                fillView(sheet, child.getCategoryId(), cellStyle, counter);
            }

        }
        else if(type.equals(serviceCategoryType2)){
            fillView(sheet, categoryId, cellStyle, counter);
        }
        CellRangeAddress region0 = new CellRangeAddress(1, counter.getCount() - 1, (short) 0, (short) 0);
        sheet.addMergedRegion(region0);
        sheet.getRow(1).getCell(0).setCellStyle(cellStyle);//居中

    }

    /**
     * @param categoryId 子分类
     */
    public void fillView( HSSFSheet sheet,  String categoryId, HSSFCellStyle cellStyle, Counter counter){
        ServiceCategory sc = serviceCategoryDao.findUniqueBy("categoryId", categoryId);
        ServiceCategory parent = serviceCategoryDao.findUniqueBy("categoryId",sc.getParentId());

        int start = counter.getCount();
        String[] values1 = {parent.getCategoryName(), sc.getCategoryName(), " ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
        String hql = " from " + Service.class.getName() + " where categoryId = ? order by serviceId asc";
        List<Service> services = serviceDao.find(hql, sc.getCategoryId());//查询服务
        if (services.size() == 0) {
            HSSFRow row = sheet.createRow(counter.getCount());
            counter.increment();
            setRowValue(row, cellStyle, values1);
            return;
        }
        for (Service service : services) {
            int serviceStart = counter.getCount();
            String[] values2 = {parent.getCategoryName(), sc.getCategoryName(), service.getServiceId(), service.getServiceName(), " ", " ", " ", " ", " ", " ", " ", " "};
            String operationHql = " from " + Operation.class.getName() + " where serviceId=? order by operationId asc";
            List<Operation> operations = operationDAO.find(operationHql, service.getServiceId());//查询场景
            if (operations.size() == 0) {
                HSSFRow row = sheet.createRow(counter.getCount());
                counter.increment();
                setRowValue(row, cellStyle, values2);
                continue;
            }
            for (Operation operation : operations) {
                int operationStart = counter.getCount();
                String[] values3 = {parent.getCategoryName(), sc.getCategoryName(), service.getServiceId(), service.getServiceName(),
                        operation.getOperationId(), operation.getOperationName(), " ", " ", " ", " ", Constants.Operation.getStateName(operation.getState()), operation.getOperationRemark()};
                //处理标准数据
                List<InterfaceInvokeVO> standardVOs = getStandardVOList(operation.getServiceId(), operation.getOperationId());
                //处理非标准数据
                List<InterfaceInvokeVO> interfaceInvokeVOs = getVOList(operation.getServiceId(), operation.getOperationId());
                interfaceInvokeVOs.addAll(standardVOs);
                if (interfaceInvokeVOs.size() == 0) {
                    HSSFRow row = sheet.createRow(counter.getCount());
                    counter.increment();
                    setRowValue(row, cellStyle, values3);
                    continue;
                }
                for (InterfaceInvokeVO interfaceInvokeVO : interfaceInvokeVOs) {
                    if(interfaceInvokeVO == null) continue;
                    HSSFRow row = sheet.createRow(counter.getCount());
                    counter.increment();
                    values3[6] = interfaceInvokeVO.getConsumers();
                    values3[7] = interfaceInvokeVO.getEcode();
                    values3[8] = interfaceInvokeVO.getInterfaceName();
                    values3[9] = interfaceInvokeVO.getProviders();
                    setRowValue(row, cellStyle, values3);
                }

                CellRangeAddress region4 = new CellRangeAddress(operationStart, counter.getCount() - 1, (short) 4, (short) 4);
                CellRangeAddress region5 = new CellRangeAddress(operationStart, counter.getCount() - 1, (short) 5, (short) 5);
                sheet.addMergedRegion(region4);//合并单元格：操作号
                sheet.addMergedRegion(region5);//合并单元格：操作名称
            }
            CellRangeAddress region2 = new CellRangeAddress(serviceStart, counter.getCount() - 1, (short) 2, (short) 2);
            CellRangeAddress region3 = new CellRangeAddress(serviceStart, counter.getCount() - 1, (short) 3, (short) 3);
            sheet.addMergedRegion(region2);//合并单元格：服务号
            sheet.addMergedRegion(region3);//合并单元格：服务名称
            HSSFRow R = sheet.getRow( serviceStart);//居中
            HSSFCell C = R.getCell(2);
            C.setCellStyle(cellStyle);
            sheet.getRow(serviceStart).getCell(3).setCellStyle(cellStyle);//居中
        }
        CellRangeAddress region1 = new CellRangeAddress(start, counter.getCount() - 1, (short) 1, (short) 1);
        sheet.addMergedRegion(region1);//合并单元格：子类
        sheet.getRow(start).getCell(1).setCellStyle(cellStyle);//居中
    }
    /**
     * 填充系统页
     */
    public void fillSystem(HSSFSheet sheet, HSSFCellStyle cellStyle) {
        List<System> systemList = systemDAOImpl.getAll();
        for (int i = 0; i < systemList.size(); i++) {
            System system = systemList.get(i);
            HSSFRow row = sheet.createRow(2 + i);

            setCellValue(row.createCell(1), cellStyle, system.getSystemId());//系统id
            setCellValue(row.createCell(2), cellStyle, system.getSystemAb());//英文简称
            setCellValue(row.createCell(3), cellStyle, system.getSystemChineseName());
            ;//中文名称
        }
    }

    public void setRowValue(HSSFRow row, HSSFCellStyle cellStyle, String[] values) {
        for (int i = 0; i < values.length; i++) {
            setCellValue(row.createCell(i), cellStyle, values[i]);
        }
    }

    public void setCellValue(HSSFCell cell, HSSFCellStyle cellStyle, String value) {
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }

    @Override
    public HibernateDAO getDAO() {
        return null;
    }

    //按系统名称连接成一个字符串
    public Map<String, String> joinBy(List<ServiceInvoke> serviceInvokes) {
        Map<String, String> result = new HashMap<String, String>();
        String sysNames = "";
        List<Interface> interList = new ArrayList<Interface>();
        String interIds = "";
        String interNames = "";
        for (int i = 0; i < serviceInvokes.size(); i++) {
            ServiceInvoke serviceInvoke = serviceInvokes.get(i);
            if (serviceInvoke.getSystem() != null) {
                if (StringUtils.isNotEmpty(serviceInvoke.getSystem().getSystemChineseName())) {
                    if (i == 0) {
                        sysNames += serviceInvoke.getSystem().getSystemChineseName();
                    } else {
                        sysNames += ", " + serviceInvoke.getSystem().getSystemChineseName();
                    }
                }
            }
            if (serviceInvoke.getInter() != null) {
                if (!interList.contains(serviceInvoke.getInter())) {
                    interList.add(serviceInvoke.getInter());
                }
            }
        }

        for (int i = 0; i < interList.size(); i++) {
            Interface inter = interList.get(i);
            String interId = (i == 0) ? inter.getInterfaceId() : ("," + inter.getInterfaceId());
            String interName = (i == 0) ? inter.getInterfaceName() : ("," + inter.getInterfaceName());
            interIds += interId;
            interNames += interName;

        }
        result.put("sysNames", sysNames);
        result.put("interIds", interIds);
        result.put("interNames", interNames);
        return result;
    }

    /**
     * 填充InterfaceInvokeVO的consumers和providers属性
     */
    public void fillInterfaceInvokeVO(InterfaceInvokeVO interfaceInvokeVO) {
        if (StringUtils.isNotEmpty(interfaceInvokeVO.getType()) && interfaceInvokeVO.getType().equals(Constants.INVOKE_TYPE_PROVIDER)) {//如果是提供者方向
            String providerHql = " from " + ServiceInvoke.class.getName() + " as si where si.serviceId=? and si.operationId=? and si.type=? and si.interfaceId=?";
            List<ServiceInvoke> provList = siDao.find(providerHql, interfaceInvokeVO.getServiceId()
                    , interfaceInvokeVO.getOperationId(), interfaceInvokeVO.getType(), interfaceInvokeVO.getInterfaceId());

            interfaceInvokeVO.setProviders(joinServiceInvokeSystemName(provList, "systemChineseName"));
            interfaceInvokeVO.setProviderIds(joinServiceInvokeSystemName(provList, "systemId"));
            interfaceInvokeVO.setProviderNames(joinServiceInvokeSystemName(provList, "systemAb"));

            String hql = "select si from " + ServiceInvoke.class.getName() + " as si ," +  InterfaceInvoke.class.getName() + " as ii"
                + " where ii.providerInvokeId = ? and si.invokeId = ii.consumerInvokeId";
            String consumers = "";
            String consumerIds = "";
            String consumerNames = "";
            List<ServiceInvoke> consumerList = new ArrayList<ServiceInvoke>();
            for(int i=0; i < provList.size(); i++){
                ServiceInvoke si = provList.get(i);
                List<ServiceInvoke> consList =  siDao.find( hql, si.getInvokeId());
                for(int j = 0; j < consList.size(); j++){
                    if(!consumerList.contains(consList.get(j))){
                        consumerList.add(consList.get(j));
                    }
                }
            }
            consumers +=  joinServiceInvokeSystemName(consumerList, "systemChineseName");
            consumerIds +=  joinServiceInvokeSystemName(consumerList,  "systemId");
            consumerNames += joinServiceInvokeSystemName(consumerList,  "systemAb");

            interfaceInvokeVO.setConsumers(consumers);
            interfaceInvokeVO.setConsumerIds(consumerIds);
            interfaceInvokeVO.setConsumerNames(consumerNames);
        }
        if (StringUtils.isNotEmpty(interfaceInvokeVO.getType()) && interfaceInvokeVO.getType().equals(Constants.INVOKE_TYPE_CONSUMER)) {//如果是消费者方向
            String providerHql = " from " + ServiceInvoke.class.getName() + " as si where si.serviceId=? and si.operationId=? and si.type=? and si.interfaceId=?";
            List<ServiceInvoke> consList = siDao.find(providerHql, interfaceInvokeVO.getServiceId()
                    , interfaceInvokeVO.getOperationId(), interfaceInvokeVO.getType(), interfaceInvokeVO.getInterfaceId());

            interfaceInvokeVO.setConsumers(joinServiceInvokeSystemName(consList, "systemChineseName"));
            interfaceInvokeVO.setConsumerIds(joinServiceInvokeSystemName(consList, "systemId"));
            interfaceInvokeVO.setConsumerNames(joinServiceInvokeSystemName(consList, "systemAb"));

            String hql = "select si from " + ServiceInvoke.class.getName() + " as si ," +  InterfaceInvoke.class.getName() + " as ii"
                    + " where ii.consumerInvokeId = ? and si.invokeId = ii.providerInvokeId";
            String providers = "";
            String providerIds = "";
            String providerNames ="";
            for(int i=0; i < consList.size(); i++){
                ServiceInvoke si = consList.get(i);
                List<ServiceInvoke> provList =  siDao.find(hql, si.getInvokeId());
                if(i > 0){
                    providers += ",";
                    providerIds += ",";
                }
                providers +=  joinServiceInvokeSystemName(provList, "systemChineseName");
                providerIds +=  joinServiceInvokeSystemName(provList, "systemId");
                providerNames +=  joinServiceInvokeSystemName(provList, "systemId");

            }
            interfaceInvokeVO.setProviders(providers);
            interfaceInvokeVO.setProviderIds(providerIds);
        }
    }
    //获取场景下标准-标准的情况
    public List<InterfaceInvokeVO> getStandardVOList(String serviceId, String operationId){
        List<InterfaceInvokeVO> result = new ArrayList<InterfaceInvokeVO>();
        List<InterfaceInvoke> standardInvokes = interfaceInvokeDAO.getStandard(serviceId, operationId);
        if(standardInvokes != null ){
            List<String> providerIds = new ArrayList<String>();
            for(int i=0; i < standardInvokes.size(); i++){//按照提供者合并消费者
                InterfaceInvoke interfaceInvoke = standardInvokes.get(i);
                ServiceInvoke provider = interfaceInvoke.getProvider();
                ServiceInvoke consumer = interfaceInvoke.getConsumer();
                if(!providerIds.contains(provider.getInvokeId())){
                    InterfaceInvokeVO vo = new InterfaceInvokeVO();
                    vo.setProviderIds(provider.getSystemId());
                    vo.setProviders(provider.getSystem().getSystemChineseName());
                    vo.setConsumerIds(consumer.getSystemId());
                    vo.setConsumers(consumer.getSystem().getSystemChineseName());
                    providerIds.add(provider.getInvokeId());
                    vo.setServiceId(serviceId);
                    vo.setOperationId(operationId);
                    vo.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
                    result.add(vo);
                }else{
                    int index = providerIds.indexOf(provider.getInvokeId());
                    InterfaceInvokeVO vo = result.get(index);

                    String consumers = vo.getConsumers() +","+consumer.getSystem().getSystemChineseName();
                    vo.setConsumers(consumers);
                }
            }
            for(int i = 0; i < result.size(); i++){//根据消费者合并提供者
                InterfaceInvokeVO vo1 = result.get(i);
                if(vo1 != null){
                    for(int j = i+1; j < result.size(); j++){
                        InterfaceInvokeVO vo2 = result.get(j);
                        if(vo2 != null){
                            if(vo1.getConsumers().equals(vo2.getConsumers())){
                                String providers = vo1.getProviders() + "," + vo2.getProviders();
                                String providerIds_ = vo1.getProviderIds() + "," + vo2.getProviderIds();
                                vo1.setProviders(providers);
                                vo1.setProviderIds(providerIds_);
                                result.set(j, null);
                            }
                        }
                    }
                }

            }
        }
        return result;
    }
    /**
     * 将一个场景下的映射关系根据接口id和类型分组
     * 转换类
     * @param serviceId
     * @param operationId
     * @return
     */
    public List<InterfaceInvokeVO> getVOList(String serviceId, String operationId){
        List<Object[]> list = interfaceInvokeDAO.getVOBySO(serviceId, operationId);
        List<InterfaceInvokeVO> result = new ArrayList<InterfaceInvokeVO>();
        for(Object[] strs : list){
            InterfaceInvokeVO vo = new InterfaceInvokeVO();
            vo.setServiceId(serviceId);
            vo.setOperationId(operationId);
            vo.setType(strs[0].toString());
            vo.setIsStandard(strs[2].toString());
            String interfaceId = strs[1].toString();
            Interface inter = interfaceDAO.findUniqueBy("interfaceId", interfaceId);
            if(inter != null){
                vo.setInterfaceId(interfaceId);
                vo.setInterfaceName(inter.getInterfaceName());
                vo.setEcode(inter.getEcode());
        }
            fillInterfaceInvokeVO(vo);
            result.add(vo);
        }
        return result;
    }

    /**
     * 将一组映射关系,按照服务场景,接口转换
     * @param serviceInvokes
     * @return
     */
    public List<InterfaceInvokeVO> getVOList(List<ServiceInvoke> serviceInvokes){
        List<InterfaceInvokeVO> result = new ArrayList<InterfaceInvokeVO>();
        List<OperationPK> temp = new ArrayList<OperationPK>();
        for(ServiceInvoke si : serviceInvokes){
            OperationPK pk = new OperationPK(si.getServiceId(), si.getOperationId());
            if(!temp.contains(pk)){
                temp.add(pk);
                List<InterfaceInvokeVO> subList =  getVOList(si.getServiceId(), si.getOperationId());
                result.addAll(subList);
            }
        }
        return result;
    }
    /**
     * 将一组serviceInvoke中的系统名称或者系统id连接起来,用逗号隔开
     * @param list
     * @return
     */
    public String joinServiceInvokeSystemName(List<ServiceInvoke> list, String field){
        String result = "";
        if(list != null && list.size() > 0){
            for(ServiceInvoke si : list){
                if(si.getSystem() != null){
                    if("systemChineseName".equals(field)){
                        result += si.getSystem().getSystemChineseName() + ",";
                    }
                    if("systemId".equals(field)){
                        result += si.getSystem().getSystemId() + ",";
                    }
                    if("systemAb".equals(field)){
                        result += si.getSystem().getSystemAb() + ",";
                    }
                }
            }
            if(result.lastIndexOf(",") > 0){
                result = result.substring(0, result.lastIndexOf(","));
            }
        }
        return result;
    }


    /**
     * 导出系统复用率统计
     * @return
     */
    public HSSFWorkbook genderSystemRuserate(ReuseRateListVO listVO) {
        try {
            HSSFWorkbook wb = getTempalteWb(Constants.EXCEL_TEMPLATE_SYSTEM_REUSERATE);
            HSSFCellStyle cellStyle = CellStyleSupport.commonStyle(wb);
            if(listVO != null ){
                List<ReuseRateVO> list = listVO.getList();
                if(list != null && list.size() > 0){
                    HSSFSheet sheet = wb.getSheet("statistics_reuse");
                    for(int i = 0; i < list.size(); i++){
                        HSSFRow row = sheet.createRow(i + 1);
                        ReuseRateVO vo = list.get(i);
                        String type = Constants.INVOKE_TYPE_PROVIDER.equals(vo.getType()) ? "提供者" : "消费者";
                        String[] values = { vo.getSystemId(), vo.getSystemChineseName(), type,vo.getServiceNum(), vo.getOperationNum(), vo.getResueOperationNum(), vo.getSum(), vo.getReuseRate()};
                        setRowValue(row, cellStyle, values);
                    }
                }
            }
            return wb;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }
    /**
     * 导出服务复用率统计
     * @return
     */
    public HSSFWorkbook genderServiceRuserate(TreeNode root) {
        try {
            HSSFWorkbook wb = getTempalteWb(Constants.EXCEL_TEMPLATE_SERVICE_REUSERATE);
            HSSFCellStyle cellStyle = CellStyleSupport.leftStyle(wb);
            List<TreeNode> list = statisticsService.getServiceReuseRate();
            if(list != null && list.size() > 0){
                root = list.get(0);
                Counter counter = new Counter(1);
                HSSFSheet sheet = wb.getSheet("statistics_reuse");
                fillServiceReuseRow(sheet, cellStyle, root, counter, "1.");
            }

            return wb;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }
    public void fillServiceReuseRow(HSSFSheet sheet, HSSFCellStyle cellStyle, TreeNode treeNode, Counter counter, String tab){
        HSSFRow row = sheet.createRow(counter.getCount());
        counter.increment();
        String[] values = { tab + treeNode.getText(), treeNode.getId(), treeNode.getAppend2(), treeNode.getAppend3(), treeNode.getAppend4(), treeNode.getAppend5(), treeNode.getAppend6()};
        setRowValue(row, cellStyle, values);
        List<TreeNode> children = treeNode.getChildren();
        if(children != null && children.size() > 0){
            for(int i = 0; i< children.size(); i++){
                TreeNode child = children.get(i);
                fillServiceReuseRow(sheet, cellStyle, child, counter, tab+ (i+1)+".");
            }
        }
    }
    /**
     * 导出发布状况统计
     * @return
     */
    public HSSFWorkbook genderReleaseState(ReleaseListVO listVO) {
        try {
            HSSFWorkbook wb = getTempalteWb(Constants.EXCEL_TEMPLATE_RELEASE_STATE);
            HSSFCellStyle cellStyle = CellStyleSupport.commonStyle(wb);
            if(listVO != null ){
                List<ReleaseVO> list = listVO.getList();
                if(list != null && list.size() > 0){
                    HSSFSheet sheet = wb.getSheet("statistics_reuse");
                    for(int i = 0; i < list.size(); i++){
                        HSSFRow row = sheet.createRow(i + 1);
                        ReleaseVO vo = list.get(i);
                        String type = Constants.INVOKE_TYPE_PROVIDER.equals(vo.getType()) ? "提供者" : "消费者";
                        String[] values = { vo.getSystemId(), vo.getSystemChineseName(), type, vo.getServiceReleaseNum(), vo.getOperationReleaseNum() };
                        setRowValue(row, cellStyle, values);
                    }
                }
            }
            return wb;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }
    /**
     * 导出发布数量统计
     * @return
     */
    public HSSFWorkbook genderReleaseCount(ReleaseListVO listVO) {
        try {
            HSSFWorkbook wb = getTempalteWb(Constants.EXCEL_TEMPLATE_RELEASE_COUNT);
            HSSFCellStyle cellStyle = CellStyleSupport.commonStyle(wb);
            if(listVO != null ){
                List<ReleaseVO> list = listVO.getList();
                if(list != null && list.size() > 0){
                    HSSFSheet sheet = wb.getSheet("statistics_reuse");
                    for(int i = 0; i < list.size(); i++){
                        HSSFRow row = sheet.createRow(i + 1);
                        ReleaseVO vo = list.get(i);
                        String type = Constants.INVOKE_TYPE_PROVIDER.equals(vo.getType()) ? "提供者" : "消费者";
                        String[] values = { vo.getSystemId(), vo.getSystemChineseName(), type, vo.getServiceReleaseNum(), vo.getOperationReleaseNum() };
                        setRowValue(row, cellStyle, values);
                    }
                }
            }
            return wb;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }
    public void addRow(HSSFSheet sheet, Counter counter){
        sheet.createRow(sheet.getLastRowNum() + 1);
        counter.increment();
        sheet.shiftRows(counter.getCount(), sheet.getLastRowNum(), 1, true, false); //插入一行
        HSSFRow row = sheet.getRow(counter.getCount());
        HSSFCell splitCell = row.createCell(6);
        splitCell.setCellStyle(splitStyle);
    }
    public boolean isSDAParentLast(SDA sda){
        String hql = "from SDA where parentId = ? and seq > ?";
        List<SDA> list = sdaDao.find(hql, sda.getParentId(), sda.getSeq());
        if(null != list && list.size() > 0){
            return false;
        }
        return true;
    }
}
