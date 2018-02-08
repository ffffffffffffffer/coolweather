package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author TanJJ
 * @time 2018/2/4 23:27
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 当前天气信息实体类
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
