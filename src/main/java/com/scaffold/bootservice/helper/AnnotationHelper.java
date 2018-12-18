package com.scaffold.bootservice.helper;


public class AnnotationHelper {
    public static String fromFullyQualifiedName(String annotationFullyQualifiedName) {
        int lastIndexOfDot = annotationFullyQualifiedName.lastIndexOf(".");
        String atAnnotationName = annotationFullyQualifiedName.substring(lastIndexOfDot + 1);
        return String.format("@%s", atAnnotationName);
    }
}
