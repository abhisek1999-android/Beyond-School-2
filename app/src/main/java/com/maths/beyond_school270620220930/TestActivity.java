package com.maths.beyond_school270620220930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.maths.beyond_school270620220930.extras.ReadText;
import com.maths.beyond_school270620220930.extras.RecognizeVoice;

public class TestActivity extends AppCompatActivity {

    ReadText readText;
    RecognizeVoice recognizeVoice;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView=findViewById(R.id.textView);
        Intent intent=getIntent();
        String set=intent.getStringExtra("Nameofchild");
        textView.setText(set);
    }

    public void buttonClick(View view) {
          finish();

    }
}