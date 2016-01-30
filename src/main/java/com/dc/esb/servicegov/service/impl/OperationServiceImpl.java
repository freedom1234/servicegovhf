package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.InterfaceInvokeDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationDAOImpl;
import com.dc.esb.servicegov.dao.impl.ServiceCategoryDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.InterfaceInvokeService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.EasyUiTreeUtil;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.ConfigVO;
import com.dc.esb.servicegov.vo.OperationExpVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
@Transactional
public class OperationServiceImpl extends AbstractBaseService<Operation, OperationPK> {


    private static final Log log = LogFactory.getLog(OperationServiceImpl.class);

    @Autowired
    private OperationDAOImpl operationDAOImpl;
    @Autowired
    private OperationHisServiceImpl operationHisService;
    @Autowired
    private SDAServiceImpl sdaService;
    @Autowired
    private SDAHisServiceImpl sdaHisService;
    @Autowired
    private SLAServiceImpl slaService;
    @Autowired
    private SLAHisServiceImpl slaHisService;
    @Autowired
    private OLAServiceImpl olaService;
    @Autowired
    private OLAHisServiceImpl olaHisService;
    @Autowired
    private ServiceServiceImpl serviceService;
    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeService;
    @Autowired
    private VersionServiceImpl versionServiceImpl;
    @Autowired
    private ServiceCategoryDAOImpl scDao;
    @Autowired
    private InterfaceInvokeDAOImpl interfaceInvokeDAO;
    @Autowired
    private GeneratorServiceImpl generatorService;

    public List<Operation> getOperationByServiceId(String serviceId) {
        return operationDAOImpl.findBy("serviceId", serviceId);
    }

    /**
     * 获取某个服务的未审核场景
     *
     * @param serviceId
     * @return TODO 请把常量"0"统一管理
     */
    public List<Operation> getUnAuditOperationByServiceId(String serviceId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        //改为取得待审核状态
//        params.put("state", Constants.Operation.OPT_STATE_UNAUDIT);
        params.put("state", Constants.Operation.OPT_STATE_REQUIRE_UNAUDIT);
        return findBy(params);

    }

    /**
     * 根据 服务ID 场景ID获取场景
     *
     * @param serviceId
     * @param operationId
     * @return TODO 请添加 主键类 OperationPK
     */
    public Operation getOperation(String serviceId, String operationId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        List<Operation> list = findBy(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;

    }


    public boolean uniqueValid(String serviceId, String operationId) {
        Operation entity = getOperation(serviceId, operationId);
        if (entity != null) {
            return false;
        }
        return true;
    }

    @Override
    public void save(Operation entity) {
        entity.setOptDate(DateUtils.format(new Date()));
        super.save(entity);
    }

    @Override
    public HibernateDAO getDAO() {
        return operationDAOImpl;
    }

    public boolean addOperation(Operation entity) {
        try {
            String versionId = versionServiceImpl.addVersion(Constants.Version.TARGET_TYPE_OPERATION, entity.getOperationId(), Constants.Version.TYPE_ELSE);
            entity.setVersionId(versionId);
            entity.setDeleted(Constants.DELTED_FALSE);
            operationDAOImpl.save(entity);
            sdaService.genderSDAAuto(entity.getServiceId(), entity.getOperationId());//自动生成sda节点


        } catch (Exception e) {
            log.error(e, e);
            return false;
        }
        return true;
    }

    public boolean editOperation(HttpServletRequest req, Operation entity) {
        try {


            operationDAOImpl.save(entity);
            versionServiceImpl.editVersion(entity.getVersionId());
            //清空
        } catch (Exception e) {
            log.error(e, e);
            return false;
        }
        return true;
    }

    /**
     * sda、sla。ola，等操作修改后调用此方法，更新版本号和场景状态
     *
     * @param serviceId
     * @param operationId
     */
    public void editReleate(String serviceId, String operationId) {
        Operation operation = getOperation(serviceId, operationId);
        versionServiceImpl.editVersion(operation.getVersionId());
        //现在无需修改状态，因为只有服务定义状态，修订状态才能修改
//        operation.setState(Constants.Operation.OPT_STATE_UNAUDIT);/*修改状态*/
        save(operation);
    }

    public void deleteOperations(OperationPK[] operationPks) {
        //TODO 删场景的时候要删除service_invoke
        //TODO 删除场景的时候删除sda
        for (OperationPK operationPK : operationPks) {
            serviceInvokeService.deleteByOperationId(operationPK.getOperationId(), operationPK.getServiceId());
            sdaService.deleteByOperationId(operationPK.getOperationId(), operationPK.getServiceId());
        }
        if (operationPks != null && operationPks.length > 0) {
            for (OperationPK operationPK : operationPks) {
                operationDAOImpl.delete(operationPK);
            }
        }
    }

    public ModelAndView detailPage(HttpServletRequest req, String operationId, String serviceId) {

        ModelAndView mv = new ModelAndView("service/operation/detailPage");
        //根据serviceId查询service信息
        com.dc.esb.servicegov.entity.Service service = serviceService.findUniqueBy("serviceId", serviceId);
        if (service != null) {
            mv.addObject("service", service);
        }
        //根据operationId查询operation
        Operation operation = getOperation(serviceId, operationId);
        if (operation != null) {
            mv.addObject("operation", operation);
        }
        return mv;
    }

    //添加接口映射关系
    public boolean addInvokeMapping(List<LinkedHashMap> serviceInvokes) {
        //清空原有接口关系
        if (serviceInvokes != null && serviceInvokes.size() > 0) {
            LinkedHashMap<String, String> map0 = serviceInvokes.get(0);
            String serviceId = map0.get("serviceId");
            String operationId = map0.get("operationId");
            serviceInvokeService.deleteByOperationId(operationId, serviceId);

            List<ServiceInvoke> consumserList = new ArrayList<ServiceInvoke>();
            List<ServiceInvoke> providerrList = new ArrayList<ServiceInvoke>();
            for (int i = 0; i < serviceInvokes.size(); i++) {
                LinkedHashMap<String, String> map = serviceInvokes.get(i);
                ServiceInvoke serviceInvoke = new ServiceInvoke();

                serviceInvoke.setServiceId(map.get("serviceId"));
                serviceInvoke.setOperationId(map.get("operationId"));
                serviceInvoke.setSystemId(map.get("systemId"));

                String interfaceId = map.get("interfaceId");
                if (StringUtils.isNotEmpty(interfaceId)) {
                    serviceInvoke.setInterfaceId(interfaceId);
                    serviceInvoke.setIsStandard(Constants.INVOKE_TYPE_STANDARD_N);
                } else {
                    serviceInvoke.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
                    serviceInvoke.setRemark("标准");
                }
                serviceInvoke.setType(map.get("type"));
                serviceInvokeService.save(serviceInvoke);
                if (Constants.INVOKE_TYPE_CONSUMER.equals(serviceInvoke.getType())) {
                    consumserList.add(serviceInvoke);
                }
                if (Constants.INVOKE_TYPE_PROVIDER.equals(serviceInvoke.getType())) {
                    providerrList.add(serviceInvoke);
                }
            }
            for (int i = 0; i < consumserList.size(); i++) {
                ServiceInvoke consumer = consumserList.get(i);
                for (int j = 0; j < providerrList.size(); j++) {
                    ServiceInvoke provider = providerrList.get(j);
                    InterfaceInvoke interfaceInvoke = new InterfaceInvoke();
                    interfaceInvoke.setConsumerInvokeId(consumer.getInvokeId());
                    interfaceInvoke.setProviderInvokeId(provider.getInvokeId());
                    interfaceInvokeDAO.save(interfaceInvoke);
                }
            }

        }

        return true;
    }

    /**
     * @param req
     * @param serviceId
     * @param operationId
     * @param consumerStr
     * @param providerStr
     * @return
     */
    public boolean addInvoke(HttpServletRequest req, String serviceId, String operationId, String consumerStr, String providerStr) {
        //清空原有关系
        serviceInvokeService.updateBySO(serviceId, operationId);
        if (!StringUtils.isEmpty(consumerStr)) {
            String[] constrs = consumerStr.split("\\,");
            for (String constr : constrs) {
                if (constr.contains("__invoke__")) { //判断是否是serviceInvokeID
                    constr = constr.replace("__invoke__", "");
                    serviceInvokeService.updateAfterOPAdd(constr, serviceId, operationId, Constants.INVOKE_TYPE_CONSUMER);
                } else {//传入参数为systemId
                    ServiceInvoke si = new ServiceInvoke();
                    si.setSystemId(constr);
                    si.setServiceId(serviceId);
                    si.setOperationId(operationId);
                    si.setType(Constants.INVOKE_TYPE_CONSUMER);
                    si.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
                    serviceInvokeService.save(si);
                }
            }
        }
        if (!StringUtils.isEmpty(providerStr)) {
            String[] prostrs = providerStr.split("\\,");
            for (String prostr : prostrs) {
                if (prostr.contains("__invoke__")) { //判断是否是serviceInvokeID
                    prostr = prostr.replace("__invoke__", "");
                    serviceInvokeService.updateAfterOPAdd(prostr, serviceId, operationId, Constants.INVOKE_TYPE_PROVIDER);
                } else {//传入参数为systemId
                    ServiceInvoke si = new ServiceInvoke();
                    si.setSystemId(prostr);
                    si.setServiceId(serviceId);
                    si.setOperationId(operationId);
                    si.setType(Constants.INVOKE_TYPE_PROVIDER);
                    si.setIsStandard(Constants.INVOKE_TYPE_STANDARD_Y);
                    serviceInvokeService.save(si);
                }
            }
        }
        return true;
    }

    /**
     * 备份operation返回autoId
     *
     * @param serviceId
     * @param operationId
     */
    public OperationHis backUpOperation(String serviceId, String operationId, String versionDesc) {
        Operation operation = getOperation(serviceId, operationId);
        OperationHis operationHis = new OperationHis(operation);
        operationHisService.save(operationHis);
        //修改operationHis 版本
        String versionHisId = versionServiceImpl.releaseVersion(operation.getVersionId(), operationHis.getAutoId(), versionDesc);
        operationHis.setVersionHisId(versionHisId);
        operationHis.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
        operationHisService.save(operationHis);
        return operationHis;
    }

    /**
     * add by liwang
     * review and modify by Vincent Fan
     */
    public boolean releaseBatch(Operation[] operations) {
        if (operations != null && operations.length > 0) {
            for (Operation operation : operations) {
                release(operation.getOperationId(), operation.getServiceId(), operation.getOperationDesc());
            }
        }
        return false;
    }


    /**
     * add by liwang
     * review and modify by Vincent Fan
     */
    public void release(String operationId, String serviceId, String versionDesc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        Operation operation = getOperation(serviceId, operationId);
        if (operation != null) {
            //备份操作基本信息
            if (StringUtils.isNotEmpty(versionDesc)) {
                try {
                    versionDesc = URLDecoder.decode(versionDesc, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e, e);
                }
            }
            OperationHis operationHis = backUpOperation(serviceId, operationId, versionDesc);
            String operationHisAutoId = operationHis.getAutoId();
            sdaService.backUpSdaByCondition(params, operationHisAutoId);
            //备份SLA
            slaService.backUpSLAByCondition(params, operationHisAutoId);
            //备份OLA
            olaService.backUpByCondition(params, operationHisAutoId);
            operation.setState(Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED);
            operationDAOImpl.save(operation);
        }
    }

    public String auditOperation(String state, String auditRemark, String[] operationIds) throws Throwable {
        String logParam = "  ";
        if (operationIds != null && operationIds.length > 0) {
            for (int i = 0; i < operationIds.length; i++) {
                String[] per = operationIds[i].split(",");
                String operationId = per[0];
                String serviceId = per[1];
                Map<String, String> map = new HashMap<String, String>();
                map.put("operationId", operationId);
                map.put("serviceId", serviceId);
                Operation ope = operationDAOImpl.findUniqureBy(map);
                ope.setState(state);
                save(ope);
                Version version = ope.getVersion();
                if (StringUtils.isNotEmpty(auditRemark)) {
                    auditRemark = URLDecoder.decode(auditRemark, "utf-8");
                }
                version.setRemark(auditRemark);
                versionServiceImpl.save(version);
                logParam += "[服务ID:" + serviceId + ", 场景ID:" + operationId + "],";
            }
        }
        return logParam.substring(0, logParam.length() - 2);
    }

    public List<Operation> getReleased(Page page, String serviceId, String serviceName, String operationId, String operationName) {
        return operationDAOImpl.getReleased(page, serviceId, serviceName, operationId, operationName);
    }

    public boolean judgeByMetadataId(String metadataId) {
        //查找场景列表
        long count = operationDAOImpl.getByMetadataIdCount(metadataId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * TODO 根据metadataId查询operation树
     *
     * @param metadataId
     * @return
     */
    public List<TreeNode> getTreeByMetadataId(String metadataId) {
        //查找场景列表
        List<Operation> opList = operationDAOImpl.getByMetadataId(metadataId);
        List<TreeNode> tree = genderTree(opList);

        return tree;
    }

    /**
     * TODO 根据场景列表组装服务场景树
     *
     * @param opList
     * @return
     */
    public List<TreeNode> genderTree(List<Operation> opList) {
        List<TreeNode> tree = new ArrayList<TreeNode>();

        for (int i = 0; i < opList.size(); i++) {
            Operation operation = opList.get(i);
            //场景名称=operationName + （serviceId：operationId）
            String text = operation.getOperationName() + "(" + operation.getServiceId() + ":" + operation.getOperationId() + ")";
            //节点转换
            Map<String, String> opFields = new HashMap<String, String>();
            opFields.put("id", "operationId");
            opFields.put("text", "operationName");
            opFields.put("append1", "operationDesc");
            opFields.put("append2", "serviceId");
            opFields.put("append3", "operationId");
            TreeNode opNode = EasyUiTreeUtil.getInstance().convertTreeNode(operation, opFields);
            opNode.setText(text);
            com.dc.esb.servicegov.entity.Service service = operation.getService();
            Map<String, String> serviceFields = new HashMap<String, String>();
            serviceFields.put("id", "serviceId");
            serviceFields.put("text", "serviceName");
            serviceFields.put("append1", "desc");
            opFields.put("append2", "serviceId");
            TreeNode serviceNode = EasyUiTreeUtil.getInstance().convertTreeNode(service, serviceFields);

            Map<String, String> scFields = new HashMap<String, String>();
            scFields.put("id", "categoryId");
            scFields.put("text", "categoryName");
            scFields.put("parentId", "parentId");
            ServiceCategory sc3 = scDao.findUniqueBy("categoryId", service.getCategoryId());
            TreeNode scNode3 = EasyUiTreeUtil.getInstance().convertTreeNode(sc3, scFields);

            ServiceCategory sc2 = scDao.findUniqueBy("categoryId", sc3.getParentId());
            ;
            TreeNode scNode2 = EasyUiTreeUtil.getInstance().convertTreeNode(sc2, scFields);

            ServiceCategory sc1 = scDao.findUniqueBy("categoryId", sc2.getParentId());
            TreeNode scNode1 = EasyUiTreeUtil.getInstance().convertTreeNode(sc1, scFields);

            opNode.setParentId(serviceNode.getId());//operation的父节点为service
            serviceNode.setParentId(scNode3.getId());//service的父节点为三级分类
            tree.add(opNode);
            if (!tree.contains(serviceNode)) {
                tree.add(serviceNode);
            }
            if (!tree.contains(scNode3)) {
                tree.add(scNode3);
            }
            if (!tree.contains(scNode2)) {
                tree.add(scNode2);
            }
            if (!tree.contains(scNode1)) {
                tree.add(scNode1);
            }
        }

        return EasyUiTreeUtil.getInstance().convertTree(tree, null);
    }

    /**
     * 删除场景要删除service_invoke  SDA
     *
     * @param list
     * @return
     */
    public boolean deleteList(List<Operation> list) {
        for (Operation per : list) {
            serviceInvokeService.deleteByOperationId(per.getOperationId(), per.getServiceId());
            sdaService.deleteByOperationId(per.getOperationId(), per.getServiceId());
            delete(per);
        }
        return true;
    }

    public String genderQueryHql(Map<String, String[]> values) throws Throwable {
        String hql = "";
        if (values != null && values.size() > 0) {
            for (String key : values.keySet()) {
                if (key.equals("serviceId") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.serviceId like '%" + values.get(key)[0] + "%' ";
                    }
                }
                if (key.equals("serviceName") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.service.serviceName like '%" + URLDecoder.decode(values.get(key)[0], "utf-8") + "%' ";
                    }
                }
                if (key.equals("serviceDesc") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.service.desc like '%" + URLDecoder.decode(values.get(key)[0], "utf-8") + "%' ";
                    }
                }
                if (key.equals("serviceState") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.service.state like '%" + values.get(key)[0] + "%' ";
                    }
                }

                if (key.equals("operationId") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.operationId like '%" + values.get(key)[0] + "%' ";
                    }
                }
                if (key.equals("operationName") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.operationName like '%" + URLDecoder.decode(values.get(key)[0], "utf-8") + "%' ";
                    }
                }
                if (key.equals("operationDesc") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.operationDesc like '%" + URLDecoder.decode(values.get(key)[0], "utf-8") + "%' ";
                    }
                }
                if (key.equals("operationState") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and a.state like '%" + values.get(key)[0] + "%' ";
                    }
                }

                if (key.equals("providerId") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        hql += " and ii.provider.systemId like '%" + values.get(key)[0] + "%' ";
                        hql += " and ii.provider.type like '%" + Constants.INVOKE_TYPE_PROVIDER + "%' ";

                    }
                }
                if (key.equals("consumerId") && values.get(key) != null && values.get(key).length > 0) {
                    if (StringUtils.isNotEmpty(values.get(key)[0])) {
                        if (StringUtils.isNotEmpty(values.get(key)[0])) {
                            hql += " and ii.consumer.systemId like '%" + values.get(key)[0] + "%' ";
                            hql += " and ii.consumer.type like '%" + Constants.INVOKE_TYPE_CONSUMER + "%' ";

                        }
                    }
                }

            }
        }
        return hql;
    }

    public long queryCount(Map<String, String[]> values) throws Throwable {
        String hql = "select a.serviceId, a.operationId from " + Operation.class.getName() + " as a ";
        if ((values.get("providerId") != null && values.get("providerId").length > 0) && StringUtils.isNotEmpty(values.get("providerId")[0])
                || (values.get("consumerId") != null && values.get("consumerId").length > 0 && StringUtils.isNotEmpty(values.get("consumerId")[0]))) {
            hql = hql + ", " + InterfaceInvoke.class.getName() + " as ii  where a.serviceId = ii.provider.serviceId and a.operationId = ii.provider.operationId ";
        } else {
            hql += " where 1=1 ";
        }
        hql += genderQueryHql(values);
        hql += " group by a.serviceId, a.operationId";
        return operationDAOImpl.find(hql).size();
    }

    public List<OperationExpVO> queryByCondition(Map<String, String[]> values, Page page) throws Throwable {
        StringBuffer hql = new StringBuffer("select a.serviceId, a.operationId from " + Operation.class.getName() + " as a ");
        if ((values.get("providerId") != null && values.get("providerId").length > 0) && StringUtils.isNotEmpty(values.get("providerId")[0])
                || (values.get("consumerId") != null && values.get("consumerId").length > 0 && StringUtils.isNotEmpty(values.get("consumerId")[0]))) {
            hql.append(", " + InterfaceInvoke.class.getName() + " as ii  where a.serviceId = ii.provider.serviceId and a.operationId = ii.provider.operationId ");
        } else {
            hql.append(" where 1=1 ");
        }
        hql.append(genderQueryHql(values));
        hql.append("  group by a.serviceId, a.operationId order by a.serviceId, a.operationId");
        List<Object[]> strsArray = operationDAOImpl.findBy(hql.toString(), page, new ArrayList<SearchCondition>());
        List<OperationExpVO> voList = new ArrayList<OperationExpVO>();
        for (Object[] strs : strsArray) {
            Operation operation = getOperation(String.valueOf(strs[0]), String.valueOf(strs[1]));
            OperationExpVO vo = new OperationExpVO(operation);
            List<ServiceInvoke> consumerList = serviceInvokeService.getByOperationAndType(operation, Constants.INVOKE_TYPE_CONSUMER);
            String cunsumers = getSystemNames(consumerList);
            vo.setConsumers(cunsumers);

            List<ServiceInvoke> providerList = serviceInvokeService.getByOperationAndType(operation, Constants.INVOKE_TYPE_PROVIDER);
            String providers = getSystemNames(providerList);
            vo.setProviders(providers);

            if (operation.getVersion() != null) {
                vo.setVersion(operation.getVersion().getCode());
            }
            voList.add(vo);
        }
        return voList;
    }

    public String getSystemNames(List<ServiceInvoke> serviceInvokes) {
        String consumer = "";
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < serviceInvokes.size(); i++) {
            ServiceInvoke si = serviceInvokes.get(i);
            if (si.getSystem() != null) {
                if (!temp.contains(si.getSystem().getSystemId())) {
                    temp.add(si.getSystem().getSystemId());
                    if (i != 0) {
                        consumer += ", ";
                    }
                    consumer += si.getSystem().getSystemChineseName();
                }

            }
        }
        return consumer;
    }

    public List<OperationBean> findOperationBy(String serviceId) {
        List<Operation> rows = findBy("serviceId", serviceId);
        List<OperationBean> operList = new ArrayList<OperationBean>();
        for (Operation operation : rows) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("serviceId", operation.getServiceId());
            map.put("operationId", operation.getOperationId());
            List<ServiceInvoke> list = serviceInvokeService.findBy(map);
            String providerSystems = "";
            String consumerSystems = "";
            for (ServiceInvoke per : list) {
                String type = per.getType();
                if (Constants.INVOKE_TYPE_PROVIDER.equals(type)) {//提供方
                    String systemAb = per.getSystem().getSystemAb();
                    if ("".equals(providerSystems)) {
                        providerSystems = systemAb;
                    } else {
                        providerSystems += "," + systemAb;
                    }
                }
                if (Constants.INVOKE_TYPE_CONSUMER.equals(type)) {//调用方
                    String systemAb = per.getSystem().getSystemAb();
                    if ("".equals(consumerSystems)) {
                        consumerSystems = systemAb;
                    } else {
                        consumerSystems += "," + systemAb;
                    }
                }
            }

            operList.add(new OperationBean(operation, providerSystems, consumerSystems));
        }
        return operList;
    }

    public boolean judgeCanRevise(Operation operation) {
        //审核通过，审核不通过，已上线，已发布,已废弃 可以变为修订状态
        String state = operation.getState();
        if (state.equals(Constants.Operation.OPT_STATE_PASS) || state.equals(Constants.Operation.OPT_STATE_UNPASS) || state.equals(Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED)
                || state.equals(Constants.Operation.LIFE_CYCLE_STATE_ONLINE) || state.equals(Constants.Operation.OPT_STATE_ABANDONED)) {
            return true;
        }
        return false;
    }

    public boolean judgeCanQuit(Operation operation) {
        //已上线 可以变为下线状态
        String state = operation.getState();
        if (state.equals(Constants.Operation.LIFE_CYCLE_STATE_ONLINE)) {
            return true;
        }
        return false;
    }

    public List<ConfigVO> getConfigVo(List list) {
        List<ConfigVO> result = new ArrayList<ConfigVO>();
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) list.get(i);
            String serviceId = map.get("serviceId").toString();
            String operationId = map.get("operationId").toString();
            Map<String, String> params = new HashMap<String, String>();
            params.put("serviceId", serviceId);
            params.put("operationId", operationId);
            Operation operation = operationDAOImpl.findUniqureBy(params);

            if (operation != null) {
                List<InterfaceInvoke> iiList = interfaceInvokeDAO.getByOperation(operation.getServiceId(), operation.getOperationId());
                for (InterfaceInvoke ii : iiList) {
                    ConfigVO configVO = new ConfigVO();
                    configVO.setServiceId(operation.getServiceId());
                    configVO.setOperationId(operation.getOperationId());
                    configVO.setOperationName(operation.getOperationName());
                    if (ii != null) {
                        ServiceInvoke siPro = ii.getProvider();
                        if (siPro != null) {
                            if (siPro.getSystem() != null) {
                                configVO.setProviderServiceInvokeId(siPro.getInvokeId());
                                configVO.setProviderName(siPro.getSystem().getSystemChineseName());
                                Interface inter = siPro.getInter();
                                if (null != inter) {
                                    configVO.setProInterfaceName(inter.getInterfaceName());
                                }
                            }
                        }
                        ServiceInvoke siCon = ii.getConsumer();
                        if (siCon != null) {
                            if (siCon.getSystem() != null) {
                                configVO.setConsumerServiceInvokeId(siCon.getInvokeId());
                                configVO.setConsumerName(siCon.getSystem().getSystemChineseName());
                                Interface inter = siCon.getInter();
                                if (null != inter) {
                                    configVO.setConInterfaceName(inter.getInterfaceName());
                                }
                            }
                        }
                    }
                    String prociderName = configVO.getProviderName();
                    Generator generator = generatorService.findLikeName(prociderName.substring(0, prociderName.length() > 3 ? 4 : prociderName.length()));
                    if (generator != null) {
                        configVO.setConIsStandard("1"); //0; 标准， 1：非标
                        configVO.setConGeneratorId(generator.getId());
                        configVO.setConGeneratorName(generator.getName());

                        configVO.setProIsStandard("1"); //0; 标准， 1：非标
                        configVO.setProGeneratorId(generator.getId());
                        configVO.setProGeneratorName(generator.getName());
                    }
//                    if(configVO.getProviderName().indexOf("电票系统")>-1){
//                        configVO.setConIsStandard("1"); //0; 标准， 1：非标
//                        configVO.setConGeneratorId("a8fafe96-7e94-41ee-bca0-2cc92139ec2a");
//                        configVO.setConGeneratorName("电票生成器");
//
//                        configVO.setProIsStandard("1"); //0; 标准， 1：非标
//                        configVO.setProGeneratorId("a8fafe96-7e94-41ee-bca0-2cc92139ec2a");
//                        configVO.setProGeneratorName("电票生成器");
//                    }else if(configVO.getProviderName().indexOf("流程银行")>-1){
//                        configVO.setConIsStandard("0"); //0; 标准， 1：非标
//                        configVO.setConGeneratorId(Constants.DEFAULT_GENERATOR_ID);
//                        configVO.setConGeneratorName("XML标准拆组包生成器");
//
//                        configVO.setProIsStandard("1"); //0; 标准， 1：非标
//                        configVO.setProGeneratorId("aa045689-c92e-4cfc-968b-b8d97ef04e02");
//                        configVO.setProGeneratorName("流程银行生成器");
//                    }
                    else {
                        configVO.setConIsStandard("0"); //0; 标准， 1：非标
                        configVO.setConGeneratorId(Constants.DEFAULT_GENERATOR_ID);
                        configVO.setConGeneratorName("XML标准拆组包生成器");

                        configVO.setProIsStandard("0"); //0; 标准， 1：非标
                        configVO.setProGeneratorId(Constants.DEFAULT_GENERATOR_ID);
                        configVO.setProGeneratorName("XML标准拆组包生成器");
                    }

                    result.add(configVO);
                }

            }

        }

        return result;

    }

    public static class OperationBean {
        private String operationId;

        private String serviceId;

        private String operationName;

        private String operationDesc;

        private String operationRemark;

        private String versionId;

        private String state;

        private String lifeCyscleState;

        private String optUser;

        private String optDate;

        private String headId;
        private String deleted;

        private String providerSystems;

        private String consumerSystems;

        public OperationBean(Operation operation, String providerSystems, String consumerSystems) {
            setOperationId(operation.getOperationId());
            setServiceId(operation.getServiceId());
            setOperationName(operation.getOperationName());
            setOperationDesc(operation.getOperationDesc());
            setOperationRemark(operation.getOperationRemark());
            setVersionId(operation.getVersionId());
            setState(operation.getState());
            setLifeCyscleState(operation.getLifeCyscleState());
            setOptUser(operation.getOptUser());
            setOptDate(operation.getOptDate());
            setHeadId(operation.getHeadId());
            setDeleted(operation.getDeleted());

            setProviderSystems(providerSystems);
            setConsumerSystems(consumerSystems);
        }

        public String getOperationId() {
            return operationId;
        }

        public void setOperationId(String operationId) {
            this.operationId = operationId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public String getOperationDesc() {
            return operationDesc;
        }

        public void setOperationDesc(String operationDesc) {
            this.operationDesc = operationDesc;
        }

        public String getOperationRemark() {
            return operationRemark;
        }

        public void setOperationRemark(String operationRemark) {
            this.operationRemark = operationRemark;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getLifeCyscleState() {
            return lifeCyscleState;
        }

        public void setLifeCyscleState(String lifeCyscleState) {
            this.lifeCyscleState = lifeCyscleState;
        }

        public String getOptUser() {
            return optUser;
        }

        public void setOptUser(String optUser) {
            this.optUser = optUser;
        }

        public String getOptDate() {
            return optDate;
        }

        public void setOptDate(String optDate) {
            this.optDate = optDate;
        }

        public String getHeadId() {
            return headId;
        }

        public void setHeadId(String headId) {
            this.headId = headId;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getProviderSystems() {
            return providerSystems;
        }

        public void setProviderSystems(String providerSystems) {
            this.providerSystems = providerSystems;
        }

        public String getConsumerSystems() {
            return consumerSystems;
        }

        public void setConsumerSystems(String consumerSystems) {
            this.consumerSystems = consumerSystems;
        }
    }

    /**
     * TODO 根据metadataId查询operation树
     *
     * @param interfaceId
     * @return
     */
    public List<TreeNode> getByInterfaceId(String interfaceId) {
        //查找场景列表
        String hql = "select o from Operation o, ServiceInvoke si where o.serviceId = si.serviceId and o.operationId = si.operationId and si.interfaceId = ?";
        List<Operation> opList = operationDAOImpl.find(hql, interfaceId);
        List<TreeNode> tree = genderTree(opList);

        return tree;
    }
}
