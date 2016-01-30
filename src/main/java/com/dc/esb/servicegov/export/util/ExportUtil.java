package com.dc.esb.servicegov.export.util;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.export.bean.MetadataNode;
import com.dc.esb.servicegov.service.SDAService;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2015/7/16.
 */
public class ExportUtil {

    /**
     * 递归获取SDA\
     * 某一节点的所有子节点
     *
     * @param sdas
     * @param sda
     * @param requestBody
     * @return
     */
    public static MetadataNode recursionFindSDA(List<SDA> sdas, SDA sda, MetadataNode requestBody) {
        List<MetadataNode> childList = getChildList(sdas, sda);// 得到子节点列表
        requestBody.setChildreans(childList);
        Iterator<MetadataNode> it = childList.iterator();
        while (it.hasNext()) {
            MetadataNode body = (MetadataNode) it.next();
            SDA s = new SDA();
            s.setId(body.getNodeId());
            if (hasChild(sdas, s)) {
                recursionFindSDA(sdas, s, body);
            }
        }

        return requestBody;
    }

    /**
     * 递归获取IDA
     * 某一节点的所有子节点
     *
     * @param idas
     * @param ida
     * @param requestBody
     * @return
     */
    public static MetadataNode recursionFindIDA(List<Ida> idas, Ida ida, MetadataNode requestBody) {
        List<MetadataNode> childList = getChildList(idas, ida);// 得到子节点列表
        requestBody.setChildreans(childList);
        Iterator<MetadataNode> it = childList.iterator();
        while (it.hasNext()) {
            MetadataNode body = (MetadataNode) it.next();
            Ida s = new Ida();
            s.setId(body.getNodeId());
            if (hasChild(idas, ida)) {
                recursionFindIDA(idas, s, body);
            }
        }

        return requestBody;
    }

    private static List<MetadataNode> getChildList(List<SDA> sdas, SDA sda) {
        List<MetadataNode> result = new ArrayList<MetadataNode>();
        for (SDA s : sdas) {
            if (s.getParentId() != null && s.getParentId().equals(sda.getId())) {
                MetadataNode requestBody = new MetadataNode();
                requestBody.setNodeId(s.getId());
                requestBody.setMetadataId(s.getMetadataId());
                requestBody.setNodeName(s.getStructName());
                requestBody.setChineseName(s.getStructAlias());
                requestBody.setType(s.getType());
                result.add(requestBody);
            }
        }
        return result;
    }

    private static List<MetadataNode> getChildList(List<Ida> idas, Ida ida) {
        List<MetadataNode> result = new ArrayList<MetadataNode>();
        for (Ida s : idas) {
            if (s.getParentId() != null && s.getParentId().equals(ida.getId())) {
                MetadataNode requestBody = new MetadataNode();
                requestBody.setNodeId(s.getId());
                requestBody.setMetadataId(s.getMetadataId());
                requestBody.setNodeName(s.getStructName());
                requestBody.setChineseName(s.getStructAlias());
                requestBody.setType(s.getType());
                result.add(requestBody);
            }
        }
        return result;
    }

    // 判断是否有子节点
    private static boolean hasChild(List<SDA> sdas, SDA sda) {
        for (SDA s : sdas) {
            if (s.getParentId() != null && s.getParentId().equals(sda.getId())) {
               return true;
            }
        }
        return false;
    }

    // 判断是否有子节点
    private static boolean hasChild(List<Ida> idas, Ida ida) {
        for (Ida s : idas) {
            if (s.getParentId() != null && s.getParentId().equals(ida.getId())) {
               return true;
            }
        }
        return false;
    }

    public static void generatorFile(MetadataNode requestBody, Element root) {
        for (MetadataNode body : requestBody.getChildreans()) {
            String prefix = "";

            if (root.getNamespace() != null && !"".equals(root.getNamespace().getPrefix())) {
                prefix = root.getNamespace().getPrefix() + ":";
            }
            Element ele = root.addElement(prefix + body.getNodeName());
            if (body.getChildreans() != null && body.getChildreans().size() > 0) {
                Element sdo = ele;
                if (body.getType() != null && body.getType().equalsIgnoreCase("array")) {
                    ele.addAttribute("type", "array");
                    sdo = ele.addElement(prefix + "sdo");

                }
                generatorFile(body, sdo);
            } else {
                String metadataId = body.getMetadataId();
                if (metadataId == null || "".equals(metadataId)) {
                    metadataId = body.getNodeName();
                }
                ele.addAttribute("metadataid", metadataId);
                ele.addAttribute("chinese_name", body.getChineseName());
            }
        }
    }

    public static void generatorFile(List<Ida> idas, Element root, String type, SDAService sdaService,String serviceId ,String operationId) {
        if("request".equals(type)){
            for (Ida ida : idas) {
                String prefix = "";

                if (root.getNamespace() != null && !"".equals(root.getNamespace().getPrefix())) {
                    prefix = root.getNamespace().getPrefix() + ":";
                }
                if(ida.getStructName()==null) continue;
                if (!ida.getStructName().equals("root") && !ida.getStructName().equals("request") && !ida.getStructName().equals("response")) {
                    if(null == ida.getMetadataId()){
                        root.addComment(ida.getStructAlias());
                        Element ele = root.addElement(prefix + "item");
                        ele.addAttribute("name","BODY."+ida.getStructName());
                        ele.setText("\"\"");
                    }else{
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("serviceId",serviceId);
                        map.put("metadataId",ida.getMetadataId());
                        map.put("operationId",operationId);
                        SDA sda = ((List<SDA>)sdaService.findBy(map)).get(0);

                        root.addComment(ida.getStructAlias());
                        Element ele = root.addElement(prefix + "item");
                        if(null != sda.getConstraint()){
                            ele.addAttribute("name",sda.getConstraint()+"."+ida.getStructName());
                        }else{
                            ele.addAttribute("name","BODY."+ida.getStructName());
                        }
                        ele.setText("\"\"");
                        ele = root.addElement(prefix + "item");
                        if(null != sda.getConstraint()){
                            ele.addAttribute("name",sda.getConstraint()+"."+ida.getStructName());
                            ele.setText("in."+sda.getConstraint()+"." + ida.getMetadataId());
                        }else{
                            ele.addAttribute("name","BODY."+ida.getStructName());
                            ele.setText("in.BODY." + ida.getMetadataId());
                        }
                    }

                }
            }
        }else if("response".equals(type)){
            for (Ida ida : idas) {
                String prefix = "";

                if (root.getNamespace() != null && !"".equals(root.getNamespace().getPrefix())) {
                    prefix = root.getNamespace().getPrefix() + ":";
                }
                if(ida.getStructName() == null) continue;
                if (!ida.getStructName().equals("root") && !ida.getStructName().equals("request") && !ida.getStructName().equals("response")) {
                    if(null == ida.getMetadataId()){
                        root.addComment(ida.getStructAlias());
                        Element ele = root.addElement(prefix + "item");
                        ele.addAttribute("name","BODY."+ida.getMetadataId());
                        ele.setText("\"\"");
                    }else{
//                        SDA sda = sdaService.getById(ida.getMetadataId());
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("serviceId",serviceId);
                        map.put("metadataId",ida.getMetadataId());
                        map.put("operationId",operationId);
                        SDA sda = ((List<SDA>)sdaService.findBy(map)).get(0);
                        root.addComment(ida.getStructAlias());
                        Element ele = root.addElement(prefix + "item");
                        if(null != sda.getConstraint()){
                            ele.addAttribute("name",sda.getConstraint()+"."+ida.getMetadataId());
                        }else{
                            ele.addAttribute("name","BODY."+ida.getMetadataId());
                        }
                        ele.setText("\"\"");
                        ele = root.addElement(prefix + "item");
                        if(null != sda.getConstraint()){
                            ele.addAttribute("name",sda.getConstraint()+"."+ida.getMetadataId());
                            ele.setText("in."+sda.getConstraint()+"." + ida.getStructName());
                        }else{
                            ele.addAttribute("name","BODY."+ida.getMetadataId());
                            ele.setText("in.BODY." + ida.getStructName());
                        }
                    }

                }
            }
        }

    }

    public static String generatorServiceDefineSOAP(List<SDA> sdas, SDA sda) {
        MetadataNode esbBody = new MetadataNode();
        esbBody.setNodeId(sda.getId());
        esbBody.setNodeName(sda.getStructName());
        esbBody = ExportUtil.recursionFindSDA(sdas, sda, esbBody);

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");
        Element element = doc.addElement("root");

        element.addNamespace(Contants.NAME_SPACE, Contants.NAME_SPACE_URL);
        Element SvcBody = element.addElement(Contants.NAME_SPACE+":SvcBody");
        ExportUtil.generatorFile(esbBody, SvcBody);

        return SvcBody.asXML().replace("xmlns:" + Contants.NAME_SPACE + "=" + Contants.NAME_SPACE_URL+"\"", "");
    }

    public static String generatorServiceDefineXML(List<SDA> sdas, SDA sda) {
        MetadataNode esbBody = new MetadataNode();
        esbBody.setNodeId(sda.getId());
        esbBody.setNodeName(sda.getStructName());
        esbBody = ExportUtil.recursionFindSDA(sdas, sda, esbBody);

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");
        Element element = doc.addElement("BODY");
        ExportUtil.generatorFile(esbBody, element);

        String xml = doc.asXML();

        return xml.substring(xml.indexOf("<BODY>"));
    }

    public static String generatorIdentifyOutXML(String systemAb,String interfaceName,String ecode, String serviceId, String operationId) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");

        Element element = null;
        String body = "";

        element = doc.addElement("system");
        element.addComment(interfaceName);
        element.addAttribute("id", systemAb);
        element.addAttribute("adapter-id",systemAb+"Adapter");

        Element ele1 = element.addElement("service");
        ele1.addAttribute("id", serviceId + operationId);
        ele1.addAttribute("mapping-id",ecode);
        ele1.addAttribute("package-id",ecode);

        body = "<system";

        String xml = doc.asXML();

        return xml.substring(xml.indexOf(body));
    }



    public static String generatorIdentifyInXML(String systemAb,String serviceName,String systemId, String serviceId, String operationId) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");

        Element element = null;
        String body = "";

        element = doc.addElement("channel-config");
        element.addAttribute("channel", systemAb);
        element.addAttribute("encoding", "UTF-8");

        Element ele1 = element.addElement("message-head");
        ele1.addAttribute("position", "0");
        ele1.addAttribute("length", "0");
        ele1.addAttribute("remove", "false");

        element.addComment(serviceName);
        Element ele2 = element.addElement("tran");
        ele2.addAttribute("id", serviceId + operationId);
        ele2.addAttribute("mapping-id","");
        ele2.addAttribute("service-id",serviceId+operationId);
        ele2.addAttribute("encoding","UTF-8");

        Element ele3 = ele2.addElement("condition");
        ele3.addAttribute("position", "SYS_HEAD.SERVICE_CODE");
        ele3.addAttribute("length", "11");
        ele3.addAttribute("value", serviceId);

        Element ele4 = ele2.addElement("condition");
        ele4.addAttribute("position", "SYS_HEAD.SERVICE_SCENE");
        ele4.addAttribute("length", "2");
        ele4.addAttribute("value", operationId);

        Element ele5 = ele2.addElement("condition");
        ele5.addAttribute("position", "SYS_HEAD.CONSUMER_ID");
        ele5.addAttribute("length", "6");
        ele5.addAttribute("value", systemId);

        body = "<channel-config";

        String xml = doc.asXML();

        return xml.substring(xml.indexOf(body));
    }

    public static String generateBaseMappingXML(List<Ida> idas,String type,String systemAb,SDAService sdaService, String serviceId, String operationId){
        Collections.sort(idas, new Comparator<Ida>() {
            @Override
            public int compare(Ida o1, Ida o2) {
                return (""+o1.getSeq()).compareTo(""+o2.getSeq());
            }
        });
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");

        Element element = doc.addElement("root");
        for(Ida ida : idas){
            Element nodeElement = element.addElement(ida.getStructName());
            nodeElement.addAttribute("type", ida.getType());
            nodeElement.addAttribute("length", ida.getLength());
            nodeElement.addAttribute("metadataid", ida.getMetadataId());
        }
        String xml = doc.asXML();
        return xml;
    }

    public static String generateBaseMappingXML(List<SDA> sdas){
        Collections.sort(sdas, new Comparator<SDA>() {
            @Override
            public int compare(SDA o1, SDA o2) {
                return (""+o1.getSeq()).compareTo(""+o2.getSeq());
            }
        });
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");

        Element element = doc.addElement("root");
        for(SDA sda : sdas){
            Element nodeElement = element.addElement(sda.getStructName());
//            nodeElement.addAttribute("type", sda.getType());
//            nodeElement.addAttribute("length", sda.getLength());
            nodeElement.addAttribute("metadataid", sda.getMetadataId());
        }
        String xml = doc.asXML();
        return xml;
    }

    public static String generatorMappingXML(List<Ida> idas,String type,String systemAb,SDAService sdaService, String serviceId, String operationId) {
        Collections.sort(idas, new Comparator<Ida>() {
            @Override
            public int compare(Ida o1, Ida o2) {
                return (""+o1.getSeq()).compareTo(""+o2.getSeq());
            }
        });
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");


        Element element = null;
        String body = "";
        if("request".equalsIgnoreCase(type)){
            //req
            element = doc.addElement("request-mapping");
            element.addAttribute("base","default");
            element.addAttribute("dict",systemAb);

            Element ele = element.addElement("request-head");
            ele.addAttribute("channel", systemAb);

            body = "<request-mapping";
        }else if("response".equalsIgnoreCase(type) ){
            //res
            element = doc.addElement("response-mapping");
            element.addAttribute("base","default");
            element.addAttribute("dict",systemAb);

            Element ele = element.addElement("response-head");
            ele.addAttribute("channel",systemAb);

            body = "<response-mapping";
        }
        ExportUtil.generatorFile(idas, element, type ,sdaService , serviceId ,operationId);

        String xml = doc.asXML();

        return xml.substring(xml.indexOf(body));
    }

    public static void main(String[] a) {
        ClassLoader loader = ExportUtil.class.getClassLoader();
        String path = loader.getResource("template/in_config/channel_service_template_soap.xml").getPath();
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new File(path));
            Element root = document.getRootElement();

//            List list =
            List<Namespace> ss = root.additionalNamespaces();
            for (Namespace s : ss) {

                System.out.println(s.asXML());
            }
        } catch (Exception e) {
        }
    }
}
