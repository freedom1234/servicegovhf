package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.service.StatisticsService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.EasyUiTreeUtil;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.ReleaseVO;
import com.dc.esb.servicegov.vo.ReuseRateVO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.seam.annotations.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService{
    @Autowired
    private SystemDAOImpl systemDAO;
    @Autowired
    private ServiceDAOImpl serviceDAO;
    @Autowired
    private OperationDAOImpl operationDAO;
    @Autowired
    private OperationHisDAOImpl operationHisDAO;
    @Autowired
    private ServiceInvokeDAOImpl serviceInvokeDAO;
    @Autowired
    private ServiceCategoryDAOImpl serviceCategoryDAO;
    @Override
    public long getReuseRateCount(Map<String, String[]> values) {
        ;
        String hql = "select count(*) from " + ServiceInvoke.class.getName() + " as si, " + Operation.class.getName() + " as o where si.type != null and si.serviceId = o.serviceId" +
                " and si.operationId = o.operationId and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")" ;
        if(values.get("type") != null && values.get("type").length > 0){
            if (StringUtils.isNotEmpty(values.get("type")[0])){
                hql += " and si.type = " + values.get("type")[0];
            }
        }
        if(values.get("systemId") != null && values.get("systemId").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemId")[0])) {
                hql += " and si.systemId like '%" + values.get("systemId")[0] + "%'";
            }
        }
        if(values.get("systemName") != null && values.get("systemName").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemName")[0])) {
                hql += " and si.system.systemChineseName like '%" + URLDecoder.decode(values.get("systemName")[0]) + "%'";
            }
        }
        hql += " group by si.systemId, si.type";
        long count = serviceInvokeDAO.find(hql).size();
        return count;
    }
    /**
     * 根据系统id，类型分组
     */
    public List<Object[]> groupBySystemIdType(Map<String, String[]> values, Page page){
        String hql = "select si.systemId, si.type from " + ServiceInvoke.class.getName() + " as si, " + Operation.class.getName()
                + " as o where si.type != null and si.serviceId = o.serviceId" +
                " and si.operationId = o.operationId and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")";
        if(values.get("type") != null && values.get("type").length > 0){
            if (StringUtils.isNotEmpty(values.get("type")[0])) {
                hql += " and si.type = " + values.get("type")[0];
            }
        }
        if(values.get("systemId") != null && values.get("systemId").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemId")[0])) {
                hql += " and si.systemId like '%" + values.get("systemId")[0] + "%'";
            }
        }
        if(values.get("systemName") != null && values.get("systemName").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemName")[0])) {
                hql += " and si.system.systemChineseName like '%" + URLDecoder.decode(values.get("systemName")[0]) + "%'";
            }
        }
        hql += " group by si.systemId, si.type";
        List<Object[]> list = serviceInvokeDAO.findBy(hql, page, new ArrayList<SearchCondition>());
        return list;
    }

    @Override
    public List<ReuseRateVO> getReuseRate(Map<String, String[]> values, Page page) {

        List<Object[]> list = groupBySystemIdType(values, page);

        List<ReuseRateVO> voList = new ArrayList<ReuseRateVO>();
        for(Object[] strs: list){
            com.dc.esb.servicegov.entity.System system = systemDAO.findUniqueBy("systemId", strs[0]);
            ReuseRateVO vo = new ReuseRateVO();
            vo.setType(String.valueOf(strs[1]));
            vo.setSystemChineseName(system.getSystemChineseName());
            vo.setSystemId(String.valueOf(strs[0]));
            long operationNum = getOperationRelaCount(String.valueOf(strs[0]), String.valueOf(strs[1]));
            vo.setOperationNum(String.valueOf(operationNum));//关联场景数
            long operationReuseNum = getOperationReuseCount(String.valueOf(strs[0]), String.valueOf(strs[1]));
            vo.setResueOperationNum(String.valueOf(operationReuseNum));//复用场景数
//            long operationInvokeNum = getOperationInvokeCount(String.valueOf(strs[0]), String.valueOf(strs[1]));
//            vo.setOperationInvokeNum(String.valueOf(operationInvokeNum));//场景消费者系统数
            long serviceNum = getServiceRelaCount(String.valueOf(strs[0]), String.valueOf(strs[1]));
            vo.setServiceNum(String.valueOf(serviceNum));//关联服务数
////            long sum = getServiceInvokeCount( String.valueOf(strs[1]));//提供者或消费者被调用总数
//            long sum = operationDAO.getAllCount();//场景总数
//            vo.setSum(String.valueOf(sum));
//            long useNum = getServiceInvokeCount(String.valueOf(strs[0]), String.valueOf(strs[1]));
//            vo.setUseNum(String.valueOf(useNum));//当前系统作为提供者或消费者被调用次数
//            if(operationInvokeNum > operationNum && operationNum > 0){
//                float r = (operationInvokeNum - operationNum + 0f)/operationInvokeNum;
//                NumberFormat nt = NumberFormat.getPercentInstance();
//                nt.setMinimumFractionDigits(2);
//                vo.setReuseRate(nt.format(r));
            if(operationReuseNum > 0){
//                float r = (operationReuseNum + 0f)/sum;
                //行方要求改为：
                float r = (operationReuseNum + 0f)/operationNum;
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(2);
                vo.setReuseRate(nt.format(r));
            }else{
                vo.setReuseRate("0");
            }
            voList.add(vo);
        }
        return voList;
    }
    /**
     * 根据系统id查询被调用场景数
     * @param systemId
     * @return
     */
    public long getOperationRelaCount(String systemId, String type){
        String hql = "select count(*) from " + ServiceInvoke.class.getName() + " as si, " + Operation.class.getName()
                + " as o where si.serviceId = o.serviceId and si.operationId = o.operationId" +
                " and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")"
                + " and si.systemId = ? and si.type = ? group by si.serviceId, si.operationId";
        long count = serviceInvokeDAO.find(hql, systemId, type).size();
        return count;
    }
//根据系统id，type计算服务场景 消费者数量大于1的场景数
    public long getOperationReuseCount(String systemId, String type){
        String sql = "SELECT COUNT(*) FROM (SELECT COUNT(*) FROM " +
                " (SELECT a1.service_id, a1.operation_id from operation a1, " +
                " (SELECT service_id, operation_id FROM service_invoke WHERE TYPE =? AND system_id=? GROUP BY service_id, operation_id) a2 " +
                " where a1.service_id = a2.service_id and a1.operation_id = a2.operation_id and a1.state in (" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")) a ," +
                " service_invoke b WHERE a.service_id = b.service_id AND a.operation_id = b.operation_id AND TYPE=? GROUP BY b.service_id, b.operation_id HAVING COUNT(*) > 1) c";
        Session session = serviceInvokeDAO.getSession();
        Query query = session.createSQLQuery(sql.toString());
        int i = 0;
        query.setParameter(i, type);
        query.setParameter(1, systemId);
        query.setParameter(2, Constants.INVOKE_TYPE_CONSUMER);
        //db2 返回Integer，mysql返回BigInteger
        Object result = query.uniqueResult();
        if(result != null && result instanceof  BigInteger){
            BigInteger bigCount = (BigInteger)query.uniqueResult();
            return bigCount.longValue();

        }else if(result != null && result instanceof  BigDecimal){
            BigDecimal a = (BigDecimal)query.uniqueResult();
            BigInteger count = new BigInteger(""+a.intValue());
            return count.longValue();
        }else if(result != null && result instanceof  Integer){
            long count = (Integer)query.uniqueResult();
            return count;
        }
        else{
            long count = (Long)query.uniqueResult();
            return count;
        }
    }
    //根据系统id，type计算服务场景 消费者数量大于1的场景数
    public long getOperationReuseCount(List<String> serviceIds){
        String sql = "SELECT COUNT(*) FROM (SELECT COUNT(*) FROM (SELECT si.service_id, si.operation_id FROM service_invoke si, operation o WHERE TYPE = :providerType AND si.service_id in (:serviceIds)" +
                " and si.service_id = o.service_id and si.operation_id = o.operation_id and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")" +
                " GROUP BY si.service_id, si.operation_id) a ," +
                " service_invoke b WHERE a.service_id = b.service_id AND a.operation_id = b.operation_id AND TYPE=:cousumerType GROUP BY b.service_id,  b.operation_id HAVING COUNT(*) > 1) c";
        Session session = serviceInvokeDAO.getSession();
        Query query = session.createSQLQuery(sql.toString());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("providerType", Constants.INVOKE_TYPE_PROVIDER);
        params.put("serviceIds", serviceIds);
        params.put("cousumerType", Constants.INVOKE_TYPE_CONSUMER);
        query.setProperties(params);
        //db2 返回Integer，mysql返回BigInteger
        Object result = query.uniqueResult();
        if(result != null && result instanceof  BigInteger){
            BigInteger bigCount = (BigInteger)query.uniqueResult();
            return bigCount.longValue();

        }else if(result != null && result instanceof  BigDecimal){
            BigDecimal a = (BigDecimal)query.uniqueResult();
            BigInteger count = new BigInteger(""+a.intValue());
            return count.longValue();
        }else if(result != null && result instanceof  Integer) {
            long count = (Integer) query.uniqueResult();
            return count;
        }
        else{
            long count = (Long)query.uniqueResult();
            return count;
        }
    }

    /**
     * @param systemId 系统id
     * @param type 类型
     * @return 系统相关所有场景消费者数量或提供者数量
     */
    public long getOperationInvokeCount(String systemId, String type){
        long count = 0;
        String hql = " select o from " + ServiceInvoke.class.getName() + " as si, " +
                Operation.class.getName() + " as o  where si.serviceId = o.serviceId and si.operationId = o.operationId and si.systemId = ? and si.type = ? ";
        List<Operation> operations = operationDAO.find(hql, systemId, type);
        for(Operation operation : operations){
            String hql2 = " select count(*) from " + ServiceInvoke.class.getName() + " as si where si.serviceId = ? and si.operationId = ? and si.type = ?";
            long singleCount = (Long)serviceInvokeDAO.findUnique(hql2, operation.getServiceId(), operation.getOperationId(), type);
            count += singleCount;
        }
        return count;
    }
    /**
     * 根据系统id查询被调用服务数
     * @param systemId
     * @return
     */
    public long getServiceRelaCount(String systemId, String type){
        String hql = "select count(*) from " + ServiceInvoke.class.getName() + " as si where si.systemId = ? and si.type = ? group by serviceId";
        long count = serviceInvokeDAO.find(hql, systemId, type).size();
        return count;
    }
    /**
     * 根据类型查询调用总数
     */
    public long getServiceInvokeCount(String type){
        String hql = "select count(*) from " + ServiceInvoke.class.getName() + " as si where si.type = ?";

        long count = (Long)serviceInvokeDAO.findUnique(hql, type);
        return count;
    }
    /**
     * 根据系统id。类型查询调用总数
     */
    public long getServiceInvokeCount(String systemId, String type){
        String hql = "select count(*) from " + ServiceInvoke.class.getName() + " as si where si.systemId = ? and si.type = ?";
        long count = (Long)serviceInvokeDAO.findUnique(hql, systemId, type);
        return count;
    }

    @Override
    public long getReleaseVOCount(Map<String, String[]> values) {
        String hql = "select si.systemId, si.type from " + ServiceInvoke.class.getName() + " as si, " + Operation.class.getName()
                + " as o where si.type != null and si.serviceId = o.serviceId" +
                " and si.operationId = o.operationId and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE + ")";;
        if(values.get("type") != null && values.get("type").length > 0){
            if (StringUtils.isNotEmpty(values.get("type")[0])) {
                hql += " and si.type = " + values.get("type")[0];
            }
        }
        if(values.get("systemId") != null && values.get("systemId").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemId")[0])) {
                hql += " and si.systemId like '%" + values.get("systemId")[0] + "%'";
            }
        }
        if(values.get("systemName") != null && values.get("systemName").length > 0){
            if (StringUtils.isNotEmpty(values.get("systemName")[0])) {
                hql += " and si.system.systemChineseName like '%" + URLDecoder.decode(values.get("systemName")[0]) + "%'";
            }
        }
        hql += " group by si.systemId, si.type";
        long count = serviceInvokeDAO.find(hql).size();
        return count;
    }

    @Override
    public List<ReleaseVO> getReleaseCountVO(Map<String, String[]> values, Page page) {

        List<Object[]> list = groupBySystemIdType(values, page);

        List<ReleaseVO> voList = new ArrayList<ReleaseVO>();
        for(Object[] strs : list){
            com.dc.esb.servicegov.entity.System system = systemDAO.findUniqueBy("systemId", strs[0]);
            ReleaseVO vo = new ReleaseVO();
            vo.setType(String.valueOf(strs[1]));
            vo.setSystemChineseName(system.getSystemChineseName());
            vo.setSystemId(String.valueOf(strs[0]));
            setReleaseCount(vo, values);
            voList.add(vo);
        }
        return voList;
    }
    /**
     * 根据系统id, 时间 查询发布场景数
     * @return
     */
    public void setReleaseCount(ReleaseVO vo, Map<String, String[]> values){
        String condition = "";
        if(values.get("startDate") != null && values.get("startDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("startDate")[0])) {
                condition += " and oh.optDate > '" + values.get("startDate")[0] + "' ";
            }
        }
        if(values.get("endDate") != null && values.get("endDate").length > 0){
            if (StringUtils.isNotEmpty(values.get("endDate")[0])) {
                condition += " and oh.optDate < '" + values.get("endDate")[0] + " 23:59:59' ";
            }
        }
        String operationHql = " select count(*) from " + ServiceInvoke.class.getName() + " si, " + OperationHis.class.getName()
                + " oh where si.systemId = ? and si.type = ? and oh.serviceId=si.serviceId and oh.operationId = si.operationId"
                + condition;
        long operationReleaseNum = (Long)operationHisDAO.findUnique(operationHql, vo.getSystemId(), vo.getType());
        String serviceHql = " select count(*) from " + ServiceInvoke.class.getName() + " si, " + OperationHis.class.getName()
                + " oh where si.systemId = ? and si.type = ? and oh.serviceId=si.serviceId and oh.operationId = si.operationId"
                + condition + " group by oh.serviceId";
        long serviceReleaseNum = operationHisDAO.find(serviceHql, vo.getSystemId(), vo.getType()).size();
        vo.setOperationReleaseNum(String.valueOf(operationReleaseNum));
        vo.setServiceReleaseNum(String.valueOf(serviceReleaseNum));

//        String hql = "select new " + OperationPK.class.getName() + "(si.serviceId, si.operationId) from " + ServiceInvoke.class.getName() + " as si where si.systemId = ? and si.type = ? group by serviceId, operationId";
//        List pkList = serviceInvokeDAO.find(hql, vo.getSystemId(), vo.getType());
//        long operationReleaseNum = 0;
//        List<String> serviceIds = new ArrayList<String>();
//        for(int i = 0; i < pkList.size(); i++){
//            OperationPK pk = (OperationPK)pkList.get(i);
//            String hql2 = " select count(*) from " + OperationHis.class.getName() + " as o where o.serviceId=? and operationId=? ";
//            if(values.get("startDate") != null && values.get("startDate").length > 0){
//                if (StringUtils.isNotEmpty(values.get("startDate")[0])) {
//                    hql2 += " and o.optDate > '" + values.get("startDate")[0] + "' ";
//                }
//            }
//            if(values.get("endDate") != null && values.get("endDate").length > 0){
//                if (StringUtils.isNotEmpty(values.get("endDate")[0])) {
//                    hql2 += " and o.optDate < '" + values.get("endDate")[0] + " 23:59:59' ";
//                }
//            }
//            long hisNum = (Long)operationHisDAO.findUnique(hql2, pk.getServiceId(), pk.getOperationId());
//            operationReleaseNum += hisNum;
//            if(!serviceIds.contains(pk.getServiceId()) && hisNum > 0){
//                serviceIds.add(pk.getServiceId());
//            }
//        }
//        vo.setOperationReleaseNum(String.valueOf(operationReleaseNum));
//        vo.setServiceReleaseNum(String.valueOf(serviceIds.size()));
    }
    /*获取发布状态统计*/
    @Override
    public List<ReleaseVO> getReleaseStateVO(Map<String, String[]> values, Page page) {

        List<Object[]> list = groupBySystemIdType(values, page);

        List<ReleaseVO> voList = new ArrayList<ReleaseVO>();
        for(Object[] strs : list){
            com.dc.esb.servicegov.entity.System system = systemDAO.findUniqueBy("systemId", strs[0]);
            ReleaseVO vo = new ReleaseVO();
            vo.setType(String.valueOf(strs[1]));
            vo.setSystemChineseName(system.getSystemChineseName());
            vo.setSystemId(String.valueOf(strs[0]));
            setReleaseState(vo, values);
            voList.add(vo);
        }
        return voList;
    }
    /**
     * 根据系统id,  查询已发布的场景数
     * @return
     */
    public void setReleaseState(ReleaseVO vo, Map<String, String[]> values){
        String operationHql = " select count(*) from " + ServiceInvoke.class.getName() + " si, " + OperationHis.class.getName()
                + " oh where si.systemId = ? and si.type = ? and oh.serviceId=si.serviceId and oh.operationId = si.operationId" +
                " group by oh.serviceId, oh.operationId";
        long operationReleaseNum = operationHisDAO.find(operationHql, vo.getSystemId(), vo.getType()).size();
        String serviceHql = " select count(*) from " + ServiceInvoke.class.getName() + " si, " + OperationHis.class.getName()
                + " oh where si.systemId = ? and si.type = ? and oh.serviceId=si.serviceId and oh.operationId = si.operationId" +
                " group by oh.serviceId";
        long serviceReleaseNum = operationHisDAO.find(serviceHql, vo.getSystemId(), vo.getType()).size();
        vo.setOperationReleaseNum(String.valueOf(operationReleaseNum));
        vo.setServiceReleaseNum(String.valueOf(serviceReleaseNum));
//
//        String hql = "select new " + OperationPK.class.getName() + "(si.serviceId, si.operationId) from " + ServiceInvoke.class.getName() + " as si where si.systemId = ? and si.type = ? group by serviceId, operationId";
//        List pkList = serviceInvokeDAO.find(hql, vo.getSystemId(), vo.getType());
//        long operationReleaseNum = 0;
//        List<String> serviceIds = new ArrayList<String>();
//        for(int i = 0; i < pkList.size(); i++){
//            OperationPK pk = (OperationPK)pkList.get(i);
//            String hql2 = " select count(*) from " + OperationHis.class.getName() + " as o where o.serviceId=? and operationId=?";
//            long count = (Long)operationHisDAO.findUnique(hql2, pk.getServiceId(), pk.getOperationId());
//            if(count > 0){
//                operationReleaseNum ++;
//                if(!serviceIds.contains(pk.getServiceId())){
//                    serviceIds.add(pk.getServiceId());
//                }
//            }
//        }
//        vo.setOperationReleaseNum(String.valueOf(operationReleaseNum));
//        vo.setServiceReleaseNum(String.valueOf(serviceIds.size()));
    }
    /**
     * 从服务分类维度计算复用率
     * @return 复用率
     */
    @Override
    public List<TreeNode> getServiceReuseRate(){
        TreeNode root = new TreeNode();//真是让人蛋疼的数据库设计，为什么不在数据库中直接插一个root节点，每次要手动拼，服务分类和服务明明就可以一张表
        root.setId("root");
        root.setText("服务类");

        List<ServiceCategory> categories = serviceCategoryDAO.getAll();
        Map<String, String > fields =  new HashMap<String, String>();
        fields.put("id", "categoryId");
        fields.put("text", "categoryName");

        List<TreeNode> categoryNodes = EasyUiTreeUtil.getInstance().convertTree(categories, fields);//将分类拼接成树
        root.setChildren(categoryNodes);
        genderCategoryService(root);
        genderServiceReuseRate(root);

        for(int i = 0; i < categoryNodes.size(); i++){//将分类下节点收缩
            TreeNode t = categoryNodes.get(i);
            t.setState("closed");
        }

        List<TreeNode> result = new ArrayList<TreeNode>();
        result.add(root);
        return result;
    }
    @Override
    public List<TreeNode> getServiceReuseRate2(){
        TreeNode root = new TreeNode();
        root.setId("root");
        root.setText("服务类");
        root.setId("root");
        String hql = " from " + ServiceCategory.class.getName() + " where parentId is null";
        List<ServiceCategory> categories = serviceCategoryDAO.find(hql);//先只加载第一层分类
        Map<String, String > fields =  new HashMap<String, String>();
        fields.put("id", "categoryId");
        fields.put("text", "categoryName");

        List<TreeNode> categoryNodes = EasyUiTreeUtil.getInstance().convertTree(categories,fields );//将分类拼接成树
        root.setChildren(categoryNodes);
//        genderCategoryService(root);
        genderServiceReuseRate(root);

        for(int i = 0; i < categoryNodes.size(); i++){//将分类下节点收缩
            TreeNode t = categoryNodes.get(i);
            t.setState("closed");
        }

        List<TreeNode> result = new ArrayList<TreeNode>();
        result.add(root);
        return result;
    }
    public void genderCategoryService(TreeNode categoryNode){//构建服务树
        if (StringUtils.isNotEmpty(categoryNode.getParentId()) && categoryNode.getChildren() == null) {
            List<com.dc.esb.servicegov.entity.Service> services = getService(categoryNode.getId());
            if (services != null && services.size() > 0) {
                List<TreeNode> serviceNodes = new ArrayList<TreeNode>();
                for (com.dc.esb.servicegov.entity.Service service : services) {
                    TreeNode serviceNode = new TreeNode();
                    serviceNode.setId(service.getServiceId());
                    serviceNode.setText(service.getServiceName());
                    serviceNode.setAppend1("service");
                    serviceNodes.add(serviceNode);
                }
                categoryNode.setChildren(serviceNodes);
            }
        }
        List<TreeNode> children = categoryNode.getChildren();
        if(children != null && children.size() > 0){
            for(TreeNode child : children){
                genderCategoryService(child);
            }
        }
    }
    //服务
    @Override
    public List<TreeNode> getServiceCategoryChildren(String categoryId){
        ServiceCategory serviceCategory = serviceCategoryDAO.findUniqueBy("categoryId", categoryId);
        Map<String, String > fields =  new HashMap<String, String>();
        fields.put("id", "categoryId");
        fields.put("text", "categoryName");
        TreeNode categoryNode = EasyUiTreeUtil.getInstance().convertTreeNode(serviceCategory, fields);
        if(serviceCategory != null){
            if (StringUtils.isNotEmpty(categoryNode.getParentId()) && categoryNode.getChildren() == null) {//二级分类
                List<com.dc.esb.servicegov.entity.Service> services = getService(categoryNode.getId());
                if (services != null && services.size() > 0) {
                    List<TreeNode> serviceNodes = new ArrayList<TreeNode>();
                    for (com.dc.esb.servicegov.entity.Service service : services) {
                        TreeNode serviceNode = new TreeNode();
                        serviceNode.setId(service.getServiceId());
                        serviceNode.setText(service.getServiceName());
                        serviceNode.setAppend1("service");
                        serviceNodes.add(serviceNode);
                    }
                    categoryNode.setChildren(serviceNodes);
                }
            }else{//如果是一级分类
                List<ServiceCategory> childList = serviceCategoryDAO.findBy("parentId", serviceCategory.getCategoryId());
                List<TreeNode> children = new ArrayList<TreeNode>();
                if(childList != null && childList.size() > 0){
                    for(ServiceCategory child : childList){
                        TreeNode childNode = EasyUiTreeUtil.getInstance().convertTreeNode(child, fields);
                        List<TreeNode> serviceNodes = getServiceCategoryChildren(child.getCategoryId());
                        if(serviceNodes != null && serviceNodes.size() > 0){
                            childNode.setState("closed");
                            childNode.setChildren(serviceNodes);
                        }
                        children.add(childNode);
                    }
                }
                categoryNode.setChildren(children);
            }
        }
        genderServiceReuseRate(categoryNode);
        return categoryNode.getChildren();
    }
    /**
     * @param treeNode 计算服务分类或服务复用率
     */
    public void genderServiceReuseRate(TreeNode treeNode) {
        String id = treeNode.getId();
        String type = treeNode.getAppend1();
        List<com.dc.esb.servicegov.entity.Service> services;
        if (StringUtils.isNotEmpty(type) && "service".equals(type)) {  //判断传入的id是分类还是服务
            services = serviceDAO.findBy("serviceId", id);
        }
        else{
            services = getService(id);
        }
        long serviceNum = services.size();
        treeNode.setAppend2(String.valueOf(serviceNum)); //服务数
        List<String> serviceIds = new ArrayList<String>();
        for(int i=0; i < services.size(); i++){
            serviceIds.add(services.get(i).getServiceId());
        }
        long sum = operationDAO.getAllCount();
        long operationNum = 0 ;
        long operationInvokeNum = 0;
        long operationReuseNum = 0;

        if(serviceIds.size() > 0){
            String optNumHql = "select count(*) from  "+ Operation.class.getName() + " as o where o.serviceId in (:serviceIds)" + " and o.state in(" + Constants.Operation.LIFE_CYCLE_STATE_PUBLISHED + ", " + Constants.Operation.LIFE_CYCLE_STATE_ONLINE +")";
            Map<String, Object> p1 = new HashMap<String, Object>();
            p1.put("serviceIds", serviceIds);
            p1.put("type", Constants.INVOKE_TYPE_CONSUMER);
            operationNum = (Long)operationDAO.findUnique(optNumHql,p1 );


            String conNumHql = "select count(*)  from " + ServiceInvoke.class.getName() + " as si where si.type=:type and si.serviceId  in (:serviceIds)";
            operationInvokeNum = (Long)serviceInvokeDAO.findUnique(conNumHql,p1 );

            operationReuseNum = getOperationReuseCount(serviceIds);

        }

//        List<Operation> operations = getOperation(services);
//        long operationNum = operations.size();

//        List<ServiceInvoke> consumers = getServiceInvoke(operations, Constants.INVOKE_TYPE_CONSUMER);
//        long operationInvokeNum = consumers.size();

        treeNode.setAppend3(String.valueOf(operationNum));//场景数
        treeNode.setAppend4(String.valueOf(operationReuseNum));//复用场景数
        treeNode.setAppend5(String.valueOf(sum));//场景总数

        if(operationReuseNum > 0){
//            float r = (operationReuseNum + 0f)/sum;
            //行方要求改为：
            float r = (operationReuseNum + 0f)/operationNum;
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            treeNode.setAppend6(nt.format(r));//复用率
        }else{
            treeNode.setAppend6("0");
        }

        List<TreeNode> children = treeNode.getChildren();
        if(children != null && children.size() > 0){
            for(TreeNode child : children){
                genderServiceReuseRate(child);
            }
        }
    }

    /**
     * 迭代：根据分类id查询服务
     * @param categoryId
     * @return
     */
    public List<com.dc.esb.servicegov.entity.Service> getService(String categoryId){
        List<com.dc.esb.servicegov.entity.Service> list = new ArrayList<com.dc.esb.servicegov.entity.Service>();
        List<ServiceCategory> children = serviceCategoryDAO.findBy("parentId", categoryId);
        if("root".equals(categoryId)){
            String hql = " from " + ServiceCategory.class.getName() + " where parentId is null ";
            children = serviceCategoryDAO.find(hql);
        }
        if(children != null && children.size() > 0){
            for(ServiceCategory serviceCategory : children){
                List<com.dc.esb.servicegov.entity.Service> childList = getService(serviceCategory.getCategoryId());
                list.addAll(childList);
            }
        }
        else{
            list = serviceDAO.findBy("categoryId", categoryId);
        }
        return list;
    }
    /**
     * @return
     */
    public List<Operation> getOperation(List<com.dc.esb.servicegov.entity.Service> services){
        List<Operation> operations = new ArrayList<Operation>();
        for(com.dc.esb.servicegov.entity.Service service : services){
            List<Operation> childOperations = operationDAO.findBy("serviceId", service.getServiceId());
            operations.addAll(childOperations);
        }
        return operations;
    }

    public List<ServiceInvoke> getServiceInvoke(List<Operation> operations, String type){
        List<ServiceInvoke> list = new ArrayList<ServiceInvoke>();
        for(Operation operation : operations){
            String hql = " from " + ServiceInvoke.class.getName() + " as si where si.serviceId=? and si.operationId = ? and si.type=?";
            List<ServiceInvoke> childList = serviceInvokeDAO.find(hql, operation.getServiceId(), operation.getOperationId(), type);
            list.addAll(childList);
        }
        return list;
    }
}
