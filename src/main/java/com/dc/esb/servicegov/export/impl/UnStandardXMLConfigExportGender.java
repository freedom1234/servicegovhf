package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
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
import org.jboss.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/17.
 */
@Component
public class UnStandardXMLConfigExportGender extends ConfigExportGenerator{
    private Log log = LogFactory.getLog(UnStandardXMLConfigExportGender.class);

    @Autowired
    IdaServiceImpl idaService;
    @Autowired
    SDAServiceImpl sdaService;

    /**
     * 生成系统请求文件
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void  genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path){
        try {
            Interface inter = serviceInvoke.getInter();
            if(null != inter){
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
//                String fileName = path + File.separator + "channel_" + system.getSystemAb() + "_service_" + serviceId + operationId + ".xml";
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
                Element serviceElement = doc.addElement("service");//根节点
                addAttribute(serviceElement, "package_type", "xml");
                addAttribute(serviceElement, "store-mode", "UTF-8");

                Ida reqestIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.REQUEST_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqestIda.getId());
                fillContent(serviceElement, children, serviceId, operationId);

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
                    mergeXmlNode(docAll.getRootElement(),serviceElement);
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
        }catch (Exception e){
            log.error(e, e);
        }
    }

    /**
     * 生成esb响应文件
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void  genrateServiceSystemFile(ServiceInvoke serviceInvoke, String path) {
        try {
            Interface inter = serviceInvoke.getInter();
            if (null != inter) {
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
//                String fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb() + ".xml";
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
                Element serviceElement = doc.addElement("service");//根节点
                addAttribute(serviceElement, "package_type", "xml");
                addAttribute(serviceElement, "store-mode", "UTF-8");

                Ida reqsponseIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.RESPONSE_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqsponseIda.getId());
                fillContent(serviceElement, children, serviceId, operationId);

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
                    mergeXmlNode(docAll.getRootElement(),serviceElement);
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
    public void fillContent(Element element, List<Ida> idas, String serviceId, String operationId){
        for(Ida ida : idas){
            Element idaElement = fillContentTag(element, ida, serviceId, operationId);
            List<Ida> children = idaService.getNotEmptyByParentId(ida.getId());
            fillContent(idaElement, children, serviceId, operationId);
        }
    }

    public Element fillContentTag(Element element, Ida ida, String serviceId, String operationId){
        Element idaElement = element.addElement(ida.getStructName());
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        params.put("xpath", ida.getXpath());
        SDA sda = sdaService.findUniqueBy(params);
        if(null != sda){
            addAttribute(idaElement, "metadataId", sda.getMetadataId());
        }
//        addAttribute(idaElement, "chinese_name", ida.getStructAlias());
        return idaElement;


    }
}
