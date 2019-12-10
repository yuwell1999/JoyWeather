package com.example.expriement3.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.expriement3.Database.WeatherDbSchema.WeatherTable;
import com.example.expriement3.Weather;

import java.util.UUID;

public class WeatherCursorWrapper extends CursorWrapper {

    public WeatherCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Weather getWeather(){

        String uuidString=getString(getColumnIndex(WeatherTable.Cols.UUID));
        String mdate =getString(getColumnIndex(WeatherTable.Cols.date));
        String mfkind=getString(getColumnIndex(WeatherTable.Cols.mfkind));
        String mlkind=getString(getColumnIndex(WeatherTable.Cols.mlkind));
        String maxTmp=getString(getColumnIndex(WeatherTable.Cols.maxTmp));
        String minTmp=getString(getColumnIndex(WeatherTable.Cols.minTmp));
        String imgkind=getString(getColumnIndex(WeatherTable.Cols.imgkind));
        String week=getString(getColumnIndex(WeatherTable.Cols.week));
        String windsc=getString(getColumnIndex(WeatherTable.Cols.windsc));
        String pre=getString(getColumnIndex(WeatherTable.Cols.pre));
        String hum=getString(getColumnIndex(WeatherTable.Cols.hum));
        String location=getString(getColumnIndex(WeatherTable.Cols.location));
        String unit=getString(getColumnIndex(WeatherTable.Cols.unit));

        Weather weather=new Weather(UUID.fromString(uuidString));

        weather.setMdate(mdate);
        weather.setMfkind(mfkind);
        weather.setMlkind(mlkind);
        weather.setMaxTmp(maxTmp);
        weather.setMinTmp(minTmp);
        weather.setImgkind(imgkind);
        weather.setWeek(week);
        weather.setWindsc(windsc);
        weather.setPre(pre);
        weather.setHum(hum);
        weather.setLocation(location);
        weather.setUnit(unit);

        return weather;
    }
}
