package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.SP.PrefConfig;

public class SettingsActivity extends AppCompatActivity {


    Switch logSwitch;
    TextView titleText;
    RelativeLayout logView;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logSwitch=findViewById(R.id.logSwitch);
        titleText=findViewById(R.id.titleText);
        back = findViewById(R.id.imageView4);

        logView=findViewById(R.id.rLayout);
        logView.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),LogActivity.class));
        });
        titleText.setText("Settings");
        if (PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.log_check)).equals("true"))
        {
            logSwitch.setChecked(true);
        }

        if (PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.log_check)).equals("false"))
        {
            logSwitch.setChecked(false);
        }
        logSwitch.setOnClickListener(v->{
            if (logSwitch.isChecked()){
                PrefConfig.writeIdInPref(getApplicationContext(),"true",getResources().getString(R.string.log_check));
            }
            if (!logSwitch.isChecked()){
                PrefConfig.writeIdInPref(getApplicationContext(),"false",getResources().getString(R.string.log_check));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

    }
}