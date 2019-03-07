package com.andy.util;


import com.andy.domain.WeatherBean_Baidu;
import com.andy.domain.WeatherBean_Baidu_City;
import com.andy.domain.WeatherBean_Baidu_City_Index;
import com.andy.domain.WeatherBean_Baidu_City_Weatherdata;
import com.rabbitmq.tools.json.JSONUtil;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static void test(String jsonStrBody){
        JSONObject jsonObj = JSONObject.fromObject(jsonStrBody);

        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("results", WeatherBean_Baidu_City.class);
        classMap.put("index", WeatherBean_Baidu_City_Index.class);
        classMap.put("weather_data", WeatherBean_Baidu_City_Weatherdata.class);
        // 将JSON转换成WeatherBean_Baidu
        WeatherBean_Baidu weather = (WeatherBean_Baidu) JSONObject.toBean(jsonObj,
                WeatherBean_Baidu.class, classMap);
        System.out.println(weather.getResults());
    }

    public static void main(String[] args) {
        String s = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\json.json")),"gbk"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){
                sb.append(line).append("\r\n");
            }
            bufferedReader.close();
            s += sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
        test(s);
    }

}
