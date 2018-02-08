package com.coolweather.android.utils;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author TanJJ
 * @time 2018/2/3 19:34
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.utils
 * @des 实用操作工具类
 */

public class Utility {

    /**
     * 将返回的json数据解析成Weather实体类
     *
     * @param response json数据
     *
     * @return 返回解析后的Weather对象
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherDate = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherDate, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * (原始方法)解析和处理服务器返回的省级数据
     *
     * @param response 服务器返回的数据
     *
     * @return 成功写入数据库与否
     * <p>
     * [{"id":1,"name":"北京"},{"id":2,"name":"上海"},{"id":3,"name":"天津"},{"id":4,"name":"重庆"},{"id":5,"name":"香港"},
     * {"id":6,"name":"澳门"},{"id":7,"name":"台湾"},{"id":8,"name":"黑龙江"},{"id":9,"name":"吉林"},{"id":10,"name":"辽宁"},
     * {"id":11,"name":"内蒙古"},{"id":12,"name":"河北"},{"id":13,"name":"河南"},{"id":14,"name":"山西"},{"id":15,
     * "name":"山东"},{"id":16,"name":"江苏"},{"id":17,"name":"浙江"},{"id":18,"name":"福建"},{"id":19,"name":"江西"},{"id":20,
     * "name":"安徽"},{"id":21,"name":"湖北"},{"id":22,"name":"湖南"},{"id":23,"name":"广东"},{"id":24,"name":"广西"},{"id":25,
     * "name":"海南"},{"id":26,"name":"贵州"},{"id":27,"name":"云南"},{"id":28,"name":"四川"},{"id":29,"name":"西藏"},{"id":30,
     * "name":"陕西"},{"id":31,"name":"宁夏"},{"id":32,"name":"甘肃"},{"id":33,"name":"青海"},{"id":34,"name":"新疆"}]
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    //存进数据库
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * (原始方法)解析和处理服务器返回的市级数据
     *
     * @param response   服务器返回的数据
     * @param provinceId 省级对应的provinceCode
     *
     * @return 成功写入数据库与否
     * [{"id":205,"name":"广州"},{"id":206,"name":"韶关"},{"id":207,"name":"惠州"},{"id":208,"name":"梅州"},{"id":209,
     * "name":"汕头"},{"id":210,"name":"深圳"},{"id":211,"name":"珠海"},{"id":212,"name":"顺德"},{"id":213,"name":"肇庆"},
     * {"id":214,"name":"湛江"},{"id":215,"name":"江门"},{"id":216,"name":"河源"},{"id":217,"name":"清远"},{"id":218,
     * "name":"云浮"},{"id":219,"name":"潮州"},{"id":220,"name":"东莞"},{"id":221,"name":"中山"},{"id":222,"name":"阳江"},
     * {"id":223,"name":"揭阳"},{"id":224,"name":"茂名"},{"id":225,"name":"汕尾"},{"id":350,"name":"佛山"}]
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * (原始方法)解析和处理服务器返回的乡级数据
     *
     * @param response 服务器返回的数据
     * @param cityId   市级对应的CityCode
     *
     * @return 成功写入数据库与否
     * [{"id":1615,"name":"中山","weather_id":"CN101281701"}]
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCountryName(jsonObject.getString("name"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
