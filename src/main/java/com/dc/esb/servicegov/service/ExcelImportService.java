package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.ServiceInvoke;
import com.dc.esb.servicegov.service.impl.ExcelImportServiceImpl;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

public interface ExcelImportService {
    public List executeImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap, Map<String, String> publicMap, Map<String, Object> headMap);

    public List executeInterfaceImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap ,ExcelImportServiceImpl.IndexDO indexDO);


    public boolean existSystem(String systemId);

    public List<ExcelImportServiceImpl.IndexDO> parseIndexSheet(Sheet indexSheet);

    public List<ExcelImportServiceImpl.IndexDO> parseInterfaceIndexSheet(Sheet indexSheet);

    public Map<String, Object> getInterfaceHead(ExcelImportServiceImpl.IndexDO indexDO, Workbook workbook);

    public Map<String, String> getPublicHead(ExcelImportServiceImpl.IndexDO indexDO);

    public Map<String, Object> getInputArg(Sheet sheet);

    public Map<String, Object> getInterfaceInputArg(Sheet sheet);

    public Map<String, Object> getOutputArg(Sheet sheet);

    public Map<String, Object> getInterfaceOutputArg(Sheet sheet);

    public Map<String, Object> getInterfaceAndServiceInfo(Sheet tranSheet);
    public Map<String, Object> getInterfaceAndServiceInfo(Sheet tranSheet,ExcelImportServiceImpl.IndexDO indexDO);

    public Map<String, Object> getInterfaceInfo(Sheet tranSheet);

    public ServiceInvoke addServiceInvoke(String invokeSystemId,String serviceId,String operationId,String type,String isStandard);

    public Map<String, Object> getServiceInfo(Sheet tranSheet,ExcelImportServiceImpl.IndexDO indexDO);
    public  Map<String, Object> getStandardInputArg(Sheet sheet);
    public Map<String, Object> getStandardOutputArg(Sheet sheet);
    public List executeStandardImport(Map<String, Object> infoMap, Map<String, Object> inputMap, Map<String, Object> outMap, Map<String, String> publicMap, Map<String, Object> headMap);


}
