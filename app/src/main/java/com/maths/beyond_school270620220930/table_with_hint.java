package com.maths.beyond_school270620220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.maths.beyond_school270620220930.extras.ReadText;

public class table_with_hint extends AppCompatActivity implements ReadText.GetResultSpeech {
    ImageView back,Restart,Pause_Play;
    CardView ShowTable;
    TextView Table;
    TextToSpeech textToSpeech;
    Spinner spinner,spinner2;
    CountDownTimer countDownTimer;
    Boolean IsRunning=false;
    boolean speakingForQues=true;
    int count=1;
    int test=0;
    int time=4500;
    int result;
    int pauseTime=200;
    String ToSet,set="";
    int counter=0;
    int TableValue;
    ReadText readText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_with_hint);
        Intent intent=getIntent();
        TableValue=intent.getIntExtra("ValueOfTable",0);
        back = findViewById(R.id.imageView2);
        ShowTable=findViewById(R.id.ShowTable);
        Restart = findViewById(R.id.imageView3);
        Restart.setVisibility(View.GONE);
        Pause_Play=findViewById(R.id.imageView5);
        Table=findViewById(R.id.textView26);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        result=TableValue*count;
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
                    finish();
                }catch (Exception e){
                    finish();
                }


            }
        });
        Pause_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 0) {

                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
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
                }

            }
        });
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        handler.postDelayed(r, 2000);


    }
//call single fuction in finish
    @Override
    public void onBackPressed() {
        readText.textToSpeech.stop();
        readText.textToSpeech.shutdown();
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
            }, 2000);
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        readText.textToSpeech.stop();
        readText.textToSpeech.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        readText.textToSpeech.stop();
        readText.textToSpeech.shutdown();
    }
}