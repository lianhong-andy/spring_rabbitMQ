package com.andy.quartz;

import java.text.SimpleDateFormat;

public class TestQuartz {
    private int i = 0;
    public void process(){
        System.out.println("startTime = " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))+"第"+(i++)+"次执行");
    }
}
