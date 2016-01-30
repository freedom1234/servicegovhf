package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.UserDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGUser;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vincentfxz on 15/7/2.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractBaseService<SGUser, String>{
    @Autowired
    private UserDAOImpl userDAO;

    @Override
    public HibernateDAO<SGUser, String> getDAO() {
        return userDAO;
    }
    
    public void passWord(String str1,String str2){
    	userDAO.passWord(str1,str2);
    }
}
