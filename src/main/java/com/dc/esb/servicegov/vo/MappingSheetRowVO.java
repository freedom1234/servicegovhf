package com.dc.esb.servicegov.vo;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.ExcelTool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.UUID;

/**
 * 用于存储接口页签行数据,得到的ida，sda需要处理后再存储到数据库
 */
public class MappingSheetRowVO {
    private int rowNum;
    private Ida ida = null;
    private SDA sda = null;

    public MappingSheetRowVO() {
    }

    public MappingSheetRowVO(MappingSheetIndexVO sheetIndex,Sheet sheet, int rowNum, List<Ida> idaParents ,List<SDA> sdaParents) {
        this.rowNum = rowNum;
        Row row = sheet.getRow(rowNum);
        if(null != row){
            String idaEnName =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaEnNameCol));
            String sdaEnName =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaEnNameCol));
            if(StringUtils.isNotEmpty(idaEnName) || StringUtils.isNotEmpty(sdaEnName)){//如果ida和sda的英文名称都是空则不处理
                String sdaChName =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaChNameCol));
                String sdaTypeAndLength =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaTypeAndLengthCol));
                String sdaConstraint =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaConstrantCol));
                String sdaRequired =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaRequiredCol));
                String sdaRemark =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.sdaRemarkCol));
                sda = new SDA();
                sda.setId(UUID.randomUUID().toString());
                sda.setStructName(sdaEnName);
                sda.setStructAlias(sdaChName);
                sda.setConstraint(sdaConstraint);
                sda.setRequired(sdaRequired);
                sda.setRemark(sdaRemark);
                sda.setType(sdaTypeAndLength);
                sda.setMetadataId(sdaEnName);
                if(null != sdaParents){
                    if(sdaParents.size() > 0){
                        SDA parentSda = sdaParents.get(sdaParents.size() -1);
                        if(null != parentSda){
                            sda.setParentId(parentSda.getId());
                            if(StringUtils.isNotEmpty(parentSda.getXpath()) && StringUtils.isNotEmpty(sdaEnName)){
                                String xpath = parentSda.getXpath() + "/" + sdaEnName;
                                sda.setXpath(xpath);
                            }
                        }
                    }
                }
                String idaChName =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaChNameCol));
                String idaType =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaTypeCol));
                String idaLength =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaLengthCol));
                String idaRequired =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaRequiredCol));
                String idaRemark =  ExcelTool.getInstance().getCellContent(row.getCell(sheetIndex.idaRemarkCol));
                ida = new Ida();
                ida.setStructName(idaEnName);
                ida.setStructAlias(idaChName);
                ida.setType(idaType);
                ida.setLength(idaLength);
                ida.setRequired(idaRequired);
                ida.setRemark(idaRemark);
                ida.setState(Constants.IDA_STATE_COMMON);
                if(StringUtils.isNotEmpty(sda.getMetadataId())){
                    ida.setMetadataId(sdaEnName);
                    ida.setSdaId(sda.getId());
                }
                if(null != idaParents){
                    if(idaParents.size() > 0){
                        Ida parentIda = idaParents.get(idaParents.size() -1);
                        if(null != parentIda){
                            ida.setParentId(parentIda.getId());
                            if(StringUtils.isNotEmpty(parentIda.getXpath()) && StringUtils.isNotEmpty(sdaEnName)){
                                String xpath = parentIda.getXpath() + "/" + sdaEnName;
                                ida.setXpath(xpath);
                            }
                        }
                    }
                }
                if(null != sda && StringUtils.isNotEmpty(sda.getType()) && sda.getType().toLowerCase().contains("array")) {
                    if (StringUtils.isNotEmpty(sda.getRemark()) && sda.getRemark().toLowerCase().startsWith("start")) {//一个新数组加入父节点缓存
                        sdaParents.add(sda);
                        idaParents.add(ida);//sda为数组则对应ida节点一定为数组节点
                    }
                    if (StringUtils.isNotEmpty(sda.getRemark()) && sda.getRemark().toLowerCase().startsWith("end")) {//最后加入的数组最先结束
                        sdaParents.remove(sdaParents.size() - 1);//删除最后一个元素
                        idaParents.remove(idaParents.size() - 1);//删除最后一个元素
                        sda = null;//end行不保存到数据库
                        ida = null;
                    }
                }

            }
        }

    }

    public Ida getIda() {
        return ida;
    }

    public void setIda(Ida ida) {
        this.ida = ida;
    }

    public SDA getSda() {
        return sda;
    }

    public void setSda(SDA sda) {
        this.sda = sda;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
}
