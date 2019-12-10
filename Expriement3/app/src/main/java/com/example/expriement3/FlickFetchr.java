package com.example.expriement3;

import android.net.Uri;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlickFetchr {
    static String part="https://free-api.heweather.net/s6/";
    static String key="bdb854428dad4b39a09d0cf7653bdf43";
    private String LatLon,Unit;
    public FlickFetchr(String L,String unit){
        Unit=unit;
        LatLon=L;
    }
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoInput(true);
        connection.connect();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public todayWeather getmaininfo(){
        todayWeather todayWeather=new todayWeather();
        String jsonString,url;
        JSONObject jsonBody,jsonIndex,jsonnow,jsonupdate,jsonbasic;
        JSONArray jsonArray;
        try {

            url= Uri.parse(part+"weather/now?").buildUpon().appendQueryParameter("location",LatLon).appendQueryParameter("unit",Unit).appendQueryParameter("key",key).build().toString();
            jsonString = getUrlString(url);
            jsonBody=new JSONObject((jsonString));
            jsonArray=jsonBody.getJSONArray("HeWeather6");
            jsonIndex=jsonArray.getJSONObject(0);
            jsonbasic=jsonIndex.getJSONObject("basic");

            todayWeather.setLat(jsonbasic.getString("lat"));
            todayWeather.setLon(jsonbasic.getString("lon"));


            jsonupdate=jsonIndex.getJSONObject("update");

            todayWeather.setLocdate(jsonupdate.getString("loc"));
            jsonnow=jsonIndex.getJSONObject("now");
            todayWeather.setTmp(jsonnow.getString("tmp"));

            todayWeather.setCond_code(jsonnow.getString("cond_code"));
            todayWeather.setCond_txt(jsonnow.getString("cond_txt"));
            todayWeather.setWind_dir(jsonnow.getString("wind_dir"));
            todayWeather.setWind_sc(jsonnow.getString("wind_sc"));
            todayWeather.setHum(jsonnow.getString("hum"));
            todayWeather.setVis(jsonnow.getString("vis"));
            todayWeather.setFl(jsonnow.getString("fl"));
            todayWeather.setPres(jsonnow.getString("pres"));

            url= Uri.parse(part+"air/now?").buildUpon().appendQueryParameter("location",LatLon).appendQueryParameter("unit",Unit).appendQueryParameter("key",key).build().toString();

            jsonString = getUrlString(url);
            jsonBody=new JSONObject((jsonString));
            jsonArray=jsonBody.getJSONArray("HeWeather6");
            jsonIndex=jsonArray.getJSONObject(0);
            jsonnow=jsonIndex.getJSONObject("air_now_city");
            todayWeather.setQlty(jsonnow.getString("qlty"));
            todayWeather.setAqi(jsonnow.getString("aqi"));
            todayWeather.setLocation(LatLon);
            todayWeather.setUnit(Unit);
        } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return todayWeather;

    }

    public String getLatLon() {
        String jsonString,result="";
        JSONObject jsonBody,jsonIndex,jsonbasic;
        JSONArray jsonArray;

        try {
            String url= Uri.parse(part+"weather/now?").buildUpon().appendQueryParameter("location",LatLon).appendQueryParameter("unit",Unit).appendQueryParameter("key",key).build().toString();

            jsonString = getUrlString(url);
            jsonBody=new JSONObject((jsonString));
            jsonArray=jsonBody.getJSONArray("HeWeather6");
            jsonIndex=jsonArray.getJSONObject(0);
            jsonbasic=jsonIndex.getJSONObject("basic");
            result=jsonbasic.getString("lat")+","+jsonbasic.getString("lon");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Weather> fetchItems() {
        List<Weather> weathers=new ArrayList<>();
        String jsonString= null;
        try {
            String url= Uri.parse(part+"weather/forecast?").buildUpon().appendQueryParameter("location",LatLon).appendQueryParameter("unit",Unit).appendQueryParameter("key",key).build().toString();
            jsonString = getUrlString(url);

            JSONObject jsonBody=new JSONObject((jsonString));
            paseItems(weathers,jsonBody);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  weathers;
    }

    private void paseItems(List<Weather> weathers,JSONObject jsonBody)throws IOException, JSONException{
        JSONArray jsonArray=jsonBody.getJSONArray("HeWeather6");
        JSONObject jsonIndex=jsonArray.getJSONObject(0);
        JSONArray weatherArray =jsonIndex.getJSONArray("daily_forecast");

        for(int i=0;i<weatherArray.length();i++){
            JSONObject weatherObject;
            weatherObject = weatherArray.getJSONObject(i);
            Weather weather=new Weather();
            weather.setMdate(weatherObject.getString("date"));
            if(i==0){
                weather.setWeek("今天");
            } else if(i==1)
                weather.setWeek("明天");
            else{
                weather.setWeek(getWeek(weatherObject.getString("date")));
            }
            weather.setLocation(LatLon);
            weather.setUnit(Unit);
            weather.setMfkind(weatherObject.getString("cond_txt_d"));
            weather.setMlkind(weatherObject.getString("cond_txt_n"));
            weather.setMaxTmp(weatherObject.getString( "tmp_max"));
            weather.setMinTmp(weatherObject.getString( "tmp_min"));
            weather.setImgkind(weatherObject.getString("cond_code_d"));
            weather.setWindsc(weatherObject.getString("wind_sc"));
            weather.setHum(weatherObject.getString("hum"));
            weather.setPre(weatherObject.getString("pres"));

            weathers.add(weather);
        }
    }
    private String getWeek(String pTime) {
        if(pTime.equals("今天")||pTime.equals("明天")){
            return pTime;
        }
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";  }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";  }
        return Week;
    }

}
