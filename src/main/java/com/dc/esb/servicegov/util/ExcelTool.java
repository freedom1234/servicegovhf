package com.dc.esb.servicegov.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

	public class ExcelTool {
		private static ExcelTool tool = new ExcelTool();
		private static final Log log = LogFactory.getLog(ExcelTool.class);

		private ExcelTool() {

		}

		public static ExcelTool getInstance() {
			if (tool == null) {
				tool = new ExcelTool();
			}
			return tool;
		}

		/**
		 * 
		 * 功能:获取excel文件的工作页
		 * 
		 * @param filePath
		 * @return
		 * @throws Exception
		 *             Workbook
		 */
		public Workbook getExcelWorkbook(String filePath) throws Exception {
			return getExcelWorkbook(new File(filePath));
		}

		public Workbook getExcelWorkbook(File file) throws Exception {
			FileInputStream is = new FileInputStream(file);
			Workbook wb = getExcelWorkbook(is);
			return wb;
		}

		public Workbook getExcelWorkbook(InputStream in) throws InvalidFormatException, IOException {
			Workbook wb = WorkbookFactory.create(in);
			return wb;
		}

		public String getCellContent(Sheet sheet, int rowNum, int colNum) {
			Row row = sheet.getRow(rowNum);
			String contents = "";
			if (row != null && row.getCell(colNum) != null) {
				contents = getCellContent(row.getCell(colNum)).trim();
			}
			return contents;
		}

		public String getCellContent(Cell cell) {
			DecimalFormat df = new DecimalFormat("0");
			if(cell == null){
				return "";
			}
			int type = cell.getCellType();
			String value = "";
			switch (type) {
			case Cell.CELL_TYPE_FORMULA:
				try{
					value = String.valueOf(df.format(cell.getNumericCellValue())).toString().trim();
				}catch(Exception e){
					log.info("处理公式单元格失败！");
				}
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue().toString().trim();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue()).toString().trim();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				value = String.valueOf(df.format(cell.getNumericCellValue())).toString().trim();
				if (value.endsWith(".0")) {
					value = value.substring(0, value.length() - 2).trim();
				}
				break;
			default:
				value = cell.toString().trim();
				break;
			}
			return value;
		}

		public boolean isNullRow(Sheet sheet, int rowNum) {
			int rowCount = sheet.getLastRowNum();
			if (rowCount >= rowNum) {
				return false;
			} else {
				return true;
			}
		}

		public int getSheetCount(Workbook wb) {
			return wb.getNumberOfSheets();
		}
		
		public int getSheetRows(Sheet sheet){
			return sheet.getPhysicalNumberOfRows();
		}

	}


