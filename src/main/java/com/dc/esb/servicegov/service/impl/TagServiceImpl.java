package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.InterfaceTagDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationTagDAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceTagDAOImpl;
import com.dc.esb.servicegov.dao.impl.TagDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.InterfaceTag;
import com.dc.esb.servicegov.entity.OperationTag;
import com.dc.esb.servicegov.entity.ServiceTag;
import com.dc.esb.servicegov.entity.Tag;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class TagServiceImpl extends AbstractBaseService<Tag, String> {

    @Autowired
    private TagDAOImpl tagDAO;

    @Autowired
    private InterfaceTagDAOImpl interfaceTagDAO;

    @Autowired
    private ServiceTagDAOImpl serviceTagDAO;

    @Autowired
    private OperationTagDAOImpl operationTagDAO;

    @Override
    public HibernateDAO<Tag, String> getDAO() {
        return tagDAO;
    }

    /**
     * 查询某个接口的所有标签
     *
     * @param interfaceId
     * @return
     */
    public List<Tag> getTagsForInterface(String interfaceId) {
        List<InterfaceTag> interfaceTags = interfaceTagDAO.findBy("interfaceId", interfaceId);
        List<String> tagIds = new ArrayList<String>();
        for (InterfaceTag interfaceTag : interfaceTags) {
            tagIds.add(interfaceTag.getTagId());
        }
        if (tagIds.size() > 0) {
            return getDAO().get(tagIds);
        }
        return new ArrayList<Tag>();
    }

    /**
     * 为接口新增标签，每次都没全量，删除标签，然后新增全部
     * 如果标签不存在，新增标签
     *
     * @param interfaceId
     * @param tags
     * @return
     */
    public boolean addTagsForInterface(String interfaceId, List<Tag> tags) {
        List<InterfaceTag> interfaceTags = interfaceTagDAO.findBy("interfaceId", interfaceId);
        interfaceTagDAO.delete(interfaceTags);
        //如果Tag不存在，要新增tag
        for (Tag tag : tags) {
            List<Tag> tagExisted = getDAO().findBy("tagName", tag.getTagName());
            if (tagExisted.size() == 0) {
                tagDAO.save(tag);
            } else {
                tag = tagExisted.get(0);
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("tagId", tag.getTagId());
            params.put("interfaceId", interfaceId);
            List<InterfaceTag> interfaceTagExisted = interfaceTagDAO.findBy(params);
            if (interfaceTagExisted.size() == 0) {
                InterfaceTag interfaceTag = new InterfaceTag();
                interfaceTag.setInterfaceId(interfaceId);
                interfaceTag.setTagId(tag.getTagId());
                interfaceTagDAO.save(interfaceTag);
            }
        }
        return true;
    }

    public List<Tag> getTagsForService(String serviceId) {
        List<ServiceTag> serviceTags = serviceTagDAO.findBy("serviceId", serviceId);
        if (serviceTags.size() > 0) {
            List<String> tagIds = new ArrayList<String>();
            for(ServiceTag serviceTag : serviceTags){
                tagIds.add(serviceTag.getTagId());
            }
            if (tagIds.size() > 0) {
                return getDAO().get(tagIds);
            }
        }
        return new ArrayList<Tag>();
    }

    public boolean addTagsForService(String serviceId, List<Tag> tags) {
        List<ServiceTag> serviceTags = serviceTagDAO.findBy("serviceId", serviceId);
        serviceTagDAO.delete(serviceTags);
        //如果Tag不存在，要新增tag
        for (Tag tag : tags) {
            List<Tag> tagExisted = getDAO().findBy("tagName", tag.getTagName());
            if (tagExisted.size() == 0) {
                tagDAO.save(tag);
            } else {
                tag = tagExisted.get(0);
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("tagId", tag.getTagId());
            params.put("serviceId", serviceId);
            List<ServiceTag> serviceTagExisted = serviceTagDAO.findBy(params);
            if (serviceTagExisted.size() == 0) {
                ServiceTag serviceTag = new ServiceTag();
                serviceTag.setServiceId(serviceId);
                serviceTag.setTagId(tag.getTagId());
                serviceTagDAO.save(serviceTag);
            }
        }
        return true;
    }

    public List<Tag> getTagsForOperation(String serviceId, String operationId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        List<OperationTag> operationTags = operationTagDAO.findBy(params);
        if (operationTags.size() > 0) {
            List<String> tagIds = new ArrayList<String>();
            for(OperationTag operationTag : operationTags){
                tagIds.add(operationTag.getTagId());
            }
            if (tagIds.size() > 0) {
                return getDAO().get(tagIds);
            }
        }
        return new ArrayList<Tag>();
    }

    public boolean addTagsForOperation(String serviceId, String operationId, List<Tag> tags) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        List<OperationTag> operationTags = operationTagDAO.findBy(params);
        operationTagDAO.delete(operationTags);
        //如果Tag不存在，要新增tag
        for (Tag tag : tags) {
            List<Tag> tagExisted = getDAO().findBy("tagName", tag.getTagName());
            if (tagExisted.size() == 0) {
                tagDAO.save(tag);
            } else {
                tag = tagExisted.get(0);
            }
            params.put("tagId", tag.getTagId());
            List<OperationTag> operationTagExisted = operationTagDAO.findBy(params);
            if (operationTagExisted.size() == 0) {
                OperationTag operationTag = new OperationTag();
                operationTag.setServiceId(serviceId);
                operationTag.setTagId(tag.getTagId());
                operationTag.setOperationId(operationId);
                operationTagDAO.save(operationTag);
            }
        }
        return true;
    }

    public String findInterfaceIdsByTag(String interfaceTag){
        String ids = "";
        Map<String,String> map = new HashMap<String, String>();
        map.put("tagName", interfaceTag);
        List<Tag> tags = tagDAO.findLike(map, MatchMode.ANYWHERE);
        for (int i = 0; i < tags.size(); i++) {
            List<InterfaceTag> interfaceTags = interfaceTagDAO.findBy("tagId", tags.get(i).getTagId());
            for (int j = 0; j < interfaceTags.size(); j++) {
                if(ids.equals("")){
                    ids += "'"+interfaceTags.get(j).getInterfaceId() + "'";
                }else{
                    ids += ",'"+interfaceTags.get(j).getInterfaceId() + "'";
                }
            }
        }
        return ids;
    }

}
