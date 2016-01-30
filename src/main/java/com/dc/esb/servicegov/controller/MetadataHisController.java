package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.service.impl.MetadataHisServiceImpl;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/30.
 */
@Controller
@RequestMapping("/metadataHis")
public class MetadataHisController {
    @Autowired
    public MetadataHisServiceImpl metadataHisService;
    @RequiresPermissions({"metadata-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/query", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest req) {
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(metadataHisService.queryCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
//        List<Metadata> rows = metadataService.queryByCondition(req.getParameterMap(), page);
        //关联categoryWord表，显示chineseWord
        List<MetadataHisServiceImpl.MetadataBean> rows = metadataHisService.queryByCondition2(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

}
