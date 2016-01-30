package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.GeneratorServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceInvokeServiceImpl;
import com.dc.esb.servicegov.vo.ConfigListVO;
import com.dc.esb.servicegov.vo.ConfigVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
@Component
public class ConfigBathGenerator {
    private static final Log logger = LogFactory.getLog(ConfigBathGenerator.class);
    private static String errorMsg = null;
    @Autowired
    private ServiceInvokeServiceImpl serviceInvokeService;
    @Autowired
    private GeneratorServiceImpl generatorService;
    /**
     * 批量生成xml文件
     * @param list
     * @return 配置文件所在
     */
    public String generate(HttpServletRequest request, ConfigListVO list){
        if(validate(list)){
            String path = ConfigExportGenerator.class.getResource("/").getPath() + "/generator" + new Date().getTime();
            for(ConfigVO configVO : list.getList()){
                generate(request, configVO, path);
            }
            return path;
        }else{
            return null;
        }
    }

    /**
     * 生成一条交易的配置文件
     * @param configVO
     * @return
     */
    public File generate(HttpServletRequest request, ConfigVO configVO, String path){
        String consumerServiceInvokeId = configVO.getConsumerServiceInvokeId();
        String providerServiceInvokeId = configVO.getProviderServiceInvokeId();
        ServiceInvoke consumerServiceInvoke = serviceInvokeService.findUniqueBy("invokeId", consumerServiceInvokeId);
        ServiceInvoke providerServiceInvoke = serviceInvokeService.findUniqueBy("invokeId", providerServiceInvokeId);

        String conGeneratorId = configVO.getConGeneratorId();
        String proGeneratorId = configVO.getProGeneratorId();
        Generator conGenerator = generatorService.findUniqueBy("id", conGeneratorId);
        Generator proGenerator = generatorService.findUniqueBy("id", proGeneratorId);

        if(null != providerServiceInvoke){
            WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
            ConfigExportGenerator proConfigExportGenerator = (ConfigExportGenerator)cont.getBean(getSpringBeanName(proGenerator.getImplementsClazz()));
            proConfigExportGenerator.generate(providerServiceInvoke, path);
        }

        //如果客户端非标准生成器，消费端也使用提供端的服务配置*********
        if (!conGenerator.getImplementsClazz().equalsIgnoreCase("com.dc.esb.servicegov.export.impl.StandardXMLConfigExportGender")) {
            WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
            ConfigExportGenerator conConfigExportGenerator = (ConfigExportGenerator)cont.getBean(getSpringBeanName(conGenerator.getImplementsClazz()));
            consumerServiceInvoke.setInter(providerServiceInvoke.getInter());
            conConfigExportGenerator.generate(consumerServiceInvoke, path);
        }
        else if(null != consumerServiceInvoke){
            WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
            ConfigExportGenerator conConfigExportGenerator = (ConfigExportGenerator)cont.getBean(getSpringBeanName(conGenerator.getImplementsClazz()));
            conConfigExportGenerator.generate(consumerServiceInvoke, path);

//                Class conGeneratorClass = Class.forName(conGenerator.getImplementsClazz());
//                ConfigExportGenerator conConfigExportGenerator = (ConfigExportGenerator)conGeneratorClass.newInstance();
//                conConfigExportGenerator.generate(consumerServiceInvoke, path);
        }

        return null;
    }

    /**
     * 数据正确性验证
     * @param configListVO
     * @return
     */
    public boolean validate(ConfigListVO configListVO){
        if(null == configListVO || 0 == configListVO.getList().size()){
            errorMsg = "传入数据为空！";
            return  false;
        }
        List<ConfigVO> list = configListVO.getList();
        for(int i = 0; i < list.size(); i++){
            ConfigVO configVO = list.get(i);
            if(null == configVO){
                errorMsg = "第"+ (i+1) + "条数据数据为空!";
            }else{
            }
        }
        return true;
    }
    public void generateErrorMsg(String errorMsg){
        if(StringUtils.isNotEmpty(errorMsg)){//如果导出过程中出现错误，将错误信息写入errorMsg.txt

        }
    }

    public String getSpringBeanName(String className){
        String[] classNameStrs = className.split("\\.");
        className = classNameStrs[classNameStrs.length-1];
        String firstChar = className.substring(0,1);
        String beanName = className.replaceFirst(firstChar, firstChar.toLowerCase());
        return  beanName;
    }
}
