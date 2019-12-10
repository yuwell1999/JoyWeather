package com.example.expriement3.Database;

public class TodayWeatherSchema {
    public static final class todayWeatherTable{
        public static final String NAME="todayweathers";
        public static class Cols{
            public static final String location="location",locdate="cate",tmp="tmp",aqi="aqi",cond_txt="cond_txt",wind_sc="wind_sc",
                    wind_dir="wind_dir",hum="hum",fl="fl",pres="pres",vis="vis",cond_code="code",qlty="qlty",unit="unit",lat="lat",lon="lon";
        }
    }
}
