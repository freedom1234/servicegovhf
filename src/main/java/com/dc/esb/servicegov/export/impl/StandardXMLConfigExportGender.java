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

import java.io.*;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
@Component
public class StandardXMLConfigExportGender extends ConfigExportGenerator {
    private Log log = LogFactory.getLog(StandardXMLConfigExportGender.class);

    /**
     * 生成系统请求文件
     *
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path) {
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
//            if(file.exists()){
//                fileName=fileName.substring(0,fileName.length()-4)+serviceInvoke.getInterfaceId()+".xml";
//                file = new File(fileName);
//            }

            Document doc = DocumentHelper.createDocument();
            Element serviceElement = doc.addElement("service");//根节点
//            addAttribute(serviceElement, "package_type", "xml");
//            addAttribute(serviceElement, "store-mode", "UTF-8");
//            fillPackageParserServiceHead(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.REQUEST_NAME, false);

            //手动写报文头 request端
            Element sys_head_req = serviceElement.addElement("SYS_HEAD");
            Element SvcId = sys_head_req.addElement("SvcId");
            SvcId.addAttribute("metadataid", "SvcId");
            Element SvcScnId = sys_head_req.addElement("SvcScnId");
            SvcScnId.addAttribute("metadataid", "SvcScnId");
            Element CnsmSysId = sys_head_req.addElement("CnsmSysId");
            CnsmSysId.addAttribute("metadataid", "CnsmSysId");
            Element ChnlType = sys_head_req.addElement("ChnlType");
            ChnlType.addAttribute("metadataid", "ChnlType");
            Element SrcSysId = sys_head_req.addElement("SrcSysId");
            SrcSysId.addAttribute("metadataid", "SrcSysId");
            Element CnsmSysSeqNo = sys_head_req.addElement("CnsmSysSeqNo");
            CnsmSysSeqNo.addAttribute("metadataid", "CnsmSysSeqNo");
            Element SrcSysSeqNo = sys_head_req.addElement("SrcSysSeqNo");
            SrcSysSeqNo.addAttribute("metadataid", "SrcSysSeqNo");
            Element Mac = sys_head_req.addElement("Mac");
            Mac.addAttribute("metadataid", "Mac");
            Element MacOrgId = sys_head_req.addElement("MacOrgId");
            MacOrgId.addAttribute("metadataid", "MacOrgId");
            Element TranMode = sys_head_req.addElement("TranMode");
            TranMode.addAttribute("metadataid", "TranMode");
            Element TranDate = sys_head_req.addElement("TranDate");
            TranDate.addAttribute("metadataid", "TranDate");
            Element TranTime = sys_head_req.addElement("TranTime");
            TranTime.addAttribute("metadataid", "TranTime");
            Element TmnlNo = sys_head_req.addElement("TmnlNo");
            TmnlNo.addAttribute("metadataid", "TmnlNo");
            Element SrcSySysnlNo = sys_head_req.addElement("SrcSySysnlNo");
            SrcSySysnlNo.addAttribute("metadataid", "SrcSySysnlNo");
            Element CnsmSysSvrId = sys_head_req.addElement("CnsmSysSvrId");
            CnsmSysSvrId.addAttribute("metadataid", "CnsmSysSvrId");
            Element SrcSysSvrId = sys_head_req.addElement("SrcSysSvrId");
            SrcSysSvrId.addAttribute("metadataid", "SrcSysSvrId");
//            Element UserLang=sys_head_req.addElement("UserLang");
//            UserLang.addAttribute("metadataid","UserLang");

            Element app_head_req = serviceElement.addElement("APP_HEAD");
            Element TellerId = app_head_req.addElement("TellerId");
            TellerId.addAttribute("metadataid", "TellerId");
            Element BranchId = app_head_req.addElement("BranchId");
            BranchId.addAttribute("metadataid", "BranchId");
            Element TlrPswd = app_head_req.addElement("TlrPswd");
            TlrPswd.addAttribute("metadataid", "TlrPswd");
            Element TlrLvl = app_head_req.addElement("TlrLvl");
            TlrLvl.addAttribute("metadataid", "TlrLvl");
            Element TlrType = app_head_req.addElement("TlrType");
            TlrType.addAttribute("metadataid", "TlrType");
            Element ChkFlag = app_head_req.addElement("ChkFlag");
            ChkFlag.addAttribute("metadataid", "ChkFlag");
            Element AuthFlag = app_head_req.addElement("AuthFlag");
            AuthFlag.addAttribute("metadataid", "AuthFlag");
            Element AuthTlrId = app_head_req.addElement("AuthTlrId");
            AuthTlrId.addAttribute("metadataid", "AuthTlrId");
//            Element AuthBrId=app_head_req.addElement("AuthBrId");
//            AuthBrId.addAttribute("metadataid","AuthBrId");
            Element AuthBrId = app_head_req.addElement("AuthBrchId");
            AuthBrId.addAttribute("metadataid", "AuthBrchId");
            Element AuthTlrPswd = app_head_req.addElement("AuthTlrPswd");
            AuthTlrPswd.addAttribute("metadataid", "AuthTlrPswd");

            fillPackageParserBody(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.REQUEST_NAME, false);

            //两个xml文件并集,保存到已存在的xml文件中
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
                        "utf-8"));
                String temp = null;
                StringBuffer xml = new StringBuffer();
                try {
                    while ((temp = reader.readLine()) != null) {
                        xml.append(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Document docAll = DocumentHelper.parseText(xml.toString());
                mergeXmlNode(docAll.getRootElement(), serviceElement);
                doc = docAll;
            }

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
        } catch (Exception e) {
            log.error("生成请求文件失败！", e);
        }
    }

    /**
     * 生成esb响应文件
     *
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void genrateServiceSystemFile(ServiceInvoke serviceInvoke, String path) {
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
//            if(file.exists()){
//                fileName=fileName.substring(0,fileName.length()-4)+serviceInvoke.getInterfaceId()+".xml";
//                file = new File(fileName);
//            }

            Document doc = DocumentHelper.createDocument();
            Element serviceElement = doc.addElement("service");//根节点
//            addAttribute(serviceElement, "package_type", "xml");
//            addAttribute(serviceElement, "store-mode", "UTF-8");
//            fillPackageParserServiceHead(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.RESPONSE_NAME, false);

//            //手动写报文头 request端
//            Element sys_head_req = serviceElement.addElement("SYS_HEAD");
//            Element SvcId=sys_head_req.addElement("SvcId");
//            SvcId.addAttribute("metadataid","SvcId");
//            Element SvcScnId=sys_head_req.addElement("SvcScnId");
//            SvcScnId.addAttribute("metadataid","SvcScnId");
//            Element CnsmSysId=sys_head_req.addElement("CnsmSysId");
//            CnsmSysId.addAttribute("metadataid","CnsmSysId");
//            Element ChnlType=sys_head_req.addElement("ChnlType");
//            ChnlType.addAttribute("metadataid","ChnlType");
//            Element SrcSysId=sys_head_req.addElement("SrcSysId");
//            SrcSysId.addAttribute("metadataid","SrcSysId");
//            Element CnsmSysSeqNo=sys_head_req.addElement("CnsmSysSeqNo");
//            CnsmSysSeqNo.addAttribute("metadataid","CnsmSysSeqNo");
//            Element SrcSysSeqNo=sys_head_req.addElement("SrcSysSeqNo");
//            SrcSysSeqNo.addAttribute("metadataid","SrcSysSeqNo");
//            Element Mac=sys_head_req.addElement("Mac");
//            Mac.addAttribute("metadataid","Mac");
//            Element MacOrgId=sys_head_req.addElement("MacOrgId");
//            MacOrgId.addAttribute("metadataid","MacOrgId");
//            Element TranMode=sys_head_req.addElement("TranMode");
//            TranMode.addAttribute("metadataid","TranMode");
//            Element TranDate=sys_head_req.addElement("TranDate");
//            TranDate.addAttribute("metadataid","TranDate");
//            Element TranTime=sys_head_req.addElement("TranTime");
//            TranTime.addAttribute("metadataid", "TranTime");
//            Element TmnlNo=sys_head_req.addElement("TmnlNo");
//            TmnlNo.addAttribute("metadataid","TmnlNo");
//            Element SrcSySysnlNo=sys_head_req.addElement("SrcSySysnlNo");
//            SrcSySysnlNo.addAttribute("metadataid","SrcSySysnlNo");
//            Element CnsmSysSvrId=sys_head_req.addElement("CnsmSysSvrId");
//            CnsmSysSvrId.addAttribute("metadataid","CnsmSysSvrId");
//            Element SrcSysSvrId=sys_head_req.addElement("SrcSysSvrId");
//            SrcSysSvrId.addAttribute("metadataid","SrcSysSvrId");
//
//            Element app_head_req = serviceElement.addElement("APP_HEAD");
//            Element TellerId=app_head_req.addElement("TellerId");
//            TellerId.addAttribute("metadataid","TellerId");
//            Element BranchId=app_head_req.addElement("BranchId");
//            BranchId.addAttribute("metadataid","BranchId");
//            Element TlrPswd=app_head_req.addElement("TlrPswd");
//            TlrPswd.addAttribute("metadataid","TlrPswd");
//            Element TlrLvl=app_head_req.addElement("TlrLvl");
//            TlrLvl.addAttribute("metadataid","TlrLvl");
//            Element TlrType=app_head_req.addElement("TlrType");
//            TlrType.addAttribute("metadataid","TlrType");
//            Element ChkFlag=app_head_req.addElement("ChkFlag");
//            ChkFlag.addAttribute("metadataid","ChkFlag");
//            Element AuthFlag=app_head_req.addElement("AuthFlag");
//            AuthFlag.addAttribute("metadataid","AuthFlag");
//            Element AuthTlrId=app_head_req.addElement("AuthTlrId");
//            AuthTlrId.addAttribute("metadataid","AuthTlrId");
//            Element AuthTlrPswd=app_head_req.addElement("AuthTlrPswd");
//            AuthTlrPswd.addAttribute("metadataid","AuthTlrPswd");
//            Element AuthBrchId=app_head_req.addElement("AuthBrchId");
//            AuthBrchId.addAttribute("metadataid","AuthBrchId");


            //手动写报文头 request端
            Element sys_head_req = serviceElement.addElement("SYS_HEAD");
            Element SvcId = sys_head_req.addElement("SvcId").addAttribute("metadataid", "SvcId");
            Element SvcScnId = sys_head_req.addElement("SvcScnId").addAttribute("metadataid", "SvcScnId");
            sys_head_req.addElement("CnsmSysId").addAttribute("metadataid", "CnsmSysId");
            sys_head_req.addElement("PrvdSysId").addAttribute("metadataid", "PrvdSysId");
            sys_head_req.addElement("CnsmSysSeqNo").addAttribute("metadataid", "CnsmSysSeqNo");
            sys_head_req.addElement("PrvdSysSeqNo").addAttribute("metadataid", "PrvdSysSeqNo");
            sys_head_req.addElement("Mac").addAttribute("metadataid", "Mac");
            sys_head_req.addElement("MacOrgId").addAttribute("metadataid", "MacOrgId");
            sys_head_req.addElement("TranDate").addAttribute("metadataid", "TranDate");
            sys_head_req.addElement("TranTime").addAttribute("metadataid", "TranTime");
            sys_head_req.addElement("TranRetSt").addAttribute("metadataid", "TranRetSt");
            Element RetInf = sys_head_req.addElement("RetInf").addAttribute("metadataid", "RetInf").addAttribute("type", "array").addAttribute("is_struct", "false");
            RetInf.addElement("RetCode").addAttribute("metadataid", "RetCode");
            RetInf.addElement("RetMsg").addAttribute("metadataid", "RetMsg");
            sys_head_req.addElement("PrvdSysSvrId").addAttribute("metadataid", "PrvdSysSvrId");
//            sys_head_req.addElement("UserLang").addAttribute("metadataid","UserLang");

            Element app_head_req = serviceElement.addElement("APP_HEAD");
            Element TellerId = app_head_req.addElement("TellerId");
            TellerId.addAttribute("metadataid", "TellerId");
            Element BranchId = app_head_req.addElement("BranchId");
            BranchId.addAttribute("metadataid", "BranchId");
            Element TlrPswd = app_head_req.addElement("TlrPswd");
            TlrPswd.addAttribute("metadataid", "TlrPswd");
            Element TlrLvl = app_head_req.addElement("TlrLvl");
            TlrLvl.addAttribute("metadataid", "TlrLvl");
            Element TlrType = app_head_req.addElement("TlrType");
            TlrType.addAttribute("metadataid", "TlrType");
            Element ChkFlag = app_head_req.addElement("ChkFlag");
            ChkFlag.addAttribute("metadataid", "ChkFlag");
            Element AuthFlag = app_head_req.addElement("AuthFlag");
            AuthFlag.addAttribute("metadataid", "AuthFlag");
            Element AuthTlrId = app_head_req.addElement("AuthTlrId");
            AuthTlrId.addAttribute("metadataid", "AuthTlrId");
//            Element AuthBrId=app_head_req.addElement("AuthBrId");
//            AuthBrId.addAttribute("metadataid","AuthBrId");
            Element AuthBrId = app_head_req.addElement("AuthBrchId");
            AuthBrId.addAttribute("metadataid", "AuthBrchId");

            fillPackageParserBody(operation, serviceInvoke.getInterfaceId(), serviceElement, Constants.ElementAttributes.RESPONSE_NAME, false);

            //两个xml文件并集,保存到已存在的xml文件中
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
                        "utf-8"));
                String temp = null;
                StringBuffer xml = new StringBuffer();
                try {
                    while ((temp = reader.readLine()) != null) {
                        xml.append(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Document docAll = DocumentHelper.parseText(xml.toString());
                mergeXmlNode(docAll.getRootElement(), serviceElement);
                doc = docAll;
            }

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
        } catch (Exception e) {
            log.error("生成响应文件失败！", e);
        }
    }

    public void fillPackageParserServiceHead(Operation operation, String interfaceId, Element targetElement, String targetName, boolean arrayFlag) {
        String[] headIds = operation.getHeadId().split("\\,");//服务报文头
        for (String headId : headIds) {
            Element headElement = targetElement.addElement(headId.toUpperCase());//添加服务报文头标签 例：<SYS_HEAD> <APP_HEAD>
            SDA reServiceHeadSDA = sdaService.getByStructName(headId, targetName);
            List<SDA> sdas = sdaService.getServiceHeadRequired(headId, reServiceHeadSDA.getId());//服务报文头必输SDA
            SDA reOperationSDA = sdaService.getByStructName(operation.getServiceId(), operation.getOperationId(), targetName);
            List<SDA> operationHeadSDAs = sdaService.getOperationHeadSDAs(operation.getServiceId(), operation.getOperationId(), headId, reOperationSDA.getId());//场景sda中约束条件为相应报文头的元素
//            sdas.addAll(operationHeadSDAs);
            addListContent(sdas, operationHeadSDAs);
            if (StringUtils.isNotEmpty(interfaceId)) {//查询接口报文头中对应约束元素syshead，apphead的加入对应头标签，其他的加入body标签
                InterfaceHeadRelate relate = interfaceHeadRelateService.findUniqueBy("interfaceId", interfaceId);
                if (null != relate) {
                    List<SDA> interfaceheadSDAs = sdaService.getByInterfaceHeadSDAs(relate.getHeadId(), targetName, headId);
                    addListContent(sdas, interfaceheadSDAs);
                }
            }
            fillPackageParserElement(headElement, sdas);
        }
    }

    /**
     * 填充body元素
     *
     * @param operation
     * @param targetElement
     */
    public void fillPackageParserBody(Operation operation, String interfaceId, Element targetElement, String structName, boolean arrayFlag) {
        Element bodyElement = targetElement.addElement("BODY");
        SDA reSDA = sdaService.getByStructName(operation.getServiceId(), operation.getOperationId(), structName);
        List<SDA> sdas = sdaService.getChildExceptServiceHead(reSDA.getId(), operation.getHeadId());
        if (StringUtils.isNotEmpty(interfaceId)) {//查询接口报文头中对应约束元素syshead，apphead的加入对应头标签，其他的加入body标签
            InterfaceHeadRelate relate = interfaceHeadRelateService.findUniqueBy("interfaceId", interfaceId);
            if (null != relate) {
                List<SDA> interfaceheadSDAs = sdaService.getByInterfaceHeadBodySDAs(relate.getHeadId(), structName);
                addListContent(sdas, interfaceheadSDAs);
            }

        }
        fillPackageParserElement(bodyElement, sdas);
    }

    public void fillPackageParserElement(Element parentElement, List<SDA> children) {
        for (SDA child : children) {
            if ("array".equalsIgnoreCase(child.getType()) || "struct".equalsIgnoreCase(child.getType())) {//处理有子节点的情况，CRCB：添加array节点
                Element childElement = parentElement.addElement(child.getStructName());
//                Element sdoElement = childElement.addElement("sdo");
                addAttribute(childElement, "metadataid", child.getMetadataId());
                addAttribute(childElement, "type", child.getType().toLowerCase());
                if ("struct".equalsIgnoreCase(child.getType().toLowerCase())) {
                    addAttribute(childElement, "is_struct", "true");
                } else {
                    addAttribute(childElement, "is_struct", "false");
                }
                List<SDA> subChildren = sdaService.getChildren(child);
                fillPackageParserElement(childElement, subChildren);
            } else {
                Element childElement = parentElement.addElement(child.getStructName());
                addAttribute(childElement, "metadataid", child.getMetadataId());
//                addAttribute(childElement, "chinese_name", child.getStructAlias());
            }
        }
    }
}
