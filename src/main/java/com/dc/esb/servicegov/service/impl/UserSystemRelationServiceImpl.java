package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.UserSystemRelationDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGUser;
import com.dc.esb.servicegov.entity.UserSystemRelation;
import com.dc.esb.servicegov.service.UserSystemRelationService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/12/15.
 */
@Service
@Transactional
public class UserSystemRelationServiceImpl  extends AbstractBaseService<UserSystemRelation, String> implements UserSystemRelationService {
    @Autowired
    UserSystemRelationDAOImpl userSystemRelationDAO;
    @Override
    public HibernateDAO<UserSystemRelation, String> getDAO() {
        return userSystemRelationDAO;
    }

    /**
     * 保存用户-系统关系
     * @param userId
     * @param systemIdsStr
     * @return
     */
    public boolean saveUserSystem(String userId, String systemIdsStr){
        if(StringUtils.isNotEmpty(systemIdsStr)){
            userSystemRelationDAO.deleteRelation(userId);//删除原有关系
            String[] systemIds = systemIdsStr.split("\\,");
            for(String systemId : systemIds){
                UserSystemRelation userSystemRelation = new UserSystemRelation();
                userSystemRelation.setSystemId(systemId);
                userSystemRelation.setUserId(userId);
                userSystemRelationDAO.save(userSystemRelation);
            }
            return true;
        }

        return false;
    }

}
