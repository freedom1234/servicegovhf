package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.service.StatisticsService;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.ReleaseVO;
import com.dc.esb.servicegov.vo.ReuseRateVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    /**
     * 查询系统复用率统计报表
     * @param req
     * @return
     */
    @RequiresPermissions({"statistics-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/systemReuseRate", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> systemReuseRate(HttpServletRequest req){
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(statisticsService.getReuseRateCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
        List<ReuseRateVO> rows = statisticsService.getReuseRate(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }

    /**
     *
     * @param req 分类id， type
     * @return 复用率
     */
    @RequiresPermissions({"statistics-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/serviceReuseRate", headers = "Accept=application/json")
    @ResponseBody
    public List<TreeNode> serviceReuseRate(HttpServletRequest req){
        List<TreeNode> rows = statisticsService.getServiceReuseRate2();

        return rows;
    }

    @RequiresPermissions({"statistics-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/serviceReuseRate/expandServiceCategory", headers = "Accept=application/json")
    @ResponseBody
    public List<TreeNode> expandServiceCategory(String serviceCategoryId){
        List<TreeNode> rows = statisticsService.getServiceCategoryChildren(serviceCategoryId);
        return rows;
    }

    /**
     * 查询发布次数统计报表
     * @param req
     * @return
     */
    @RequiresPermissions({"statistics-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/release/count", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> releaseCount(HttpServletRequest req){
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(statisticsService.getReleaseVOCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
        List<ReleaseVO> rows = statisticsService.getReleaseCountVO(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }
    /**
     * 查询发布状态统计报表
     * @param req
     * @return
     */
    @RequiresPermissions({"statistics-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/release/state", headers = "Accept=application/json")
    @ResponseBody
    public Map<String, Object> releaseState(HttpServletRequest req){
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        Page page = new Page(statisticsService.getReleaseVOCount(req.getParameterMap()), rowCount);
        page.setPage(pageNo);
        List<ReleaseVO> rows = statisticsService.getReleaseStateVO(req.getParameterMap(), page);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", page.getResultCount());
        result.put("rows", rows);
        return result;
    }
}
