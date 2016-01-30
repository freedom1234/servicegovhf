package com.dc.esb.servicegov.excel.impl;

import com.dc.esb.servicegov.excel.support.MappingExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Iterator;

public class ExcelStyleSetting {

	//设置索引页其他行样式
	public static void setSturctIndexOthersStyle(Sheet sheet,Workbook wb, CellStyle cellStyle){
		for (int j = 0; j < sheet.getLastRowNum(); j++) {
			Row row = sheet.getRow(j + 1);
			row.setHeightInPoints((short) 20);
			for (int i = 0; i < 4; i++) {
				row.getCell(i).setCellStyle(cellStyle);
			}
		}
	}


	//设置元数据详细页的样式
	public static void setMetadataDetailStyle(Sheet metadataSheet,Workbook wb , int len
			, CellStyle cellStyleArr, CellStyle cellStyleGreen,
				CellStyle cellStyleMaroon, CellStyle cellStyleYellow){
		metadataSheet.setColumnWidth(0, 15*256);
		metadataSheet.setColumnWidth(1, 18*256);
		metadataSheet.setColumnWidth(2, 16*256);
		metadataSheet.setColumnWidth(3, 14*256);
		metadataSheet.setColumnWidth(4, 17*256);
		metadataSheet.setColumnWidth(5, 2*256);
		metadataSheet.setColumnWidth(6, 14*256);
		metadataSheet.setColumnWidth(7, 16*256);
		metadataSheet.setColumnWidth(8, 17*256);
		metadataSheet.setColumnWidth(9, 18*256);

		CellStyle cellStyle=MappingExcelUtils.getCellStyle(wb);


		//第一行
		Iterator<Row> iterator=metadataSheet.iterator();
		for(int j=0;j<10;j++){
			metadataSheet.getRow(0).getCell(j).setCellStyle(cellStyle);
			//设置行高度
			metadataSheet.getRow(0).setHeightInPoints((short)20);
		}


		//处理其他行
		while (iterator.hasNext()) {
			Row row = iterator.next();
			for (int i = 0; i < 10; i++) {
				row.getCell(i).setCellStyle(cellStyle);
				row.setHeightInPoints((short) 20);
			}
		}

		//设置某些单元格的粗体字
		CellStyle cellStyleBold=MappingExcelUtils.getCellStyleBold(wb);
		for(int i=0;i<10;i++){
			metadataSheet.getRow(2).getCell(i).setCellStyle(cellStyleBold);
			metadataSheet.getRow(3).getCell(i).setCellStyle(cellStyleBold);
		}
		metadataSheet.getRow(0).getCell(0).setCellStyle(cellStyleBold);
		metadataSheet.getRow(0).getCell(6).setCellStyle(cellStyleBold);
		metadataSheet.getRow(1).getCell(0).setCellStyle(cellStyleBold);
		metadataSheet.getRow(1).getCell(6).setCellStyle(cellStyleBold);

		for(int i=0;i<=4;i++){
			metadataSheet.getRow(2).getCell(i).setCellStyle(cellStyleYellow);
			metadataSheet.getRow(3).getCell(i).setCellStyle(cellStyleYellow);
		}
		for(int i=6;i<=9;i++){
			metadataSheet.getRow(2).getCell(i).setCellStyle(cellStyleYellow);
			metadataSheet.getRow(3).getCell(i).setCellStyle(cellStyleYellow);
		}

		for(int i=0;i<=3;i++){
			metadataSheet.getRow(i).getCell(5).setCellStyle(cellStyleMaroon);
		}

		for(int i=5;i<5+len;i++){
			metadataSheet.getRow(i).getCell(5).setCellStyle(cellStyleMaroon);
		}

		for(int i=0;i<10;i++){
			metadataSheet.getRow(4).getCell(i).setCellStyle(cellStyleGreen);
			metadataSheet.getRow(5+len).getCell(i).setCellStyle(cellStyleGreen);
		}

		Iterator<Row> iteratorArr=metadataSheet.iterator();
		// 处理其他行
		while (iteratorArr.hasNext()) {
			Row row = iteratorArr.next();
			for (int i = 0; i < 10; i++) {
				// Array的行标明为黄色
				if ("Array".equals(row.getCell(7).getStringCellValue().trim())) {
					row.getCell(i).setCellStyle(cellStyleArr);
				}
			}
		}

		//合并单元格
		metadataSheet.addMergedRegion(new CellRangeAddress(0,0,1,4));
		metadataSheet.addMergedRegion(new CellRangeAddress(0,0,7,9));
		metadataSheet.addMergedRegion(new CellRangeAddress(1,1,1,4));
		metadataSheet.addMergedRegion(new CellRangeAddress(1,1,7,9));
		metadataSheet.addMergedRegion(new CellRangeAddress(2,2,0,4));
		metadataSheet.addMergedRegion(new CellRangeAddress(2,2,6,9));
		metadataSheet.addMergedRegion(new CellRangeAddress(4,4,0,9));
		metadataSheet.addMergedRegion(new CellRangeAddress(5+len,5+len,0,9));
		metadataSheet.addMergedRegion(new CellRangeAddress(0,3,5,5));
		metadataSheet.addMergedRegion(new CellRangeAddress(5,5+len,5,5));
	}

	//创建空白行，颜色为浅绿色
	public static void createBlankRow(Sheet metadataSheet,Workbook wb,int index){

		Row row=metadataSheet.createRow(index);
		row.createCell(0).setCellValue("");
		row.createCell(1).setCellValue("");
		row.createCell(2).setCellValue("");
		row.createCell(3).setCellValue("");
		row.createCell(4).setCellValue("");
		row.createCell(5).setCellValue("");
		row.createCell(6).setCellValue("");
		row.createCell(7).setCellValue("");
		row.createCell(8).setCellValue("");
		row.createCell(9).setCellValue("");
		row.createCell(10).setCellValue("");
	}


	//设置索引页第一行样式
	public static void setStructIndexStyle(Sheet sheet,Workbook wb, CellStyle cellStyle){
		sheet.setColumnWidth(0, 15*256);
		sheet.setColumnWidth(1, 18*256);
		sheet.setColumnWidth(2, 16*256);
		sheet.setColumnWidth(3, 14*256);
		Row row=sheet.getRow(0);
		row.setHeightInPoints((short) 20);
		for(int i=0;i<4;i++){
			row.getCell(i).setCellStyle(cellStyle);
		}
	}
	
}
