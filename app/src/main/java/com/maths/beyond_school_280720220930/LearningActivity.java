package com.maths.beyond_school_280720220930;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityLearningBinding;
import com.maths.beyond_school_280720220930.databinding.AnimSingleLayoutBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.model.AnimData;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.AnimationUtil;
import com.maths.beyond_school_280720220930.utils.Soundex;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import org.json.JSONException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


// subject=addition,subtraction....
// selectSub= 1- Digit addition

@RequiresApi(api = Build.VERSION_CODES.O)
public class LearningActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = LearningActivity.class.getSimpleName();
    private ActivityLearningBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private int DELAY_ON_STARTING_STT = 500;
    private int DELAY_ON_SETTING_QUESTION = 3000;
    private TextToSpeckConverter tts,ttsHelper,ttsHelperAnim;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;
    private Boolean isCallTTS = true;
    private Boolean isNewQuestionGenerated = true;
    private Boolean isAnswered = false;
    private boolean isAnsByTyping = false;
    private boolean isTimerRunning = true;
    private Toolbar toolbar;
    Animation myFadeInAnimation;
    private String subject = "";
    private String selectedSub = "";
    private String digit = "";
    private String videoUrl = "";
    private String api_key = "";
    private LogDatabase logDatabase;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private long startTime = 0, endTime = 0;
    private String logs = "";
    int currentNum1 = 0;
    int currentNum2 = 0;
    private TextView digit1Text;
    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    private MediaPlayer mediaPlayer = null;
    private int attempt = 0;
    private YouTubePlayerView ytPlayer;
    private String kidsName="",kidsId="",kidsGrade="";
    private int kidsAge=0;
    Observable observable;
    private LinearLayout addAnimLayout,finalView;
    private TextView descTextView, finalText;

    private int num=0;
    private List<ProgressM> progressData;
    private long timeSpend=0;
    private ProgressDataBase progressDataBase;

    public static final int TIMER_VALUE = 15;
    EditText ans;
    private View separator;
    private Animation slideLeftAnim, slideRightAnim, fadeIn;
    private    List<AnimData> animMath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();

        logDatabase = LogDatabase.getDbInstance(this);
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        subject = getIntent().getStringExtra("subject");
        digit = getIntent().getStringExtra("max_digit");
        videoUrl = getIntent().getStringExtra("video_url");
        selectedSub = getIntent().getStringExtra("selected_sub");
        api_key = getResources().getString(R.string.youtube_api);


          animMath = AnimationUtil.getAnimList(72,45,Integer.parseInt(digit),subject);
        ytPlayer = findViewById(R.id.videoView);

        progressDataBase=ProgressDataBase.getDbInstance(LearningActivity.this);

        kidsName=PrefConfig.readIdInPref(LearningActivity.this,getResources().getString(R.string.kids_name));
        kidsId=PrefConfig.readIdInPref(LearningActivity.this,getResources().getString(R.string.kids_id));
        kidsAge=UtilityFunctions.calculateAge(PrefConfig.readIdInPref(LearningActivity.this,getResources().getString(R.string.kids_dob)));



        Log.i("digit",digit);
        Log.i("url",videoUrl);



        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();

        try {
            initTTS_helper();
            initTTSHelperAnim();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initMediaPlayer();
       // initYouTube();
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });
        //TODO: To be turned on
        binding.playPause.setEnabled(false);
        initProcess();




        checkProgressData();


    }
    private void checkProgressData() {
        progressData=UtilityFunctions.checkProgressAvailable(progressDataBase,"Mathematics"+subject,selectedSub,new Date(),0,true);

        try{
            if (progressData!=null){
                // see the changes
                timeSpend=progressData.get(0).time_spend;
                binding.timeText.setText(timeSpend+"");
            }

        }catch (Exception e){}

    }


    private void timer(){
        isTimerRunning=false;
        observable.interval(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here

                        binding.timerProgress.setMax(15);
                        binding.timerProgress.setProgress(Integer.parseInt((x+1)+""));
                        // see the changes
                        binding.timeText.setText((timeSpend+x+1)+"");
                        Log.i("task",x+"");
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnComplete(() ->
                        // do whatever you need on complete
                        Log.i("TSK","task")
                ).subscribe();


    }
    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void initYouTube() {

        ytPlayer.initialize(api_key, this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        tts.initialize(" ",LearningActivity.this);
            tts.shutdown();
        }catch (Exception e){}

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



    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(LearningActivity.this);
        hintDialog.setCancelable(true);
        hintDialog.setAlertTitle("Woohoo!!");
        hintDialog.setAlertDesciption("Hey, you completed practice successfully !!\nNow you can proceed to take test.");

        hintDialog.actionButton("START TEST");
        hintDialog.actionButtonBackgroundColor(R.color.primary);
        hintDialog.setOnActionListener(viewId->{

            switch (viewId.getId()){

                case R.id.closeButton:
                    hintDialog.dismiss();
                    isCallTTS=true;
                    break;
                case R.id.buttonAction:
                    goToTest();
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
        binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(LearningActivity.this, R.color.green));
        binding.ansTextView.setEnabled(true);
        isAnswered = false;

        if (isCallTTS) {
            int maxVal = 20;
            int minVal = 2;
            if (digit.equals("2")) {
                maxVal = 99;
                minVal = 10;
            }

            if (digit.equals("3")) {
                maxVal = 999;
                minVal = 100;

            }
            if (digit.equals("4")) {
                maxVal = 9999;
                minVal = 1000;

            }


            if (isNewQuestionGenerated) {
                if (digit.equals("1")) {
                    currentNum1 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                    currentNum2 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                } else {
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    currentNum2 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                }

                if (subject.equals("subtraction")) {
                    if (currentNum1 < currentNum2) {
                        int temp = currentNum1;
                        currentNum1 = currentNum2;
                        currentNum2 = temp;
                    }
                }
                if (subject.equals("division")) {
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    currentNum2 = UtilityFunctions.getRandomIntegerUpto(9, 2);
                    while (!UtilityFunctions.isDivisible(currentNum1, currentNum2)) {
                        currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    }
                }

                if (subject.equals("multiplication")) {
                    currentNum1 = Integer.parseInt(digit);
                    currentNum2 = currentQuestion;
                }
            }
            logs += "Question :" + currentNum1 + binding.operator.getText() + "" + currentNum2 + "=?\n";
            digit1Text.setText(currentNum1 + "");
            binding.digitTwo.setText(currentNum2 + "");
            binding.progress.setText(currentQuestion + "/ " + MAX_QUESTION);
            binding.ansTextView.setText("?");
            currentAnswer = MathsHelper.getMathResult(subject, currentNum1, currentNum2);


            isCallSTT = true;
            tts.initialize(MathsHelper.getMathQuestion(subject, currentNum1, currentNum2), this);
            binding.animWoman.playAnimation();
            isNewQuestionGenerated = true;
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private void play() {

        if (isTimerRunning)
            timer();

        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.playPause.setChecked(true);
        isCallTTS = true;
        try {
            setQuestion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(digit)>2 && !subject.equals("multiplication") )
        binding.hintButton.setVisibility(View.INVISIBLE);
        else
        binding.hintButton.setVisibility(View.VISIBLE);
    }

    private void pause() {

        binding.animationVoice.setVisibility(View.GONE);
        binding.questionProgress.setProgress(0);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);

        binding.animWoman.pauseAnimation();
        binding.animWoman.cancelAnimation();
//        UtilityFunctions.runOnUiThread(()->{
//            tts.stop();
//        },50);

        isCallSTT = false;
        isCallTTS = false;
        if (!isAnswered)
            isNewQuestionGenerated = false;
    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {

                play();
            } else {
                pause();
            }
        });
    }

    private void setBasicUiElement() {

        // binding.toolBar.titleText.setText("Learn "+ subject.substring(0, 1).toUpperCase() + subject.substring(1));
        binding.toolBar.titleText.setText(selectedSub);
        // binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));

        binding.ansTextView.setOnEditorActionListener(editorActionListener);
        binding.ansTextView.setEnabled(false);

        myFadeInAnimation = AnimationUtils.loadAnimation(LearningActivity.this, R.anim.blink);

        binding.ansTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    UtilityFunctions.runOnUiThread(()->{binding.ansTextView.setText("");},100);

                    binding.indicator.setVisibility(View.INVISIBLE);
                    binding.indicator.clearAnimation();
                    pause();
                    stt.destroy();
                } else {
                    //lost focus
                }
            }
        });

        binding.ansTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {

//                if (binding.ansTextView.getText().toString().equals(currentAnswer+"")){
//
//                    UtilityFunctions.runOnUiThread(()->{
//                        isCallTTS = true;
//                        initSTT();
//                        isNewQuestionGenerated = true;
//                        justifyAns(binding.ansTextView.getText().toString());
//                        binding.ansTextView.clearFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                    },100);
//
//                }
            }
        });


        if (subject.equals("addition")) {
            binding.operator.setText("+");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);

            digit1Text = binding.digitOneH;

        } else if (subject.equals("subtraction")) {
            binding.operator.setText("-");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);
            digit1Text = binding.digitOneH;
        } else if (subject.equals("multiplication")) {
            binding.operator.setText("×");
            //   binding.subSub.setText("Multiplication upto "+digit +"'s Table");
            digit1Text = binding.digitOne;

        } else if (subject.equals("division")) {
            binding.operator.setText("÷");
            digit1Text = binding.digitOne;
        }

//        binding.learnOrTest.setOnClickListener(v->{
//            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
//            intent.putExtra("subject",subject);
//            intent.putExtra("max_digit", digit);
//            startActivity(intent);
//        });


        binding.hintButton.setOnClickListener(v -> {
            pause();
            binding.playPause.setChecked(false);


            try {
                displayTutorialAnimation();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            displayHintDialog("The answer is " + currentAnswer);
        });

        binding.playVideoLayout.setOnClickListener(view -> {

            if (!subject.equals("multiplication")) {
                try {
                    displayTutorialAnimation();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });


        binding.giveTestButton.setOnClickListener(v->{
            goToTest();
        });


        if (subject.equals("multiplication"))
            binding.playVideoLayout.setVisibility(View.INVISIBLE);



    }

    private void goToTest(){
        Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
        intent.putExtra("subject",subject);
        intent.putExtra("max_digit", digit);
        intent.putExtra("video_url",videoUrl);
        intent.putExtra("selected_sub",selectedSub);
        startActivity(intent);
        finish();
    }

    private void displayVideoDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(LearningActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.video_dialog, null);


        ytPlayer = mView.findViewById(R.id.videoView);
        ImageView closeButton = mView.findViewById(R.id.closeButton);
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

        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        closeButton.setOnClickListener(v -> alertDialog.dismiss());


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("digit", digit);
        outState.putString("video_url", videoUrl);
        outState.putString("subject", subject);
        outState.putString("selected_sub", selectedSub);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        digit = savedInstanceState.getString("digit");
        videoUrl = savedInstanceState.getString("videoUrl");
        subject = savedInstanceState.getString("subject");
        selectedSub = savedInstanceState.getString("selected_sub");
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
                    Log.i("inSideTTS", "InitSST");
                    UtilityFunctions.runOnUiThread(() -> {

                        //  speakAnswer();
                        startTime = new Date().getTime();
                        UtilityFunctions.muteAudioStream(LearningActivity.this);

                        isCallSTT = false;
                        isAnsByTyping = false;
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
        displayCompleteDialog();
    }


    private void initTTS_helper() throws ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback[]{new ConversionCallback() {
            @Override
            public void onCompletion() {


                UtilityFunctions.runOnUiThread(() -> {
                    play();
                }, 10);

                binding.playPause.setEnabled(true);
                binding.animWoman.cancelAnimation();
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

                ttsHelper.destroy();
            }
        }}).get();
    }

    private void initTTSHelperAnim() throws ExecutionException, InterruptedException {
        ttsHelperAnim = new TTSHelperAsyncTask().execute(new ConversionCallback[]{new ConversionCallback() {
            @Override
            public void onCompletion() {

                Log.i("Index_Size",num+"");
                if (num<animMath.size()) {

                    UtilityFunctions.runOnUiThread(() -> {

                    ttsHelperAnim.initialize(animMath.get(num).getDescription(), LearningActivity.this);
                        animHandel(animMath.get(num).getAnswer(),animMath.get(num).getDescription(),animMath.get(num).getOperation());
                    num++;

                    }, 500);

//                    try{
//                    UtilityFunctions.runOnUiThread(() -> {
//
//                        Log.i("Index Size",animMath.size()-num+"");
//                        animHandel(animMath.get(num).getAnswer(),animMath.get(num).getDescription());
//                        ttsHelperAnim.initialize(animMath.get(num).getDescription(), LearningActivity.this);
//                        num++;
//                    }, 500);
//
//
//
//                    }catch (Exception e){
//
//                        Toast.makeText(LearningActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                }
                else
                {
                    num=0;
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

                ttsHelperAnim.destroy();
            }
        }}).get();
    }

    //TODO: Must be turn on
    private void initProcess() {

        ttsHelper.initialize("Lets learn " + selectedSub.split("\\(")[0] + " , you can speak and write answer in the answer box", LearningActivity.this);
        // play();
        binding.animWoman.playAnimation();

    }

    /**
     * Initialize STT engine
     * Answer will be check here
     */

    private TextView.OnEditorActionListener editorActionListener = (textView, i, keyEvent) -> {

        switch (i) {
            case EditorInfo.IME_ACTION_DONE:
                if (!binding.digitTwo.getText().toString().equals("?")) {
                    isCallTTS = true;
                    try{
                        initSTT();
                    }catch (Exception e){}

                    if (!binding.playPause.isChecked())
                        binding.playPause.setChecked(true);
                    isNewQuestionGenerated = true;
                    justifyAns(binding.ansTextView.getText().toString());
                    binding.ansTextView.clearFocus();
                }
                break;
        }
        return false;

    };


    private void initSTT() {
        stt = SpeechToTextBuilder.builder(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {

                Log.i(TAG, "On Succcess" + result);
                successResultCalling(result);
            }


            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion: Done");
                binding.animationVoice.setVisibility(View.GONE);
            }

            @Override
            public void getLogResult(String title) {
                Log.i("INLOG", title);
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void getStringResult(String title) throws JSONException {
                ConversionCallback.super.getStringResult(title);
                Log.i("SoundXCalled", title + ",title: " + Soundex.getCode(title) + ", ans:" + Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)));
                if (Soundex.getCode(title).equals(Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)))) {
                    successResultCalling(currentAnswer + "");
                } else {
                    // Changes
                    try {
                        UtilityFunctions.runOnUiThread(() -> {
                            isCallSTT = true;
                            tts.initialize("", LearningActivity.this);
                        }, 250);
                    } catch (Exception e) {
                        Log.i(TAG, "Inside String res");
                        pause();
                    }
                }


            }

            @Override
            public void onErrorOccurred(String errorMessage) {

                Log.i(TAG, "Inside err" + errorMessage);
                if (errorMessage.equals("No match")) {
                    try {
                        UtilityFunctions.runOnUiThread(() -> {
                            isCallSTT = true;
                            tts.initialize("", LearningActivity.this);
                        }, 250);

                    } catch (Exception e) {
                        Log.i(TAG, "Inside err");
                        pause();
                    }
                } else {

                    Log.i(TAG, "InsideErrElse" + errorMessage + isCallSTT + isCallTTS + isAnsByTyping);
                    // binding.playPause.setChecked(false);
                    //pause();
                }


            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void justifyAns(String result) {
        binding.indicator.setVisibility(View.INVISIBLE);
        binding.indicator.clearAnimation();

        binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(LearningActivity.this, R.color.green));
        isAnswered = true;
        try {
            UtilityFunctions.unMuteAudioStream(LearningActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //stt.stop();
        binding.ansTextView.setText(result);
        isCallSTT = false;
        Boolean lcsResult = new UtilityFunctions().matchingSeq(result.trim(), currentAnswer + "");


        endTime = new Date().getTime();
        binding.animWoman.playAnimation();
        if (lcsResult) {
            tts.initialize(UtilityFunctions.getCompliment(true), LearningActivity.this);
            mediaPlayer.start();
            //TODO: PLACE CORRECT KIDS DATA
            UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidsName,
                    "Mathematics-Practice-" + subject, kidsAge, currentAnswer + "", result, true, (int) (endTime - startTime),
                    currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", "maths");
            logs += "Tag: Correct\n" + "Time Taken: " + UtilityFunctions.formatTime(endTime - startTime) + "\n";
            DELAY_ON_STARTING_STT = 500;

            DELAY_ON_SETTING_QUESTION = 2000;
            correctAnswer++;
            attempt = 3;

            setNewQuestion();

        } else {

            binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(LearningActivity.this, R.color.red));

            if (attempt > 0) {
                binding.indicator.setVisibility(View.VISIBLE);
                binding.indicator.setAnimation(myFadeInAnimation);
                attempt--;
                isCallSTT = true;
                tts.initialize(UtilityFunctions.getCompliment(false), LearningActivity.this);
            } else {
                //TODO: PLACE CORRECT KIDS DATA
                tts.initialize("Wrong Answer and the correct answer is " + currentAnswer, LearningActivity.this);
                UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidsName,
                        "Mathematics-Practice-" + subject,kidsAge, currentAnswer + "", result, false, (int) (endTime - startTime),
                        currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", "maths");
                logs += "Tag: Wrong\n" + "Time Taken: " + UtilityFunctions.formatTime(endTime - startTime) + "\n";
                DELAY_ON_STARTING_STT = 1800;
                DELAY_ON_SETTING_QUESTION = 3000;
                wrongAnswer++;
                attempt = 3;
                isCallTTS = true;
                setNewQuestion();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void successResultCalling(String result) {

        Log.d(TAG, "onSuccess: " + result);

        justifyAns(result);

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
    protected void onStop() {
        super.onStop();
        Log.i("OnStop", "OnStop");
        if (tts!=null) {
            tts.stop();
            tts.destroy();
        }
        if (ttsHelper!=null)
            ttsHelper.destroy();
    }

    @Override
    protected void onPause() {

        pause();
        Log.i("OnPause", "OnPause");
        tts.destroy();
        stt.destroy();
        try {
            UtilityFunctions.runOnUiThread(()->{tts.stop();},10);
            tts.shutdown();
            tts.initialize(" ", LearningActivity.this);
        } catch (Exception e) {


        }
        checkLogIsEnable();
        binding.animWoman.pauseAnimation();
        try {
            UtilityFunctions.unMuteAudioStream(LearningActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // see the changes
        UtilityFunctions.checkProgressAvailable(progressDataBase,"Mathematics"+subject,selectedSub,new Date(),
                Integer.parseInt(binding.timeText.getText().toString()),false);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCallTTS = true;
        initSTT();
        initTTS();

    }

    private void displayTutorialAnimation() throws ExecutionException, InterruptedException {


        animMath=AnimationUtil.getAnimList(currentNum1,currentNum2,Integer.parseInt(digit),subject);

        final AlertDialog.Builder alert = new AlertDialog.Builder(LearningActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.tutorial_anim, null);


        num=0;
        ImageView closeButton = mView.findViewById(R.id.closeButton);
        addAnimLayout=mView.findViewById(R.id.insert_point);
        finalText=mView.findViewById(R.id.finalAns);
        finalView=mView.findViewById(R.id.finalView);
        descTextView=mView.findViewById(R.id.descTextView);

        initTTSHelperAnim();

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String initText="";
        if (!subject.equals("multiplication")) {
            initText = "Let’s learn a trick that makes you perform " + subject + " of two "+digit+"-digit numbers easily and quickly.";

        }else
            initText = "Let’s learn a trick that makes you perform " + subject + " of two numbers numbers easily and quickly.";


        descTextView.setText(initText);
        ttsHelperAnim.initialize(initText,LearningActivity.this);
//        ttsHelperAnim.initialize(animMath.get(0).getDescription(),LearningActivity.this);
//        animHandel(animMath.get(0).getAnswer());

        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        closeButton.setOnClickListener(v -> {alertDialog.dismiss();
        ttsHelperAnim.stop();
        });


    }

    private void animHandel( String answer, String decs,String operation){
        slideLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        slideRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.anim_single_layout, null);
        AnimSingleLayoutBinding binding=AnimSingleLayoutBinding.bind(view);
        binding.description.setText(decs);
        if (operation.equals("subtraction"))
            binding.operator.setText("-");
        else if(operation.equals("division"))
            binding.operator.setText("÷");
        else if (operation.equals("multiplication"))
            binding.operator.setText("×");
        binding.slNumView.setText((num+1)+".");

        binding.digitOne.setText(answer.split("_")[0]);
        binding.digitTwo.setText(answer.split("_")[1]);
        binding.ansTextView.setText(answer.split("_")[2]);
        view.startAnimation(slideRightAnim);
        ViewGroup main = (ViewGroup) addAnimLayout;
        main.addView(view, num);

        if (num==animMath.size()-1){

            finalView.setVisibility(View.VISIBLE);
            finalView.startAnimation(slideRightAnim);
            finalText.setText(answer.split("_")[2]);
        }
    }


    @Override
    protected void onDestroy() {

        tts.destroy();
        stt.destroy();
        try {
            UtilityFunctions.unMuteAudioStream(LearningActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onDestroy();
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
        if (!b) {
            //youTubePlayer.cueVideo("EaXUuwz_caU"); //Start NO automatically the video
            youTubePlayer.loadVideo(videoUrl.split("/")[3]);//Execute a playlist
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    static class TTSHelperAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }

    static class TTSHelperAsyncTaskAnim extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }
}