package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class IntrospectedTableHelper {


    public static String getCamelizedTableName(IntrospectedTable introspectedTable) {
        String recordType = introspectedTable.getBaseRecordType();
        return recordType.substring(recordType.lastIndexOf(".") + 1, recordType.length());
    }

    public static String getPrimaryKeyJavaType(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        String primaryKeyJavaType = "java.lang.Integer";
        if (primaryKeyColumns.size() > 0) {
            primaryKeyJavaType = String.valueOf(primaryKeyColumns.get(0).getFullyQualifiedJavaType());
        }
        return primaryKeyJavaType;
    }

    /**
     *
     * @param targetProject
     * @param targetPackage
     * @return
     */
    private static String getPhysicalBasePath(String targetProject, String targetPackage) {
        return targetProject + "/" + targetPackage.replace(".", "/") + "/";
    }

    public static String getModelFilePath(IntrospectedTable introspectedTable, String suffixName) {
        JavaModelGeneratorConfiguration configuration = introspectedTable.getContext().getJavaModelGeneratorConfiguration();
        String targetPackage = configuration.getProperty("modelTargetPackage");
        String targetProject = configuration.getTargetProject();
        return getPhysicalBasePath(targetProject, targetPackage) + getCamelizedTableName(introspectedTable) + suffixName + ".java";
    }

    /**
     * 获取xml的文件名
     * @param introspectedTable
     * @return
     */
    public static String getXmlMapperFilePath(IntrospectedTable introspectedTable) {
        SqlMapGeneratorConfiguration configuration = introspectedTable.getContext().getSqlMapGeneratorConfiguration();
        String targetPackage = configuration.getTargetPackage();
        String targetProject = configuration.getTargetProject();
        return getPhysicalBasePath(targetProject, targetPackage) + getCamelizedTableName(introspectedTable) + "Mapper.xml";
    }

    public static String getDaoFilePath(IntrospectedTable introspectedTable, String suffixName) {
        SqlMapGeneratorConfiguration configuration = introspectedTable.getContext().getSqlMapGeneratorConfiguration();
        String targetPackage = configuration.getTargetPackage();
        String targetProject = configuration.getTargetProject();
        return getPhysicalBasePath(targetProject, targetPackage) + getCamelizedTableName(introspectedTable) + suffixName + ".java";
    }

    public static String getServiceFilePath(String targetProject, String targetPackage, String camelizedTableName, String suffixName) {
        return getPhysicalBasePath(targetProject, targetPackage) + camelizedTableName + suffixName + ".java";
    }

    public static String getControllerFilePath(String targetProject, String targetPackage, String camelizedTableName, String suffixName) {
        return getPhysicalBasePath(targetProject, targetPackage) + camelizedTableName + suffixName + ".java";
    }


//    /**
//     * 获取model配置
//     * @param introspectedTable
//     * @param propertyName
//     * @return
//     */
//    public static String getModelConfigurationValue(IntrospectedTable introspectedTable, String propertyName) {
//        return getModelConfigurationValue(introspectedTable.getContext(), propertyName, "");
//    }
//
//    /**
//     * 获取model配置
//     * @param introspectedTable
//     * @param propertyName
//     * @param defaultValue
//     * @return
//     */
//    public static String getModelConfigurationValue(IntrospectedTable introspectedTable, String propertyName, String defaultValue) {
//        return getModelConfigurationValue(introspectedTable.getContext(), propertyName, defaultValue);
//    }

    /**
     * 获取model配置
     * @param context
     * @param propertyName
     * @return
     */
    public static String getModelConfigurationValue(Context context, String propertyName) {
        return getModelConfigurationValue(context, propertyName, "");
    }

    /**
     * 获取model配置
     * @param context
     * @param propertyName
     * @param defaultValue
     * @return
     */
    public static String getModelConfigurationValue(Context context, String propertyName, String defaultValue) {
        return getConfigurationPropertyValue(context.getJavaModelGeneratorConfiguration(), propertyName, defaultValue);
    }

    public static String getDaoConfigurationValue(Context context, String propertyName) {
        return getDaoConfigurationValue(context, propertyName, "");
    }

    public static String getDaoConfigurationValue(Context context, String propertyName, String defaultValue) {
        return getConfigurationPropertyValue(context.getSqlMapGeneratorConfiguration(), propertyName, defaultValue);
    }

    /**
     *
     * @param propertyHolder
     * @param propertyName
     * @param defaultValue
     * @return
     */
    private static String getConfigurationPropertyValue(PropertyHolder propertyHolder, String propertyName, String defaultValue) {
        String propertyValue = propertyHolder.getProperty(propertyName);
        return stringHasValue(propertyValue) ? propertyValue : defaultValue;
    }
}
