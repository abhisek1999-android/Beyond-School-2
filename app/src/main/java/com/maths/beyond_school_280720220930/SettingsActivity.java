package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.SP.PrefConfig;

public class SettingsActivity extends AppCompatActivity {


    Switch logSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logSwitch=findViewById(R.id.logSwitch);


        if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("true"))
        {
            logSwitch.setChecked(true);
        }

        if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("false"))
        {
            logSwitch.setChecked(false);
        }
        logSwitch.setOnClickListener(v->{
            if (logSwitch.isChecked()){
                PrefConfig.writeIdInPref(getApplicationContext(),"true","IS_LOG_ENABLE");
            }
            if (!logSwitch.isChecked()){
                PrefConfig.writeIdInPref(getApplicationContext(),"false","IS_LOG_ENABLE");

            }
        });

    }
}