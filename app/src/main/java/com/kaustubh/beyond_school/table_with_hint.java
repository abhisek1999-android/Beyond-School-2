package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class table_with_hint extends AppCompatActivity {
    ImageView back,pause_play_button;
    Button attempt_quiz;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_with_hint);
        back = findViewById(R.id.imageView2);
        pause_play_button = findViewById(R.id.imageView3);
        attempt_quiz = findViewById(R.id.attempt_quiz);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        attempt_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(table_with_hint.this, table_questions.class);
                startActivity(intent);
            }
        });
        pause_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter==0){
                    pause_play_button.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                    back.setVisibility(View.GONE);
                    attempt_quiz.setVisibility(View.GONE);
                    counter=1;
                }
                else if (counter==1){
                    pause_play_button.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    back.setVisibility(View.VISIBLE);
                    attempt_quiz.setVisibility(View.VISIBLE);
                    counter=0;
                }
            }
        });
    }
}