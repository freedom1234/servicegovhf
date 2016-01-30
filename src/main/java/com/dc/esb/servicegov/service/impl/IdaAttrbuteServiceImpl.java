package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.IdaAttrbuteDAOImpl;
import com.dc.esb.servicegov.dao.impl.SDAAttrbuteDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.IdaAttribute;
import com.dc.esb.servicegov.entity.SDAAttribute;
import com.dc.esb.servicegov.service.SDAAttrbuteService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IdaAttrbuteServiceImpl extends AbstractBaseService<IdaAttribute,String> implements SDAAttrbuteService {
    @Autowired
    private IdaAttrbuteDAOImpl idaAttrbuteDAO;
    @Override
    public HibernateDAO<IdaAttribute, String> getDAO() {
        return idaAttrbuteDAO;
    }

    /**
     * 判断ida是否有附加属性
     * @param idaId
     * @return
     */
    public boolean judgeAttr(String idaId){
        List<IdaAttribute> list = idaAttrbuteDAO.findBy("idaId", idaId);
        if(null != list && 0 < list.size()){
            return true;
        }
        return false;
    }
}
