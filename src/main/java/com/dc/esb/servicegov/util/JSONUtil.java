package com.dc.esb.servicegov.util;

import com.dc.esb.servicegov.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.drools.core.util.StringUtils;

import java.lang.System;
import java.util.List;

public class JSONUtil {
	private static JSONUtil interfaceObj;
	public static JSONUtil getInterface(){
		if(interfaceObj == null){
			interfaceObj = new JSONUtil();
		}
		return interfaceObj;
	}

	public static JsonConfig genderJsonConfig(String[] names){
		JsonConfig config = new JsonConfig();  //过滤属性  
	    config.setIgnoreDefaultExcludes(false);       
	    config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); 
	    final  String[] fnames = names;
		config.setExcludes(new String[]{"handler", "hibernateLazyInitializer"});
	    config.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source/* 属性的拥有者 */, String name /*属性名字*/, Object value/* 属性值 */) {
				if (fnames != null && fnames.length > 0) {
					for (String fname : fnames) {
						if (name.equals(fname)) {
							return false;
						}
					}
				}

				return true;
			}
		});

	    return config;
	}
	public JSONArray convert(List rows, String[] fields){
		JsonConfig serviceInvokeJC = genderJsonConfig(fields);
		JSONArray ja = JSONArray.fromObject(rows, serviceInvokeJC);

		return ja;
	}
}
