package com.scaffold.bootservice.plugin;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import  java.util.Properties;

import org.mybatis.generator.config.Context;

public class PropertyHelper {

    public static String valueFromProperties(Properties properties, String propertyName) {
        return valueFromProperties(properties, propertyName, "");
    }

    /**
     * 从键值对的property中获取指定propertyName的值
     * @param properties
     * @param propertyName
     * @param defaultValue
     * @return
     */
    public static String valueFromProperties(Properties properties, String propertyName, String defaultValue) {
        if (!properties.containsKey(propertyName)) {
            return defaultValue;
        }
        String propertyValue = properties.get(propertyName).toString();
        return stringHasValue(propertyValue) ? propertyValue : defaultValue;
    }

    /**
     * 获取model文件后缀
     * @param context
     * @return
     */
    public static String getModelSuffixName(Context context) {
        return IntrospectedTableHelper.getModelConfigurationValue(context, "modelSuffixName", "Model");
    }

    /**
     * 获取dao文件后缀
     * @param context
     * @return
     */
    public static String getDaoSuffixName(Context context) {
        return IntrospectedTableHelper.getDaoConfigurationValue(context, "daoSuffixName", "Dao");
    }

    /**
     * 获取service文件后缀
     * @param properties
     * @return
     */
    public static String getServiceSuffixName(Properties properties) {
        return PropertyHelper.valueFromProperties(properties, "serviceSuffixName", "Service");
    }

}
