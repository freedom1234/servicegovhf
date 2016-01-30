package com.dc.esb.servicegov.export.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.export.IConfigGenerator;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;

@Component
public class MetadataConfigGenerator implements IConfigGenerator {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(MetadataConfigGenerator.class);
	@Autowired
	private MetadataServiceImpl metadataService;

	@Override
	public File generate() {
		File metadataConfigFile = new File("metadata.xml");
		// 获取元数据
		List<Metadata> metadatas = metadataService.getAllMetadata();
		//创建文件
		//创建文件
		Document doc = DocumentHelper.createDocument();
		Element rootElement = doc.addElement("metadata");//根节点
		for (int i = 0; i < metadatas.size(); i++) {
			//子节点
			Element info = rootElement.addElement(metadatas.get(i)
					.getMetadataId());
			//向根节点下面的子节点插入属性
			String typeStr = "";
			if("array".equalsIgnoreCase(metadatas.get(i).getType())){//常熟ESB业务要求：不需要scale属性，类型除了array全部是string
				typeStr = "array";
			}else{
				typeStr = "string";
			}
			addAttribute(info, "type", typeStr);
			if (!"array".equalsIgnoreCase(metadatas.get(i).getType()) && !"struct".equalsIgnoreCase(metadatas.get(i).getType()) ) {//判断子节点是否为数组
				addAttribute(info,"length", metadatas.get(i).getLength() );
//				addAttribute(info,"chinese_name", metadatas.get(i).getChineseName());
//				addAttribute(info, "scale", metadatas.get(i).getScale());
			}
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			FileOutputStream fos = new FileOutputStream("metadata.xml");
			XMLWriter writer = new XMLWriter(fos, format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			log.error(e, e);
		}
		return metadataConfigFile;
	}

	public void addAttribute(Element element, String name, String value){
		if(StringUtils.isEmpty(value)){
			value = "";
		}
		element.addAttribute(name, value);
	}
}
