package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.PermissionDAOImpl;
import com.dc.esb.servicegov.dao.impl.RolePermissionRelationDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Permission;
import com.dc.esb.servicegov.entity.PermissionCategory;
import com.dc.esb.servicegov.entity.RolePermissionRelation;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/7/13.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractBaseService<Permission,String> {
    @Autowired
    private PermissionDAOImpl permissionDAOImpl;
    @Autowired
    private RolePermissionRelationDAOImpl rolePermissionRelationDAO;
    @Override
    public HibernateDAO<Permission, String> getDAO() {
        return permissionDAOImpl;
    }

    public List<PermissionTreeBean>  getTreeJson(List<PermissionCategory> permissionCategoryList, List<Permission> permissionList,String roleID){
        List<PermissionTreeBean> treeBeans = new ArrayList<PermissionTreeBean>();
        //权限大类
        List<PermissionTreeBean> parents = new ArrayList<PermissionTreeBean>();
        for (PermissionCategory permissionCategory : permissionCategoryList) {
            PermissionTreeBean treeViewBean = new PermissionTreeBean();
            treeViewBean.setId(permissionCategory.getId());
            treeViewBean.setText(permissionCategory.getChineseName());
            treeViewBean.setType("permissionCategory");
//            treeViewBean.setPermissionCategory(permissionCategory);
            treeBeans.add(treeViewBean);
            //父节点
            if (permissionCategory.getParentId() == null) {
                parents.add(treeViewBean);
            }
        }
        //权限
        for (Permission permission : permissionList) {
            for (PermissionTreeBean per : treeBeans) {
                if (per.getId().equals(permission.getCategoryId())) {
                    PermissionTreeBean treeViewBean = new PermissionTreeBean();
                    treeViewBean.setId(permission.getId());
                    treeViewBean.setText(permission.getChineseName());
                    treeViewBean.setType("permission");
//                    treeViewBean.setPermission(permission);
                    //得到状态
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("roleId",roleID);
                    map.put("permissionId",permission.getId());
                    RolePermissionRelation relation = rolePermissionRelationDAO.findUniqureBy(map);
                    if(null!=relation){
                        treeViewBean.setPermissionState("1");
                    }else {
                        treeViewBean.setPermissionState("0");
                    }
                    List<PermissionTreeBean> children = per.getChildren();
                    children.add(treeViewBean);
                    per.setChildren(children);

                }
            }
        }
        for (PermissionCategory permissionCategory : permissionCategoryList) {
            //子节点
            if (permissionCategory.getParentId() != null) {
                PermissionTreeBean treeViewBean = null;
                for (PermissionTreeBean per : treeBeans) {
                    if (per.getId().equals(permissionCategory.getId())) {
                        treeViewBean = per;
                    }
                }
                //找到父节点
                for (PermissionTreeBean permissionTreeBean : parents) {
                    if (permissionTreeBean.getId().equals(permissionCategory.getParentId())) {
                        List<PermissionTreeBean> children = permissionTreeBean.getChildren();
                        children.add(treeViewBean);
                        permissionTreeBean.setChildren(children);
                    }
                }
            }
        }
        PermissionTreeBean root = new PermissionTreeBean();
        root.setId("root");
        root.setChildren(parents);
        root.setText("权限类");
//        root.setState("close");
        root.setType("root");

        List<PermissionTreeBean> list = new ArrayList<PermissionTreeBean>();
        list.add(root);
        return list;
    }

    public static class PermissionTreeBean{
        List<PermissionTreeBean> children = new ArrayList<PermissionTreeBean>();
        private String id;//permissionId
        private String text;
        private String state;//展开状态
        private String permissionState = "0";//1有权限 0无权限
        private String type;//权限类别   权限
//        private Permission permission;
//        private PermissionCategory permissionCategory;

        public String getPermissionState() {
            return permissionState;
        }

        public void setPermissionState(String permissionState) {
            this.permissionState = permissionState;
        }

        public List<PermissionTreeBean> getChildren() {
            return children;
        }

        public void setChildren(List<PermissionTreeBean> children) {
            this.children = children;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

//        public Permission getPermission() {
//            return permission;
//        }
//
//        public void setPermission(Permission permission) {
//            this.permission = permission;
//        }
//
//        public PermissionCategory getPermissionCategory() {
//            return permissionCategory;
//        }
//
//        public void setPermissionCategory(PermissionCategory permissionCategory) {
//            this.permissionCategory = permissionCategory;
//        }
    }
}
