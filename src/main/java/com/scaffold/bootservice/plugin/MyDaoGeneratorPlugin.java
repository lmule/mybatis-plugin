package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class MyDaoGeneratorPlugin extends PluginAdapter {

    private String rootInterface;
    private String targetPackage;
    private String targetProject;
    private String modelSuffixName;
    private String daoSuffixName;
    private String modelTargetPackage;
    private Boolean isOverwrite;

    @Override
    public boolean validate(List<String> list) {
        rootInterface = IntrospectedTableHelper.getDaoConfigurationValue(context, "rootInterface");
        targetPackage = this.getContext().getSqlMapGeneratorConfiguration().getTargetPackage();
        targetProject = this.getContext().getSqlMapGeneratorConfiguration().getTargetProject();
        modelSuffixName = PropertyHelper.getModelSuffixName(context);
        daoSuffixName = PropertyHelper.getDaoSuffixName(context);
        modelTargetPackage = IntrospectedTableHelper.getModelConfigurationValue(context, "modelTargetPackage");
        isOverwrite = Boolean.parseBoolean(IntrospectedTableHelper.getDaoConfigurationValue(context, "isOverwrite", "false"));

        return stringHasValue(rootInterface)
                && stringHasValue(modelTargetPackage)
                && stringHasValue(targetPackage)
                && stringHasValue(targetProject);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<>();
        GeneratedJavaFile generatedJavaFile = generatedDaoFile(introspectedTable);
        if (null != generatedJavaFile) {
            files.add(generatedJavaFile);
        }

        return files;
    }

    private GeneratedJavaFile generatedDaoFile(IntrospectedTable introspectedTable) {
        if (!isOverwrite) {
            File daoFile = new File(IntrospectedTableHelper.getDaoFilePath(introspectedTable, daoSuffixName));
            if (daoFile.exists()) {
                return null;
            }
        }

        String daoName = IntrospectedTableHelper.getCamelizedTableName(introspectedTable) + daoSuffixName;
        FullyQualifiedJavaType daoFullyQualifiedJavaType = new FullyQualifiedJavaType(targetPackage + "." + daoName);

        Interface interfaze = new Interface(daoFullyQualifiedJavaType);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        String modelName = IntrospectedTableHelper.getCamelizedTableName(introspectedTable) + modelSuffixName;
        interfaze.addImportedType(new FullyQualifiedJavaType(modelTargetPackage + "." + modelName));
        interfaze.addImportedType(new FullyQualifiedJavaType(rootInterface));
        if (stringHasValue(rootInterface)) {
            String primaryKeyJavaType = IntrospectedTableHelper.getPrimaryKeyJavaType(introspectedTable);
            String genericName = String.format("<%s, %s>", modelName, primaryKeyJavaType);
            interfaze.addSuperInterface(new FullyQualifiedJavaType(rootInterface + genericName));
        }

        JavaFormatter javaFormatter = new DefaultJavaFormatter();
        javaFormatter.setContext(context);
        return new GeneratedJavaFile(interfaze, targetProject, javaFormatter);
    }
}
