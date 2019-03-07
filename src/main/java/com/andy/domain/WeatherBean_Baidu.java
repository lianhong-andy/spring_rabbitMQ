package com.andy.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 天气Bean
 *
 * @author SHANHY
 *
 */
@SuppressWarnings("serial")
public class WeatherBean_Baidu implements Serializable {

    private String error;//错误号
    private String status;//状态值
    private String date;//日期
    private List<WeatherBean_Baidu_City> results;//城市天气预报集合（因为一次可以查询多个城市）

    public WeatherBean_Baidu() {
        super();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WeatherBean_Baidu_City> getResults() {
        return results;
    }

    public void setResults(List<WeatherBean_Baidu_City> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "WeatherBean_Baidu{" +
                "error='" + error + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", results=" + results +
                '}';
    }
}
