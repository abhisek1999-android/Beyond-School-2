package com.maths.beyond_school_280720220930.english_activity.spelling;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.databinding.ActivityEnglishSpellingBinding;

public class EnglishSpellingActivity extends AppCompatActivity {

    private ActivityEnglishSpellingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishSpellingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}