package com.example.expriement3;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class setting extends AppCompatActivity {
    private CheckBox mcheckbox;
    private RadioGroup mradiogroup;
    private RadioButton mrbtn1,mrbtn2;
    private Toolbar mtoolbar;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting);
        mcheckbox=findViewById(R.id.checkbox);
        mradiogroup=findViewById(R.id.radiogroup);
        mrbtn1=findViewById(R.id.rdn1);
        mrbtn2=findViewById(R.id.rdn2);

        pref=getSharedPreferences("data", MODE_PRIVATE);
        editor=pref.edit();
        mcheckbox.setChecked(pref.getBoolean("notify",false));

        if(mrbtn2.getText().toString().equals(pref.getString("weatherunit",""))) {
            mrbtn2.setChecked(true);
        } else{
            mrbtn1.setChecked(true);
        }

        mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("notify",b);
                editor.commit();
                mcheckbox.setChecked(b);
            }

        });

        mradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(radioButtonId);
                editor.putString("weatherunit",rb.getText().toString());
                editor.commit();
            }
        });

        mtoolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
