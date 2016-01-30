package com.dc.esb.servicegov.excel.support;

import com.dc.esb.servicegov.entity.SDA;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MappingExcelUtils {
	
	public static List<Map<String, SDA>> lstStructName = new ArrayList<Map<String, SDA>>();
	
	public static List<Map<String, SDA>> getLstStructName() {
		return lstStructName;
	}

	public static void addLstStructName(List<Map<String, SDA>> l) {
		lstStructName.addAll(l);
	}

	private MappingExcelUtils(){
	}
	
	public static CellStyle getBaseCellStyle(Workbook wb){
		CellStyle cellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getCellStyleBold(Workbook wb){
		CellStyle cellStyleBold=wb.createCellStyle();
		cellStyleBold.setFont(getBoldFont(wb));
		cellStyleBold.setWrapText(true);
		cellStyleBold.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleBold.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleBold.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleBold.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleBold.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleBold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyleBold;
	}

	public static CellStyle getCellStyle(Workbook wb) {
		CellStyle cellStyle=wb.createCellStyle();
		cellStyle.setFont(getNormalFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getCellStyle4r(Workbook wb) {
		CellStyle cellStyle=wb.createCellStyle();
		//font.setItalic(true);
		//font.setStrikeout(true);
		cellStyle.setFont(getNormalFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getArrayCellStyle4r(Workbook wb) {
		CellStyle cellStyleArr=wb.createCellStyle();
		cellStyleArr.setFont(getNormalFont(wb));
		cellStyleArr.setWrapText(true);
		cellStyleArr.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleArr.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleArr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleArr.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
		cellStyleArr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyleArr;
	}

	public static CellStyle getCellStyleLightYellow(Workbook wb) {
		CellStyle cellStyle1=wb.createCellStyle();
		cellStyle1.setFont(getBoldFont(wb));
		cellStyle1.setWrapText(true);
		cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle1.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle1.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
		cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle1;
	}

	public static CellStyle getCellStyleLightGreen(Workbook wb) {
		CellStyle cellStyle2=wb.createCellStyle();
		cellStyle2.setFont(getBoldFont(wb));
		cellStyle2.setWrapText(true);
		cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle2.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
		cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle2;
	}

	public static CellStyle getCellStyleMaroon(Workbook wb) {
		CellStyle cellStyle3=wb.createCellStyle();
		cellStyle3.setFont(getBoldFont(wb));
		cellStyle3.setWrapText(true);
		cellStyle3.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle3.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle3.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle3.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle3.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle3.setFillForegroundColor(new HSSFColor.MAROON().getIndex());
		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle3;
	}

	/**
	 *
	 * @param wb
	 * @return
	 */
	public static Font getBoldFont(Workbook wb) {
		Font font2=wb.createFont();
		font2.setFontHeightInPoints((short)9);
		font2.setFontName("宋体");
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		return font2;
	}

	/**
	 *
	 * @param wb
	 * @return
	 */
	public static Font getNormalFont(Workbook wb) {
		Font font=wb.createFont();
		font.setFontHeightInPoints((short)9);
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		return font;
	}

	public static CellStyle getBaseHeadCellStyle(Workbook wb){
		CellStyle cellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getStructIndexCellStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getNormalFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getStructIndexCellStyle1(Workbook wb) {
		CellStyle cellStyle=wb.createCellStyle();
		cellStyle.setFont(MappingExcelUtils.getBoldFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setFillForegroundColor(new HSSFColor.GREY_50_PERCENT().getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	public static CellStyle getTitleCellStyle(Workbook wb){
		CellStyle cellStyle = getBaseHeadCellStyle(wb);
		cellStyle.setFillForegroundColor(new HSSFColor.GREY_50_PERCENT()
				.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

		return cellStyle;
	}

	public static CellStyle getBodyCellStyle(Workbook wb){
		CellStyle cellStyle = getBaseCellStyle(wb);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	public static CellStyle getArrayCellStyle(Workbook wb){
		CellStyle cellStyleArr=wb.createCellStyle();
		cellStyleArr.setFont(getNormalFont(wb));
		cellStyleArr.setWrapText(true);
		cellStyleArr.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleArr.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleArr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleArr.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
		cellStyleArr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyleArr;
	}

	public static CellStyle getNoMappingCellStyle(Workbook wb){
		CellStyle cellStyleArr=wb.createCellStyle();
		cellStyleArr.setFont(getNormalFont(wb));
		cellStyleArr.setWrapText(true);
		cellStyleArr.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleArr.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleArr.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleArr.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleArr.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		cellStyleArr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyleArr;
	}

	public static CellStyle get_cellStyle(Workbook wb) {
		CellStyle _cellStyle=wb.createCellStyle();
		Font _font=wb.createFont();
		_font.setFontHeightInPoints((short)9);
		_font.setFontName("宋体");
		_font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		//font.setItalic(true);
		//font.setStrikeout(true);
		_cellStyle.setFont(_font);
		_cellStyle.setWrapText(true);
		_cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		_cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return _cellStyle;
	}
	
	public static CellStyle getBodyHeaderCellStyle(Workbook wb){
		CellStyle cellStyle = getBaseHeadCellStyle(wb);
		cellStyle.setFont(getBoldFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}
	
	
	public static CellStyle getBodyHeaderCenterCellStyle(Workbook wb){
		CellStyle cellStyle = getBaseHeadCellStyle(wb);
		cellStyle.setFont(getBoldFont(wb));
		cellStyle.setWrapText(true);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}
	
	public static void fillCell(int rowNo, int colNo,String data, Sheet sheet, CellStyle cellStyle){
		Row row = sheet.getRow(rowNo);
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		fillCell(row, colNo, data, cellStyle);
	}
	
	public static void fillCell(Row row, int colNo,String data, CellStyle cellStyle){
		Cell cell = row.createCell(colNo);
		if (cellStyle != null){
			cell.setCellStyle(cellStyle);
		}
		cell.setCellValue(data);
	}
}
