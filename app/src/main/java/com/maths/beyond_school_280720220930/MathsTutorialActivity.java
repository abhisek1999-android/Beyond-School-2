package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.databinding.ActivityMathsTutorialBinding;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;

public class MathsTutorialActivity extends AppCompatActivity {


    private ActivityMathsTutorialBinding binding;
    private Animation slideLeftAnim,slideRightAnim,fadeIn;
    private TextToSpeckConverter tts;
    private int DELAY_ON_STARTING_STT=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMathsTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        initTTS();
        slideLeftAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left);
        slideRightAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        fadeIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);


        binding.digitTwo.setText("9");

        binding.pressHere.setOnClickListener(v->{
            tts.initialize("Lets start learning math",MathsTutorialActivity.this);

        });
    }

    public void startAnimation(){
        binding.digitOne.startAnimation(slideLeftAnim);
        binding.secondDigitLayout.setAnimation(slideRightAnim);
        binding.operator.setAnimation(fadeIn);



    }


    /**
     * Initialize TTS engine
     * Answer will be check here
     */
    private void initTTS() {
        tts = TextToSpeechBuilder.builder(new ConversionCallback() {
            @Override
            public void onCompletion() {

                    Log.i("inSideTTS","InitSST");
                    UtilityFunctions.runOnUiThread(() -> {

                        binding.boardLayout.setVisibility(View.VISIBLE);
                        startAnimation();


                    }, DELAY_ON_STARTING_STT);

            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                UtilityFunctions.simpleToast(MathsTutorialActivity.this, errorMessage);
            }
        });

    }

}