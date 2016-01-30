package com.dc.esb.servicegov.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.EnglishWord;
import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.service.impl.MetadataServiceImpl;
import com.dc.esb.servicegov.service.impl.SystemLogServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dc.esb.servicegov.entity.CategoryWord;
import com.dc.esb.servicegov.service.impl.CategoryWordServiceImpl;
import com.dc.esb.servicegov.util.DateUtils;
import org.springframework.web.portlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/categoryWord")
public class CategoryWordController {
    @Autowired
    private SystemLogServiceImpl systemLogService;

    protected Log logger = LogFactory.getLog(getClass());
    @Autowired
    private CategoryWordServiceImpl categoryWordService;
    @Autowired
    private MetadataServiceImpl metadataService;

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    Map<String, Object> getAll(HttpServletRequest req) {
        int pageNo = Integer.parseInt(req.getParameter("page"));
        int rowCount = Integer.parseInt(req.getParameter("rows"));
        String englishWord = req.getParameter("englishWord");
        String chineseWord = req.getParameter("chineseWord");
        String esglisgAb = req.getParameter("esglisgAb");
        String remark = req.getParameter("remark");

        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();
        StringBuffer hql = new StringBuffer("select c from CategoryWord c where 1=1 ");
        if (null != englishWord && !"".equals(englishWord)) {
            hql.append(" and englishWord like ?");
            searchConds.add(new SearchCondition("englishWord", "%" + englishWord + "%"));
        }
        if (null != chineseWord && !"".equals(chineseWord)) {
            hql.append(" and chineseWord like ?");
            try {
                chineseWord = URLDecoder.decode(chineseWord, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            searchConds.add(new SearchCondition("chineseWord", "%" + chineseWord + "%"));
        }
        if (null != esglisgAb && !"".equals(esglisgAb)) {
            hql.append(" and esglisgAb like ?");
            searchConds.add(new SearchCondition("esglisgAb", "%" + esglisgAb + "%"));
        }
        if (null != remark && !"".equals(remark)) {
            hql.append(" and remark like ?");
            try {
                remark = URLDecoder.decode(remark, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            searchConds.add(new SearchCondition("remark", "%" + remark + "%"));
        }
        hql.append(" order by esglisgAb");
//        Page page = categoryWordService.getAll(rowCount);
        Page page = new Page();
        if(searchConds.size() <= 0){
            page = categoryWordService.getPageBy(hql.toString(),rowCount);
        }else{
            page = categoryWordService.getPageBy(hql.toString(),rowCount,searchConds);
        }

        page.setPage(pageNo);

        List<CategoryWord> list = categoryWordService.findBy(hql.toString(), page,searchConds);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total", page.getResultCount());
        map.put("rows", list);
        return map;
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getById/{id}", headers = "Accept=application/json")
    public
    @ResponseBody
    CategoryWord getById(@PathVariable(value = "id") String id) {
        return categoryWordService.getById(id);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByEnglishWord/{englishWord}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByEnglishWord(@PathVariable(value = "englishWord") String englishWord) {
        return categoryWordService.getByEnglishWord(englishWord);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByChineseWord/{chineseWord}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByChineseWord(@PathVariable(value = "chineseWord") String chineseWord) {
        return categoryWordService.getByChineseWord(chineseWord);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByEsglisgAb/{esglisgAb}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByEsglisgAb(@PathVariable(value = "esglisgAb") String esglisgAb) {
        return categoryWordService.getByEsglisgAb(esglisgAb);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByRemark/{remark}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByRemark(@PathVariable(value = "remark") String remark) {
        return categoryWordService.getByRemark(remark);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByPotUser/{potUser}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByPotUser(@PathVariable(value = "potUser") String potUser) {
        return categoryWordService.getByPotUser(potUser);
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getByPotDate/{potDate}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByPotDate(@PathVariable(value = "potDate") String potDate) {
        return categoryWordService.getByPotDate(potDate);
    }

   /* @RequiresPermissions({"categoryWord-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(@RequestBody CategoryWord categoryWord) {
        OperationLog operationLog = systemLogService.record("类别词","添加","中文名称：" + categoryWord.getChineseWord()+"; 英文名称："+ categoryWord.getEnglishWord());

        categoryWord.setOptDate(DateUtils.format(new Date()));
        categoryWordService.save(categoryWord);

        systemLogService.updateResult(operationLog);
        return true;
    }*/

    @RequiresPermissions({"categoryWord-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/add", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean add(@RequestBody CategoryWord categoryWord) {
        OperationLog operationLog = systemLogService.record("类别词","添加","中文名称：" + categoryWord.getChineseWord()+"; 英文名称："+ categoryWord.getEsglisgAb());

        categoryWord.setOptDate(DateUtils.format(new Date()));
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        categoryWord.setOptUser(userName);
        categoryWordService.insert(categoryWord);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"categoryWord-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/modify", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean modify(@RequestBody CategoryWord categoryWord) {
        OperationLog operationLog = systemLogService.record("类别词","修改","中文名称：" + categoryWord.getChineseWord()+"; 英文名称："+ categoryWord.getEnglishWord());

        categoryWordService.save(categoryWord);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"categoryWord-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{Id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean delete(@PathVariable String Id) {
        OperationLog operationLog = systemLogService.record("类别词","删除", "类别词ID" + Id );

        categoryWordService.deleteById(Id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/get/EnglishWord/{englishWord}/ChineseWord/{chineseWord}/EsglisgAb/{esglisgAb}/Remark/{remark}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<CategoryWord> getByParams(@PathVariable(value = "englishWord") String englishWord, @PathVariable(value = "chineseWord") String chineseWord, @PathVariable(value = "esglisgAb") String esglisgAb, @PathVariable(value = "remark") String remark) {
        Map<String, String> params = new HashMap<String, String>();
        if (!"isNull".equals(englishWord))
            params.put("englishWord", englishWord);
        if (!"isNull".equals(chineseWord))
            params.put("chineseWord", chineseWord);
        if (!"isNull".equals(esglisgAb))
            params.put("esglisgAb", esglisgAb);
        if (!"isNull".equals(remark))
            params.put("remark", remark);
        List<CategoryWord> words = categoryWordService.findBy(params);
        return words;
    }

    @RequiresPermissions({"categoryWord-get"})
    @RequestMapping(method = RequestMethod.POST, value = "/query", headers = "Accept=application/json")
    public @ResponseBody Map<String, Object> query(@RequestBody Map<String, String> params){
        List<CategoryWord> rows = categoryWordService.findLikeAnyWhere(params);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }

    @RequiresPermissions({"categoryWord-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/saveCategoryWord/{type}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean saveCategoryWord(@RequestBody List list,@PathVariable String type) {//type 1 insert  2 update
        OperationLog operationLog = systemLogService.record("类别词","批量保存","类别词列表：");
        String logParams = "";
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            List<CategoryWord> list1 = categoryWordService.findBy("englishWord", map.get("esglisgAb"));
            List<CategoryWord> list2 = categoryWordService.findBy("chineseWord", map.get("chineseWord"));
            if((list1.size() > 0 || list2.size() > 0) && type.equals("1")){
                return false;
            }
            CategoryWord categoryWord = null;
            if(type.equals("1")){
                categoryWord = new CategoryWord();
            }else if(type.equals("2")){
                categoryWord = categoryWordService.getById(map.get("id"));
            }
            categoryWord.setId(map.get("id"));
            categoryWord.setChineseWord(map.get("chineseWord"));
            //TZB没有englishWord
//            categoryWord.setEnglishWord(map.get("englishWord"));
            categoryWord.setEnglishWord(map.get("esglisgAb"));
            categoryWord.setEsglisgAb(map.get("esglisgAb"));
            categoryWord.setRemark(map.get("remark"));
            categoryWord.setOptDate(DateUtils.format(new Date()));
            categoryWord.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
            categoryWordService.save(categoryWord);
            logParams += categoryWord.getChineseWord()+",";
        }
        operationLog.setParams("类别词列表："+ logParams);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"categoryWord-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/deleteCategoryWord", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteCategoryWord(@RequestBody List list) throws Exception {
        OperationLog operationLog = systemLogService.record("类别词","批量删除","类别词列表：");
        String logParams = "";

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            Set<String> keySet = map.keySet();
            String id = map.get("id");
            //与元数据关联的不能删除
            String esglisgAb = map.get("esglisgAb");
            List<Metadata> metadatas = metadataService.findBy("categoryWordId", esglisgAb);
            if (metadatas.size() > 0) {
                return false;
            }
            categoryWordService.deleteById(id);
            logParams += esglisgAb + ",";
        }

        operationLog.setParams("类别词列表（英文缩写）："+ logParams);
        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"categoryWord-delete"})
    @RequestMapping(method = RequestMethod.POST, value = "/deleteCategoryWord2", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteCategoryWord2(@RequestBody CategoryWord categoryWord) throws Exception {
        OperationLog operationLog = systemLogService.record("类别词","删除","名称：" + categoryWord.getChineseWord());
        String id = categoryWord.getId();
        //与元数据关联的不能删除
        String esglisgAb = categoryWord.getEsglisgAb();
        List<Metadata> metadatas = metadataService.findBy("categoryWordId", esglisgAb);
        if (metadatas.size() > 0) {
            return false;
        }
        categoryWordService.delete(categoryWord);
        systemLogService.updateResult(operationLog);
        return true;
    }

    /**
     * 类别词esglisgAb唯一性验证
     *
     * @param esglisgAb
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String esglisgAb) {
        return categoryWordService.uniqueValid(esglisgAb);
    }

    /**
     * 类别词chineseWord唯一性验证
     *
     * @param chineseWord
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValidName", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValidName(String chineseWord) {
        Map<String,String> map = new HashMap<String, String>();
        try {
            chineseWord = URLDecoder.decode(chineseWord,"UTF-8");
        }catch (UnsupportedEncodingException e){

        }
        map.put("chineseWord",chineseWord);
        return categoryWordService.uniqueValid(map);
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
//        ModelAndView mv = new ModelAndView("login/login");
//        return mv;
//        return "forward:/jsp/403.jsp";
        return "403";
    }

}
