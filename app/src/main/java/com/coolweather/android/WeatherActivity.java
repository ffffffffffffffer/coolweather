package com.coolweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.utils.HttpUtils;
import com.coolweather.android.utils.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView mWeatherLayout;
    private TextView mTitleCity;
    private TextView mTitleUpdateTime;
    private TextView mDegreeText;
    private TextView mWeatherInfoText;
    private LinearLayout mForecastLayout;
    private TextView mAqiText;
    private TextView mPm25Text;
    private TextView mComfortText;
    private TextView mCarWashText;
    private TextView mSportText;
    private ImageView mBingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);

            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }

        //初始化控件
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        mDegreeText = (TextView) findViewById(R.id.degree_text);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mAqiText = (TextView) findViewById(R.id.aqi_text);
        mPm25Text = (TextView) findViewById(R.id.pm25_text);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mCarWashText = (TextView) findViewById(R.id.car_wash_text);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mBingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

        //获取weather数据
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = sharedPreferences.getString("bing_pic", null);
        if (bing_pic != null) {
            Glide.with(this).load(bing_pic).into(mBingPicImg);
        } else {
            loadBingPic();
        }

        String weatherData = sharedPreferences.getString("weather", null);
        if (weatherData != null) {
            //解析json数据
            Weather weather = Utility.handleWeatherResponse(weatherData);
            //显示weather数据到各个控件中
            showWeatherInfo(weather);
        } else {
            //没有数据就去获取数据
            String weather_id = getIntent().getStringExtra("weather_id");
            mWeatherLayout.setVisibility(View.INVISIBLE);
            //根据上一个页面传入的weatherId去请求天气接口
            requestWeather(weather_id);
        }

    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicUrl = response.body().string();
                if (bingPicUrl != null) {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                            .edit();
                    edit.putString("bing_pic", bingPicUrl);
                    edit.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(bingPicUrl).into(mBingPicImg);
                        }
                    });
                }
            }
        });
    }

    private void requestWeather(final String weather_id) {
        //拼接Url
        //http://guolin.tech/api/weather?cityid=CN101281701&key=b96a3792eb9c48d08f5bfc9c42d91cbe
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weather_id +
                "&key=b96a3792eb9c48d08f5bfc9c42d91cbe";
        //请求网络
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取响应数据
                final String weatherData = response.body().string();
                //解析json数据
                final Weather weather = Utility.handleWeatherResponse(weatherData);
                //在主线程中更新各个控件数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //判断解析出来的数据中的status是否为ok
                        if (weather != null && "ok".equals(weather.status)) {
                            //保存数据
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            edit.putString("weather", weatherData);
                            edit.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //在主线程显示toast
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        //取出weather对象中的数据
        //设置各个控件对应的数据
        String cityName = weather.basic.cityName;
        //格式:2018-02-04 22:45 ,只要时间
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String temperature = weather.now.temperature + "℃";
        String moreInfo = weather.now.more.info;
        List<Forecast> forecastList = weather.forecastList;

        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegreeText.setText(temperature);
        mWeatherInfoText.setText(moreInfo);

        //由于未来天气存在不确定性,所以要根据数据动态加载
        mForecastLayout.removeAllViews();
        for (Forecast forecast : forecastList) {
            //加载天气预报的子item布局
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dataText = (TextView) view.findViewById(R.id.data_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperture.max);
            minText.setText(forecast.temperture.min);
            mForecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);
        }

        String carWashInfo = "洗车指数: " + weather.suggestion.carWash.info;
        String sportInfo = "运动建议: " + weather.suggestion.sport.info;
        String comfortInfo = "舒适度: " + weather.suggestion.comfort.info;
        mComfortText.setText(comfortInfo);
        mSportText.setText(sportInfo);
        mCarWashText.setText(carWashInfo);

        mWeatherLayout.setVisibility(View.VISIBLE);
    }
}
