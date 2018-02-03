package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * @author TanJJ
 * @time 2018/2/3 17:12
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.db
 * @des 城市实体
 */

public class City extends DataSupport {
    private  int id;
    private  String cityName;
    private  int cityCode;
    private int provinceId;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
