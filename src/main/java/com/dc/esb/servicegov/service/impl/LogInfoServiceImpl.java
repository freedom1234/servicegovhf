package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.LogInfoDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.LogInfo;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.Utils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/14.
 */
@Service
@Transactional
public class LogInfoServiceImpl extends AbstractBaseService<LogInfo,String>{
    @Autowired
    LogInfoDAOImpl logInfoDAO;

    public void saveLog(String detail,String type){
        LogInfo logInfo = new LogInfo();
        logInfo.setDetail(detail);
        logInfo.setType(type);
        logInfo.setTime(DateUtils.format(new Date()));
        logInfoDAO.save(logInfo);
    }
    @Override
    public HibernateDAO<LogInfo, String> getDAO() {
        return logInfoDAO;
    }
}
