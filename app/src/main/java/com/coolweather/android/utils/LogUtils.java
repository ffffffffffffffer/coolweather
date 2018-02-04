package com.coolweather.android.utils;

import android.util.Log;

/**
 * @author TanJJ
 * @time 2018/2/3 23:55
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android.utils
 * @des 日志工具类
 */

public class LogUtils {
    private static int verbose = 0;
    private static int debug = 1;
    private static int info = 2;
    private static int warn = 3;
    private static int error = 4;
    private static int nothing = 5;
    private static int level = verbose;

    public static void v(Object o, String content) {
        if (level <= verbose) {
            Log.v(o.getClass().getSimpleName(), content);
        }
    }

    public static void d(Object o, String content) {
        if (level <= debug) {
            Log.d(o.getClass().getSimpleName(), content);
        }
    }

    public static void i(Object o, String content) {
        if (level <= info) {
            Log.i(o.getClass().getSimpleName(), content);
        }
    }

    public static void w(Object o, String content) {
        if (level <= warn) {
            Log.w(o.getClass().getSimpleName(), content);
        }
    }

    public static void e(Object o, String content) {
        if (level <= error) {
            Log.e(o.getClass().getSimpleName(), content);
        }
    }
}
