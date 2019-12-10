package com.example.expriement3;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.expriement3.backService.PollService;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends SingleFragmentActivity implements mainfragment.Callbacks, View.OnClickListener {

    private Toolbar mtoolbar;
    private PopupWindow mPopupWindow;
    private UUID nowid;
    @Override
    protected Fragment createfragment() {
        return new mainfragment();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onWeatherSelected(Weather weather) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=DetailActivity.newIntent(this,weather.getmId());
            startActivity(intent);
        } else{
            Fragment newDetail=detailFragment.newInstance(weather.getmId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }

    private TextView mloc;
    private String city,unit;

    private void refreshLoc(){
        city=method.getnowcity(MainActivity.this);
        mloc.setText(city);
        SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
        String u=pref.getString("weatherunit","");
        if(u.equals("英制")){
            unit="i";
        }
        else
            unit="m";
        if(findViewById(R.id.detail_fragment_container)!=null){
            List<Weather> weathers=WeatherLab.get(MainActivity.this).getWeathers(city,unit);
            if(weathers.size()!=0) {
                UUID id = weathers.get(0).getmId();
                Fragment newDetail = detailFragment.newInstance(id);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            }
        }

    }

    private static final String[] ALL_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET};
    private static final int ALL_PERMISSIONS_CODE = 1;
    private void requestPermission() {
        // 当API大于 23 时，才动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(MainActivity.this,ALL_PERMISSIONS,ALL_PERMISSIONS_CODE);
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

        @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLoc();
        SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
        boolean start=pref.getBoolean("notify",false);
        if(start){

            boolean shouldStartAlarm = !PollService.isServiceAlarmOn(MainActivity.this);
            if( shouldStartAlarm ){
                PollService.setServiceAlarm(MainActivity.this, true);
            }
        } else{
            PollService.setServiceAlarm(MainActivity.this, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermission();
        if(method.getnowcity(MainActivity.this).equals("")){
            method.storenowcity(MainActivity.this,"长沙");
            method.addcitys(MainActivity.this,"长沙");
        }

        mloc=findViewById(R.id.toolbartiltle);
        mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_city_add);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_setting:
                        popUpMyOverflow();
                        break;
                    case R.id.action_share:
                        method.share((View)getWindow()
                                .getDecorView(),MainActivity.this);
                        break;

                }
                return false;
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,cityActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top +10;
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(mtoolbar, Gravity.RIGHT | Gravity.TOP, 0, yOffset);
            //设置popupWindow上边控件item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
        } else {
            mPopupWindow.showAtLocation(mtoolbar, Gravity.RIGHT | Gravity.TOP, 0, yOffset);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item1:
                try {
                    todayWeather todayweather=todayWeatherLab.get(MainActivity.this).gettodayWeather(city,unit);
                    Log.e("343252",todayweather.getLat());
                    Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication=appname&poiname="+city+"&lat=" +
                            todayweather.getLat()+ "&lon=" +todayweather.getLon()+ "&dev=1&style=2");
                    startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_item2:
                Intent i=new Intent(MainActivity.this,setting.class);
                startActivity(i);
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}