package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.PdfUtils;
import com.dc.esb.servicegov.vo.OperationPKVO;
import com.dc.esb.servicegov.vo.SDAVO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
@Service
@Transactional
public class PdfServiceImpl {
    protected Log log = LogFactory.getLog(getClass());
    @Autowired
    private ServiceDAOImpl serviceDAO;
    @Autowired
    private OperationDAOImpl operationDAO;
    @Autowired
    private ServiceInvokeDAOImpl serviceInvokeDAO;
    @Autowired
    private MetadataDAOImpl metadataDAO;
    @Autowired
    private SDADAOImpl sdadao;
    @Autowired
    private ServiceCategoryDAOImpl serviceCategoryDAO;
    @Autowired
    private ExcelExportServiceImpl excelExportService;
    int maxWidth = 520;

    private static final String serviceType = "service";
    private static final String serviceCategoryType0 = "root";
    private static final String serviceCategoryType1 = "serviceCategory";

    protected Log logger = LogFactory.getLog(getClass());

    public File genderServicePdf(String id, String type) throws Exception{
        File pdfFile = createFdfFile(id);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile))
                .setInitialLeading(16);
        document.open();
        if(serviceCategoryType0.equals(type)){
            genderPdfByServiceCategoryRoot(id, document);
        }
        if(serviceCategoryType1.equals(type)){
            genderPdfByServiceCategory(id, document,"1");
        }
        if(serviceType.equals(type)){
            genderPdfByService(id, document,"1");
        }
        document.close();
        return pdfFile;
    }

    /**
     * 根据传入的operation列表构建pdf
     * @param pkvo
     * @return
     * @throws Exception
     */
    public File genderServicePdf(OperationPKVO pkvo) throws Exception{
        File pdfFile = createFdfFile("operation_" + new Date().getTime());
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile))
                .setInitialLeading(16);
        document.open();

        Map<com.dc.esb.servicegov.entity.Service, List<Operation>> map = new HashMap<com.dc.esb.servicegov.entity.Service, List<Operation>>();
        for(OperationPK pk : pkvo.getPks()){
            Operation operation = operationDAO.getBySO(pk.getServiceId(), pk.getOperationId());
            List<Operation> operations = map.get(operation.getService());
            if(operations != null){
                operations.add(operation);
            }
            else{
                operations = new ArrayList<Operation>();
                operations.add(operation);
                map.put(operation.getService(), operations);
            }
        }
        int i = 0;
        for(Map.Entry<com.dc.esb.servicegov.entity.Service, List<Operation>> entry:map.entrySet()){
            com.dc.esb.servicegov.entity.Service service = entry.getKey();
            printServiceTitle(service, document, String.valueOf(++i));
            List<Operation> operations = entry.getValue();
            printOperationInfo(operations, document);
        }
        document.close();
        return pdfFile;
    }
    /**
     * 根节点操作，导出所有
     * @param serviceCategoryId
     * @return
     */
    public void genderPdfByServiceCategoryRoot(String serviceCategoryId, Document document) throws Exception {
        String hql = " from " + ServiceCategory.class.getName() +" where parentId is null order by categoryId asc";
        List<ServiceCategory> children = serviceCategoryDAO.find(hql);
        if(children.size() > 0){
            for(int i = 0; i< children.size(); i++){
                ServiceCategory child = children.get(i);
                genderPdfByServiceCategory(child.getCategoryId(), document, String.valueOf(i+1));
            }
        }
    }

    /**
     * 分类节点操作
     * @param serviceCategoryId
     * @param document
     * @throws Exception
     */
    public void genderPdfByServiceCategory(String serviceCategoryId, Document document, String tab) throws Exception{
        ServiceCategory sc = serviceCategoryDAO.findUniqueBy("categoryId", serviceCategoryId);

        Phrase opDescPhrase = new Phrase(tab + "  "+sc.getCategoryName(), PdfUtils.ST_SONG_BIG_FONT);
        document.add(opDescPhrase);
        document.add(Chunk.NEWLINE);
        String hql = " from " + ServiceCategory.class.getName() +" where parentId = ? order by categoryId asc";
        List<ServiceCategory> children = serviceCategoryDAO.find(hql, serviceCategoryId);
        if(children.size() > 0){
            for(int i = 0; i< children.size(); i++){
                ServiceCategory child = children.get(i);
                genderPdfByServiceCategory(child.getCategoryId(), document, tab+"."+(i+1));
            }
        }
        else{
            String hql2 = " from " + com.dc.esb.servicegov.entity.Service.class.getName() +" where categoryId = ? order by serviceId asc";
            List<com.dc.esb.servicegov.entity.Service> services = serviceDAO.find(hql2, serviceCategoryId);
            if(services.size() > 0){
                for(int i = 0; i < services.size(); i++){
                    com.dc.esb.servicegov.entity.Service service = services.get(i);
                    genderPdfByService(service.getServiceId(), document, tab +"."+ (i+1));
                }
            }
        }
    }

    public void genderPdfByService(String serviceId, Document document, String tab) throws Exception{
        com.dc.esb.servicegov.entity.Service service = serviceDAO.findUniqueBy("serviceId", serviceId);
        printServiceTitle(service, document, tab);
        String hql = " from " + Operation.class.getName() +" where serviceId = ? order by operationId asc";
        List<Operation> operations = operationDAO.find(hql, serviceId);
        printOperationInfo(operations, document);
    }

    /**
     * 打印服务信息
     */
    public void printServiceTitle(com.dc.esb.servicegov.entity.Service service, Document document, String tab) throws Exception{
        Phrase servicePhrase = new Phrase(tab, PdfUtils.ST_SONG_BIG_BOLD_FONT);
        servicePhrase.add("  "+service.getServiceName());
        servicePhrase.add("("+service.getServiceId()+")");
        document.add(servicePhrase);
        document.add(Chunk.NEWLINE);

        Phrase descPhrase = new Phrase("              功能描述："+service.getDesc(), PdfUtils.ST_SONG_MIDDLE_FONT);
        document.add(descPhrase);
        document.add(Chunk.NEWLINE);

        Phrase opPhrase = new Phrase("              本服务有以下场景：", PdfUtils.ST_SONG_MIDDLE_FONT);
        document.add(opPhrase);
        document.add(Chunk.NEWLINE);
    }
    public void printOperationInfo(List<Operation> operations, Document document) throws Exception{
        if (null != operations) {
            int i=0;
            for (Operation operation : operations) {
                try {
                    Phrase opPhrase = new Phrase("              "+operation.getOperationId()+": "+operation.getOperationName(), PdfUtils.ST_SONG_MIDDLE_BOLD_FONT);
                    document.add(opPhrase);
                    document.add(Chunk.NEWLINE);
                    //场景描述
                    Phrase opDescPhrase = new Phrase("              场景描述："+operation.getOperationDesc(), PdfUtils.ST_SONG_MIDDLE_BOLD_FONT);
                    document.add(opDescPhrase);
                    document.add(Chunk.NEWLINE);

                    //服务提供者
                    List<ServiceInvoke> providers = serviceInvokeDAO.getByOperationAndType(operation, Constants.INVOKE_TYPE_PROVIDER);
                    Phrase opConsumerPhrase = new Phrase("              服务提供者："+ joinBy(providers), PdfUtils.ST_SONG_MIDDLE_BOLD_FONT);
                    document.add(opConsumerPhrase);
                    document.add(Chunk.NEWLINE);
                    //服务消费者
                    List<ServiceInvoke> consumers = serviceInvokeDAO.getByOperationAndType(operation, Constants.INVOKE_TYPE_CONSUMER);
                    Phrase opProviderPhrase = new Phrase("              服务消费者："+joinBy(consumers), PdfUtils.ST_SONG_MIDDLE_BOLD_FONT);
                    document.add(opProviderPhrase);
                    document.add(Chunk.NEWLINE);

                    Phrase operationPhrase = new Phrase(operation.getOperationName(), PdfUtils.ST_SONG_BIG_BOLD_FONT);
                    Paragraph operationParagraph = new Paragraph(operationPhrase);
                    Chapter chapter = new Chapter(operationParagraph, i++);
                    render(operation, document, chapter);
                } catch (Exception e) {
                    String errorMsg = "为操作[" + operation.getOperationId() + ":" + operation.getOperationName() + "]创建pdf时失败！";
                    log.error(errorMsg, e);
                    throw e;
                }
            }
        } else {
            String errorMsg = "生成PDF失败，服务为空！";
            log.error(errorMsg);
        }
    }
    public void printOperationTitle(Operation operation, Document document) throws  Exception{

    }
    public File createFdfFile(String fileName) throws Exception{
        File pdfFile = null;
        String pdfDir = "tmppdf";
        File pdfDirFile = new File(pdfDir);
        if (!pdfDirFile.exists()) {
            pdfDirFile.mkdirs();
        }
        String pdfPath = pdfDir + File.separator + "银行服务手册-" + fileName + new Date().getTime()+ ".pdf";
        pdfFile = new File(pdfPath);
        if (pdfFile.exists()) {
            log.error("file path:" + pdfFile.getAbsolutePath());
            boolean deleted = pdfFile.delete();
            if (!deleted) {
                String errorMsg = "删除已经存在的文件[" + pdfPath + "]";
                log.error(errorMsg);
            }
        }
        pdfFile.createNewFile();
        return pdfFile;
    }
    private void render(Operation operation, Document document,Chapter chapter) throws Exception {
        Section operationSection = renderTitle(operation, document, chapter);
        renderSDA(operation, document, operationSection);
    }
    private Section renderTitle(Operation operation, Document document, Chapter chapter) throws Exception {
        String serviceId = operation.getService().getServiceId();
        String serviceName = operation.getService().getServiceName();
        Section operationSection = null;
        try {
            Phrase opDescPhrase = new Phrase();
            Paragraph opDescParagraph = new Paragraph(opDescPhrase);
            operationSection = chapter.addSection(opDescParagraph);
            operationSection.setBookmarkTitle(serviceId + "(" + serviceName + ")");
            operationSection.setBookmarkOpen(false);
            operationSection.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
        } catch (Exception e) {
            String errorMsg = "渲染服务名称[" + serviceId + serviceName + "]失败！";
            log.error(errorMsg, e);
            throw e;
        }
        return operationSection;

    }

    private void renderSDA(Operation operationDO, Document document, Section section) throws Exception {
        try{
            if(operationDO != null){
                PdfPTable table = new PdfPTable(7);
                PdfPCell thCell = new PdfPCell();

                Phrase operationPhrase = new Phrase();
                operationPhrase.add(new Phrase("场景", PdfUtils.ST_SONG_SMALL_BOLD_FONT));
                operationPhrase.add(new Phrase(":" + operationDO.getOperationName(), PdfUtils.TABLE_BOLD_FONT));
                PdfUtils.renderTableHeader(operationPhrase, thCell);
                thCell.setColspan(7);
                table.addCell(thCell);
                PdfPCell index = new PdfPCell();
                PdfUtils.renderChineseTableHeader("序号", index);
                table.addCell(index);
                PdfPCell headerENcell = new PdfPCell();
                PdfUtils.renderChineseTableHeader("字段名称", headerENcell);
                table.addCell(headerENcell);
                PdfPCell headerTypeENcell = new PdfPCell();
                PdfUtils.renderChineseTableHeader("字段类型", headerTypeENcell);
                table.addCell(headerTypeENcell);
                PdfPCell headerCNcell = new PdfPCell(new Phrase());
                PdfUtils.renderChineseTableHeader("字段说明", headerCNcell);
                table.addCell(headerCNcell);
                PdfPCell headerRequired = new PdfPCell();
                PdfUtils.renderChineseTableHeader("是否必输", headerRequired);
                table.addCell(headerRequired);
                PdfPCell headerResist = new PdfPCell();
                PdfUtils.renderChineseTableHeader("约束条件", headerResist);
                table.addCell(headerResist);
                PdfPCell headerRemarkcell = new PdfPCell();
                PdfUtils.renderChineseTableHeader("备注", headerRemarkcell);
                table.addCell(headerRemarkcell);
                PdfPCell headerDirectcell = new PdfPCell();
                PdfUtils.renderChineseTableHeader("输入", headerDirectcell);
                headerDirectcell.setBackgroundColor(Color.PINK);
                headerDirectcell.setColspan(7);
                table.addCell(headerDirectcell);
                Map<String, String> params = new HashMap<String, String>();
                params.put("serviceId", operationDO.getServiceId());
                params.put("operationId", operationDO.getOperationId());
                params.put("structName", Constants.ElementAttributes.REQUEST_NAME);
                SDA reqSDA = sdadao.findUniqureBy(params);
                if (null != reqSDA) {
                    List<SDA> childrenOfReq = sdadao.findBy("parentId", reqSDA.getId());
                    if (null != childrenOfReq) {
                        for (int i = 0; i < childrenOfReq.size(); i++) {
                            int j = i+1;
                            renderSDA("" + j, childrenOfReq.get(i), table, 0, Color.PINK);
                        }
                    }
                }
                PdfPCell headerDirectcell2 = new PdfPCell();
                PdfUtils.renderChineseTableHeader("输出", headerDirectcell2);
                headerDirectcell2.setBackgroundColor(Color.CYAN);
                headerDirectcell2.setColspan(7);
                table.addCell(headerDirectcell2);
                params.put("structName", Constants.ElementAttributes.RESPONSE_NAME);
                SDA rspSDA = sdadao.findUniqureBy(params);
                if (null != rspSDA) {
                    List<SDA> childrenOfRsp = sdadao.findBy("parentId", rspSDA.getId());
                    if (null != childrenOfRsp) {
                        for (int i = 0; i < childrenOfRsp.size(); i++) {
                            int j = i+1;
                            renderSDA("" + j, childrenOfRsp.get(i), table, 0, Color.CYAN);
                        }
                    }
                }
                table.setSpacingBefore(5);
                section.add(table);
                document.add(table);
            }
        }catch (Exception e){
            log.error("渲染服务[" + operationDO.getOperationId() + "]的SDA失败！");
            throw e;
        }
//        try {
//            SDAVO sda = getSDAofService(operationDO);
//            if(sda!=null){
////                PdfPTable table = new PdfPTable(6);
//                PdfPTable table = new PdfPTable(7);
//                PdfPCell thCell = new PdfPCell();
//
//                Phrase operationPhrase = new Phrase();
//                operationPhrase.add(new Phrase("操作", PdfUtils.ST_SONG_SMALL_BOLD_FONT));
//                operationPhrase.add(new Phrase(":" + operationDO.getOperationId(), PdfUtils.TABLE_BOLD_FONT));
//                PdfUtils.renderTableHeader(operationPhrase, thCell);
////                PdfUtils.renderChineseTableHeader("服务", thCell);
//
////                thCell.setColspan(6);
//                thCell.setColspan(7);
//                table.addCell(thCell);
//                PdfPCell index = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("序号", index);
//                table.addCell(index);
//                PdfPCell headerENcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("字段名称", headerENcell);
//                table.addCell(headerENcell);
//                PdfPCell headerTypeENcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("字段类型", headerTypeENcell);
//                table.addCell(headerTypeENcell);
//                PdfPCell headerCNcell = new PdfPCell(new Phrase());
//                PdfUtils.renderChineseTableHeader("字段说明", headerCNcell);
//                table.addCell(headerCNcell);
//                PdfPCell headerRequired = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("是否必输", headerRequired);
//                table.addCell(headerRequired);
//                PdfPCell headerResist = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("约束条件", headerResist);
//                table.addCell(headerResist);
//                PdfPCell headerRemarkcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("备注", headerRemarkcell);
//                table.addCell(headerRemarkcell);
//                List<SDAVO> childrenOfRoot = sda.getChildNode();
//                SDAVO reqSDA = null;
//                SDAVO rspSDA = null;
//                if (null != childrenOfRoot) {
//                    for (SDAVO node : childrenOfRoot) {
//                        if ("request".equalsIgnoreCase(node.getValue().getStructName())) {
//                            reqSDA = node;
//                        }
//                        if ("response".equalsIgnoreCase(node.getValue().getStructName())) {
//                            rspSDA = node;
//                        }
//                    }
//                }
//                PdfPCell headerDirectcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("输入", headerDirectcell);
//                headerDirectcell.setBackgroundColor(Color.PINK);
////                headerDirectcell.setColspan(6);
//                headerDirectcell.setColspan(7);
//                table.addCell(headerDirectcell);
//                if (null != reqSDA) {
//                    List<SDAVO> childrenOfReq = reqSDA.getChildNode();
//                    if (null != childrenOfReq) {
////                        for (SDAVO childOfReq : childrenOfReq) {
////                            renderSDANode(childOfReq, table, 0, Color.PINK);
////                        }
//                        for (int i = 0; i < childrenOfReq.size(); i++) {
//                            int j = i+1;
//                            renderSDANode(""+j,childrenOfReq.get(i), table, 0, Color.PINK);
//                        }
//                    }
//                }
//                PdfPCell headerDirectcell2 = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("输出", headerDirectcell2);
//                headerDirectcell2.setBackgroundColor(Color.CYAN);
////                headerDirectcell2.setColspan(6);
//                headerDirectcell2.setColspan(7);
//                table.addCell(headerDirectcell2);
//                if (null != rspSDA) {
//                    List<SDAVO> childrenOfRsp = rspSDA.getChildNode();
//                    if (null != childrenOfRsp) {
////                        for (SDAVO childSDA : childrenOfRsp) {
////                            renderSDANode(childSDA, table, 0, Color.CYAN);
////                        }
//                        for (int i = 0; i < childrenOfRsp.size(); i++) {
//                            int j = i+1;
//                            renderSDANode(""+j,childrenOfRsp.get(i),table,0,Color.CYAN);
//                        }
//                    }
//                }
//                table.setSpacingBefore(5);
//                section.add(table);
//                document.add(table);
//            }else{
//                PdfPTable table = new PdfPTable(6);
//                PdfPCell thCell = new PdfPCell();
//
//                Phrase operationPhrase = new Phrase();
//                operationPhrase.add(new Phrase("操作", PdfUtils.ST_SONG_SMALL_BOLD_FONT));
//                operationPhrase.add(new Phrase(":" + operationDO.getOperationId(), PdfUtils.TABLE_BOLD_FONT));
//                PdfUtils.renderTableHeader(operationPhrase, thCell);
////                PdfUtils.renderChineseTableHeader("服务", thCell);
//
////                thCell.setColspan(6);
//                thCell.setColspan(7);
//                table.addCell(thCell);
//                PdfPCell headerENcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("字段名称", headerENcell);
//                table.addCell(headerENcell);
//                PdfPCell headerTypeENcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("字段类型", headerTypeENcell);
//                table.addCell(headerTypeENcell);
//                PdfPCell headerCNcell = new PdfPCell(new Phrase());
//                PdfUtils.renderChineseTableHeader("字段说明", headerCNcell);
//                table.addCell(headerENcell);
//                PdfPCell headerRequired = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("是否必输", headerRequired);
//                table.addCell(headerRequired);
//                PdfPCell headerResist = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("约束条件", headerResist);
//                table.addCell(headerResist);
//                PdfPCell headerRemarkcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("备注", headerRemarkcell);
//                table.addCell(headerRemarkcell);
//                PdfPCell headerDirectcell = new PdfPCell();
//                PdfUtils.renderChineseTableHeader("无SDA", headerDirectcell);
//                headerDirectcell.setBackgroundColor(Color.PINK);
////                headerDirectcell.setColspan(6);
//                headerDirectcell.setColspan(7);
//                table.addCell(headerDirectcell);
//                table.setSpacingBefore(5);
//                section.add(table);
//                document.add(table);
//            }
//        } catch (Exception e) {
//            log.error("渲染服务[" + operationDO.getOperationId() + "]的SDA失败！");
//            throw e;
//        }
    }
    private void renderSDA(String index,SDA sda, PdfPTable table, int offset, Color indexColor) throws Exception {
        String sdaNodeId = sda.getStructName();
        String sdaNodeType = "";
        String sdaNodeChineseName = sda.getStructAlias();
        String sdaNodeRequired = sda.getRequired();
        String sdaNodeResist = sda.getConstraint();
        String sdaNodeRemark = sda.getRemark();

        PdfPCell idCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeId, idCell);
        idCell.setIndent(offset);

        PdfPCell indexCell = new PdfPCell();
        PdfUtils.renderLatinTableData(index, indexCell);

        if (null != indexColor) {
//            idCell.setBackgroundColor(indexColor);
            indexCell.setBackgroundColor(indexColor);
        }

        PdfPCell typeCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeType, typeCell);

        PdfPCell cnCell = new PdfPCell();
        PdfUtils.renderChineseTableData(sdaNodeChineseName, cnCell);

        PdfPCell requiredCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeRequired, requiredCell);

        PdfPCell resistCell = new PdfPCell();
        PdfUtils.renderChineseTableData(sdaNodeResist, resistCell);

        PdfPCell remarkCell = new PdfPCell();

        List<SDA> childSDAs = sdadao.findBy("parentId", sda.getId());
        if (null != childSDAs && childSDAs.size() > 0) {
            idCell.setBackgroundColor(Color.yellow);
            PdfUtils.renderChineseTableData(sda.getRemark(), remarkCell);
        } else {
            PdfUtils.renderChineseTableData(sdaNodeRemark, remarkCell);
        }

        table.addCell(indexCell);
        table.addCell(idCell);
        table.addCell(typeCell);
        table.addCell(cnCell);
        table.addCell(requiredCell);
        table.addCell(resistCell);
        table.addCell(remarkCell);

        if (null != childSDAs && childSDAs.size() > 0) {
            int childOffSet = offset + 10;
            for (int i = 0; i < childSDAs.size(); i++) {
                int j = i + 1;
                renderSDA("" + index + "." + j, childSDAs.get(i), table, childOffSet, indexColor);
            }
            SDA endSDA = new SDA();
            endSDA.setId(UUID.randomUUID().toString());
            endSDA.setStructName(sda.getStructName());
            endSDA.setType(sda.getType());
            endSDA.setConstraint(sda.getConstraint());
            endSDA.setRequired(sda.getRequired());
            endSDA.setId(UUID.randomUUID().toString());
            endSDA.setRemark("end");
            renderSDA("" + index, endSDA, table, offset, Color.yellow);
        }
    }
//    private void renderSDANode(SDAVO sda, PdfPTable table, int offset, Color indexColor) throws Exception {
    //增加序号
    private void renderSDANode(String index,SDAVO sda, PdfPTable table, int offset, Color indexColor) throws Exception {
        String sdaNodeId = sda.getValue().getStructName();
        String sdaNodeType = "";
        String sdaNodeChineseName = sda.getValue().getStructAlias();
        String sdaNodeRequired = sda.getValue().getRequired();
        String sdaNodeResist = sda.getValue().getConstraint();
        String sdaNodeRemark = sda.getValue().getRemark();

        String metadataId = sda.getValue().getMetadataId();
        if (null != metadataId && !"".equals(metadataId)) {
            Metadata metadata = metadataDAO.findUniqueBy("metadataId", metadataId);
            String type = "";
            String length = "";
            String scale = "";
            if(null!=metadata){
                type = metadata.getType();
                length = metadata.getLength();
                scale = metadata.getScale();
            }else{
                sdaNodeType = "该元数据不存在";
            }
            if (null != type) {
                sdaNodeType = type;
            }
            String typeAndScale = null;
            if (null != length) {
                typeAndScale = length;
            }
            if (null != scale) {
                typeAndScale = typeAndScale + "," + scale;
            }
            if (null != typeAndScale) {
                sdaNodeType = sdaNodeType + "(" + typeAndScale + ")";
            }
        }
        PdfPCell idCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeId, idCell);
        idCell.setIndent(offset);

        PdfPCell indexCell = new PdfPCell();
        PdfUtils.renderLatinTableData(index, indexCell);

        if (null != indexColor) {
//            idCell.setBackgroundColor(indexColor);
            indexCell.setBackgroundColor(indexColor);
        }

        PdfPCell typeCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeType, typeCell);

        PdfPCell cnCell = new PdfPCell();
        PdfUtils.renderChineseTableData(sdaNodeChineseName, cnCell);

        PdfPCell requiredCell = new PdfPCell();
        PdfUtils.renderLatinTableData(sdaNodeRequired, requiredCell);

        PdfPCell resistCell = new PdfPCell();
        PdfUtils.renderChineseTableData(sdaNodeResist, resistCell);

        PdfPCell remarkCell = new PdfPCell();

        List<SDAVO> childSDAs = sda.getChildNode();
        if (null != childSDAs && childSDAs.size() > 0) {
            idCell.setBackgroundColor(Color.yellow);
//          o
            PdfUtils.renderChineseTableData(sdaNodeRemark, remarkCell);
        }else{
            PdfUtils.renderChineseTableData(sdaNodeRemark, remarkCell);
        }

        table.addCell(indexCell);
        table.addCell(idCell);
        table.addCell(typeCell);
        table.addCell(cnCell);
        table.addCell(requiredCell);
        table.addCell(resistCell);
        table.addCell(remarkCell);

        if (null != childSDAs && childSDAs.size() > 0) {
            int childOffSet = offset + 10;
//            for (SDAVO childSDA : childSDAs) {
//                renderSDANode(childSDA, table, childOffSet, indexColor);
//            }
            for (int i = 0; i < childSDAs.size(); i++) {
                int j = i+1;
                renderSDANode(""+index +"."+j ,childSDAs.get(i), table, childOffSet, indexColor);
            }
            SDAVO endVO = new SDAVO();
            SDA voValue = sda.getValue();
            voValue.setRemark("end");
            endVO.setValue(voValue);
            renderSDANode(""+index,endVO, table, offset, Color.yellow);
        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  }

    public SDAVO getSDAofService(Operation operation){
        SDA sda = sdadao.findRootByOperation(operation);
        if(sda != null){
            SDAVO sdavo = genderSDAVO(sda);
            return sdavo;
        }
        return null;
    }
    public SDAVO genderSDAVO(SDA sda){
        SDAVO sdavo = new SDAVO();
        sdavo.setValue(sda);
        List<SDAVO> children = new ArrayList<SDAVO>();
        List<SDA> sdaList = sdadao.findBy("parentId", sda.getId());
        for(int i = 0; i< sdaList.size(); i++){
            SDAVO child = genderSDAVO(sdaList.get(i));
            children.add(child);
        }
        sdavo.setChildNode(children);
        return sdavo;
    }

    public String joinBy(List<ServiceInvoke> serviceInvokes){
        String consumer = "";
        List<String> temp = new ArrayList<String>();
        for(int i = 0; i < serviceInvokes.size(); i++){
            ServiceInvoke si = serviceInvokes.get(i);
            if(si.getSystem() != null){
                if(!temp.contains(si.getSystem().getSystemId())){
                    temp.add(si.getSystem().getSystemId());
                    if(i != 0){
                        consumer += ", ";
                    }
                    consumer += si.getSystem().getSystemChineseName();
                }

            }
        }
        return consumer;
    }
}
