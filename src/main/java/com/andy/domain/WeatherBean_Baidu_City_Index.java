package com.andy.domain;

import java.io.Serializable;

/**
 * 天气Bean
 *
 * @author SHANHY
 *
 */
@SuppressWarnings("serial")
public class WeatherBean_Baidu_City_Index implements Serializable {

    private String title;//标题
    private String zs;//舒适度
    private String tipt;//指数简述
    private String des;//指数概述

    public WeatherBean_Baidu_City_Index() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getTipt() {
        return tipt;
    }

    public void setTipt(String tipt) {
        this.tipt = tipt;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "WeatherBean_Baidu_City_Index{" +
                "title='" + title + '\'' +
                ", zs='" + zs + '\'' +
                ", tipt='" + tipt + '\'' +
                ", des='" + des + '\'' +
                '}';
    }
}
