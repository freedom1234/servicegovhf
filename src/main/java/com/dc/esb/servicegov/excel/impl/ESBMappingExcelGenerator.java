package com.dc.esb.servicegov.excel.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.excel.IConfigGenerater;
import com.dc.esb.servicegov.excel.support.MappingExcelUtils;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.vo.RelationVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ESBMappingExcelGenerator implements
        IConfigGenerater<Object, Object> {

    private static final Log log = LogFactory
            .getLog(ESBMappingExcelGenerator.class);

    @Autowired
    private ServiceServiceImpl serviceServiceImpl;
    @Autowired
    private OperationServiceImpl operationServiceImpl;
    @Autowired
    private MetadataServiceImpl metadataServiceImpl;
    @Autowired
    private InterfaceServiceImpl interfaceService;
    @Autowired
    private SystemServiceImpl systemServiceImpl;
    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeServiceImpl;

    List<Map<String, SDA>> lstStructName = new ArrayList<Map<String, SDA>>();

    Sheet sheet2 = null;

    Workbook wb;


    private CountDownLatch countDown;

    Hyperlink href = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);

    public ESBMappingExcelGenerator() {

    }

    private CellStyle structIndexCellStyle;
    private CellStyle structIndexCellStyle1;

    private CellStyle arrayCellStyle;
    private CellStyle cellStyleLightYellow;
    private CellStyle cellStyleLightGreen;
    private CellStyle cellStyleMaroon;

    private CellStyle titleCellStyle;
    private CellStyle bodyCellStyle;

    private CellStyle headCellStyle;
    private CellStyle headCenterCellStyle;
    private CellStyle cellStyle;
    // required
    private CellStyle cellStyle4r;

    private CellStyle greyCellStyle;
    private CellStyle arrayCellStyle4r;

    private void init() {
        wb = new HSSFWorkbook();
        structIndexCellStyle = MappingExcelUtils.getStructIndexCellStyle(wb);
        structIndexCellStyle1 = MappingExcelUtils.getStructIndexCellStyle1(wb);

        arrayCellStyle = MappingExcelUtils.getArrayCellStyle(wb);
        cellStyleLightGreen = MappingExcelUtils.getCellStyleLightGreen(wb);
        cellStyleMaroon = MappingExcelUtils.getCellStyleMaroon(wb);
        cellStyleLightYellow = MappingExcelUtils.getCellStyleLightYellow(wb);

        titleCellStyle = MappingExcelUtils.getTitleCellStyle(wb);
        bodyCellStyle = MappingExcelUtils.getBodyCellStyle(wb);

        headCellStyle = MappingExcelUtils.getBodyHeaderCellStyle(wb);
        headCenterCellStyle = MappingExcelUtils.getBodyHeaderCenterCellStyle(wb);
        cellStyle = MappingExcelUtils.getCellStyle(wb);
        cellStyle4r = MappingExcelUtils.getCellStyle4r(wb);
        arrayCellStyle4r = MappingExcelUtils.getArrayCellStyle4r(wb);

        greyCellStyle = MappingExcelUtils.getNoMappingCellStyle(wb);
    }

    /**
     * @return
     */
    public File generate(String type, String serviceId) {
        try {
            init();
            List<RelationVO> lst = new ArrayList<RelationVO>();
            if(type.equals("SERVICE")){
                lst = serviceInvokeServiceImpl.getInvokerRelationByServiceId(serviceId);
            }
            // 多个导出
//            if (type.equals("multi")) {
//                String[] ids = new String[params.length - 2];
//                for (int tni = 1; tni < params.length - 1; tni++) {
////					lst.add(serviceServiceImpl.getserviceInvokerRelationByInterfaceId(
////							params[tni]).get(0));
//                    ids[tni - 1] = params[tni];
//                }
//
//            } else {
//                // 按照系统导出
//                String sysId = params[1];
//                String type = params[2];
//                if (type.equals("consumer")) {
//                    lst = serviceServiceImpl
//                            .getServiceInvokeRelationByConsumer(sysId);
//                } else if (type.equals("provider")) {
//                    lst = serviceServiceImpl
//                            .getServiceInvokeRelationByProvider(sysId);
//                } else {
//                    lst = serviceServiceImpl
//                            .getServiceInvokeRelationByConsumer(sysId);
//                    lst.addAll(serviceServiceImpl
//                            .getServiceInvokeRelationByProvider(sysId));
//                }
//            }
            return generate(lst, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public File generate(List<RelationVO> param, String type)
            throws Exception {
        FileOutputStream outputStream = null;
        File excelFile = null;
        excelFile = File.createTempFile("mapping_excel", ".xls");
        excelFile.renameTo(new File("mapping_excel.xls"));
        ExecutorService executor = Executors.newFixedThreadPool(100);
        countDown = new CountDownLatch(param.size());
        List<ExcelGenerateTask> cc = new ArrayList<ExcelGenerateTask>();
        long start = System.currentTimeMillis();
        log.error("开始创建任务:" + param.size());
        for (RelationVO r : param) {
            if (wb.getSheet(r.getInterfaceId()) != null) {
                countDown.countDown();
                continue;
            }
            Sheet sheet = wb.createSheet(r.getInterfaceId());
            ExcelGenerateTask task = ExcelGenerateTaskFactory.getInstance().factory(type);
            task.init(r, wb, sheet, countDown, lstStructName);
            task.initManager(serviceServiceImpl, operationServiceImpl, systemServiceImpl, interfaceService,
                    metadataServiceImpl);
            task.setStyle1(arrayCellStyle, cellStyleLightYellow,
                    cellStyleLightGreen, cellStyleMaroon);
            task.setStyle2(titleCellStyle, bodyCellStyle, greyCellStyle, arrayCellStyle4r);
            task.setStyle3(headCellStyle, cellStyle4r, cellStyle, headCenterCellStyle);
            cc.add(task);
        }
        log.error("任务创建完成，开始并发执行子任务");
        for (ExcelGenerateTask ksat : cc) {
            executor.execute(ksat);
        }
        log.error("" + countDown.getCount());
        countDown.await(60 * 5, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();
        log.error("生成:" + param.size() + "个sheet耗时:" + (end - start) / 1000
                + "s");
        wb.setSheetOrder("INDEX", 0);
        if (wb.getSheet("INDEX1") != null)
            wb.setSheetOrder("INDEX1", 1);
        long sst = System.currentTimeMillis();

     //   generateStructsSheets(wb); 取消
        log
                .info((System.currentTimeMillis() - sst) / 1000
                        + "生成STURCT SHEET耗时");

        outputStream = new FileOutputStream(excelFile);
        wb.write(outputStream);
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭文件[" + excelFile.getAbsolutePath() + "]输出流失败！");
            }
        }

        return excelFile;
    }

    /**
     * @param wb
     */
    /**
     * 暂时去掉
     * @param wb
     */
//    public void generateStructsSheets(Workbook wb) {
//        lstStructName = MappingExcelUtils.getLstStructName();
//        Iterator<Map<String, SDA>> it = lstStructName.iterator();
//        while (it.hasNext()) {
//            Map<String, SDA> m = it.next();
//            Iterator<Map.Entry<String, SDA>> id = m.entrySet().iterator();
//            while (id.hasNext()) {
//                Map.Entry<String, SDA> entry = (Map.Entry<String, SDA>) id
//                        .next();
//                SDA n = entry.getValue();
//                createMetadataStuct(n, wb);
//                log.info("SERVICEID " + n.getServiceId() + ", RESOURCEID "
//                        + n.getId());
//            }
//        }
//    }

    /**
     */
//    public void createMetadataStuct(SDA n, Workbook wb) {
//        // insert into index1
//        Sheet structIndexSheet = wb.getSheet("INDEX1");
//        if (structIndexSheet == null) {
//            structIndexSheet = wb.createSheet("INDEX1");
//            wb.setSheetOrder("INDEX1", 2);
//            Row row = structIndexSheet.createRow(0);
//            MappingExcelUtils.fillCell(row, 0, "ESB结构英文名称", headCellStyle);
//            MappingExcelUtils.fillCell(row, 1, "ESB结构中文名称", headCellStyle);
//            MappingExcelUtils.fillCell(row, 2, "处理人", headCellStyle);
//            MappingExcelUtils.fillCell(row, 3, "更新时间", headCellStyle);
//
//        }
//
//        int i = structIndexSheet.getLastRowNum();
//        // 从最后一行+1开始插入数据
//        Row row = structIndexSheet.createRow(i + 1);
//        row.createCell(0).setCellValue(setValue(n.getMetadataId()));
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("metadataId", n.getMetadataId());
//
//        String alias = metadataServiceImpl.findUniqueBy(params).getChineseName();
//        row.createCell(1).setCellValue(setValue(alias));
//        row.createCell(2).setCellValue("");
//        row.createCell(3).setCellValue("");
//        // 设置Struct Index Sheet样式
//        ExcelStyleSetting.setStructIndexStyle(structIndexSheet, wb,
//                structIndexCellStyle1);
//        ExcelStyleSetting.setSturctIndexOthersStyle(structIndexSheet, wb,
//                structIndexCellStyle);
//
//        // 创建metadataStruct sheet
//        String structId = n.getStructId();
//        createHeaderSheet(wb, structId);
//    }
//
//    /**
//     * @param wb
//     * @param structId
//     */
//    private void createHeaderSheet(Workbook wb, String structId) {
//        int i;
//        Row row;
//        Metadata metadata = metadataServiceImpl.getMetadataByMid(structId);
//        List<MetadataStructsAttr> metadataList = metadataServiceImpl
//                .getMetaStructById(structId);
//        // Struct Detail Sheet
//        Sheet sheet = wb.getSheet(structId);
//        if (sheet == null) {
//            sheet = wb.createSheet(structId);
//            createMetadataHeadInfo(sheet, metadata);
//        }
////		ExcelStyleSetting.createBlankRow(sheet, wb, 4);
//        Row row4 = sheet.createRow(4);
//        for (int f = 0; f < 10; f++) {
//            MappingExcelUtils.fillCell(row4, f, "", cellStyle);
//        }
//        Iterator<MetadataStructsAttr> iterator = metadataList.iterator();
//        i = 0;
//        while (iterator.hasNext()) {
//            MetadataStructsAttr node = iterator.next();
//
//            String metadataId = node.getElementId();
////			MetadataViewBean viewBean = null;
////			try {
////				viewBean = serviceServiceImpl.getServiceMetadataInfo(metadataId);
////			} catch (DataException e) {
////				e.printStackTrace();
////			}
//
//            row = sheet.createRow(i + 5);
//            String typeLengthScale = metadata.getTypeLengthAndScale();
//            if (typeLengthScale.equalsIgnoreCase("struct") ||
//                    typeLengthScale.equalsIgnoreCase("array")) {
//
//                MappingExcelUtils.fillCell(row, 0, node.getElementName(), arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 1, node.getMetadataId(), arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 2, "", arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 3, node.getIsRequired(), arrayCellStyle4r);
//                MappingExcelUtils.fillCell(row, 4, node.getRemark(), arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 5, "", arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 6, metadataId, arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 7, typeLengthScale, arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 8, node.getMetadataId(), arrayCellStyle);
//                MappingExcelUtils.fillCell(row, 9, node.getRemark(), arrayCellStyle);
//
//            } else {
//
//                MappingExcelUtils.fillCell(row, 0, node.getElementName(), cellStyle);
//                MappingExcelUtils.fillCell(row, 1, node.getMetadataId(), cellStyle);
//                MappingExcelUtils.fillCell(row, 2, "", cellStyle);
//                MappingExcelUtils.fillCell(row, 3, node.getIsRequired(), cellStyle4r);
//                MappingExcelUtils.fillCell(row, 4, node.getRemark(), cellStyle);
//                MappingExcelUtils.fillCell(row, 5, "", cellStyle);
//                MappingExcelUtils.fillCell(row, 6, metadataId, cellStyle);
//                MappingExcelUtils.fillCell(row, 7, typeLengthScale, cellStyle);
//                MappingExcelUtils.fillCell(row, 8, node.getMetadataId(), cellStyle);
//                MappingExcelUtils.fillCell(row, 9, node.getRemark(), cellStyle);
//            }
//
//            // 设置HREF
//            if (node.getElementId().endsWith("Header")) {
//                href.setAddress("#" + node.getElementId() + "!A1");
//                row.getCell(9).setHyperlink(href);
//                row.getCell(4).setHyperlink(href);
//            }
//            i++;
//        }
//        Iterator<Row> it = sheet.iterator();
//        while (it.hasNext()) {
//            Row r = it.next();
//            r.setHeightInPoints((short) 20);
//            r.getCell(5).setCellStyle(cellStyleMaroon);
//        }
//
//        for (int q = 0; q <= 4; q++) {
//            sheet.getRow(2).getCell(q).setCellStyle(cellStyleLightYellow);
//            sheet.getRow(3).getCell(q).setCellStyle(cellStyleLightYellow);
//        }
//        for (int q = 6; q <= 9; q++) {
//            sheet.getRow(2).getCell(q).setCellStyle(cellStyleLightYellow);
//            sheet.getRow(3).getCell(q).setCellStyle(cellStyleLightYellow);
//        }
//
//        // 设置元数据详细页的样式
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
//        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
//        sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 9));
//        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
//        sheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 9));
//
//        sheet.setColumnWidth(0, 15 * 256);
//        sheet.setColumnWidth(1, 18 * 256);
//        sheet.setColumnWidth(2, 16 * 256);
//        sheet.setColumnWidth(3, 14 * 256);
//        sheet.setColumnWidth(4, 17 * 256);
//        sheet.setColumnWidth(5, 2 * 256);
//        sheet.setColumnWidth(6, 14 * 256);
//        sheet.setColumnWidth(7, 16 * 256);
//        sheet.setColumnWidth(8, 17 * 256);
//        sheet.setColumnWidth(9, 18 * 256);
//
//        for (MetadataStructsAttr node : metadataList) {
//            if (node.getElementId().endsWith("Header")) {
//                createHeaderSheet(wb, node.getElementId());
//            }
//        }
//    }
//
//
//    /**
//     * @param metadataSheet
//     * @param m
//     */
//    private void createMetadataHeadInfo(Sheet metadataSheet, Metadata m) {
//        Row row0 = metadataSheet.createRow(0);
//
//        MappingExcelUtils.fillCell(row0, 0, "结构英文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 1, m.getMetadataId(), cellStyle);
//        MappingExcelUtils.fillCell(row0, 2, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 3, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 4, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 5, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 6, "ESB结构英文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 7, m.getName(), cellStyle);
//        MappingExcelUtils.fillCell(row0, 8, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 9, "", headCellStyle);
//        MappingExcelUtils.fillCell(row0, 10, "返回", headCellStyle);
//
//
//        Row row1 = metadataSheet.createRow(1);
//
//        MappingExcelUtils.fillCell(row1, 0, "结构中文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 1, m.getName(), cellStyle);
//        MappingExcelUtils.fillCell(row1, 2, "", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 3, "", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 4, "", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 5, "", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 6, "ESB结构中文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 7, m.getName(), cellStyle);
//        MappingExcelUtils.fillCell(row1, 8, "", headCellStyle);
//        MappingExcelUtils.fillCell(row1, 9, "", headCellStyle);
//
//        Row row2 = metadataSheet.createRow(2);
//
//        MappingExcelUtils.fillCell(row2, 0, "原始接口", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 1, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 2, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 3, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 4, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 5, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 6, "SPDBSD", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 7, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 8, "", headCellStyle);
//        MappingExcelUtils.fillCell(row2, 9, "", headCellStyle);
//
//        Row row3 = metadataSheet.createRow(3);
//
//        MappingExcelUtils.fillCell(row3, 0, "英文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 1, "中文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 2, "数据类型", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 3, "是否必输", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 4, "备注", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 5, "", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 6, "英文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 7, "数据类型/长度", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 8, "中文名称", headCellStyle);
//        MappingExcelUtils.fillCell(row3, 9, "备注", headCellStyle);
//
//        href.setAddress("#INDEX!A1");
//        metadataSheet.getRow(0).getCell(10).setHyperlink(href);
//    }


    private String setValue(Object obj) {
        if (obj == null || "".equals(obj.toString())) {
            return "";
        } else {
            return obj.toString();
        }
    }

    @Override
    public List<Object> getAllExportData() {
        return null;
    }

    @Override
    public List<Object> getExportDataByConditions(Map<String, String> mapConditions) {
        return null;
    }
}
