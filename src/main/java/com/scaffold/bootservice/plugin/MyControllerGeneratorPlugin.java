package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class MyControllerGeneratorPlugin extends PluginAdapter {

    private String controllerSuffixName;
    private String rootClass;
    private String controllerTargetPackage;
    private String controllerTargetProject;

    @Override
    public boolean validate(List<String> list) {
        controllerSuffixName = PropertyHelper.getControllerSuffixName(properties);
        controllerTargetPackage = PropertyHelper.valueFromProperties(properties, "targetPackage");
        controllerTargetProject = PropertyHelper.valueFromProperties(properties, "targetProject");
        rootClass = PropertyHelper.valueFromProperties(properties, "rootClass");

        return stringHasValue(controllerTargetPackage)
                && stringHasValue(controllerTargetProject);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<>();
        GeneratedJavaFile generatedJavaFile = generatedControllerFile(introspectedTable);
        if (null != generatedJavaFile) {
            files.add(generatedJavaFile);
        }
        return files;
    }

    private GeneratedJavaFile generatedControllerFile(IntrospectedTable introspectedTable) {
        String camelizedTableName = IntrospectedTableHelper.getCamelizedTableName(introspectedTable);
        File controllerFile = new File(IntrospectedTableHelper.getControllerFilePath(controllerTargetProject,
                controllerTargetPackage,
                camelizedTableName,
                controllerSuffixName));
        if (controllerFile.exists()) {
            return null;
        }
        String controllerName = camelizedTableName + controllerSuffixName;
        FullyQualifiedJavaType controllerFullyQualifiedJavaType = new FullyQualifiedJavaType(controllerTargetPackage + "." + controllerName);

        TopLevelClass topLevelClass = new TopLevelClass(controllerFullyQualifiedJavaType);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RestController");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping;");

        topLevelClass.addJavaDocLine("@RestController");
        topLevelClass.addJavaDocLine(String.format("@RequestMapping(\"/%s\")", introspectedTable.getFullyQualifiedTable()));

        if (stringHasValue(rootClass)) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(rootClass));
            topLevelClass.setSuperClass(rootClass);

            String serviceSuffixName = PropertyHelper.valueFromProperties(properties, "serviceSuffixName");
            String serviceName = camelizedTableName + serviceSuffixName;
            String serviceTargetPackage = PropertyHelper.valueFromProperties(properties, "serviceTargetPackage");
            topLevelClass.addImportedType(serviceTargetPackage + "." + serviceName);

            String modelSuffixName = PropertyHelper.getModelSuffixName(context);
            String modelName = camelizedTableName + modelSuffixName;
            String modelTargetPackage = IntrospectedTableHelper.getModelConfigurationValue(context, "modelTargetPackage");
            topLevelClass.addImportedType(modelTargetPackage + "." + modelName);

            String genericName = String.format("<%s, %s>", serviceName, modelName);
            topLevelClass.setSuperClass(new FullyQualifiedJavaType(rootClass + genericName));
        }

        JavaFormatter javaFormatter = new DefaultJavaFormatter();
        javaFormatter.setContext(context);
        return new GeneratedJavaFile(topLevelClass, controllerTargetProject, javaFormatter);
    }

}

