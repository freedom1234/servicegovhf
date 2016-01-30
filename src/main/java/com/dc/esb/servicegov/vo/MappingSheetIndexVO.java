package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.util.ExcelTool;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 */
public class MappingSheetIndexVO {
    public Integer inputIndex = 6;//默认输入开始行
    public Integer outputIndex = 7;//默认输出开始行

    public Integer idaEnNameCol = 0;
    public Integer idaChNameCol = 1;
    public Integer idaTypeCol = 2;
    public Integer idaLengthCol = 3;
    public Integer idaRequiredCol = 4;
    public Integer idaRemarkCol = 5;

    public Integer sdaEnNameCol = 7;
    public Integer sdaChNameCol = 8;
    public Integer sdaTypeAndLengthCol = 9;
    public Integer sdaConstrantCol = 10;
    public Integer sdaRequiredCol = 11;
    public Integer sdaRemarkCol = 12;
    public MappingSheetIndexVO(Sheet sheet){
        if(null != sheet){
            for(int i=0; i <= sheet.getLastRowNum(); i++){
                String value = ExcelTool.getInstance().getCellContent(sheet.getRow(i).getCell(0));
                if("输入".equals(value)){
                    inputIndex = i+1;
                }
                if("输出".equals(value)){
                    outputIndex = i+1;
                    break;
                }
            }
        }
    }
}
