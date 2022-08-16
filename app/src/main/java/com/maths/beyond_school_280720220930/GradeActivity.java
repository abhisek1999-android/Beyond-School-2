package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.ActivitySelectSubBinding;
import com.maths.beyond_school_280720220930.model.KidsData;

import java.util.ArrayList;
import java.util.List;

public class GradeActivity extends AppCompatActivity {
    GradeDatabase database;
    Grades_data data;
    Button next;
    TextView grade, desc;
    String grade1;
    String[] desc1 = {""};
    List<String> list1, list2, list4;


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        database = GradeDatabase.getDbInstance(this);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        retrieveKidsData();
        var englishGradeDatabase = EnglishGradeDatabase.getDbInstance(this);
        englishGradeDatabase.englishDao().getEnglishModel(1);
        next = findViewById(R.id.next);
        grade = findViewById(R.id.grade);
        desc = findViewById(R.id.desc);
        list1 = new ArrayList<>();
        list1.add("GRADE 1");
        list1.add("GRADE 2");
        list1.add("GRADE 3");

        list2 = new ArrayList<>();
        list2.add("GRADE 2");
        list2.add("GRADE 3");

        /*list3=new ArrayList<>();
        list3.add("GRADE 2");*/

        list4 = new ArrayList<>();
        list4.add("GRADE 3");

        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add1, new ArrayList<>(list1), "https://youtu.be/1RaL_2okktE"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add2, new ArrayList<>(list1), "https://youtu.be/RKL0TX8ogmw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.sub1, new ArrayList<>(list1), "https://youtu.be/ShCq1BVVbQ0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.sub2, new ArrayList<>(list1), "https://youtu.be/sBJp_Toqlhw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.mul1, new ArrayList<>(list1), "https://youtu.be/fZFwHpiAVE0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.div1, new ArrayList<>(list1), "https://youtu.be/5VaqKu0ENlY"));

        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add3, new ArrayList<>(list2), "https://youtu.be/TBzsG75tvhw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.sub3, new ArrayList<>(list2), "https://youtu.be/f0HPkXpzKf0"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.mul2, new ArrayList<>(list2), "https://youtu.be/Yo_6G5TrNqo"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.div2, new ArrayList<>(list2), "https://youtu.be/2muobEZUalE"));

        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add4, new ArrayList<>(list4), "https://youtu.be/fu7K8QFPt1o"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add5, new ArrayList<>(list4), "https://youtu.be/2BGAUI2cdyw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.sub4, new ArrayList<>(list4), "https://youtu.be/83ePrGNcUiw"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.sub5, new ArrayList<>(list4), "https://youtu.be/pvU6eNBKfaI"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.mul3, new ArrayList<>(list4), "https://youtu.be/RUGs1NmEikQ"));
        database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.div3, new ArrayList<>(list4), "https://youtu.be/PFfcC6MO660"));

        database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocabulary, new ArrayList<>(list1), ""));


        grade1 = getIntent().getStringExtra("grade");

        grade.setText(grade1);
        if (grade1.equals(getResources().getString(R.string.grade1))) {
            desc.setText(R.string.grade_1_data);
        } else if (grade1.equals(getResources().getString(R.string.grade2))) {
            desc.setText(R.string.grade_2_data);
        } else {
            desc.setText(R.string.grade_3_data);
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GradeActivity.this, Select_Sub_Activity.class);
                //intent.putExtra("name",getIntent().getStringExtra("name"));
                //intent.putExtra("image",getIntent().getStringExtra("image"));
                //intent.putExtra("age",getIntent().getStringExtra("age"));
                intent.putExtra("grade", grade1);
                startActivity(intent);
                finish();
            }
        });

    }

    private void retrieveKidsData() {

        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        //     Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                        Log.i("No_data", "No_data");
                    } else {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                            KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                            kidsData.setKids_id(queryDocumentSnapshot.getId());
                            PrefConfig.writeIdInPref(getApplicationContext(),kidsData.getGrade(),"KIDS_GRADE");
                            Log.i("KidsData", kidsData.getName() + "");

                        }
                    }
                });
    }
}