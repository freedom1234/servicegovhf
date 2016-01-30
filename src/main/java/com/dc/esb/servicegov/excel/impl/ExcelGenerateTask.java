package com.dc.esb.servicegov.excel.impl;

import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.vo.RelationVO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public interface ExcelGenerateTask extends Runnable {

    public void initManager(ServiceServiceImpl serviceServiceImpl,
                            OperationServiceImpl operationServiceImpl, SystemServiceImpl systemManager,
                            InterfaceServiceImpl interfaceManager, MetadataServiceImpl metadataManager);

    public void setStyle1(CellStyle s1, CellStyle s2, CellStyle s3, CellStyle s4);

    public void setStyle2(CellStyle s1, CellStyle s2, CellStyle s3, CellStyle s4);

    public void setStyle3(CellStyle s1, CellStyle s2, CellStyle s3, CellStyle s4);

    public void init(RelationVO r, Workbook wb, Sheet sheet, CountDownLatch countDown, List<Map<String, SDA>> lstStructName);
}
