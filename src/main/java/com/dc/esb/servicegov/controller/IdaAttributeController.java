package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.IdaAttribute;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.SDAAttribute;
import com.dc.esb.servicegov.service.impl.IdaAttrbuteServiceImpl;
import com.dc.esb.servicegov.service.impl.SDAAttrbuteServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */

@Controller
@RequestMapping("idaAttribute")
public class IdaAttributeController {
    @Autowired
    private SystemLogServiceImpl systemLogService;
    @Autowired
    IdaAttrbuteServiceImpl idaAttrbuteService;

    @RequestMapping(value="/add", headers = "Accept=application/json")
    @ResponseBody
    public  boolean add(@RequestBody IdaAttribute idaAttribute){
        OperationLog operationLog = systemLogService.record("Ida属性","添加","属性值：" + idaAttribute.getValue());

        idaAttrbuteService.save(idaAttribute);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequestMapping(value="/getByIdaId")
    @ResponseBody
    public List<IdaAttribute> getByIdaId(String idaId){
        List<IdaAttribute> result = idaAttrbuteService.findBy("idaId", idaId);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deletes", headers = "Accept=application/json")
    @ResponseBody
    public boolean deletes(@RequestBody String[] attrIds) {
        OperationLog operationLog = systemLogService.record("Ida属性","删除","数量：" + attrIds.length);

        for(String attrId : attrIds){
            idaAttrbuteService.deleteById(attrId);
        }

        systemLogService.updateResult(operationLog);
        return true;
    }
}
