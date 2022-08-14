package com.maths.beyond_school_280720220930;

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
import android.widget.TextView;

import com.maths.beyond_school_280720220930.databinding.ActivityLearningBinding;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class LearningActivity extends AppCompatActivity {

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
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();


        subject=getIntent().getStringExtra("subject");
        digit=getIntent().getStringExtra("max_digit");



        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });
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
                currentNum1=UtilityFunctions.getRandomIntegerUpto(10);
                currentNum2=UtilityFunctions.getRandomNumber(1);
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

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);

                isCallTTS=true;
                try {
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //   binding.textView26.setVisibility(View.VISIBLE);
                //   binding.textViewQuestion.setVisibility(View.GONE);
                binding.animationVoice.setVisibility(View.GONE);
                binding.questionProgress.setProgress(0);
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                isCallSTT=false;
                isCallTTS=false;

            }
        });
    }

    private void setBasicUiElement() {

        binding.toolBar.titleText.setText("Learn "+ subject.substring(0, 1).toUpperCase() + subject.substring(1));
        binding.subject.setText("Mathematics");
        binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
        if (subject.equals("addition"))
            binding.operator.setText("+");

        else if (subject.equals("subtraction"))
            binding.operator.setText("-");

        else if (subject.equals("multiplication")){
            binding.operator.setText("×");
            binding.subSub.setText("Multiplication upto "+digit +"'s Table");
        }



        else if (subject.equals("division"))
            binding.operator.setText("÷");
        binding.learnOrTest.setOnClickListener(v->{
            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
            intent.putExtra("subject",subject);
            intent.putExtra("max_digit", digit);
            startActivity(intent);
        });

    }



    private void showSetSpeedDialDialog(int i) {

        final AlertDialog.Builder alert=new AlertDialog.Builder(LearningActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.youtube_dialog, null);






        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try{
            alertDialog.show();
        }catch (Exception e){

        }
//        button.setOnClickListener(v->{
//
//            alertDialog.dismiss();
//            Intent intent=new Intent(getApplicationContext(),SearchContactActivity.class);
//            intent.putExtra("speedDialVal",i+"");
//            startActivity(intent);
//        });

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

                        speakAnswer();
//                        UtilityFunctions.muteAudioStream(LearningActivity.this);
                          isCallSTT=false;
//                        stt.initialize("", LearningActivity.this);
//                        binding.animationVoice.setVisibility(View.VISIBLE);
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
}