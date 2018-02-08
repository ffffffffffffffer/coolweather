package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author TanJJ
 * @time 2018/2/4 23:20
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 天气基本信息实体类
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherID;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
