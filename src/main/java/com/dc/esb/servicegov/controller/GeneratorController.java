package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.Generator;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.GeneratorServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by Administrator on 2015/11/25.
 */
@Controller
@RequestMapping("/generator")
public class GeneratorController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    GeneratorServiceImpl generatorService;

    @Autowired
    GeneratorServiceImpl generatorServiceImpl;

    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    @ResponseBody
    public List<Generator> getAll() {
        return generatorService.getAll();
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAllGenerator", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getAllGenerator(@RequestParam("page") int pageNo, @RequestParam("rows") int rowCount) {
        List<Generator> list = new ArrayList<Generator>();
        Page page = generatorService.getAll(rowCount);
        page.setPage(pageNo);
        list = generatorService.getAll(page);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", list);
        return result;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(@RequestBody Generator generator) {
        OperationLog operationLog = systemLogService.record("生成类", "添加", "生成类名称：" + generator.getName());
        String id = UUID.randomUUID().toString();
        generator.setId(id);
        generatorService.save(generator);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/checkClassExit", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean checkClassExit(String implementsClazz) {
        try {
            if (implementsClazz == null || "".equals(implementsClazz)) {
                return false;
            }
            Class aClass = Class.forName(implementsClazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}", headers = "Accept=application/json")
    public ModelAndView getById(@PathVariable String id) {
        Generator generator = generatorService.getById(id);
        ModelAndView model = new ModelAndView();
        model.addObject("generator", generator);
        model.setViewName("generator/generatorEdit");
        return model;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify(@RequestBody Generator generator) {
        OperationLog operationLog = systemLogService.record("生成类", "修改", "生成类名称：" + generator.getName());
        generatorServiceImpl.update(generator);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresRoles({"admin"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String id) {
        OperationLog operationLog = systemLogService.record("生成类", "删除", "生成类id" + id);
        generatorServiceImpl.deleteById(id);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }
}
