package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.FileManagerDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.FileManager;
import com.dc.esb.servicegov.service.FileManagerService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/7/12.
 */
@Service
@Transactional
public class FileManagerServiceImpl extends AbstractBaseService<FileManager, String> implements FileManagerService {

    @Autowired
    FileManagerDAOImpl fileManagerDao;

    @Override
    public HibernateDAO<FileManager, String> getDAO() {
        return fileManagerDao;
    }
}
