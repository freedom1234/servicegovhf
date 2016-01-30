package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.ServiceHead;
import com.dc.esb.servicegov.service.impl.ServiceHeadSdaServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceHeadServiceImpl;
import com.dc.esb.servicegov.util.TreeNode;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
@Controller
@RequestMapping("/serviceHeadSda")
public class ServiceHeadSdaController {
    @Autowired
    private ServiceHeadServiceImpl serviceHeadService;
    @Autowired
    private ServiceHeadSdaServiceImpl serviceHeadSdaService;

    @RequiresPermissions({"serviceHead-get"})
    @RequestMapping("/sdaPage")
    public ModelAndView sdaPage(String serviceHeadId){

        ModelAndView mv = new ModelAndView("service/service_head/service_head_sda");
        ServiceHead entity = serviceHeadService.findUniqueBy("headId", serviceHeadId);
        mv.addObject("entity", entity);

        return mv;
    }

    @RequiresPermissions({"serviceHead-get"})
    @RequestMapping("/sdaTree")
    @ResponseBody
    public List<TreeNode> getSDATree(String serviceHeadId){
        return serviceHeadSdaService.genderSDATree(serviceHeadId);
    }
}
