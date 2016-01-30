package com.dc.esb.servicegov.rsimport.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dc.esb.servicegov.entity.EnglishWord;
import com.dc.esb.servicegov.rsimport.IResourceParser;
import com.dc.esb.servicegov.rsimport.support.ExcelUtils;
import com.dc.esb.servicegov.service.impl.EnglishWordServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EnglishWordXlsxParserImpl implements IResourceParser {

	private static final String SHEET_NAME = "表2中文名称及缩写对照表";//"表2中英文名称及缩写对照表";
	private static final int START_ROW_NUM = 1;

	private static final String CHINESE_WORD = "词汇中文名称";
	private static final String ENGLISH_WORD = "词汇英文名称";
	private static final String WORDA = "词汇英文缩写";
	private static final String REMARK = "备注";
	private static final String OPT_DATE = "更新时间";//"修订日期";
	private static final String OPT_USER = "处理人";//"修订人";

	private static int CHINESE_WORD_COLUMN = 0;
	private static int ENGLISH_WORD_COLUMN = 1;
	private static int WORDA_COLUMN = 2;
	private static int OPT_DATE_COLUMN = 3;
	private static int OPT_USER_COLUMN = 4;
	private static int REMARK_COLUMN = 5;
	@Autowired
    private EnglishWordServiceImpl englishWordService;
	@Override
	public void parse(Workbook workbook) {
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		parseSheet(sheet);
	}

	@Transactional
	private void parseSheet(Sheet sheet) {
		initIndexColnum(sheet);
		englishWordService.deleteAll();
		for (int rowNum = START_ROW_NUM; rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row row = sheet.getRow(rowNum);
			EnglishWord englishWord =parseRow(row);
			if(null != englishWord){
				englishWordService.save(englishWord);
			}
		}
		
	}

	private EnglishWord parseRow(Row row) {
		EnglishWord englishWord = new EnglishWord();
		if(StringUtils.isEmpty(ExcelUtils.getValue(row.getCell(ENGLISH_WORD_COLUMN)))){//如果第一列为空，返回null
			return null;
		}
		englishWord.setChineseWord(ExcelUtils.getValue(row.getCell(CHINESE_WORD_COLUMN)));
		englishWord.setEnglishWord(ExcelUtils.getValue(row.getCell(ENGLISH_WORD_COLUMN)));
		englishWord.setWordAb(ExcelUtils.getValue(row.getCell(WORDA_COLUMN)));
		englishWord.setOptDate(ExcelUtils.getValue(row.getCell(OPT_DATE_COLUMN)));
		englishWord.setOptUser(ExcelUtils.getValue(row.getCell(OPT_USER_COLUMN)));
		englishWord.setRemark(ExcelUtils.getValue(row.getCell(REMARK_COLUMN)));
		return englishWord;
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
				if(OPT_DATE.equals(content)){
					OPT_DATE_COLUMN = i;
				}
				if(WORDA.equals(content)){
					WORDA_COLUMN = i;
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
