package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.maths.beyond_school_280720220930.databinding.ActivityLearningBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.dialogs.VideoDialog;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class LearningActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = LearningActivity.class.getSimpleName();
    private ActivityLearningBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private int DELAY_ON_STARTING_STT=500;
    private int DELAY_ON_SETTING_QUESTION=2000;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;
    private Boolean isCallTTS = true;
    private Toolbar toolbar;

    private String subject="";
    private String digit="";
    private String videoUrl="";
    private String api_key="";
    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;

    private YouTubePlayerView ytPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();


        subject=getIntent().getStringExtra("subject");
        digit=getIntent().getStringExtra("max_digit");
        videoUrl=getIntent().getStringExtra("video_url");
        api_key=getResources().getString(R.string.youtube_api);



        ytPlayer=findViewById(R.id.videoView);

        Log.i("digit",digit);
        Log.i("url",videoUrl);




        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();

       // initYouTube();
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });




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
        hintDialog.setAlertTitle("Hint");
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
        if (isCallTTS){
            var currentNum1 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
            var currentNum2 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));

            if (subject.equals("subtraction")){
                if (currentNum1<currentNum2){
                    int temp=currentNum1;
                    currentNum1=currentNum2;
                    currentNum2=temp;
                }
            }
            if (subject.equals("division")){

                currentNum1=UtilityFunctions.getRandomIntegerUpto(20);
                while (!UtilityFunctions.isDivisible(currentNum1,currentNum2)){
                    currentNum1=UtilityFunctions.getRandomIntegerUpto(20);
                }
            }

            if (subject.equals("multiplication"))
            {
                currentNum1=Integer.parseInt(digit);
                currentNum2=currentQuestion;
            }

            binding.digitOne.setText(currentNum1+"");
            binding.digitTwo.setText(currentNum2+"");
            binding.progress.setText(currentQuestion+"/ "+MAX_QUESTION);
            binding.ansTextView.setText("?");
            currentAnswer = MathsHelper.getMathResult(subject,currentNum1,currentNum2);

            isCallSTT = true;
            tts.initialize(MathsHelper.getMathQuestion(subject,currentNum1,currentNum2), this);

        }


    }



    private void play(){
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
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

        isCallSTT=false;
        isCallTTS=false;
    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                binding.hintButton.setVisibility(View.VISIBLE);
                play();
            }
            else {
              pause();
            }
        });
    }

    private void setBasicUiElement() {

       // binding.toolBar.titleText.setText("Learn "+ subject.substring(0, 1).toUpperCase() + subject.substring(1));
        binding.toolBar.titleText.setText( digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
       // binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
        if (subject.equals("addition"))
            binding.operator.setText("+");

        else if (subject.equals("subtraction"))
            binding.operator.setText("-");

        else if (subject.equals("multiplication")){
            binding.operator.setText("×");
         //   binding.subSub.setText("Multiplication upto "+digit +"'s Table");
            binding.toolBar.titleText.setText("Multiplication upto "+digit +"'s Table");
        }


        else if (subject.equals("division"))
            binding.operator.setText("÷");


//        binding.learnOrTest.setOnClickListener(v->{
//            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
//            intent.putExtra("subject",subject);
//            intent.putExtra("max_digit", digit);
//            startActivity(intent);
//        });



        binding.hintButton.setOnClickListener(v->{
            pause();
            binding.playPause.setChecked(false);
           displayHintDialog(subject.substring(0, 1).toUpperCase() + subject.substring(1)+" of "+binding.digitOne.getText().toString() +" and "+ binding.digitTwo.getText().toString()+" is "+currentAnswer);
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
            startActivity(intent);
        });


        if (subject.equals("multiplication"))
            binding.playVideoLayout.setVisibility(View.INVISIBLE);

        if (digit.equals("1")){

            binding.digitOne.setText("0");
            binding.digitTwo.setText("0");
        }else{
            binding.digitTwo.setText("00");
            binding.digitOne.setText("00");
        }

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
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        digit=savedInstanceState.getString("digit");
        videoUrl=savedInstanceState.getString("videoUrl");
        subject=savedInstanceState.getString("subject");
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
                        UtilityFunctions.muteAudioStream(LearningActivity.this);
                        isCallSTT=false;
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
        currentQuestion = 1;
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.progress.setText("1/10");
    }


    private void speakAnswer(){

        binding.ansTextView.setText(currentAnswer+"");
        tts.initialize("the answer is " + currentAnswer, LearningActivity.this);
        DELAY_ON_STARTING_STT=2000;

        currentQuestion++;
        if (currentQuestion <= MAX_QUESTION) {

            UtilityFunctions.runOnUiThread(() -> {
                try {
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, DELAY_ON_SETTING_QUESTION);
        } else {
            resetViews();
        }

    }

    /**
     * Initialize STT engine
     * Answer will be check here
     */
    private void initSTT() {
        stt = SpeechToTextBuilder.builder(new ConversionCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: " + result);

                try {
                    UtilityFunctions.unMuteAudioStream(LearningActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //stt.stop();
                binding.ansTextView.setText(result);
                isCallSTT = false;
                Boolean lcsResult=new UtilityFunctions().matchingSeq(result.trim(),currentAnswer+"");

                if (lcsResult) {
                    tts.initialize("Correct Answer", LearningActivity.this);
                    DELAY_ON_STARTING_STT=500;
                    correctAnswer++;
                } else {
                    tts.initialize("Wrong Answer and the correct answer is " + currentAnswer, LearningActivity.this);
                    DELAY_ON_STARTING_STT=2000;
                    wrongAnswer++;
                }

                currentQuestion++;
                if (currentQuestion <= MAX_QUESTION) {

                    UtilityFunctions.runOnUiThread(() -> {
                        try {
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
            public void onCompletion() {
                Log.d(TAG, "onCompletion: Done");
                binding.animationVoice.setVisibility(View.GONE);
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                isCallSTT = true;
                tts.initialize("", LearningActivity.this);

                //  stt.initialize("", LearningActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.destroy();
        stt.destroy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.destroy();
        stt.destroy();
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