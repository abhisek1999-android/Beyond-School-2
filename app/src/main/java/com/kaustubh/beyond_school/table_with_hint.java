package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class table_with_hint extends AppCompatActivity {
    ImageView back,pause_play_button;
    CardView ShowTable;
    TextView Table;
    TextToSpeech textToSpeech;
    Boolean IsRunning=false;
    int count=1;
    int test=0;
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
        pause_play_button = findViewById(R.id.imageView3);
        Table=findViewById(R.id.textView26);
        ShowTable.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pause_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 0) {
                    pause_play_button.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                    ShowTable.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);
                    test = 0;
                    timer();
                    counter = 1;
                  }

                else if (counter==1){
                    pause_play_button.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    ShowTable.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                    textToSpeech.stop();
                    counter=0;
                    test=1;
                }

            }
        });
    }
    public void  timer(){
    CountDownTimer countDownTimer = new CountDownTimer(45000, 4500) {
        @Override
        public void onTick(long l) {
            if (test == 1) {
                textToSpeech.stop();
                count = 1;
                cancel();
            }
            if (count <= 10 && test == 0) {
                ReadFullTable(TableValue);
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
        int result=count*TableValue;
        switch (count){
            case 1:{
                ToSet=TableValue+" ones are "+result;
                read(ToSet);
                set=TableValue+" X 1 = "+result;
                Table.setText(set);
                break;
            }
            case 2:{
                ToSet=TableValue+" twos are "+result;
                read(ToSet);
                set=TableValue+" X 2 = "+result;
                Table.setText(set);
                break;
            }
            case 3:{
                ToSet=TableValue+" threes are "+result;
                read(ToSet);
                set=TableValue+" X 3 = "+result;
                Table.setText(set);
                break;
            }
            case 4:{
                ToSet=TableValue+" fours are "+result;
                read(ToSet);
                set=TableValue+" X 4 = "+result;
                Table.setText(set);
                break;
            }
            case 5:{
                ToSet=TableValue+" fives are "+result;
                read(ToSet);
                set=TableValue+" X 5 = "+result;
                Table.setText(set);
                break;
            }
            case 6:{
                ToSet=TableValue+" sixs are "+result;
                read(ToSet);
                set=TableValue+" X 6 = "+result;
                Table.setText(set);
                break;
            }
            case 7:{
                ToSet=TableValue+" sevens are "+result;
                read(ToSet);
                set=TableValue+" X 7 = "+result;
                Table.setText(set);
                break;
            }
            case 8:{
                ToSet=TableValue+" eights are "+result;
                read(ToSet);
                set=TableValue+" X 8 = "+result;
                Table.setText(set);
                break;
            }
            case 9:{
                ToSet=TableValue+" nines are "+result;
                read(ToSet);
                set=TableValue+" X 9 = "+result;
                Table.setText(set);
                break;
            }
            case 10: {
                ToSet = TableValue + " tens are "+result;
                read(ToSet);
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
                    textToSpeech.setLanguage(new Locale("hi","IN"));
                    textToSpeech.setSpeechRate(1);
                    textToSpeech.speak(toread,TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });


    }
}