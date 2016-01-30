package com.dc.esb.servicegov.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.impl.InterfaceHeadRelateDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.Interface;
import com.dc.esb.servicegov.entity.InterfaceHeadRelate;
import com.dc.esb.servicegov.service.IdaService;
import com.dc.esb.servicegov.service.InterfaceService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.InterfaceHeadDAOImpl;
import com.dc.esb.servicegov.entity.InterfaceHead;
import com.dc.esb.servicegov.service.InterfaceHeadService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class InterfaceHeadServiceImpl extends AbstractBaseService<InterfaceHead, String> implements InterfaceHeadService {

    @Autowired
    private InterfaceHeadDAOImpl interfaceHeadDAO;
    @Autowired
    private IdaService idaService;
    @Autowired
    InterfaceHeadRelateDAOImpl interfaceHeadRelateDAO;
    @Autowired
    private InterfaceService interfaceService;

    @Override
    public HibernateDAO<InterfaceHead, String> getDAO() {
        return interfaceHeadDAO;
    }

    /**
     * 前端唯一性验证
     *
     * @param headName
     * @return
     */
    @Override
    public boolean uniqueValid(String headName) {
        InterfaceHead entity = findUniqueBy("headName", headName);
        if (entity != null) {
            return false;
        }
        return true;
    }

    @Override
    public List<List<Ida>> getHeadersByInterfaceId(String interfaceId) {
        List<List<Ida>> heads = new ArrayList<List<Ida>>();
        Interface inter = interfaceService.getById(interfaceId);
        List<InterfaceHeadRelate> interfaceHeadRelates = inter.getHeadRelates();
        for (InterfaceHeadRelate interfaceHeadRelate : interfaceHeadRelates) {
            String headId = interfaceHeadRelate.getHeadId();
            List<Ida> headIdas = idaService.findBy("headId", headId);
            heads.add(headIdas);
        }
        return heads;
    }

    @Override
    public void initHDA(InterfaceHead interfaceHead) {
        Ida ida = new Ida();
        ida.setHeadId(interfaceHead.getHeadId());
        ida.setParentId(null);
        ida.setStructName(Constants.ElementAttributes.ROOT_NAME);
        ida.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
        ida.setXpath(Constants.ElementAttributes.ROOT_XPATH);
        idaService.save(ida);
        String parentId = ida.getId();

        ida = new Ida();
        ida.setHeadId(interfaceHead.getHeadId());
        ida.setParentId(parentId);
        ida.setStructName(Constants.ElementAttributes.REQUEST_NAME);
        ida.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
        ida.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
        idaService.save(ida);

        ida = new Ida();
        ida.setHeadId(interfaceHead.getHeadId());
        ida.setParentId(parentId);
        ida.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
        ida.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
        ida.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
        idaService.save(ida);
    }

    public List<InterfaceHead> getByInterfaceId(String interfaceId) {
        String hql = " select ih from " + InterfaceHead.class.getName() + " ih, "
                + InterfaceHeadRelate.class.getName() + " ihr where ihr.interfaceId = ? and ihr.headId = ih.headId";
        List<InterfaceHead> heads = interfaceHeadDAO.find(hql, interfaceId);
        return heads;
    }

    public List<InterfaceHead> getByInterfaceIds(List<String> interfaceIds) {
        String hql = " select distinct ih from " + InterfaceHead.class.getName() + " ih, "
                + InterfaceHeadRelate.class.getName() + " ihr where ihr.interfaceId in (:interfaceIds) and ihr.headId = ih.headId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("interfaceIds", interfaceIds);
        List<InterfaceHead> heads = interfaceHeadDAO.find(hql, param);
        return heads;
    }

    public String getHeadNames(List<InterfaceHead> heads) {
        String headNames = "";
        if (heads != null) {
            for (int i = 0; i < heads.size(); i++) {
                headNames += heads.get(i).getHeadName();
            }
        }
        return headNames;
    }
    public String getHeadNames(String interfaceId){
        List<InterfaceHead> heads =getByInterfaceId(interfaceId);
        if(null != heads){
            return getHeadNames(heads);
        }
        return "";
    }
}
