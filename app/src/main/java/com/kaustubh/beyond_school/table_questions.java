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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.kaustubh.beyond_school.extras.ReadText;
import com.kaustubh.beyond_school.extras.RecognizeVoice;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class table_questions extends AppCompatActivity implements RecognizeVoice.GetResult, ReadText.GetResultSpeech {
ImageView back;
ToggleButton pause_play;
CardView card;
TextView Table,right_ans,wrong_ans,question_count,ans;
int counter,count=1,TableValue,rtans=0,wrans=0;
int result,time=500;
String ToSet,set;
LinearLayout layout;
CountDownTimer countDownTimer;
CompositeDisposable disposableSpeech;
Boolean isActive=true,currRes=true;
RecognizeVoice recognizeVoice;
ReadText readText;
ProgressBar progressBar;
TextView collectdata;
LottieAnimationView mic;
    AudioManager amanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_questions);
        Intent intent=getIntent();
        TableValue=intent.getIntExtra("ValueOfTable",0);
        back=findViewById(R.id.imageView4);
        card=findViewById(R.id.ShowTable);
        pause_play=findViewById(R.id.playPause);
        Table=findViewById(R.id.textView26);
        question_count=findViewById(R.id.textView22);
        right_ans=findViewById(R.id.textView25);
        wrong_ans=findViewById(R.id.textView36);
        layout=findViewById(R.id.layout_set);
        ans=findViewById(R.id.textView27);
        right_ans.setText(String.valueOf(rtans));
        wrong_ans.setText(String.valueOf(wrans));
//        disposableSpeech=new CompositeDisposable();
        progressBar=findViewById(R.id.progressBar1);

        collectdata=findViewById(R.id.textView24);
        mic=findViewById(R.id.animationVoice);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
        mic.setVisibility(View.GONE);

        amanager  = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        recognizeVoice=new RecognizeVoice(table_questions.this,this);
        readText=new ReadText(getApplicationContext(),table_questions.this);
        counter=0;
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.numbers2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pause_play.isChecked()){
                    progressBar.setVisibility(View.VISIBLE);
                    isActive=true;
                    ReadFullTable(TableValue);
                    if (count>10)
                    count=1;
                    counter=0;
                }
                if (!pause_play.isChecked()){

                    isActive=false;
                    amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    progressBar.setVisibility(View.INVISIBLE);
                    readText.textToSpeech.stop();
                    recognizeVoice.speech.stopListening();
                    counter=1;
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recognizeVoice.speech.stopListening();
                readText.textToSpeech.shutdown();
                isActive=false;
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart","OnStart");
    }

//    public void speechDisposable(){
//        disposableSpeech.add(ReadText.currentMethodSpc.subscribe(method->{
//            Log.i("LOG_ContactsRX",method+"");
//            performSpeechOperation(method);
//        }));
//    }



//    private void performSpeechOperation(String method) {
//
//        switch (method){
//
//            case "onDone":{
//                Log.i("InActivity","onActivityOnDONE");
//
//
//            }
//        }
//    }

    public void ReadFullTable(int TableValue){
        //recognizeVoice.startListening();
        Log.i("InActivity","ReadFullText");
        result=count*TableValue;
       // ans.setText("");
       // isActive=true;

        if (isActive){
            switch (count){
                case 1:{
                    Log.i("InActivity",count+"");
                    ToSet=TableValue+" ones are ";
                    readText.read(ToSet);
                    set=TableValue+" X 1 = ?";
                    Table.setText(set);
                    break;
                }
                case 2:{
                    ToSet=TableValue+" twos are ";
                    readText.read(ToSet);
                    set=TableValue+" X 2 = ?";
                    Table.setText(set);
                    break;
                }
                case 3:{
                    ToSet=TableValue+" threes are ";
                    readText.read(ToSet);
                    set=TableValue+" X 3 = ?";
                    Table.setText(set);
                    break;
                }
                case 4:{
                    ToSet=TableValue+" fours are ";
                    readText.read(ToSet);
                    set=TableValue+" X 4 = ?";
                    Table.setText(set);
                    break;
                }
                case 5:{
                    ToSet=TableValue+" fives are ?";
                    readText.read(ToSet);
                    set=TableValue+" X 5 = ?";
                    Table.setText(set);
                    break;
                }
                case 6:{
                    ToSet=TableValue+" sixs are ";
                    readText.read(ToSet);
                    set=TableValue+" X 6 = ?";
                    Table.setText(set);
                    break;
                }
                case 7:{
                    ToSet=TableValue+" sevens are ";
                    readText.read(ToSet);
                    set=TableValue+" X 7 = ?";
                    Table.setText(set);
                    break;
                }
                case 8:{
                    ToSet=TableValue+" eights are ";
                    readText.read(ToSet);
                    set=TableValue+" X 8 = ?";
                    Table.setText(set);
                    break;
                }
                case 9:{
                    ToSet=TableValue+" nines are ?";
                    readText.read(ToSet);
                    set=TableValue+" X 9 = ?";
                    Table.setText(set);
                    break;
                }
                case 10: {
                    ToSet =TableValue + " tens are ";
                    readText.read(ToSet);
                    set = TableValue + " X 10 = ?";
                    Table.setText(set);
                    break;
                }
            }

        }
        if (count<=10){
            question_count.setText(String.valueOf(count)+"/10");
        }
        if (count>10){

            counter=0;
            pause_play.setChecked(false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            collectdata.setText("");
            ans.setText("");
        }


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
      recognizeVoice.speech.stopListening();
        recognizeVoice.speech.destroy();
        super.onPause();
        Log.i("activity","onPause");
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

    }
    @Override
    protected void onResume() {
        super.onResume();
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recognizeVoice.stopListening();
        mic.setVisibility(View.GONE);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    @Override
    public void gettingResult(String title) {

        Log.i("InActivity","onResult"+title);
        ans.setText(title);
        String temp=collectdata.getText().toString();
        if (!temp.equals(""))
        collectdata.setText(temp+","+title.trim());
        else
         collectdata.setText(title.trim());
        count++;
        recognizeVoice.stopListening();
        mic.setVisibility(View.GONE);
        try{
            if (Integer.parseInt(title.trim())==result){

                rtans++;
                readText.read("CORRECT");
                right_ans.setText(String.valueOf(rtans));
            }
            else{
                wrans++;
                readText.read("INCORRECT, Correct is "+result);
                wrong_ans.setText(String.valueOf(wrans));
            }

        }catch (Exception e){
            e.printStackTrace();
            readText.read("INCORRECT, Correct is "+result);
            wrans++;
            wrong_ans.setText(String.valueOf(wrans));
        }
//   CountDownTimer countDown=new CountDownTimer(1000,1000) {
//       @Override
//       public void onTick(long l) {
//
//       }
//
//       @Override
//       public void onFinish() {
//           if (count<=10){
//               isActive=true;
//               ReadFullTable(TableValue);
//               ans.setText("");
//               recognizeVoice.stopListening();
//               mic.setVisibility(View.GONE);
//
//           }
//           else{
//               recognizeVoice.stopListening();
//               mic.setVisibility(View.GONE);
//               Toast.makeText(table_questions.this,"PASS",Toast.LENGTH_SHORT).show();
//           }
//       }
//   }.start();

    }

    @Override
    public void errorAction() {
        Log.i("Error","err");


//        if (counter==0)
//            isActive=true;
//        ReadFullTable(TableValue);

        mic.setVisibility(View.GONE);

        try{
            Handler handler = new Handler();
            final Runnable r = new Runnable()
            {
                public void run()
                {
                    if (counter==0)
                        isActive=true;
                    ReadFullTable(TableValue);
                }
            };
            handler.postDelayed(r, 3000);
        }catch (Exception e){
            if (counter==0)
                isActive=true;
            ReadFullTable(TableValue);
        }





    }

    @Override
    public void gettingResultSpeech() {

        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("InActivityrun","OnDone");
                if (isActive){
                    currRes=true;
                    isActive=false;
                    mic.setVisibility(View.VISIBLE);
                    recognizeVoice.startListening();
                }

            }
        }, 100);


    }
}