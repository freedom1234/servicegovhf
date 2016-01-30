package com.dc.esb.servicegov.wsdl.impl;

import com.dc.esb.servicegov.wsdl.Constants;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-20
 * Time: 上午2:20
 */
public class SpdbServiceSchemaGeneratorTest extends TestCase {
    private static final Log log = LogFactory.getLog(SpdbServiceSchemaGeneratorTest.class);

    private static final Namespace NS_URI_XMLNS =  new Namespace("x", Constants.NS_URI_XMLNS);
    private static final QName QN_ELEM = new QName("element", NS_URI_XMLNS);

    public void testGenerate() throws Exception {

        Document document = DocumentHelper.createDocument();
        Element schemaRoot = document.addElement(new QName("schema", NS_URI_XMLNS));
        schemaRoot.addNamespace("d", "http://esb.spdbbiz.com/metadata");
        schemaRoot.addNamespace("s", "http://esb.spdbbiz.com/services/" + "lala");
        schemaRoot.addAttribute("targetNamespace", "http://esb.spdbbiz.com/services/" + "lala");
        schemaRoot.addAttribute("elementFormDefault", "qualified");
        schemaRoot.addAttribute("attributeFormDefault", "qualified");
        Element metadataImportElem = schemaRoot.addElement(new QName("import", NS_URI_XMLNS));
        metadataImportElem.addAttribute("namespace", "http://esb.spdbbiz.com/metadata");
        metadataImportElem.addAttribute("schemaLocation","SoapHeader.xsd");

//        <x:element name="ReqHeader" type="d:ReqHeaderType"/>
//        <x:element name="RspHeader" type="d:RspHeaderType"/>
        Element ReqHeaderElem = schemaRoot.addElement(QN_ELEM);
        ReqHeaderElem.addAttribute("name","ReqHeader");
        ReqHeaderElem.addAttribute("type", "d:ReqHeaderType");
        Element RspHeaderElem = schemaRoot.addElement(QN_ELEM);
        RspHeaderElem.addAttribute("name","RspHeader");
        RspHeaderElem.addAttribute("type", "d:RspHeaderType");




        // 美化格式

        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter( System.out, format );
            writer.write( document );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public void testParse(){
//       Workbook minggedeWorkBook = WorkbookFactory
        String filePath = "";
        try {
            InputStream in = new FileInputStream(filePath);
            Workbook workbook = new HSSFWorkbook();
        } catch (FileNotFoundException e) {

        }
    }
}
