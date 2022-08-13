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
import java.util.List;
import java.util.Objects;

public class GradeActivity extends AppCompatActivity {
    GradeDatabase database;
    Grades_data data;
    Button next;
    TextView grade,desc;
    String grade1;
    String[] desc1={""};
    List<String> list1,list2,list4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        database=GradeDatabase.getDbInstance(this);
        next=findViewById(R.id.next);
        grade=findViewById(R.id.grade);
        desc=findViewById(R.id.desc);
        list1=new ArrayList<>();
        list1.add("GRADE 1");
        list1.add("GRADE 2");
        list1.add("GRADE 3");

        list2=new ArrayList<>();
        list2.add("GRADE 2");
        list2.add("GRADE 3");

        /*list3=new ArrayList<>();
        list3.add("GRADE 2");*/

        list4=new ArrayList<>();
        list4.add("GRADE 3");

        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.add1,new ArrayList<>(list1),"https://youtu.be/1RaL_2okktE"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.add2,new ArrayList<>(list1),"https://youtu.be/RKL0TX8ogmw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.sub1,new ArrayList<>(list1),"https://youtu.be/ShCq1BVVbQ0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.sub2,new ArrayList<>(list1),"https://youtu.be/sBJp_Toqlhw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.mul1,new ArrayList<>(list1),"https://youtu.be/fZFwHpiAVE0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.div1,new ArrayList<>(list1),"https://youtu.be/5VaqKu0ENlY"));

        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.add3,new ArrayList<>(list2),"https://youtu.be/TBzsG75tvhw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.sub3,new ArrayList<>(list2),"https://youtu.be/f0HPkXpzKf0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.mul2,new ArrayList<>(list2),"https://youtu.be/Yo_6G5TrNqo"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.div2,new ArrayList<>(list2),"https://youtu.be/2muobEZUalE"));

        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.add4,new ArrayList<>(list4),"https://youtu.be/fu7K8QFPt1o"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.add5,new ArrayList<>(list4),"https://youtu.be/2BGAUI2cdyw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.sub4,new ArrayList<>(list4),"https://youtu.be/83ePrGNcUiw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.sub5,new ArrayList<>(list4),"https://youtu.be/pvU6eNBKfaI"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.mul3,new ArrayList<>(list4),"https://youtu.be/RUGs1NmEikQ"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math,R.string.div3,new ArrayList<>(list4),"https://youtu.be/PFfcC6MO660"));

        database.gradesDao().insertNotes(new Grades_data(R.string.english,R.string.vocabulary,new ArrayList<>(list1),""));


        grade1= getIntent().getStringExtra("grade");

        grade.setText(grade1);
        if (grade1.equals(getResources().getString(R.string.grade1))){
            desc.setText(R.string.grade_1_data);
        }else if (grade1.equals(getResources().getString(R.string.grade2))){
            desc.setText(R.string.grade_2_data);
        }else{
            desc.setText(R.string.grade_3_data);
        }



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GradeActivity.this,Select_Sub_Activity.class);
                //intent.putExtra("name",getIntent().getStringExtra("name"));
                //intent.putExtra("image",getIntent().getStringExtra("image"));
                //intent.putExtra("age",getIntent().getStringExtra("age"));
                intent.putExtra("grade",grade1);
                startActivity(intent);
                finish();
            }
        });

    }
}