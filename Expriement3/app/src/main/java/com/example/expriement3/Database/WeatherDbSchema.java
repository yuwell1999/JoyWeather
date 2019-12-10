package com.example.expriement3.Database;

public class WeatherDbSchema {
    public static final class WeatherTable{
        public static final String NAME="weathers";
        public static class Cols{
            public static final String UUID="uuid",date="date",mfkind="mfkind",
                    mlkind="mlkind",maxTmp="maxTmp",minTmp="minTmp",imgkind="imgkind",
                    week="week",windsc="windsc",pre="pre",hum="hum",location="location",unit="unit";
        }
    }
}
