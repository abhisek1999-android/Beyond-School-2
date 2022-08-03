package com.maths.beyond_school_280720220930;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.model.AlarmReceiver;
import com.maths.beyond_school_280720220930.utils.Utils;

import java.util.Calendar;

public class AlarmAtTime extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "Time";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    TextView titletext;
    ImageView back;
    CardView cancelalarm, setalarm;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TimePicker picker;
    SwitchMaterial switchToggle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isEnable = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_at_time);
        cancelalarm = findViewById(R.id.cancelalarmbtn);
        setalarm = findViewById(R.id.setalarmbtn);
        titletext = findViewById(R.id.titleText);
        back = findViewById(R.id.imageView4);
        picker = (TimePicker) findViewById(R.id.datePicker1);
        picker.setIs24HourView(false);
        switchToggle = findViewById(R.id.switch_toggle);
        createNotificationChannel();
        titletext.setText("Set Reminder");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains(KEY_HOUR)) {
            picker.setHour(sharedPreferences.getInt("hour", 12));
            picker.setMinute(sharedPreferences.getInt("minute", 00));
        }
        setalarm.setOnClickListener(view -> saveChange());
        cancelalarm.setOnClickListener(view -> finish());
        back.setOnClickListener(view -> onBackPressed());
        switchToggleSetUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveChange() {
        setAlarm();
        PrefConfig.writeBooleanInPref(this, isEnable, getResources().getString(R.string.alarm_enable));
    }

    private void switchToggleSetUp() {
        switchToggle.setChecked(PrefConfig.readBooleanInPref(this, getResources().getString(R.string.alarm_enable)));
        switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isEnable = isChecked;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelAlarm() {

        Intent intent = new Intent(this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, Utils.getPendingIntentFlag());

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);
        picker.setHour(12);
        picker.setMinute(00);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm() {
        try {
            calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= 23) {
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                putval(picker.getHour(), picker.getMinute());
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
                calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                putval(picker.getCurrentHour(), picker.getCurrentMinute());
            }


            if (isEnable)
                setAlarm(this, calendar);
            else {
                Toast.makeText(this, "Alarm is disabled", Toast.LENGTH_SHORT).show();
                cancelAlarm();

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Noti_Err", e.getMessage());
            //    Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void setAlarm(Context context, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, Utils.getPendingIntentFlag());

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Alarm Set Successfully ", Toast.LENGTH_SHORT).show();
    }

    private void putval(int hour, int minute) {
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.apply();
    }

    /*private void showTimePicker() {
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
                    //selectedTime.setText(String.format("%02d",(timePicker.getHour()-12))+" : "+String.format("%02d",timePicker.getMinute())+" PM");

                }else{
                    //selectedTime.setText(timePicker.getHour()+" : "+timePicker.getMinute()+" AM");
                }


                calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });
        calendar=Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 23 ){
            calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
            calendar.set(Calendar.MINUTE,picker.getMinute());
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }
        else{
            calendar.set(Calendar.HOUR_OF_DAY,picker.getCurrentHour());
            calendar.set(Calendar.MINUTE,picker.getCurrentMinute());
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }
    }*/


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Beyond_SchoolChannel";
            String description = "Channel for Setting Alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Beyond_school", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AlarmAtTime.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}