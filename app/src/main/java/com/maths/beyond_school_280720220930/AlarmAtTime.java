package com.maths.beyond_school_280720220930;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.maths.beyond_school_280720220930.model.AlarmReceiver;

import java.util.Calendar;

public class AlarmAtTime extends AppCompatActivity {
    TextView selectalarm,cancelalarm,setalarm,selectedTime;
    MaterialTimePicker timePicker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    AnalogClock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_at_time);
        selectedTime=findViewById(R.id.selectedTime);
        selectalarm=findViewById(R.id.selecttimebtn);
        cancelalarm=findViewById(R.id.cancelalarmbtn);
        setalarm=findViewById(R.id.setalarmbtn);
        clock=findViewById(R.id.clock);

        createNotificationChannel();

        selectalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        setalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
        cancelalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });
    }

    private void cancelAlarm() {

        Intent intent=new Intent(this, AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);

        if (alarmManager==null){
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();

    }

    private void setAlarm() {
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        timePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();
        timePicker.show(getSupportFragmentManager(),"Beyond_school");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view) {
                if (timePicker.getHour()>12){
                    selectedTime.setText(String.format("%02d",(timePicker.getHour()-12))+" : "+String.format("%02d",timePicker.getMinute())+" PM");

                }else{
                    selectedTime.setText(timePicker.getHour()+" : "+timePicker.getMinute()+" AM");
                }


                calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="Beyond_SchoolChannel";
            String description="Channel for Setting Alarm";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("Beyond_school",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}