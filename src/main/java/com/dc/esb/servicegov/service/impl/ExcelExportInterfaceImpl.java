package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.IdaDAOImpl;
import com.dc.esb.servicegov.dao.impl.InterfaceDAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceInvokeDAOImpl;
import com.dc.esb.servicegov.dao.impl.SystemDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.Counter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
 * Created by jiangqi on 2015/8/11.
 */
@Service
@Transactional
public class ExcelExportInterfaceImpl extends AbstractBaseService {
    protected Log logger = LogFactory.getLog(getClass());

    private static final String INTERFACE = "interface";

    @Autowired
    private InterfaceDAOImpl interfaceDAO;
    @Autowired
    private SystemDAOImpl systemDAO;
    @Autowired
    private IdaDAOImpl idaDAO;
    @Autowired
    private ServiceInvokeDAOImpl serviceInvokeDAO;

    public HSSFWorkbook genderExcel(String[] interfaceIds, String type,String systemId){
        if(INTERFACE.equals(type)){
			return interfaceExcel(interfaceIds,systemId);
        }
        else{
            String errorMsg = "暂时不支持类型为["+type+"]的文档导出！";
            logger.error(errorMsg);
        }
        return null;
    }

    /**
     * TODO根据服务id生成excel
     * @param interfaceIds
     * @return
     */
    public HSSFWorkbook interfaceExcel(String[] interfaceIds,String systemId){
        List<Interface> list = new ArrayList<Interface>();
        for (String id : interfaceIds){
            Interface inter = interfaceDAO.findUniqueBy("interfaceId", id);
            list.add(inter);
        }
        com.dc.esb.servicegov.entity.System system = systemDAO.findUniqueBy("systemId", systemId);

        HSSFWorkbook workbook = fillExcel(list,system);
        return workbook;
    }

    private HSSFWorkbook fillExcel(List<Interface> interList,System system){
        if(null  != interList){
            HSSFWorkbook workbook =  getTempalteWb(Constants.EXCEL_TEMPLATE_INTERFACE);
            fillIndex(workbook, interList,system);
            fillMapings(workbook, interList);
            //List<InterfaceHeadVO> ihvList = getByInterfaceHeadVOServiceId(serviceId);
            //fillHeads(workbook, ihvList);
            return workbook;
        }
        return null;
    }

    /**
     * 循环填充mapping
     * @param workbook
     * @param interList
     * @return
     */
    private void fillMapings(HSSFWorkbook workbook, List<Interface> interList) {
        HSSFSheet mappingSheet = workbook.getSheet("MAPPING");
        int poolSize = interList.size() > 10?10:interList.size();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < interList.size(); i++) {
            if(null == interList.get(i).getInterfaceId()) continue;
            HSSFSheet sheet = workbook.cloneSheet(workbook.getSheetIndex(mappingSheet));//复制模板中mapping页
            workbook.setSheetName(workbook.getSheetIndex(sheet), interList.get(i).getInterfaceId());//修改sheet名称
            fillMapping(sheet, interList.get(i));
        }
        pool.shutdown();
        while(true){ //判断多线程是否结束
            try {
                if(pool.isTerminated()){
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
     * TODO 填充mapping页
     */
    private boolean fillMapping(HSSFSheet sheet, Interface inter){
        try {
            HSSFRow row0 = sheet.getRow(0);
            HSSFRow row1 = sheet.getRow(1);
            HSSFRow row2 = sheet.getRow(2);
            HSSFRow row3 = sheet.getRow(3);

            if (null != inter){
                row0.createCell(1).setCellValue(inter.getEcode());
                row1.createCell(1).setCellValue(inter.getInterfaceName());
                row2.createCell(1).setCellValue(inter.getDesc());
            }


            //获取request元数据
            List<Ida> reqListIda = getIdaByParentName(inter.getInterfaceId(), "request");
            List<Ida> resListIda = getIdaByParentName(inter.getInterfaceId(), "response");
            Counter counter = new Counter(6);
            for (int i = 0; i < reqListIda.size(); i++) {
                fillMappRow(sheet, counter, reqListIda.get(i) );
            }
            counter.increment();
            for (int i = 0; i < resListIda.size(); i++) {
                fillMappRow(sheet, counter, resListIda.get(i) );
            }
//            if(resListIda.size() > 0){//处理没有对应的ida，可能没有
//                for(int i = 0; i < resListIda.size(); i++){
//                    fillIda(sheet, 8+reqListSDA.size()+reqListIda.size()+resListSDA.size()+i, reqListIda.get(i));
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("===========填充[" + sheet.getSheetName() + "]页失败===========");
            return false;
        }
        return true;
    }

    public void fillMappRow(HSSFSheet sheet, Counter counter, Ida ida){
        sheet.createRow(sheet.getLastRowNum() + 1);
        counter.increment();
        sheet.shiftRows(counter.getCount(), sheet.getLastRowNum(), 1, true, false); //插入一行
        //插入ida
        fillIda(sheet, counter.getCount(), ida);
    }

    public void fillIda(HSSFSheet sheet, int index, Ida ida){
        HSSFRow row = sheet.getRow(index);
        fillIda(sheet, row, ida);
        if(!StringUtils.isEmpty(ida.getType()) && (ida.getType().equalsIgnoreCase("array") || ida.getType().equalsIgnoreCase("struct"))){
            List<Ida> childList = getIdaChildren(ida.getId());
            for(int i = 0; i < childList.size(); i++){
                fillIda(sheet, index + i + 1, childList.get(i));
            }
            ida.setRemark("end");
            HSSFRow last = sheet.getRow(index + childList.size() + 1);
            fillIda(sheet, last, ida);
        }
    }

    public void fillIda(HSSFSheet sheet, HSSFRow row, Ida ida){
        row.createCell(0).setCellValue(ida.getStructName());//英文名称
        row.createCell(1).setCellValue(ida.getStructAlias());//中文名称
        row.createCell(2).setCellValue(ida.getType());//类型
        row.createCell(3).setCellValue(ida.getLength());//长度
        row.createCell(4).setCellValue(ida.getRequired());//是否必输
        row.createCell(5).setCellValue(ida.getRemark());//备注
    }

    public List<Ida> getIdaChildren(String id){
        List<Ida> list = idaDAO.findBy("_parentId", id);
        return list;
    }

    public List<Ida> getIdaByParentName(String interfaceId, String parentName){
        String hql = " from " + Ida.class.getName() + " as i where i._parentId in("+
                " select i2.id from "+ Ida.class.getName() + " as i2 where i2.interfaceId = ? and structName = ?"+
                ")";
        List<Ida> list = idaDAO.find(hql, interfaceId, parentName);
        return list;
    }

    /**
     * TODO 填充index页
     * @param workbook
     * @param interList
     * @param system
     */
    private boolean fillIndex(HSSFWorkbook workbook, List<Interface> interList,System system){
        HSSFSheet sheet = workbook.getSheet("INDEX");
        try {
            for (int i = 0; i < interList.size(); i++) {
                HSSFRow row = sheet.createRow(i+1);
                row.createCell(0).setCellValue(interList.get(i).getInterfaceId());//接口id
                row.createCell(1).setCellValue(system.getSystemAb());//系统
                String status = "";
                if(interList.get(i).getStatus() != null){
                    if(interList.get(i).getStatus().equals(Constants.INTERFACE_STATUS_TC)){
                        status = "投产";
                    } else if (interList.get(i).getStatus().equals(Constants.INTERFACE_STATUS_FQ)){
                        status = "废弃";
                    }
                }
                row.createCell(2).setCellValue(status);//交易状态
                Map<String ,String > map = new HashMap<String, String>();
                map.put("interfaceId",interList.get(i).getInterfaceId());
                map.put("systemId",system.getSystemId());
                row.createCell(4).setCellValue("修改");//导入模式
                List<ServiceInvoke> list = serviceInvokeDAO.findBy(map);
                if(null == list)  continue;
                String type = "";
                if(list.get(0).getType().equals(Constants.INVOKE_TYPE_PROVIDER)){
                    type = "Provider";
                }else if(list.get(0).getType().equals(Constants.INVOKE_TYPE_CONSUMER)){
                    type = "Consumer";
                }
                row.createCell(3).setCellValue(system.getSystemAb());//接口方向

            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("===========填充[" + sheet.getSheetName() + "]页失败===========");
            return false;
        }
        return true;
    }

    /**
     * TODO 读取模板
     * @return
     */
    public  HSSFWorkbook getTempalteWb(String templatePath){
        File file = new File(templatePath);
        HSSFWorkbook wb = null;
        BufferedInputStream in = null;
        InputStream is;
        try {
            is = new FileInputStream(file);
            wb = new HSSFWorkbook(is);
//            in = new BufferedInputStream(new FileInputStream(file));
//            POIFSFileSystem fs = new POIFSFileSystem(in);
//
//            wb = new HSSFWorkbook(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    @Override
    public HibernateDAO getDAO() {
        return null;
    }
}
