package com.maths.beyond_school_280720220930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.maths.beyond_school_280720220930.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    private ActivityAlarmAtTimeBinding binding;


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
        if (!time.equals(""))
            binding.textInputLayoutTimer.getEditText().setText(time);


    }


    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.time_slots);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.textInputLayoutTimer.getEditText());
        editText.setAdapter(adapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            binding.kidsGrade.setText(binding.kidsGrade.getAdapter().getItem(0).toString(), false);
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

    @SuppressLint("Range")
    public void buttonClick(View view) throws ParseException {
        Cursor cur = this.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,null, null, null, null);
        try
        {

            PrefConfig.writeIdInPref(AlarmAtTime.this,binding.textInputLayoutTimer.getEditText().getText().toString(),getResources().getString(R.string.timer_time));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
            Date currDate=new Date();
            String datetime= dateFormatter.format(currDate)+" "+binding.textInputLayoutTimer.getEditText().getText().toString().replace(" ","").split("-")[0];
           String endTime= "2023/12/31 "+binding.textInputLayoutTimer.getEditText().getText().toString().replace(" ","").split("-")[1];
          //  String endTime="2023/08/12 06:30";

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(formatter.parse(datetime));
            
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(formatter.parse(endTime));
            var eventdate = startCal.get(Calendar.YEAR)+"/"+startCal.get(Calendar.MONTH)+"/"+startCal.get(Calendar.DAY_OF_MONTH)+" "+startCal.get(Calendar.HOUR_OF_DAY)+":"+startCal.get(Calendar.MINUTE);
            Log.e("event date",eventdate);
            // provide CalendarContract.Calendars.CONTENT_URI to
            // ContentResolver to query calendars

            if (cur.moveToFirst())
            {
               long calendarID=cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID));
                ContentValues eventValues = new ContentValues();
                eventValues.put(CalendarContract.Events.DTSTART, ((startCal.getTimeInMillis())));
                eventValues.put(CalendarContract.Events.DTEND, ((endCal.getTimeInMillis())));
                //eventValues.put(CalendarContract.Events.DURATION,  "+P10000H");
                eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                eventValues.put (CalendarContract.Events.CALENDAR_ID, calendarID);
                eventValues.put(CalendarContract.Events.TITLE, "Beyond School Reminder");
              //  eventValues.put(CalendarContract.Events.RRULE,"FREQ=DAILY;COUNT=1;BYDAY=MO,TU");
                eventValues.put(CalendarContract.Events.DESCRIPTION,"Its time to read!!");
                eventValues.put(CalendarContract.Events.ALL_DAY,false);
                eventValues.put(CalendarContract.Events.HAS_ALARM,true);

                Uri eventUri = this.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eventValues);
                var eventID = ContentUris.parseId(eventUri);


                ContentValues reminder = new ContentValues();
                reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminder.put(CalendarContract.Reminders.MINUTES, 5);

                reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
                Toast.makeText(this, "Event Added", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("Error_Events",e.getMessage());
        }
        finally
        {
            if (cur != null)
            {
                cur.close();
            }
        }

    }
}