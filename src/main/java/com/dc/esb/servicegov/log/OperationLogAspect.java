package com.dc.esb.servicegov.log;

import com.dc.esb.servicegov.dao.impl.OperationLogDAOImpl;
import com.dc.esb.servicegov.dao.impl.OperationLogTypeDAOImpl;
import com.dc.esb.servicegov.entity.OperationLog;
import com.dc.esb.servicegov.entity.OperationLogType;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/4.
 */
@Aspect
public class OperationLogAspect {
    private static final Log log = LogFactory.getLog(OperationLogAspect.class);
    @Autowired
    OperationLogTypeDAOImpl operationLogTypeDAO;
    @Autowired
    OperationLogDAOImpl operationLogDAO;

    public void supportLog(JoinPoint joinPoint, String optType) throws Throwable {
        OperationLog operationLog = saveOperationLog(joinPoint);
        OperationLogType optLogType = getOptType(joinPoint);
        if (optLogType != null) {
            operationLog.setOptType(optLogType.getOperationName());
            operationLog.setChineseName(optLogType.getChineseName());
        } else {
            operationLog.setOptType(optType);
        }
        String[] names = operationLog.getClassName().split("\\.");
        String name = names[names.length - 1];
        operationLog.setChineseName(name);
        operationLog.setOptResult("成功");
        operationLogDAO.save(operationLog);
    }

    //*通用保存*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.save(..))")
    public void afterServiceImplSave(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "保存");
    }

    //*通用新增*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.insert(..))")
    public void afterServiceImplInsert(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "新增");
    }

    //*通用更新*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.update(..))")
    public void afterServiceImplUpdate(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "更新");
    }

    //*通用删除*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.delete(..))")
    public void afterServiceImplDelete(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "删除");
    }

    //*通用删除*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.deleteById(..))")
    public void afterServiceImplDeleteById(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "删除byId");
    }

    /*全部删除*/
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.deleteAll(..))")
    public void afterServiceImpldeleteAll(JoinPoint joinPoint) throws Throwable {
        supportLog(joinPoint, "全部删除");
    }

    /**
     * 操作日志(后置通知)
     *
     * @param joinPoint
     * @throws Throwable
     */
    @AfterReturning("execution(* com.dc.esb.servicegov.service.*.*.*(..))")
    public void afterServiceImplCalls(JoinPoint joinPoint) throws Throwable {
        OperationLogType optType = getOptType(joinPoint);
        if (optType != null) {
            OperationLog operationLog = saveOperationLog(joinPoint);
            operationLog.setOptType(optType.getOperationName());
            operationLog.setChineseName(optType.getChineseName());
            operationLog.setOptResult("成功");
            operationLogDAO.save(operationLog);
        }
    }

    /**
     * 操作日志(后置通知)
     *
     * @param joinPoint
     * @throws Throwable
     */
    @AfterThrowing("execution(* com.dc.esb.servicegov.service.impl.*.*(..))")
    public void afterServiceCallException(JoinPoint joinPoint) throws Throwable {
        OperationLog operationLog = saveOperationLog(joinPoint);

        OperationLogType optType = getOptType(joinPoint);
        if (optType != null) {
            operationLog.setOptType(optType.getOperationName());
            operationLog.setChineseName(optType.getChineseName());
        }
        operationLog.setOptResult("失败");
        operationLogDAO.save(operationLog);
    }


    public OperationLog saveOperationLog(JoinPoint joinPoint) throws Throwable {
        //获取登录管理员
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        //获取类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取操作内容
        String opContent = adminOptionContent(joinPoint.getArgs(), methodName);
        if (opContent.length() > 500) {
            opContent = opContent.substring(0, 499);
        }
        //创建日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setOptUser(userName);
        operationLog.setOptDate(DateUtils.format(new Date()));
        operationLog.setClassName(className);
        operationLog.setMethodName(methodName);
        operationLog.setParams(opContent);
//        operationLog.setOptResult("操作成功");
        return operationLog;
    }

    /**
     * 根据类名、方法名查询操作名称
     */
    public OperationLogType getOptType(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String hql = " from " + OperationLogType.class.getName() + " where className = ? and methodName = ? ";
        OperationLogType operationLogType = operationLogTypeDAO.findUnique(hql, className, methodName);
        return operationLogType;
    }

    /**
     * 使用Java反射来获取被拦截方法(insert、update)的参数值，
     * 将参数值拼接为操作内容
     */
    public String adminOptionContent(Object[] args, String mName) {
        try {
            if (args == null) {
                return null;
            }

            StringBuffer rs = new StringBuffer();
            rs.append(mName);
            String className = null;
            int index = 1;
            // 遍历参数对象
            for (int i = 0; i< args.length; i++) {
                Object info = args[i];
                if(info == null){
                    rs.append("[参数:null]");
                }
                //获取对象类型
                if(null == info) continue;
                className = info.getClass().getName();
                className = className.substring(className.lastIndexOf(".") + 1);
                rs.append("[参数" + index + "，类型：" + className + "，值：");
                if (info instanceof String) {
                    rs.append(String.valueOf(info));
                    continue;
                }
                // 获取对象的所有方法
                Method[] methods = info.getClass().getDeclaredMethods();

                // 遍历方法，判断get方法
                for (Method method : methods) {

                    String methodName = method.getName();
                    // 判断是不是get方法
                    if (methodName.indexOf("get") == -1) {// 不是get方法
                        continue;// 不处理
                    }

                    Object rsValue = null;
                    try {

                        // 调用get方法，获取返回值
                        rsValue = method.invoke(info);

                        if (rsValue == null) {//没有返回值
                            continue;
                        }

                    } catch (Exception e) {
                        continue;
                    }

                    //将值加入内容中
                    rs.append("(" + methodName + " : " + rsValue + ")");
                }

                rs.append("]");

                index++;
            }

            return rs.toString();
        } catch (Exception e) {
            log.error(e,e);
        }
        return "参数拼接失败！";
    }


}
