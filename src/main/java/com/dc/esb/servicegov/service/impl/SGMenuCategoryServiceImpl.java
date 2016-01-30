package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SGMenuCategoryDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SGMenuCategory;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Service
@Transactional
public class SGMenuCategoryServiceImpl extends AbstractBaseService<SGMenuCategory,String> {
    @Autowired
    private SGMenuCategoryDAOImpl menuCategoryDAO;
    @Override
    public HibernateDAO<SGMenuCategory, String> getDAO() {
        return menuCategoryDAO;
    }
}
