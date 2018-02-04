package com.coolweather.android;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * @author TanJJ
 * @time 2018/2/4 0:00
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android
 * @des 自定义的application
 */

public class MyApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化litepal库
        LitePal.initialize(this);
        //获取全局的context
        mContext = this;
    }
}
