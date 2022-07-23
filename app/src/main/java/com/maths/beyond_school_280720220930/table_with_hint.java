package com.maths.beyond_school_280720220930;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.notification.StickyNotification;


public class table_with_hint extends AppCompatActivity implements ReadText.GetResultSpeech {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "beyond";
    private static final String KEY_MULTIPLICANT = "multiplicant";
    private static final String KEY_MULTIPLIER = "multiplier";
    private static final String KEY_STATUS = "status";

    String status;

    ImageView back,Restart,Pause_Play;
    CardView ShowTable;
    TextView Table;
    TextToSpeech textToSpeech;
    Spinner spinner,spinner2;
    CountDownTimer countDownTimer;
    Boolean IsRunning=false;
    boolean speakingForQues=true;
    int count;
    int test=0;
    int time=4500;
    int result;
    int pauseTime=200;
    String ToSet,set="";
    int counter=0;
    int TableValue;
    ReadText readText;
    NotificationManager nManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_with_hint);



        Context mContext = getApplicationContext();
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock =  powerManager.newWakeLock(PARTIAL_WAKE_LOCK,"motionDetection:keepAwake");
        wakeLock.acquire();




        Intent intent=getIntent();
        count=intent.getIntExtra("count",1);
        TableValue=intent.getIntExtra("ValueOfTable",0);
        status=intent.getStringExtra("status");

        sharedPreferences=getSharedPreferences(String.valueOf(TableValue), Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        back = findViewById(R.id.imageView4);
        ShowTable=findViewById(R.id.ShowTable);
        Restart = findViewById(R.id.imageView3);
        Restart.setVisibility(View.GONE);
        Pause_Play=findViewById(R.id.imageView5);
        Table=findViewById(R.id.textView26);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        result=TableValue*count;
        nManager = ((NotificationManager) getApplicationContext().getSystemService(NotificationManager.class));
        readText=new ReadText(getApplicationContext(),this);
        Table.setText(String.valueOf(TableValue)+" X "+String.valueOf(count)+" = " + String.valueOf(result));
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time=Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.numbers2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pauseTime=Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    readText.textToSpeech.stop();
                    readText.textToSpeech.shutdown();
                    count=1;
                    onBackPressed();
                    finish();
                }catch (Exception e){
                    finish();
                }


            }
        });
        Pause_Play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (counter == 0) {

                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                    new StickyNotification(getApplicationContext(),table_with_hint.class,"| Table of "+TableValue+" | with hint").makeNotification();
                    test = 0;
                    IsRunning=true;
                    ReadFullTable(TableValue);
                    Restart.setVisibility(View.GONE);
                    counter = 1;
                }
                else if (counter==1){
                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    readText.textToSpeech.stop();
                    IsRunning=false;
                    count--;
                    counter=0;
                    test=1;

                    nManager.cancelAll();
                }

            }
        });
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                count=1;
                Restart.setVisibility(View.GONE);
                Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                test = 0;
                IsRunning=true;
                ReadFullTable(TableValue);
                counter = 1;
            }
        });
    }


    public void ReadFullTable(int TableValue){


        if (IsRunning){

            Log.i("CountValue",count+"");
            result=count*TableValue;
            switch (count){
                case 1:{
                    ToSet=TableValue+" ones are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 1 = "+result;
                    Table.setText(set);
                    break;
                }
                case 2:{
                    ToSet=TableValue+" twos are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 2 = "+result;
                    Table.setText(set);
                    break;
                }
                case 3:{
                    ToSet=TableValue+" threes are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 3 = "+result;
                    Table.setText(set);
                    break;
                }
                case 4:{
                    ToSet=TableValue+" fours are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 4 = "+result;
                    Table.setText(set);
                    break;
                }
                case 5:{
                    ToSet=TableValue+" fives are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 5 = "+result;
                    Table.setText(set);
                    break;
                }
                case 6:{
                    ToSet=TableValue+" sixs are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 6 = "+result;
                    Table.setText(set);
                    break;
                }
                case 7:{
                    ToSet=TableValue+" sevens are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 7 = "+result;
                    Table.setText(set);
                    break;
                }
                case 8:{
                    ToSet=TableValue+" eights are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 8 = "+result;
                    Table.setText(set);
                    break;
                }
                case 9:{
                    ToSet=TableValue+" nines are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set=TableValue+" X 9 = "+result;
                    Table.setText(set);
                    break;
                }
                case 10: {
                    ToSet = TableValue + " tens are ";
                    readText.read(ToSet);
                    speakingForQues=false;
                    pause();
                    set = TableValue + " X 10 = "+result;
                    Table.setText(set);
                    break;
                }
                case 11:{
                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    Restart.setVisibility(View.VISIBLE);
                    readText.textToSpeech.stop();
                    IsRunning=false;
                    count--;
                    counter=0;
                    test=1;
                    nManager.cancelAll();
                }
            }
            count++;
        }

    }

    public void pause(){


        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                readText.read(String.valueOf(result));
                speakingForQues=true;
            }
        };
        handler.postDelayed(r, 1800);


    }
//call single fuction in finish
    @Override
    public void onBackPressed() {
        readText.textToSpeech.stop();
        readText.textToSpeech.shutdown();
        nManager.cancelAll();
        if (count<11) {
            editor.putInt(KEY_MULTIPLICANT,TableValue);
            editor.putInt(KEY_MULTIPLIER,count);
            editor.putString(KEY_STATUS,status);
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }
        super.onBackPressed();
    }

    @Override
    public void gettingResultSpeech() {

        if (speakingForQues){
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadFullTable(TableValue);
                }
            }, 1800);
        }
    }



    @Override
    protected void onDestroy() {
        nManager.cancelAll();
        readText.textToSpeech.stop();
        readText.textToSpeech.shutdown();

        Log.i("destroy","destroy");
        super.onDestroy();


    }

    //    @Override
//    protected void onStop() {
//        nManager.cancelAll();
//        readText.textToSpeech.stop();
//        readText.textToSpeech.shutdown();
//        super.onStop();
//    }
}