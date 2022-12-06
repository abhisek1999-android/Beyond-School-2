package com.maths.beyond_school_new_071020221400;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity{



    private ActivitySettingsBinding binding;
    String[] countries = {"India", "United States",
            "United Kingdom", "Canada"};
    String[] c_code = {"IN", "US", "GB", "CA"};
    String selected;
    int selection=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LogActivity.class));
        });
        binding.toolBar.titleText.setText("Settings");
        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.log_check)).equals("true")) {
           binding.logSwitch.setChecked(true);
        }

        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.log_check)).equals("false")) {
            binding.logSwitch.setChecked(false);
        }
        binding.logSwitch.setOnClickListener(v -> {
            if (binding.logSwitch.isChecked()) {
                PrefConfig.writeIdInPref(getApplicationContext(), "true", getResources().getString(R.string.log_check));
            }
            if (!binding.logSwitch.isChecked()) {
                PrefConfig.writeIdInPref(getApplicationContext(), "false", getResources().getString(R.string.log_check));

            }
        });

        binding.toolBar.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String key = PrefConfig.readIdInPref(getApplicationContext(), "Country_code");
        String country_name = PrefConfig.readIdInPref(getApplicationContext(), "Country");
        selection=PrefConfig.readIntInPref(getApplicationContext(),"set_select");
        binding.txt2.setText(country_name);

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                countries);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinner.setAdapter(ad);
        binding.spinner.setSelection(selection);

        /*PrefConfig.writeIdInPref(getApplicationContext(),countries[0],"Country");
        PrefConfig.writeIdInPref(getApplicationContext(),c_code[0],"Country_code");
        PrefConfig.writeIntInPref(getApplicationContext(),0,"set_select");*/



        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selected =countries[i];
                PrefConfig.writeIdInPref(getApplicationContext(),selected,"Country");
                PrefConfig.writeIdInPref(getApplicationContext(),c_code[i],"Country_code");
                PrefConfig.writeIntInPref(getApplicationContext(),i,"set_select");
                binding.txt2.setText(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.appVersion.setText("Version: "+BuildConfig.VERSION_NAME+" ( "+BuildConfig.VERSION_CODE+" ) ");


    }


}