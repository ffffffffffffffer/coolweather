package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.utils.HttpUtils;
import com.coolweather.android.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author TanJJ
 * @time 2018/2/10 23:34
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.service
 * @des 自动更新数据服务
 */
public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        //获取定时管理类
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //更新间隔:8小时
        long anHour = 8 * 60 * 60 * 1000;
        //获取手机开启时间与更新时间相加得到要定时器触发的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        //每隔8个小时就重新开启一次service更新最新天气
        Intent intent2 = new Intent(this, AutoUpdateService.class);
        PendingIntent operation = PendingIntent.getService(this, 0, intent2, 0);
        //操作前先取消pendingIntent
        alarmManager.cancel(operation);
        //设置定时器定时条件
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, operation);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        //图片地址
        String bingPicString = "http://guolin.tech/api/bing_pic";
        //网络请求
        HttpUtils.sendOkHttpRequest(bingPicString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                //把最新的图片地址存储进bing_pic中
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences
                        (AutoUpdateService.this).edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();

            }
        });

    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        //获取本地私有空间的天气信息
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);
        if (weatherString != null) {
            //解析json天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            if (weather != null && "ok".equals(weather.status)) {
                //有缓存时,直接解析天气数据
                //获取县级id
                String weatherID = weather.basic.weatherID;
                //编写天气接口
                String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherID +
                        "&key=b96a3792eb9c48d08f5bfc9c42d91cbe";
                //请求网络接口
                HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //把最新数据储存到本地私有空间
                        String weatherData = response.body().string();
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("weather", weatherData);
                        edit.apply();
                    }
                });
            }
        }
    }
}
