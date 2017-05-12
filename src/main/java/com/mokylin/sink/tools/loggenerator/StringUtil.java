package com.mokylin.sink.tools.loggenerator;

/**
 *
 * @author xiaoyaguang
 * @create 2015-12-26 20:52
 */

public class StringUtil {

    public static String firstLetterToLowerCase(String string) {
        return string.substring(0, 1).toLowerCase().concat(string.substring(1));
    }

    public static String firstLetterToUpperCase(String string) {
        return string.substring(0, 1).toUpperCase().concat(string.substring(1));
    }

}
