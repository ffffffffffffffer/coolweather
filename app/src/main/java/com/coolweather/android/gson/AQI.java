package com.coolweather.android.gson;

/**
 * @author TanJJ
 * @time 2018/2/4 23:23
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 空气质量指数实体类
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
