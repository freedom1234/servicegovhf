package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.export.IMetadataConfigGenerator;
import com.dc.esb.servicegov.export.bean.ExportBean;
import com.dc.esb.servicegov.export.bean.MetadataNode;
import com.dc.esb.servicegov.export.util.ExportUtil;
import com.dc.esb.servicegov.export.util.FileUtil;
import com.dc.esb.servicegov.service.*;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2015/7/15.
 */
@Component
public class StandardXMLConfigGenerator implements IMetadataConfigGenerator {

    protected Log logger = LogFactory.getLog(getClass());

    private String requestText = "";
    private String responseText = "";

    public void init(String requestText,String responseText){
        this.requestText = requestText;
        this.responseText = responseText;

    }
    @Autowired
    SystemService systemService;
    @Autowired
    InterfaceService interfaceService;
    @Override
    public File generatorIn(List<Ida> idas, List<SDA> sdas, ExportBean export) {
        File file = null;
        SDA SDARequest = null;
        SDA SDAResponse = null;
        for (SDA sda : sdas) {
            if (sda.getStructName().equalsIgnoreCase("request")) {
                SDARequest = sda;
                continue;
            }
            if (sda.getStructName().equalsIgnoreCase("response")) {
                SDAResponse = sda;
            }
            if (SDARequest != null && SDAResponse != null) {
                break;
            }
        }

        //读取in_config模板
        ClassLoader loader = this.getClass().getClassLoader();
        String service_define_path = loader.getResource("template/in_config/service_define_template.xml").getPath();
        String channel__service_path = loader.getResource("template/in_config/channel_service_template.xml").getPath();
        String service_system__path = loader.getResource("template/in_config/service_system_template.xml").getPath();
        String destpath = loader.getResource("").getPath() + "/generator/"+export.getServiceId()+export.getOperationId();
        try {
            System system_consumer = systemService.getById(export.getConsumerSystemId());


            FileUtil.copyFile(service_define_path,destpath+"/in_config/metadata/service_"+export.getServiceId()+export.getOperationId()+".xml",requestText,responseText);
            FileUtil.copyFile(channel__service_path,destpath+"/in_config/metadata/channel_"+system_consumer.getSystemAb()+"_service_"+export.getServiceId()+export.getOperationId()+".xml",requestText,"");
            FileUtil.copyFile(service_system__path,destpath+"/in_config/metadata/service_"+export.getServiceId()+export.getOperationId()+"_system_"+system_consumer.getSystemAb()+".xml","",responseText);

            file = new File(destpath);

        } catch (Exception e) {
            logger.error("StandardConfigGenerator.generatorIn 导出出现异常,"+e.getMessage());
        }
        return file;
    }

    @Override
    public void generatorOut(List<Ida> idas, List<SDA> sdas, ExportBean export) {
        SDA SDARequest = null;
        SDA SDAResponse = null;
        for (SDA sda : sdas) {
            if (sda.getStructName().equalsIgnoreCase("request")) {
                SDARequest = sda;
                continue;
            }
            if (sda.getStructName().equalsIgnoreCase("response")) {
                SDAResponse = sda;
            }
            if (SDARequest != null && SDAResponse != null) {
                break;
            }
        }


        //读取in_config模板
        ClassLoader loader = this.getClass().getClassLoader();
        String service_define_path = loader.getResource("template/out_config/service_define_template.xml").getPath();
        String channel__service_path = loader.getResource("template/out_config/channel_service_template.xml").getPath();
        String service_system__path = loader.getResource("template/out_config/service_system_template.xml").getPath();

        //TZB导出
        String type_mapping_ecode_path = loader.getResource("template/out_config/type_mapping_ecode.xml").getPath();

        String destpath = loader.getResource("").getPath() + "/generator/" + export.getServiceId()+export.getOperationId();

        try {
            System provide_system = systemService.getById(export.getProviderSystemId());
            FileUtil.copyFile(service_define_path,destpath+"/out_config/metadata/service_"+export.getServiceId()+export.getOperationId()+".xml",requestText,responseText);
            FileUtil.copyFile(channel__service_path,destpath+"/out_config/metadata/channel_"+provide_system.getSystemAb()+"_service_"+export.getServiceId()+export.getOperationId()+".xml","",responseText);
            FileUtil.copyFile(service_system__path,destpath+"/out_config/metadata/service_"+export.getServiceId()+export.getOperationId()+"_system_"+provide_system.getSystemAb()+".xml",requestText,"");

            //TZB导出
//            FileUtil.copyFileTZB(type_mapping_ecode_path,destpath+"/out_config/provider_mapping_ecode_"+export.getProviderInterfaceId()+".xml",requestText,responseText);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StandardConfigGenerator.generatorOut 导出出现异常,"+e.getMessage());
        }
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public InterfaceService getInterfaceService() {
        return interfaceService;
    }

    public void setInterfaceService(InterfaceService interfaceService) {
        this.interfaceService = interfaceService;
    }

    public void setSdaService(SDAService sdaService){
    }

    @Override
    public void setOperationService(OperationServiceImpl operationService) {

    }

    @Override
    public void setInterfaceHeadService(InterfaceHeadService interfaceHeadService) {

    }

    @Override
    public void setIdaService(IdaService idaService) {

    }


    public static void main(String[] args) {

    }
}
