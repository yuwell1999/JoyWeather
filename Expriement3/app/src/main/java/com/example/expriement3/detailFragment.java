package com.example.expriement3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class detailFragment extends Fragment {
    private static final String ARG_WEATHER_ID="weather_id";
    Weather mweather;
    TextView mweek,mdate,mtmp,mkind,mhum,mpre,mwind;
    ImageView mimgkind;
    public static detailFragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(ARG_WEATHER_ID,id);
        detailFragment fragment=new detailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_detail,container,false);
        UUID id=(UUID)getArguments().getSerializable(ARG_WEATHER_ID);

        mweather=WeatherLab.get(getActivity()).getWeather(id);
        mweek=v.findViewById(R.id.week);
        mdate=v.findViewById(R.id.datetime);
        mtmp=v.findViewById(R.id.tmp);
        mkind=v.findViewById(R.id.kind);
        mhum=v.findViewById(R.id.humidity);
        mpre=v.findViewById(R.id.pressure);
        mwind=v.findViewById(R.id.wind);
        mimgkind=v.findViewById(R.id.kindimg);
        mweek.setText(mweather.getWeek());
        mdate.setText(mweather.getMdate());
        mtmp.setText(mweather.getMaxTmp()+"/"+mweather.getMinTmp());
        mkind.setText(mweather.getMfkind());
        mimgkind.setImageResource(getResource("w"+mweather.getImgkind()));
        mhum.setText("Humidity："+mweather.getHum()+"%");
        mpre.setText("Pressure："+mweather.getPre()+"hpa");
        mwind.setText("Wind："+mweather.getWindsc()+"km/h");

       return v;

    }
    public int  getResource(String imageName){
        Context ctx=getActivity().getBaseContext();
        int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        return resId;
    }
}
