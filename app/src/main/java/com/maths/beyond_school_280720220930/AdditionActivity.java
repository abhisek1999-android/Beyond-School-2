package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.FormatFlagsConversionMismatchException;

public class AdditionActivity extends AppCompatActivity implements ReadText.GetResultSpeech, RecognizeVoice.GetResult {

    private static final String TAG = AdditionActivity.class.getSimpleName();
    private ActivityAdditionBinding binding;
    private MathsHelper mathsHelper;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private int digit1=0;
    private int digit2=0;


    private Boolean FLAG_RECOGNIZATION=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mathsHelper = MathsHelper.getInstance(this, this, this);
        setToolbar();
        setButtonClick();


    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                setQuestion();
               // new UtilityFunctions().muteAudioStream(AdditionActivity.this);
            } else {
                mathsHelper.stopListening();
                mathsHelper.stopReading();
            }
        });
    }

    private void setQuestion() {
        if (currentQuestion != 10) {
            currentQuestion++;


        //    Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
            digit1=UtilityFunctions.getRandomNumber(1);
            digit2=UtilityFunctions.getRandomNumber(1);
            currentAnswer = mathsHelper.add(digit1,digit2);
            binding.questionView.setText(digit1+"+"+digit2+" = ");
            binding.ansTextView.setText("?");
            FLAG_RECOGNIZATION=true;
            Log.d(TAG, "setQuestion: " + currentAnswer+", "+FLAG_RECOGNIZATION);

        } else {
            mathsHelper.stopListening();
            UtilityFunctions.simpleToast(this, "You have completed the quiz" + wrongAnswer + "wrong answers" + correctAnswer + "correct answers");

            UtilityFunctions.runOnUiThread(()->{

                binding.wrongText.setText("0");
                binding.correctText.setText("0");
                binding.questionView.setText("Lets Start");
                currentQuestion=0;
            },1000);

        }
    }

    private void setToolbar() {
        binding.toolBar.titleText.setText(getIntent().getStringExtra("status"));
        binding.toolBar.imageViewBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, select_action.class);
            startActivity(intent);
            finish();
        });
    }



    // Result form TTS
    @Override
    public void gettingResultSpeech() {

        Log.i("IN_ACTIVITY","TTS RES"+FLAG_RECOGNIZATION);
        startListening();
    }

    private void startListening(){

        UtilityFunctions.runOnUiThread(() -> {
            Log.i("IN_ACTIVITY","TTS RES FLAG RUNNING_OUTSIDE"+FLAG_RECOGNIZATION);
            if (FLAG_RECOGNIZATION){

                mathsHelper.startListening();
                Log.i("IN_ACTIVITY","TTS RES FLAG RUNNING");
                FLAG_RECOGNIZATION=false;
            }
        },3000);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FLAG_RECOGNIZATION=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FLAG_RECOGNIZATION=true;
        mathsHelper.stopReading();

    }

    @Override
    protected void onPause() {
        super.onPause();
        FLAG_RECOGNIZATION=true;
        mathsHelper.stopReading();
    }

    // STP: RecognizeVoice.GetResult
    @Override
    public void gettingResult(String title) {
        mathsHelper.stopListening();


        Log.i("IN_ACTIVITY","STT RES"+FLAG_RECOGNIZATION);
        binding.ansTextView.setText(title);
        FLAG_RECOGNIZATION =false;
        if (title.equals(String.valueOf(currentAnswer))) {


            mathsHelper.readText("Correct answer");
            Log.d(TAG, "gettingResult: " + currentAnswer);
            correctAnswer++;
            binding.correctText.setText(correctAnswer+"");
        } else {

            mathsHelper.readText("Wrong answer. The correct answer is " + currentAnswer);
            wrongAnswer++;
            binding.wrongText.setText(wrongAnswer+"");
        }

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                setQuestion();
            }
        };
        handler.postDelayed(r, 3000);



    }

    @Override
    public void getLogResult(String title) {

    }

    @Override
    public void errorAction(int i) {

        Log.i("IN_ACTIVITY","ERROR");
        if (i== SpeechRecognizer.ERROR_NO_MATCH){
            FLAG_RECOGNIZATION=true;
               startListening();
        }

}}