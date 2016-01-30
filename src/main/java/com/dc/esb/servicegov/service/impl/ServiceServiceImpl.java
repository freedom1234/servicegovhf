package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.OperationDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationHisDAOImpl;
import com.dc.esb.servicegov.dao.impl.SDADAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.OperationHis;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.util.EasyUiTreeUtil;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.RelationVO;
import com.dc.esb.servicegov.vo.SDAVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ServiceServiceImpl extends AbstractBaseService<com.dc.esb.servicegov.entity.Service, String> {

    @Autowired
    private ServiceDAOImpl serviceDAOImpl;

    @Autowired
    private OperationDAOImpl operationDAOImpl;

    @Autowired
    private OperationServiceImpl operationServiceImpl;

    @Autowired
    private OperationHisDAOImpl operationHisDAO;

    @Autowired
    private SDADAOImpl sdaDAO;

    @Override
    public HibernateDAO<com.dc.esb.servicegov.entity.Service, String> getDAO() {
        return serviceDAOImpl;
    }

    public com.dc.esb.servicegov.entity.Service getUniqueByServiceId(String serviceId) {
        return serviceDAOImpl.findUniqueBy("serviceId", serviceId);
    }

    public List<Operation> getOperationByServiceId(String serviceId) {
        String hql = " from Operation a where a.serviceId = ?";
        return operationDAOImpl.find(hql, serviceId);
    }

    public List<TreeNode> genderServiceTree() {
        List<com.dc.esb.servicegov.entity.Service> list = serviceDAOImpl.getAll();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("id", "serviceId");
        fields.put("text", "serviceName");
        fields.put("append1", "version");
        fields.put("append2", "desc");
        fields.put("append3", "remark");

        EasyUiTreeUtil eUtil = new EasyUiTreeUtil();

        return eUtil.convertTree(list, fields);

    }

    public SDAVO getSDAofRelation(RelationVO relation) throws Exception {
        SDAVO root = null;
        if (null != relation) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("serviceId", relation.getServiceId().trim());
            params.put("operationId", relation.getOperationId().trim());
            List<SDA> nodes = sdaDAO.findBy(params, "seq");
            // 获取node节点的属性
            Map<String, SDAVO> sdaMap = new HashMap<String, SDAVO>(nodes.size());
            String tmpPath = "/";
            for (SDA sdaNode : nodes) {
                SDAVO sda = new SDAVO();
                sda.setValue(sdaNode);
                // sda.setProperties(nodeProperties);
                sdaMap.put(sdaNode.getId(), sda);
                String parentResourceId = sdaNode.getParentId();
                if ("/".equalsIgnoreCase(parentResourceId)) {
                    root = sda;
                    sda.setXpath("/");
                }
                String metadataId = sda.getValue().getMetadataId();
                String structName = sda.getValue().getStructName();
                SDAVO parentSDA = sdaMap.get(parentResourceId);

                if (null != parentSDA) {
                    parentSDA.addChild(sda);
                    sda.setXpath(tmpPath + "/" + metadataId);
                    if ("request".equalsIgnoreCase(structName)) {
                        tmpPath = "/request";
                    }
                    if ("response".equalsIgnoreCase(structName)) {
                        tmpPath = "/response";
                    }
                }
            }
            sdaMap = null;
        }
        return root;
    }
    @Override
    public void deleteById(String id){
        //TODO 删除服务前要删除场景
        Map<String,String> map = new HashMap<String, String>();
        map.put("serviceId",id);
        List<Operation> list = operationServiceImpl.findBy(map);
        //TODO 删除operationHis
        List<OperationHis> operationHisList = operationHisDAO.findBy(map);
        operationHisDAO.delete(operationHisList);

        operationServiceImpl.deleteList(list);
        super.deleteById(id);
    }

    /**
     * 前端唯一性验证
     * @param serviceId
     * @return
     */
    public boolean uniqueValid(String serviceId) {
        com.dc.esb.servicegov.entity.Service entity = findUniqueBy("serviceId",serviceId);
        if (entity != null) {
            return false;
        }
        return true;
    }

}
