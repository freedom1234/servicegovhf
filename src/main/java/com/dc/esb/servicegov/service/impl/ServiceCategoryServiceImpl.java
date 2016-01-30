package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceCategoryDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ServiceCategory;
import com.dc.esb.servicegov.service.ServiceCategoryService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.vo.ServiceTreeViewBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServiceCategoryServiceImpl extends AbstractBaseService<ServiceCategory, String> implements ServiceCategoryService {

    @Autowired
    private ServiceCategoryDAOImpl serviceCategoryDAOImpl;

    @Override
    public HibernateDAO<ServiceCategory, String> getDAO() {
        return serviceCategoryDAOImpl;
    }

    public List<ServiceCategory> getCategoryHierarchyByLeafIds(List<String> categoryIds) {
        List<ServiceCategory> categories = new ArrayList<ServiceCategory>();
        List<ServiceCategory> leafCategories = getDAO().get(categoryIds);
        if (null != leafCategories) {
            categories.addAll(leafCategories);
            List<String> parentIds = new ArrayList<String>();
            for (ServiceCategory serviceCategory : leafCategories) {
                parentIds.remove(serviceCategory.getParentId());
                parentIds.add(serviceCategory.getParentId());
            }
            List<ServiceCategory> parentCategories = getDAO().get(parentIds);
            if(null != parentCategories){
                categories.addAll(parentCategories);
            }
        }
        return categories;
    }


}
