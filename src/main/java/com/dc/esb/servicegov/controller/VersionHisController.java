package com.dc.esb.servicegov.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.OperationHisServiceImpl;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dc.esb.servicegov.service.impl.VersionHisServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/versionHis")
public class VersionHisController {
	@Autowired
	private VersionHisServiceImpl versionHisServiceImpl;
	@Autowired
	private OperationServiceImpl operationService;
	@Autowired
	private ServiceServiceImpl serviceService;
	@Autowired
	private OperationHisServiceImpl operationHisService;

	@RequiresPermissions({"versionHis-get"})
	@RequestMapping("/hisVersionList")
	@ResponseBody
	public Map<String, Object> hisVersionList(String keyValue, String startDate,String endDate,HttpServletRequest req) {
		if(null == keyValue){
			keyValue = "";
		}
		try{
			keyValue = URLDecoder.decode(keyValue, "utf-8");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

		int pageNo = Integer.parseInt(req.getParameter("page"));
		int rowCount = Integer.parseInt(req.getParameter("rows"));
//		Page page = versionHisServiceImpl.getAll(rowCount);

		Map<String, Object> result = new HashMap<String, Object>();

		String hql = " from OperationHis oh left join oh.versionHis where 1=1 ";
		if(StringUtils.isNotEmpty(keyValue)){
			hql += " and oh.operationId like '%"+keyValue+"%' or oh.operationName like '%"+keyValue+"%'" + " or oh.serviceId like '%"+keyValue+"%'";
		}
		if(StringUtils.isNotEmpty(startDate)){
			hql += " and oh.optDate >'" + startDate + "' ";
		}
		if(StringUtils.isNotEmpty(endDate)){
			hql += " and oh.optDate <'" + endDate + " 23:59:59' ";
		}
		hql += " order by oh.versionHis.optDate desc";

//		String hql = " from VersionHis v left join v.operationHis";
//		if(StringUtils.isNotEmpty(keyValue)){
////			hql += " where code like '%"+keyValue+"%' or versionDesc like '%"+keyValue+"%' or remark like '%"+keyValue+"%' order by optDate desc";
//			hql += " where v.operationHis.operationId like '%"+keyValue+"%' or v.operationHis.operationName like '%"+keyValue+"%'";
//			if ("".equals(serviceStr)) {
//				hql += " order by v.optDate desc";
//			}else{
//				hql += " or v.operationHis.serviceId in ("+serviceStr+") order by v.optDate desc";
//			}
//		}else {
//			hql += " order by v.optDate desc";
//		}
		Page page = versionHisServiceImpl.getPageBy(hql,rowCount);
		page.setPage(pageNo);
		List<VersionHisServiceImpl.VersionHisBean> rows = versionHisServiceImpl.findVersionBeanBy(hql, page);
		result.put("total", page.getResultCount());
		result.put("rows", rows);
		return result;
	}

	@RequiresPermissions({"versionHis-get"})
	@RequestMapping("/hisDetailPage")
	public ModelAndView hisDetailPage(String autoId) {
		ModelAndView mv = new ModelAndView("version/versionHisDetail");
		OperationHis operationHis = operationHisService.findUniqueBy("autoId", autoId);
		if(operationHis != null){
			mv.addObject("operationHis", operationHis);
		}
		Service service = operationHis.getService();
		if(null != service){
			mv.addObject("service",service);
		}
		return mv;
	}
	@RequiresPermissions({"versionHis-get"})
	@RequestMapping("/judgeVersionHis")
	@ResponseBody
	public VersionHis judgeVersionHis(String versionId){
		String hql = " from " + VersionHis.class.getName() + " where id=? order by code desc";
		List<VersionHis> list = versionHisServiceImpl.find(hql, versionId);
		if(list.size() > 0){
			return list.get(0);
		}
		return new VersionHis();
	}
	@RequiresPermissions({"versionHis-get"})
	@RequestMapping("/judgeVersionPre")
	@ResponseBody
	public VersionHis getPreVersion(String autoId){//根据一个历史版本的autoId，查询前一个版本
		VersionHis	result = versionHisServiceImpl.getPreVersion(autoId);
		return result;
	}
}
