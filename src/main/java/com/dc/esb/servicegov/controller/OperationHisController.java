package com.dc.esb.servicegov.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.entity.OperationHis;
import com.dc.esb.servicegov.service.impl.OperationHisServiceImpl;

@Controller
@RequestMapping("/operationHis")
public class OperationHisController {
	@Autowired
	private OperationHisServiceImpl operationHisServiceImpl;

	@RequiresPermissions({"service-get"})
	@RequestMapping("/hisPage")
	public ModelAndView hisPage(HttpServletRequest req, String operationId, String serviceId){
		return operationHisServiceImpl.hisPage(req, operationId, serviceId);
	}
	
	//根据服务和场景id
//	@RequiresPermissions({"service-get"})
	@RequiresPermissions({"version-get"})
	@RequestMapping("/getByOS/{serviceId}/{operationId}")
	@ResponseBody
	public Map<String, Object> getByOS(@PathVariable(value="serviceId") String serviceId, @PathVariable(value="operationId") String operationId) {
		List<?> rows = operationHisServiceImpl.getByOS(operationId, serviceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rows.size());
		result.put("rows", rows);
		return result;
	}

	@RequiresPermissions({"service-get"})
	@RequestMapping("/getByAutoId")
	@ResponseBody
	public OperationHis getByAutoId(String autoId){
		return operationHisServiceImpl.getByAutoId(autoId);
	}

	@RequiresPermissions({"baseLine-get"})
	@RequestMapping("/operationHisList")
	@ResponseBody
	public Map<String, Object> operationHisList(HttpServletRequest req) {
		int pageNo = Integer.parseInt(req.getParameter("page"));
		int rowCount = Integer.parseInt(req.getParameter("rows"));

		String serviceId = req.getParameter("serviceId");
		if(null == serviceId) serviceId = "";
		String serviceName = req.getParameter("serviceName");
		if(null != serviceName && !"".equals(serviceName)){
			try{
				serviceName = URLDecoder.decode(req.getParameter("serviceName"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			serviceName = "";
		}
		String operationId = req.getParameter("operationId");
		if(null == operationId) operationId = "";
		String operationName = req.getParameter("operationName");
		if(null != operationName && !"".equals(operationName)){
			try{
				operationName = URLDecoder.decode(req.getParameter("operationName"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			operationName = "";
		}



		String hql = "from OperationHis a where a.state = '"+Constants.Operation.OPT_STATE_PASS+"'";
		hql += " and a.service.serviceId like ? and a.service.serviceName like ?";
		hql += " and a.operationId like ? and a.operationName like ?";
		Page page = operationHisServiceImpl.getPageBy(hql, rowCount,"%"+serviceId+"%","%"+serviceName+"%","%"+operationId+"%","%"+operationName+"%");
		page.setPage(pageNo);
		Map<String, Object> result = new HashMap<String, Object>();
		hql = "from OperationHis a where a.state = '"+Constants.Operation.OPT_STATE_PASS+"'" +
				" and a.service.serviceId like ? and a.service.serviceName like ?"+
				" and a.operationId like ? and a.operationName like ?"+
				" order by a.versionHis.optDate desc";
		List<?> rows = operationHisServiceImpl.findBy(hql,page,"%"+serviceId+"%","%"+serviceName+"%","%"+operationId+"%","%"+operationName+"%");

		result.put("total", page.getResultCount());
		result.put("rows", rows);
		return result;
	}
	@RequestMapping("/hisJudge")
	@ResponseBody
	public boolean hisJudge(String serviceId, String operationId){
		return operationHisServiceImpl.hisJudge(serviceId, operationId);
	}

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
