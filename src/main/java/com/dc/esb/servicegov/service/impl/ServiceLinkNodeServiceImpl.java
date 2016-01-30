package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.ServiceLinkNodeDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.entity.ServiceLinkNode;
import com.dc.esb.servicegov.entity.ServiceLinkProperty;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.vo.ServiceLinkNodeVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by vincentfxz on 15/9/7.
 */
@Service
@Transactional
public class ServiceLinkNodeServiceImpl extends AbstractBaseService<ServiceLinkNode, String> {
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ServiceLinkNodeDAOImpl serviceLinkNodeDAO;
    @Autowired
    private ServiceLinkPropertyServiceImpl serviceLinkPropertyService;
    @Autowired
    private OperationServiceImpl operationService;
    @Autowired
    private ServiceServiceImpl serviceService;

    @Override
    public HibernateDAO<ServiceLinkNode, String> getDAO() {
        return serviceLinkNodeDAO;
    }

    public ServiceLinkNodeVO getServiceLinkNode(ServiceInvoke serviceInvoke){
        ServiceLinkNodeVO serviceLinkNodeVO = new ServiceLinkNodeVO(serviceInvoke);
        if (serviceInvoke.getOperationId() != null && serviceInvoke.getServiceId() != null) {
            Operation operation = operationService.getOperation(serviceInvoke.getServiceId(), serviceInvoke.getOperationId());
            com.dc.esb.servicegov.entity.Service service = serviceService.getById(serviceInvoke.getServiceId());
            serviceLinkNodeVO.setServiceName(service.getServiceName());
            if (operation != null){
                serviceLinkNodeVO.setOperationName(operation.getOperationName());
            }else{
                logger.error("serviceInvoke:"+serviceInvoke.getInvokeId());
            }
        }
        List<ServiceLinkProperty> serviceLinkProperties = serviceLinkPropertyService.findBy("invokeId", serviceInvoke.getInvokeId());
        for(ServiceLinkProperty serviceLinkProperty : serviceLinkProperties){
            String propertyName = serviceLinkProperty.getPropertyName();
            String propertyValue = serviceLinkProperty.getPropertyValue();
            if("nodeType".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setNodeType(propertyValue);
            }
            if("location".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setLocation(propertyValue);
            }
            if("bussCategory".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setBussCategory(propertyValue);
            }
            if("status".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setStatus(propertyValue);
            }
            if("esbAccessPattern".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setEsbAccessPattern(propertyValue);
            }
            if("condition".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setCondition(propertyValue);
            }
            if("connectionDesc".equalsIgnoreCase(propertyName)){
                serviceLinkNodeVO.setConnectionDesc(propertyValue);
            }
        }
        return serviceLinkNodeVO;
    }
}
