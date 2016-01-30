package com.dc.esb.servicegov.excel;

import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.impl.ExcelExportServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by Administrator on 2015/7/21.
 */
public class MappingSheetTask extends  Thread{
    protected Log logger = LogFactory.getLog(getClass());
    private HSSFSheet sheet;
    private ServiceInvoke serviceInvoke;

    private ExcelExportServiceImpl service;

    public MappingSheetTask( HSSFSheet sheet, ServiceInvoke serviceInvoke, ExcelExportServiceImpl service){
        this.sheet = sheet;
        this.serviceInvoke = serviceInvoke;
        this.service = service;
    }

    @Override
    public void run(){
        logger.info("===========交易[" + sheet.getSheetName() + "],开始制作sheet=============");
        long time = System.currentTimeMillis();
        service.fillMapping(sheet, serviceInvoke);
//        service.notify();
        long useTime = System.currentTimeMillis() - time;
        logger.info("===========交易[" + sheet.getSheetName() + "],sheet制作完成，耗时" + useTime + "ms=============");

    }
}
