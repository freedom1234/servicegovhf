package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.ExcelTool;
import com.dc.esb.servicegov.util.GlobalImport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.hibernate.NonUniqueObjectException;
import org.jboss.seam.annotations.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by vincentfxz on 15/7/23.
 */
@Service("TaizhouExcelImportService")
@Transactional
public class TaizhouExcelImportServiceImpl extends ExcelImportServiceImpl {
    /**
     * 获取接口、服务,场景信息
     * @param tranSheet
     * @return
     */
    @Override
    public Map<String, Object> getServiceInfo(Sheet tranSheet,IndexDO indexDO) {
        boolean flag = true;
        // 读取每个sheet页服务信息
        int start = tranSheet.getFirstRowNum();
        int end = tranSheet.getLastRowNum();
        Interface inter = new Interface();
        inter.setInterfaceId(tranSheet.getSheetName());
        com.dc.esb.servicegov.entity.Service service = new com.dc.esb.servicegov.entity.Service();
        Operation oper = new Operation();
        String interfaceStatus = indexDO.getInterfaceStatus();
        String operationState = indexDO.getOperationState();
        inter.setStatus(interfaceStatus);
        oper.setState(operationState);
        for (int j = start; j <= end; j++) {
            Row sheetRow = tranSheet.getRow(j);
            if(sheetRow == null) continue;
            String serviceName = "";
            String serviceId = "";
            String operId = "";
            String operName = "";
            String serviceDesc = "";
            String operDesc = "";
            String tranCode = "";
            String tranName = "";
            String tranDesc = "";
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
                        /*if (tranCode == null || "".equals(tranCode)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，交易码为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，交易码为空", "导入");
                            flag = false;
                        }*/
                        inter.setEcode(tranCode);
                    } else if ("交易名称".equals(cell) && k==0) {
                        tranName = sheetRow.getCell(k + 1).getStringCellValue();
                       /* if (tranName == null || "".equals(tranName)) {
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

        //标准也要有interface,用于记录交易码和交易名称
        resMap.put("interface", inter);
        resMap.put("service", service);
        resMap.put("operation", oper);

        return resMap;
    }

    @Override
    public  Map<String, Object> getStandardInputArg(Sheet sheet) {
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<SDA> sdas = new ArrayList<SDA>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();

        int order = 0;
        for (int j = start; j <= end; j++) {
            SDA sda = new SDA();
            sda.setId(UUID.randomUUID().toString());
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null) continue;
            Cell cellObj = sheetRow.getCell(0);

            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                if ("输出".equals(cell)) {
                    readline = j++;
                    break;
                }

            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setMetadataId(isNull(cell));
                sda.setStructName(isNull(cell));

            }

            cellObj = sheetRow.getCell(8);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setStructAlias(isNull(cell));
            }

            //TODO 本地化修改(第九个类型和长度合并)
            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                cell = isNull(cell).replaceAll("，",",");
                String[] str = cell.split("[()]+");
                if(str.length>1){
                    //DOUBLE(16,2) STRING(6)
                    String len = str[1];
                    sda.setType(str[0]);
                    String[] lenArr = len.split(",");
                    sda.setLength(lenArr[0]);

                }else{
                    //STRUCT
                    sda.setType(isNull(cell));
                    sda.setLength(isNull(cell));
                }
            }

            cellObj = sheetRow.getCell(11);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                sda.setRemark(remark);
            }

            if(null != sda.getMetadataId() && !"".equals(sda.getMetadataId()) && !"end".equalsIgnoreCase(sda.getRemark())){
                Metadata metadata = metadataService.findUniqueBy("metadataId", sda.getMetadataId());
                if(metadata==null){
                    logger.error(sheet.getSheetName()+"页,元数据["+sda.getMetadataId()+"]未配置，导入失败...");
                    msg.append(sda.getMetadataId()).append(",");
                    flag = false;
                }
            }
            sda.setSeq(order);

            sdas.add(sda);
            order++;

        }

        if(!flag){
            logInfoService.saveLog(sheet.getSheetName()+"页,元数据["+msg.toString()+"]未配置，导入失败...","导入(输入)");
            return null;
        }
        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("sdas", sdas);

        return resMap;
    }

    @Override
    public Map<String, Object> getStandardOutputArg(Sheet sheet){
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<SDA> sdas = new ArrayList<SDA>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();
        int order = 0;
        for (int j = start; j <= end; j++) {
            SDA sda = new SDA();
            sda.setId(UUID.randomUUID().toString());
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null) continue;
            Cell cellObj = sheetRow.getCell(0);

            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                if ("输出".equals(cell)) {
                    continue;
                }
            }

            cellObj = sheetRow.getCell(7);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setStructName(isNull(cell));
                sda.setMetadataId(isNull(cell));
            }

            cellObj = sheetRow.getCell(8);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setStructAlias(isNull(cell));
            }

            //TODO 本地化修改(第九个类型和长度合并)
            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                cell = isNull(cell).replaceAll("，",",");
                String[] str = cell.split("[()]+");
                if(str.length>1){
                    //DOUBLE(16,2) STRING(6)
                    String len = str[1];
                    sda.setType(str[0]);
                    String[] lenArr = len.split(",");
                    sda.setLength(lenArr[0]);

                }else{
                    //STRUCT
                    sda.setType(isNull(cell));
                    sda.setLength(isNull(cell));
                }
            }
            cellObj = sheetRow.getCell(11);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
                sda.setRemark(remark);
            }

            if (sda.getMetadataId() != null && !"".equals(sda.getMetadataId()) && !"end".equalsIgnoreCase(sda.getRemark())) {
                Metadata metadata = metadataService.findUniqueBy("metadataId", sda.getMetadataId());
                if (metadata == null) {
                    logger.error(sheet.getSheetName() + "页,元数据[" + sda.getMetadataId() + "]未配置，导入失败...");
                    msg.append(sda.getMetadataId()).append(",");
                    flag = false;
                }
            }
//e

            sda.setSeq(order);

            sdas.add(sda);
            order++;

        }

        if (!flag) {
            logInfoService.saveLog(sheet.getSheetName() + "页,元数据[" + msg.toString() + "]未配置，导入失败...", "导入(输出)");
            return null;
        }

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("sdas", sdas);

        return resMap;
    }

    @Override
    public Map<String, Object> getInterfaceInputArg(Sheet sheet){
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<Ida> idas = new ArrayList<Ida>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();

        int order = 0;
        for (int j = start; j <= end; j++) {
            Ida ida = new Ida();
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
            ida.setSeq(order);
            idas.add(ida);
            order++;
        }

        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("idas", idas);
        return resMap;
    }

    @Override
    public Map<String, Object> getInputArg(Sheet sheet) {
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
            sda.setId(UUID.randomUUID().toString());
            ida.setSdaId(sda.getId());
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

            //TODO 本地化修改(第九个类型和长度合并)
            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                cell = isNull(cell).replaceAll("，",",");
                String[] str = cell.split("[()]+");
                if(str.length>1){
                    //DOUBLE(16,2) STRING(6)
                    String len = str[1];
                    sda.setType(str[0]);
                    String[] lenArr = len.split(",");
                    sda.setLength(lenArr[0]);
                }else{
                    //STRUCT
                    sda.setType(isNull(cell));
                    sda.setLength(isNull(cell));
                }
            }

            //约束条件
            cellObj = sheetRow.getCell(10);
            if(cellObj != null){
                String cell = tools.getCellContent(cellObj);
                sda.setConstraint(isNull(cell));
            }

            cellObj = sheetRow.getCell(11);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
//                if("start".equalsIgnoreCase(remark)) {
//                    sda.setMetadataId("");
//                }
                sda.setRemark(remark);
            }

            if(ida.getMetadataId()!=null&&!"".equals(ida.getMetadataId()) && !"end".equalsIgnoreCase(sda.getRemark())){
                Metadata metadata = metadataService.findUniqueBy("metadataId", sda.getMetadataId());
                if(metadata==null){
                    logger.error(sheet.getSheetName()+"页,元数据["+ida.getMetadataId()+"]未配置，导入失败...");
                    msg.append(ida.getMetadataId()).append(",");
                    flag = false;
                }
            }
            sda.setSeq(order);

            idas.add(ida);
            sdas.add(sda);
            order++;

        }

        if(!flag){
            logInfoService.saveLog(sheet.getSheetName()+"页,元数据["+msg.toString()+"]未配置，导入失败...","导入(输入)");
            return null;
        }
        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put("idas", idas);
        resMap.put("sdas", sdas);

        return resMap;
    }

    public Map<String, Object> getInterfaceOutputArg(Sheet sheet){
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        List<Ida> idas = new ArrayList<Ida>();
        ExcelTool tools = ExcelTool.getInstance();
        int start = readline;
        int end = sheet.getLastRowNum();
        int order = 0;
        for (int j = start; j <= end; j++) {
            Ida ida = new Ida();
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
            ida.setSeq(order);
            idas.add(ida);
            order++;
        }

        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("idas", idas);
        return resMap;
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
            sda.setId(UUID.randomUUID().toString());
            ida.setSdaId(sda.getId());
            Row sheetRow = sheet.getRow(j);
            if(sheetRow == null){
                continue;
            }
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

            //TODO 本地化修改(第九个类型和长度合并)
            cellObj = sheetRow.getCell(9);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                cell = isNull(cell).replaceAll("，",",");
                String[] str = cell.split("[()]+");
                if(str.length>1){
                    //DOUBLE(16,2) STRING(6)
                    String len = str[1];
                    sda.setType(str[0]);
                    String[] lenArr = len.split(",");
                    sda.setLength(lenArr[0]);

                }else{
                    //STRUCT
                    sda.setType(isNull(cell));
                    sda.setLength(isNull(cell));
                }
            }

            //约束条件
            cellObj = sheetRow.getCell(10);
            if(cellObj != null){
                String cell = tools.getCellContent(cellObj);
                sda.setConstraint(isNull(cell));
            }

            cellObj = sheetRow.getCell(11);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                sda.setRequired(isNull(cell));
            }
            cellObj = sheetRow.getCell(12);
            if (cellObj != null) {
                String cell = tools.getCellContent(cellObj);
                String remark = isNull(cell);
//                if ("start".equalsIgnoreCase(cell)) {
//                    sda.setMetadataId("");
//                }
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
                ida.setState(Constants.IDA_STATE_COMMON);
                if ("".equals(isNull(cell))) {
                    ida.setState(Constants.IDA_STATE_DISABLE);
//                    continue;
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
                ida.setState(Constants.IDA_STATE_COMMON);
                if ("".equals(isNull(cell))) {
                    ida.setState(Constants.IDA_STATE_DISABLE);
//                    continue;
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
                        logInfoService.saveLog(sheet.getSheetName() + "页,元数据[" + cell + "]未配置，导入失败...", "导入");
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
    @Override
    public List executeStandardImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap, Map<String, String> publicMap, Map<String, Object> headMap) {
        com.dc.esb.servicegov.entity.Service service = (com.dc.esb.servicegov.entity.Service) infoMap.get("service");
        Operation operation = (Operation) infoMap.get("operation");
        Interface inter = (Interface)infoMap.get("interface");

        List<SDA> sdainput = (List<SDA>) inputMap.get("sdas");
        List<SDA> sdaoutput = (List<SDA>) outMap.get("sdas");
        List<OperationPK>  operationPKs = (List<OperationPK>)infoMap.get("operationPKs");

        //导入服务定义相关信息
        logger.info("导入服务定义信息...");
        List list = new ArrayList();
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
            //TODO 接口id为null
//            ServiceInvoke provider_invoke = serviceInvokeProviderQuery(service, operation, systemId, interfacepoint,null);
            ServiceInvoke provider_invoke = serviceInvokeProviderQuery(service, operation, systemId, interfacepoint,inter.getInterfaceId());
            //获取消费关系
            //ServiceInvoke cusumer_invoke = serviceInvokeCusumerQuery(service, operation, cusumerSystem);
            //导入接口相关信息
            logger.info("导入接口定义信息...");
            List list1 = insertStrandardInvoke(inter,service, operation, provider_invoke, systemId, type);
            boolean exists = (Boolean)list1.get(0);
            provider_invoke = (ServiceInvoke)list1.get(1);


            insertSDA(true, existsOper, operation, service, sdainput, sdaoutput);
            //处理业务报文头
            //TODO 标准不管报文头
//            if (headMap != null && headMap.size()>0) {
//                insertInterfaceHead(exists, inter, headMap);
//            }
            list.add(true);
            list.add(provider_invoke);
        } else {
            list.add(false);
            return list;
        }
        return list;
    }

    @Override
    protected List insertStrandardInvoke(Interface inter,com.dc.esb.servicegov.entity.Service service, Operation operation, ServiceInvoke provider_invoke, String providerSystem, String type) {
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
                interfaceDao.save(interfaceDB);
                inter.setInterfaceId(interfaceDB.getInterfaceId());
                exists = true;
            }else {
                String versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
                inter.setVersionId(versionId);
                interfaceDao.save(inter);
                provider_invoke.setInterfaceId(inter.getInterfaceId());
                provider_invoke.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
                serviceInvokeDAO.save(provider_invoke);
                exists = true;
            }
        } else {
            String versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
            inter.setVersionId(versionId);
            //建立调用关系
            try{
                //TODO 两个提供方都调用同一个接口时候
                interfaceDao.save(inter);
            }catch (NonUniqueObjectException e){
                e.printStackTrace();
            }
            //建立调用关系
            provider_invoke = new ServiceInvoke();
            //TODO 已经改为id格式
//			paramMap.put("systemAb", providerSystem);
//			System system = systemDao.findUniqureBy(paramMap);
            provider_invoke.setSystemId(providerSystem);
            provider_invoke.setServiceId(service.getServiceId());
            provider_invoke.setOperationId(operation.getOperationId());
            provider_invoke.setType(type);
            provider_invoke.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
            provider_invoke.setInterfaceId(inter.getInterfaceId());
            //添加协议==================
            // provider_invoke.setProtocolId("");
            serviceInvokeDAO.save(provider_invoke);
        }

        list.add(exists);
        list.add(provider_invoke);

        return list;
    }

    /**
     * 解析Index页,获取IndexDO对象
     * @param indexSheet
     * @return
     */
    @Override
    public List parseInterfaceIndexSheet(Sheet indexSheet) {
        List<IndexDO> indexDOs = new ArrayList<IndexDO>();
        int endRow = indexSheet.getLastRowNum();
        StringBuffer msg = new StringBuffer();
        for (int i = 1; i <= endRow; i++) {
            Row row = indexSheet.getRow(i);
            // 读取每一行第一列，获取每个交易sheet名称
            String sheetName = getCell(row, INTERFACE_INDEX_SHEET_NAME_COL);
            //读取系统名称
            String systemAb = getCell(row, INTERFACE_SYSTEM_NAME_COL);

            //原始接口：新增or修改（防止文档错误编写问题）
            String optType = getCell(row, INTERFACE_ADD_OR_MODIFY_COL);
            if(optType.equals("新增")){
                optType = "0";
            }else if (optType.equals("修改")){
                optType = "1";
            }else{
                optType = "0";
            }


            HashMap<String,String> param = new HashMap<String, String>();
            param.put("systemAb",systemAb);
            System system = systemDao.findUniqureBy(param);
            if (null == system) {
                logger.error("" + systemAb + "系统不存在");
                logInfoService.saveLog("" + systemAb + "系统不存在", "导入");
                msg.append("" + systemAb + "系统不存在，");
                continue;
            }
            //原始接口导入，格式不一样,用INTERFACE_STATUS_COL
            String interfaceStatus = getCell(row, INTERFACE_STATUS_COL);
            if ("投产".equals(interfaceStatus)){
                interfaceStatus = Constants.INTERFACE_STATUS_TC;
            }else if ("废弃".equals(interfaceStatus)){
                interfaceStatus = Constants.INTERFACE_STATUS_FQ;
            }else{
                interfaceStatus = "";
            }
            //提供方或消费方
            String invokeType = getCell(row,INTERFACE_POINT_COL);
            if("Provider".equalsIgnoreCase(invokeType)){
                invokeType = "0";
            }else if("Consumer".equalsIgnoreCase(invokeType)){
                invokeType = "1";
            }else{
                invokeType = "";
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
                operationState =  Constants.Operation.OPT_STATE_UNAUDIT;//默认为服务定义状态
            }
            IndexDO indexDO = new IndexDO();
            indexDO.setSheetName(sheetName);
            indexDO.setSystemAb(systemAb);
            indexDO.setSystemId(system.getSystemId());
            indexDO.setInterfaceStatus(interfaceStatus);
            indexDO.setInvokeType(invokeType);
            indexDO.setOperationState(operationState);
            indexDO.setOptType(optType);
            indexDOs.add(indexDO);
        }
        List list = new ArrayList();
        list.add(indexDOs);
        list.add(msg);
        return list;
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
            if("".equals(sheetName)) continue;
            //接口消费方
//            String consumerSystem = getCell(row, INDEX_CONSUMER_COL);
            //形式：name,name
            String consumerSystem[] = getCell(row, INDEX_CONSUMER_COL).replaceAll("，",",").split(",");
            for (int j = 0; j < consumerSystem.length; j++) {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("systemAb",consumerSystem[j]);
                System system = systemDao.findUniqureBy(param);

                if (null == system) {
                    logger.error("" + consumerSystem[j] + "系统不存在");
                    logInfoService.saveLog("" + consumerSystem[j] + "系统不存在", "导入");
                    msg.append("" + consumerSystem[j] + "系统不存在");
                    continue;
                }
                String consumerSystemId = system.getSystemId();
                //接口提供方
                String providerSystem[] = getCell(row, INDEX_PROVIDER_COL).replaceAll("，",",").split(",");
                for(int k = 0; k < providerSystem.length; k++){
                    param = new HashMap<String, String>();
                    param.put("systemAb",providerSystem[k]);
                    system = systemDao.findUniqureBy(param);
                    if (null == system) {
                        logger.error("" + providerSystem[k] + "系统不存在");
                        logInfoService.saveLog("" + providerSystem[k] + "系统不存在", "导入");
                        msg.append("" + providerSystem[k] + "系统不存在");
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
                    //如果手动输错
                    if(temp.split("[()]+").length <2){
                        logger.error("" + temp + "，服务格式错误");
                        logInfoService.saveLog("" + temp + "，服务格式错误", "导入");
                        msg.append("" + temp + "，服务格式错误");
                        continue;
                    }
                    String serviceId = temp.split("[()]+")[1];
                    String systemId = consumerSystemId;
                    String systemAb = consumerSystem[j];
                    if ("Provider".equalsIgnoreCase(interfacePoint)) {
                        systemId = providerSystemId;
                        systemAb = providerSystem[k];
                    }
                    IndexDO indexDO = new IndexDO();
                    indexDO.setConsumerSystem(consumerSystem[j]);
                    indexDO.setConsumerSystemId(consumerSystemId);
                    indexDO.setSheetName(sheetName);
                    indexDO.setInterfaceHead(interfaceHead);
                    indexDO.setProviderSystem(providerSystem[k]);
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

            }
        }
        List list = new ArrayList();
        list.add(indexDOs);
        list.add(msg);
        return list;
    }

    /**
     * 获取接口信息
     * @param tranSheet
     * @return
     */
    @Override
    public Map<String, Object> getInterfaceInfo(Sheet tranSheet){
        boolean flag = true;
        // 读取每个sheet页交易信息与服务信息
        int start = tranSheet.getFirstRowNum();
        int end = tranSheet.getLastRowNum();
        Interface inter = new Interface();
        inter.setInterfaceId(tranSheet.getSheetName());
        for (int j = start; j <= end; j++) {
            Row sheetRow = tranSheet.getRow(j);
            if(sheetRow == null) continue;
            String tranCode = "";
            String tranName = "";
            String interfaceDesc = "";
            int cellStart = sheetRow.getFirstCellNum();
            int cellEnd = sheetRow.getLastCellNum();

            for (int k = cellStart; k < cellEnd; k++) {

                Cell cellObj = sheetRow.getCell(k);
                if (cellObj != null) {

                    String cell = ExcelTool.getInstance().getCellContent(
                            cellObj);
                    if ("交易码".equals(cell) && k==0) {
                        //TODO 类型报错
                        //cell可能是null
                        if(null != sheetRow.getCell(k + 1)){
                            sheetRow.getCell(k + 1).setCellType(Cell.CELL_TYPE_STRING);
                            tranCode = sheetRow.getCell(k + 1).getStringCellValue();
//                        if (tranCode == null || "".equals(tranCode)) {
//                            logger.error(tranSheet.getSheetName()
//                                    + "sheet页，交易码为空");
//                            logInfoService.saveLog(tranSheet.getSheetName()
//                                    + "sheet页，交易码为空", "导入");
//                            flag = false;
//                        }
                            inter.setEcode(tranCode);
                        }
                    } else if ("交易名称".equals(cell) && k==0) {
                        //cell可能是null
                        if(null != sheetRow.getCell(k + 1)){
                            tranName = sheetRow.getCell(k + 1).getStringCellValue();
                       /* if (tranName == null || "".equals(tranName)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，交易名称为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，交易名称为空", "导入");
                            flag = false;
                        }*/
                            inter.setInterfaceName(tranName);
                        }
                    } else if ("接口功能描述".equals(cell)) {
                        //cell可能是null
                        if(null != sheetRow.getCell(k + 1)){
                            interfaceDesc = sheetRow.getCell(k + 1).getStringCellValue();
//                        if (interfaceDesc == null || "".equals(interfaceDesc)) {
//                            logger.error(tranSheet.getSheetName()
//                                    + "sheet页，接口功能描述为空");
//                            logInfoService.saveLog(tranSheet.getSheetName()
//                                    + "sheet页，接口功能描述为空", "导入");
//                            flag = false;
//                        }
                            inter.setDesc(interfaceDesc);
                        }
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

        return resMap;
    }

    protected boolean insertInterface(Interface inter,String systemId,String invokeType) {
        Map<String, String> paramMap = new HashMap<String, String>();
        boolean exists = false;
        Interface temp = interfaceDao.get(inter.getInterfaceId());
        exists = null!=temp;
        if(!exists){
            //不存在接口，则新增，并加入service_invoke记录
            String versionId = inter.getVersionId();/**接口版本管理，如果未存在新增版本信息，否则编辑**/
            if (versionId == null || "".equals(versionId)) {
                versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(),Constants.Version.TYPE_ELSE);
                inter.setVersionId(versionId);
            } else {
                versionService.editVersion(versionId);
            }
            inter.setOptDate(DateUtils.format(new Date()));
            inter.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
            interfaceDao.save(inter);
            //添加serviceInvoke记录，（原始接口导入需要setType）
            ServiceInvoke invoke = new ServiceInvoke();
            invoke.setSystemId(systemId);
            invoke.setInterfaceId(inter.getInterfaceId());
            invoke.setType(invokeType);
            serviceInvokeDAO.save(invoke);
        }else{
            String versionId = temp.getVersionId();/**接口版本管理，如果未存在新增版本信息，否则编辑**/
            if (versionId == null || "".equals(versionId)) {
                versionId = versionService.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, temp.getInterfaceId(),Constants.Version.TYPE_ELSE);
                temp.setVersionId(versionId);
            } else {
                versionService.editVersion(versionId);
            }
            temp.setOptDate(DateUtils.format(new Date()));
            temp.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
            temp.setInterfaceName(inter.getInterfaceName());
            temp.setEcode(inter.getEcode());
            temp.setStatus(inter.getStatus());
            interfaceDao.save(temp);
        }
        return exists;
    }

    public List executeInterfaceImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap,IndexDO indexDO){
        String systemId = indexDO.getSystemId();
        String invokeType = indexDO.getInvokeType();
        Interface inter = (Interface) infoMap.get("interface");
        inter.setStatus(indexDO.getInterfaceStatus());
        List<Ida> idainput = (List<Ida>) inputMap.get("idas");
        List<Ida> idaoutput = (List<Ida>) outMap.get("idas");
        logger.info("导入接口定义信息...");
        StringBuffer msg = new StringBuffer();
        boolean exists = insertInterface(inter,systemId,invokeType);
//        if(!exists){
            insertIDA(inter, idainput, idaoutput);
//        }else{
//            msg.append(""+inter.getInterfaceId()+"接口已经存在!");
//        }
        List list = new ArrayList();
        list.add(exists);
        list.add(msg);

        return list;
    }

    protected void insertIDA(Interface inter, List<Ida> idainput, List<Ida> idaoutput) {
        //添加报文，自动生成固定报文头<root><request><response>
        //root
        Ida ida = new Ida();
        String rootId = "", requestId = "", responseId = "";
        //先删除
        String hql = "delete from Ida where interfaceId=?";
        idaDao.exeHql(hql, inter.getInterfaceId());

        ida.setInterfaceId(inter.getInterfaceId());
        ida.setParentId(null);
        ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
        ida.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
        ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
        idaDao.save(ida);
        rootId = ida.getId();

        ida = new Ida();
        ida.setInterfaceId(inter.getInterfaceId());
        ida.setParentId(rootId);
        ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
        ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
        ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
        ida.setSeq(0);
        idaDao.save(ida);
        requestId = ida.getId();

        ida = new Ida();
        ida.setInterfaceId(inter.getInterfaceId());
        ida.setParentId(rootId);
        ida.setSeq(1);
        ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
        ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
        ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
        idaDao.save(ida);
        responseId = ida.getId();

        String parentId = null;
        for (int i = 0; i < idainput.size(); i++) {
            ida = idainput.get(i);
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(requestId);
            if (parentId != null) {
                ida.setParentId(parentId);
            }

            //包含bug，当节点end后，下一节点 不在request 或 response下 就会出现问题，
            if ("end".equalsIgnoreCase(ida.getRemark()) || "不映射".equalsIgnoreCase(ida.getRemark()) || ida.getStructName() == null || "".equals(ida.getStructName())) {
                if ("end".equalsIgnoreCase(ida.getRemark())) {
                    parentId = null;
                }
                continue;
            }
            //ida.setArgType("input");
            idaDao.save(ida);
            //包含子节点
            if (ida.getRemark() != null && ida.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                parentId = ida.getId();
            }
        }

        parentId = null;
        for (int i = 0; i < idaoutput.size(); i++) {
            ida = idaoutput.get(i);
            ida.setInterfaceId(inter.getInterfaceId());
            ida.setParentId(responseId);
            if (parentId != null) {
                ida.setParentId(parentId);
            }

            if ("end".equalsIgnoreCase(ida.getRemark()) || "不映射".equalsIgnoreCase(ida.getRemark()) || ida.getStructName() == null || "".equals(ida.getStructName())) {
                if ("end".equalsIgnoreCase(ida.getRemark())) {
                    parentId = null;
                }
                continue;
            }
            idaDao.save(ida);
            //包含子节点
            if (ida.getRemark() != null && ida.getRemark().toLowerCase().trim().indexOf("start") == 0) {
                parentId = ida.getId();
            }
        }

    }

    /**
     * 获取交易、服务、场景信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getInterfaceAndServiceInfo(Sheet tranSheet,IndexDO indexDO) {
        boolean flag = true;
        // 读取每个sheet页交易信息与服务信息
        int start = tranSheet.getFirstRowNum();
        int end = tranSheet.getLastRowNum();
        Interface inter = new Interface();
        inter.setInterfaceId(tranSheet.getSheetName());
        com.dc.esb.servicegov.entity.Service service = new com.dc.esb.servicegov.entity.Service();
        Operation oper = new Operation();
        String interfaceStatus = indexDO.getInterfaceStatus();
        String operationState =indexDO.getOperationState();
        inter.setStatus(interfaceStatus);
        oper.setState(operationState);
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
                        /*if (tranCode == null || "".equals(tranCode)) {
                            logger.error(tranSheet.getSheetName()
                                    + "sheet页，交易码为空");
                            logInfoService.saveLog(tranSheet.getSheetName()
                                    + "sheet页，交易码为空", "导入");
                            flag = false;
                        }*/
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
                       /* if (tranName == null || "".equals(tranName)) {
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
}
