package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.RoleMenuRelationDAOImpl;
import com.dc.esb.servicegov.dao.impl.RolePermissionRelationDAOImpl;
import com.dc.esb.servicegov.dao.impl.SGMenuDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.RoleMenuRelation;
import com.dc.esb.servicegov.entity.RolePermissionRelation;
import com.dc.esb.servicegov.entity.SGMenu;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Service
@Transactional
public class RoleMenuRelationServiceImpl extends AbstractBaseService<RoleMenuRelation, String> {
    @Autowired
    private RoleMenuRelationDAOImpl roleMenuRelationDAO;
    @Autowired
    private SGMenuDAOImpl sgMenuDAO;
    @Autowired
    private RolePermissionRelationDAOImpl rolePermissionRelationDAO;

    @Override
    public HibernateDAO<RoleMenuRelation, String> getDAO() {
        return roleMenuRelationDAO;
    }

    /**
     * 保存关联关系，如果存在权限，则不改变
     * @param relation
     */
    public void insertRelation(RoleMenuRelation relation){
        roleMenuRelationDAO.insert(relation);
        SGMenu sgMenu = sgMenuDAO.get(relation.getSgMenuId());
        //权限新增
        Map<String,String> params = new HashMap<String, String>();
        params.put("roleId",relation.getRoleId());
        params.put("permissionId",sgMenu.getPermissionId());
        List<RolePermissionRelation> permissionRelations = rolePermissionRelationDAO.findBy(params);
        //如果不存在该权限，则添加
        if(null == permissionRelations || permissionRelations.size() == 0){
            RolePermissionRelation permissionRelation = new RolePermissionRelation();
            permissionRelation.setRoleId(relation.getRoleId());
            permissionRelation.setPermissionId(sgMenu.getPermissionId());
            rolePermissionRelationDAO.insert(permissionRelation);
        }
    }

    /**
     * 删除关联关系，如果有权限，则删除
     * @param relation
     */
    public void deleteRelation(RoleMenuRelation relation){
        roleMenuRelationDAO.delete(relation);
        SGMenu sgMenu = sgMenuDAO.get(relation.getSgMenuId());
        //权限删除
        Map<String,String> params = new HashMap<String, String>();
        params.put("roleId",relation.getRoleId());
        params.put("permissionId",sgMenu.getPermissionId());
        List<RolePermissionRelation> permissionRelations = rolePermissionRelationDAO.findBy(params);
        if(null != permissionRelations || permissionRelations.size()==0){
            rolePermissionRelationDAO.delete(permissionRelations);
        }
    }
}
