package com.coolweather.android.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author TanJJ
 * @time 2018/2/3 19:30
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.utils
 * @des 网络处理工具
 */

public class HttpUtils {

    /**
     * 通过OkHttp请求网络
     *
     * @param address  请求地址
     * @param callback OkHttp的回调接口
     */
    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
