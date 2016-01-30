package com.dc.esb.servicegov.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.IOP.ENCODING_CDR_ENCAPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.util.TreeNode;

@Controller
@RequestMapping("sda")
public class SDAController {
	@Autowired
	private SystemLogServiceImpl systemLogService;

	@Autowired
	SDAServiceImpl serviceImpl;

	@RequiresPermissions({"service-get"})
	//根据serviceId,operationId获取服务数据协议信息
	@RequestMapping("/sdaPage")
	public ModelAndView sdaPage(String operationId, String serviceId, HttpServletRequest req){
		return serviceImpl.sdaPage(operationId, serviceId, req);
	}

//	@RequiresPermissions({"service-get"})
	@RequiresPermissions({"sda-get"})
	//根据serviceId，operationId获取sda树
	@RequestMapping("/sdaTree")
	@ResponseBody
	public List<TreeNode> getSDATree(String serviceId, String operationId){
		return serviceImpl.genderSDATree(serviceId, operationId);
	}
	@RequiresPermissions({"sda-get"})
	//根据serviceId，operationId获取sda树
	@RequestMapping("/headSdaTree")
	@ResponseBody
	public List<TreeNode> getSDATree3(String headId){
		return serviceImpl.genderSDATree(headId);
	}

	@RequiresPermissions({"sda-get"})
	//根据serviceId，operationId获取sda树
	@RequestMapping("/sdaComboTree")
	@ResponseBody
	public List<TreeNode> getSDATree2(String serviceId, String operationId){
		return serviceImpl.genderSDATree2(serviceId, operationId);
	}

	@RequiresPermissions({"sda-get"})
	//根据serviceId，operationId获取sda树
	@RequestMapping("/headSdaComboTree")
	@ResponseBody
	public List<TreeNode> getSDATree2(String headId){
		return serviceImpl.genderSDATree2(headId);
	}

	@RequiresPermissions({"sda-get"})
	//生成一个sdaId
	@RequestMapping("/genderSDAUuid")
	@ResponseBody
	public String genderSDAUuid(){
		String result = UUID.randomUUID().toString();
		return result;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sda-update"})
	//保存对象数组
	@RequestMapping(method = RequestMethod.POST, value = "/saveSDA", headers = "Accept=application/json")
	@ResponseBody
	public boolean saveSDA(@RequestBody SDA[] sdas){
		OperationLog operationLog = systemLogService.record("SDA", "批量保存", "");
		//判断场景状态是否为服务定义或修订
		boolean canModifyOperation = serviceImpl.judgeCanModifyOperation(sdas[0].getServiceId(), sdas[0].getOperationId());
		if(!canModifyOperation){
			return false;
		}
		String logParam = serviceImpl.save(sdas);

		operationLog.setParams(logParam);
		systemLogService.updateResult(operationLog);
		return true;
	}
	@RequiresPermissions({"sda-update"})
	//保存对象数组
	@RequestMapping(method = RequestMethod.POST, value = "/commonSaveSDA", headers = "Accept=application/json")
	@ResponseBody
	public boolean commonSaveSDA(@RequestBody SDA[] sdas){
		OperationLog operationLog = systemLogService.record("SDA", "报文头SDA批量保存", "数量：" + sdas.length);

		String logParam = serviceImpl.saveHeadSDA(sdas);

		operationLog.setParams(logParam);
		systemLogService.updateResult(operationLog);
		return true;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sda-delete"})
	//删除数据
	@RequestMapping(method = RequestMethod.POST, value = "/deleteSDA", headers = "Accept=application/json")
	@ResponseBody
	public boolean deleteSDA(@RequestBody String[] delIds){
		OperationLog operationLog = systemLogService.record("SDA", "批量删除", "数量：" + delIds.length);

		String logParam = serviceImpl.delete(delIds);

		operationLog.setParams(logParam);
		systemLogService.updateResult(operationLog);
		return true;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sda-update"})
	@RequestMapping("/moveUp")
	@ResponseBody
	public boolean moveUp(String id){
		OperationLog operationLog = systemLogService.record("SDA","元素上移","");
		SDA entity = serviceImpl.findUniqueBy("id", id);
		if(entity != null){
			operationLog.setParams("SDA:" + entity.getStructName());
		}
		boolean result = serviceImpl.moveUp(id);

		systemLogService.updateResult(operationLog);
		return result;
	}

//	@RequiresPermissions({"service-update"})
	@RequiresPermissions({"sda-update"})
	@RequestMapping("/moveDown")
	@ResponseBody
	public boolean moveDown(String id){
		OperationLog operationLog = systemLogService.record("SDA","元素下移","元素ID:" + id);
		SDA entity = serviceImpl.findUniqueBy("id", id);
		if(entity != null){
			operationLog.setParams("SDA:" + entity.getStructName());
		}

		boolean result = serviceImpl.moveDown(id);

		systemLogService.updateResult(operationLog);
		return result;
	}

	/**
	 * 查询sda的子节点中是否含有metadataId的元素
	 * @param parentId sda的id
	 * @param metadataId 元数据id
	 */
	@RequiresPermissions({"sda-get"})
	@RequestMapping("getChildByMetadataId")
	@ResponseBody
	public SDA getChildByMetadataId(String parentId, String metadataId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentId", parentId);
		params.put("metadataId", metadataId);
		SDA sda  = serviceImpl.findUniqueBy(params);
		return sda;
	}
	@RequiresPermissions({"sda-get"})
	//根据serviceId，operationId获取sda树
	@RequestMapping("/querySDATree")
	@ResponseBody
	public List<TreeNode> querySDATree(String serviceId, String operationId, String keyword){
		return serviceImpl.querySDATree(serviceId, operationId, keyword);
	}

	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}
}
