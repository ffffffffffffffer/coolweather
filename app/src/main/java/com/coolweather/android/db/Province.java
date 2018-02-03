package com.coolweather.android.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * @author TanJJ
 * @time 2018/2/3 17:10
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.db
 * @des 省实体类
 */

public class Province extends DataSupport {

    /**
     * id : 1
     * name : 北京
     */

    private int id;
    @SerializedName("name")
    private String provinceName;
    private int provinceCode;

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
