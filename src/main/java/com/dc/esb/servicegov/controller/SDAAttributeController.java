package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.OperationPK;
import com.dc.esb.servicegov.entity.SDAAttribute;
import com.dc.esb.servicegov.service.impl.SDAAttrbuteServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/1/4.
 */

@Controller
@RequestMapping("sdaAttribute")
public class SDAAttributeController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    SDAAttrbuteServiceImpl sdaAttrbuteService;

    @RequestMapping(value="/add", headers = "Accept=application/json")
    @ResponseBody
    public  boolean add(@RequestBody SDAAttribute sdaAttribute){
        OperationLog operationLog = systemLogService.record("SDA属性","添加","属性值：" + sdaAttribute.getValue());

        sdaAttrbuteService.save(sdaAttribute);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequestMapping(value="/getBySDAId")
    @ResponseBody
    public List<SDAAttribute> getBySDAId(String sdaId){
        List<SDAAttribute> result = sdaAttrbuteService.findBy("sdaId", sdaId);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deletes", headers = "Accept=application/json")
    @ResponseBody
    public boolean deletes(@RequestBody String[] attrIds) {
        OperationLog operationLog = systemLogService.record("SDA属性","删除","数量：" + attrIds.length);

        for(String attrId : attrIds){
            sdaAttrbuteService.deleteById(attrId);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }
}
