package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class AdditionActivity extends AppCompatActivity {

    private static final String TAG = AdditionActivity.class.getSimpleName();
    private ActivityAdditionBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setToolbar();
        initTTS();
        initSTT();
        setButtonClick();
    }

    /**
     * Initialize TTS engine
     * Answer will be check here
     */
    private void initTTS() {
        tts = TextToSpeechBuilder.builder(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (isCallSTT) {
                    UtilityFunctions.runOnUiThread(() -> {
                        stt.initialize("Speak the answer", AdditionActivity.this);
                        UtilityFunctions.simpleToast(AdditionActivity.this, "Called");
                        binding.animationVoice.setVisibility(View.VISIBLE);
                    }, 1000);
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
                isCallSTT = false;
                if (Objects.equals(result, String.valueOf(currentAnswer))) {
                    tts.initialize("Correct Answer", AdditionActivity.this);
                    correctAnswer++;
                } else {
                    tts.initialize("Wrong Answer and the correct answer is " + currentAnswer, AdditionActivity.this);
                    wrongAnswer++;
                }
                setWrongCorrectView();
                currentQuestion++;
                if (currentQuestion <= MAX_QUESTION) {
                    UtilityFunctions.runOnUiThread(() -> {
                        setQuestion();
                    }, 1500);
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
                tts.initialize("Speak your answer ", AdditionActivity.this);
            }
        });
    }

    private void setWrongCorrectView() {
        binding.textView36.setText(String.valueOf(wrongAnswer));
        binding.textView25.setText(String.valueOf(correctAnswer));
    }

    private void resetViews() {
        binding.playPause.setChecked(false);
        currentQuestion = 1;
        correctAnswer = 0;
        wrongAnswer = 0;
        binding.textView26.setVisibility(View.VISIBLE);
        binding.textViewQuestion.setVisibility(View.GONE);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getIntent().getStringExtra("status"));
        binding.toolBar.imageViewBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, select_action.class);
            startActivity(intent);
            finish();
        });
    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                binding.textView25.setText("0");
                binding.textView36.setText("0");
                binding.textView26.setVisibility(View.GONE);
                binding.textViewQuestion.setVisibility(View.VISIBLE);
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                setQuestion();
            } else {
                binding.textView26.setVisibility(View.VISIBLE);
                binding.textViewQuestion.setVisibility(View.GONE);
                binding.animationVoice.setVisibility(View.GONE);
                binding.questionProgress.setProgress(0);
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                tts.destroy();
                stt.destroy();
            }
        });
    }

    private void setQuestion() {
        var currentNum1 = UtilityFunctions.getRandomNumber(1);
        var currentNum2 = UtilityFunctions.getRandomNumber(1);

        binding.questionProgress.setProgress((int) ((((double) currentQuestion) / (double) MAX_QUESTION) * 100));
        binding.textViewQuestion.setText(getResources().getString(R.string.question_text_view, String.valueOf(currentQuestion), String.valueOf(MAX_QUESTION)));
        binding.textViewQuestion.setText(getResources().getString(R.string.addition_text_view, String.valueOf(currentNum1), String.valueOf(currentNum2)));

        currentAnswer = currentNum1 + currentNum2;
        isCallSTT = true;
        tts.initialize("What is the sum of " + currentNum1 + " and " + currentNum2, this);

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