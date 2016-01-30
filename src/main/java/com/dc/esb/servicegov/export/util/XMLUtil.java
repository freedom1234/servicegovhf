package com.dc.esb.servicegov.export.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by vincentfxz on 15/11/25.
 */
public class XMLUtil {

    private static final Log log = LogFactory.getLog(XMLUtil.class);

    public static String formatXml(String inputXML) {
        Document document = null;
        String requestXML = null;
        XMLWriter writer = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(inputXML));
            if (document != null) {
                StringWriter stringWriter = new StringWriter();
                OutputFormat format = new OutputFormat(" ", true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                requestXML = stringWriter.getBuffer().toString();
            }
        } catch (IOException e) {
            log.error(e,e);
        } catch (DocumentException e) {
            log.error(e,e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
        return requestXML;
    }


}
