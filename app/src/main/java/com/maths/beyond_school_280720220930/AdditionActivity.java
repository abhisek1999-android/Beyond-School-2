package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;

public class AdditionActivity extends AppCompatActivity {

    private static final String TAG = AdditionActivity.class.getSimpleName();
    private ActivityAdditionBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private int DELAY_ON_STARTING_STT = 500;
    private int DELAY_ON_SETTING_QUESTION = 3000;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;
    private Boolean isCallTTS = true;
    private Boolean isNewQuestionGenerated = true;
    private Boolean isAnswered = false;
    private Toolbar toolbar;

    private TextView digit1Text;
    private String subject = "";
    private String digit = "",videoUrl="",selectedSub="";

    private LogDatabase logDatabase;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private long startTime = 0, endTime = 0,diff=0;
    private String logs = "";
    private int currentNum1=0 ;
    private int currentNum2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();

        binding.toolBar.titleText.setText("Addition");
        subject = getIntent().getStringExtra("subject");
        digit = getIntent().getStringExtra("max_digit");
        videoUrl=getIntent().getStringExtra("video_url");
        selectedSub=getIntent().getStringExtra("selected_sub");

        logDatabase = LogDatabase.getDbInstance(this);
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();

        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
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



        binding.learnOrTest.setOnClickListener(v->{
            Intent intent =new Intent(getApplicationContext(), LearningActivity.class);
            intent.putExtra("subject",subject);
            intent.putExtra("max_digit", digit);
            intent.putExtra("video_url",videoUrl);
            intent.putExtra("selected_sub",selectedSub);
            startActivity(intent);
            finish();
        });

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
                        startTime=new Date().getTime();
                        UtilityFunctions.muteAudioStream(AdditionActivity.this);
                        isCallSTT = false;
                        stt.initialize("", AdditionActivity.this);
                        binding.animationVoice.setVisibility(View.VISIBLE);
                    }, DELAY_ON_STARTING_STT);
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                UtilityFunctions.simpleToast(AdditionActivity.this, errorMessage);
            }
        });

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
                Log.d(TAG, "onSuccess: " + result);


                endTime=new Date().getTime();

                try {
                    UtilityFunctions.unMuteAudioStream(AdditionActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logs+="Detected Result"+result+"\n";

                //stt.stop();
                binding.ansTextView.setText(result);
                isCallSTT = false;
                Boolean lcsResult = new UtilityFunctions().matchingSeq(result.trim(), currentAnswer + "");



                if (lcsResult) {

                    diff=endTime-startTime;

                    tts.initialize("Correct Answer", AdditionActivity.this);
                    logs+="Tag: Correct\n" +"Time Taken: "+UtilityFunctions.formatTime(diff)+"\n";


                    UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), "kidsid_default", "Name_default",
                            "Mathematics-Test-"+ subject, 22,currentAnswer+"", result, true, (int) (diff),
                            currentNum1+""+binding.operator.getText()+""+currentNum2+"=?","maths");

                    DELAY_ON_STARTING_STT = 500;
                    DELAY_ON_SETTING_QUESTION=2000;
                    correctAnswer++;
                } else {

                    logs+="Tag: Wrong\n"+"Time Taken: "+UtilityFunctions.formatTime(diff)+"\n";
                    tts.initialize("Wrong Answer", AdditionActivity.this);

                    UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), "kidsid_default", "Name_default",
                            "Mathematics-Test-"+ subject, 22,currentAnswer+"", result, false, (int) (diff),
                            currentNum1+""+binding.operator.getText()+""+currentNum2+"=?","maths");
                    DELAY_ON_STARTING_STT = 500;
                    DELAY_ON_SETTING_QUESTION=2000;
                    wrongAnswer++;
                }
                setWrongCorrectView();
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
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs+=title+"\n";
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                UtilityFunctions.runOnUiThread(()->{
                    isCallSTT = true;
                    tts.initialize("", AdditionActivity.this);
                },250);

                //  stt.initialize("", AdditionActivity.this);
            }
        });
    }

    private void setWrongCorrectView() {
        binding.wrongText.setText(String.valueOf(wrongAnswer));
        binding.correctText.setText(String.valueOf(correctAnswer));
    }

    private void resetViews() {
        binding.playPause.setChecked(false);
        isCallSTT = false;
        isCallTTS = false;
        currentQuestion = 1;
        correctAnswer = 0;
        wrongAnswer = 0;

        //  binding.textView26.setVisibility(View.VISIBLE);
        //  binding.textViewQuestion.setVisibility(View.GONE);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.progress.setText("1/10");
    }


    private void setToolbar() {


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

        binding.toolBar.titleText.setText(getIntent().getStringExtra("status"));

    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                binding.correctText.setText("0");
                binding.wrongText.setText("0");
                //    binding.textView26.setVisibility(View.GONE);
                //   binding.textViewQuestion.setVisibility(View.VISIBLE);
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);

                isCallTTS = true;
                try {
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //     binding.textView26.setVisibility(View.VISIBLE);
                //   binding.textViewQuestion.setVisibility(View.GONE);
                binding.animationVoice.setVisibility(View.GONE);
                binding.questionProgress.setProgress(0);
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                isCallSTT = false;
                isCallTTS = false;
                if (!isAnswered)
                    isNewQuestionGenerated=false;

            }
        });
    }

    private void setQuestion() throws InterruptedException {

        UtilityFunctions.unMuteAudioStream(AdditionActivity.this);
        isAnswered=false;

        if (isCallTTS) {

            if (isNewQuestionGenerated){
             currentNum1 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
             currentNum2 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));

            if (subject.equals("subtraction")) {
                if (currentNum1 < currentNum2) {
                    int temp = currentNum1;
                    currentNum1 = currentNum2;
                    currentNum2 = temp;
                }
            }
            if (subject.equals("division")) {

                int maxVal=20;
                if (digit.equals("2"))
                    maxVal=99;
                if (digit.equals("3"))
                    maxVal=999;
                if (digit.equals("4"))
                    maxVal=9999;

                currentNum1=UtilityFunctions.getRandomIntegerUpto(maxVal);
                currentNum2=UtilityFunctions.getRandomIntegerUpto(9);
                while (!UtilityFunctions.isDivisible(currentNum1, currentNum2)) {
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal);
                }
            }

            if (subject.equals("multiplication")) {
                currentNum1 = UtilityFunctions.getRandomIntegerUpto(10);
                currentNum2 = UtilityFunctions.getRandomNumber(1);
            }}


            logs+="Question :"+currentNum1+binding.operator.getText()+""+currentNum2+"=?\n";
            digit1Text.setText(currentNum1 + "");
            binding.digitTwo.setText(currentNum2 + "");
            binding.progress.setText(currentQuestion + "/ " + MAX_QUESTION);
            binding.ansTextView.setText("?");
            currentAnswer = MathsHelper.getMathResult(subject, currentNum1, currentNum2);

            isCallSTT = true;
            tts.initialize(MathsHelper.getMathQuestion(subject, currentNum1, currentNum2), this);

        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        isCallTTS=false;
        isCallSTT=false;

        tts.stop();
        stt.stop();

        checkLogIsEnable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCallTTS=true;
        initSTT();
        initTTS();
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
    protected void onDestroy() {
        super.onDestroy();
        tts.destroy();
        stt.destroy();
    }
}