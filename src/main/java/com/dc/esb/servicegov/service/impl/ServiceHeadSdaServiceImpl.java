package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.SDADAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.EasyUiTreeUtil;
import com.dc.esb.servicegov.util.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2015/11/12.
 */
@Service
@Transactional
public class ServiceHeadSdaServiceImpl extends AbstractBaseService<SDA, String>{
    @Autowired
    private SDADAOImpl sdaDAO;

    @Override
    public HibernateDAO<SDA, String> getDAO() {
        return sdaDAO;
    }

    public List<SDA> genderSDAAuto(String serviceHeadId){
        List<SDA> result = new ArrayList<SDA>();
        SDA sdaRoot = new SDA();
        sdaRoot.setId(UUID.randomUUID().toString());
        sdaRoot.setStructName(Constants.ElementAttributes.ROOT_NAME);
        sdaRoot.setStructAlias(Constants.ElementAttributes.ROOT_ALIAS);
        sdaRoot.setXpath(Constants.ElementAttributes.ROOT_XPATH);
        sdaRoot.setSeq(0);
        sdaRoot.setServiceHeadId(serviceHeadId);

        sdaDAO.save(sdaRoot);
        result.add(sdaRoot);

        SDA sdaReq = new SDA();
        sdaReq.setId(UUID.randomUUID().toString());
        sdaReq.setStructName(Constants.ElementAttributes.REQUEST_NAME);
        sdaReq.setStructAlias(Constants.ElementAttributes.REQUEST_ALIAS);
        sdaReq.setXpath(Constants.ElementAttributes.REQUEST_XPATH);
        sdaReq.setSeq(1);
        sdaReq.setServiceHeadId(serviceHeadId);
        sdaReq.setParentId(sdaRoot.getId());

        sdaDAO.save(sdaReq);
        result.add(sdaReq);

        SDA sdaRes = new SDA();
        sdaRes.setId(UUID.randomUUID().toString());
        sdaRes.setStructName(Constants.ElementAttributes.RESPONSE_NAME);
        sdaRes.setStructAlias(Constants.ElementAttributes.RESPONSE_ALIAS);
        sdaRes.setXpath(Constants.ElementAttributes.RESPONSE_XPATH);
        sdaRes.setSeq(2);
        sdaRes.setServiceHeadId(serviceHeadId);
        sdaRes.setParentId(sdaRoot.getId());
        sdaDAO.save(sdaRes);
        result.add(sdaRes);
        return result;
    }

    public List<TreeNode> genderSDATree(String serviceHeadId) {

        List<SDA> list = sdaDAO.findBy("serviceHeadId", serviceHeadId);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("id", "id");
        fields.put("text", "structName");
        fields.put("append1", "structAlias");
        fields.put("append2", "type");
        fields.put("append3", "length");
        fields.put("append4", "metadataId");
        fields.put("append5", "required");
        fields.put("append6", "remark");
        fields.put("append7", "constraint");
        fields.put("append8", "scale");
        fields.put("append9", "serviceHeadId");
        fields.put("attributes", "seq");

        EasyUiTreeUtil eUtil = new EasyUiTreeUtil();

        List<TreeNode> nodeList = eUtil.convertTree(list, fields);
        return nodeList;

    }
}
