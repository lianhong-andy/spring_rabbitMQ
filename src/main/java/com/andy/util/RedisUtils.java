package com.andy.util;


import org.redisson.config.Config;

public class RedisUtils {
    public static void  redisUtils(){
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("127.0.0.1:6369","127.0.0.1:6379", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);

    }
}
