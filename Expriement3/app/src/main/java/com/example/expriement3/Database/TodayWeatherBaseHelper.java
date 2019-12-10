package com.example.expriement3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expriement3.Database.TodayWeatherSchema.todayWeatherTable;

public class TodayWeatherBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="todayweatherBase.db";
    public TodayWeatherBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ todayWeatherTable.NAME+ "("+" _id integer primary key autoincrement, " +  todayWeatherTable.Cols.location + ","+  todayWeatherTable.Cols.locdate + ","
                +  todayWeatherTable.Cols.tmp + "," +  todayWeatherTable.Cols.aqi + "," +  todayWeatherTable.Cols.cond_txt + "," +  todayWeatherTable.Cols.wind_sc+ ","
                +  todayWeatherTable.Cols.wind_dir + "," +  todayWeatherTable.Cols.hum + "," +  todayWeatherTable.Cols.fl + "," +  todayWeatherTable.Cols.pres + ","
                +  todayWeatherTable.Cols.vis + "," +  todayWeatherTable.Cols.cond_code + "," +  todayWeatherTable.Cols.qlty+ "," +  todayWeatherTable.Cols.unit+ "," +  todayWeatherTable.Cols.lat+ "," +  todayWeatherTable.Cols.lon+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
