package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
//@Component("cBSConfigExportGender2")
public class CBSConfigExportGender2 extends ConfigExportGenerator{
    private Log log = LogFactory.getLog(CBSConfigExportGender2.class);
    /**
     * 生成系统请求文件
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void  genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path){
        try {
            String serviceId = serviceInvoke.getServiceId();
            String operationId = serviceInvoke.getOperationId();
            Operation operation = operationService.getOperation(serviceId, operationId);
            com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
//            String fileName = path + File.separator + "channel_" + system.getSystemAb().substring(2).toLowerCase()+ "_channel_service_" + serviceId + operationId + ".xml";
            String fileName;
            if (path.indexOf("in_config") > 0) {
//                fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_system_service_" + serviceId + operationId + ".xml";
                fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_channel_service_" + serviceId + operationId + ".xml";
            } else {
//                fileName = path + File.separator + "service_" + serviceId + operationId + "_system_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel.xml";
                fileName = path + File.separator + "service_" + serviceId + operationId + "_system_b_" + system.getSystemAb().toLowerCase().substring(2) + "_system.xml";
            }
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                fileName=fileName.substring(0,fileName.length()-4)+serviceInvoke.getInterfaceId()+".xml";
                file = new File(fileName);
            }

            Document doc = DocumentHelper.createDocument();
            Element serviceElement = doc.addElement("service");//根节点
            addAttribute(serviceElement, "package_type", "kv");
            addAttribute(serviceElement, "store-mode", "GB18030");
//            fillPackageParserServiceHead(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.REQUEST_NAME, false);

//            //手动写报文头 request端
//            Element sys_head_req = serviceElement.addElement("SYS_HEAD");
//            Element SvcId=sys_head_req.addElement("SvcId");
//            SvcId.addAttribute("metadataid","SvcId");

            serviceElement.addElement("MSGNM").addAttribute("metadataid","MSGNM").addAttribute("ftype","fix")
                    .addAttribute("length","6").addAttribute("expression","'000000'");
            serviceElement.addElement("CnsmSysId").addAttribute("metadataid","CnsmSysId").addAttribute("ftype", "fix")
                    .addAttribute("length", "2");
            serviceElement.addElement("STATENM").addAttribute("metadataid", "STATENM").addAttribute("ftype","fix")
                    .addAttribute("length", "2").addAttribute("expression", "'00'");
            serviceElement.addElement("TranCode").addAttribute("metadataid","TranCode").addAttribute("ftype", "fix")
                    .addAttribute("length", "10");
            serviceElement.addElement("TellerId").addAttribute("metadataid","TellerId").addAttribute("ftype","fix")
                    .addAttribute("length","8");
            serviceElement.addElement("SessionId").addAttribute("metadataid", "SessionId").addAttribute("ftype","fix")
                    .addAttribute("length", "20");
            serviceElement.addElement("RESER").addAttribute("metadataid", "RESER").addAttribute("ftype","fix")
                    .addAttribute("length", "10");
            serviceElement.addElement("mdtrdt").addAttribute("metadataid", "TranDate");
            serviceElement.addElement("mdtrsq").addAttribute("metadataid","CnsmSysSeqNo");
            serviceElement.addElement("systcd").addAttribute("metadataid","CnsmSysId");
            serviceElement.addElement("prcscd").addAttribute("metadataid","TranCode");
            serviceElement.addElement("servtp").addAttribute("metadataid","ChnlType");
            serviceElement.addElement("cityno").addAttribute("metadataid","SubBnkId");
            serviceElement.addElement("brchno").addAttribute("metadataid","BranchId");
            serviceElement.addElement("userid").addAttribute("metadataid","TellerId");

            fillPackageParserBody(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.REQUEST_NAME, false);
            try {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("utf-8");
                FileOutputStream fos = new FileOutputStream(fileName);
                XMLWriter writer = new XMLWriter(fos, format);
                writer.write(doc);
                writer.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }catch (Exception e){
            log.error("生成请求文件失败！", e);
        }
    }

    /**
     * 生成esb响应文件
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void  genrateServiceSystemFile(ServiceInvoke serviceInvoke, String path){
        try {
            String serviceId = serviceInvoke.getServiceId();
            String operationId = serviceInvoke.getOperationId();
            Operation operation = operationService.getOperation(serviceId, operationId);
            com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
            String fileName;
//            String fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().substring(2).toLowerCase() + "_channel.xml";
            if (path.indexOf("in_config") > 0) {
//                fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_system.xml";
                fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_channel.xml";
            } else {
//                fileName = path + File.separator + "channel_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel_service_" + serviceId + operationId + ".xml";
                fileName = path + File.separator + "channel_b_" + system.getSystemAb().toLowerCase().substring(2) + "_system_service_" + serviceId + operationId + ".xml";
            }
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                fileName=fileName.substring(0,fileName.length()-4)+serviceInvoke.getInterfaceId()+".xml";
                file = new File(fileName);
            }

            Document doc = DocumentHelper.createDocument();
            Element serviceElement = doc.addElement("service");//根节点
            addAttribute(serviceElement, "package_type", "kv");
            addAttribute(serviceElement, "store-mode", "GB18030");
//            fillPackageParserServiceHead(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.RESPONSE_NAME, false);

            serviceElement.addElement("erorcd").addAttribute("length", "50").addAttribute("type","string")
                    .addAttribute("metadataid", "RetCode").addAttribute("expression", "fp:core_to_ret_code(${/sdoroot/SYS_HEAD/array/RetInf/sdo/RetCode})");
            serviceElement.addElement("erortx").addAttribute("length", "256").addAttribute("type", "string")
                    .addAttribute("metadataid", "RetMsg");
            serviceElement.addElement("mdtrsq").addAttribute("length","50").addAttribute("type","string")
                    .addAttribute("metadataid","CnsmSysSeqNo");
            serviceElement.addElement("systcd").addAttribute("length","50").addAttribute("type","string")
                    .addAttribute("metadataid","CnsmSysId");
            serviceElement.addElement("trandt").addAttribute("length","50").addAttribute("type","string")
                    .addAttribute("metadataid","TranDate");
            serviceElement.addElement("tranti").addAttribute("length","9").addAttribute("metadataid","TranTime")
                    .addAttribute("expression","fp:TranTime(${/sdoroot/SYS_HEAD/TranTime})");
            serviceElement.addElement("transq").addAttribute("length","50").addAttribute("type","string")
                    .addAttribute("metadataid","PrvdSysSeqNo");
            serviceElement.addElement("mdtrdt").addAttribute("length","8").addAttribute("type","string")
                    .addAttribute("metadataid","CnsmSysDt");

            fillPackageParserBody(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.RESPONSE_NAME, false);
            try {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("utf-8");
                FileOutputStream fos = new FileOutputStream(fileName);
                XMLWriter writer = new XMLWriter(fos, format);
                writer.write(doc);
                writer.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }catch (Exception e){
            log.error("生成响应文件失败！", e);
        }
    }

    public void fillPackageParserServiceHead(Operation operation, String interfaceId, Element targetElement, String targetName, boolean arrayFlag){
        String[] headIds = operation.getHeadId().split("\\,");//服务报文头
        for(String headId : headIds){
            Element headElement = targetElement.addElement(headId.toUpperCase());//添加服务报文头标签 例：<SYS_HEAD> <APP_HEAD>
            SDA reServiceHeadSDA = sdaService.getByStructName(headId, targetName);
            List<SDA> sdas = sdaService.getServiceHeadRequired(headId, reServiceHeadSDA.getId());//服务报文头必输SDA
            SDA reOperationSDA = sdaService.getByStructName(operation.getServiceId(), operation.getOperationId(), targetName);
            List<SDA> operationHeadSDAs = sdaService.getOperationHeadSDAs(operation.getServiceId(), operation.getOperationId(), headId, reOperationSDA.getId());//场景sda中约束条件为相应报文头的元素
//            sdas.addAll(operationHeadSDAs);
            addListContent(sdas, operationHeadSDAs);
            if(StringUtils.isNotEmpty(interfaceId)){//查询接口报文头中对应约束元素syshead，apphead的加入对应头标签，其他的加入body标签
                InterfaceHeadRelate relate =interfaceHeadRelateService.findUniqueBy("interfaceId", interfaceId);
                if(null != relate){
                    List<SDA> interfaceheadSDAs = sdaService.getByInterfaceHeadSDAs(relate.getHeadId(), targetName, headId );
                    addListContent(sdas, interfaceheadSDAs);
                }
            }
            fillPackageParserElement(headElement, sdas);
        }
    }

    /**
     * 填充body元素
     * @param operation
     * @param targetElement
     */
    public void fillPackageParserBody(Operation operation, String interfaceId, Element targetElement, String structName, boolean arrayFlag){
        Element bodyElement = targetElement.addElement("BODY");
        SDA reSDA = sdaService.getByStructName(operation.getServiceId(), operation.getOperationId(), structName);
        List<SDA> sdas = sdaService.getChildExceptServiceHead(reSDA.getId(), operation.getHeadId());
        if(StringUtils.isNotEmpty(interfaceId)){//查询接口报文头中对应约束元素syshead，apphead的加入对应头标签，其他的加入body标签
            InterfaceHeadRelate relate =interfaceHeadRelateService.findUniqueBy("interfaceId", interfaceId);
            if(null != relate){
                List<SDA> interfaceheadSDAs = sdaService.getByInterfaceHeadBodySDAs(relate.getHeadId(), structName);
                addListContent(sdas, interfaceheadSDAs);
            }

        }
        fillPackageParserElement(bodyElement, sdas);
    }

    public void fillPackageParserElement(Element parentElement, List<SDA> children){
        for(SDA child : children){
            if("array".equalsIgnoreCase(child.getType()) || "struct".equalsIgnoreCase(child.getType())){//处理有子节点的情况，CRCB：添加array节点
                Element childElement = parentElement.addElement(child.getStructName());
                Element sdoElement = childElement.addElement("sdo");
                addAttribute(childElement, "metadataid", child.getMetadataId());
                addAttribute(childElement, "type", child.getType());
                if("struct".equalsIgnoreCase(child.getType().toLowerCase())){
                    addAttribute(childElement, "is_struct", "true");
                }else{
                    addAttribute(childElement, "is_struct", "false");
                }
                List<SDA> subChildren = sdaService.getChildren(child);
                fillPackageParserElement(sdoElement, subChildren);
            }else{
                Element childElement = parentElement.addElement(child.getStructName());
                addAttribute(childElement, "metadataid", child.getMetadataId());
//                addAttribute(childElement, "chinese_name", child.getStructAlias());
            }
        }
    }
}
