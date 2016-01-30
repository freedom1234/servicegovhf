package com.dc.esb.servicegov.export;

import com.dc.esb.servicegov.entity.Ida;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.export.bean.ExportBean;
import com.dc.esb.servicegov.service.*;
import com.dc.esb.servicegov.service.impl.OperationServiceImpl;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2015/7/15.
 */
public interface IMetadataConfigGenerator {

    /**
     * 返回生成xml的文件目录   路径为 前缀路径/in_config/metadata 返回前缀路径目录即可
     * @param idas 接口定义
     * @return
     */
    public File generatorIn(List<Ida> idas,List<SDA> sdas,ExportBean export);

    /**
     * 生成文件目录放到generatorIn(前缀路径/in_config/metadata) 前缀路径/out_config/metadata
     * @param sdas 服务定义
     */
    public void generatorOut(List<Ida> idas,List<SDA> sdas,ExportBean export);

    public void setSystemService(SystemService systemService);

    public void setInterfaceService(InterfaceService interfaceService);

    public void setSdaService(SDAService sdaService);

    public void setOperationService(OperationServiceImpl operationService);

    public void setInterfaceHeadService(InterfaceHeadService interfaceHeadService);

    public void setIdaService(IdaService idaService);

}
