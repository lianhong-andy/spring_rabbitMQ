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
public class WeatherBean_Baidu_City implements Serializable {

    private String currentCity;//城市名称
    private String pm25;//pm2.5值
    private List<WeatherBean_Baidu_City_Index> index;//指数集合
    private List<WeatherBean_Baidu_City_Weatherdata> weather_data;//几天的天气集合

    public WeatherBean_Baidu_City() {
        super();
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public List<WeatherBean_Baidu_City_Index> getIndex() {
        return index;
    }

    public void setIndex(List<WeatherBean_Baidu_City_Index> index) {
        this.index = index;
    }

    public List<WeatherBean_Baidu_City_Weatherdata> getWeather_data() {
        return weather_data;
    }

    public void setWeather_data(
            List<WeatherBean_Baidu_City_Weatherdata> weather_data) {
        this.weather_data = weather_data;
    }

    @Override
    public String toString() {
        return "WeatherBean_Baidu_City{" +
                "currentCity='" + currentCity + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", index=" + index +
                ", weather_data=" + weather_data +
                '}';
    }
}