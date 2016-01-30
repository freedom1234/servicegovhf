package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.IdaDAOImpl;
import com.dc.esb.servicegov.dao.impl.IdaHISDAOImpl;
import com.dc.esb.servicegov.dao.impl.InterfaceDAOImpl;
import com.dc.esb.servicegov.dao.impl.InterfaceHISDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.*;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.Constants;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.InterfaceExVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.lang.System;
import java.net.URLDecoder;
import java.util.*;

@Service
@Transactional
public class InterfaceServiceImpl extends AbstractBaseService<Interface, String> implements InterfaceService {

    private static final Log log = LogFactory.getLog(InterfaceServiceImpl.class);

    @Autowired
    private InterfaceDAOImpl interfaceDAOImpl;
    @Autowired
    private InterfaceHeadService interfaceHeadService;
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private SystemProtocolService systemProtocolService;
    @Autowired
    private FileManagerService fileManagerService;
    @Autowired
    private InterfaceHISDAOImpl interfaceHISDAO;
    @Autowired
    private IdaDAOImpl idaDAO;
    @Autowired
    private IdaHISDAOImpl idaHISDAO;
    @Autowired
    private VersionServiceImpl versionServiceImpl;
    @Autowired
    private SystemServiceImpl systemService;

    /**
     * TODO 这里为什么要这么做
     *
     * @return
     */
    @Override
    public HibernateDAO<Interface, String> getDAO() {
        return interfaceDAOImpl;
    }

    public Interface getInterfaceById(String hql, String interfaceId) {

        List<SearchCondition> searchConds = new ArrayList<SearchCondition>();
        SearchCondition search = new SearchCondition();
        search.setField("interfaceId");
        search.setFieldValue(interfaceId);
        searchConds.add(search);
        List<Interface> inters = interfaceDAOImpl.findBy(hql, searchConds);
        System.out.print(inters.size() + "========================================");
        Interface inter = inters.get(0);
        System.out.print(inter);
        return inter;
    }

    public List<Interface> getBySystemId(String systemId) {
        String hql = "select distinct i from " + Interface.class.getName() + " as i , " + ServiceInvoke.class.getName()
                + " as si where i.interfaceId = si.interfaceId and si.systemId = ?";
        List<Interface> list = interfaceDAOImpl.find(hql, systemId);
        return list;
    }

    public List<Interface> findByConditions(String condition) {
        condition = "%" + condition + "%";
        StringBuffer hql = new StringBuffer("select t from Interface t where t.interfaceId like ?");
        hql.append(" or t.desc like ?");
        hql.append(" or t.interfaceName like ?");
        hql.append(" or t.ecode like ?");
        List<Interface> list = interfaceDAOImpl.find(hql.toString(), condition, condition, condition, condition);
        return list;
    }

    @Override
    public List<InterfaceExVO> queryByCondition(Map<String, String[]> values, Page page) {
        return new ArrayList<InterfaceExVO>();
    }


    @Override
    public List<TreeNode> getLeftTreeBySystems(List<com.dc.esb.servicegov.entity.System> systems) {
        List<TreeNode> rootList = new ArrayList<TreeNode>();
        for (com.dc.esb.servicegov.entity.System system : systems) {
            List<TreeNode> systemTreeChildren = getSingleSystemTreeNode(system.getSystemId());
            //构造系统节点
            TreeNode rootinterface = new TreeNode();
            rootinterface.setId(system.getSystemId());
            rootinterface.setText(system.getSystemChineseName());
            rootinterface.setClick("disable");
            rootinterface.setChildren(systemTreeChildren);
            rootList.add(rootinterface);
        }
        //返回整个系统节点数组
        return rootList;
    }

    /**
     * 获取单个系统下的树节点
     *
     * @param systemId
     * @return
     */
    public List<TreeNode> getSingleSystemTreeNode(String systemId) {
        List<TreeNode> systemTreeChildren = new ArrayList<TreeNode>();
        com.dc.esb.servicegov.entity.System system = systemService.getById(systemId);
        if (null != system) {
            //构造接口操作节点
            TreeNode interfacesNode = new TreeNode();
            interfacesNode.setId(system.getSystemId()+"_interface");
            interfacesNode.setText("接口");
            interfacesNode.setClick("interfaces");
            List<TreeNode> interfaceTreeChildren = getInterfaceTreeChildren(system);
            interfacesNode.setChildren(interfaceTreeChildren);
            //构造报文头操作节点
            TreeNode headsNode = new TreeNode();
            headsNode.setId(system.getSystemId()+"_interface");
            headsNode.setText("报文头");
            headsNode.setClick("heads");
            //构造报文头数据节点
            List<TreeNode> headTreeNodes = getHeadTreeChildren(system);
            headsNode.setChildren(headTreeNodes);
            //构造协议操作节点
            TreeNode protocolNode = new TreeNode();
            protocolNode.setId(system.getSystemId()+"_interface");
            protocolNode.setText("协议");
            protocolNode.setClick("protocols");
            protocolNode.setTarget("/interface/getLeftTree/subProtocolTree/system/" + system.getSystemId());
            //构造协议数据节点
            List<TreeNode> protocolTreeNodes = getProtocolTreeChildren(system);
            protocolNode.setChildren(protocolTreeNodes);
            //构造文档操作节点
            TreeNode fileNode = new TreeNode();
            fileNode.setId(system.getSystemId()+"_interface");
            fileNode.setText("文档");
            fileNode.setClick("files");
            //构造文档的数据节点
            List<TreeNode> fileTreeNodes = getFileTreeChildren(system);
            fileNode.setChildren(fileTreeNodes);

            systemTreeChildren.add(interfacesNode);
            systemTreeChildren.add(headsNode);
            systemTreeChildren.add(protocolNode);
            systemTreeChildren.add(fileNode);
        }
        return systemTreeChildren;
    }

    public List<TreeNode> getInterfaceTreeChildren(com.dc.esb.servicegov.entity.System system){
        List<TreeNode> interfaceTreeChildren = new ArrayList<TreeNode>();
        try {
            List<ServiceInvoke> serviceIns = system.getServiceInvokes();
            for (ServiceInvoke si : serviceIns) {
                TreeNode child = new TreeNode();
                if (null == si.getInter()) {
                    continue;
                }
                child.setId(si.getInter().getInterfaceId());
                child.setText(si.getInter().getInterfaceName() + "(" + si.getInter().getInterfaceId() + ")");
                child.setAppend1(si.getInterfaceId());
                if (!contains(interfaceTreeChildren, child)) {
                    interfaceTreeChildren.add(child);
                }
            }
            Collections.sort(interfaceTreeChildren, new Comparator<TreeNode>() {

                @Override
                public int compare(TreeNode o1, TreeNode o2) {
                    return o1.getAppend1().compareToIgnoreCase(o2.getAppend1());
                }
            });
        } catch (Exception e) {
            log.error(e, e);
        }
        return interfaceTreeChildren;
    }

    public List<TreeNode> getHeadTreeChildren(com.dc.esb.servicegov.entity.System system){
        List<InterfaceHead> heads = interfaceHeadService.findBy("systemId", system.getSystemId());
        List<TreeNode> headTreeNodes = new ArrayList<TreeNode>();
        for (InterfaceHead head : heads) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(head.getHeadId());
            treeNode.setClick("head");
            treeNode.setText(head.getHeadName());
            headTreeNodes.add(treeNode);
        }
        return headTreeNodes;
    }

    public List<TreeNode> getProtocolTreeChildren(com.dc.esb.servicegov.entity.System system){
        List<TreeNode> protocolTreeNodes = new ArrayList<TreeNode>();
        List<SystemProtocol> systemProtocols = systemProtocolService.findBy("systemId", system.getSystemId());
        for (SystemProtocol systemProtocol : systemProtocols) {
            String protocolId = systemProtocol.getProtocolId();
            Protocol protocol = protocolService.getById(protocolId);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(protocol.getProtocolId());
            treeNode.setClick("protocol");
            treeNode.setText(protocol.getProtocolName());

            protocolTreeNodes.add(treeNode);
        }
        return protocolTreeNodes;
    }

    public List<TreeNode> getFileTreeChildren(com.dc.esb.servicegov.entity.System system){
        List<FileManager> files = fileManagerService.findBy("systemId", system.getSystemId());
        List<TreeNode> fileTreeNodes = new ArrayList<TreeNode>();
        for (FileManager file : files) {
            String fileName = file.getFileName();
            String id = file.getFileId();
            TreeNode treeNode = new TreeNode();
            treeNode.setId(id);
            treeNode.setText(fileName);
            treeNode.setClick("file");
            fileTreeNodes.add(treeNode);
        }
        return fileTreeNodes;
    }



    private boolean contains(List<TreeNode> childList, TreeNode treeNode) {
        for (TreeNode node : childList) {
            if (node.getId().equals(treeNode.getId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param interfaceIds 接口id用‘,’拼接的字符串
     * @return
     */
    @Override
    public boolean releaseBatch(String interfaceIds, String versionDesc) {
        if (StringUtils.isNotEmpty(interfaceIds)) {
            if(StringUtils.isNotEmpty(versionDesc)){
                try {
                    versionDesc = URLDecoder.decode(versionDesc, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e, e);
                }
            }
            String[] ids = interfaceIds.split("\\,");
            for (int i = 0; i < ids.length; i++) {
                try {
                    release(ids[i], versionDesc);
                } catch (Exception e) {
                    log.error(e, e);
                    continue;
                }
            }
        }
        return true;
    }

    /**
     * @param interfaceId 接口id
     * @param versionDesc 版本描述
     * @return
     */
    public boolean release(String interfaceId, String versionDesc) {
        //对于每一个接口备份interfaceHis，versionHis，idaHis,（interfaceHead,  protocolHis关系暂时不加入版本管理）
        Interface inter = interfaceDAOImpl.findUniqueBy("interfaceId", interfaceId);
        InterfaceHIS interfaceHis = new InterfaceHIS(inter);
        interfaceHISDAO.save(interfaceHis);//备份interfaceHis
        if(StringUtils.isEmpty(inter.getVersionId())){//如果当前接口没有版本信息（之前接口没有版本功能）
            String versionId = versionServiceImpl.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(), Constants.Version.TYPE_ELSE);
            inter.setVersionId(versionId);
        }
        String versionHisId = versionServiceImpl.releaseVersion(inter.getVersionId(), interfaceHis.getAutoId(), versionDesc);//生成发布版本，返回versionHisId
        interfaceHis.setVersionHisId(versionHisId);
        interfaceHISDAO.save(interfaceHis);//关联接口历史版本
        String hql = " from " + Ida.class.getName() + " where interfaceId = ?";
        List<Ida> idas = idaDAO.find(hql, inter.getInterfaceId());
        for (int i = 0; i < idas.size(); i++) {//生成ida历史
            IdaHIS idaHIS = new IdaHIS(idas.get(i), interfaceHis.getAutoId());
            idaHIS.setInterfaceHisId(interfaceHis.getAutoId());
            idaHISDAO.save(idaHIS);
        }
        return true;
    }

    /**
     * @param inter   接口
     * @param addSave 新增/修改
     */
    @Override
    public void save(Interface inter, boolean addSave) {
        //新增操作
        if (addSave) {
            String versionId = versionServiceImpl.addVersion(Constants.Version.TARGET_TYPE_INTERFACE, inter.getInterfaceId(), Constants.Version.TYPE_ELSE);
            inter.setVersionId(versionId);

        } else {//修改操作
            versionServiceImpl.editVersion(inter.getVersionId());
        }
        inter.setOptUser(SecurityUtils.getSubject().getPrincipal().toString());
        inter.setOptDate(DateUtils.format(new Date()));
        interfaceDAOImpl.save(inter);
    }

}
