package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceHeadDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.Metadata;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.entity.ServiceHead;
import com.dc.esb.servicegov.service.ServiceHeadService;

import java.util.*;

@Service
@Transactional
public class ServiceHeadServiceImpl extends AbstractBaseService<ServiceHead,String> implements ServiceHeadService{

    @Autowired
    private ServiceHeadDAOImpl serviceHeadDAO;
    @Autowired
    private ServiceHeadSdaServiceImpl serviceHeadSdaService;

    @Override
    public HibernateDAO<ServiceHead, String> getDAO() {
        return serviceHeadDAO;
    }

    public long queryCount(Map<String, String[]> params){
        String hql = " select count(*) FROM ServiceHead where 1=1";
        hql += genderCondition(params);
        long result = serviceHeadDAO.findUnique(hql);
        return result;
    }

    public List<ServiceHead> queryByCondition(Map<String, String[]> values, Page page){
        String hql = " from ServiceHead a where 1=1 ";
        hql += genderCondition(values);
        return serviceHeadDAO.findBy(hql, page, new ArrayList<SearchCondition>());
    }
    public String genderCondition(Map<String, String[]> params){
        String hql = "";
        if(params != null){
            if(params.get("headName") != null){
                if(StringUtils.isNotEmpty(params.get("headName")[0]))
                    hql += " and headName like '%" + params.get("headName")[0] +"%' ";
            }
            if(params.get("headDesc") != null){
                if(StringUtils.isNotEmpty(params.get("headDesc")[0]))
                    hql += " and headDesc like '%" + params.get("headDesc")[0] +"%' ";
            }
            if(params.get("type") != null){
                if(StringUtils.isNotEmpty(params.get("type")[0]))
                    hql += " and type like '%" + params.get("type")[0] +"%' ";
            }
        }
        return hql;
    }
    public boolean uniqueName(String headName){
        String hql = " select count(*) from ServiceHead where headName=?";
        long num = serviceHeadDAO.findUnique(hql, headName);
        if(num > 0){
            return false;
        }
        return true;
    }

    public void add(ServiceHead serviceHead){
        if(serviceHead != null){
            serviceHead.setHeadId(UUID.randomUUID().toString());
            serviceHeadDAO.save(serviceHead);
            serviceHeadSdaService.genderSDAAuto(serviceHead.getHeadId());//自动生成sda的root，request，response节点
        }
    }

    public void update(ServiceHead serviceHead){
        if(serviceHead != null){
            if(serviceHead.getHeadId() == null){
                serviceHead.setHeadId(UUID.randomUUID().toString());
            }
            serviceHeadDAO.save(serviceHead);
        }
    }

    public List<ServiceHead> getByIdStr(String headIdStr){
        if(StringUtils.isNotEmpty(headIdStr)){
            String[] headIds = headIdStr.split("\\,");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("headIds", headIds);
            String hql = " from ServiceHead" + " sh where sh.headId in (:headIds)";
            List<ServiceHead> list = serviceHeadDAO.find(hql, param);
            return list;
        }
        return null;
    }
}
