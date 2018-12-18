//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

public class MyXMLMapperGenerator extends XMLMapperGenerator {

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);
        this.addResultMapWithoutBLOBsElement(answer);
        this.addResultMapWithBLOBsElement(answer);
        this.addExistedXMLElement(answer);

        return answer;
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }


    protected void addExistedXMLElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new ExistedXmlElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
}