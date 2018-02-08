package com.coolweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.utils.HttpUtils;
import com.coolweather.android.utils.LogUtils;
import com.coolweather.android.utils.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.litepal.crud.DataSupport.findAll;

/**
 * @author TanJJ
 * @time 2018/2/3 20:11
 * @ProjectName CoolWeather
 * @PackageName com.coolweather.android
 * @des 选择区域Fragment
 */

public class ChooseAreaFragment extends Fragment {

    private static final String AREA_URL = "http://guolin.tech/api/china";
    /*
        省级type
     */
    private int province_type = 1;
    /*
        市级type
     */
    private int city_type = 2;
    /*
        县级type
     */
    private int county_type = 3;

    //数据集合
    private List<String> mDateList = new ArrayList<>();

    /*
        选中级别
     */
    private int currentLevel;
    /*
        省级数据
     */
    private List<Province> mProvinceList = new ArrayList<>();
    /*
        市级数据
     */
    private List<City> mCityList = new ArrayList<>();
    /*
        县级数据
     */
    private List<County> mCountyList = new ArrayList<>();
    /*
        选中省级
     */
    private Province mSelectProvince;
    /*
        选中市级
     */
    private City mSelectCity;
    /*
        选中县级
     */
    private County mSelectCounty;

    private TextView mTitleText;
    private Button mBack_button;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mBack_button = (Button) view.findViewById(R.id.back_button);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout
                .simple_list_item_1, mDateList);
        mListView.setAdapter(mArrayAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置ListView的子item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == province_type) {
                    //如果等于省级的话,说明是点击了省级的某个省,应该显示该省的市级
                    mSelectProvince = mProvinceList.get(position);
                    //获取对应的市级数据
                    queryCity();
                } else if (currentLevel == city_type) {
                    //如果等于市级的话,说明是点击了市级的某个市,应该显示该市的县级
                    mSelectCity = mCityList.get(position);
                    //获取对应的县级数据
                    queryCounty();
                } else if (currentLevel == county_type) {
                    //如果等于县级的话,说明是点击了市级的某个县,应该显示该县的天气信息
                    String weatherId = mCountyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        //设置点击返回键后的数据显示
        mBack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == city_type) {
                    //显示省级数据
                    queryProvince();
                } else if (currentLevel == county_type) {
                    //显示市级数据
                    queryCity();
                }
            }
        });
        //显示默认数据
        queryProvince();
    }


    /**
     * 查询县级数据
     */
    private void queryCounty() {
        mTitleText.setText(mSelectCity.getCityName());
        mBack_button.setVisibility(View.VISIBLE);
        //读取县级数据
        mCountyList = DataSupport.where("cityId=?", String.valueOf(mSelectCity.getCityCode())).find(County.class);
        if (mCountyList.size() > 0) {
            mDateList.clear();
            for (County county : mCountyList) {
                mDateList.add(county.getCountryName());
            }
            //更新当前显示的集合
            currentLevel = county_type;
            //设置列表选中第一个
            mListView.setSelection(0);
            //更新Adapter
            mArrayAdapter.notifyDataSetChanged();
        } else {
            String address = AREA_URL + "/" + mSelectProvince.getProvinceCode() + "/" + mSelectCity.getCityCode();
            queryFromServer(address,
                    "county");
        }

    }

    /**
     * 查询市级数据
     */
    private void queryCity() {
        //设置标题显示的省级名称
        mTitleText.setText(mSelectProvince.getProvinceName());
        mBack_button.setVisibility(View.VISIBLE);
        //读取市级数据库
        mCityList = DataSupport.where("provinceId=?", String.valueOf(mSelectProvince.getProvinceCode())).find
                (City.class);
        if (mCityList.size() > 0) {
            mDateList.clear();
            //有就从数据库中读取
            for (City city : mCityList) {
                mDateList.add(city.getCityName());
            }
            //更新当前显示的集合
            currentLevel = city_type;
            //设置列表选中第一个
            mListView.setSelection(0);
            //更新Adapter
            mArrayAdapter.notifyDataSetChanged();
        } else {
            //没有就请求网络获取数据
            String address = AREA_URL + "/" + mSelectProvince.getProvinceCode();
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询省级数据
     */
    private void queryProvince() {
        mTitleText.setText("中国");
        mBack_button.setVisibility(View.GONE);
        //读取省级数据库
        mProvinceList = findAll(Province.class);
        //本地数据库有就从本地中取出
        if (mProvinceList.size() > 0) {
            mDateList.clear();
            for (Province province : mProvinceList) {
                mDateList.add(province.getProvinceName());
            }
            //从省级集合中取出数据放进用于显示的mDateList中
            currentLevel = province_type;
            //设置列表选中第一个
            mListView.setSelection(0);
            //更新Adapter
            mArrayAdapter.notifyDataSetChanged();
        } else {
            //没有就请求网络获取数据
            queryFromServer(AREA_URL, "province");

        }
    }

    private void queryFromServer(String areaUrl, final String type) {
        showProgress();
        HttpUtils.sendOkHttpRequest(areaUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当前是在子线程中,必须回到主线程做错误提示
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgress();
                        Toast.makeText(getContext(), "加载失败..", Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.e(this, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseDate = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseDate);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseDate, mSelectProvince.getProvinceCode());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseDate, mSelectCity.getCityCode());
                }
                if (result) {
                    //当前是在子线程中,要更新数据必须回到主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgress();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }
        });

    }

    private void closeProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载数据中.....");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

}
