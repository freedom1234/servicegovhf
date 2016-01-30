package com.dc.esb.servicegov.rsimport.impl;


import com.dc.esb.servicegov.service.impl.LogInfoServiceImpl;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.hibernate.NonUniqueObjectException;
import org.jboss.seam.annotations.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dc.esb.servicegov.entity.CategoryWord;

import com.dc.esb.servicegov.rsimport.IResourceParser;
import com.dc.esb.servicegov.rsimport.support.ExcelUtils;
import com.dc.esb.servicegov.service.impl.CategoryWordServiceImpl;

import java.util.Date;
import java.util.List;

@Component
public class CategoryWordParserImpl implements IResourceParser {

	private static final Log log = LogFactory.getLog(CategoryWordParserImpl.class);
	@Autowired
	LogInfoServiceImpl logInfoService;

	private static final String SHEET_NAME = "表3类别词";
	private static final int START_ROW_NUM = 1;

	private static final String CHINESE_WORD= "类别词\n中文名称";//"类别词";
	private static final String ENGLISH_WORD = "类别词\n英文全称";//"类别词英文全称";
	private static final String ESGLISGA = "类别词\n英文缩写";//"类别词英文缩写"
	private static final String REMARK = "备注";

	private static int CHINESE_WORD_COLUMN = 0;
	private static int ENGLISH_WORD_COLUMN = 1;
	private static int ESGLISGA_COLUMN = 2;
	private static int REMARK_COLUMN = 3;

    @Autowired
    private CategoryWordServiceImpl categoryWordService;
	@Override
	public void parse(Workbook workbook) {
		Sheet sheet = workbook.getSheet(SHEET_NAME);//根据页签名找到对应的表
		parseSheet(sheet);
	}

	@Transactional
	private void parseSheet(Sheet sheet) {
		initIndexColnum(sheet);
//		categoryWordService.deleteAll();
		for (int rowNum = START_ROW_NUM; rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row row = sheet.getRow(rowNum);
			CategoryWord categoryWord =parseRow(row, rowNum);
			if(null == categoryWord) continue;
			//增加导入修订人和时间
			String userName = (String) SecurityUtils.getSubject().getPrincipal();
			categoryWord.setOptUser(userName);
			categoryWord.setOptDate(DateUtils.format(new Date()));
			//判断是否重复

			CategoryWord categoryWord1 = categoryWordService.findUniqueBy("esglisgAb", categoryWord.getEsglisgAb());
			if(null != categoryWord1){
				categoryWord1.setRemark(categoryWord.getRemark());
				categoryWord1.setChineseWord(categoryWord.getChineseWord());
				categoryWord1.setEnglishWord(categoryWord.getEnglishWord());
				categoryWord1.setOptDate(DateUtils.format(new Date()));
				userName = (String) SecurityUtils.getSubject().getPrincipal();
				categoryWord1.setOptUser(userName);
				//重复则覆盖
				categoryWordService.save(categoryWord1);
				continue;
			}
			try{
				log.info("开始导入类别词["+categoryWord.getEsglisgAb()+"]");
				categoryWordService.save(categoryWord);
			}catch(NonUniqueObjectException e){
				log.error("类别词[" + categoryWord.getId() + "]重复,执行覆盖！", e);
				logInfoService.saveLog("第" + (rowNum+1) + "行类别词[" + categoryWord.getId() + "]重复,执行覆盖！", "表3类别词");
				CategoryWord categoryWordToDel = categoryWordService.getById(categoryWord.getId());
				categoryWordService.delete(categoryWordToDel);
				categoryWordService.save(categoryWord);
			}
		}
	}
	
	private CategoryWord parseRow(Row row, int rowNum) {//将对应列的数据插入list
		try {
			CategoryWord categoryWord = new CategoryWord();
			categoryWord.setChineseWord(ExcelUtils.getValue(row.getCell(CHINESE_WORD_COLUMN)));
			categoryWord.setEnglishWord(ExcelUtils.getValue(row.getCell(ENGLISH_WORD_COLUMN)));
			categoryWord.setEsglisgAb(ExcelUtils.getValue(row.getCell(ESGLISGA_COLUMN)));
			categoryWord.setRemark(ExcelUtils.getValue(row.getCell(REMARK_COLUMN)));
			return categoryWord;
		}catch (Exception e){
			log.error(e, e);
			logInfoService.saveLog("第"+(rowNum+1)+"行解析数据失败！", "表3类别词");
		}
		return null;
	}

	/**
	 * 初始化字段序号
	 * @param sheet
	 */
	public void initIndexColnum(Sheet sheet){
		if(sheet != null){
			Row row = sheet.getRow(0);
			for(int i = 0; i < row.getLastCellNum(); i++){//遍历第1行所有单元格
				String content = row.getCell(i).getStringCellValue();

				if(CHINESE_WORD.equals(content)){
					CHINESE_WORD_COLUMN = i;
				}
				if(ENGLISH_WORD.equals(content)){
					ENGLISH_WORD_COLUMN = i;
				}
				if(ESGLISGA.equals(content)){
					ESGLISGA_COLUMN = i;
				}
				if(REMARK.equals(content)){
					REMARK_COLUMN = i;
				}
			}
		}
	}
}
