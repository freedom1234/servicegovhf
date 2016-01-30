package com.dc.esb.servicegov.rsimport.support;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtils {
	private static final Log log = LogFactory.getLog(ExcelUtils.class);

	public static String getValue(Cell cell) {
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
}
