package com.dc.esb.servicegov.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.dc.esb.servicegov.dao.impl.OperationDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationHisDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.BaseLineDAOImpl;
import com.dc.esb.servicegov.util.DateUtils;

@Service
@Transactional
public class BaseLineServiceImpl extends AbstractBaseService<BaseLine, String> {
    @Autowired
    private BaseLineDAOImpl baseLineDAO;
    @Autowired
    private VersionServiceImpl versionServiceImpl;
    @Autowired
    private VersionHisServiceImpl versionHisServiceImpl;
    @Autowired
    private OperationServiceImpl operationServiceImpl;
    @Autowired
    private OperationHisServiceImpl operationHisServiceImpl;
    @Autowired
    private BVServiceImpl bvServiceImpl;
    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeServiceImpl;
    @Autowired
    private OperationHisDAOImpl operationHisDAO;
    @Autowired
    private OperationDAOImpl operationDAO;

    public boolean release(HttpServletRequest req, String code, String blDesc, String versionHisIds) {
        //新建基线
        BaseLine bl = new BaseLine();
        bl.setBaseId(UUID.randomUUID().toString());
        bl.setCode(code);
        bl.setBlDesc(blDesc);
        bl.setOptDate(DateUtils.format(new Date()));
        bl.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
        String versionId = versionServiceImpl.addVersion(Constants.Version.TARGET_TYPE_BASELINE, bl.getBaseId(), Constants.Version.TARGET_TYPE_BASELINE);
        bl.setVersionId(versionId);
        baseLineDAO.save(bl);

        //保存基线版本关系
        if (!StringUtils.isEmpty(versionHisIds)) {
            String[] vids = versionHisIds.split("\\,");
            for (String versionHisId : vids) {
                BaseLineVersionHisMapping bvm = new BaseLineVersionHisMapping();
                bvm.setBaseLineId(bl.getBaseId());
                bvm.setVersionHisId(versionHisId);
                bvServiceImpl.save(bvm);
                //TODO 更改versionHis的基线版本号和type(基线版本号根据BaseLineVersionHisMapping查找基线）
                VersionHis vh = versionHisServiceImpl.getById(versionHisId);
//                vh.setType(Constants.Version.TYPE_BASELINE);
                //更新服务状态为上线
                List<OperationHis> operationHises = operationHisDAO.findBy("versionHisId", versionHisId);
                for (OperationHis operationHis : operationHises) {
                    String serviceId = operationHis.getServiceId();
                    String operationId = operationHis.getOperationId();
                    Operation operation = operationServiceImpl.getOperation(serviceId, operationId);
                    if (null != operation) {
                        operation.setState(Constants.Operation.LIFE_CYCLE_STATE_ONLINE);
                        operationServiceImpl.save(operation);
                    }

                }
            }
            //TODO versionHis的type到底是什么
            versionHisServiceImpl.updateVerionHis(Constants.Version.TARGET_TYPE_BASELINE, vids);
        }


        return true;
    }

    public List<BaseLine> getBaseLine(String code, String blDesc) {

        return baseLineDAO.getBaseLine(code, blDesc);
    }

    public List<?> getBLOperationHiss(String baseId) {
        return operationHisServiceImpl.getBLOperationHiss(baseId);
    }

    @Override
    public HibernateDAO<BaseLine, String> getDAO() {
        return baseLineDAO;
    }
}
