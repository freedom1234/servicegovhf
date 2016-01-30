package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.entity.System;
import com.dc.esb.servicegov.export.IMetadataConfigGenerator;
import com.dc.esb.servicegov.export.bean.ExportBean;
import com.dc.esb.servicegov.export.util.ExportUtil;
import com.dc.esb.servicegov.export.util.FileUtil;
import com.dc.esb.servicegov.service.*;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/15.
 */
@Component
public class TZBStandardXMLConfigGenerator implements IMetadataConfigGenerator {

    protected Log logger = LogFactory.getLog(getClass());

    private String requestText = "";
    private String responseText = "";

    public void init(String requestText, String responseText) {
        this.requestText = requestText;
        this.responseText = responseText;

    }

    @Autowired
    SystemService systemService;
    @Autowired
    InterfaceService interfaceService;
    @Autowired
    SDAService sdaService;
    @Autowired
    OperationServiceImpl operationService;

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
        String destpath = loader.getResource("").getPath() + "/generator/" + export.getServiceId() + export.getOperationId();
        try {
            System system_consumer = systemService.getById(export.getConsumerSystemId());


//            FileUtil.copyFile(service_define_path,destpath+"/in_config/metadata/service_"+export.getServiceId()+export.getOperationId()+".xml",requestText,responseText);
//            FileUtil.copyFile(channel__service_path,destpath+"/in_config/metadata/channel_"+system_consumer.getSystemAb()+"_service_"+export.getServiceId()+export.getOperationId()+".xml",requestText,"");
//            FileUtil.copyFile(service_system__path,destpath+"/in_config/metadata/service_"+export.getServiceId()+export.getOperationId()+"_system_"+system_consumer.getSystemAb()+".xml","",responseText);

            file = new File(destpath);

        } catch (Exception e) {
            logger.error("StandardConfigGenerator.generatorIn 导出出现异常," + e.getMessage());
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

        List<Ida> reqIdas = new ArrayList<Ida>();
        List<Ida> resIdas = new ArrayList<Ida>();
        String reqId = "";
        String resId = "";
        for (Ida ida : idas) {
            if (ida.getStructName() == null) continue;
            if (ida.getStructName().equals("request")) {
                reqId = ida.getId();
            }
            if (ida.getStructName().equals("response")) {
                resId = ida.getId();
            }
        }
        for (Ida ida : idas) {
            if (null == ida.getParentId()) {
                continue;
            } else if (ida.getParentId().equals(reqId)) {
                reqIdas.add(ida);
            } else if (ida.getParentId().equals(resId)) {
                resIdas.add(ida);
            }
        }
        System provide_system = systemService.getById(export.getProviderSystemId());
        System consumer_system = systemService.getById(export.getConsumerSystemId());
        Interface provide_interface = interfaceService.getById(export.getProviderInterfaceId());
        //test
        requestText = ExportUtil.generatorMappingXML(reqIdas, "request", provide_system.getSystemAb(),
                sdaService, export.getServiceId(), export.getOperationId());
        responseText = ExportUtil.generatorMappingXML(resIdas, "response", "esb", sdaService, export.getServiceId(),
                export.getOperationId());
        //in_config
        Map<String, String> map = new HashMap<String, String>();
        map.put("serviceId", export.getServiceId());
        map.put("operationId", export.getOperationId());
        Operation operation = operationService.findUniqueBy(map);
        String identifySystemInText = ExportUtil.generatorIdentifyInXML(consumer_system.getSystemAb(), operation.getOperationName(), consumer_system.getSystemId(), export.getServiceId(), export.getOperationId());

        String identifySystemOutText = ExportUtil.generatorIdentifyOutXML(provide_system.getSystemAb(), provide_interface.getInterfaceName(), provide_interface.getEcode(), export.getServiceId(), export.getOperationId());

        //out_config
//        String identifySystemOutText = ExportUtil.generatorIdentifyOutXML(provide_system.getSystemAb(), export.getServiceId(), export.getOperationId());


//        ExportUtil.generatorMappingXML(idas,sdas);

        //读取out_config模板
        ClassLoader loader = this.getClass().getClassLoader();
        String service_define_path = loader.getResource("template/out_config/service_define_template.xml").getPath();
        String channel__service_path = loader.getResource("template/out_config/channel_service_template.xml").getPath();
        String service_system__path = loader.getResource("template/out_config/service_system_template.xml").getPath();

        //TZB导出
        String type_mapping_ecode_path = loader.getResource("template/out_config/type_mapping_ecode.xml").getPath();

        String identify_config_system_out_path = loader.getResource("template/out_config/system_identify_system.xml").getPath();
        String identify_config_system_int_path = loader.getResource("template/in_config/identify_config_system.xml").getPath();

        String destpath = loader.getResource("").getPath() + "/generator/" + export.getServiceId() + export.getOperationId();

        try {
            FileUtil.copyFile(service_define_path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + ".xml", requestText, responseText);
            FileUtil.copyFile(channel__service_path, destpath + "/out_config/metadata/channel_" + provide_system.getSystemAb() + "_service_" + export.getServiceId() + export.getOperationId() + ".xml", "", responseText);
            FileUtil.copyFile(service_system__path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + "_system_" + provide_system.getSystemAb() + ".xml", requestText, "");
            String systemAb = provide_system.getSystemAb();
            String ecode = provide_interface.getEcode();
            //TZB导出
            FileUtil.copyFileTZB(type_mapping_ecode_path, destpath + "/out_config/frameworkdist/channel/mapping/provider_mapping_ecode_" + export.getProviderInterfaceId() + ".xml", systemAb, ecode, requestText, responseText);
            //in_config/identify
            FileUtil.copyFileTZBIdentify(identify_config_system_int_path, destpath + "/in_config/frameworkdist/channel/identify/identify_config_" + consumer_system.getSystemAb() + ".xml", consumer_system.getSystemAb(), identifySystemInText);
            //out_config/identify
            FileUtil.copyFileTZBIdentifyOut(identify_config_system_out_path, destpath + "/out_config/frameworkdist/channel/identify/system_identify_" + provide_system.getSystemAb() + ".xml", provide_system.getSystemAb(), identifySystemOutText);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StandardConfigGenerator.generatorOut 导出出现异常," + e.getMessage());
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

    @Override
    public void setInterfaceService(InterfaceService interfaceService) {
        this.interfaceService = interfaceService;
    }

    public static void main(String[] args) {

    }

    public SDAService getSdaService() {
        return sdaService;
    }

    public void setSdaService(SDAService sdaService) {
        this.sdaService = sdaService;
    }

    public OperationServiceImpl getOperationService() {
        return operationService;
    }

    public void setOperationService(OperationServiceImpl operationService) {
        this.operationService = operationService;
    }

    @Override
    public void setInterfaceHeadService(InterfaceHeadService interfaceHeadService) {

    }

    @Override
    public void setIdaService(IdaService idaService) {

    }
}
