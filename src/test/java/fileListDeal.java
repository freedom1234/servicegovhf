import org.apache.xmlbeans.impl.xb.xsdschema.impl.FacetImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.*;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongxfa on 2016/1/29.
 */
public class fileListDeal {

    @org.junit.Test
    public void test(){
        String path="D:\\workfile\\配置文件\\电票\\change\\in";
//        String path="D:\\workfile\\配置文件\\电票\\change\\out";
        File file = new File(path);
        List<File> list=new ArrayList<File>();
        File[] arr;
        try {
            arr = file.listFiles();
            for (int i = 0; i < arr.length; i++) {
                File f=arr[i];
                String name=arr[i].getName();
//                if(name.length()>20){
//                if(name.startsWith("service") && name.endsWith("system.xml")){
                if(name.startsWith("channel")){
                    System.out.println(name);

                    change(path, name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void change (String path,String fileName){
        BufferedReader reader = null ;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path+"/"+fileName)),
                    "utf-8"));
        }  catch (Exception e) {
            e.printStackTrace();
        }
        String temp = null;
        StringBuffer xml = new StringBuffer();
        try {
            while ((temp = reader.readLine()) != null) {
                xml.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Document doc = DocumentHelper.parseText(xml.toString());

            Element root = doc.getRootElement();
            root.remove(root.attribute("store-mode"));
            root.element("body").addAttribute("xmlencoding", "UTF-8");





                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("utf-8");
                FileOutputStream fos = new FileOutputStream("D:\\workfile\\配置文件\\电票\\change\\inok\\"+fileName);
                XMLWriter writer = new XMLWriter(fos, format);
                writer.write(doc);
                writer.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void aaa(){
//        Document doc = DocumentHelper.createDocument();
//        Element root = doc.addElement("root");
//        Element a = root.addElement("a1");
//        a.addElement("a1.1").addElement("a1.1.1");
//        root.addElement("a2");
//        root.addElement("a5");
//
//       Element e = (Element)root.selectNodes("root/a1").get(0);
//        System.out.println(e.asXML());
//
//    }
}
