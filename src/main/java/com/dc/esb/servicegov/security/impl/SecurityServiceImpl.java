package com.dc.esb.servicegov.security.impl;

import com.dc.esb.servicegov.dao.impl.*;
import com.dc.esb.servicegov.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincentfxz on 15/7/22.
 */
@Service
public class SecurityServiceImpl {
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private RoleDAOImpl roleDAO;
    @Autowired
    private UserRoleRelationDAOImpl userRoleRelationDAO;
    @Autowired
    private PermissionDAOImpl permissionDAO;
    @Autowired
    private RolePermissionRelationDAOImpl rolePermissionRelationDAO;

    public SGUser getUserById(String userId) {
        return userDAO.get(userId);
    }

    public List<Role> getRoleOfUser(String userId) {
        List<Role> roles = new ArrayList<Role>();
        List<UserRoleRelation> userRoleRelations = userRoleRelationDAO.findBy("userId", userId);
        for (UserRoleRelation userRoleRelation : userRoleRelations) {
            Role role = roleDAO.get(userRoleRelation.getRoleId());
            roles.add(role);
        }
        return roles;
    }

    public List<Permission> getPermissionOfRole(String roleId) {
        List<Permission> permissions = new ArrayList<Permission>();
        List<RolePermissionRelation> rolePermissionRelations = rolePermissionRelationDAO.findBy("roleId", roleId);
        for(RolePermissionRelation rolePermissionRelation : rolePermissionRelations){
            Permission permission = permissionDAO.get(rolePermissionRelation.getPermissionId());
            permissions.add(permission);
        }
        return permissions;
    }


}
