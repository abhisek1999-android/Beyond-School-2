package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {
    List<Grades_data> notes;
    //List<String> list4;
    GradeDatabase database;
    CardView add,sub,mul,div;
    int count=16,add_val=0,sub_val=0,mul_val=1,div_val=1;
    String grade;
    TextView grade_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        add=findViewById(R.id.add);
        sub=findViewById(R.id.sub);
        mul=findViewById(R.id.mul);
        div=findViewById(R.id.div);
        grade_val=findViewById(R.id.grade_val);

        grade=getIntent().getStringExtra("grade");

        database = GradeDatabase.getDbInstance(this);
        //list4=new ArrayList<>();
        //list4.add("GRADE 3");
            notes = database.gradesDao().valus();
            for(int i=0;i<count;i++){
                Grades_data data = notes.get(i);

//                if(data.getSubject().equals("Mathematics")) {
//                for (String element : data.getGrade()) {
//                        if (element.equals(grade)) {
//                            if(data.getSubsubject().equals("Addition")){
//                                if (data.getChapter()>add_val){
//                                    add_val=data.getChapter();
//                                }
//                            }
//                            if(data.getSubsubject().equals("Subtraction")){
//                                if (data.getChapter()>sub_val){
//                                    sub_val=data.getChapter();
//                                }
//                            }
//                            if(data.getSubsubject().equals("Multiplication")){
//                                if (data.getChapter()>mul_val){
//                                    mul_val=data.getChapter();
//                                }
//                            }
//                            if(data.getSubsubject().equals("Division")){
//                                if (data.getChapter()>div_val){
//                                    div_val=data.getChapter();
//                                }
//                            }
//                        }
//                    }
//                }
            }

            grade_val.setText(grade);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   // Toast.makeText(TopicsActivity.this, String.valueOf(add_val), Toast.LENGTH_SHORT).show();

                Intent addIntent=new Intent(getApplicationContext(),AdditionActivity.class);
                addIntent.putExtra("subject","addition");
                startActivity(addIntent);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addIntent=new Intent(getApplicationContext(),AdditionActivity.class);
                addIntent.putExtra("subject","subtraction");
                startActivity(addIntent);
               // Toast.makeText(TopicsActivity.this, String.valueOf(sub_val), Toast.LENGTH_SHORT).show();
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent=new Intent(getApplicationContext(),AdditionActivity.class);
                addIntent.putExtra("subject","multiplication");
                startActivity(addIntent);
                //Toast.makeText(TopicsActivity.this, String.valueOf(mul_val), Toast.LENGTH_SHORT).show();
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent addIntent=new Intent(getApplicationContext(),AdditionActivity.class);
//                addIntent.putExtra("subject","division");
//                startActivity(addIntent);
                //Toast.makeText(TopicsActivity.this, String.valueOf(div_val), Toast.LENGTH_SHORT).show();
            }
        });

    }
}