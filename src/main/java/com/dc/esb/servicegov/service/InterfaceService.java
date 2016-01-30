package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.entity.Interface;
import com.dc.esb.servicegov.service.support.BaseService;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.InterfaceExVO;

import java.util.List;
import java.util.Map;

public interface InterfaceService extends BaseService<Interface, String> {
    public Interface getInterfaceById(String hql, String interfaceId);

    public List<Interface> getBySystemId(String systemId);

    public List<Interface> findByConditions(String condition);

    public List<InterfaceExVO> queryByCondition(Map<String, String[]> values, Page page);

    public List<TreeNode> getLeftTreeBySystems(List<com.dc.esb.servicegov.entity.System> systems);

    public boolean releaseBatch(String interfaceIds, String versionDesc);

    public void save(Interface inter, boolean addSave);

    public List<TreeNode> getSingleSystemTreeNode(String systemId);

    public List<TreeNode> getInterfaceTreeChildren(com.dc.esb.servicegov.entity.System system);

    public List<TreeNode> getHeadTreeChildren(com.dc.esb.servicegov.entity.System system);

    public List<TreeNode> getProtocolTreeChildren(com.dc.esb.servicegov.entity.System system);

    public List<TreeNode> getFileTreeChildren(com.dc.esb.servicegov.entity.System system);


}
