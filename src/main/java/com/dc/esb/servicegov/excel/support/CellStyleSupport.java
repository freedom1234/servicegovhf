package com.dc.esb.servicegov.excel.support;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Created by wang on 2015/8/17.
 */
public class CellStyleSupport {
    public static HSSFCellStyle commonStyle(HSSFWorkbook wb){
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        //生成一个字体
        HSSFFont font=wb.createFont();
        font.setFontName("宋体");
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short) 9);
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }
    public static HSSFCellStyle arrayStyle(HSSFWorkbook wb){
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
//        cellStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        //生成一个字体
        HSSFFont font=wb.createFont();
        font.setFontName("宋体");
//        font.setColor(HSSFColor.DARK_YELLOW.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short) 9);
//        cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
//        cellStyle.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public static HSSFCellStyle leftStyle(HSSFWorkbook wb){
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//居中
        //生成一个字体
        HSSFFont font=wb.createFont();
        font.setFontName("宋体");
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short) 9);
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

}
