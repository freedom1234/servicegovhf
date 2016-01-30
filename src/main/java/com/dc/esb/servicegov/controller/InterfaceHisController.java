package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.InterfaceHIS;
import com.dc.esb.servicegov.service.impl.InterfaceHISServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/9/14.
 */

@Controller
@RequestMapping("/interfaceHis")
public class InterfaceHisController {
    @Autowired
    private InterfaceHISServiceImpl interfaceHISService;

    @RequestMapping("/history/{interfaceId}")
    @ResponseBody
    public Map<String, Object> getInterfaceHistory(@PathVariable String interfaceId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<InterfaceHIS> list = interfaceHISService.getHis(interfaceId);

        result.put("total", list.size());
        result.put("rows", list);
        return result;
    }
}
