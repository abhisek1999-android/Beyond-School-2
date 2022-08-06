package com.maths.beyond_school_280720220930;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;
import com.maths.beyond_school_280720220930.extras.UtilityFunctions;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.notification.StickyNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class table_questions extends AppCompatActivity implements RecognizeVoice.GetResult, ReadText.GetResultSpeech {
    ImageView back,logInfo;
    ToggleButton pause_play;
    CardView card;
    TextView Table, right_ans, wrong_ans, question_count, ans;
    int counter, count = 1, TableValue, rtans = 0, wrans = 0;
    public  int result=0, time = 500;
    String ToSet, set;
    LinearLayout layout;
    CountDownTimer countDownTimer;
    CompositeDisposable disposableSpeech;
    Boolean isActive = false, currRes = true;
    RecognizeVoice recognizeVoice;
    ReadText readText;
    ProgressBar progressBarQuestion;
    TextView collectdata;
    LottieAnimationView mic;
    AudioManager amanager;
    NotificationManager nManager;
    TextView titleText,logTextView;
    private CardView logPad;
    int repeatRec=0;
    TextView tapInfoText;
    ImageView bunnyImage;
    private BottomSheetBehavior mBottomSheetBehavior;
    FirebaseAnalytics mFirebaseAnalytics;
    public static BehaviorSubject<Integer> resultState = BehaviorSubject.create();
    Bundle resultBundle;
    Toolbar toolbar;
    Date start;
    Date end;
    Date timeStamp;
    long delayTime=2000;
    long delayAtTheEnd=500;
    String status;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;

    String kidsId="",kidsName="";

    FirebaseFirestore kidsDb=FirebaseFirestore.getInstance();
    private int kidsAge=0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_questions);

        Context mContext = getApplicationContext();
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock =  powerManager.newWakeLock(PARTIAL_WAKE_LOCK,"motionDetection:keepAwake");
        wakeLock.acquire();

        mAuth= FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            retrieveKidsData();
        }

        logInfo=findViewById(R.id.logInfo);

        if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("true")){
            logInfo.setVisibility(View.VISIBLE);

        }

        logInfo.setOnClickListener(v->{
                startActivity(new Intent(getApplicationContext(),LogActivity.class));
        });

        setSupportActionBar(toolbar);


        mFirebaseAnalytics=FirebaseAnalytics.getInstance(mContext);
        resultBundle=new Bundle();

        resultState.onNext(result);
        nManager = ((NotificationManager) getApplicationContext().getSystemService(NotificationManager.class));
        Intent intent = getIntent();
        TableValue = intent.getIntExtra("ValueOfTable", 0);
        status=intent.getStringExtra("status");
        count=intent.getIntExtra("count",1);
        rtans=intent.getIntExtra("right",0);
        wrans=intent.getIntExtra("wrong",0);

        back = findViewById(R.id.imageView4);
        card = findViewById(R.id.ShowTable);
        pause_play = findViewById(R.id.playPause);
        Table = findViewById(R.id.textView26);
        question_count = findViewById(R.id.textView22);
        right_ans = findViewById(R.id.textView25);
        wrong_ans = findViewById(R.id.textView36);
        layout = findViewById(R.id.layout_set);
        ans = findViewById(R.id.textView27);
        right_ans.setText(String.valueOf(rtans));
        logTextView=findViewById(R.id.logTextView);
        titleText=findViewById(R.id.titleText);
        wrong_ans.setText(String.valueOf(wrans));
        bunnyImage=findViewById(R.id.bunny);
        //disposableSpeech=new CompositeDisposable();
        progressBarQuestion = findViewById(R.id.questionProgress);
        progressBarQuestion.setMax(10);
        tapInfoText=findViewById(R.id.tapInfoTextView);
        logPad=findViewById(R.id.logCard);
        mBottomSheetBehavior = BottomSheetBehavior.from(logPad);
        collectdata = findViewById(R.id.textView24);
        collectdata.setVisibility(View.GONE);
        mic = findViewById(R.id.animationVoice);
        //15:23



        Table.setText("Table of "+TableValue);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        mic.setVisibility(View.GONE);

        amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        recognizeVoice = new RecognizeVoice(table_questions.this, this);
        readText = new ReadText(getApplicationContext(), table_questions.this);
        counter = 0;
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.numbers2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        titleText.setText("Practice Table");

        pause_play.setOnClickListener(view -> {

            ans.setVisibility(View.VISIBLE);
            ans.setText("?");
            tapInfoText.setVisibility(View.GONE);

            if (pause_play.isChecked()) {

                timeStamp=new Date();
                muteAudioStream();
              //  progressBar.setVisibility(View.VISIBLE);
                if (count > 10)
                    count = 1;
                if(isActive){
                    readText.textToSpeech.stop();
                    recognizeVoice.speech.stopListening();
                }

                isActive = true;

               //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                try {
                    ReadFullTable(TableValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter = 0;

                new StickyNotification(getApplicationContext(),table_questions.class,"Table of "+TableValue+" | without hint").makeNotification();

            }
            if (!pause_play.isChecked()) {
                pauseAll();
                try {
                    unMuteAudioStream();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                nManager.cancelAll();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nManager.cancelAll();
                recognizeVoice.speech.stopListening();
                readText.textToSpeech.shutdown();
                isActive = false;
               // amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                try {
                    unMuteAudioStream();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(table_questions.this,select_action.class);
                intent.putExtra("value",TableValue);
                startActivity(intent);
                finish();
            }
        });
        logPadController();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart", "OnStart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("true")){
                startActivity(new Intent(getApplicationContext(),LogActivity.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void unMuteAudioStream() throws InterruptedException {
        Thread.sleep(500);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

        try{
            amanager.setStreamMute(AudioManager.STREAM_DTMF, false);
        }catch (Exception e){}
      //
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        amanager.setStreamMute(AudioManager.STREAM_ACCESSIBILITY, false);

        logTextView.setText(logTextView.getText().toString()+"AudioSate: unMute\n");
    }

    public void muteAudioStream(){
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        try{
            amanager.setStreamMute(AudioManager.STREAM_DTMF, true);
        }catch (Exception e){}
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ACCESSIBILITY, true);
        logTextView.setText(logTextView.getText().toString()+"AudioSate: mute\n");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        nManager.cancelAll();
      //  amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        try {
            unMuteAudioStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recognizeVoice.speech.stopListening();
        readText.textToSpeech.shutdown();
        isActive = false;
        Intent intent=new Intent(table_questions.this,select_action.class);
        intent.putExtra("value",TableValue);
        startActivity(intent);
        finish();


    }

    public void pauseAll(){

        isActive = false;
      // amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        try {
            unMuteAudioStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //   progressBar.setVisibility(View.INVISIBLE);
        pause_play.setChecked(false);
        mic.setVisibility(View.GONE);
        readText.textToSpeech.stop();
        recognizeVoice.speech.stopListening();
        counter = 1;

    }


    private void logPadController() {



        if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("true")){
            bunnyImage.setOnClickListener(v -> {

                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            });




            // doing some stuffs when bottom sheet is opening or closing like roatting button icon............................
            mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {



                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {


                }
            });
        }



    }

    public void ReadFullTable(int TableValue) throws InterruptedException {
        //recognizeVoice.startListening();
        unMuteAudioStream();
        ans.setText("?");
        Log.i("InActivity", "ReadFullText");
        result = count * TableValue;
        resultState.onNext(result);

        // isActive=true;

        if (isActive) {
            switch (count) {
                case 1: {
                    Log.i("InActivity", count + "");
                    ToSet = TableValue + " ones are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 1 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 2: {
                    ToSet = TableValue + " twos are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 2 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 3: {
                    ToSet = TableValue + " threes are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 3 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 4: {
                    ToSet = TableValue + " fours are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 4 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 5: {
                    ToSet = TableValue + " fives are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 5 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 6: {
                    ToSet = TableValue + " sixs are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 6 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 7: {
                    ToSet = TableValue + " sevens are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 7 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 8: {
                    ToSet = TableValue + " eights are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 8 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 9: {
                    ToSet = TableValue + " nines are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 9 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                    break;
                }
                case 10: {
                    ToSet = TableValue + " tens are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 10 = ";
                    Table.setText(set);
                    logTextView.setText(logTextView.getText().toString()+"QUESTION: "+ToSet+"\n");
                   pause_play.setEnabled(false);
                    break;
                }
                case 11:{
                    Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
        if (count <= 10) {
            question_count.setText(String.valueOf(count) + "/10");
            progressBarQuestion.setProgress(count);

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
//        recognizeVoice.speech.destroy();
        pauseAll();
        super.onPause();
        Log.i("activity", "onPause");
     //   amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        try {
            unMuteAudioStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!logTextView.getText().toString().equals("LOG"))
        {
            if (PrefConfig.readIdInPref(getApplicationContext(),"IS_LOG_ENABLE").equals("true")){
                addData();
            }
            addProgressData();
        }

        last_status();
        try{

            if (count>=10){
                if (PrefConfig.readIntInPref(getApplicationContext(),getResources().getString(R.string.last_table))<TableValue){
                    PrefConfig.writeIntInPref(getApplicationContext(),TableValue,getResources().getString(R.string.last_table));
                }
            }


        }catch (Exception e){}


    }

    private void addData() {

        try{
            LogDatabase db=LogDatabase.getDbInstance(this.getApplicationContext());
            com.maths.beyond_school_280720220930.database.log.Log logInfo=new com.maths.beyond_school_280720220930.database.log.Log();
            logInfo.log_content=logTextView.getText().toString();
            logInfo.timestamp=timeStamp.getTime()+"";

            db.logDao().insertNotes(logInfo);
        }catch (Exception e){

        }

    }


     private void last_status() {

        try{
            if (count<11) {

                PrefConfig.writeIntInPref(getApplicationContext(),TableValue,getResources().getString(R.string.multiplicand));
                PrefConfig.writeIntInPref(getApplicationContext(),count,getResources().getString(R.string.multiplier));
                PrefConfig.writeIntInPref(getApplicationContext(),rtans,getResources().getString(R.string.right));
                PrefConfig.writeIntInPref(getApplicationContext(),wrans,getResources().getString(R.string.wrong));
                PrefConfig.writeIdInPref(getApplicationContext(),status,getResources().getString(R.string.status));

            } else {

            }
        }catch (Exception e){
        }

    }

    private void addProgressData() {

        try{
            Date date=new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");

            long diff = date.getTime() - timeStamp.getTime();
            ProgressDataBase db=ProgressDataBase.getDbInstance(this.getApplicationContext());
            ProgressM progressM=new ProgressM();
            progressM.correct=rtans;
            progressM.time_to_complete=diff;
            progressM.wrong=wrans;
            progressM.time=timeFormatter.format(date)+"";
            progressM.is_completed="Yes";
            progressM.table=TableValue+"";
            progressM.date=formatter.format(date)+"";
            progressM.timestamp=date.getTime();
            db.progressDao().insertNotes(progressM);

        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
       // muteAudioStream();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nManager.cancelAll();
        recognizeVoice.speech.stopListening();
        recognizeVoice.speech.destroy();
       mic.setVisibility(View.GONE);
        //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        try {
            unMuteAudioStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void gettingResult(String title) throws InterruptedException {

        Log.i("InActivity", "onResult" + title);

        if (title.equals("buddy stop")){
           mic.setVisibility(View.GONE);
            pause_play.setChecked(false);
            pauseAll();
        }
        else{


            ans.setText(title);
            String temp = collectdata.getText().toString();
            Boolean lcsResult=new UtilityFunctions().matchingSeq(title.trim(),result+"");
            Log.i("lcsResult",lcsResult+"");
            if (!temp.equals("") && lcsResult)
                collectdata.setText(temp + "," + result);

            else if (!temp.equals("") && !lcsResult)
                collectdata.setText(temp + "," + title.trim());

            else if(temp.equals("")&& lcsResult){
                try {
                    collectdata.setText(result+"");
                }catch (Exception e){}

            }

            else
                collectdata.setText(title.trim());

            count++;
            recognizeVoice.stopListening();
            mic.setVisibility(View.GONE);

            try {
                unMuteAudioStream();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (lcsResult) {

                    end=new Date();
                    long diff = end.getTime() - start.getTime();
                    logTextView.setText(logTextView.getText().toString()+new UtilityFunctions().formatTime(diff)+"\n");

                    sendAnalyticsData(result+"",title,true,(int)(diff/1000),Table.getText().toString());
                    delayTime=1500;
                    delayAtTheEnd=900;
                    rtans++;
                    readText.read("CORRECT");
                    right_ans.setText(String.valueOf(rtans));
                }

                else {
                    end=new Date();
                    long diff = end.getTime() - start.getTime();
                    logTextView.setText(logTextView.getText().toString()+new UtilityFunctions().formatTime(diff)+"\n");
                    sendAnalyticsData(result+"",title,false,(int)(diff/1000),Table.getText().toString());
                    delayTime=3000;
                    delayAtTheEnd=1800;
                    wrans++;
                    readText.read("INCORRECT, Correct is " + result);
                    wrong_ans.setText(String.valueOf(wrans));
                }

            } catch (Exception e) {
                e.printStackTrace();
                end=new Date();
                long diff = end.getTime() - start.getTime();
                logTextView.setText(logTextView.getText().toString()+new UtilityFunctions().formatTime(diff)+"\n");
                sendAnalyticsData(result+"",title,true, (int) (diff/1000),Table.getText().toString());
                delayTime=3000;
                delayAtTheEnd=1800;
                readText.read("INCORRECT, Correct is " + result);
                wrans++;
                wrong_ans.setText(String.valueOf(wrans));
            }
            if (count>10){
                Log.i("inactivity","gt10");

                unMuteAudioStream();
                Thread.sleep(delayAtTheEnd);
                Intent intent=new Intent(table_questions.this,ScoreActivity.class);
                intent.putExtra("score",rtans);
                intent.putExtra("activity","table_q");
                intent.putExtra("status",status);
                intent.putExtra("tname",TableValue);
                startActivity(intent);
                finish();
            }
            Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    if (count <= 10) {
                        if (counter == 0)
                            isActive = true;
                        repeatRec=0;
                        try {
                            ReadFullTable(TableValue);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recognizeVoice.stopListening();
                    } else {
                        recognizeVoice.stopListening();

                    }
                }
            };
            handler.postDelayed(r, delayTime);

        }
    }

    @Override
    public void getLogResult(String title) {


        logTextView.setText(logTextView.getText().toString()+title);
    }

    @Override
    public void errorAction(int i) throws InterruptedException {
        Log.i("Error", "err");

        if (count <= 10 && i == SpeechRecognizer.ERROR_NO_MATCH) {

            if (repeatRec<3){
                repeatRec++;
              //  Toast.makeText(this, "attempt"+repeatRec, Toast.LENGTH_SHORT).show();
                recognizeVoice.startListening();
                mic.setVisibility(View.VISIBLE);
            }else{
                   mic.setVisibility(View.GONE);
                try {
                    Handler handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            if (counter == 0)
                                isActive = true;
                                repeatRec=0;
                            try {
                                ReadFullTable(TableValue);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(r, 1000);
                }

                catch (Exception e) {
                    if (counter == 0)
                        isActive = true;
                    ReadFullTable(TableValue);
                }

            }
        }


//        if (i==SpeechRecognizer.ERROR_CLIENT){
//            recognizeVoice.startListening();
//            mic.setVisibility(View.VISIBLE);
//        }

//        if (count > 10) {
//            Log.i("inactivity","gt10");
//
//            Thread.sleep(delayAtTheEnd);
//            unMuteAudioStream();
//            Intent intent=new Intent(table_questions.this,ScoreActivity.class);
//            intent.putExtra("activity","table_q");
//            intent.putExtra("score",rtans);
//            intent.putExtra("status",status);
//            intent.putExtra("tname",TableValue);
//            startActivity(intent);
//            finish();
////            Handler handler = new Handler();
////            final Runnable r = new Runnable() {
////                public void run() {
////                   // recognizeVoice.speech.stopListening();
////                    counter = 0;
////                    pause_play.setChecked(false);
////                    rtans = 0;
////                    wrans = 0;
////                    right_ans.setText("0");
////                    wrong_ans.setText("0");
////                    amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
////                    collectdata.setText("");
////                    ans.setText("");
////                    nManager.cancelAll();
////                    pause_play.setVisibility(View.VISIBLE);
////                }
////            };
////            handler.postDelayed(r, 1000);
//
//
//        }


    }

    @Override
    public void gettingResultSpeech() {


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("InActivityrun", "OnDone");
                if (isActive) {
                    currRes = true;
                    isActive = false;
                    muteAudioStream();
                        recognizeVoice.startListening();
                        mic.setVisibility(View.VISIBLE);
                        Log.i("InActivityrun", "insideOnDone");
                        start=new Date();

                }
            }
        }, 80);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void retrieveKidsData() {

        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    if (queryDocumentSnapshots.isEmpty()){
                        //     Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                        Log.i("No_data","No_data");
                    }else{
                        for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){

                            KidsData kidsData=queryDocumentSnapshot.toObject(KidsData.class);
                            kidsData.setKids_id(queryDocumentSnapshot.getId());
                            kidsId=kidsData.getKids_id();
                            kidsName=kidsData.getName();
                            kidsAge=new UtilityFunctions().calculateAge(kidsData.getAge());

                            Log.i("KidsData",kidsData.getName()+"");
                        }
                    }

                });
    }


    public void sendAnalyticsData(String result, String detected, Boolean tag, int timeTaken, String s){

        resultBundle.putString("original_result",result);
        resultBundle.putInt("table",TableValue);
        resultBundle.putString("detected_result",detected);
        resultBundle.putBoolean("is_correct",tag);
        resultBundle.putInt("timeTaken",timeTaken);
        resultBundle.putString("question",s);
        resultBundle.putString("parent_id",mCurrentUser.getUid());
        resultBundle.putString("kids_id",kidsId);
        resultBundle.putString("kids_name",kidsName);
        resultBundle.putInt("kids_age",kidsAge);
        resultBundle.putString("type","maths_multiplication_table_with_hint");
        mFirebaseAnalytics.logEvent("maths",resultBundle);

    }
}