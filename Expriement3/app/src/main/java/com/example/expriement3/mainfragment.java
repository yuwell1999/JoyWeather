package com.example.expriement3;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expriement3.backService.PollService;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.os.SystemClock.sleep;

public class mainfragment extends Fragment {


    private Callbacks mCallbacks;
    TextView mTVtmp,mTVair,mTVkind,mTVvisit,mTVwind,mTVwet,mTVfl,mTVpres,mTVnowdate;
    ImageView mimgkind;
    RecyclerView recyclerView;
    private String location;
    WeatherListAdapter adapter;
    private String unit;
    private Map<String,String> infomap;
    public interface Callbacks{
        void onWeatherSelected(Weather weather);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }


    private class FetchItemsTask extends AsyncTask<Void,Void,List<Weather>> {
        @Override
        protected List<Weather> doInBackground(Void... params) {

            return new FlickFetchr(location,unit).fetchItems();

        }
        @Override
        protected void onPostExecute(List<Weather> weathers) {
            if(weathers.size()!=0) {
                WeatherLab weatherLab = WeatherLab.get(getActivity());
                weatherLab.storeWeathers(weathers, location,unit);
                updaterecycleview();
            }
        }
    }

    private class getMainTask extends AsyncTask<Void,Void, todayWeather> {
        @Override
        protected todayWeather doInBackground(Void... voids) {
            return new FlickFetchr(location,unit).getmaininfo();
        }

        @Override
        protected void onPostExecute(todayWeather todayWeather) {
            if(todayWeather.getLocation()!=null) {
                todayWeatherLab todayweatherLab = todayWeatherLab.get(getActivity());
                todayweatherLab.storetodayWeather(todayWeather, location,unit);
                updatemaininfo();
            }
        }
    }

    public int  getResource(String imageName){
        Context ctx=getActivity().getBaseContext();
        int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    private String GetMonthEN(String date) {
        String[] dates=date.substring(0,10).split("-");
        String strReturn = "";
        String strParaMonthn = "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec";
        String[] strSubMonth = strParaMonthn.split("_");
        strReturn = strSubMonth[Integer.valueOf(dates[1]) - 1];
        return strReturn+" "+dates[2];
    }

    @Override
    public void onStart() {
        super.onStart();
        location=method.getnowcity(getActivity());
        SharedPreferences pref=getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        String msg=pref.getString("weatherunit","");
        if(msg.equals("英制")){
            unit="i";
        }
        else
            unit="m";
        updaterecycleview();
        updatemaininfo();
        new FetchItemsTask().execute();
        new getMainTask().execute();
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void updatemaininfo(){
        todayWeatherLab todayweatherLab=todayWeatherLab.get(getActivity());
        todayWeather todayweather=todayweatherLab.gettodayWeather(location,unit);
        if(todayweather!=null){

                mTVnowdate.setText("Today "+GetMonthEN(todayweather.getLocdate()));

                mTVair.setText(todayweather.getQlty()+":"+todayweather.getAqi());
                mTVkind.setText(todayweather.getCond_txt());

                mTVwet.setText(todayweather.getHum()+"%\n"+"湿度");
                mTVfl.setText(todayweather.getFl()+"°\n"+"体感");
                mTVpres.setText(todayweather.getPres()+"mb\n"+"气压");
                mTVwind.setText(todayweather.getWind_sc()+"级\n"+todayweather.getWind_dir());
                String imgname="w"+todayweather.getCond_code();
                mimgkind.setImageResource(getResource(imgname));
            if(unit.equals("m")){
                mTVtmp.setText(todayweather.getTmp()+"℃");
                mTVvisit.setText("能见度"+":"+todayweather.getVis()+"km");
            }
            else if(unit.equals("i")){
                mTVtmp.setText(todayweather.getTmp()+"℉");
                mTVvisit.setText("能见度"+":"+todayweather.getVis()+"mile");
            }
        }
    }

    private void updaterecycleview(){
        WeatherLab weatherLab=WeatherLab.get(getActivity());;
        List <Weather>weathers=weatherLab.getWeathers(location,unit);

        if(adapter==null){
            adapter=new WeatherListAdapter(weathers);
            recyclerView.setAdapter(adapter);
        } else{
            adapter.setWeathers(weathers);
            recyclerView.setAdapter(adapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);

        mTVair=v.findViewById(R.id.airlevel);
        mTVkind=v.findViewById(R.id.kindinfo);
        mTVtmp=v.findViewById(R.id.maxTmp);
        mimgkind=v.findViewById(R.id.kindimg);
        mTVwet=v.findViewById(R.id.wetinfo);
        mTVwind=v.findViewById(R.id.windinfo);
        mTVvisit=v.findViewById(R.id.visit);
        mTVfl=v.findViewById(R.id.flinfo);
        mTVpres=v.findViewById(R.id.presinfo);
        mTVnowdate=v.findViewById(R.id.nowdate);

        recyclerView=v.findViewById(R.id.recyle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        return v;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView kind,tmp;
        ImageView dateimgkind;
        private Weather mweather;

        public ViewHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.weather_list,parent,false));
             kind= (TextView)itemView.findViewById(R.id.weatherkind);
             tmp = itemView.findViewById(R.id.weathertmp);
             dateimgkind=itemView.findViewById(R.id.dateimgkind);
             itemView.setOnClickListener(this);
        }

        public void onbind(Weather weather){
            mweather=weather;
            kind.setText(weather.getWeek() + "·" + weather.getMfkind());
            tmp.setText(weather.getMinTmp() + "°" + "/" + weather.getMaxTmp() + "°");
            dateimgkind.setImageResource(getResource("w" + weather.getImgkind()));
            if(unit.equals("m")) {
                tmp.setText(weather.getMinTmp() + "℃" + "/" + weather.getMaxTmp() + "℃");
            }
            else if (unit.equals("i")) {
                tmp.setText(weather.getMinTmp() + "℉" + "/" + weather.getMaxTmp() + "℉");
            }
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onWeatherSelected(mweather);
        }

    }

    private class WeatherListAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Weather> mWeatherList;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            Weather weather=mWeatherList.get(position);
            holder.onbind(weather);
        }
        public void setWeathers(List<Weather> weathers){
            mWeatherList=weathers;
        }

        @Override
        public int getItemCount() {
            return mWeatherList.size();
        }
        public WeatherListAdapter(List<Weather> weatherlist){
            mWeatherList=weatherlist;
        }
    }
}
