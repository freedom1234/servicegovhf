package com.dc.esb.servicegov.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.service.IdaService;
import com.dc.esb.servicegov.service.InterfaceHeadService;
import com.dc.esb.servicegov.util.TreeNode;

@Controller
@RequestMapping("/interfaceHead")
public class InterfaceHeadController {
	@Autowired
	private SystemLogServiceImpl systemLogService;
	@Autowired
	private InterfaceHeadService interfaceHeadService;
	
	@Autowired
	private IdaService idaService;
	@Autowired
	private SDAServiceImpl sdaService;

	@RequiresPermissions({"system-get"})
	@RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
	public @ResponseBody
	List<TreeNode> getAll() {
		List<InterfaceHead> heads = interfaceHeadService.getAll();

		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode();
		root.setId("root");
		root.setText("报文头管理");
		for (InterfaceHead head : heads) {
			TreeNode tree = new TreeNode();
			tree.setId(head.getHeadId());
			tree.setText(head.getHeadName());
			trees.add(tree);
		}
		root.setChildren(trees);
		List<TreeNode> rootList = new ArrayList<TreeNode>();
		rootList.add(root);
		return rootList;
	}

//	@RequiresPermissions({"system-add"})
	@RequiresPermissions({"interfaceHead-add"})
	@RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
	public @ResponseBody
	boolean save(@RequestBody
	InterfaceHead head) {
		OperationLog operationLog = systemLogService.record("报文头", "保存", "名称：" + head.getHeadName());

		boolean add = false;
		if(head.getHeadId()==null || "".equals(head.getHeadId())){
			add = true;
		}
		interfaceHeadService.save(head);
		
		//添加报文，自动生成固定报文头<root><request><response>
		if(add){
			Map<String, SDA> map = sdaService.genderSDAAuto(head.getHeadId());
			//root
			Ida ida = new Ida();
			ida.setHeadId(head.getHeadId());
			ida.setParentId(null);
			ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
			ida.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
			ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
			ida.setState(Constants.IDA_STATE_COMMON);
			ida.setSdaId(map.get("root").getId());
			idaService.save(ida);
			String parentId = ida.getId();
			
			ida = new Ida();
			ida.setHeadId(head.getHeadId());
			ida.setParentId(parentId);
			ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
			ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
			ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
			ida.setState(Constants.IDA_STATE_COMMON);
			ida.setSeq(0);
			ida.setSdaId(map.get("request").getId());
			idaService.save(ida);
			
			ida = new Ida();
			ida.setHeadId(head.getHeadId());
			ida.setParentId(parentId);
			ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
			ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
			ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
			ida.setState(Constants.IDA_STATE_COMMON);
			ida.setSeq(1);ida.setSdaId(map.get("response").getId());
			idaService.save(ida);
		}

		systemLogService.updateResult(operationLog);
		return true;
	}

//	@RequiresPermissions({"system-update"})
//	@RequiresPermissions({"interfaceHead-update"})
	@RequestMapping(method = RequestMethod.GET, value = "/edit/{headId}", headers = "Accept=application/json")
	public ModelAndView getInterfaceHead(@PathVariable
	String headId) {

		InterfaceHead head = interfaceHeadService.getById(headId);
		ModelAndView modelAndView = new ModelAndView();  
        modelAndView.addObject("head", head);  
        modelAndView.setViewName("sysadmin/header_edit");  
		return modelAndView;
	}

//	@RequiresPermissions({"system-delete"})
	@RequiresPermissions({"interfaceHead-delete"})
	@RequestMapping(method = RequestMethod.GET, value = "/delete/{headId}", headers = "Accept=application/json")
	public @ResponseBody
	boolean delete(@PathVariable
			String headId) {
		OperationLog operationLog = systemLogService.record("报文头","删除","报文头ID:" + headId);

		interfaceHeadService.deleteById(headId);

		systemLogService.updateResult(operationLog);
		return true;
	}
	/**
	 * 报文头headName唯一性验证
	 *
	 * @param headName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/uniqueValid", headers = "Accept=application/json")
	public
	@ResponseBody
	boolean uniqueValid(String headName) {
		return interfaceHeadService.uniqueValid(headName);
	}
	@ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
	public String processUnauthorizedException() {
		return "403";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/sdaPage", headers = "Accept=application/json")
	public ModelAndView sdaPage(String headId) {
		ModelAndView mv = new ModelAndView("sysadmin/interface_head_sda");
		InterfaceHead head = interfaceHeadService.findUniqueBy("headId", headId);
		mv.addObject("head", head);

		return mv;
	}
}
