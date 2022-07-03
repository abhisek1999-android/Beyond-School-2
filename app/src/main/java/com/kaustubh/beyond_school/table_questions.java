package com.kaustubh.beyond_school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class table_questions extends AppCompatActivity {
ImageView back,pause_play;
CardView card;
TextView Table,right_ans,wrong_ans,question_count,ans;
int counter,count=1,TableValue,rtans=0,wrans=0;
int result;
TextToSpeech textToSpeech;
SpeechRecognizer speechRecognizer;
String ToSet,set;
LinearLayout layout;
CountDownTimer countDownTimer;
AudioManager audioManager;
    Intent speechRecognizerIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_questions);
        Intent intent=getIntent();
        TableValue=intent.getIntExtra("ValueOfTable",0);
        back=findViewById(R.id.imageView4);
        card=findViewById(R.id.ShowTable);
        pause_play=findViewById(R.id.imageView);
        Table=findViewById(R.id.textView26);
        question_count=findViewById(R.id.textView22);
        right_ans=findViewById(R.id.textView25);
        wrong_ans=findViewById(R.id.textView36);
        layout=findViewById(R.id.layout_set);
        layout.setVisibility(View.GONE);
        card.setVisibility(View.GONE);
        ans=findViewById(R.id.textView27);
        right_ans.setText(String.valueOf(rtans));
        wrong_ans.setText(String.valueOf(wrans));
        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
      //  audioManager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_SHOW_UI);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
         speechRecognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        counter=0;
        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter==0){
                    pause_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                    card.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    //add delay for wait
                    ReadFullTable(TableValue);
                    back.setVisibility(View.GONE);
                    counter=1;
                }
                else if (counter==1){
                    pause_play.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    card.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                    speechRecognizer.stopListening();
                    counter=0;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void StartListening(){
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM,true);
        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(table_questions.this,"started Listening",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float v) {

            }
            @Override
            public void onBufferReceived(byte[] bytes) {
            }
            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
                if (counter==1) {
                    CountDownTimer counte = new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }
                        @Override
                        public void onFinish() {
                            StartListening();
                        }
                    }.start();
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                Toast.makeText(table_questions.this,"hello",Toast.LENGTH_SHORT).show();
                ArrayList<String> data=bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                ans.setText(data.get(0));
                count++;
                CountDownTimer countDownTimer1= new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        speechRecognizer.stopListening();
                        if (ans.getText().toString().equals(String.valueOf(result))){
                            rtans++;
                            right_ans.setText(String.valueOf(rtans));
                        }
                        else{
                            wrans++;
                            wrong_ans.setText(String.valueOf(wrans));
                        }
                        if (count<=10){
                            ReadFullTable(TableValue);
                        }
                        else{
                            Toast.makeText(table_questions.this,"passssss",Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();

            }
            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });
    }
    public void ReadFullTable(int TableValue){
         result=count*TableValue;
        ans.setText("");
        switch (count){
            case 1:{
                ToSet="what is "+TableValue+" ones are ";
                read(ToSet);
                set=TableValue+" X 1 = ?";
                Table.setText(set);
                break;
            }
            case 2:{
                ToSet="what is "+TableValue+" twos are ";
                read(ToSet);
                set=TableValue+" X 2 = ?";
                Table.setText(set);
                break;
            }
            case 3:{
                ToSet="what is "+TableValue+" threes are ";
                read(ToSet);
                set=TableValue+" X 3 = ?";
                Table.setText(set);
                break;
            }
            case 4:{
                ToSet="what is "+TableValue+" fours are ";
                read(ToSet);
                set=TableValue+" X 4 = ?";
                Table.setText(set);
                break;
            }
            case 5:{
                ToSet="what is "+TableValue+" fives are ?";
                read(ToSet);
                set=TableValue+" X 5 = ?";
                Table.setText(set);
                break;
            }
            case 6:{
                ToSet="what is "+TableValue+" sixs are ";
                read(ToSet);
                set=TableValue+" X 6 = ?";
                Table.setText(set);
                break;
            }
            case 7:{
                ToSet="what is "+TableValue+" sevens are ";
                read(ToSet);
                set=TableValue+" X 7 = ?";
                Table.setText(set);
                break;
            }
            case 8:{
                ToSet="what is "+TableValue+" eights are ";
                read(ToSet);
                set=TableValue+" X 8 = ?";
                Table.setText(set);
                break;
            }
            case 9:{
                ToSet="what is "+TableValue+" nines are ?";
                read(ToSet);
                set=TableValue+" X 9 = ?";
                Table.setText(set);
                break;
            }
            case 10: {
                ToSet = "what is "+TableValue + " tens are ";
                read(ToSet);
                set = TableValue + " X 10 = ?";
                Table.setText(set);
                break;
            }
        }
        question_count.setText(String.valueOf(count)+"/10");

    }
    public  void read(String toread){
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(new Locale("en","IN"));
                    textToSpeech.setSpeechRate((float) 0.8);
                    textToSpeech.speak(toread,TextToSpeech.QUEUE_FLUSH,null);
                    countDownTimer=new CountDownTimer(4000,4000) {
                        @Override
                        public void onTick(long l) {
                        }
                        @Override
                        public void onFinish() {
                            textToSpeech.stop();
                                StartListening();
                        }
                    }.start();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    protected void onPause() {
        speechRecognizer.destroy();
        super.onPause();
        Log.i("activity","onpause");

    }
    @Override
    protected void onResume() {
        StartListening();
        super.onResume();
    }
}