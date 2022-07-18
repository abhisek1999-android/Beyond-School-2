package com.maths.beyond_school270620220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class select_action extends AppCompatActivity {
    ImageView back;
    TextView tableName;

    CardView TableWithHint,TableWithoutHint,RandomTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);
        //temporary for developing
        tableName=findViewById(R.id.tableName);
        back=findViewById(R.id.imageView8);
        TableWithHint=findViewById(R.id.button4);
        TableWithoutHint=findViewById(R.id.button);
        RandomTable=findViewById(R.id.button6);
        Intent intent=getIntent();

        int TableValue=intent.getIntExtra("value",0);
        tableName.setText(String.format("%02d", TableValue)+"");
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
        RandomTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(select_action.this,Random_questions.class);
                intent3.putExtra("ValueOfTable",TableValue);
                startActivity(intent3);
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