package com.maths.beyond_school270620220930;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school270620220930.extras.ReadText;
import com.maths.beyond_school270620220930.extras.RecognizeVoice;
import com.maths.beyond_school270620220930.extras.UtilityFunctions;

import io.reactivex.disposables.CompositeDisposable;

public class table_questions extends AppCompatActivity implements RecognizeVoice.GetResult, ReadText.GetResultSpeech {
    ImageView back;
    ToggleButton pause_play;
    CardView card;
    TextView Table, right_ans, wrong_ans, question_count, ans;
    int counter, count = 9, TableValue, rtans = 0, wrans = 0;
    int result, time = 500;
    String ToSet, set;
    LinearLayout layout;
    CountDownTimer countDownTimer;
    CompositeDisposable disposableSpeech;
    Boolean isActive = false, currRes = true;
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
//
//        Context mContext = getApplicationContext();
//        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//        final PowerManager.WakeLock wakeLock =  powerManager.newWakeLock(PARTIAL_WAKE_LOCK,"motionDetection:keepAwake");
//        wakeLock.acquire();


        Intent intent = getIntent();
        TableValue = intent.getIntExtra("ValueOfTable", 0);
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
        wrong_ans.setText(String.valueOf(wrans));
//        disposableSpeech=new CompositeDisposable();
        progressBar = findViewById(R.id.progressBar1);
        collectdata = findViewById(R.id.textView24);
        mic = findViewById(R.id.animationVoice);

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
        pause_play.setOnClickListener(view -> {

            if (pause_play.isChecked()) {
                progressBar.setVisibility(View.VISIBLE);
                if (count > 10)
                    count = 1;
                if(isActive){
                    readText.textToSpeech.stop();
                    recognizeVoice.speech.stopListening();
                }

                isActive = true;
                ReadFullTable(TableValue);
                counter = 0;
            }
            if (!pause_play.isChecked()) {

                isActive = false;
                amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                progressBar.setVisibility(View.INVISIBLE);
                readText.textToSpeech.stop();
                recognizeVoice.speech.stopListening();
                counter = 1;
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recognizeVoice.speech.stopListening();
                readText.textToSpeech.shutdown();
                isActive = false;
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart", "OnStart");
    }



    public void ReadFullTable(int TableValue) {
        //recognizeVoice.startListening();
        ans.setText("");
        Log.i("InActivity", "ReadFullText");
        result = count * TableValue;

        // isActive=true;

        if (isActive) {
            switch (count) {
                case 1: {
                    Log.i("InActivity", count + "");
                    ToSet = TableValue + " ones are ";
                    readText.read(ToSet);
                    set = TableValue + " X 1 = ?";
                    Table.setText(set);
                    break;
                }
                case 2: {
                    ToSet = TableValue + " twos are ";
                    readText.read(ToSet);
                    set = TableValue + " X 2 = ?";
                    Table.setText(set);
                    break;
                }
                case 3: {
                    ToSet = TableValue + " threes are ";
                    readText.read(ToSet);
                    set = TableValue + " X 3 = ?";
                    Table.setText(set);
                    break;
                }
                case 4: {
                    ToSet = TableValue + " fours are ";
                    readText.read(ToSet);
                    set = TableValue + " X 4 = ?";
                    Table.setText(set);
                    break;
                }
                case 5: {
                    ToSet = TableValue + " fives are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 5 = ?";
                    Table.setText(set);
                    break;
                }
                case 6: {
                    ToSet = TableValue + " sixs are ";
                    readText.read(ToSet);
                    set = TableValue + " X 6 = ?";
                    Table.setText(set);
                    break;
                }
                case 7: {
                    ToSet = TableValue + " sevens are ";
                    readText.read(ToSet);
                    set = TableValue + " X 7 = ?";
                    Table.setText(set);
                    break;
                }
                case 8: {
                    ToSet = TableValue + " eights are ";
                    readText.read(ToSet);
                    set = TableValue + " X 8 = ?";
                    Table.setText(set);
                    break;
                }
                case 9: {
                    ToSet = TableValue + " nines are ?";
                    readText.read(ToSet);
                    set = TableValue + " X 9 = ?";
                    Table.setText(set);
                    break;
                }
                case 10: {
                    ToSet = TableValue + " tens are ";
                    readText.read(ToSet);
                    set = TableValue + " X 10 = ?";
                    Table.setText(set);
                    break;
                }
            }

        }
        if (count <= 10) {
            question_count.setText(String.valueOf(count) + "/10");
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
        Log.i("activity", "onPause");
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

        Log.i("InActivity", "onResult" + title);
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
            if (lcsResult) {

                rtans++;
                readText.read("CORRECT");
                right_ans.setText(String.valueOf(rtans));
            }

            else {
                wrans++;
                readText.read("INCORRECT, Correct is " + result);
                wrong_ans.setText(String.valueOf(wrans));
            }

        } catch (Exception e) {
            e.printStackTrace();
            readText.read("INCORRECT, Correct is " + result);
            wrans++;
            wrong_ans.setText(String.valueOf(wrans));
        }


        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (count <= 10) {
                    if (counter == 0)
                        isActive = true;
                    ReadFullTable(TableValue);
                    recognizeVoice.stopListening();
                }
                else {
                    recognizeVoice.stopListening();
                    mic.setVisibility(View.GONE);
                    try {
                        Dialog dialog = new Dialog(table_questions.this);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                        View mView = getLayoutInflater().inflate(R.layout.result_display, null);
                         dialog.setContentView(mView);
                        ProgressBar resultProgress = mView.findViewById(R.id.progressresult);
                        resultProgress.setMax(10);
                        resultProgress.setProgress(rtans);
                        TextView displayText = mView.findViewById(R.id.displayresulttext);
                        displayText.setText(String.valueOf(rtans) + "/10");
                        ImageView menu=mView.findViewById(R.id.menu);
                        menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        ImageView retry=mView.findViewById(R.id.retry);
                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              dialog.dismiss();

                            }
                        });
                        ImageView share=mView.findViewById(R.id.share);
                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Beyond school a era out of school");
                                String shareMessage= "\nI got "+String.valueOf(rtans)+"while learning table \n you can also try this \n\n";
                                shareMessage = shareMessage + BuildConfig.APPLICATION_ID +"\n\n";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            }
                        });
                        dialog.show();
                    }catch (Exception e){
                        Log.i("InActivity",String.valueOf(e));
                    }
                }
            }
        };
        handler.postDelayed(r, 3000);




    }

    @Override
    public void errorAction(int i) {
        Log.i("Error", "err");


        if (count <= 10 && i == SpeechRecognizer.ERROR_NO_MATCH) {
            mic.setVisibility(View.GONE);

            try {
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        if (counter == 0)
                            isActive = true;
                        ReadFullTable(TableValue);
                    }
                };
                handler.postDelayed(r, 3000);
            } catch (Exception e) {
                if (counter == 0)
                    isActive = true;
                ReadFullTable(TableValue);
            }
        }
        if (count > 10) {

            Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    counter = 0;
                    pause_play.setChecked(false);
                    rtans = 0;
                    wrans = 0;
                    right_ans.setText("0");
                    wrong_ans.setText("0");
                    amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    collectdata.setText("");
                    ans.setText("");
                }
            };
            handler.postDelayed(r, 1000);


        }


    }

    @Override
    public void gettingResultSpeech() {

        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i("InActivityrun", "OnDone");
                if (isActive) {
                    currRes = true;
                    isActive = false;
                    mic.setVisibility(View.VISIBLE);
                    recognizeVoice.startListening();
                }

            }
        }, 100);


    }
}