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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vincentfxz on 15/10/27.
 */
public class GZNSXMLConfigGenerator implements IMetadataConfigGenerator {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SystemService systemService;
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private SDAService sdaService;
    @Autowired
    private OperationServiceImpl operationService;
    @Autowired
    private InterfaceHeadService interfaceHeadService;
    @Autowired
    private IdaService idaService;

    private String requestText = "";
    private String responseText = "";

    public void init(String requestText,String responseText){
        this.requestText = requestText;
        this.responseText = responseText;

    }

    @Override
    public File generatorIn(List<Ida> idas, List<SDA> sdas, ExportBean export) {
        if(idas != null){
            String reqId = "";
            String resId = "";
            List<Ida> reqIdas = new ArrayList<Ida>();
            List<Ida> resIdas = new ArrayList<Ida>();
            for (Ida ida : idas){
                if(ida.getStructName() == null) continue;
                if(ida.getStructName().equals("request")){
                    reqId = ida.getId();
                }
                if(ida.getStructName().equals("response")){
                    resId = ida.getId();
                }
            }
            for(Ida ida : idas){
                if(null == ida.getParentId()){
                    continue;
                }else if(ida.getParentId().equals(reqId)){
                    reqIdas.add(ida);
                }else if(ida.getParentId().equals(resId)){
                    resIdas.add(ida);
                }
            }
            ClassLoader loader = this.getClass().getClassLoader();
            String destpath = loader.getResource("").getPath() + "/generator/" + export.getServiceId() + export.getOperationId();
            String service_define_path = loader.getResource("gzns_template/in_config/service_define_template.xml").getPath();
            String channel__service_path = loader.getResource("gzns_template/in_config/channel_service_template.xml").getPath();
            String service_system__path = loader.getResource("gzns_template/in_config/service_system_template.xml").getPath();

            com.dc.esb.servicegov.entity.System provide_system = systemService.getById(export.getProviderSystemId());
            System consumer_system = systemService.getById(export.getConsumerSystemId());

            requestText = ExportUtil.generateBaseMappingXML(resIdas, "response", consumer_system.getSystemAb(),
                    sdaService, export.getServiceId(), export.getOperationId());
            responseText = ExportUtil.generateBaseMappingXML(reqIdas, "request", "esb", sdaService, export.getServiceId(),
                    export.getOperationId());
            String reqHeadText = null;
            String rspHeadText = null;
            String interfaceId = export.getProviderInterfaceId();
            Interface inter = interfaceService.getById(interfaceId);
            List<InterfaceHeadRelate> interfaceHeadRelates = inter.getHeadRelates();
            if(null != interfaceHeadRelates){
                if(interfaceHeadRelates.size() > 0){
                    String interfaceHeadId = interfaceHeadRelates.get(0).getHeadId();
                    List<Ida> headIdas = idaService.findBy("headId",interfaceHeadId);
                    String reqHeadId = "";
                    String resHeadId = "";
                    List<Ida> reqHeadIdas = new ArrayList<Ida>();
                    List<Ida> resHeadIdas = new ArrayList<Ida>();
                    for (Ida ida : headIdas){
                        if(ida.getStructName() == null) continue;
                        if(ida.getStructName().equals("request")){
                            reqHeadId = ida.getId();
                        }
                        if(ida.getStructName().equals("response")){
                            resHeadId = ida.getId();
                        }
                    }
                    for(Ida ida : headIdas){
                        if(null == ida.getParentId()){
                            continue;
                        }else if(ida.getParentId().equals(reqHeadId)){
                            reqHeadIdas.add(ida);
                        }else if(ida.getParentId().equals(resHeadId)){
                            resHeadIdas.add(ida);
                        }
                    }
                    reqHeadText = ExportUtil.generateBaseMappingXML(headIdas, "request", "esb", sdaService, export.getServiceId(),
                            export.getOperationId());
                    rspHeadText = ExportUtil.generateBaseMappingXML(reqIdas, "request", "esb", sdaService, export.getServiceId(),
                            export.getOperationId());

                }
            }


            try {
                FileUtil.copyFile(service_define_path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + ".xml", requestText, responseText);
                FileUtil.copyFile(channel__service_path, destpath + "/out_config/metadata/channel_" + provide_system.getSystemAb() + "_service_" + export.getServiceId() + export.getOperationId() + ".xml", "", responseText,"", rspHeadText);
                FileUtil.copyFile(service_system__path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + "_system_" + provide_system.getSystemAb() + ".xml", requestText, "",reqHeadText,"");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("StandardConfigGenerator.generatorOut 导出出现异常," + e.getMessage());
            }


        }else{
            logger.error("接口["+export.getConsumerInterfaceId()+"]的数据协议内容为空");
        }
        return null;
    }

    @Override
    public void generatorOut(List<Ida> idas, List<SDA> sdas, ExportBean export) {
        if(idas != null){
            String reqId = "";
            String resId = "";
            List<Ida> reqIdas = new ArrayList<Ida>();
            List<Ida> resIdas = new ArrayList<Ida>();
            for (Ida ida : idas){
                if(ida.getStructName() == null) continue;
                if(ida.getStructName().equals("request")){
                    reqId = ida.getId();
                }
                if(ida.getStructName().equals("response")){
                    resId = ida.getId();
                }
            }
            for(Ida ida : idas){
                if(null == ida.getParentId()){
                    continue;
                }else if(ida.getParentId().equals(reqId)){
                    reqIdas.add(ida);
                }else if(ida.getParentId().equals(resId)){
                    resIdas.add(ida);
                }
            }

            ClassLoader loader = this.getClass().getClassLoader();
            String destpath = loader.getResource("").getPath() + "/generator/" + export.getServiceId() + export.getOperationId();
            String service_define_path = loader.getResource("gzns_template/out_config/service_define_template.xml").getPath();
            String channel__service_path = loader.getResource("gzns_template/out_config/channel_service_template.xml").getPath();
            String service_system__path = loader.getResource("gzns_template/out_config/service_system_template.xml").getPath();

            com.dc.esb.servicegov.entity.System provide_system = systemService.getById(export.getProviderSystemId());

            requestText = ExportUtil.generateBaseMappingXML(reqIdas, "request", provide_system.getSystemAb(),
                    sdaService, export.getServiceId(), export.getOperationId());
            requestText = requestText.substring(requestText.indexOf("<root>") + "<root>".length(),requestText.indexOf("</root>"));
            responseText = ExportUtil.generateBaseMappingXML(resIdas, "response", "esb", sdaService, export.getServiceId(),
                    export.getOperationId());
            responseText = responseText.substring(responseText.indexOf("<root>") + "<root>".length(),responseText.indexOf("</root>"));

            String reqHeadText = null;
            String rspHeadText = null;
            String interfaceId = export.getProviderInterfaceId();
            Interface inter = interfaceService.getById(interfaceId);
            List<InterfaceHeadRelate> interfaceHeadRelates = inter.getHeadRelates();
            if(null != interfaceHeadRelates){
                if(interfaceHeadRelates.size() > 0){
                    String interfaceHeadId = interfaceHeadRelates.get(0).getHeadId();
                    List<Ida> headIdas = idaService.findBy("headId",interfaceHeadId);
                    String reqHeadId = "";
                    String resHeadId = "";
                    List<Ida> reqHeadIdas = new ArrayList<Ida>();
                    List<Ida> resHeadIdas = new ArrayList<Ida>();
                    for (Ida ida : headIdas){
                        if(ida.getStructName() == null) continue;
                        if(ida.getStructName().equals("request")){
                            reqHeadId = ida.getId();
                        }
                        if(ida.getStructName().equals("response")){
                            resHeadId = ida.getId();
                        }
                    }
                    for(Ida ida : headIdas){
                        if(null == ida.getParentId()){
                            continue;
                        }else if(ida.getParentId().equals(reqHeadId)){
                            reqHeadIdas.add(ida);
                        }else if(ida.getParentId().equals(resHeadId)){
                            resHeadIdas.add(ida);
                        }
                    }
                    reqHeadText = ExportUtil.generateBaseMappingXML(reqHeadIdas, "request", "esb", sdaService, export.getServiceId(),
                            export.getOperationId());
                    reqHeadText = reqHeadText.substring(reqHeadText.indexOf("<root>") + "<root>".length(),reqHeadText.indexOf("</root>"));
                    rspHeadText = ExportUtil.generateBaseMappingXML(resHeadIdas, "request", "esb", sdaService, export.getServiceId(),
                            export.getOperationId());
                    rspHeadText = rspHeadText.substring(rspHeadText.indexOf("<root>") + "<root>".length(),rspHeadText.indexOf("</root>"));

                }
            }

            List<SDA> reqSdas = getSdaBody(export.getServiceId(), export.getOperationId(), "request");
            List<SDA> rspSdas = getSdaBody(export.getServiceId(), export.getOperationId(), "response");

            String requestServicdDef = ExportUtil.generateBaseMappingXML(reqSdas);
            requestServicdDef = requestServicdDef.substring(requestServicdDef.indexOf("<root>") + "<root>".length(),requestServicdDef.indexOf("</root>"));
            String responseServicdDef = ExportUtil.generateBaseMappingXML(rspSdas);
            responseServicdDef = responseServicdDef.substring(responseServicdDef.indexOf("<root>") + "<root>".length(),responseServicdDef.indexOf("</root>"));

            try {
                FileUtil.copyFile(service_define_path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + ".xml", requestServicdDef, responseServicdDef);
                FileUtil.copyFile(channel__service_path, destpath + "/out_config/metadata/channel_" + provide_system.getSystemAb() + "_service_" + export.getServiceId() + export.getOperationId() + ".xml", "", responseText, "" , rspHeadText);
                FileUtil.copyFile(service_system__path, destpath + "/out_config/metadata/service_" + export.getServiceId() + export.getOperationId() + "_system_" + provide_system.getSystemAb() + ".xml", requestText, "", reqHeadText, "");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("StandardConfigGenerator.generatorOut 导出出现异常," + e.getMessage());
            }


        }else{
            logger.error("接口["+export.getConsumerInterfaceId()+"]的数据协议内容为空");
        }
    }

    public List<SDA> getSdaBody(String serviceId, String operationId, String type){
        List<SDA> sdaAll = new ArrayList<SDA>();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("serviceId", serviceId);
        param.put("operationId", operationId);
        sdaAll = sdaService.findBy(param);
        String bodyId = "";
        List<SDA> bodySdas = new ArrayList<SDA>();
        for (SDA sda : sdaAll){
            if(sda.getStructName() == null) continue;
            if(sda.getStructName().equals(type)){
                bodyId = sda.getId();
            }
        }
        for(SDA sda : sdaAll){
            if(null == sda.getParentId()){
                continue;
            }else if(sda.getParentId().equals(bodyId)){
                bodySdas.add(sda);
            }
        }
        return bodySdas;
    }


    @Override
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;

    }

    @Override
    public void setInterfaceService(InterfaceService interfaceService) {
        this.interfaceService = interfaceService;
    }

    @Override
    public void setSdaService(SDAService sdaService) {
        this.sdaService = sdaService;
    }

    @Override
    public void setOperationService(OperationServiceImpl operationService) {
        this.operationService = operationService;
    }

    @Override
    public void setInterfaceHeadService(InterfaceHeadService interfaceHeadService) {
        this.interfaceHeadService = interfaceHeadService;
    }

    @Override
    public void setIdaService(IdaService idaService){
        this.idaService = idaService;
    }
}
