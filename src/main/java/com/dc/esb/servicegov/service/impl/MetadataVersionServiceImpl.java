package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.MetadataVersionDaoImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.MetadataVersion;
import com.dc.esb.servicegov.service.MetadataVersionService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/12/31.
 */
@Service
@Transactional
public class MetadataVersionServiceImpl extends AbstractBaseService<MetadataVersion, String> implements MetadataVersionService{
    final static String initVersionNo = "V1.0.0";
    @Autowired
    private MetadataVersionDaoImpl metadataVersionDao;
    @Override
    public HibernateDAO<MetadataVersion, String> getDAO() {
        return metadataVersionDao;
    }

    public String getLastVersion(){
        String hql = " from " + MetadataVersion.class.getName() + " as m order by  m.versionNo desc ";
        Query query = metadataVersionDao.getSession().createQuery(hql);
        query.setFirstResult(0);
        query.setMaxResults(1);

        List<MetadataVersion> list = query.list();
        if(null == list || list.size() == 0){
            return initVersionNo;
        }
        return list.get(0).getVersionNo();
    }
}
