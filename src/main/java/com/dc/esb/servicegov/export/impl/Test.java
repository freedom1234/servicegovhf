package com.dc.esb.servicegov.export.impl;
import com.dc.esb.servicegov.export.IPackerParserConfigGenerator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;
import java.lang.*;
/**
 * Created by kongxfa on 2015/12/15.
 */
public class Test {
    /*public static void main(String[] args) throws Exception {
//        Class c = Class.forName("asdfasdf");
        IPackerParserConfigGenerator generator = null;
        try {
            Class c = Class.forName("com.dc.esb.servicegov.export.impl.TZBStandardXMLConfigGenerator");
            generator = (IPackerParserConfigGenerator) c.newInstance();
            System.out.println("aaaaaaaaaaaaa");
        }
        catch(Exception e) {
            throw e;
        }
    }*/

    /*public static void main(String []args){
        String a="<cust_no metadataid=\"DrwrClntNo\" />\n" +
                "\t\t\t\t<cust_account metadataid=\"AcctNo\" />\n" +
                "\t\t\t\t<remitter metadataid=\"DrwrName\" />\n" +
                "\t\t\t\t<payee metadataid=\"PyeName\" />\n" +
                "\t\t\t\t<draweeBank metadataid=\"PymtBnkNm\" />\n" +
                "\t\t\t\t<dueDt metadataid=\"BillEndDt\" />\n" +
                "\t\t\t\n" +
                "\t\t\t\t<pageSize metadataid=\"PerPgNum\" />\n" +
                "\t\t\t\t<currentPage metadataid=\"Page\" />";

        int i = a.indexOf("pageSize");
        int j=a.indexOf("currentPage");
        if(i>0&&j>0){
            a="<bean>\n"+a.substring(0,(i<j?i:j)-1)+"\n </bean>\n<page>\n "+a.substring((i<j?i:j)-1)+"\n </page>";
        }
        System.out.println(a);
    }*/

//    public static void main(String[]args){
////        String a="23456";
////        System.out.println(a.indexOf("1"));
//        int i= 0;
//        if(i==1 && i++ >-1){
//            System.out.print("-----------"+i);
//        }
//        System.out.print("-----------"+i);
//    }

//    public static void main(String[] args){
//        String a="asadsfa";
//        String b=a.substring(0,a.length()>3?4:a.length());
//        System.out.println(a.length());
//        System.out.println(b);
//    }

    public static void main(String[] args){
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root");
        Element a = root.addElement("a1");
        root.addElement("a2");
        root.addElement("a5");
//        Element b=a.addElement("b1");
//        a.addElement("b2");
//        a.addElement("b2");

        Document doc2 = DocumentHelper.createDocument();
        Element root2 = doc2.addElement("root2");
        Element a2 = root2.addElement("a1");
        root2.addElement("a2").addElement("a2.1").addElement("a2.1.1");
        root2.addElement("a4");
        root2.addElement("a3").addAttribute("name","kk").addAttribute("age","100").addElement("a3.1");
//        Element b1=a2.addElement("c");
//        System.out.println(doc.asXML());
//        try {
////            Document dd = DocumentHelper.parseText(doc.asXML());
//////            System.out.println(dd.asXML());
////            Element bb = dd.getRootElement().element("a");
////            bb.addElement("++");
////            System.out.println(dd.asXML());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Element r1=doc.getRootElement();
        Element r2=doc2.getRootElement();
        add(r1,r2);
        System.out.println(r1.asXML());
    }

    public static void add(Element r1,Element r2){
        List<Element> listAll = r1.elements();
        List<Element> listSub = r2.elements();//总
        Boolean isHave=false;
        for (int i = 0; i < listSub.size(); i++) {
            isHave=false;
            for (int j = 0; j < listAll.size(); j++) {
                if (listAll.get(j).getName().equals(listSub.get(i).getName())) {
//                    System.out.println("相同"+list.get(j).asXML()+"==="+list2.get(i).asXML());
                    isHave=true;

                    List<Element> liAll=listAll.get(j).elements();
                    List<Element> li=listSub.get(i).elements();
                   Element eeALL=  r1.element(listAll.get(j).getName());
                   Element eeSUB = r2.element(listSub.get(i).getName());
                    add(eeALL,eeSUB);
//                    if (li.size()>0 && liAll.size()>0){
//                        for (int k = 0; k < li.size(); k++) {
//
//                        }
//                    }

                    break;
                }else{
//                    System.out.println("不相同" + listAll.get(j).asXML() + "===" + listSub.get(i).asXML());
//                    r1.add(e);
                }
            }
            if(!isHave){
                Element e=listSub.get(i);
//                System.out.println(e.asXML());
//                e.setParent(r1);
//                r1.add(e);
                e.attributes().toString();
                r1.addElement(e.getName());
                r1.element(e.getName()).appendAttributes(e);
                if(r2.elements()!=null){
                    System.out.println("父节点相同，有子节点:执行add()"+e.getName());
                    add(r1.element(e.getName()), r2.element(e.getName()));
                }

//                r1.addElement(list2.get(i).getName());
            }
        }
        System.out.println(r1.asXML());
    }
}
