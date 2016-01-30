package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.collections.map.LinkedMap;
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
import java.util.*;

/**
 * Created by kongxfa on 2015/12/25.
 */
@Component("eCDSPackerParserConfigGenerator")
public class ECDSPackerParserConfigGenerator extends ConfigExportGenerator {
    private Log log = LogFactory.getLog(UnStandardXMLConfigExportGender.class);

    @Autowired
    IdaServiceImpl idaService;
    @Autowired
    SDAServiceImpl sdaService;

    @Autowired
    ServiceInvokeServiceImpl serviceInvokeService;

    /**
     * 生成系统请求文件
     *
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path) {
        try {
            Interface inter = serviceInvoke.getInter();
            if (null != inter) {
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
                String fileName = null;
                if (path.indexOf("in_config") > 0) {
                    fileName = path + File.separator + "channel_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel_service_" + serviceId + operationId + ".xml";
                } else {
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_system.xml";
                }

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
//                if (file.exists()) {
//                    fileName = fileName.substring(0, fileName.length() - 4) + serviceInvoke.getInterfaceId() + ".xml";
//                    file = new File(fileName);
//                }

                Document doc = DocumentHelper.createDocument();
                Element serviceElement = doc.addElement("sdoroot");//根节点
                addAttribute(serviceElement, "package_type", "mixed");
                addAttribute(serviceElement, "store-mode", "GB18030");

                //加入节点 header 。tranCode
                Element header = serviceElement.addElement("header");
                addAttribute(header, "type", "fix");
                Element tranCode = header.addElement("tranCode");
                addAttribute(tranCode, "length", "4");
                addAttribute(tranCode, "type", "string");
                addAttribute(tranCode, "isSdoHeader", "true");

                Element body = serviceElement.addElement("body");
                addAttribute(body, "type", "xml");
                addAttribute(body, "createXmlHead", "true");
                addAttribute(body, "xmlencoding", "GB18030");
                Element Result = body.addElement("Result");
                addAttribute(Result, "package_type", "xml");

                Ida reqsponseIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.REQUEST_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqsponseIda.getId());

                //加入 bean、page、UserLogonInfo节点，判断是否有page、UserLogonInfo节点
                boolean havePage = false;
                boolean haveUserLogonInfo = false;
                for (Ida ida : children) {
                    if ("pageSize".equalsIgnoreCase(ida.getStructName()) || "currentPage".equalsIgnoreCase(ida.getStructName())) {
                        havePage = true;
                    }
                    if ("Sysid".equalsIgnoreCase(ida.getStructName()) || "userNo".equalsIgnoreCase(ida.getStructName())) {
                        haveUserLogonInfo = true;
                    }
                }

                Element bean = Result.addElement("Bean");
                Element page = null;
                Element UserLogonInfo = null;
                if (havePage) {
                    page = Result.addElement("Page");
                }
                if (haveUserLogonInfo) {
                    UserLogonInfo = Result.addElement("UserLogonInfo");
                }
                fillContentBeanPage(bean, page, UserLogonInfo, children, serviceId, operationId);
//                else{
//                    fillContent(bean, children, serviceId, operationId);
//                }

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
            }

        } catch (Exception e) {
            log.error(e, e);
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
            Interface inter = serviceInvoke.getInter();
            if (null != inter) {
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
                String fileName = null;
                if (path.indexOf("in_config") > 0) {
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel.xml";
                } else {
                    fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_system_service_" + serviceId + operationId + ".xml";
                }

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
//                if (file.exists()) {
//                    fileName = fileName.substring(0, fileName.length() - 4) + serviceInvoke.getInterfaceId() + ".xml";
//                    file = new File(fileName);
//                }

                Document doc = DocumentHelper.createDocument();
                Element serviceElement = doc.addElement("Result");//根节点
                addAttribute(serviceElement, "package_type", "xml");
//                addAttribute(serviceElement, "store-mode", "UTF-8");

                Ida reqestIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.RESPONSE_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqestIda.getId());
                fillContent(serviceElement, children, serviceId, operationId);

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
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 填充
     */
    public void fillContent(Element element, List<Ida> idas, String serviceId, String operationId) {
        for (Ida ida : idas) {
            Element idaElement = fillContentTag(element, ida, serviceId, operationId);
            List<Ida> children = idaService.getNotEmptyByParentId(ida.getId());
            fillContent(idaElement, children, serviceId, operationId);
        }
    }

    /**
     * 填充bean、page节点
     */
    public void fillContentBeanPage(Element bean, Element page, Element UserLogonInfo, List<Ida> idas, String serviceId, String operationId) {
        for (Ida ida : idas) {
            if ("Page".equalsIgnoreCase(ida.getStructName())) {
                continue;
            }
            if ("UserLogonInfo".equalsIgnoreCase(ida.getStructName())) {
                continue;
            }
            if ("pageSize".equals(ida.getStructName()) || "currentPage".equals(ida.getStructName())) {
                Element idaElement = fillContentTag(page, ida, serviceId, operationId);
            } else if ("Sysid".equals(ida.getStructName()) || "userNo".equals(ida.getStructName())) {
                Element idaElement = fillContentTag(UserLogonInfo, ida, serviceId, operationId);
            } else {
                Element idaElement = fillContentTag(bean, ida, serviceId, operationId);
                List<Ida> children = idaService.getNotEmptyByParentId(ida.getId());
                fillContent(idaElement, children, serviceId, operationId);
            }
        }
    }

    public Element fillContentTag(Element element, Ida ida, String serviceId, String operationId) {
//        Element idaElement = element.addElement(ida.getStructName());
        Element idaElement = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        params.put("xpath", ida.getXpath());
        SDA sda = sdaService.findUniqueBy(params);
        String type = null;
        if (null != sda) {
            type = sda.getType();
        }
        if ("array".equalsIgnoreCase(ida.getType()) || "array".equalsIgnoreCase(type)) {
            idaElement = element.addElement("info").addElement(ida.getStructName());
        } else {
            idaElement = element.addElement(ida.getStructName());
        }
        if (null != sda) {
            addAttribute(idaElement, "metadataid", sda.getMetadataId());
//            type = sda.getType();
        }
//        if ("array".equalsIgnoreCase(ida.getType())) {
        if ("array".equalsIgnoreCase(ida.getType()) || "array".equalsIgnoreCase(type)) {
            addAttribute(idaElement, "type", "array");
            addAttribute(idaElement, "is_struct", "false");
        }
//        addAttribute(idaElement, "chinese_name", ida.getStructAlias());
        return idaElement;


    }

}
