package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaustubh.beyond_school.extras.ReadText;

import java.util.Locale;

public class table_with_hint extends AppCompatActivity implements ReadText.GetResultSpeech {
    ImageView back,Restart,Pause_Play;
    CardView ShowTable;
    TextView Table;
    TextToSpeech textToSpeech;
    Spinner spinner,spinner2;
    CountDownTimer countDownTimer;
    Boolean IsRunning=false;
    int count=1;
    int test=0;
    int time=4500;
    int result;
    int pauseTime=200;
    String ToSet,set="";
    int counter=0;
    int TableValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_with_hint);
        Intent intent=getIntent();
        TableValue=intent.getIntExtra("ValueOfTable",0);
        back = findViewById(R.id.imageView2);
        ShowTable=findViewById(R.id.ShowTable);
        Restart = findViewById(R.id.imageView3);
        Pause_Play=findViewById(R.id.imageView5);
        Table=findViewById(R.id.textView26);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        result=TableValue*count;
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
                    textToSpeech.stop();
                    countDownTimer.cancel();
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
                  //  ShowTable.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);
                    test = 0;
                    timer();
                    counter = 1;
                }
                else if (counter==1){
                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    //ShowTable.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                    textToSpeech.stop();
                    countDownTimer.cancel();
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

                    Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));

                    back.setVisibility(View.GONE);
                    if (IsRunning) {
                        textToSpeech.stop();
                        countDownTimer.cancel();
                    }
                    IsRunning=false;
                    test = 0;
                    timer();
                    counter = 1;


            }
        });
    }
    public void  timer(){
        //can we use for loop intead of countdown timer
     countDownTimer = new CountDownTimer(time*10L, time) {
        @Override
        public void onTick(long l) {
            if (test == 1) {
                textToSpeech.stop();
                Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                count = 1;
                cancel();
            }
            //use variables for 10
            if (count <= 10 && test == 0) {
                ReadFullTable(TableValue);
                Pause_Play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
            }
            IsRunning = true;
        }
        @Override
        public void onFinish() {
            count = 1;
            IsRunning = false;
            timer();
        }
    };
    if (!IsRunning) {
        countDownTimer.cancel();
        countDownTimer.start();
    }
    else{
        countDownTimer.cancel();
        count=1;
        IsRunning=false;
    }
    }

    public void ReadFullTable(int TableValue){
        result=count*TableValue;
        switch (count){
            case 1:{
                ToSet=TableValue+" ones are ";
                read(ToSet);
                pause();
                set=TableValue+" X 1 = "+result;
                Table.setText(set);
                break;
            }
            case 2:{
                ToSet=TableValue+" twos are ";
                read(ToSet);
                pause();
                set=TableValue+" X 2 = "+result;
                Table.setText(set);
                break;
            }
            case 3:{
                ToSet=TableValue+" threes are ";
                read(ToSet);
                pause();
                set=TableValue+" X 3 = "+result;
                Table.setText(set);
                break;
            }
            case 4:{
                ToSet=TableValue+" fours are ";
                read(ToSet);
                pause();
                set=TableValue+" X 4 = "+result;
                Table.setText(set);
                break;
            }
            case 5:{
                ToSet=TableValue+" fives are ";
                read(ToSet);
                pause();
                set=TableValue+" X 5 = "+result;
                Table.setText(set);
                break;
            }
            case 6:{
                ToSet=TableValue+" sixs are ";
                read(ToSet);
                pause();
                set=TableValue+" X 6 = "+result;
                Table.setText(set);
                break;
            }
            case 7:{
                ToSet=TableValue+" sevens are ";
                read(ToSet);
                pause();
                set=TableValue+" X 7 = "+result;
                Table.setText(set);
                break;
            }
            case 8:{
                ToSet=TableValue+" eights are ";
                read(ToSet);
                pause();
                set=TableValue+" X 8 = "+result;
                Table.setText(set);
                break;
            }
            case 9:{
                ToSet=TableValue+" nines are ";
                read(ToSet);
                pause();
                set=TableValue+" X 9 = "+result;
                Table.setText(set);
                break;
            }
            case 10: {
                ToSet = TableValue + " tens are ";
                read(ToSet);
                pause();
                set = TableValue + " X 10 = "+result;
                Table.setText(set);
                break;
            }
        }
        count++;
    }
    public  void read(String toread){
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(new Locale("en","IN"));
                    textToSpeech.setSpeechRate((float) 0.8);
                    textToSpeech.speak(toread,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }
    public void pause(){
        CountDownTimer Timer=new CountDownTimer(pauseTime,pauseTime) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                read(String.valueOf(result));
            }
        }.start();
    }
//call single fuction in finish
    @Override
    public void onBackPressed() {
        textToSpeech.stop();
        count=1;
        countDownTimer.cancel();
        super.onBackPressed();
    }

    @Override
    public void gettingResultSpeech() {





    }
}