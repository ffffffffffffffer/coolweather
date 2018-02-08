package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author TanJJ
 * @time 2018/2/4 23:37
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 天气预报实体类
 */

public class Forecast {
    public String date;
    @SerializedName("cond")
    public More more;
    @SerializedName("tmp")
    public Temperature temperture;

    public class More {
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        public String max;
        public String min;

    }
}
