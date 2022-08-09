package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class GradeActivity extends AppCompatActivity {
    GradeDatabase database;
    Grades_data data;
    Button next;
    TextView grade,desc;
    String grade1;
    String[] desc1={""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        database=GradeDatabase.getDbInstance(this);
        next=findViewById(R.id.next);
        grade=findViewById(R.id.grade);
        desc=findViewById(R.id.desc);
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Addition",1,new ArrayList<>(Arrays.asList("GRADE 1")),"https://youtu.be/1RaL_2okktE"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Addition",2,new ArrayList<>(Arrays.asList("GRADE 1","GRADE 2")),"https://youtu.be/RKL0TX8ogmw"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Subtraction",1,new ArrayList<>(Arrays.asList("GRADE 1")),"https://youtu.be/ShCq1BVVbQ0"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Subtraction",2,new ArrayList<>(Arrays.asList("GRADE 1","GRADE 2")),"https://youtu.be/sBJp_Toqlhw"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Multiplication",10,new ArrayList<>(Arrays.asList("GRADE 1")),"https://youtu.be/fZFwHpiAVE0"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Division",1,new ArrayList<>(Arrays.asList("GRADE 1")),"https://youtu.be/5VaqKu0ENlY"));

        database.gradesDao().insertNotes(new Grades_data("Mathematics","Addition",3,new ArrayList<>(Arrays.asList("GRADE 2")),"https://youtu.be/TBzsG75tvhw"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Subtraction",3,new ArrayList<>(Arrays.asList("GRADE 2")),"https://youtu.be/f0HPkXpzKf0"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Multiplication",12,new ArrayList<>(Arrays.asList("GRADE 2")),"https://youtu.be/Yo_6G5TrNqo"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Division",1,new ArrayList<>(Arrays.asList("GRADE 2")),"https://youtu.be/2muobEZUalE"));

        database.gradesDao().insertNotes(new Grades_data("Mathematics","Addition",4,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/fu7K8QFPt1o"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Addition",5,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/2BGAUI2cdyw"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Subtraction",4,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/83ePrGNcUiw"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Subtraction",5,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/pvU6eNBKfaI"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Multiplication",15,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/RUGs1NmEikQ"));
        database.gradesDao().insertNotes(new Grades_data("Mathematics","Division",4,new ArrayList<>(Arrays.asList("GRADE 3")),"https://youtu.be/PFfcC6MO660"));


        grade1=getIntent().getStringExtra("grade");
        grade.setText(grade1);
        if (grade1=="GRADE 1"){
            desc.setText("A grade 1 student should know:\n1.Addition upto 2-digits.\n 2.Subtraction upto 2-digits.\n 3.Multiplying og 1-digit \nand tables up to 5.\n 4.Division of numbers upto 20" );
        }else if (grade1=="GRADE 1"){
            desc.setText("");
        }else{
            desc.setText("");
        }



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GradeActivity.this,Select_Subject.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("image",getIntent().getStringExtra("image"));
                intent.putExtra("age",getIntent().getStringExtra("age"));
                intent.putExtra("grade",getIntent().getStringExtra("grade"));
                startActivity(intent);
                finishAffinity();
            }
        });

    }
}