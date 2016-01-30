package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.Operation;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.Service;
import com.dc.esb.servicegov.entity.ServiceCategory;
import com.dc.esb.servicegov.service.impl.*;
import com.dc.esb.servicegov.util.DateUtils;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.ServiceTreeViewBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.sql.BatchUpdateException;
import java.util.*;

@Controller
@RequestMapping("/service")
public class ServiceController {

    private static final Log log = LogFactory.getLog(ServiceController.class);

    @Autowired
    private SystemLogServiceImpl systemLogService;

    @Autowired
    private ServiceServiceImpl serviceServiceImpl;
    @Autowired
    private OperationServiceImpl operationServiceImpl;
    @Autowired
    private ServiceCategoryServiceImpl serviceCategoryServiceImpl;
    @Autowired
    private ProcessContextServiceImpl processContextService;

    @RequiresPermissions({"service-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getTree", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceTreeViewBean> getAllTreeBean() {
        List<ServiceCategory> serviceCategoryList = serviceCategoryServiceImpl.getAll();
        List<Service> serviceList = serviceServiceImpl.getAll("serviceId", true);
        return getTreeJson(serviceCategoryList, serviceList);
    }

    @RequiresPermissions({"service-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addService", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addService(@RequestBody Service service) {
        OperationLog operationLog = systemLogService.record("服务","添加","服务名称：" + service.getServiceName());
        //TODO 新增相同id就被覆盖了
        try {
            serviceServiceImpl.insert(service);
        } catch (DataIntegrityViolationException e) {
            log.error(e,e);
            return false;
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addService/{processId}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addServiceWithProcess(@RequestBody Service service, @PathVariable String processId) {
        OperationLog operationLog = systemLogService.record("服务","添加(任务)","服务名称：" + service.getServiceName());
        //TODO 新增相同id就被覆盖了
        String optUser = SecurityUtils.getSubject().getPrincipal().toString();
        String optDate = DateUtils.format(new Date());
        try {
            serviceServiceImpl.insert(service);
            com.dc.esb.servicegov.entity.ProcessContext processContext = new com.dc.esb.servicegov.entity.ProcessContext();
            processContext.setName("服务定义");
            processContext.setProcessId(processId);
            processContext.setKey("service");
            processContext.setValue(service.getServiceId());
            processContext.setType("result");
            processContext.setRemark("添加服务[" + service.getServiceId() + "(" + service.getServiceName() + ")" + "]");
            processContext.setOptDate(optDate);
            processContext.setOptUser(optUser);
            processContextService.save(processContext);
        } catch (DataIntegrityViolationException e) {
            log.error(e,e);
            return false;
        }

        systemLogService.updateResult(operationLog);
        return true;
    }

    /**
     * 服务serviceId唯一性验证
     *
     * @param serviceId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uniqueValid", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean uniqueValid(String serviceId) {
        return serviceServiceImpl.uniqueValid(serviceId);
    }

    @RequiresPermissions({"service-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/editService", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean editService(@RequestBody Service service) {
        OperationLog operationLog = systemLogService.record("服务","修改","服务名称：" + service.getServiceName());

        serviceServiceImpl.save(service);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteService/{Id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteService(@PathVariable String Id) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("serviceId",Id);
        List<Operation> list = operationServiceImpl.findBy(map);
        if(list.size() > 0) return false;
        OperationLog operationLog = systemLogService.record("服务","删除","服务ID：" +Id);

        serviceServiceImpl.deleteById(Id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-add"})
    @RequestMapping(method = RequestMethod.POST, value = "/addServiceCategory", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean addServiceCategory(@RequestBody ServiceCategory serviceCategory) {
        OperationLog operationLog = systemLogService.record("服务分类", "添加", "分类名称：" + serviceCategory.getCategoryName());

        serviceCategoryServiceImpl.save(serviceCategory);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-update"})
    @RequestMapping(method = RequestMethod.POST, value = "/editServiceCategory", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean editService(@RequestBody ServiceCategory serviceCategory) {
        OperationLog operationLog = systemLogService.record("服务分类","修改","分类名称：" + serviceCategory.getCategoryName());

        serviceCategoryServiceImpl.save(serviceCategory);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"serviceCategory-delete"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteServiceCategory/{Id}", headers = "Accept=application/json")
    public
    @ResponseBody
    boolean deleteServiceCategory(@PathVariable String Id) {
        try {
            Id = java.net.URLDecoder.decode(Id,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //大类下是否有服务,大类下是否有小类
        List<Service> list = serviceServiceImpl.findBy("categoryId", Id);
        List<ServiceCategory> list2 = serviceCategoryServiceImpl.findBy("parentId",Id);
        if(list2.size()>0) return false;
        if(list.size()>0) return false;
        OperationLog operationLog = systemLogService.record("服务分类", "删除", "分类ID：" + Id);

        serviceCategoryServiceImpl.deleteById(Id);

        systemLogService.updateResult(operationLog);
        return true;
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/searchService/{serviceName}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceTreeViewBean> searchService(@PathVariable String serviceName) {
        List<ServiceTreeViewBean> TreeViewBeans = new ArrayList<ServiceTreeViewBean>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceName", serviceName);
        List<Service> serviceList = serviceServiceImpl.findLike(params);
        List<String> categoryIdList = new ArrayList<String>();
        for (Service service : serviceList) {
            categoryIdList.add(service.getCategoryId());
        }
        if (categoryIdList.size() != 0) {
            List<ServiceCategory> serviceCategoryList = serviceCategoryServiceImpl.getCategoryHierarchyByLeafIds(categoryIdList);
            TreeViewBeans = getTreeJson(serviceCategoryList, serviceList);
        }
        return TreeViewBeans;

    }

    @RequiresPermissions({"service-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/searchService/processId/{processId}", headers = "Accept=application/json")
    public
    @ResponseBody
    List<ServiceTreeViewBean> searchServiceByProcessId(@PathVariable("processId") String processId) {
        List<ServiceTreeViewBean> TreeViewBeans = new ArrayList<ServiceTreeViewBean>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("processId", processId);
        List<Service> serviceList = serviceServiceImpl.findLike(params);
        List<String> categoryIdList = new ArrayList<String>();
        for (Service service : serviceList) {
            categoryIdList.add(service.getCategoryId());
        }
        if (categoryIdList.size() != 0) {
            List<ServiceCategory> serviceCategoryList = serviceCategoryServiceImpl.getCategoryHierarchyByLeafIds(categoryIdList);
            TreeViewBeans = getTreeJson(serviceCategoryList, serviceList);
        }
        return TreeViewBeans;

    }

    private List<ServiceTreeViewBean> getTreeJson(List<ServiceCategory> serviceCategoryList, List<Service> serviceList) {
        List<ServiceTreeViewBean> treeBeans = new ArrayList<ServiceTreeViewBean>();
        //服务类
        List<ServiceTreeViewBean> parents = new ArrayList<ServiceTreeViewBean>();
        for (ServiceCategory serviceCategory : serviceCategoryList) {
            ServiceTreeViewBean treeViewBean = new ServiceTreeViewBean();
            treeViewBean.setId(serviceCategory.getCategoryId());
            treeViewBean.setText(serviceCategory.getCategoryName() + "(" + serviceCategory.getCategoryId()+")");
            treeViewBean.setType("serviceCategory");
            treeViewBean.setServiceCategory(serviceCategory);
            treeBeans.add(treeViewBean);
            //父节点
            if (serviceCategory.getParentId() == null) {
                parents.add(treeViewBean);
            }
        }
        //服务
        for (Service service : serviceList) {
            for (ServiceTreeViewBean per : treeBeans) {
                if (per.getId().equals(service.getCategoryId())) {
                    ServiceTreeViewBean treeViewBean = new ServiceTreeViewBean();
                    treeViewBean.setId(service.getServiceId());
                    treeViewBean.setText(service.getServiceName() + "("+service.getServiceId()+")");
                    treeViewBean.setType("service");
                    treeViewBean.setService(service);
                    List<ServiceTreeViewBean> children = per.getChildren();
                    children.add(treeViewBean);
                    per.setChildren(children);

                }
            }
        }
        for (ServiceCategory serviceCategory : serviceCategoryList) {
            //子节点
            if (serviceCategory.getParentId() != null) {
                ServiceTreeViewBean treeViewBean = null;
                for (ServiceTreeViewBean per : treeBeans) {
                    if (per.getId().equals(serviceCategory.getCategoryId())) {
                        treeViewBean = per;
                    }
                }
                //找到父节点
                for (ServiceTreeViewBean serviceTreeViewBean : parents) {
                    if (serviceTreeViewBean.getId().equals(serviceCategory.getParentId())) {
                        List<ServiceTreeViewBean> children = serviceTreeViewBean.getChildren();
                        children.add(treeViewBean);
                        serviceTreeViewBean.setChildren(children);
                    }
                }
            }
        }
        ServiceTreeViewBean root = new ServiceTreeViewBean();
        root.setId("root");
        root.setChildren(parents);
        root.setText("服务类");
        root.setState("close");
        root.setType("root");

        List<ServiceTreeViewBean> list = new ArrayList<ServiceTreeViewBean>();
        list.add(root);
        return list;
    }

    //根据服务id跳转到服务基本信息页面
    @RequiresPermissions({"service-get"})
    @RequestMapping("/serviceGrid")
    public ModelAndView serviceGrid(String serviceId) {
        ModelAndView mv = new ModelAndView("service/serviceGrid");
        Service entity = serviceServiceImpl.getUniqueByServiceId(serviceId);
        if (entity != null) {
            mv.addObject("entity", entity);
        }
        return mv;
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping("/serviceAppandForm/process/{processId}")
    public ModelAndView serviceAppandForm(@PathVariable("processId") String processId) {
        ModelAndView mv = new ModelAndView("service/serviceGrid");
        mv.addObject(processId);
        return mv;
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping("serviceTree")
    public List<TreeNode> serviceTree() {
        return serviceServiceImpl.genderServiceTree();
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept=application/json")
    public
    @ResponseBody
    List<Service> getAll() {
        return serviceServiceImpl.getAll();
    }

    @RequiresPermissions({"service-get"})
    @RequestMapping("serviceCategorys")
    public
    @ResponseBody
    List<ServiceCategory> serviceCategorys() {
        String hql = " from " + ServiceCategory.class.getName() + " where parentId is null";
        List<ServiceCategory> list = serviceCategoryServiceImpl.find(hql);
        return list;
    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public String processUnauthorizedException() {
        return "403";
    }

}
