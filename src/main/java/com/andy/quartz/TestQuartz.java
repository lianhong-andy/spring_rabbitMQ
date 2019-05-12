package com.andy.quartz;

import com.andy.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class TestQuartz {
    private static final Logger logger = LoggerFactory.getLogger(TestQuartz.class);
    private int i = 0;
    public void process(){
        logger.info("日志打印===》");
        System.out.println("startTime = " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))+"第"+(i++)+"次执行");
    }
}
