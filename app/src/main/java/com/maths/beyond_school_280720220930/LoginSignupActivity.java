package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.ActivityLoginSignupBinding;
import com.maths.beyond_school_280720220930.signin_methods.GoogleSignInActivity;
import com.maths.beyond_school_280720220930.signin_methods.PhoneNumberLogin;

import java.util.ArrayList;
import java.util.List;

public class LoginSignupActivity extends AppCompatActivity {


    private ActivityLoginSignupBinding binding;
    GradeDatabase database;
    Grades_data data;
    String[] desc1 = {""};
    List<String> list1, list2, list4;
    List<String> en1, en2, en4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googleSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
        });

        binding.phoneNumberSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PhoneNumberLogin.class));
        });

        insertData();


    }

    private void insertData() {


        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.is_data_loaded)).equals("")) {


            PrefConfig.writeIdInPref(getApplicationContext(), "YES", getResources().getString(R.string.is_data_loaded));


            database = GradeDatabase.getDbInstance(this);

            var englishGradeDatabase = EnglishGradeDatabase.getDbInstance(this);
            englishGradeDatabase.englishDao().getEnglishModel(1);

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
            en1 = new ArrayList<>();
            en1.add("GRADE 1");
            en2 = new ArrayList<>();
            en2.add("GRADE 2");
            en4 = new ArrayList<>();
            en4.add("GRADE 3");

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.add1),true,true,true,true,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.add2),true,true,true,false,"https://youtu.be/RKL0TX8ogmw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.sub1),true,true,true,true,"https://youtu.be/ShCq1BVVbQ0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.sub2),true,true,true,false,"https://youtu.be/sBJp_Toqlhw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.mul1),true,true,true,true,"https://youtu.be/fZFwHpiAVE0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.div1),true,true,true,true,"https://youtu.be/5VaqKu0ENlY"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.add3),false,true,true,false,"https://youtu.be/TBzsG75tvhw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.sub3),false,true,true,false,"https://youtu.be/f0HPkXpzKf0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.mul2),false,true,true,false,"https://youtu.be/Yo_6G5TrNqo"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.div2),false,true,true,false,"https://youtu.be/2muobEZUalE"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.add4),false,false,true,false,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.add5),false,false,true,false,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.sub4),false,false,true,false,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.sub5),false,false,true,false,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.mul3),false,false,true,false,"https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math),getResources().getString(R.string.div3),false,false,true,false,"https://youtu.be/1RaL_2okktE"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab1), true,false,false, true,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab2), true,false,false,false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab3),true,false,false,false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab4),true,false,false, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab5), true,false,false, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab6), true,false,false, false,""));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab7, new ArrayList<>(list1), ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab8), false,true,false,true, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab9), false,true,false, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab10), false,true,false,false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab11), false,true,false,false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab12), false,true,false,false, ""));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab13, new ArrayList<>(list2), ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab14),false,false,true, true,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab15), false,false,true, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab16),false,false,true, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab17), false,false,true, false,""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab18), false,false,true,false, ""));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab19, new ArrayList<>(list2), ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english),getResources().getString(R.string.spelling),true,true,true,true,""));
           /* database.gradesDao().insertNotes(new Grades_data(R.string.math, R.string.add1, new ArrayList<>(list1), "https://youtu.be/1RaL_2okktE"));
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

            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab1, new ArrayList<>(en1), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab2, new ArrayList<>(en1), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab3, new ArrayList<>(en1), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab4, new ArrayList<>(en1), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab5, new ArrayList<>(en1), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab6, new ArrayList<>(en1), ""));
//            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab7, new ArrayList<>(list1), ""));

            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab8, new ArrayList<>(en2), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab9, new ArrayList<>(en2), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab10, new ArrayList<>(en2), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab11, new ArrayList<>(en2), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab12, new ArrayList<>(en2), ""));
//            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab13, new ArrayList<>(list2), ""));

            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab14, new ArrayList<>(en4), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab15, new ArrayList<>(en4), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab16, new ArrayList<>(en4), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab17, new ArrayList<>(en4), ""));
            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab18, new ArrayList<>(en4), ""));
//            database.gradesDao().insertNotes(new Grades_data(R.string.english, R.string.vocab19, new ArrayList<>(list2), ""));

            database.gradesDao().insertNotes(new Grades_data(R.string.english,R.string.spelling,new ArrayList<>(list1),""));*/
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}