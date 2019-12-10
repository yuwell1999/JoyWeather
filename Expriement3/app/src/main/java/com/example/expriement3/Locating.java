package com.example.expriement3;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



public class Locating {
    private LocationClient mlocationClient;
    private MyLocationListener myListener = new MyLocationListener();
    private Handler handler;
    private String addr,country,province,city,district,street;
    public void initlocationset(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式，默认高精度
        option.setCoorType("bd09ll");//可选，设置返回经纬度坐标类型，百度经纬度坐标；
        option.setScanSpan(1000);//可选，设置发起定位请求的间隔
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);

        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        mlocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){

            addr = location.getAddrStr();    //获取详细地址信息
            country = location.getCountry();    //获取国家
            province = location.getProvince();    //获取省份
            city = location.getCity();    //获取城市
            district = location.getDistrict();    //获取区县
            street = location.getStreet();    //获取街道信息
            if(location.getLocType()==161) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
            Log.e("34",street);
        }
    }

    public Locating(Context C,Handler H){
        mlocationClient=new LocationClient(C);
        handler=H;
        mlocationClient.registerLocationListener(myListener);

        initlocationset();
    }

    public String getCity() {
        return city;
    }

    public void startLocate(){
        mlocationClient.start();
    }
    public void stopLocate(){
        mlocationClient.stop();
    }
    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }
}
