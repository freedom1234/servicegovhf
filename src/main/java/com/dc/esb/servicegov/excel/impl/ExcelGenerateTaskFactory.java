package com.dc.esb.servicegov.excel.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.dc.esb.servicegov.excel.support.Constants.*;

public class ExcelGenerateTaskFactory  {
	
	private static final Log log = LogFactory.getLog(ExcelGenerateTaskFactory.class);
	
	private static ExcelGenerateTaskFactory instance = new ExcelGenerateTaskFactory();
	
	private ExcelGenerateTaskFactory(){
		
	}
	
	public static ExcelGenerateTaskFactory getInstance(){
		return instance;
	}
	
	public ExcelGenerateTask factory(String type) throws Exception {
		if(MAPPING_FILE_TYPE.equals(type)){
//			return new MappingGeneraterTask();
		}else if(SERVICE_FILE_TYPE.equals(type)){
			return new ServiceExcelGeneraterTask();
		}else if(INTERFACE_FILE_TYPE.equals(type)){
//			return new InterfaceGeneaterTask();
		}else{
			String errorMsg = "暂时不支持类型为["+type+"]的文档导出！";
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
		return null;
	}

}
