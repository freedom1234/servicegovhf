package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.MetadataTemplateDAOImpl;
import com.dc.esb.servicegov.entity.MetadataTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MetadataTemplateServiceImpl {
	@Autowired
	private MetadataTemplateDAOImpl metadataTemplateDAOImpl;
	public List<MetadataTemplate> getAllMetadataTemplate(){
		return metadataTemplateDAOImpl.getAll();
	}
	
	public List<MetadataTemplate> getById(String templateId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("templateId", templateId);
		return metadataTemplateDAOImpl.findBy(params);	
	}
	
	public List<MetadataTemplate> getByType(String type){
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		return metadataTemplateDAOImpl.findBy(params);	
	}
	
	public List<MetadataTemplate> getByLength(String length){
		Map<String, String> params = new HashMap<String, String>();
		params.put("length", length);
		return metadataTemplateDAOImpl.findBy(params);	
	}
	
	public List<MetadataTemplate> getByScale(String scale){
		Map<String, String> params = new HashMap<String, String>();
		params.put("scale", scale);
		return metadataTemplateDAOImpl.findBy(params);	
	}
	
	public boolean addMetadataTemplate(MetadataTemplate metadataTemplate){		
		metadataTemplateDAOImpl.save(metadataTemplate);
		return true;
	}
	
	public boolean modifyMetadataTemplate(MetadataTemplate metadataTemplate){
		metadataTemplateDAOImpl.save(metadataTemplate);	
		return true;
	}
	
	public void deleteMetadataTemplate(String templateId){
		metadataTemplateDAOImpl.delete(templateId);
	}
}
