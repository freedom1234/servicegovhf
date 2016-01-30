package com.dc.esb.servicegov.export;

import com.dc.esb.servicegov.export.exception.ExportException;

import java.io.File;
import java.util.List;

/**
 * Created by vincentfxz on 15/11/24.
 */
public interface IPackerParserConfigGenerator {

    public List<File> generate(List<List<? extends IExportableNode>> headList, List<? extends IExportableNode> nodeList, String serviceId,
                               String operationId, String systemAb, String type) throws ExportException;

    public String getTemplatePath();

    public String getMsgType();

}
