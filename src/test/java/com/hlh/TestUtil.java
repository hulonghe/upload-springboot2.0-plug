package com.hlh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class TestUtil {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Test
    public void test() {
        logger.info("/======================================================================/");
        logger.info("/======================================================================/");
    }

    @Test
    public void testEncode() throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("你那个百度NGD弄好了吗？", "utf-8"));
    }

    @Test
    public void testAK() throws UnsupportedEncodingException {
        String encode = URLEncoder.encode("TA3riKMjNLQFYCFw0jV0omNiqLN9aIyyJHxTqhgWTt0%3D", "utf-8");
        System.out.println(encode);
    }

}
