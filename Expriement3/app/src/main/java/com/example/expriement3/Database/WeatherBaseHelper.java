package com.example.expriement3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.expriement3.Database.WeatherDbSchema.WeatherTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="weatherBase.db";

    public WeatherBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ WeatherTable.NAME+ "("+" _id integer primary key autoincrement, " +  WeatherTable.Cols.UUID + "," +
                WeatherTable.Cols.date + "," +WeatherTable.Cols.mfkind + "," +WeatherTable.Cols.mlkind + "," +WeatherTable.Cols.maxTmp + "," +
                WeatherTable.Cols.minTmp + "," +WeatherTable.Cols.imgkind + "," +WeatherTable.Cols.week + "," +WeatherTable.Cols.windsc + "," +
                        WeatherTable.Cols.pre + "," + WeatherTable.Cols.hum+ "," + WeatherTable.Cols.location + "," + WeatherTable.Cols.unit+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
