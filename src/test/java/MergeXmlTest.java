import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

/**
 * Created by kongxfa on 2016/1/23.
 */
public class MergeXmlTest {

    @Test
    public void MergeXml(){
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root");
        Element a = root.addElement("a1");
        root.addElement("a2");
//        root.addElement("a5");
//        Element b=a.addElement("b1");
//        a.addElement("b2");
//        a.addElement("b2");
//
        Document doc2 = DocumentHelper.createDocument();
        Element root2 = doc2.addElement("root2");
        Element a2 = root2.addElement("a1");
        root2.addElement("a2").addElement("a2.1").addElement("a2.1.1");
        root2.addElement("a4").addElement("a4.1").addAttribute("name","kxf");
//        root2.addElement("a3").addAttribute("name","kk").addAttribute("age","100").addElement("a3.1");
//        System.out.println(doc.asXML());<?xml version="1.0" encoding="UTF-8"?><root><a1/><a2/><a5/></root>
//        System.out.println(doc2.asXML());<?xml version="1.0" encoding="UTF-8"?><root2><a1/><a2><a2.1><a2.1.1/></a2.1></a2><a4/><a3 name="kk" age="100"><a3.1/></a3></root2>
//        Element b1=a2.addElement("c");
//        System.out.println(doc.asXML());
//        try {
//            doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a1/><a2/><a5/></root>");
//            doc2 = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root2><a1/><a2><a2.1><a2.1.1><a2.1.1.1哈哈/></a2.1.1></a2.1></a2><a4/><a3 name=\"kk\" age=\"100\"><a3.1/></a3></root2>");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Element r1=doc.getRootElement();
        Element r2=doc2.getRootElement();
        mergeXmlNode(r1, r2);
        System.out.println(r1.asXML());
    }

    public void mergeXmlNode(Element rAll, Element rSub){
        List<Element> listAll = rAll.elements();
        List<Element> listSub = rSub.elements();
        Boolean isHave=false;
        for (int i = 0; i < listSub.size(); i++) {
            isHave=false;
            for (int j = 0; j < listAll.size(); j++) {
                if (listAll.get(j).getName().equals(listSub.get(i).getName())) {
                    isHave=true;
                    List<Element> liAll=listAll.get(j).elements();
                    List<Element> li=listSub.get(i).elements();
                    Element eAll=  rAll.element(listAll.get(j).getName());
                    Element eSub = rSub.element(listSub.get(i).getName());
                    if (eSub.elements().size()>0) {
                        mergeXmlNode(eAll, eSub);
                    }
                    break;
                }
            }
            if(!isHave){
                Element eSub=listSub.get(i);
                rAll.addElement(eSub.getName());
                rAll.element(eSub.getName()).appendAttributes(eSub);
                if(eSub.elements().size()>0){
//                    System.out.println("本节点复制完，再复制子节点:执行add()===="+eSub.getName());
                    mergeXmlNode(rAll.element(eSub.getName()), rSub.element(eSub.getName()));
                }
            }
        }
    }
}
