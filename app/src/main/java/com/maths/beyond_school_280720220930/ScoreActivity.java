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

public class ScoreActivity extends AppCompatActivity {
    TextView score,certify, head,right,wrong,nextTextView;
    CardView replay,next,home;
    ImageView back;
    ProgressBar progressBar;
    String status="";

    String activity="";
    int score_val=0,tname=2,wrans=0,maxTable=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score=findViewById(R.id.displayresulttext);
        certify=findViewById(R.id.certify);
        head=findViewById(R.id.titleText);
        //replay=findViewById(R.id.restart);
        next=findViewById(R.id.nextbtn);
        right=findViewById(R.id.right);
        wrong=findViewById(R.id.wrong);
        //home=findViewById(R.id.home);
        back=findViewById(R.id.imageViewBack);
        progressBar=findViewById(R.id.progressBar);
        nextTextView=findViewById(R.id.next);
        PrefConfig.writeIntInPref(getApplicationContext(),0,getResources().getString(R.string.multiplicand));

            head.setText("Score Card");
            score_val=getIntent().getIntExtra("score",0);
            tname=getIntent().getIntExtra("tname",0);
            activity=getIntent().getStringExtra("activity");
            status=getIntent().getStringExtra("status");
            maxTable=PrefConfig.readIntInPref(getApplicationContext(),getResources().getString(R.string.last_table));
            //Toast.makeText(this, ""+score_val, Toast.LENGTH_SHORT).show();



        if (activity.equals("table_q")){
            nextTextView.setText("Try Random");
        }

            progressBar.setMax(10);
            progressBar.setProgress(score_val);
            back.setVisibility(View.GONE);
            score.setText(score_val+" / 10");

            if (score_val<10) {
                right.setText("0" + String.valueOf(score_val));
            }else{
                right.setText(String.valueOf(score_val));
            }
            wrans=10-score_val;
            if (wrans<10){
                wrong.setText("0"+String.valueOf(10-score_val));
            }else {
                wrong.setText(String.valueOf(wrans));
            }
            if (score_val>=9){
                if (status.equals("practice"))
                  certify.setText("Well Done! You have mastered Table upto "+maxTable);
                else
                  certify.setText("Well Done! You have mastered Table of "+tname);
            }else {
                next.setVisibility(View.GONE);
                certify.setText("Don't worry,You Tried your best. Revise the Table and give it one more try");
            }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        /*replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (activity.equals("table_q")){
                    Intent intent=new Intent(ScoreActivity.this,Random_questions.class);
                    intent.putExtra("ValueOfTable",tname);
                    intent.putExtra("status","tableRandom");
                    startActivity(intent);
                    finishAffinity();
                }
                else{
                    if (status.equals("practice")){

                    }
                    else {

                    }

                }

            }
        });
        /*home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ScoreActivity.this,select_action.class);
                intent.putExtra("value",tname);
                startActivity(intent);
                finish();
            }
        });*/

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (activity.equals("table_q")){
            Intent intent=new Intent(ScoreActivity.this,table_questions.class);
            intent.putExtra("ValueOfTable",tname);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent=new Intent(ScoreActivity.this,Random_questions.class);
            intent.putExtra("ValueOfTable",tname);
            startActivity(intent);
            finish();
        }


    }
}