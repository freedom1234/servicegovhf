package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.Interface;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.impl.IdaServiceImpl;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kongxfa on 2016/1/12.
 */
@Component("aPBKPackerParserConfigGenerator")
public class APBKPackerParserConfigGenerator extends ConfigExportGenerator {
    private Log log = LogFactory.getLog(UnStandardXMLConfigExportGender.class);

    @Autowired
    IdaServiceImpl idaService;
    @Autowired
    SDAServiceImpl sdaService;

    private String interfaceId = "";


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
                    fileName = path + File.separator + "channel_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel_service_" + serviceId + operationId + ".xml";
                } else {
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_system.xml";
                }

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                Document doc = DocumentHelper.createDocument();
                Element root = doc.addElement("ROOT");//根节点
                Element HEAD = root.addElement("HEAD");

                //加入节点 header 。tranCode
//                Element head = root.addElement("HEAD");
//                addAttribute(header, "type", "fix");
//                Element tranCode = header.addElement("tranCode");
//                addAttribute(tranCode, "length", "4");
//                addAttribute(tranCode, "type", "string");
//                addAttribute(tranCode, "isSdoHeader", "true");
//
//                Element body = serviceElement.addElement("body");
//                addAttribute(body, "type", "xml");
//                addAttribute(body, "createXmlHead", "true");
//                addAttribute(body, "xmlencoding", "GB18030");
//                Element Result = body.addElement("Result");
//                addAttribute(Result, "package_type", "xml");


                Ida reqsponseIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.REQUEST_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqsponseIda.getId());

                //加入 bean、page、UserLogonInfo节点，判断是否有page、UserLogonInfo节点
                boolean haveTAIL = false;
                boolean haveBODY = false;
                for (Ida ida : children) {
                    if ("RETCODE".equalsIgnoreCase(ida.getStructName()) || "RETREASON".equalsIgnoreCase(ida.getStructName())) {
                        haveTAIL = true;
                    }
                    if ("PRIVATE".equalsIgnoreCase(ida.getStructName())) {
                        haveBODY = true;
                    }
                }

                Element TAIL = null;
                Element BODY = null;
                if (haveTAIL) {
                    TAIL = root.addElement("TAIL");
                }
                if (haveBODY) {
                    BODY = root.addElement("BODY");
                }
                interfaceId = serviceInvoke.getInterfaceId();
                fillContentTAILBODY(HEAD, TAIL, BODY, children, serviceId, operationId);
//                else{
//                    fillContent(bean, children, serviceId, operationId);
//                }

                //两个xml文件并集,保存到已存在的xml文件中
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
                            "utf-8"));
                    String temp = null;
                    StringBuffer xml=new StringBuffer();
                    try {
                        while ((temp = reader.readLine()) != null) {
                            xml.append(temp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Document docAll = DocumentHelper.parseText(xml.toString());
                    mergeXmlNode(docAll.getRootElement(),root);
                    doc=docAll;
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
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_b_" + system.getSystemAb().toLowerCase().substring(2) + "_channel.xml";
                } else {
                    fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_system_service_" + serviceId + operationId + ".xml";
                }

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                Document doc = DocumentHelper.createDocument();


                Ida reqestIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.RESPONSE_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqestIda.getId());

                Element root = doc.addElement("ROOT");//根节点
//                addAttribute(serviceElement, "package_type", "xml");

                boolean haveTAIL = false;
                boolean haveBODY = false;
                for (Ida ida : children) {
                    if ("RETCODE".equalsIgnoreCase(ida.getStructName()) || "RETREASON".equalsIgnoreCase(ida.getStructName())) {
                        haveTAIL = true;
                    }
                    if ("PRIVATE".equalsIgnoreCase(ida.getStructName())) {
                        haveBODY = true;
                    }
                }

                Element HEAD = root.addElement("HEAD");
                Element TAIL = null;
                Element BODY = null;
                if (haveTAIL) {
                    TAIL = root.addElement("TAIL");
                }
                if (haveBODY) {
                    BODY = root.addElement("BODY");
                }

                interfaceId = serviceInvoke.getInterfaceId();
//                fillContent(root, children, serviceId, operationId);
                fillContentTAILBODY(HEAD, TAIL, BODY, children, serviceId, operationId);

                //两个xml文件并集,保存到已存在的xml文件中
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
                            "utf-8"));
                    String temp = null;
                    StringBuffer xml=new StringBuffer();
                    try {
                        while ((temp = reader.readLine()) != null) {
                            xml.append(temp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Document docAll = DocumentHelper.parseText(xml.toString());
                    mergeXmlNode(docAll.getRootElement(),root);
                    doc=docAll;
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
     * 填充 TAIL、BODY 节点
     */
    public void fillContentTAILBODY(Element HEAD, Element TAIL, Element BODY, List<Ida> idas, String serviceId, String operationId) {
        for (Ida ida : idas) {
            if ("RETCODE".equals(ida.getStructName()) || "RETREASON".equals(ida.getStructName())) {
                Element idaElement = fillContentTag(TAIL, ida, serviceId, operationId);
            } else if ("PRIVATE".equals(ida.getStructName())) {
                Element idaElement = fillContentTag(BODY, ida, serviceId, operationId);
            } else {
                Element idaElement = fillContentTag(HEAD, ida, serviceId, operationId);
                List<Ida> children = idaService.getNotEmptyByParentId(ida.getId());
                fillContent(idaElement, children, serviceId, operationId);
            }
        }
    }


    public Element fillContentTag(Element element, Ida ida, String serviceId, String operationId) {
        Element idaElement = element.addElement(ida.getStructName());
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        params.put("xpath", ida.getXpath());
        SDA sda = sdaService.findUniqueBy(params);
        if (null != sda) {
            addAttribute(idaElement, "metadataid", sda.getMetadataId());
        }
        if (ida.getStructName().equals("TRANSID")) {
            addAttribute(idaElement, "expression", "'" + interfaceId + "'");
            addAttribute(idaElement, "isSdoHeader", "true");
            if (idaElement.attribute("metadataid") != null) {
                idaElement.remove(idaElement.attribute("metadataid"));
            }
        }
        if ("array".equalsIgnoreCase(ida.getType())) {
            addAttribute(idaElement, "type", "array");
            addAttribute(idaElement, "is_struct", "false");
        }
//        addAttribute(idaElement, "chinese_name", ida.getStructAlias());
        return idaElement;


    }

}
