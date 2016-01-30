package com.dc.esb.servicegov.export.common;

import com.dc.esb.servicegov.export.IExportableNode;
import com.dc.esb.servicegov.export.IPackerParserConfigGenerator;
import com.dc.esb.servicegov.export.exception.ExportException;
import com.dc.esb.servicegov.export.util.FileUtil;
import com.dc.esb.servicegov.export.util.XMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vincentfxz on 15/11/24.
 */
public abstract class AbstractPackerParserConfigGenerator implements IPackerParserConfigGenerator {

    private static final Log log = LogFactory.getLog(AbstractPackerParserConfigGenerator.class);

    public String getTemplatePath() {
        return getMsgType() + "_template";
    }

    public String getConfigFolder(String type) {
        String configFolder = null;

        if ("provider".equalsIgnoreCase(type)) {
            configFolder = "out_config";
        } else if ("consumer".equalsIgnoreCase(type)) {
            configFolder = "in_config";
        } else {
            log.error("无法获取拆包包文件文件夹,因为类型[" + type + "]不存在,期望的类型为[consumer/provider]");
        }
        return configFolder;
    }

    /**
     * 生成XML的拆组包文件
     *
     * @param bodyNodeList export node 比如 sda 或者Ida
     * @param serviceId    服务Id
     * @param operationId  场景ID
     * @param systemAb     系统简称
     * @param type         类型，是提供方／消费方 provider/consumer
     * @return
     */
    @Override
    public synchronized List<File> generate(List<List<? extends IExportableNode>> headNodeList, List<? extends IExportableNode> bodyNodeList, String serviceId,
                                            String operationId, String systemAb, String type) throws ExportException {

        log.info("开始导出拆组包配置,服务[" + serviceId + "]场景[" + operationId + "],系统[" + systemAb + "],类型[" + type + "]," +
                "报文类型[" + getMsgType() + "],使用模版[" + getTemplatePath() + "]");

        ClassLoader loader = this.getClass().getClassLoader();
        List<File> resultFiles = new ArrayList<File>();
        String configFolder = getConfigFolder(type);
        String templatePath = getTemplatePath();
        templatePath = loader.getResource("").getPath() + "/" + templatePath + "/" + configFolder;
        String destPath = loader.getResource("").getPath() + "/generator/" + serviceId + operationId;
        File resultDir = new File(destPath);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        } else {
            resultDir.delete();
        }
        resultFiles.add(resultDir);

        destPath = destPath + "/" + configFolder + "/metadata";
        File destDir = new File(destPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        } else {
            destDir.delete();
        }

        String reqContent = composeContent(bodyNodeList, "request");
        String respContent = composeContent(bodyNodeList, "response");
        //在request中，加 bean、page节点
        int i = reqContent.indexOf("pageSize");
        int j = reqContent.indexOf("currentPage");
        if (i > 0 && j > 0) {
            reqContent = "<Bean>\n" + reqContent.substring(0, (i < j ? i : j) - 1) +
                    "\n </Bean>\n<Page>\n " + reqContent.substring((i < j ? i : j) - 1) + "\n </Page>";
        }


        String reqHeadContent = "";
        String respHeadContent = "";
        for (List<? extends IExportableNode> headNodes : headNodeList) {
            reqHeadContent += composeContent(headNodes, "request");
            respHeadContent += composeContent(headNodes, "response");
        }

        File templateDir = new File(templatePath);
        if (templateDir.isDirectory()) {
            File[] configFiles = templateDir.listFiles();
            if (null != configFiles) {
                for (File configFile : configFiles) {
                    String fileName = configFile.getName();
                    fileName = fileName.replace("serviceId", serviceId + operationId);
                    fileName = fileName.replace("systemAb", systemAb);
                    String fileContent = FileUtil.readFile(configFile);
                    fileContent = fileContent.replace("${reqHead}$", reqHeadContent);
                    fileContent = fileContent.replace("${request}$", reqContent);
                    fileContent = fileContent.replace("${rspHead}$", respHeadContent);
                    fileContent = fileContent.replace("${response}$", respContent);
                    fileContent = XMLUtil.formatXml(fileContent);
                    String targetFilePath = destPath + "/" + fileName;
                    FileUtil.writeFile(fileContent, targetFilePath);
                }
            }
        }
        return resultFiles;
    }

    protected Map<String, List<IExportableNode>> flat2Hierarchy(List<? extends IExportableNode> nodes) {
        Map<String, List<IExportableNode>> arrayHierarchy = new HashMap<String, List<IExportableNode>>();
        for (IExportableNode node : nodes) {
            String nodeId = node.getId();
            if (!arrayHierarchy.containsKey(nodeId)) {
                arrayHierarchy.put(nodeId, new ArrayList<IExportableNode>());
            }
            String parentId = node.getParentId();
            if (arrayHierarchy.containsKey(parentId)) {
                List<IExportableNode> arrayNodes = arrayHierarchy.get(parentId);
                arrayNodes.add(node);
                arrayHierarchy.put(parentId, arrayNodes);
            }
        }
        return arrayHierarchy;
    }

    protected String composeLine(IExportableNode node) {
        StringBuilder sb = new StringBuilder();
        if (null != node.getStructName() && !"".equalsIgnoreCase(node.getStructName().trim())) {
            sb.append("<");
            sb.append(node.getStructName());
            sb.append(" ");
            String metadataId = node.getMetadataId();
            if (null != metadataId && !"".equalsIgnoreCase(metadataId.trim())) {
                sb.append("metadataid=\"" + node.getMetadataId() + "\"");
                sb.append(" ");
            }
            // kongxfa modify 2015年12月25日 14:15:50
//            String type = node.getType();
//            if (null != type && !"".equalsIgnoreCase(type.trim())) {
//                sb.append("type=\"" + node.getType() + "\"");
//                sb.append(" ");
//            }
//            String length = node.getLength();
//            if (null != length && !"".equalsIgnoreCase(length.trim())) {
//                sb.append("length=\"" + node.getLength() + "\"");
//                sb.append(" ");
//            }
            sb.append("/>\n");
        }
        return sb.toString();
    }

    protected String composeContent(List<? extends IExportableNode> nodeList, String rootStructName) {
        StringBuilder contentBuilder = new StringBuilder();
        Map<String, List<IExportableNode>> hierarchy = flat2Hierarchy(nodeList);
        String rootId = null;
        for (IExportableNode node : nodeList) {
            if (rootStructName.equalsIgnoreCase(node.getStructName())) {
                rootId = node.getId();
            }
        }
        if (null == rootId) {
            log.error("找不到节点[" + rootStructName + "]");
        } else {
            List<IExportableNode> rootChildren = hierarchy.get(rootId);
            contentBuilder.append(composeContent(rootChildren, hierarchy));
        }
        return contentBuilder.toString();
    }

    protected String composeContent(List<IExportableNode> nodeList, Map<String, List<IExportableNode>> hierarchy) {
        StringBuilder contentBuilder = new StringBuilder();
        for (IExportableNode node : nodeList) {
            String type = node.getType();
            if (type.equalsIgnoreCase("array")) {
                List<IExportableNode> childNode = hierarchy.get(node.getId());
                contentBuilder.append("<");
                contentBuilder.append(node.getStructName());
                contentBuilder.append(" ");
                contentBuilder.append("type=\"array\"");
                contentBuilder.append(" ");
                contentBuilder.append("is_struct=\"false\""); //add 2015年12月28日 15:38:06
                contentBuilder.append(" ");
                contentBuilder.append("metadataida=\"");
                contentBuilder.append(node.getMetadataId());
                contentBuilder.append("\">\n");
                contentBuilder.append(composeContent(childNode, hierarchy));
                contentBuilder.append("</");
                contentBuilder.append(node.getStructName());
                contentBuilder.append(">\n");
            } else {
                contentBuilder.append(composeLine(node));
            }
        }
        return contentBuilder.toString();
    }


    public abstract String getMsgType();


}
