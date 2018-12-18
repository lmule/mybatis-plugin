package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class MyServiceGeneratorPlugin extends PluginAdapter {

    private String modelSuffixName;
    private String daoSuffixName;
    private String serviceSuffixName;
    private String rootClass;
    private String targetPackage;
    private String targetProject;

    @Override
    public boolean validate(List<String> list) {
        modelSuffixName = PropertyHelper.getModelSuffixName(context);
        daoSuffixName = PropertyHelper.getDaoSuffixName(context);
        serviceSuffixName = PropertyHelper.getServiceSuffixName(properties);
        targetPackage = PropertyHelper.valueFromProperties(properties, "targetPackage");
        targetProject = PropertyHelper.valueFromProperties(properties, "targetProject");
        rootClass = PropertyHelper.valueFromProperties(properties, "rootClass");

        return stringHasValue(targetPackage)
                && stringHasValue(targetProject);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<>();
        GeneratedJavaFile generatedJavaFile = generatedModelFile(introspectedTable);
        if (null != generatedJavaFile) {
            files.add(generatedJavaFile);
        }
        return files;
    }

    private GeneratedJavaFile generatedModelFile(IntrospectedTable introspectedTable) {
        String serviceName = IntrospectedTableHelper.getCamelizeTableName(introspectedTable) + serviceSuffixName;
        FullyQualifiedJavaType serviceFullyQualifiedJavaType = new FullyQualifiedJavaType(targetPackage + "." + serviceName);

//        String recordType = introspectedTable.getBaseRecordType();
        TopLevelClass modelTopLevelClass = new TopLevelClass(serviceFullyQualifiedJavaType);
        modelTopLevelClass.setVisibility(JavaVisibility.PUBLIC);
        modelTopLevelClass.addImportedType(serviceFullyQualifiedJavaType);


//        Interface interfaze = new Interface(daoFullyQualifiedJavaType);
//        interfaze.setVisibility(JavaVisibility.PUBLIC);
//        String modelName = IntrospectedTableHelper.getCamelizeTableName(introspectedTable) + modelSuffixName;
//        interfaze.addImportedType(new FullyQualifiedJavaType(modelTargetPackage + "." + modelName));
        if (stringHasValue(rootClass)) {
            modelTopLevelClass.setSuperClass(rootClass);
            String modelName = IntrospectedTableHelper.getCamelizeTableName(introspectedTable) + modelSuffixName;
            String daoName = IntrospectedTableHelper.getCamelizeTableName(introspectedTable) + daoSuffixName;
            String genericName = String.format("<%s, %s>", daoName, modelName);
            modelTopLevelClass.setSuperClass(new FullyQualifiedJavaType(rootClass + genericName));
//            interfaze.addSuperInterface(new FullyQualifiedJavaType(rootInterface + genericName));
        }

        JavaFormatter javaFormatter = new DefaultJavaFormatter();
        javaFormatter.setContext(context);
        return new GeneratedJavaFile(modelTopLevelClass, targetProject, javaFormatter);
    }
}
