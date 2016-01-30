package com.dc.esb.servicegov.util;

import java.util.HashMap;
import java.util.Map;

public class GlobalMenuId {
	
	public final static String resourceImportMenuId ="resourceImportMenuId";
	
	public static Map<String, String> menuIdMap = new HashMap<String, String>();

	public static void saveMenuId(String id, String menuId) {
		if (!menuIdMap.containsKey(id)) {
			menuIdMap.put(id, menuId);
		}
	}

	public static String getMenuId(String id) {
			return menuIdMap.get(id);
	}
}
