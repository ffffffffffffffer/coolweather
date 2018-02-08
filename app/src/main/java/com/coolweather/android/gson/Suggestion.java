package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author TanJJ
 * @time 2018/2/4 23:30
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.gson
 * @des 天气建议实体类
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }
    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
