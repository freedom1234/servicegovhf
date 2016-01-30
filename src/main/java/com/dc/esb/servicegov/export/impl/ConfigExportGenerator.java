package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.impl.InterfaceHeadRelateServiceImpl;
import com.dc.esb.servicegov.service.impl.InterfaceServiceImpl;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
@Component
public class ConfigExportGenerator {
    private Log log = LogFactory.getLog(ConfigExportGenerator.class);
    @Autowired
    OperationServiceImpl operationService;
    @Autowired
    SDAServiceImpl sdaService;
    @Autowired
    InterfaceServiceImpl interfaceService;
    @Autowired
    InterfaceHeadRelateServiceImpl interfaceHeadRelateService;

    public void generate(ServiceInvoke serviceInvoke, String path) {
        if (Constants.INVOKE_TYPE_CONSUMER.equals(serviceInvoke.getType())) {
            path = path + File.separator + "in_config";
        }
        if (Constants.INVOKE_TYPE_PROVIDER.equals(serviceInvoke.getType())) {
            path = path + File.separator + "out_config";
        }
        genrateSystemServiceFile(serviceInvoke, path);//生成系统请求文件
        genrateServiceFile(serviceInvoke, path);
        genrateServiceSystemFile(serviceInvoke, path);//生成esb响应文件
    }

    /**
     * 生成系统请求文件
     *
     * @param serviceInvoke
     * @param path
     */
    public void genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path) {
    }

    /**
     * 生成esb响应文件
     *
     * @param serviceInvoke
     * @param path
     */
    public void genrateServiceSystemFile(ServiceInvoke serviceInvoke, String path) {
    }

    /**
     * 生成服务定义文件
     *
     * @param serviceInvoke
     * @param path
     * @return
     */
    public void genrateServiceFile(ServiceInvoke serviceInvoke, String path) {
        try {
            String serviceId = serviceInvoke.getServiceId();
            String operationId = serviceInvoke.getOperationId();
            Operation operation = operationService.getOperation(serviceId, operationId);
            com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
            String fileName = path + File.separator + "service_" + serviceId + operationId + ".xml";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
//            if (file.exists()) {
//                fileName = fileName.substring(0, fileName.length() - 4) + serviceInvoke.getInterfaceId() + ".xml";
//                file = new File(fileName);
//            }

            Document doc = DocumentHelper.createDocument();
            Element rootElement = doc.addElement("S" + serviceId + operationId);//根节点
            Element requestElement = rootElement.addElement("request");
            Element reqSdoRoottElement = requestElement.addElement("sdoroot");
            Element responseElement = rootElement.addElement("response");
            Element resSdoRoottElement = responseElement.addElement("sdoroot");
//            fillServiceHead(operation, serviceInvoke.getInterfaceId(), reqSdoRoottElement, Constants.ElementAttributes.REQUEST_NAME, true);
//            fillServiceHead(operation, serviceInvoke.getInterfaceId(),resSdoRoottElement, Constants.ElementAttributes.RESPONSE_NAME, true);

            //手动写报文头 request端
            Element sys_head_req = reqSdoRoottElement.addElement("SYS_HEAD");
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

            Element app_head_req = reqSdoRoottElement.addElement("APP_HEAD");
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

            //手动写报文头 response端
            Element sys_head_res = resSdoRoottElement.addElement("SYS_HEAD");
            Element SvcId1 = sys_head_res.addElement("SvcId").addAttribute("metadataid", "SvcId");
            Element SvcScnId1 = sys_head_res.addElement("SvcScnId").addAttribute("metadataid", "SvcScnId");
            Element CnsmSysId1 = sys_head_res.addElement("CnsmSysId").addAttribute("metadataid", "CnsmSysId");
            Element PrvdSysId = sys_head_res.addElement("PrvdSysId").addAttribute("metadataid", "PrvdSysId");
            Element CnsmSysSeqNo1 = sys_head_res.addElement("CnsmSysSeqNo").addAttribute("metadataid", "CnsmSysSeqNo");
            Element PrvdSysSeqNo = sys_head_res.addElement("PrvdSysSeqNo").addAttribute("metadataid", "PrvdSysSeqNo");
            Element Mac1 = sys_head_res.addElement("Mac").addAttribute("metadataid", "Mac");
            Element MacOrgId1 = sys_head_res.addElement("MacOrgId").addAttribute("metadataid", "MacOrgId");
            Element TranDate1 = sys_head_res.addElement("TranDate").addAttribute("metadataid", "TranDate");
            Element TranTime1 = sys_head_res.addElement("TranTime").addAttribute("metadataid", "TranTime");
            Element TranRetSt = sys_head_res.addElement("TranRetSt").addAttribute("metadataid", "TranRetSt");
            Element array = sys_head_res.addElement("array");
            Element RetInf = array.addElement("RetInf").addAttribute("metadataid", "RetInf").addAttribute("type", "array").addAttribute("is_struct", "false");
            Element sdo = RetInf.addElement("sdo");
            Element RetCode = sdo.addElement("RetCode").addAttribute("metadataid", "RetCode");
            Element RetMsg = sdo.addElement("RetMsg").addAttribute("metadataid", "RetMsg");
            Element PrvdSysSvrId = sys_head_res.addElement("PrvdSysSvrId").addAttribute("metadataid", "PrvdSysSvrId");
//            Element UserLang1=sys_head_res.addElement("UserLang").addAttribute("metadataid", "UserLang");

            Element app_head_res = resSdoRoottElement.addElement("APP_HEAD");
            Element TellerId1 = app_head_res.addElement("TellerId").addAttribute("metadataid", "TellerId");
            Element BranchId1 = app_head_res.addElement("BranchId").addAttribute("metadataid", "BranchId");
            Element TlrPswd1 = app_head_res.addElement("TlrPswd").addAttribute("metadataid", "TlrPswd");
            Element TlrLvl1 = app_head_res.addElement("TlrLvl").addAttribute("metadataid", "TlrLvl");
            Element TlrType1 = app_head_res.addElement("TlrType").addAttribute("metadataid", "TlrType");
            Element ChkFlag1 = app_head_res.addElement("ChkFlag").addAttribute("metadataid", "ChkFlag");
            Element AuthFlag1 = app_head_res.addElement("AuthFlag").addAttribute("metadataid", "AuthFlag");
            Element AuthTlrId1 = app_head_res.addElement("AuthTlrId").addAttribute("metadataid", "AuthTlrId");
//            Element AuthBrId1 =app_head_res.addElement("AuthBrId").addAttribute("metadataid","AuthBrId");
            Element AuthBrId1 = app_head_res.addElement("AuthBrchId").addAttribute("metadataid", "AuthBrchId");

            fillBody(operation, serviceInvoke.getInterfaceId(), reqSdoRoottElement, Constants.ElementAttributes.REQUEST_NAME, true);
            fillBody(operation, serviceInvoke.getInterfaceId(), resSdoRoottElement, Constants.ElementAttributes.RESPONSE_NAME, true);

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
                mergeXmlNode(docAll.getRootElement(), rootElement);
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
            log.error("生成服务定义文件失败！", e);
        }
    }

    /**
     * 填充服务报文头元素
     *
     * @param operation
     * @param targetElement
     */
    public void fillServiceHead(Operation operation, String interfaceId, Element targetElement, String targetName, boolean arrayFlag) {
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
            fillElement(headElement, sdas, arrayFlag);
        }
    }

    /**
     * 填充body元素
     *
     * @param operation
     * @param targetElement
     */
    public void fillBody(Operation operation, String interfaceId, Element targetElement, String structName, boolean arrayFlag) {
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
        fillElement(bodyElement, sdas, arrayFlag);
    }

    /**
     * 填充body元素
     *
     * @param operation
     * @param bodyElement
     */
    public void fillBodyInside(Operation operation, String interfaceId, Element bodyElement, String structName, boolean arrayFlag) {
        SDA reSDA = sdaService.getByStructName(operation.getServiceId(), operation.getOperationId(), structName);
        List<SDA> sdas = sdaService.getChildExceptServiceHead(reSDA.getId(), operation.getHeadId());
        if (StringUtils.isNotEmpty(interfaceId)) {//查询接口报文头中对应约束元素syshead，apphead的加入对应头标签，其他的加入body标签
            InterfaceHeadRelate relate = interfaceHeadRelateService.findUniqueBy("interfaceId", interfaceId);
            if (null != relate) {
                List<SDA> interfaceheadSDAs = sdaService.getByInterfaceHeadBodySDAs(relate.getHeadId(), structName);
                addListContent(sdas, interfaceheadSDAs);
            }
        }
        fillElement(bodyElement, sdas, arrayFlag);
    }

    public void fillElement(Element parentElement, List<SDA> children, boolean arrayFlag) {
        for (SDA child : children) {
            if ("array".equalsIgnoreCase(child.getType()) || "struct".equalsIgnoreCase(child.getType())) {//处理有子节点的情况，CRCB：添加array节点
                Element element;
                if (arrayFlag) {
                    element = parentElement.addElement("array");
                } else {
                    element = parentElement;
                }
                Element childElement = element.addElement(child.getStructName());
                addAttribute(childElement, "metadataid", child.getMetadataId());
//                addAttribute(childElement, "type", child.getType());
                addAttribute(childElement, "type", child.getType().toLowerCase());
                if ("struct".equalsIgnoreCase(child.getType().toLowerCase())) {
                    addAttribute(childElement, "is_struct", "true");
                } else {
                    addAttribute(childElement, "is_struct", "false");
                }
                Element sdoElement = childElement.addElement("sdo");
                List<SDA> subChildren = sdaService.getChildren(child);
                fillElement(sdoElement, subChildren, arrayFlag);
            } else {
                Element childElement = parentElement.addElement(child.getStructName());
                addAttribute(childElement, "metadataid", child.getMetadataId());
//                addAttribute(childElement, "chinese_name", child.getStructAlias());
            }
        }
    }

    public void addAttribute(Element element, String name, String value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        element.addAttribute(name, value);
    }

    public void addListContent(List<SDA> parentList, List<SDA> childList) {
        for (SDA child : childList) {
            if (!parentList.contains(child)) {
                boolean exsitFlag = false;
                for (SDA parent : parentList) {
                    if (parent.getMetadataId().equals(child.getMetadataId())) {
                        exsitFlag = true;
                        break;
                    }
                }
                if (!exsitFlag) parentList.add(child);
            }
        }
    }

    //两个xml节点合并
    public void mergeXmlNode(Element rAll, Element rSub) {
        List<Element> listAll = rAll.elements();
        List<Element> listSub = rSub.elements();
        Boolean isHave = false;
        for (int i = 0; i < listSub.size(); i++) {
            isHave = false;
            for (int j = 0; j < listAll.size(); j++) {
                if (listAll.get(j).getName().equals(listSub.get(i).getName())) {
                    isHave = true;
                    List<Element> liAll = listAll.get(j).elements();
                    List<Element> li = listSub.get(i).elements();
                    Element eAll = rAll.element(listAll.get(j).getName());
                    Element eSub = rSub.element(listSub.get(i).getName());
                    if (eSub.elements().size() > 0) {
                        mergeXmlNode(eAll, eSub);
                    }
                    break;
                }
            }
            if (!isHave) {
                Element eSub = listSub.get(i);
                rAll.addElement(eSub.getName());
                rAll.element(eSub.getName()).appendAttributes(eSub);
                if (eSub.elements().size() > 0) {
//                    System.out.println("本节点复制完，再复制子节点:执行add()===="+eSub.getName());
                    mergeXmlNode(rAll.element(eSub.getName()), rSub.element(eSub.getName()));
                }
            }
        }
    }
}
