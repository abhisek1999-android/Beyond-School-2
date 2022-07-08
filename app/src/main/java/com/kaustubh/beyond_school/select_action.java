package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class select_action extends AppCompatActivity {
    ImageView back;
    TextView textView;

    CardView TableWithHint,TableWithoutHint,RandomTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);
        //temporary for developing
        textView=findViewById(R.id.textView33);
        back=findViewById(R.id.imageView8);
        TableWithHint=findViewById(R.id.button4);
        TableWithoutHint=findViewById(R.id.button);
        RandomTable=findViewById(R.id.button6);
        Intent intent=getIntent();
        int TableValue=intent.getIntExtra("value",0);
        textView.setText("Select Action for Table of "+TableValue);
        TableWithHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(select_action.this,table_with_hint.class);
                intent1.putExtra("ValueOfTable",TableValue);
                startActivity(intent1);
            }
        });
        TableWithoutHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(select_action.this,table_questions.class);
                intent2.putExtra("ValueOfTable",TableValue);
                startActivity(intent2);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}