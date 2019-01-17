package com.mokylin.sink.tools.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yaguang.xiao on 2017/7/19.
 */
public class TestException {

    private static final Logger logger = LoggerFactory.getLogger(TestException.class);

    public static void main(String[] args) {

        try {
            throwException();
        } catch (Throwable throwable) {
            logger.error("error ", throwable);
        }

    }

    private static void throwException() {
        throw new NullPointerException();
    }

}
