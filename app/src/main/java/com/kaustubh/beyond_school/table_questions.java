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
import android.os.Handler;
import android.os.Looper;
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

import com.kaustubh.beyond_school.extras.ReadText;
import com.kaustubh.beyond_school.extras.RecognizeVoice;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class table_questions extends AppCompatActivity implements RecognizeVoice.GetResult {
ImageView back,pause_play;
CardView card;
TextView Table,right_ans,wrong_ans,question_count,ans;
int counter,count=1,TableValue,rtans=0,wrans=0;
int result;
String ToSet,set;
LinearLayout layout;
CountDownTimer countDownTimer;
AudioManager audioManager;
CompositeDisposable disposableSpeech;
CompositeDisposable disposableRecognizer;
Boolean isActive=true,currRes=true;

RecognizeVoice recognizeVoice;
ReadText readText;


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
        disposableSpeech=new CompositeDisposable();
        disposableRecognizer=new CompositeDisposable();

      //  audioManager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_SHOW_UI);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }

        readText=new ReadText(getApplicationContext());
        recognizeVoice=new RecognizeVoice(getApplicationContext(),this);

        counter=0;
        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // readText.read("Hello World");


                if (counter==0){
                    speechDisposable();
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
                    recognizeVoice.stopListening();
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart","OnStart");
    }

    public void speechDisposable(){
        disposableSpeech.add(ReadText.currentMethodSpc.subscribe(method->{
            Log.i("LOG_ContactsRX",method+"");
            performSpeechOperation(method);
        }));
    }



    private void performSpeechOperation(String method) {

        switch (method){
            
            case "onDone":{
                Log.i("InActivity","onActivityOnDONE");

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("InActivityrun","InActivityrun");
                        if (isActive){
                            currRes=true;
                            isActive=false;
                            recognizeVoice.startListening();
                        }

                    }
                }, 1000);

                break;
            }
        }
    }

    public void ReadFullTable(int TableValue){
        //recognizeVoice.startListening();
        result=count*TableValue;
       // ans.setText("");
        isActive=true;
        switch (count){
            case 1:{
                ToSet="what is "+TableValue+" ones are ";
                readText.read(ToSet);
                set=TableValue+" X 1 = ?";
                Table.setText(set);
                break;
            }
            case 2:{
                ToSet="what is "+TableValue+" twos are ";
                readText.read(ToSet);
                set=TableValue+" X 2 = ?";
                Table.setText(set);
                break;
            }
            case 3:{
                ToSet="what is "+TableValue+" threes are ";
                readText.read(ToSet);
                set=TableValue+" X 3 = ?";
                Table.setText(set);
                break;
            }
            case 4:{
                ToSet="what is "+TableValue+" fours are ";
                readText.read(ToSet);
                set=TableValue+" X 4 = ?";
                Table.setText(set);
                break;
            }
            case 5:{
                ToSet="what is "+TableValue+" fives are ?";
                readText.read(ToSet);
                set=TableValue+" X 5 = ?";
                Table.setText(set);
                break;
            }
            case 6:{
                ToSet="what is "+TableValue+" sixs are ";
                readText.read(ToSet);
                set=TableValue+" X 6 = ?";
                Table.setText(set);
                break;
            }
            case 7:{
                ToSet="what is "+TableValue+" sevens are ";
                readText.read(ToSet);
                set=TableValue+" X 7 = ?";
                Table.setText(set);
                break;
            }
            case 8:{
                ToSet="what is "+TableValue+" eights are ";
                readText.read(ToSet);
                set=TableValue+" X 8 = ?";
                Table.setText(set);
                break;
            }
            case 9:{
                ToSet="what is "+TableValue+" nines are ?";
                readText.read(ToSet);
                set=TableValue+" X 9 = ?";
                Table.setText(set);
                break;
            }
            case 10: {
                ToSet = "what is "+TableValue + " tens are ";
                readText.read(ToSet);
                set = TableValue + " X 10 = ?";
                Table.setText(set);
                break;
            }
        }
        question_count.setText(String.valueOf(count)+"/10");

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

        super.onPause();
        Log.i("activity","onPause");

    }
    @Override
    protected void onResume() {
//        StartListening();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposableSpeech.dispose();
        disposableRecognizer.dispose();
    }

    @Override
    public void gettingResult(String title) {

        Log.i("InActivity","onResult"+title);
        ans.setText(title+","+result);
        count++;
        recognizeVoice.stopListening();
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
            recognizeVoice.stopListening();

        }
        else{
            recognizeVoice.stopListening();
            disposableSpeech.dispose();
            Toast.makeText(table_questions.this,"PASS",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void errorAction() {
        ReadFullTable(TableValue);
    }
}