package com.dc.esb.servicegov.rsimport.impl;

import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.entity.MetadataHis;
import com.dc.esb.servicegov.rsimport.IResourceParser;
import com.dc.esb.servicegov.rsimport.support.ExcelUtils;
import com.dc.esb.servicegov.service.impl.LogInfoServiceImpl;
import com.dc.esb.servicegov.service.impl.MetadataHisServiceImpl;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;
import com.dc.esb.servicegov.service.impl.VersionServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MetadataOutdatedParserImpl implements IResourceParser {
    @Autowired
    LogInfoServiceImpl logInfoService;
    private static final Log log = LogFactory.getLog(MetadataXlsxParserImpl.class);

    private static final String SHEET_NAME = "表7过时元数据";
    private static final int START_ROW_NUM = 2;
    private static final String DATA_CATEGORY = "数据分类";
    private static final String BUZZ_CATEGORY = "业务分类";
    private static final String METADATA_ID = "标准数据名称";
    private static final String CHINESE_NAME = "标准中文名称";
    private static final String METADATA_NAME = "标准英文名称";
    private static final String CATEGORY_WORD_ID = "类别词";
    private static final String TYPE = "数据类型";
    private static final String DATA_FORMULA = "数据格式";
    private static final String OPT_DATE = "更新日期";
    private static final String OPT_USER= "修订者";
    private static final String REMARK= "备注";

    private static int DATA_CATEGORY_COLUMN = 0;
    private static int BUZZ_CATEGORY_COLUMN = 2;
    private static int METADATA_ID_COLUMN = 3;
    private static int CHINESE_NAME_COLUMN = 4;
    private static int METADATA_NAME_COLUMN = 5;
    private static int CATEGORY_WORD_ID_COLUMN = 6;
    private static int TYPE_COLUMN = 7;
    private static int DATA_FORMULA_COLUMN = 8;
    private static int OPT_DATE_COLUMN = 13;
    private static int OPT_USER_COLUMN = 14;
    private static int REMARK_COLUMN = 15;
    @Autowired
    private MetadataServiceImpl metadataService;
    @Autowired
    private MetadataHisServiceImpl metadataHisService;
    @Autowired
    private VersionServiceImpl versionService;
    @Override
    public void parse(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        if (null != sheet) {

            parseSheet(sheet);
        }
    }

    private void parseSheet(Sheet sheet) {
        initIndexColnum(sheet);
//		List<Metadata> metadatas = new ArrayList<Metadata>();
        for (int rowNum = START_ROW_NUM; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Metadata metadata = parseRow(row, rowNum);
            if(null == metadata) continue;
            String userName = (String) SecurityUtils.getSubject().getPrincipal();
            metadata.setOptUser(userName);
            metadata.setOptDate(DateUtils.format(new Date() ));
            try {
                Metadata metadataExsit = metadataService.getById(metadata.getMetadataId());
                if(null == metadataExsit){
                    metadataService.save(metadata);
                }

                MetadataHis metadataHis = new MetadataHis(metadata);
                metadataHisService.addMetadataHis(metadataHis);
            } catch(Exception e ){
                log.error("元数据[" + metadata.getMetadataId() + "]导入出错");
                logInfoService.saveLog("第" + (rowNum+1) + "行导入[" + metadata.getMetadataId() + "]失败！"+e.getMessage(), "表7过时元数据");
            }
        }
    }

    private Metadata parseRow(Row row, int rowNum) {
        try{
            Metadata metadata =  new Metadata();
            metadata.setDataCategory(getValueFromCell(row, DATA_CATEGORY_COLUMN));
            metadata.setMetadataId(getValueFromCell(row, METADATA_ID_COLUMN));
            metadata.setChineseName(getValueFromCell(row, CHINESE_NAME_COLUMN));
            metadata.setMetadataName(getValueFromCell(row, METADATA_NAME_COLUMN));
            metadata.setCategoryWordId(getValueFromCell(row, CATEGORY_WORD_ID_COLUMN));
            metadata.setBuzzCategory(getValueFromCell(row, BUZZ_CATEGORY_COLUMN));
            metadata.setRemark(getValueFromCell(row, REMARK_COLUMN));
            String dataFormula = getValueFromCell(row, DATA_FORMULA_COLUMN);
            //TODO 本地化修改
            String[] str = dataFormula.split("[()]+");
            String type = getTypeFromFormula(str[0]);
            String length = "";
            String scale = "";
            if (str.length > 1) {
                length = getLengthFromFormula(str[1].replaceAll("，", ","));
                scale = getScaleFromFormula(str[1].replaceAll("，", ","));
            }
            metadata.setType(type);
            metadata.setLength(length);
            metadata.setScale(scale);
            metadata.setOptDate(getValueFromCell(row, OPT_DATE_COLUMN));
            metadata.setOptUser(getValueFromCell(row, OPT_USER_COLUMN));
            metadata.setStatus(Constants.Metadata.STATUS_OUTDATED);
            return metadata;
        }catch (Exception e){
            log.error(e, e);
            logInfoService.saveLog("第"+(rowNum+1)+"行解析数据失败！", "表7过时元数据");
        }
        return null;
    }

    //TODO 本地化修改
    public static String getTypeFromFormula(String formula) {
        String type = "String";
        if (null != formula) {
            if (StringUtils.containsIgnoreCase(formula, "ARRAY")) {
                type = Constants.Metadata.ARRAY_TYPE;
            } else if (StringUtils.containsIgnoreCase(formula, "STRUCT")) {
                type = Constants.Metadata.STRUCT_TYPE;
            }
        }
        return type;
    }
    public static String getLengthFromFormula(String formula) {
        String length = "";
        if (null != formula) {
            String str[] = formula.split(",");
            length = str[0];
        }
        return length;
    }
    public static String getScaleFromFormula(String formula) {
        String scale = "";

        if (null != formula) {
            String str[] = formula.split(",");
            if (str.length > 1) {
                scale = str[1];
            }
        }
        return scale;
    }
    public static String getValueFromCell(Row row, int column) {
        return ExcelUtils.getValue(row.getCell(column));
    }
    /**
     * 初始化字段序号
     * @param sheet
     */
    public void initIndexColnum(Sheet sheet){
        if(sheet != null){
            Row row = sheet.getRow(1);
            for(int i = 0; i < row.getLastCellNum(); i++){//遍历第1行所有单元格
                String content = row.getCell(i).getStringCellValue();

                if(DATA_CATEGORY.equals(content)){
                    DATA_CATEGORY_COLUMN = i;
                }
                if(BUZZ_CATEGORY.equals(content)){
                    BUZZ_CATEGORY_COLUMN = i;
                }
                if(METADATA_ID.equals(content)){
                    METADATA_ID_COLUMN = i;
                }
                if(CHINESE_NAME.equals(content)){
                    CHINESE_NAME_COLUMN = i;
                }
                if(METADATA_NAME.equals(content)){
                    METADATA_NAME_COLUMN = i;
                }
                if(CATEGORY_WORD_ID.equals(content)){
                    CATEGORY_WORD_ID_COLUMN = i;
                }
                if(TYPE.equals(content)){
                    TYPE_COLUMN = i;
                }
                if(DATA_FORMULA.equals(content)){
                    DATA_FORMULA_COLUMN = i;
                }
                if(OPT_DATE.equals(content)){
                    OPT_DATE_COLUMN = i;
                }
                if(OPT_USER.equals(content)){
                    OPT_USER_COLUMN = i;
                }
                if(REMARK.equals(content)){
                    REMARK_COLUMN = i;
                }
            }
        }
    }

}
