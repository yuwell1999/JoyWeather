package com.example.expriement3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class DetailActivity extends SingleFragmentActivity{
    private static final String EXTRA_WEATHER_ID="weather_id";
    private Toolbar mtoolbar;
    public static Intent newIntent(Context context,UUID id){
        Intent intent=new Intent(context,DetailActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID,id);
        return intent;
    }

    @Override
    protected Fragment createfragment() {
        UUID id=(UUID)getIntent()
                .getSerializableExtra((EXTRA_WEATHER_ID));
        return  detailFragment.newInstance(id);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mtoolbar.setSubtitleTextColor(Color.WHITE);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_share:
                        method.share((View)getWindow().getDecorView(),DetailActivity.this);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return true;
    }
}