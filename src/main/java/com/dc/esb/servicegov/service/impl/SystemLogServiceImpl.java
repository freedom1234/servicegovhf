package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.OperationLogDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationLogTypeDAOImpl;
import com.dc.esb.servicegov.dao.impl.UserDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.OperationLogType;
import com.dc.esb.servicegov.entity.SGUser;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.vo.ReuseRateVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jboss.seam.annotations.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wang on 2015/8/18.
 */
@Service
@Transactional
public class SystemLogServiceImpl  extends AbstractBaseService<OperationLog,String> {
    @Autowired
    OperationLogDAOImpl operationLogDAO;
    @Autowired
    OperationLogTypeDAOImpl operationLogTypeDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Override
    public HibernateDAO<OperationLog, String> getDAO() {
        return operationLogDAO;
    }

    public void insertLogType(OperationLogType operationLogType){
        operationLogTypeDAO.save(operationLogType);
    }

    public long getLogCount(Map<String, String[]> values) {
        String hql = "select count(*) from " + OperationLog.class.getName() + " o where 1=1";
        if(values.get("optUser") != null && values.get("optUser").length > 0){
            if (StringUtils.isNotEmpty(values.get("optUser")[0])){
                hql += " and o.optUser like '%" + values.get("optUser")[0]+"%'";
            }
        }
        if(values.get("startDate") != null && values.get("startDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("startDate")[0])) {
                hql += " and o.optDate > '" + values.get("startDate")[0] + "' ";
            }
        }
        if(values.get("endDate") != null && values.get("endDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("endDate")[0])) {
                hql += " and o.optDate < '" + values.get("endDate")[0] + "' ";
            }
        }
        long count = (Long)operationLogDAO.findUnique(hql);
        return count;
    }
    public List<OperationLog> getLogs(Map<String, String[]> values, Page page) {
        String hql = " from " + OperationLog.class.getName() + " o where 1=1";
        if(values.get("optUser") != null && values.get("optUser").length > 0){
            if (StringUtils.isNotEmpty(values.get("optUser")[0])){
                hql += " and o.optUser like '%" + values.get("optUser")[0]+"%'";
            }
        }
        if(values.get("startDate") != null && values.get("startDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("startDate")[0])) {
                hql += " and o.optDate > '" + values.get("startDate")[0] + "' ";
            }
        }
        if(values.get("endDate") != null && values.get("endDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("endDate")[0])) {
                hql += " and o.optDate < '" + values.get("endDate")[0] + "' ";
            }
        }
         hql += " order by optDate desc";
        List<OperationLog> list = operationLogDAO.find(hql, page);
        return list;
    }

    public void addLoginLog(String userName,String optType, String result){
        OperationLog operationLog = new OperationLog();
        operationLog.setOptUser(userName);
        operationLog.setOptDate(DateUtils.format(new Date()));
        operationLog.setChineseName("登录");
        operationLog.setOptType(optType);
        operationLog.setParams("用户名：" + userName);
        operationLog.setOptResult(result);
        operationLogDAO.save(operationLog);
    }

    public OperationLog record(String chineseName, String optType, String params){
        OperationLog operationLog = new OperationLog();
        String optUser = (String) SecurityUtils.getSubject().getPrincipal();
        operationLog.setOptUser(optUser);
        SGUser sgUser = userDAO.findUniqueBy("id", optUser);
        operationLog.setUser_name(sgUser.getName());
        operationLog.setOptDate(DateUtils.format(new Date()));
        operationLog.setOptResult("失败");//初始值为失败，方法结束后更新
        operationLog.setChineseName((chineseName != null ? chineseName : ""));
        operationLog.setOptType((optType != null ? optType : ""));
        operationLog.setParams((params != null ? params : ""));

        operationLogDAO.save(operationLog);
        return  operationLog;
    }

    public void updateResult(OperationLog operationLog){
        operationLog.setOptResult("成功");
        operationLogDAO.save(operationLog);
    }

    /**
     * 获取数据字典修订记录
     * 英文单词、类别词、元数据、数据字典导入记录
     * @return
     */
    public List<OperationLog> getDataDectionaryRecord(String startDate, String endDate){
        List<OperationLog> result = new ArrayList<OperationLog>();
        Map<String, Object> params = new HashMap<String, Object>();
        endDate += " 23:59:59";
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("chineseName", "元数据");
        List<String> optType = new ArrayList<String>();
        optType.add("新增");
        optType.add("修改");
        optType.add("删除");
        optType.add("批量删除");
        params.put("optType", optType);
        List<OperationLog> metadataRecordList = getByParams(params);
        result.addAll(metadataRecordList);

        params.put("chineseName", "类别词");
        optType.clear();
        optType.add("添加");
        optType.add("修改");
        optType.add("删除");
        optType.add("批量保存");
        optType.add("批量删除");
        params.put("optType", optType);
        List<OperationLog> categorywordRecordList = getByParams(params);
        result.addAll(categorywordRecordList);

        params.put("chineseName", "英文单词");
        optType.clear();
        optType.add("添加");
        optType.add("修改");
        optType.add("删除");
        params.put("optType", optType);
        List<OperationLog> englishwordRecordList = getByParams(params);
        result.addAll(englishwordRecordList);

        params.put("chineseName", "数据字典");
        optType.clear();
        optType.add("导入");
        params.put("optType", optType);
        List<OperationLog> importRecordList = getByParams(params);
        result.addAll(importRecordList);
        return result;
    }

    /**
     * 获取元数据增删改查记录
     * @return
     */
    public List<OperationLog> getByParams(Map<String, Object> params){
        String hql = " from OperationLog where chineseName=:chineseName and optType in(:optType) and optDate>:startDate and optDate<:endDate";
        List<OperationLog> result = operationLogDAO.find(hql, params);
        return result;
    }
}
