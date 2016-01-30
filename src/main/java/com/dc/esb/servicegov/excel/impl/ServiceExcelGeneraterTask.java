package com.dc.esb.servicegov.excel.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.excel.support.MappingExcelUtils;
import com.dc.esb.servicegov.service.InterfaceService;
import com.dc.esb.servicegov.service.SystemService;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.vo.MappingExcelIndexVo;
import com.dc.esb.servicegov.vo.RelationVO;
import com.dc.esb.servicegov.vo.SDAVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.exception.DataException;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class ServiceExcelGeneraterTask implements ExcelGenerateTask {

private static final Log log = LogFactory.getLog(ServiceExcelGeneraterTask.class);
	
	private ServiceServiceImpl serviceServiceImpl;
	private SystemServiceImpl systemServiceImpl;
	private InterfaceServiceImpl interfaceServiceImpl;
	private OperationServiceImpl operationServiceImpl;
	private MetadataServiceImpl metadataServiceImpl;

	private RelationVO r;
	private CountDownLatch countDown;
	//记录"输出"所在的行索引
	private int outputIndex=0;

	//记录"输入"所在的行索引
	private int inputIndex=0;

	//记录最后一行的行索引
	private int lastIndex=0;
	private List<Map<String, SDA>> lstStructName = new ArrayList<Map<String, SDA>>();
	// 全局的行
	int i = 6;
	// 当前操作SHEET
	private Sheet sheet2;
	// 全局WORKBOOK
	private Workbook wb;

	Hyperlink href = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);

	private CellStyle arrayCellStyle;
	private CellStyle cellStyleLightYellow;
	private CellStyle cellStyleLightGreen;
	private CellStyle cellStyleMaroon;

	private CellStyle titleCellStyle;
	private CellStyle bodyCellStyle;

	private CellStyle headCellStyle;
	private CellStyle cellStyle;

	public ServiceExcelGeneraterTask(){

	}

	public ServiceExcelGeneraterTask(Workbook wb,Sheet sheet,CountDownLatch countDown){
		this.r = r;
		this.wb = wb;
		this.countDown = countDown;
		this.sheet2 = sheet;
	}

	public void setStyle1(CellStyle s1,CellStyle s2,CellStyle s3,CellStyle s4){
		this.arrayCellStyle = s1;
		this.cellStyleLightYellow = s2;
		this.cellStyleLightGreen = s3;
		this.cellStyleMaroon = s4;
	}

	public void setStyle2(CellStyle s1,CellStyle s2,CellStyle s3,CellStyle s4){
		this.titleCellStyle = s1;
		this.bodyCellStyle = s2;
	}

	public void setStyle3(CellStyle s1,CellStyle s2,CellStyle s3,CellStyle s4){
		this.headCellStyle = s1;
		this.cellStyle = s3;
	}

	@Override
	public void run() {
		log.info("进入SERVICE TASK");
		try {
			long start = java.lang.System.currentTimeMillis();
			i = 6;
			reset();
			List<Service> lstService = serviceServiceImpl.findBy("serviceId", r
					.getServiceId()); // InstInfoDtlQry S120030021
			SDAVO sda = null;
			if (lstService.size() == 0)
				return;
			sda = serviceServiceImpl.getSDAofRelation(r);
			log.info("得到SDA");
			String interfaceId = r.getInterfaceId();
			// 有问题的服务 待处理
			if (interfaceId.equals("FB52")) {
				countDown.countDown();
				return;
			}

			MappingExcelIndexVo mVo = createMappingExcelIndexVo(r);
			printIndexInfo(mVo);

			// sheet2 = synCreateSheet(wb, interfaceId);

			printHeader(r, sheet2, mVo);

			renderSDA(sda);

			setSheet2TextFont(sheet2);
			long end = java.lang.System.currentTimeMillis();
			log.error("生成单个sheet:[" + sheet2.getSheetName() + "]耗时:"
					+ (end - start) + "ms");
			countDown.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("SERVICE TASK结束!");
	}

	/**
	 * 设置sheet2(当前操作SHEET)文本字体和颜色 合并单元格
	 */
	private void setSheet2TextFont(Sheet sheet){

		Iterator<Row> iterator=sheet.iterator();
		//处理其他行
		Row row = null;
		Hyperlink href = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
		href.setAddress("#INDEX!A1");
		MappingExcelUtils.fillCell(0, 10, "返回", sheet, cellStyle);
		sheet.getRow(0).getCell(10).setHyperlink(href);
		while (iterator.hasNext()) {
			row = iterator.next();
			row.setHeightInPoints((short) 20);
		}
		for(int i=0;i<=4;i++){
		    sheet.getRow(4).getCell(i).setCellStyle(cellStyleLightYellow);
		    sheet.getRow(5).getCell(i).setCellStyle(cellStyleLightYellow);
		}
		for(int i=6;i<=9;i++){
			sheet.getRow(4).getCell(i).setCellStyle(cellStyleLightYellow);
			sheet.getRow(5).getCell(i).setCellStyle(cellStyleLightYellow);
		}
		lastIndex = i-1;
		for(int i=0;i<=lastIndex;i++){
			if(i!=inputIndex&&i!=outputIndex){
			sheet.getRow(i).getCell(5).setCellStyle(cellStyleMaroon);
			}
		}

		for(int i=0;i<10;i++){
			sheet.getRow(inputIndex).getCell(i).setCellStyle(cellStyleLightGreen);
			sheet.getRow(outputIndex).getCell(i).setCellStyle(cellStyleLightGreen);
		}

		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,4));
		sheet.addMergedRegion(new CellRangeAddress(0,0,7,9));
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,4));
		sheet.addMergedRegion(new CellRangeAddress(1,1,7,9));
		sheet.addMergedRegion(new CellRangeAddress(2,2,1,4));
		sheet.addMergedRegion(new CellRangeAddress(2,2,7,9));
		sheet.addMergedRegion(new CellRangeAddress(3,3,1,4));
		sheet.addMergedRegion(new CellRangeAddress(3,3,7,9));
		sheet.addMergedRegion(new CellRangeAddress(4,4,0,4));
		sheet.addMergedRegion(new CellRangeAddress(4,4,6,9));
		sheet.addMergedRegion(new CellRangeAddress(0,5,5,5));
		if(outputIndex>7){
			sheet.addMergedRegion(new CellRangeAddress(7,outputIndex-1,5,5));
		}
		if (outputIndex+1 <= i-1)
			sheet.addMergedRegion(new CellRangeAddress(outputIndex+1,i-1,5,5));

		// 设置列宽度
		sheet.setColumnWidth(0, 15*256);
		sheet.setColumnWidth(1, 18*256);
		sheet.setColumnWidth(2, 16*256);
		sheet.setColumnWidth(3, 14*256);
		sheet.setColumnWidth(4, 17*256);
		sheet.setColumnWidth(5, 2*256);
		sheet.setColumnWidth(6, 14*256);
		sheet.setColumnWidth(7, 16*256);
		sheet.setColumnWidth(8, 17*256);
		sheet.setColumnWidth(9, 18*256);
	}

	/**
	 * reset 全局变量
	 */
	private void reset() {
		this.inputIndex = 0;
		this.outputIndex = 0;
		this.lstStructName = new ArrayList<Map<String, SDA>>();
		this.lastIndex = 0;
	}

	/**
	 * 生成 MappingExcelIndexVo
	 * @param r
	 * @return
	 */
	private MappingExcelIndexVo createMappingExcelIndexVo(
			RelationVO r) {
		MappingExcelIndexVo mVo = new MappingExcelIndexVo();
		String interfaceId = r.getInterfaceId();
		String operationId = r.getOperationId();
		String serviceId = r.getServiceId();
		String consumerSysAB = r.getConsumerSystemAb();
		String providerId = r.getProviderSystemId();
		System providerSys = systemServiceImpl.findUniqueBy("systemId", providerId);
		String providerSysAB = providerSys.getSystemAb();
		Interface interfaceInfo = new Interface();
		// Relation表中有 接口表中没有interfaces.get(0)会有NullPointer
		interfaceInfo =interfaceServiceImpl.findUniqueBy("interfaceId", interfaceId);
		Service service = serviceServiceImpl.findUniqueBy("serviceId", serviceId);
		Operation operation = operationServiceImpl.findUniqueBy("operationId", operationId);
		mVo.setInterfaceId(interfaceId);
		mVo.setInterfaceName(interfaceInfo.getInterfaceName());
		mVo.setServiceId(serviceId);
		mVo.setProviderSysId(providerId);
		mVo.setServiceName(service.getServiceName());
		mVo.setServiceRemark(service.getRemark());
		mVo.setOperationRemark(operation.getOperationRemark());
		mVo.setOperationId(operationId);
		mVo.setOperationName(operation.getOperationName());
		mVo.setConsumerSysAb(consumerSysAB);
		mVo.setProviderSysAb(providerSysAB);
		// 根据INTERFACE_ID找到SVC_ASM_RELATE_VIEW中的记录对应消息类型
//		SvcAsmRelateView view = serviceServiceImpl.getRelationViewByInterfaceId(interfaceId);
//		mVo.setType(view.getDirection());
//		mVo.setMsgType(view.getProvideMsgType());
//		mVo.setMsgConvert(r.getMsgConvert());
		return mVo;
	}

	/**
	 * CREATE & PAINT INDEX
	 */
	private  void printIndexInfo(MappingExcelIndexVo mVo) {
		synchronized(getClass()){
			Sheet indexSheet = wb.getSheet("INDEX");
			if (null == indexSheet) {
				indexSheet = wb.createSheet("INDEX");
				printSheetIndexLabel(indexSheet);
			}
			if (mVo.getMsgConvert().size() > 1) {
				for (int i=0;i<mVo.getMsgConvert().size();i++) {
					printSheetIndexData(i, mVo, indexSheet);
				}
			} else {
				printSheetIndexData(mVo, indexSheet);
			}
		}
	}
	/**
	 * 设置INDEX页的标题行
	 * @param indexSheet
	 */
	private void printSheetIndexLabel(Sheet indexSheet) {
		// 处理Index页
		Row titleRow = indexSheet.createRow(0);
		titleRow.setHeightInPoints((short) 22.5);
		MappingExcelUtils.fillCell(titleRow, 0, "交易代码", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 1, "交易名称", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 2, "服务名称", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 3, "服务操作ID", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 4, "服务操作名称", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 5, "调用方", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 6, "服务操作提供系统", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 7, "接口方向", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 8, "接口提供系统ID", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 9, "报文类型", titleCellStyle);
		MappingExcelUtils.fillCell(titleRow, 10, "报文转换方向", titleCellStyle);
		indexSheet.setColumnWidth(0, 23*256);
		indexSheet.setColumnWidth(1, 23*256);
		indexSheet.setColumnWidth(2, 23*256);
		indexSheet.setColumnWidth(3, 23*256);
		indexSheet.setColumnWidth(4, 23*256);
		indexSheet.setColumnWidth(6, 22*256);
		indexSheet.setColumnWidth(9, 22*256);
		indexSheet.setColumnWidth(10, 22*256);
	}

	/**
	 * INDEX页追加一条数据
	 * @param mVo
	 * @param indexSheet
	 */
	private void printSheetIndexData(MappingExcelIndexVo mVo, Sheet indexSheet) {
		int lastRow = indexSheet.getLastRowNum();
		Row rowAdded = indexSheet.createRow(lastRow + 1);
		rowAdded.setHeightInPoints((short) 22.5);
		MappingExcelUtils.fillCell(rowAdded, 0, mVo.getInterfaceId(), bodyCellStyle);
		href.setAddress("#" + mVo.getInterfaceId() + "!A1");
		rowAdded.getCell(0).setHyperlink(href);
		MappingExcelUtils.fillCell(rowAdded, 1, mVo.getInterfaceName(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 2, mVo.getServiceName() + "(" + mVo.getServiceId()
				+ ")", bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 3, mVo.getOperationId(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 4, mVo.getOperationName(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 5, mVo.getConsumerSysAb(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 6, mVo.getProviderSysAb(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 7, "0".equals(mVo.getType()) ? "Consumer"
				: "Provider", bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 8, mVo.getProviderSysId(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 9, mVo.getMsgType(), bodyCellStyle);
//		MappingExcelUtils.fillCell(rowAdded, 10, mVo.getMsgConvert().get(0), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 10, "", bodyCellStyle);
	}

	/**
	 * recurse
	 * @param i
	 * @param mVo
	 * @param indexSheet
	 */
	private void printSheetIndexData(int i, MappingExcelIndexVo mVo, Sheet indexSheet) {
		int lastRow = indexSheet.getLastRowNum();
		Row rowAdded = indexSheet.createRow(lastRow + 1);
		rowAdded.setHeightInPoints((short) 22.5);
		MappingExcelUtils.fillCell(rowAdded, 0, mVo.getInterfaceId(), bodyCellStyle);
		href.setAddress("#" + mVo.getInterfaceId() + "!A1");
		rowAdded.getCell(0).setHyperlink(href);
		MappingExcelUtils.fillCell(rowAdded, 1, mVo.getInterfaceName(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 2, mVo.getServiceName() + "(" + mVo.getServiceId()
				+ ")", bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 3, mVo.getOperationId(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 4, mVo.getOperationName(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 5, mVo.getConsumerSysAb(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 6, mVo.getProviderSysAb(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 7, "0".equals(mVo.getType()) ? "Consumer"
				: "Provider", bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 8, mVo.getProviderSysId(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 9, mVo.getMsgType(), bodyCellStyle);
		MappingExcelUtils.fillCell(rowAdded, 10, mVo.getMsgConvert().get(i), bodyCellStyle);
	}

	/**
	 * PAINT SHEET2 HEADER
	 */
	private void printHeader(RelationVO r,
			Sheet contentSheet, MappingExcelIndexVo mVo) {
		MappingExcelUtils.fillCell(0, 0, "交易码", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(0, 1, mVo.getInterfaceId(), contentSheet, bodyCellStyle);
		MappingExcelUtils.fillCell(0, 6, "服务名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(0, 7, mVo.getServiceName() + "(" + mVo.getServiceId() + ")",
				contentSheet, cellStyle);
		MappingExcelUtils.fillCell(1, 0, "交易名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(1, 1, mVo.getInterfaceName(), contentSheet, cellStyle);
		MappingExcelUtils.fillCell(1, 6, "服务操作名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(1, 7, mVo.getOperationId() + mVo.getOperationName(),
				contentSheet, cellStyle);
		MappingExcelUtils.fillCell(2, 6, "服务描述", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(2, 7, mVo.getServiceRemark(), contentSheet, cellStyle);
		MappingExcelUtils.fillCell(3, 6, "服务操作描述", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(3, 7, mVo.getOperationRemark(), contentSheet, cellStyle);
		MappingExcelUtils.fillCell(4, 0, "原始接口", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(4, 6, "SPDBSD", contentSheet, headCellStyle);

		MappingExcelUtils.fillCell(5, 0, "英文名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 1, "中文名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 2, "数据类型", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 3, "是否必输", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 4, "备注", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 6, "英文名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 7, "数据类型", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 8, "中文名称", contentSheet, headCellStyle);
		MappingExcelUtils.fillCell(5, 9, "备注", contentSheet, headCellStyle);

		// 初始化表头HEADER中的空格子
		for (int i=0;i<6;i++) {
			for (int j=0;j<10;j++) {
				if (contentSheet.getRow(i).getCell(j) == null) {
					MappingExcelUtils.fillCell(i, j, "", contentSheet, cellStyle);
				}
			}
		}
	}

	/**
	 * 递归 SDA
	 * @param sda
	 */
	public void renderSDA(SDAVO sda) {
		SDA node = sda.getValue();

		paintSDANode(node);
		if (sda.getChildNode() != null) {
			List<SDAVO> childSda = sda.getChildNode();
			for (SDAVO a : childSda) {
				renderSDA(a);
			}
		}

		// PAINT ARRAY END
		String alias = "";
		if (node.getType().equalsIgnoreCase("array")) {
			Metadata m = metadataServiceImpl.findUniqueBy("metadataId", node.getMetadataId());
			alias = m==null ? alias : m.getChineseName();
			Row temprow = sheet2.createRow(i);
			i++;
			MappingExcelUtils.fillCell(temprow, 5, "", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 6, node.getMetadataId(), arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 7, "Array", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 8, alias, arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 9, "End", arrayCellStyle);
		} else if (node.getType().equalsIgnoreCase("struct") || (node.getType().equals("") && node.getRemark().equalsIgnoreCase("Start"))) {
			Metadata m = metadataServiceImpl.findUniqueBy("metadataId", node.getMetadataId());
			alias = m==null ? alias : m.getMetadataAlias();
			Row temprow = sheet2.createRow(i);
			i++;
			MappingExcelUtils.fillCell(temprow, 5, "", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 6, node.getMetadataId(), arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 7, "Struct", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 8, alias, arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 9, "End", arrayCellStyle);

		}
	}


	/**
	 *
	 */
	public void paintSDANode(SDA n) {
		Row temprow=sheet2.createRow(i);
		log.info("元数据ID" + n.getMetadataId());
		String alias = "";
		try {
			if (!n.getMetadataId().equals("")) {
//				MetadataStructsAttr attr = serviceServiceImpl.getMetadataAttrById(n.getMetadataId());
				Metadata attr = metadataServiceImpl.findUniqueBy("metadataId", n.getMetadataId());
				if (attr != null) {
					alias = attr.getMetadataAlias();
				}
			}
		} catch (RuntimeException e1) {
			log.info(n.getMetadataId());
		}
		String structName = n.getStructName();
		String type = n.getType();
		String metadataId = n.getMetadataId();
		if (structName.equals("SvcBody")) {
			return ;
		} else if (structName.equals("request")) {
			temprow=sheet2.createRow(i);
			Row row=sheet2.createRow(i);
			row.createCell(0).setCellValue("输入");
			row.createCell(1).setCellValue("");
			row.createCell(2).setCellValue("");
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			row.createCell(6).setCellValue("");
			row.createCell(7).setCellValue("");
			row.createCell(8).setCellValue("");
			row.createCell(9).setCellValue("");
			sheet2.addMergedRegion(new CellRangeAddress(i,i,0,9));
			inputIndex=i;
			//转到下一行
			i++;
			return ;
		} else if (structName.equals("response")) {
			temprow=sheet2.createRow(i);
			Row row=sheet2.createRow(i);
			row.createCell(0).setCellValue("输出");
			row.createCell(1).setCellValue("");
			row.createCell(2).setCellValue("");
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			row.createCell(6).setCellValue("");
			row.createCell(7).setCellValue("");
			row.createCell(8).setCellValue("");
			row.createCell(9).setCellValue("");
			sheet2.addMergedRegion(new CellRangeAddress(i,i,0,9));
			outputIndex=i;
			i++;
			return ;
		}
		if (StringUtils.isNotEmpty(type) && type.equals("array")) {
			try {

				Metadata m = metadataServiceImpl.findUniqueBy("metadataId", n.getMetadataId());
				alias = m==null ? alias : m.getMetadataAlias();
				// 画出Array开始节点
				MappingExcelUtils.fillCell(temprow, 5, "", arrayCellStyle);
				MappingExcelUtils.fillCell(temprow, 6, n.getMetadataId(), arrayCellStyle);
				MappingExcelUtils.fillCell(temprow, 7, "Array", arrayCellStyle);
				MappingExcelUtils.fillCell(temprow, 8, alias, arrayCellStyle);
				MappingExcelUtils.fillCell(temprow, 9, n.getRemark(), arrayCellStyle);
				
				i++;
				return ;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception at paint SDANode, metadataId:" + metadataId);
			}
		} else if (StringUtils.isNotEmpty(type) && type.equalsIgnoreCase("struct")) {
			Map<String, SDA> map = new HashMap<String, SDA>();
			map.put(n.getMetadataId(), n);
			lstStructName.add(map);
			MappingExcelUtils.fillCell(temprow, 5, "", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 6, n.getMetadataId(), arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 7, "Struct", arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 8, alias, arrayCellStyle);
			MappingExcelUtils.fillCell(temprow, 9, n.getRemark(), arrayCellStyle);
			i++;
			return ;
		}
		try {
			// Normal painting
//			String mmid = n.getMetadataId().trim();
			String mmid = n.getMetadataId();
			if ("".equals(mmid))
				return ;
			Metadata metadata = metadataServiceImpl.findUniqueBy("metadataId", n.getMetadataId());
			MappingExcelUtils.fillCell(temprow, 5, "", cellStyle);
			MappingExcelUtils.fillCell(temprow, 6, n.getMetadataId(), cellStyle);
			if (metadata != null) {
				String mm = metadata.getLength()==null?"":"(" + metadata.getLength() + ")";
				MappingExcelUtils.fillCell(temprow, 7, type 
						+ mm, cellStyle);
			}
			MappingExcelUtils.fillCell(temprow, 8, alias, cellStyle);
			MappingExcelUtils.fillCell(temprow, 9, n.getRemark(), cellStyle);
			
			i++;
			
		} catch (DataException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initManager(
			ServiceServiceImpl serviceServiceImpl,
			OperationServiceImpl operationServiceImpl,
			SystemServiceImpl systemServiceImpl,
			InterfaceServiceImpl interfaceServiceImpl,
			MetadataServiceImpl metadataServiceImpl
			) {

		this.serviceServiceImpl = serviceServiceImpl ;
		this.systemServiceImpl = systemServiceImpl;
		this.interfaceServiceImpl = interfaceServiceImpl;
		this.metadataServiceImpl = metadataServiceImpl;
		this.operationServiceImpl = operationServiceImpl;

	}

	@Override
	public void init(
			RelationVO r,
			Workbook wb,
			Sheet sheet,
			CountDownLatch countDown,
			List<Map<String, SDA>> lstStructName) {
		
		this.r = r;
		this.wb = wb;
		this.sheet2 = sheet;
		this.countDown = countDown;
		this.lstStructName = lstStructName;
		
	}

}
