package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;
import com.maths.beyond_school_280720220930.subjects.MathsHelper;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class AdditionActivity extends AppCompatActivity implements ReadText.GetResultSpeech, RecognizeVoice.GetResult {

    private static final String TAG = AdditionActivity.class.getSimpleName();
    private ActivityAdditionBinding binding;
    private MathsHelper mathsHelper;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;


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
            } else {
                mathsHelper.stopListening();
                mathsHelper.stopReading();
            }
        });
    }

    private void setQuestion() {
        if (currentQuestion != 10) {
            Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
            currentAnswer = mathsHelper.add(UtilityFunctions.getRandomNumber(1), UtilityFunctions.getRandomNumber(1));
            Log.d(TAG, "setQuestion: " + currentAnswer);
        } else {
            mathsHelper.stopListening();
            UtilityFunctions.simpleToast(this, "You have completed the quiz" + wrongAnswer + "wrong answers" + correctAnswer + "correct answers");
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

    @Override
    public void gettingResultSpeech() {
        UtilityFunctions.runOnUiThread(() -> {
            mathsHelper.startListening();
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mathsHelper.stopReading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mathsHelper.stopReading();
    }


    // STP: RecognizeVoice.GetResult
    @Override
    public void gettingResult(String title) {
        if (title.equals(String.valueOf(currentAnswer))) {
            mathsHelper.readText("Correct answer");
            Log.d(TAG, "gettingResult: " + currentAnswer);
            currentAnswer++;
        } else {
            mathsHelper.readText("Wrong answer. The correct answer is " + currentAnswer);
            wrongAnswer++;
        }
        mathsHelper.stopListening();
//        setQuestion();

    }

    @Override
    public void getLogResult(String title) {

    }

    @Override
    public void errorAction(int i) {
        UtilityFunctions.runOnUiThread(() -> {
            mathsHelper.stopListening();
//            setQuestion();
        });
    }

    @Override
    public void finishAction() {
//        mathsHelper.stopListening();
    }
}