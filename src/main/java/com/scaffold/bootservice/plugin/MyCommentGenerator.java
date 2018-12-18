package com.scaffold.bootservice.plugin;

import java.text.SimpleDateFormat;
import java.util.*;

import com.scaffold.bootservice.helper.AnnotationHelper;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
//import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

public class MyCommentGenerator implements CommentGenerator {
    private Properties properties = new Properties();
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean addRemarkComments = false;
    private SimpleDateFormat dateFormat;

    private List<String> classAnnotationFullyQualifiedNames = new LinkedList<>();


    public MyCommentGenerator() {
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    public void addComment(XmlElement xmlElement) {
//        if (!this.suppressAllComments) {
//            xmlElement.addElement(new TextElement("<!--"));
//            StringBuilder sb = new StringBuilder();
//            sb.append("  WARNING - ");
//            sb.append("@mbg.generated");
//            xmlElement.addElement(new TextElement(sb.toString()));
//            xmlElement.addElement(new TextElement("  This element is automatically generated by MyBatis Generator, do not modify."));
//            String s = this.getDateString();
//            if (s != null) {
//                sb.setLength(0);
//                sb.append("  This element was generated on ");
//                sb.append(s);
//                sb.append('.');
//                xmlElement.addElement(new TextElement(sb.toString()));
//            }
//
//            xmlElement.addElement(new TextElement("-->"));
//        }
    }

    public void addRootComment(XmlElement rootElement) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }
        String classAnnotation = this.properties.getProperty("classAnnotationFullyQualifiedNames");
        classAnnotationFullyQualifiedNames.addAll(Arrays.asList(classAnnotation.split(",")));
    }

    /**
     * changed by lmule
     * @param javaElement
     * @param markAsDoNotDelete
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append("@mbg.generated");
//        if (markAsDoNotDelete) {
//            sb.append(" do_not_delete_during_merge");
//        }
//
//        String s = this.getDateString();
//        if (s != null) {
//            sb.append(' ');
//            sb.append(s);
//        }
//
//        javaElement.addJavaDocLine(sb.toString());
    }

    protected String getDateString() {
        if (this.suppressDate) {
            return null;
        } else {
            return this.dateFormat != null ? this.dateFormat.format(new Date()) : (new Date()).toString();
        }
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        this.addClassComment(innerClass, introspectedTable, false);
    }

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments && this.addRemarkComments) {
            StringBuilder sb = new StringBuilder();
            topLevelClass.addJavaDocLine("/**");
            String remarks = introspectedTable.getRemarks();
            sb.append(" * ");
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                sb.append(remarks + ": ");
            }
            String tableName = introspectedTable.getFullyQualifiedTable().toString();
            sb.append(tableName);
            topLevelClass.addJavaDocLine(sb.toString());
            this.addJavadocTag(topLevelClass, true);
            topLevelClass.addJavaDocLine(" */");

            classAnnotationFullyQualifiedNames.forEach(classAnnotationFullyQualifiedName -> {
                String classAnnotation = AnnotationHelper.fromFullyQualifiedName(classAnnotationFullyQualifiedName);
                topLevelClass.addJavaDocLine(String.format("%s(name = \"%s\")", classAnnotation, tableName));
                topLevelClass.addImportedType(new FullyQualifiedJavaType(classAnnotationFullyQualifiedName));
            });
        }
    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerEnum.addJavaDocLine("/**");
            innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator.");
            sb.append(" * This enum corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            innerEnum.addJavaDocLine(sb.toString());
            this.addJavadocTag(innerEnum, false);
            innerEnum.addJavaDocLine(" */");
        }
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            field.addJavaDocLine("/**");
            String remarks = introspectedColumn.getRemarks();
            StringBuilder sb = new StringBuilder();
            sb.append(" * ");
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                sb.append(remarks);
            }
            field.addJavaDocLine(sb.toString());
            this.addJavadocTag(field, false);
            field.addJavaDocLine(" */");
        }
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
//        if (!this.suppressAllComments) {
//            StringBuilder sb = new StringBuilder();
//            field.addJavaDocLine("/**");
//            field.addJavaDocLine(" * This field was generated by MyBatis Generator.");
//            sb.append(" * This field corresponds to the database table ");
//            sb.append(introspectedTable.getFullyQualifiedTable());
//            field.addJavaDocLine(sb.toString());
//            this.addJavadocTag(field, false);
//            field.addJavaDocLine(" */");
//        }
    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * This method was generated by MyBatis Generator.");
            sb.append(" * This method corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            method.addJavaDocLine(sb.toString());
            this.addJavadocTag(method, false);
            method.addJavaDocLine(" */");
        }
    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            StringBuilder sb = new StringBuilder();
            sb.append(" * ");
            if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                sb.append("获取" + introspectedColumn.getRemarks());
            }
            method.addJavaDocLine(sb.toString());
            sb.setLength(0);
            sb.append(" * @return");
            if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                sb.append(" " + introspectedColumn.getRemarks());
            }
            method.addJavaDocLine(sb.toString());
            this.addJavadocTag(method, false);
            method.addJavaDocLine(" */");
        }
    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            StringBuilder sb = new StringBuilder();
            sb.append(" * ");
            if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                sb.append("设置" + introspectedColumn.getRemarks());
            }
            method.addJavaDocLine(sb.toString());
            Parameter param = method.getParameters().get(0);
            sb.setLength(0);
            sb.append(" * @param ");
            sb.append(param.getName());
            if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                sb.append(" " + introspectedColumn.getRemarks());
            }
            method.addJavaDocLine(sb.toString());
            this.addJavadocTag(method, false);
            method.addJavaDocLine(" */");
        }
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
//        if (!this.suppressAllComments) {
//            StringBuilder sb = new StringBuilder();
//            innerClass.addJavaDocLine("/**");
//            if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
//                sb.append(introspectedTable.getRemarks() + ":");
//            }
//            sb.append(introspectedTable.getFullyQualifiedTable());
//            innerClass.addJavaDocLine(sb.toString());
//            this.addJavadocTag(innerClass, markAsDoNotDelete);
//            innerClass.addJavaDocLine(" */");
//        }
    }
}
