package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SLADAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.SLA;
import com.dc.esb.servicegov.entity.SLAHis;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SLAServiceImpl extends AbstractBaseService<SLA, String> {

    private static final Log log = LogFactory.getLog(SLAServiceImpl.class);

    @Autowired
    private SLADAOImpl slaDAOImpl;
    @Autowired
    private ServiceServiceImpl serviceService;
    @Autowired
    private OperationServiceImpl operationService;
    @Autowired
    private SLAHisServiceImpl slaHisService;

    @Override
    public HibernateDAO<SLA, String> getDAO() {
        return slaDAOImpl;
    }

    public List<SLA> getTemplateSLA(Map<String, String> params) {
        return slaDAOImpl.findTemplateBy(params);
    }

    public void backUpSLAByCondition(Map<String, String> params, String autoId){
        log.info("find sla by["+params.values()+"]");
        List<SLA> slaList = findBy(params);

        if(slaList != null && slaList.size() > 0){
            for(SLA sla : slaList){
                SLAHis slaHis = new SLAHis(sla, autoId);
                slaHisService.save(slaHis);
            }
        }
    }
    @Override
    public void save(SLA sla){
        super.save(sla);
        operationService.editReleate(sla.getServiceId(), sla.getOperationId());
    }

    public void saveTemplate(SLA sla){
        super.save(sla);
    }
    @Override
    public void deleteById(String id) {
        SLA sla = slaDAOImpl.findUniqueBy("slaId", id);
        if(null != sla.getServiceId()){
            operationService.editReleate(sla.getServiceId(), sla.getOperationId());
        }

        getDAO().delete(id);
    }

    public List<SLA> getAllTemplateSLA(){
        return slaDAOImpl.getAllTemplateSLA();
    }

    public List<SLA> getTemplateSLABy(Map<String, String> params){
        return slaDAOImpl.getAllTemplateSLABy(params);
    }

    /**
     * 前端唯一性验证
     * @param slaName
     * @return
     */
    public boolean uniqueValid(String slaName) {
        SLA entity = findUniqueBy("slaName",slaName);
        if (entity != null) {
            return false;
        }
        return true;
    }

//    public List<SLA> findBy(Map<String, String> params) {
//        return slaDAOImpl.findBy(params);
//    }
}