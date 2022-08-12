package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.maths.beyond_school_280720220930.SP.PrefConfig;
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
    private int DELAY_ON_STARTING_STT=500;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private Boolean isCallSTT = false;
    private Boolean isCallTTS = true;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setToolbar();
        initTTS();
        initSTT();
        setButtonClick();

        binding.toolBar.titleText.setText("Addition");
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
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
                    }, 3000);
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
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                startActivity(new Intent(getApplicationContext(),LogActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        binding.toolBar.imageViewBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, select_action.class);
            startActivity(intent);
            finish();
        });
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

//                tts.destroy();
//                stt.stop();
                //stt.destroy();
            }
        });
    }

    private void setQuestion() throws InterruptedException {

        UtilityFunctions.unMuteAudioStream(AdditionActivity.this);
        if (isCallTTS){
            var currentNum1 = UtilityFunctions.getRandomNumber(1);
            var currentNum2 = UtilityFunctions.getRandomNumber(1);
            //   binding.questionProgress.setProgress((int) ((((double) currentQuestion) / (double) 3) * 100));
            //  binding.textViewQuestion.setText(getResources().getString(R.string.addition_text_view, String.valueOf(currentNum1), String.valueOf(currentNum2)));
            binding.digitOne.setText(currentNum1+"");
            binding.digitTwo.setText(currentNum2+"");
            binding.ansTextView.setText("?");
            currentAnswer = currentNum1 + currentNum2;

            isCallSTT = true;
            tts.initialize("What is the sum of " + currentNum1 + " and " + currentNum2, this);

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