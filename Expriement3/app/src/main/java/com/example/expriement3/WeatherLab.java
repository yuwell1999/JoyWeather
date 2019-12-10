package com.example.expriement3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.expriement3.Database.WeatherBaseHelper;

import com.example.expriement3.Database.WeatherCursorWrapper;
import com.example.expriement3.Database.WeatherDbSchema;
import com.example.expriement3.Database.WeatherDbSchema.WeatherTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WeatherLab {
    private static WeatherLab sWeatherLab;
    public static WeatherLab get(Context context){
        if(sWeatherLab==null)
            sWeatherLab=new WeatherLab(context);
        return sWeatherLab;
    }
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private WeatherLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new WeatherBaseHelper(mContext).getWritableDatabase();
    }

    public List<Weather> getWeathers(String location,String unit){
        List<Weather> weathers=new ArrayList<>();
        WeatherCursorWrapper cursor=queryWeathes(WeatherTable.Cols.location+"=?"+" and "+WeatherTable.Cols.unit+"=?",new String[]{location,unit});
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Log.e("3",cursor.getWeather().getMdate());
                weathers.add(cursor.getWeather());
                cursor.moveToNext();
            }
        } finally{
            cursor.close();
        }
        return weathers;
    }

    public Weather getWeather(UUID id){
        WeatherCursorWrapper cursor=queryWeathes(WeatherTable.Cols.UUID+"=?",new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getWeather();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Weather weather){
        ContentValues values=new ContentValues();

        values.put(WeatherTable.Cols.UUID,weather.getmId().toString());
        values.put(WeatherTable.Cols.date,weather.getMdate());
        values.put(WeatherTable.Cols.mfkind,weather.getMfkind());
        values.put(WeatherTable.Cols.mlkind,weather.getMlkind());
        values.put(WeatherTable.Cols.maxTmp,weather.getMaxTmp());
        values.put(WeatherTable.Cols.minTmp,weather.getMinTmp());
        values.put(WeatherTable.Cols.imgkind,weather.getImgkind());
        values.put(WeatherTable.Cols.week,weather.getWeek());
        values.put(WeatherTable.Cols.date,weather.getMdate());
        values.put(WeatherTable.Cols.windsc,weather.getWindsc());
        values.put(WeatherTable.Cols.hum,weather.getHum());
        values.put(WeatherTable.Cols.location,weather.getLocation());
        values.put(WeatherTable.Cols.pre,weather.getPre());
        values.put(WeatherTable.Cols.unit,weather.getUnit());

        return values;
    }
    public void storeWeathers(List<Weather> weathers,String location,String unit){
        WeatherCursorWrapper cursor=queryWeathes(WeatherTable.Cols.location+"=?"+" and "+WeatherTable.Cols.unit+"=?",new String[]{location,unit});
        try {
            if (cursor.getCount() != 0)
                mDatabase.delete(WeatherTable.NAME,WeatherTable.Cols.location+"=?"+" and "+WeatherTable.Cols.unit+"=?",new String[]{location,unit});
            addWeathers(weathers);
        }finally {
            cursor.close();
        }
    }

    public void addWeathers(List<Weather> weathers){
        for(Weather weather:weathers){
            ContentValues values = getContentValues(weather);
            mDatabase.insert(WeatherTable.NAME,null,values);
        }
    }

    private WeatherCursorWrapper queryWeathes(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(WeatherTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new WeatherCursorWrapper(cursor);
    }
}
