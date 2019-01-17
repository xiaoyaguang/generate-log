package com.mokylin.sink.tools.log;

import org.joda.time.DateTime;

/**
 * Created by yaguang.xiao on 2017/9/19.
 */
public class Test {

    public static void main(String[] args) {
        DateTime dateTime = new DateTime(9223372036854775807l);
        System.out.println(dateTime);

        System.out.println(Long.MAX_VALUE);
    }

}
