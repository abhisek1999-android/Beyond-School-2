package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class AdditionActivity extends AppCompatActivity {

    private static final String TAG = AdditionActivity.class.getSimpleName();
    private ActivityAdditionBinding binding;
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
    private Toolbar toolbar;
    private String videoUrl="";

    private String subject="";
    private String digit="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setToolbar();


        subject=getIntent().getStringExtra("subject");
        digit=getIntent().getStringExtra("max_digit");
        videoUrl=getIntent().getStringExtra("video_url");
        
        initTTS();
        initSTT();
        setButtonClick();
        setBasicUiElement();

    }

    private void setBasicUiElement() {

        binding.toolBar.titleText.setText( digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
//        binding.subject.setText("Mathematics");
//        binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));
        if (subject.equals("addition"))
            binding.operator.setText("+");

        else if (subject.equals("subtraction"))
            binding.operator.setText("-");

        else if (subject.equals("multiplication"))
        { binding.operator.setText("×");
            binding.toolBar.titleText.setText("Multiplication upto "+digit +"'s Table");}

        else if (subject.equals("division"))
            binding.operator.setText("÷");

        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.learnOrTest.setOnClickListener(v->{

            if (subject.equals("multiplication")){

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
            else{
                Intent intent =new Intent(getApplicationContext(), LearningActivity.class);
                intent.putExtra("subject",subject);
                intent.putExtra("max_digit", digit);
                intent.putExtra("video_url",videoUrl);
                startActivity(intent);
            }


        });

        if (digit.equals("1")){

            binding.digitOne.setText("0");
            binding.digitTwo.setText("0");
        }else{
            binding.digitTwo.setText("00");
            binding.digitOne.setText("00");
        }

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
                       UtilityFunctions.muteAudioStream(AdditionActivity.this);
                        isCallSTT=false;
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
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: " + result);

                try {
                    UtilityFunctions.unMuteAudioStream(AdditionActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //stt.stop();
                binding.ansTextView.setText(result);
                isCallSTT = false;
                Boolean lcsResult=new UtilityFunctions().matchingSeq(result.trim(),currentAnswer+"");

                if (lcsResult) {
                    tts.initialize("Correct Answer", AdditionActivity.this);
                    DELAY_ON_STARTING_STT=500;
                    correctAnswer++;
                } else {
                    tts.initialize("Wrong Answer and the correct answer is " + currentAnswer, AdditionActivity.this);
                    DELAY_ON_STARTING_STT=2000;
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
            public void onErrorOccurred(String errorMessage) {
                isCallSTT = true;
             tts.initialize("", AdditionActivity.this);

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
                    startActivity(new Intent(getApplicationContext(),LogActivity.class));

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

                isCallTTS=true;
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
                isCallSTT=false;
                isCallTTS=false;

            }
        });
    }

    private void setQuestion() throws InterruptedException {

       UtilityFunctions.unMuteAudioStream(AdditionActivity.this);
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