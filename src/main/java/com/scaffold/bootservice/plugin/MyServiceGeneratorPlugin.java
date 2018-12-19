package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
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
    private String serviceTargetPackage;
    private String serviceTargetProject;

    @Override
    public boolean validate(List<String> list) {
        modelSuffixName = PropertyHelper.getModelSuffixName(context);
        daoSuffixName = PropertyHelper.getDaoSuffixName(context);
        serviceSuffixName = PropertyHelper.getServiceSuffixName(properties);
        serviceTargetPackage = PropertyHelper.valueFromProperties(properties, "targetPackage");
        serviceTargetProject = PropertyHelper.valueFromProperties(properties, "targetProject");
        rootClass = PropertyHelper.valueFromProperties(properties, "rootClass");

        return stringHasValue(serviceTargetPackage)
                && stringHasValue(serviceTargetProject);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<>();
        GeneratedJavaFile generatedJavaFile = generatedServiceFile(introspectedTable);
        if (null != generatedJavaFile) {
            files.add(generatedJavaFile);
        }
        return files;
    }

    private GeneratedJavaFile generatedServiceFile(IntrospectedTable introspectedTable) {
        String camelizedTableName = IntrospectedTableHelper.getCamelizedTableName(introspectedTable);
        File serviceFile = new File(IntrospectedTableHelper.getServiceFilePath(serviceTargetProject,
                serviceTargetPackage,
                camelizedTableName,
                serviceSuffixName));
        if (serviceFile.exists()) {
            return null;
        }
        String serviceName = camelizedTableName + serviceSuffixName;
        FullyQualifiedJavaType serviceFullyQualifiedJavaType = new FullyQualifiedJavaType(serviceTargetPackage + "." + serviceName);

        TopLevelClass modelTopLevelClass = new TopLevelClass(serviceFullyQualifiedJavaType);
        modelTopLevelClass.setVisibility(JavaVisibility.PUBLIC);
        modelTopLevelClass.addImportedType(new FullyQualifiedJavaType(rootClass));


        if (stringHasValue(rootClass)) {
            modelTopLevelClass.setSuperClass(rootClass);

            String modelName = camelizedTableName + modelSuffixName;
            String modelTargetPackage = IntrospectedTableHelper.getModelConfigurationValue(context, "modelTargetPackage");
            FullyQualifiedJavaType modelFullyQualifiedJavaType = new FullyQualifiedJavaType(modelTargetPackage + "." + modelName);
            modelTopLevelClass.addImportedType(modelFullyQualifiedJavaType);

            String daoName = camelizedTableName + daoSuffixName;
            String daoTargetPackage = context.getSqlMapGeneratorConfiguration().getTargetPackage();
            FullyQualifiedJavaType daoFullyQualifiedJavaType = new FullyQualifiedJavaType(daoTargetPackage + "." + daoName);
            modelTopLevelClass.addImportedType(daoFullyQualifiedJavaType);

            String genericName = String.format("<%s, %s>", daoName, modelName);
            modelTopLevelClass.setSuperClass(new FullyQualifiedJavaType(rootClass + genericName));
        }

        JavaFormatter javaFormatter = new DefaultJavaFormatter();
        javaFormatter.setContext(context);
        return new GeneratedJavaFile(modelTopLevelClass, serviceTargetProject, javaFormatter);
    }
}
