package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityLearningBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.dialogs.VideoDialog;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.Soundex;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import org.json.JSONException;

import java.util.Date;

public class LearningActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = LearningActivity.class.getSimpleName();
    private ActivityLearningBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private int DELAY_ON_STARTING_STT=500;
    private int DELAY_ON_SETTING_QUESTION=3000;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;
    private Boolean isCallTTS = true;
    private Boolean isNewQuestionGenerated = true;
    private Boolean isAnswered = false;
    private Toolbar toolbar;

    private String subject="";
    private String selectedSub="";
    private String digit="";
    private String videoUrl="";
    private String api_key="";
    private LogDatabase logDatabase;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private long startTime = 0, endTime = 0;
    private String logs = "";
    int currentNum1=0 ;
    int currentNum2=0;
    private TextView digit1Text;
    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    private MediaPlayer mediaPlayer = null;
    private int attempt=3;
    private YouTubePlayerView ytPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();

        logDatabase = LogDatabase.getDbInstance(this);
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        subject=getIntent().getStringExtra("subject");
        digit=getIntent().getStringExtra("max_digit");
        videoUrl=getIntent().getStringExtra("video_url");
        selectedSub=getIntent().getStringExtra("selected_sub");
        api_key=getResources().getString(R.string.youtube_api);



        ytPlayer=findViewById(R.id.videoView);

        Log.i("digit",digit);
        Log.i("url",videoUrl);




        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();

        initMediaPlayer();
       // initYouTube();
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });

        initProcess();




    }
    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }
    private void initYouTube() {

        ytPlayer.initialize(api_key,this);
        playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };

        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        };


    }

    private void displayHintDialog(String answer) {

        HintDialog hintDialog = new HintDialog(LearningActivity.this);
        hintDialog.setCancelable(false);
        hintDialog.setAlertTitle("HINT");
        hintDialog.setAlertDesciption(answer);

        hintDialog.setOnActionListener(viewId->{

            switch (viewId.getId()){

                case R.id.closeButton:
                    hintDialog.dismiss();
                    isCallTTS=true;
                    break;


            }
        });

        hintDialog.show();

    }


    private void setToolbar() {

       binding.toolBar.imageViewBack.setImageDrawable(getDrawable(R.drawable.ic_baseline_arrow_back_24));

        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_log:
                    startActivity(new Intent(getApplicationContext(), LogActivity.class));

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        });



    }


    private void setQuestion() throws InterruptedException {

        UtilityFunctions.unMuteAudioStream(LearningActivity.this);


        isAnswered=false;
        if (isCallTTS){
            int maxVal=20;
            int minVal=2;
            if (digit.equals("2")){
                maxVal=99;
                minVal=10;
            }

            if (digit.equals("3")){
                maxVal=999;
                minVal=100;

            }
            if (digit.equals("4")){
                maxVal=9999;
                minVal=1000;

            }


            if (isNewQuestionGenerated){
                if (digit.equals("1")){
                    currentNum1 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                    currentNum2 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                }
                else{
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal,minVal);
                    currentNum2 = UtilityFunctions.getRandomIntegerUpto(maxVal,minVal);
                }

                if (subject.equals("subtraction")){
                    if (currentNum1<currentNum2){
                        int temp=currentNum1;
                        currentNum1=currentNum2;
                        currentNum2=temp;
                    }
                }
                if (subject.equals("division")){
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal,minVal);
                    currentNum2=UtilityFunctions.getRandomIntegerUpto(9,2);
                    while (!UtilityFunctions.isDivisible(currentNum1,currentNum2)){
                        currentNum1=UtilityFunctions.getRandomIntegerUpto(maxVal,minVal);
                    }
                }

            if (subject.equals("multiplication"))
            {
                currentNum1=Integer.parseInt(digit);
                currentNum2=currentQuestion;
            }}
            logs+="Question :"+currentNum1+binding.operator.getText()+""+currentNum2+"=?\n";
            digit1Text.setText(currentNum1+"");
            binding.digitTwo.setText(currentNum2+"");
            binding.progress.setText(currentQuestion+"/ "+MAX_QUESTION);
            binding.ansTextView.setText("?");
            currentAnswer = MathsHelper.getMathResult(subject,currentNum1,currentNum2);


            isCallSTT = true;
            tts.initialize(MathsHelper.getMathQuestion(subject,currentNum1,currentNum2), this);
            binding.animWoman.playAnimation();
            isNewQuestionGenerated=true;
        }


    }



    private void play(){
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.playPause.setChecked(true);
        isCallTTS=true;
        try {
            setQuestion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void pause(){
        binding.animationVoice.setVisibility(View.GONE);
        binding.questionProgress.setProgress(0);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);

        binding.animWoman.pauseAnimation();
        isCallSTT=false;
        isCallTTS=false;
        if (!isAnswered)
        isNewQuestionGenerated=false;
    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
              //  binding.hintButton.setVisibility(View.VISIBLE);
                play();
            }
            else {
              pause();
            }
        });
    }

    private void setBasicUiElement() {

       // binding.toolBar.titleText.setText("Learn "+ subject.substring(0, 1).toUpperCase() + subject.substring(1));
        binding.toolBar.titleText.setText(selectedSub);
       // binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
        if (subject.equals("addition")){
            binding.operator.setText("+");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);

            digit1Text=binding.digitOneH;

        }


        else if (subject.equals("subtraction")){
            binding.operator.setText("-");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);
            digit1Text=binding.digitOneH;
        }


        else if (subject.equals("multiplication")){
            binding.operator.setText("×");
            //   binding.subSub.setText("Multiplication upto "+digit +"'s Table");
            digit1Text=binding.digitOne;

        }


        else if (subject.equals("division")){
            binding.operator.setText("÷");
            digit1Text=binding.digitOne;
        }

//        binding.learnOrTest.setOnClickListener(v->{
//            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
//            intent.putExtra("subject",subject);
//            intent.putExtra("max_digit", digit);
//            startActivity(intent);
//        });



        binding.hintButton.setOnClickListener(v->{
            pause();
            binding.playPause.setChecked(false);
           displayHintDialog("The answer is "+currentAnswer);
        });

        binding.playVideoLayout.setOnClickListener(view -> {

            if (!subject.equals("multiplication"))
            displayVideoDialog();


        });


        binding.giveTestButton.setOnClickListener(v->{
            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
            intent.putExtra("subject",subject);
            intent.putExtra("max_digit", digit);
            intent.putExtra("video_url",videoUrl);
            intent.putExtra("selected_sub",selectedSub);
            startActivity(intent);
            finish();
        });


        if (subject.equals("multiplication"))
            binding.playVideoLayout.setVisibility(View.INVISIBLE);



    }

    private void displayVideoDialog() {
        final AlertDialog.Builder alert=new AlertDialog.Builder(LearningActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.video_dialog, null);


         ytPlayer=mView.findViewById(R.id.videoView);
        ImageView closeButton=  mView.findViewById(R.id.closeButton);
         initYouTube();

//        ytPlayer.initialize(
//                api_key,
//                new YouTubePlayer.OnInitializedListener() {
//                    // Implement two methods by clicking on red
//                    // error bulb inside onInitializationSuccess
//                    // method add the video link or the playlist
//                    // link that you want to play In here we
//                    // also handle the play and pause
//                    // functionality
//                    @Override
//                    public void onInitializationSuccess(
//                            YouTubePlayer.Provider provider,
//                            YouTubePlayer youTubePlayer, boolean b)
//                    {
//                        youTubePlayer.loadVideo("HzeK7g8cD0Y");
//                        youTubePlayer.play();
//                    }
//
//                    // Inside onInitializationFailure
//                    // implement the failure functionality
//                    // Here we will show toast
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                                        YouTubeInitializationResult
//                                                                youTubeInitializationResult)
//                    {
//                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        closeButton.setOnClickListener(v->alertDialog.dismiss());





    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("digit",digit);
        outState.putString("video_url",videoUrl);
        outState.putString("subject",subject);
        outState.putString("selected_sub",selectedSub);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        digit=savedInstanceState.getString("digit");
        videoUrl=savedInstanceState.getString("videoUrl");
        subject=savedInstanceState.getString("subject");
        selectedSub=savedInstanceState.getString("selected_sub");
    }


    /**
     * Initialize TTS engine
     * Answer will be check here
     */
    private void initTTS() {
        tts = TextToSpeechBuilder.builder(new ConversionCallback() {
            @Override
            public void onCompletion() {


                if (isCallSTT && isCallTTS) {
                    Log.i("inSideTTS","InitSST");
                    UtilityFunctions.runOnUiThread(() -> {

                      //  speakAnswer();
                        startTime=new Date().getTime();
                        UtilityFunctions.muteAudioStream(LearningActivity.this);

                        isCallSTT=false;
                        binding.animWoman.cancelAnimation();
                        stt.initialize("", LearningActivity.this);
                        binding.animationVoice.setVisibility(View.VISIBLE);
                    }, DELAY_ON_STARTING_STT);
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                UtilityFunctions.simpleToast(LearningActivity.this, errorMessage);
            }
        });

    }


    private void resetViews() {
        binding.playPause.setChecked(false);
        isCallSTT = false;
        isCallTTS = false;
        currentQuestion = 1;
        mediaPlayer.pause();
        binding.animWoman.cancelAnimation();
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.progress.setText("1/10");
    }


    private void initProcess(){

        tts.initialize("Lets learn " + selectedSub.split("\\(")[0], LearningActivity.this);

        UtilityFunctions.runOnUiThread(()->play(),2000);
       // play();

    }

    /**
     * Initialize STT engine
     * Answer will be check here
     */
    private void initSTT() {
        stt = SpeechToTextBuilder.builder(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {

                successResultCalling(result);



            }


            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion: Done");
                binding.animationVoice.setVisibility(View.GONE);
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs+=title+"\n";
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void getStringResult(String title) throws JSONException {
                ConversionCallback.super.getStringResult(title);
                Log.i("SoundXCalled",title+",title: "+Soundex.getCode(title)+", ans:"+Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)));
                if (Soundex.getCode(title).equals(Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)))){
                    successResultCalling(currentAnswer+"");
                }


            }

            @Override
            public void onErrorOccurred(String errorMessage) {

                try{
                UtilityFunctions.runOnUiThread(()->{
                    isCallSTT = true;
                    tts.initialize("", LearningActivity.this);
                },250);}catch (Exception e){
                    pause();
                }


                //  stt.initialize("", LearningActivity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void successResultCalling(String result) {

        Log.d(TAG, "onSuccess: " + result);

        isAnswered=true;
        try {
            UtilityFunctions.unMuteAudioStream(LearningActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //stt.stop();
        binding.ansTextView.setText(result);
        isCallSTT = false;
        Boolean lcsResult=new UtilityFunctions().matchingSeq(result.trim(),currentAnswer+"");


        endTime=new Date().getTime();
        binding.animWoman.playAnimation();
        if (lcsResult) {
            tts.initialize(UtilityFunctions.getCompliment(true), LearningActivity.this);
            mediaPlayer.start();
            UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), "kidsid_default", "Name_default",
                    "Mathematics-Practice-"+ subject, 22,currentAnswer+"", result, true, (int) (endTime-startTime),
                    currentNum1+""+binding.operator.getText()+""+currentNum2+"=?","maths");
            logs+="Tag: Correct\n" +"Time Taken: "+UtilityFunctions.formatTime(endTime-startTime)+"\n";
            DELAY_ON_STARTING_STT=500;

            DELAY_ON_SETTING_QUESTION=2000;
            correctAnswer++;
            attempt=3;
            setNewQuestion();

        } else {

            if (attempt>0){
                attempt--;
                isCallSTT=true;
                tts.initialize(UtilityFunctions.getCompliment(false), LearningActivity.this);
            }else{
            tts.initialize("Wrong Answer and the correct answer is " + currentAnswer, LearningActivity.this);
            UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), "kidsid_default", "Name_default",
                    "Mathematics-Practice-"+ subject, 22,currentAnswer+"", result, false, (int) (endTime-startTime),
                    currentNum1+""+binding.operator.getText()+""+currentNum2+"=?","maths");
            logs+="Tag: Wrong\n" +"Time Taken: "+UtilityFunctions.formatTime(endTime-startTime)+"\n";
            DELAY_ON_STARTING_STT=1800;
            DELAY_ON_SETTING_QUESTION=3000;
            wrongAnswer++;
            attempt=3;
            setNewQuestion();
        }}




    }

    private void setNewQuestion() {
        currentQuestion++;
        if (currentQuestion <= MAX_QUESTION) {

            UtilityFunctions.runOnUiThread(() -> {
                try {
                    mediaPlayer.pause();
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, DELAY_ON_SETTING_QUESTION);
        } else {
            resetViews();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        pause();
        tts.stop();
        stt.stop();
        checkLogIsEnable();
        binding.animWoman.pauseAnimation();
        try {
            UtilityFunctions.unMuteAudioStream(LearningActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCallTTS=true;
        initSTT();
        initTTS();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.destroy();
        stt.destroy();
        try{
        UtilityFunctions.unMuteAudioStream(LearningActivity.this);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }


    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        UtilityFunctions.saveLog(logDatabase, logs);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        if(!b){
            //youTubePlayer.cueVideo("EaXUuwz_caU"); //Start NO automatically the video
            youTubePlayer.loadVideo(videoUrl.split("/")[3]);//Execute a playlist
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}