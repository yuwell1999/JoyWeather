package com.example.expriement3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class cityActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar mtoolbar;
    private String unit,updatecity;
    private citysAdapter adapter;
    private List<String> cityList;

    private class getMainTask1 extends AsyncTask<Void,Void, todayWeather> {

        @Override
        protected todayWeather doInBackground(Void... voids) {
            return new FlickFetchr(updatecity,"m").getmaininfo();
        }

        @Override
        protected void onPostExecute(todayWeather todayWeather) {
            if(todayWeather.getLocation()!=null) {
                todayWeatherLab todayweatherLab = todayWeatherLab.get(cityActivity.this);
                todayweatherLab.storetodayWeather(todayWeather,updatecity,unit);
                if(unit.equals("m"))
                    updatecitylist();
            }
        }
    }

    private class getMainTask2 extends AsyncTask<Void,Void, todayWeather> {

        @Override
        protected todayWeather doInBackground(Void... voids) {
            return new FlickFetchr(updatecity,"i").getmaininfo();
        }

        @Override
        protected void onPostExecute(todayWeather todayWeather) {
            if(todayWeather.getLocation()!=null) {
                todayWeatherLab todayweatherLab = todayWeatherLab.get(cityActivity.this);
                todayweatherLab.storetodayWeather(todayWeather,updatecity,"i");
                if(unit.equals("i"))
                    updatecitylist();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);

        mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.city_add){
                    final EditText inputServer = new EditText(cityActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(cityActivity.this);

                    builder.setTitle("请输入城市名")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(inputServer)
                            .setNegativeButton("取消", null);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String newCity = inputServer.getText().toString();
                            if(storecityList(newCity)){
                                updatecity=newCity;
                                Log.e("新城市",updatecity);
                                cityList.add(updatecity);
                                new getMainTask1().execute();
                                new getMainTask2().execute();
                            }
                        }
                    });

                    builder.show();
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recyle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        cityList=new ArrayList<>();

        getcityList();

        updatecity=cityList.get(0);
        unit=method.getunit(cityActivity.this);

        if(todayWeatherLab.get(cityActivity.this).gettodayWeather(updatecity,unit)==null)
            if(unit.equals("m"))
                new getMainTask1().execute();
            else new getMainTask2().execute();

        recyclerView.setNestedScrollingEnabled(false);
        updatecitylist();
    }

    private void getcityList(){
        String citys=method.getcitys(cityActivity.this);
        Log.e("城市有",citys);
        if(citys.equals(""))
            return;
        String[] city=citys.split(",");

        for(int i=0;i<city.length;i++){
            cityList.add(city[i]);
        }
    }

    private boolean storecityList(String newcity){
        String citys=method.getcitys(cityActivity.this);
        String[] city=citys.split(",");
        for(int i=0;i<city.length;i++){
            if(city[i].equals(newcity)){
                return false;
            }
        }
        method.addcitys(cityActivity.this,newcity);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu,menu);
        return true;
    }

    private void updatecitylist(){
        if(adapter==null){
            adapter=new cityActivity.citysAdapter(cityList);
            recyclerView.setAdapter(adapter);
        } else{
            adapter.setcitys(cityList);
            recyclerView.setAdapter(adapter);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView mcity,mtmp,mwindsc,maqi;
        ImageView mimg;
        String city;

        public ViewHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.city_list,parent,false));

            mimg=itemView.findViewById(R.id.city_kindimg);
            mcity=itemView.findViewById(R.id.city);
            mtmp=itemView.findViewById(R.id.tmp);
            mwindsc=itemView.findViewById(R.id.info);
            maqi=itemView.findViewById(R.id.aqi);

            itemView.setOnClickListener(this);
        }

        //在城市列表显示较为详细的信息
        public void onbind(String city){
            this.city=city;
            unit=method.getunit(cityActivity.this);
            todayWeatherLab todayweatherLab=todayWeatherLab.get(cityActivity.this);
            todayWeather todayweather=todayweatherLab.gettodayWeather(city,unit);
            if(todayweather!=null) {
                if (unit.equals("m"))
                    mtmp.setText(todayweather.getTmp() + "℃");
                else
                    mtmp.setText(todayweather.getTmp() + "℉");
                mcity.setText(todayweather.getLocation());

                mimg.setImageResource(method.getResource("w" + todayweather.getCond_code(), cityActivity.this));
                mwindsc.setText(todayweather.getWind_dir() + ":" + todayweather.getWind_sc() + "级");
                maqi.setText(todayweather.getQlty() + ":" + todayweather.getAqi());
            }
        }

        @Override
        public void onClick(View view) {
            method.storenowcity(cityActivity.this,city);
            finish();
        }
    }

    private class citysAdapter extends RecyclerView.Adapter<cityActivity.ViewHolder>{
        private List<String>  mcitys;

        @NonNull
        @Override
        public cityActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(cityActivity.this);
            return new ViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final cityActivity.ViewHolder holder, int position) {
            String city=mcitys.get(position);
            holder.onbind(city);
        }

        @Override
        public int getItemCount() {
            return mcitys.size();
        }
        public citysAdapter(List<String> citys){
            mcitys=citys;
        }
        public void setcitys(List<String> citys){
            mcitys=citys;
        }
    }
}
