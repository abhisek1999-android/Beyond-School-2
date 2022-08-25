package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.SP.PrefConfig;

public class SettingsActivity extends AppCompatActivity{


    Switch logSwitch;
    TextView titleText;
    RelativeLayout logView;
    TextView txt2;
    ImageView back;
    Spinner spinner;
    String[] countries = {"India", "United States",
            "United Kingdom", "Canada"};
    String[] c_code = {"IN", "US", "GB", "CA"};
    String selected;
    int selection=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logSwitch = findViewById(R.id.logSwitch);
        titleText = findViewById(R.id.titleText);
        back = findViewById(R.id.imageViewBack);
        spinner = findViewById(R.id.spinner);
        txt2 = findViewById(R.id.txt2);

        logView = findViewById(R.id.rLayout);
        logView.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LogActivity.class));
        });
        titleText.setText("Settings");
        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.log_check)).equals("true")) {
            logSwitch.setChecked(true);
        }

        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.log_check)).equals("false")) {
            logSwitch.setChecked(false);
        }
        logSwitch.setOnClickListener(v -> {
            if (logSwitch.isChecked()) {
                PrefConfig.writeIdInPref(getApplicationContext(), "true", getResources().getString(R.string.log_check));
            }
            if (!logSwitch.isChecked()) {
                PrefConfig.writeIdInPref(getApplicationContext(), "false", getResources().getString(R.string.log_check));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String key = PrefConfig.readIdInPref(getApplicationContext(), "Country_code");
        String country_name = PrefConfig.readIdInPref(getApplicationContext(), "Country");
        selection=PrefConfig.readIntInPref(getApplicationContext(),"set_select");
        txt2.setText(country_name);
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();


        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                countries);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spinner.setAdapter(ad);
        spinner.setSelection(selection);

        /*PrefConfig.writeIdInPref(getApplicationContext(),countries[0],"Country");
        PrefConfig.writeIdInPref(getApplicationContext(),c_code[0],"Country_code");
        PrefConfig.writeIntInPref(getApplicationContext(),0,"set_select");*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selected =countries[i];
                PrefConfig.writeIdInPref(getApplicationContext(),selected,"Country");
                PrefConfig.writeIdInPref(getApplicationContext(),c_code[i],"Country_code");
                PrefConfig.writeIntInPref(getApplicationContext(),i,"set_select");
                txt2.setText(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}