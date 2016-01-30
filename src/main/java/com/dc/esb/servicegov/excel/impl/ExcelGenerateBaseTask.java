package com.dc.esb.servicegov.excel.impl;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ExcelGenerateBaseTask<T> {

	public void init(List<T> list, Workbook wb, Sheet sheet);
	
	public void createHeadInfo();
	
	public void createBodyInfo();
	
	public void setOtherStyle();

}
