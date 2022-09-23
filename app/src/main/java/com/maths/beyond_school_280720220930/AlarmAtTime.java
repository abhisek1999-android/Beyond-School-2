package com.maths.beyond_school_280720220930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityAlarmAtTimeBinding;
import com.maths.beyond_school_280720220930.model.AlarmReceiver;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.maths.beyond_school_280720220930.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class AlarmAtTime extends AppCompatActivity {
    private String time = "";


    private int REQUEST_CALENDER_ACCESS = 100;
    private TextView titletext;
    private SwitchCompat setAlarm;
    private String[] array;
    private ArrayAdapter adapter;


    public ActivityAlarmAtTimeBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAlarmAtTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkCalenderPermission();

        titletext = findViewById(R.id.titleText);
        setAlarm = findViewById(R.id.setAlarm);
        titletext.setText("Set Reminder");
        binding.toolBar.imageViewBack.setOnClickListener(view -> onBackPressed());

        setUpTextLayoutGrade();

        time=PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.timer_time));



        if (!time.equals("")){
            if (UtilityFunctions.isEventInCal(AlarmAtTime.this,PrefConfig.readIntInPref(AlarmAtTime.this,getResources().getString(R.string.calender_event_id))+""))
            { binding.extraInclude.selecttimebtn.setText("UPDATE");
            binding.extraInclude.selecttimebtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.accent));}
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                binding.extraInclude.kidsGrade.setText(binding.extraInclude.kidsGrade.getAdapter().getItem(Arrays.asList(array).indexOf(time)).toString(), false);
            }
        }

        binding.extraInclude.selecttimebtn.setOnClickListener(v->{
            try {
                time=PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.timer_time));
                if (!time.equals("")){
                  updateEvent();
                    //setEvent();
                }else{
                    setEvent();
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        });



    }


    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.time_slots);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.extraInclude.textInputLayoutTimer.getEditText());
        editText.setAdapter(adapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            binding.extraInclude.kidsGrade.setText(binding.extraInclude.kidsGrade.getAdapter().getItem(0).toString(), false);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


    private void checkCalenderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
            if (ContextCompat.checkSelfPermission(this, String.valueOf(new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR})) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR}, REQUEST_CALENDER_ACCESS);
            }
        }
    }
    
    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_CALENDER_ACCESS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                checkCalenderPermission();
            }
        }

    }


    public void updateEvent() throws ParseException {

        int eventId=PrefConfig.readIntInPref(AlarmAtTime.this,getResources().getString(R.string.calender_event_id));
        if (UtilityFunctions.isEventInCal(AlarmAtTime.this,eventId+"")){
            try{


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm aa");
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
                Date currDate=new Date();
                String datetime= dateFormatter.format(currDate)+" "+binding.extraInclude.textInputLayoutTimer.getEditText().getText().toString().trim().split("-")[0];
                String endTime= "2023/12/31 "+binding.extraInclude.textInputLayoutTimer.getEditText().getText().toString().trim().split("-")[1];
                //  String endTime="2023/08/12 06:30";

                Calendar startCal = Calendar.getInstance();
                startCal.setTime(formatter.parse(datetime));

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(formatter.parse(endTime));
                ContentResolver cr = getApplicationContext().getContentResolver();
                Uri eventUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                ContentValues event = new ContentValues();
                event.put(CalendarContract.Events.DTSTART, ((startCal.getTimeInMillis())));
                //eventValues.put(CalendarContract.Events.DTEND, ((endCal.getTimeInMillis())));
                event.put(CalendarContract.Events.DURATION,  "+P30M");
                event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                event.put(CalendarContract.Events.TITLE, "Beyond School Reminder");
                event.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
                cr.update(eventUri, event, null, null);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                PrefConfig.writeIdInPref(AlarmAtTime.this,binding.extraInclude.textInputLayoutTimer.getEditText().getText().toString(),getResources().getString(R.string.timer_time));
            }


            catch (Exception e){


                Log.i("Exception",e.getMessage());
            }


        }else{

            setEvent();
        }





    }

    @SuppressLint("Range")
    public void setEvent() throws ParseException {
        Cursor cur = this.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,null, null, null, null);
        UtilityFunctions.setEvent(AlarmAtTime.this,binding.extraInclude.textInputLayoutTimer);
    }

}