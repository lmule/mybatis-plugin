package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.*;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyModelGeneratorPlugin extends PluginAdapter {

    private String modelSuffixName;
    private String targetPackage;
    private String targetProject;
    private Boolean isOverwrite;

    @Override
    public boolean validate(List<String> list) {
        modelSuffixName = PropertyHelper.getModelSuffixName(context);
        targetPackage = IntrospectedTableHelper.getModelConfigurationValue(context, "modelTargetPackage");
        targetProject = this.getContext().getJavaModelGeneratorConfiguration().getTargetProject();
        isOverwrite = Boolean.parseBoolean(IntrospectedTableHelper.getModelConfigurationValue(context, "isOverwrite", "false"));

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
        if (!isOverwrite) {
            File modelFile = new File(IntrospectedTableHelper.getFullFilePath(introspectedTable, modelSuffixName));
            if (modelFile.exists()) {
                return null;
            }
        }

        String recordType = introspectedTable.getBaseRecordType();
        String modelName = IntrospectedTableHelper.getCamelizeTableName(introspectedTable) + modelSuffixName;
        FullyQualifiedJavaType modelQualifiedType = new FullyQualifiedJavaType(targetPackage + "." + modelName);
        TopLevelClass modelTopLevelClass = new TopLevelClass(modelQualifiedType);
        modelTopLevelClass.setVisibility(JavaVisibility.PUBLIC);
        modelTopLevelClass.addImportedType(new FullyQualifiedJavaType(recordType));
        modelTopLevelClass.setSuperClass(new FullyQualifiedJavaType(recordType));

        JavaFormatter javaFormatter = new DefaultJavaFormatter();
        javaFormatter.setContext(context);
        return new GeneratedJavaFile(modelTopLevelClass, targetProject, javaFormatter);
    }
}
