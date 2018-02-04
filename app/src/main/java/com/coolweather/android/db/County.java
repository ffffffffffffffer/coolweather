package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * @author TanJJ
 * @time 2018/2/3 17:14
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.db
 * @des 城乡实体类
 */

public class County extends DataSupport {
    private int id;
    private String weatherId;
    private int cityId;
    private String countryName;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
