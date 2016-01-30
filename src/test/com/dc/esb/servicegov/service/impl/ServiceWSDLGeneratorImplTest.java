package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.wsdl.extensions.soap.*;
import junit.framework.TestCase;

import javax.wsdl.*;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import javax.wsdl.extensions.soap.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;

import java.util.ArrayList;
import java.util.List;

import static com.dc.esb.servicegov.service.impl.WSDLConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-19
 * Time: 上午10:37
 */
public class ServiceWSDLGeneratorImplTest extends TestCase{
    public void testGenerate() throws Exception {
        WSDLFactory wsdlFactory = WSDLFactory.newInstance();

        Definition wsdlDefinition = wsdlFactory.newDefinition();
        String tns = "http://esb.spdbbiz.com/services/S120020002/wsdl";
        wsdlDefinition.setTargetNamespace(tns);
        wsdlDefinition.addNamespace("tns", tns);
        wsdlDefinition.addNamespace(HTTP_PREFIX, HTTP_NAMESPACE);
        wsdlDefinition.addNamespace(MIME_PREFIX, MIME_NAMEPACE);
        wsdlDefinition.addNamespace(SOAP_PREFIX, SOAP_NAMESPACE);
        wsdlDefinition.addNamespace(SOAP_NC_PREFIX, SOAP_NC_NAMESPACE);
        wsdlDefinition.addNamespace(XSD_PREFIX, XSD_NAMESPACE);

        wsdlDefinition.addNamespace("s", "http://esb.spdbbiz.com/services/S120020002");
        wsdlDefinition.addNamespace("d", "http://esb.spdbbiz.com/services/S120020002/metadata");

        //create types
        Types types =  wsdlDefinition.createTypes();
        ExtensionRegistry extReg = wsdlDefinition.getExtensionRegistry();
        Schema schema = (Schema) extReg.createExtension(Types.class, new QName(XSD_NAMESPACE, "schema", XSD_PREFIX));
        schema.setTargetNamespace("http://esb.spdbbiz.com/services/S120020002/wsdl");
        SchemaImport schemaImport = schema.createImport();
        schemaImport.setSchemaLocationURI("S120020002.xsd");
        schemaImport.setNamespaceURI("http://esb.spdbbiz.com/services/S120020002");
        schema.addImport(schemaImport);
        types.addExtensibilityElement(schema);
        //create Parts
        Part part1 = wsdlDefinition.createPart();
        part1.setName("RspFrgnExgArbSysSignMnt");
        //设置 part1 的 Schema Type 为 xsd:string
        part1.setTypeName(new QName("http://esb.spdbbiz.com/services/S120020002","RspFrgnExgArbSysSignMnt","s"));

        Part part2 = wsdlDefinition.createPart();
        part2.setName("RspFrgnExgArbSysSignMnt");
        //设置 part1 的 Schema Type 为 xsd:string
        part2.setTypeName(new QName("http://esb.spdbbiz.com/services/S120020002","ReqFrgnExgArbSysSignMnt","s"));

        Part part3 = wsdlDefinition.createPart();
        part3.setName("ReqHeader");
        //设置 part1 的 Schema Type 为 xsd:string
        part3.setTypeName(new QName("http://esb.spdbbiz.com/services/S120020002/metadata","ReqHeader","d"));

        Part part4 = wsdlDefinition.createPart();
        part4.setName("RspHeader");
        //设置 part1 的 Schema Type 为 xsd:string
        part4.setTypeName(new QName("http://esb.spdbbiz.com/services/S120020002/metadata","RspHeader","d"));

        //create messages
        Message message1 = wsdlDefinition.createMessage();
        message1.setQName(new QName(tns,"RspFrgnExgArbSysSignMnt"));
        //为 message 添加 Part
        message1.addPart(part1);
        message1.setUndefined(false);

        //create messages
        Message message2 = wsdlDefinition.createMessage();
        message2.setQName(new QName(tns,"ReqFrgnExgArbSysSignMnt"));
        //为 message 添加 Part
        message2.addPart(part2);
        message2.setUndefined(false);

        //create messages
        Message message3 = wsdlDefinition.createMessage();
        message3.setQName(new QName(tns,"ReqHeader"));
        //为 message 添加 Part
        message1.addPart(part3);
        message1.setUndefined(false);

        //create messages
        Message message4 = wsdlDefinition.createMessage();
        message4.setQName(new QName(tns,"RspHeader"));
        //为 message 添加 Part
        message4.addPart(part4);
        message4.setUndefined(false);


        wsdlDefinition.addMessage(message1);
        wsdlDefinition.addMessage(message2);

        PortType portType = wsdlDefinition.createPortType();
        portType.setQName(new QName(tns,"ESBServerPortType"));
        //创建 Operation
        Operation operation = wsdlDefinition.createOperation();
        operation.setName("FrgnExgArbSysSignMnt");
        //创建 Input，并设置 Input 的 message
        Input input = wsdlDefinition.createInput();
        input.setMessage(message2);
        //创建 Output，并设置 Output 的 message
        Output output = wsdlDefinition.createOutput();
        output.setMessage(message1);
        //设置 Operation 的输入，输出，操作类型
        operation.setInput(input);
        operation.setOutput(output);
        operation.setStyle(OperationType.REQUEST_RESPONSE);
        //这行语句很重要 ！
        operation.setUndefined(false);
        portType.addOperation(operation);
        portType.setUndefined(false);

        wsdlDefinition.addPortType(portType);

        //创建绑定（binding）
        Binding binding = wsdlDefinition.createBinding();
        binding.setQName(new QName(tns,"ESBServerSoapBinding"));
        //创建SOAP绑定（SOAP binding）
        SOAPBinding soapBinding = new SOAPBindingImpl();
        //设置 style = "document"
        soapBinding.setStyle("document");
        //设置 SOAP传输协议 为 HTTP
        soapBinding.setTransportURI("http://schemas.xmlsoap.org/soap/http");
        //soapBinding 是 WSDL 中的扩展元素，
        //为 binding 添加扩展元素 soapBinding
        binding.addExtensibilityElement(soapBinding);
        //设置绑定的端口类型
        binding.setPortType(portType);
        //创建绑定操作（Binding Operation）
        BindingOperation bindingOperation = wsdlDefinition.createBindingOperation();
        //创建 bindingInput
        BindingInput bindingInput = wsdlDefinition.createBindingInput();

        SOAPOperation soapOperation = new SOAPOperationImpl();
        soapOperation.setSoapActionURI("urn:/FrgnExgArbSysSignMnt");
        bindingOperation.addExtensibilityElement(soapOperation);

        //创建 SOAP body ，设置 use = "literal"
        SOAPBody soapBody1 = new SOAPBodyImpl();
        soapBody1.setUse("literal");
        List<String> parts1 = new ArrayList<String> ();
        parts1.add("ReqHeader");
        soapBody1.setParts(parts1);

        SOAPHeader soapHeader = new SOAPHeaderImpl();
        soapHeader.setMessage(new QName(tns,"ReqHeader"));


        bindingInput.addExtensibilityElement(soapBody1);
        BindingOutput bindingOutput = wsdlDefinition.createBindingOutput();
        SOAPBody soapBody2 = new SOAPBodyImpl();
        soapBody2.setUse("literal");
        bindingOutput.addExtensibilityElement(soapBody2);


        //设置 bindingOperation 的名称，绑定输入 和 绑定输出
        bindingOperation.setName("FrgnExgArbSysSignMnt");
        bindingOperation.setBindingInput(bindingInput);
        bindingOperation.setBindingOutput(bindingOutput);
//        bindingOperation.setExtensionAttribute("soapAction");
        binding.addBindingOperation(bindingOperation);
        //这行语句很重要 ！
        binding.setUndefined(false);
        wsdlDefinition.addBinding(binding);

        //创建 service
        Service service = wsdlDefinition.createService();
        service.setQName(new QName(tns,"S120020002"));
        //创建服务端口 port
        Port port = wsdlDefinition.createPort();
        //设置服务端口的 binding，名称，并添加SOAP地址
        port.setBinding(binding);
        port.setName("ESBServerSoapEndpoint");
        SOAPAddress soapAddress = new SOAPAddressImpl();
        soapAddress.setLocationURI("http://esb.spdbbiz.com:7701/services/S120020002");
        port.addExtensibilityElement(soapAddress);
        service.addPort(port);
        wsdlDefinition.addService(service);

        wsdlDefinition.setTypes(types);
        WSDLWriter writer = wsdlFactory.newWSDLWriter();
        writer.writeWSDL(wsdlDefinition,System.out);
    }
}
