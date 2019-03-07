package com.andy.domain;

import java.io.Serializable;

/**
 * 天气Bean
 *
 * @author SHANHY
 *
 */
@SuppressWarnings("serial")
public class WeatherBean_Baidu_City_Weatherdata implements Serializable {

    private String date;// 日期
    private String dayPictureUrl;// 白天的天气图片
    private String nightPictureUrl;// 晚上的天气图片
    private String weather;// 天气
    private String wind;// 风向
    private String temperature;// 温度

    public WeatherBean_Baidu_City_Weatherdata() {
        super();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayPictureUrl() {
        return dayPictureUrl;
    }

    public void setDayPictureUrl(String dayPictureUrl) {
        this.dayPictureUrl = dayPictureUrl;
    }

    public String getNightPictureUrl() {
        return nightPictureUrl;
    }

    public void setNightPictureUrl(String nightPictureUrl) {
        this.nightPictureUrl = nightPictureUrl;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherBean_Baidu_City_Weatherdata{" +
                "date='" + date + '\'' +
                ", dayPictureUrl='" + dayPictureUrl + '\'' +
                ", nightPictureUrl='" + nightPictureUrl + '\'' +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}
