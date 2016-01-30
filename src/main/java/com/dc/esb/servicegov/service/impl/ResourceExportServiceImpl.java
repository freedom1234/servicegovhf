package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.CategoryWordDAOImpl;
import com.dc.esb.servicegov.dao.impl.EnglishWordDAOImpl;
import com.dc.esb.servicegov.dao.impl.MetadataDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.CategoryWord;
import com.dc.esb.servicegov.entity.EnglishWord;
import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.excel.support.CellStyleSupport;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

/**
 * Created by wang on 2015/9/10.
 */

@Service
@Transactional
public class ResourceExportServiceImpl  extends AbstractBaseService<String, String> {
    @Autowired
    EnglishWordDAOImpl englishWordDAO;
    @Autowired
    CategoryWordDAOImpl categoryWordDAO;
    @Autowired
    MetadataDAOImpl metadataDAO;

    static String RecordSheetName = "表1修订记录";
    static String EnglishWordSheetName = "表2中英文名称及缩写对照表";
    static String CategoryWordSheetName = "表3类别词";
    static String DataSheetName = "表4元数据";
    static String ArraySheetName = "表5数组";
    static String OutdateSheetName = "表7过时元数据";

    static HSSFCellStyle commonStyle;//默认单元格样式

    public HSSFWorkbook genderResourceExcel() {
        //读取数据字典模板
        HSSFWorkbook workbook = getTempalteWb(Constants.EXCEL_TEMPLATE_DATA_DICTIONARY);
        commonStyle =  CellStyleSupport.leftStyle(workbook);
        //填充修订记录页
        HSSFSheet recordSheet = workbook.getSheet(RecordSheetName);
        fillRecordSheet(recordSheet);
        //填充英文单词页
        HSSFSheet englishWordSheet = workbook.getSheet(EnglishWordSheetName);
        fillEnglishWordSheet(englishWordSheet);
        //填充类别词页
        HSSFSheet categoryWordSheet = workbook.getSheet(CategoryWordSheetName);
        fillCategoryWordSheet(categoryWordSheet);
        //填充数据字典页
        HSSFSheet dataSheet = workbook.getSheet(DataSheetName);
        fillDataSheet(dataSheet);
        //填充数组页
        HSSFSheet arraySheet = workbook.getSheet(ArraySheetName);
        fillArraySheet(arraySheet);
        //填充过时页
        HSSFSheet outdateSheet = workbook.getSheet(OutdateSheetName);
        fillOutdateSheet(outdateSheet);
        return workbook;
    }
    /**填充修订记录页**/
    public boolean fillRecordSheet(HSSFSheet sheet){
        return true;
    }
    /**填充英文单词页**/
    public boolean fillEnglishWordSheet(HSSFSheet sheet){
        List<EnglishWord> list = englishWordDAO.getAll();
        for(int i=0; i < list.size(); i++){
            EnglishWord englishWord = list.get(i);
            if(englishWord != null){
                HSSFRow row = sheet.createRow(i + 1);
                String values[] = {englishWord.getChineseWord(), englishWord.getEnglishWord(), englishWord.getWordAb(), englishWord.getOptDate(), englishWord.getOptUser()};
                setRowValue(row, commonStyle, values);
            }
        }
        return true;
    }
    /**填充类别词页**/
    public boolean fillCategoryWordSheet(HSSFSheet sheet){
        List<CategoryWord> list = categoryWordDAO.getAll();
        for(int i = 0; i < list.size(); i++){
            CategoryWord categoryWord = list.get(i);
            if(categoryWord != null){
                HSSFRow row = sheet.createRow(i + 1);
                String values[] = {categoryWord.getChineseWord(), categoryWord.getEnglishWord(), categoryWord.getEsglisgAb(), categoryWord.getRemark()};
                setRowValue(row, commonStyle, values);
            }
        }
        return true;
    }
    /**填充数据字典页**/
    public boolean fillDataSheet(HSSFSheet sheet){
        String hql = " from " + Metadata.class.getName() +" m where ((m.type != ? and m.type != ?) or m.type is null) and m.status != ?";
        List<Metadata> list = metadataDAO.find(hql, Constants.Metadata.ARRAY_TYPE, Constants.Metadata.STRUCT_TYPE, Constants.Metadata.STATUS_OUTDATED);
        for(int i = 0; i < list.size(); i++){
            Metadata metadata = list.get(i);
            if(metadata != null){
                HSSFRow row = sheet.createRow(i + 2);
                String values[] = {metadata.getDataCategory(), "", metadata.getBuzzCategory(), metadata.getMetadataId(), metadata.getChineseName(), metadata.getMetadataName(), metadata.getCategoryWordId(),
                "", metadata.getFormula(), "", "", "", "", metadata.getOptDate(), metadata.getOptUser(), metadata.getRemark()};
                setRowValue(row, commonStyle, values);
            }
        }
        return true;
    }
    /**填充数据字典页**/
    public boolean fillOutdateSheet(HSSFSheet sheet){
        String hql = " from " + Metadata.class.getName() +" m where ((m.type != ? and m.type != ?) or m.type is null) and m.status = ?";
        List<Metadata> list = metadataDAO.find(hql, Constants.Metadata.ARRAY_TYPE, Constants.Metadata.STRUCT_TYPE, Constants.Metadata.STATUS_OUTDATED);
        for(int i = 0; i < list.size(); i++){
            Metadata metadata = list.get(i);
            if(metadata != null){
                HSSFRow row = sheet.createRow(i + 2);
                String values[] = {metadata.getDataCategory(), "", metadata.getBuzzCategory(), metadata.getMetadataId(), metadata.getChineseName(), metadata.getMetadataName(), metadata.getCategoryWordId(),
                        "", metadata.getFormula(), "", "", "", "", metadata.getOptDate(), metadata.getOptUser(), metadata.getRemark()};
                setRowValue(row, commonStyle, values);
            }
        }
        return true;
    }
    /**填充数组页**/
    public boolean fillArraySheet(HSSFSheet sheet){
        String hql = " from " + Metadata.class.getName() +" m where m.type = ? or m.type = ?";
        List<Metadata> list = metadataDAO.find(hql, Constants.Metadata.ARRAY_TYPE, Constants.Metadata.STRUCT_TYPE);
        for(int i = 0; i < list.size(); i++){
            Metadata metadata = list.get(i);
            if(metadata != null){
                HSSFRow row = sheet.createRow(i + 2);
                String values[] = {metadata.getDataCategory(), "", metadata.getBuzzCategory(), metadata.getMetadataId(), metadata.getChineseName(), metadata.getMetadataName(), metadata.getCategoryWordId(),
                        "", metadata.getFormula(), "", "", "", "", metadata.getOptDate(), metadata.getOptUser(), metadata.getRemark()};
                setRowValue(row, commonStyle, values);
            }
        }
        return true;
    }
    //读取模板方法
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

    public void setRowValue(HSSFRow row, HSSFCellStyle cellStyle, String[] values) {
        for (int i = 0; i < values.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue( values[i]);
        }
    }
    @Override
    public HibernateDAO<String, String> getDAO() {
        return null;
    }
}
