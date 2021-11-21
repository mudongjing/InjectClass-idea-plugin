package xyz.wcd.utils;

public class StringNameUtil {
    public static String createSetterInjectMethod(String field){
        if (field.charAt(0)>='a' && field.charAt(0)<='z'){ return "set"+field.substring(0,1).toUpperCase()+field.substring(1); }
        else{ return "set"+field; }
    }
    public static String createGetterInjectMethod(String field){
        if (field.charAt(0)>='a' && field.charAt(0)<='z'){ return "get"+field.substring(0,1).toUpperCase()+field.substring(1);}
        else{ return "get"+field;}
    }
}
