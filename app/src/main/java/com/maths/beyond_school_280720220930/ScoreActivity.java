package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;
    private int wrongAns=0,correctAns=0;
    private String chapter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        wrongAns=getIntent().getIntExtra("wrongAns",0);
        correctAns=getIntent().getIntExtra("correctAns",0);
        chapter=getIntent().getStringExtra("chapter");

        setBasicUiElements();
    }

    private void setBasicUiElements() {

        binding.displayResultText.setText(correctAns+"/"+(wrongAns+correctAns));
        binding.correctText.setText(correctAns+"");
        binding.wrongText.setText(wrongAns+"");
        binding.progressBar.setMax(correctAns+wrongAns);
        binding.progressBar.setProgress(correctAns);

        binding.toolBar.titleText.setText("Score Board");

        if (correctAns>=9){
            binding.certify.setText("Congratulation!! You have mastered "+chapter);
        }else{


            binding.nextbtn.setVisibility(View.INVISIBLE);
        }


        binding.nextbtn.setOnClickListener(v->{
            Intent intent=new Intent(getApplicationContext(),HomeScreen.class);
            startActivity(intent);
            finish();
        });

        binding.progress.setText("Your Score");
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}