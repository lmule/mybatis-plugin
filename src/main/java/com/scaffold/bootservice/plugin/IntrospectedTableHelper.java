package com.scaffold.bootservice.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class IntrospectedTableHelper {


    public static String getCamelizeTableName(IntrospectedTable introspectedTable) {
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
     * 获取生成文件的base相对物理路径
     * @param introspectedTable
     * @return
     */
    public static String getPhysicalBasePath(IntrospectedTable introspectedTable) {
        SqlMapGeneratorConfiguration configuration = introspectedTable.getContext().getSqlMapGeneratorConfiguration();
        String targetPackage = configuration.getTargetPackage();
        String targetProject = configuration.getTargetProject();
        return targetProject + "/" + targetPackage.replace(".", "/") + "/";
    }

    /**
     * 获取xml的文件名
     * @param introspectedTable
     * @return
     */
    public static String getXmlMapperPath(IntrospectedTable introspectedTable) {
        return getPhysicalBasePath(introspectedTable) + getCamelizeTableName(introspectedTable) + "Mapper.xml";
    }

    /**
     * 获取文件名全路径
     * @param introspectedTable
     * @param suffix
     * @return
     */
    public static String getFullFilePath(IntrospectedTable introspectedTable, String suffix) {
        return getPhysicalBasePath(introspectedTable) + getCamelizeTableName(introspectedTable) + suffix + ".java";
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
