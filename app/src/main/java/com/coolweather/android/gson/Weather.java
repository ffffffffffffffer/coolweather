package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author TanJJ
 * @time 2018/2/4 23:40
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 天气实体类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
