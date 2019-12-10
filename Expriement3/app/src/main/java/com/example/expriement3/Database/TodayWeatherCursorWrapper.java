package com.example.expriement3.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.expriement3.Database.TodayWeatherSchema.todayWeatherTable;
import com.example.expriement3.todayWeather;

public class TodayWeatherCursorWrapper extends CursorWrapper {
    public TodayWeatherCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public todayWeather gettodayWeather(){

        String location=getString(getColumnIndex(todayWeatherTable.Cols.location));
        String date=getString(getColumnIndex(todayWeatherTable.Cols.locdate));
        String tmp=getString(getColumnIndex(todayWeatherTable.Cols.tmp));
        String aqi=getString(getColumnIndex(todayWeatherTable.Cols.aqi));
        String con_txt=getString(getColumnIndex(todayWeatherTable.Cols.cond_txt));
        String wind_sc=getString(getColumnIndex(todayWeatherTable.Cols.wind_sc));
        String wind_dir=getString(getColumnIndex(todayWeatherTable.Cols.wind_dir));
        String hum=getString(getColumnIndex(todayWeatherTable.Cols.hum));
        String fl=getString(getColumnIndex(todayWeatherTable.Cols.fl));
        String pres=getString(getColumnIndex(todayWeatherTable.Cols.pres));
        String vis=getString(getColumnIndex(todayWeatherTable.Cols.vis));
        String con_code=getString(getColumnIndex(todayWeatherTable.Cols.cond_code));
        String qlty=getString(getColumnIndex(todayWeatherTable.Cols.qlty));
        String unit=getString(getColumnIndex(todayWeatherTable.Cols.unit));
        String lat=getString(getColumnIndex(todayWeatherTable.Cols.lat));
        String lon=getString(getColumnIndex(todayWeatherTable.Cols.lon));

        todayWeather todayweather=new todayWeather();

        todayweather.setLocation(location);
        todayweather.setLocdate(date);
        todayweather.setTmp(tmp);
        todayweather.setAqi(aqi);
        todayweather.setCond_txt(con_txt);
        todayweather.setWind_sc(wind_sc);
        todayweather.setWind_dir(wind_dir);
        todayweather.setHum(hum);
        todayweather.setFl(fl);
        todayweather.setPres(pres);
        todayweather.setVis(vis);
        todayweather.setCond_code(con_code);
        todayweather.setQlty(qlty);
        todayweather.setUnit(unit);
        todayweather.setLat(lat);
        todayweather.setLon(lon);

        return todayweather;
    }

}
