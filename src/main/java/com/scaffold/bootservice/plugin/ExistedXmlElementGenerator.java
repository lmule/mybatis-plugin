package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class ExistedXmlElementGenerator extends AbstractXmlElementGenerator {

    public ExistedXmlElementGenerator() {
    }

    @Override
    public void addElements(XmlElement parentElement) {
        Document rootDocument = getExistedDocument();
        if (null == rootDocument) {
            return;
        }

        Node mapperNode = deleteBaseResultMapNode(rootDocument);
        NodeList nodeList = mapperNode.getChildNodes();
        for (Integer i = 0; i < nodeList.getLength() ; i++) {
            Element element = w3cNode2XmlElement(nodeList.item(i));
            if (element != null) {
                parentElement.addElement(element);
            }
        }
    }

    private Element w3cNode2XmlElement(Node w3cNode) {
        if (w3cNode.getNodeType() == 3) {
            if (w3cNode.getNodeValue().trim().isEmpty()) {
                return null;
            }
            String content = w3cNode.getTextContent().trim();
            return new TextElement(content);
        }

        XmlElement xmlElement = new XmlElement(w3cNode.getNodeName());

        NamedNodeMap attributes = w3cNode.getAttributes();
        for (Integer j = 0; j < attributes.getLength(); j ++) {
            Node nodeAttribute = attributes.item(j);
            Attribute elementAttribute = new Attribute(nodeAttribute.getNodeName(), nodeAttribute.getNodeValue());
            xmlElement.addAttribute(elementAttribute);
        }

        NodeList nodeList = w3cNode.getChildNodes();
        for (Integer i = 0; i < nodeList.getLength() ; i++) {
            Node node = nodeList.item(i);
            Element tmpXmlElement = w3cNode2XmlElement(node);
            if (tmpXmlElement != null) {
                xmlElement.addElement(tmpXmlElement);
            }
        }

        return xmlElement;
    }

    private Node deleteBaseResultMapNode(Document rootDocument) {
        Node mapperNode = rootDocument.getChildNodes().item(1);
        NodeList nodeList = mapperNode.getChildNodes();
        for (Integer i = nodeList.getLength() - 1; i >= 0 ; i--) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() != 1 || !currentNode.getNodeName().equals("resultMap")) {
                continue;
            }

            NamedNodeMap attributes = currentNode.getAttributes();
            String id = "";
            if (attributes.getNamedItem("id") != null) {
                id = attributes.getNamedItem("id").getNodeValue();
            }
            if (!id.equals("ResultMapWithBLOBs") && !id.equals("BaseResultMap") && !id.equals("allColumns")) {
                continue;
            }

            String type = "";
            if (attributes.getNamedItem("type") != null) {
                type = attributes.getNamedItem("type").getNodeValue();
            }
            String modelPackage = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getTargetPackage() + "." +
                    IntrospectedTableHelper.getCamelizedTableName(introspectedTable);
            if (!type.equals(modelPackage)) {
                continue;
            }

            mapperNode.removeChild(currentNode);
        }
        return mapperNode;
    }

    private Document getExistedDocument() {
        try {
            String path = IntrospectedTableHelper.getXmlMapperFilePath(introspectedTable);
            InputSource inputSource =new InputSource(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new NullEntityResolver());
            return builder.parse(inputSource);
        }
        catch (Exception e) {

        }
        return null;
    }

    private static class NullEntityResolver implements EntityResolver {

        private NullEntityResolver() {
        }

        public InputSource resolveEntity(String publicId, String systemId) {
            StringReader sr = new StringReader("");
            return new InputSource(sr);
        }
    }
}
