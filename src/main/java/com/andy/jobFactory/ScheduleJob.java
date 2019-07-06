package com.andy.jobFactory;

import java.util.Date;

/**
 *  
* @Description: 计划任务信息 
* @author lianhong
 */
public class ScheduleJob {  
  
    public static final String STATUS_RUNNING = "1";  
    public static final String STATUS_NOT_RUNNING = "0";  
    public static final String CONCURRENT_IS = "1";  
    public static final String CONCURRENT_NOT = "0";  
    private Long jobId;  
  
    private Date createTime;
  
    private Date updateTime;  
    /** 
     * 任务名称 
     */  
    private String jobName;  
    /** 
     * 任务分组 
     */  
    private String jobGroup;  
    /** 
     * 任务状态 是否启动任务 
     */  
    private String jobStatus;  
    /** 
     * cron表达式 
     */  
    private String cronExpression;  
    /** 
     * 描述 
     */  
    private String description;  
    /** 
     * 任务执行时调用哪个类的方法 包名+类名 
     */  
    private String beanClass;  
    /** 
     * 任务是否有状态 
     */  
    private String isConcurrent;  
    /** 
     * spring bean 
     */  
    private String springId;  
    /** 
     * 任务调用的方法名 
     */  
    private String methodName;

    public static String getStatusRunning() {
        return STATUS_RUNNING;
    }

    public static String getStatusNotRunning() {
        return STATUS_NOT_RUNNING;
    }

    public static String getConcurrentIs() {
        return CONCURRENT_IS;
    }

    public static String getConcurrentNot() {
        return CONCURRENT_NOT;
    }
}