package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.kaustubh.beyond_school.extras.ReadText;
import com.kaustubh.beyond_school.extras.RecognizeVoice;

public class TestActivity extends AppCompatActivity {

    ReadText readText;
    RecognizeVoice recognizeVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
       // readText=new ReadText(getApplicationContext());
       // recognizeVoice=new RecognizeVoice(getApplicationContext());

    }

    public void buttonClick(View view) {

   readText.read("hello world");
        recognizeVoice.startListening();

    }
}