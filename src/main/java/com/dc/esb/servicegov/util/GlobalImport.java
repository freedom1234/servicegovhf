package com.dc.esb.servicegov.util;

import com.dc.esb.servicegov.entity.Interface;
import com.dc.esb.servicegov.entity.InterfaceHead;

import java.util.HashMap;
import java.util.Map;

public class GlobalImport {

	public static boolean flag = true;
    public static boolean operateFlag;

    //导入
    public static Map<String,InterfaceHead> headMap = new HashMap<String,InterfaceHead>();
}