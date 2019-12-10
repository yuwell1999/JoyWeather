package com.example.expriement3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.expriement3.Database.TodayWeatherBaseHelper;
import com.example.expriement3.Database.TodayWeatherCursorWrapper;

import com.example.expriement3.Database.TodayWeatherSchema.todayWeatherTable;

public class todayWeatherLab {
    private static todayWeatherLab stodayWeatherLab;

    public static todayWeatherLab get(Context context){
        if(stodayWeatherLab==null)
            stodayWeatherLab=new todayWeatherLab(context);
        return stodayWeatherLab;
    }
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private todayWeatherLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TodayWeatherBaseHelper(mContext).getWritableDatabase();

    }
    public todayWeather gettodayWeather(String location,String unit){
        TodayWeatherCursorWrapper cursor=queryWeathes(todayWeatherTable.Cols.location+"=?"+" and "+todayWeatherTable.Cols.unit+"=?",new String[]{location,unit});
        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.gettodayWeather();
        }
        finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(todayWeather todayweather){
        ContentValues values=new ContentValues();

        values.put(todayWeatherTable.Cols.location,todayweather.getLocation());
        values.put(todayWeatherTable.Cols.locdate,todayweather.getLocdate());
        values.put(todayWeatherTable.Cols.tmp,todayweather.getTmp());
        values.put(todayWeatherTable.Cols.aqi,todayweather.getAqi());
        values.put(todayWeatherTable.Cols.cond_txt,todayweather.getCond_txt());
        values.put(todayWeatherTable.Cols.wind_sc,todayweather.getWind_sc());
        values.put(todayWeatherTable.Cols.wind_dir,todayweather.getWind_dir());
        values.put(todayWeatherTable.Cols.hum,todayweather.getHum());
        values.put(todayWeatherTable.Cols.fl,todayweather.getFl());
        values.put(todayWeatherTable.Cols.pres,todayweather.getPres());
        values.put(todayWeatherTable.Cols.vis,todayweather.getVis());
        values.put(todayWeatherTable.Cols.cond_code,todayweather.getCond_code());
        values.put(todayWeatherTable.Cols.qlty,todayweather.getQlty());
        values.put(todayWeatherTable.Cols.unit,todayweather.getUnit());
        values.put(todayWeatherTable.Cols.lat,todayweather.getLat());
        values.put(todayWeatherTable.Cols.lon,todayweather.getLon());

        return values;
    }
    public void storetodayWeather(todayWeather todayWeather,String location,String unit){
        TodayWeatherCursorWrapper cursor=queryWeathes(todayWeatherTable.Cols.location+"=?"+" and "+todayWeatherTable.Cols.unit+"=?",new String[]{location,unit});
        try {
            if (cursor.getCount() != 0)
                mDatabase.delete(todayWeatherTable.NAME, todayWeatherTable.Cols.location+"=?"+" and "+todayWeatherTable.Cols.unit+"=?",new String[]{location,unit});
            addtodayWeather(todayWeather);
        }
        finally {
            cursor.close();
        }

    }
    public void addtodayWeather(todayWeather todayWeather){
            ContentValues values = getContentValues(todayWeather);
            mDatabase.insert(todayWeatherTable.NAME,null,values);
    }

    private TodayWeatherCursorWrapper queryWeathes(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(todayWeatherTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new TodayWeatherCursorWrapper(cursor);
    }
}
