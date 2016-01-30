package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SDAAttrbuteDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.ErrorCode;
import com.dc.esb.servicegov.entity.SDAAttribute;
import com.dc.esb.servicegov.service.ErrorCodeService;
import com.dc.esb.servicegov.service.SDAAttrbuteService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SDAAttrbuteServiceImpl extends AbstractBaseService<SDAAttribute,String> implements SDAAttrbuteService {
    @Autowired
    private SDAAttrbuteDAOImpl sdaAttrbuteDAO;
    @Override
    public HibernateDAO<SDAAttribute, String> getDAO() {
        return sdaAttrbuteDAO;
    }

    /**
     * 判断sda是否有附加属性
     * @param sdaId
     * @return
     */
    public boolean judgeAttr(String sdaId){
        List<SDAAttribute> list = sdaAttrbuteDAO.findBy("sdaId", sdaId);
        if(null != list && 0 < list.size()){
            return true;
        }
        return false;
    }
}
