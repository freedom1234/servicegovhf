package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.impl.RoleMenuRelationDAOImpl;
import com.dc.esb.servicegov.dao.impl.SGMenuDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.RoleMenuRelation;
import com.dc.esb.servicegov.entity.SGMenu;
import com.dc.esb.servicegov.entity.SGMenuCategory;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangqi on 2015/10/14.
 */
@Service
@Transactional
public class SGMenuServiceImpl extends AbstractBaseService<SGMenu,String> {
    @Autowired
    private SGMenuDAOImpl sgMenuDAO;
    @Autowired
    private RoleMenuRelationDAOImpl roleMenuRelationDAO;
    @Override
    public HibernateDAO<SGMenu, String> getDAO() {
        return sgMenuDAO;
    }

    public List<PermissionTreeBean> getTreeJson(List<SGMenuCategory> sgMenuCategoryList, List<SGMenu> sgMenuList,String roleID){
        List<PermissionTreeBean> treeBeans = new ArrayList<PermissionTreeBean>();
        //菜单大类
        List<PermissionTreeBean> parents = new ArrayList<PermissionTreeBean>();
        for (SGMenuCategory sgMenuCategory : sgMenuCategoryList) {
            PermissionTreeBean treeViewBean = new PermissionTreeBean();
            treeViewBean.setId(sgMenuCategory.getId());
            treeViewBean.setText(sgMenuCategory.getChineseName());
            treeViewBean.setType("permissionCategory");
            treeBeans.add(treeViewBean);
            //父节点
            if (sgMenuCategory.getParentId() == null) {
                parents.add(treeViewBean);
            }
        }
        //菜单
        for (SGMenu sgMenu : sgMenuList) {
            for (PermissionTreeBean per : treeBeans) {
                if (per.getId().equals(sgMenu.getSgMenuCategoryId())) {
                    PermissionTreeBean treeViewBean = new PermissionTreeBean();
                    treeViewBean.setId(sgMenu.getId());
                    treeViewBean.setText(sgMenu.getName());
                    treeViewBean.setType("permission");
                    treeViewBean.setPermissionId(sgMenu.getPermissionId());
                    //得到状态
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("roleId",roleID);
                    map.put("sgMenuId",sgMenu.getId());
                    RoleMenuRelation relation = roleMenuRelationDAO.findUniqureBy(map);
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
        for (SGMenuCategory sgMenuCategory : sgMenuCategoryList) {
            //子节点
            if (sgMenuCategory.getParentId() != null) {
                PermissionTreeBean treeViewBean = null;
                for (PermissionTreeBean per : treeBeans) {
                    if (per.getId().equals(sgMenuCategory.getId())) {
                        treeViewBean = per;
                    }
                }
                //找到父节点
                for (PermissionTreeBean permissionTreeBean : parents) {
                    if (permissionTreeBean.getId().equals(sgMenuCategory.getParentId())) {
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
        private String id;//sgMenuId
        private String text;
        private String state;//展开状态
        private String permissionState = "0";//1有权限 0无权限
        private String type;//权限类别   权限
        private String permissionId;//权限id

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

        public String getPermissionId() {
            return permissionId;
        }

        public void setPermissionId(String permissionId) {
            this.permissionId = permissionId;
        }
    }
}
