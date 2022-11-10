package com.maths.beyond_school_new_071020221400;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.databinding.ActivityLoginSignupBinding;
import com.maths.beyond_school_new_071020221400.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_new_071020221400.signin_methods.GoogleSignInActivity;
import com.maths.beyond_school_new_071020221400.utils.typeconverters.GradeConverter;

import java.util.ArrayList;
import java.util.List;

public class LoginSignupActivity extends AppCompatActivity {


    private ActivityLoginSignupBinding binding;
    GradeDatabase database;
    Grades_data data;
    String[] desc1 = {""};
    List<String> list1, list2, list4;
    List<String> en1, en2, en4;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private int REQUEST_CALENDER_ACCESS = 100;
    private String TAG = "LoginSignUp";
    private GradeDatabase gradeDatabase;
    private List chapterListEng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        insertData();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        gradeDatabase = GradeDatabase.getDbInstance(this);
        binding.googleSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
        });

        chapterListEng=new ArrayList<>();

        PrefConfig.writeNormalListInPref(LoginSignupActivity.this, chapterListEng, getResources().getString(R.string.saved_english_value));
        binding.phoneNumberSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EnterPhoneNumberActivity.class);
            startActivity(intent);
            finish();
        });

      //  checkCalenderPermission();

        if (mUser != null) {
            checkUserAlreadyAvailable();
        }
//        getNewData();

//        startActivity(new Intent(this, ViewCurriculum.class));

    }

    private void checkCalenderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
            if (ContextCompat.checkSelfPermission(this, String.valueOf(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR})) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, REQUEST_CALENDER_ACCESS);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_CALENDER_ACCESS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
             //   checkCalenderPermission();
            }
        }

    }

    private void checkUserAlreadyAvailable() {

        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)).equals("")) {
            var intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
            intent.putExtra("type", "next");
            startActivity(intent);
            finish();

        } else {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
//            startActivity(new Intent(getApplicationContext(), EnglishActivity.class));
            finish();
        }

        finish();
    }

    private void insertData() {


        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.is_data_loaded)).equals("")) {


            PrefConfig.writeIdInPref(getApplicationContext(), "YES", getResources().getString(R.string.is_data_loaded));


            database = GradeDatabase.getDbInstance(this);

            var englishGradeDatabase = EnglishGradeDatabase.getDbInstance(this);
            englishGradeDatabase.englishDao().getEnglishModel(1);
            englishGradeDatabase.spellingDao();
//            englishGradeDatabase.expressionDao().getEnglishModel(1);

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
            //for language to set initial value
            PrefConfig.writeIdInPref(getApplicationContext(), "India", "Country");
            PrefConfig.writeIdInPref(getApplicationContext(), "IN", "Country_code");
            PrefConfig.writeIntInPref(getApplicationContext(), 0, "set_select");

            //database
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add1), true, true, true, true, true, true, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add2), true, true, true, true, true, false, false, "https://youtu.be/RKL0TX8ogmw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub1), true, true, true, true, true, true, false, "https://youtu.be/ShCq1BVVbQ0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub2), true, true, true, true, true, false, false, "https://youtu.be/sBJp_Toqlhw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul1), true, true, true, true, true, true, false, "https://youtu.be/fZFwHpiAVE0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div1), true, true, true, true, true, true, false, "https://youtu.be/5VaqKu0ENlY"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add3), false, true, true, true, true, false, false, "https://youtu.be/TBzsG75tvhw"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub3), false, true, true, true, true, false, false, "https://youtu.be/f0HPkXpzKf0"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul2), false, true, true, true, true, false, false, "https://youtu.be/Yo_6G5TrNqo"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div2), false, true, true, true, true, false, false, "https://youtu.be/2muobEZUalE"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add4), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add5), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub4), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub5), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul3), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div3), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE"));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab1), true, true, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab1_1), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab2), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab2_1), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab3), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab3_1), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab4), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab4_1), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab5), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab5_1), true, false, false, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab6), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab6_1), true, true, true, true, true, false, false, ""));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab7, new ArrayList<>(list1), ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab8), false, true, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab8_1), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab9), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab9_1), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab10), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab10_1), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab11), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab11_1), false, true, true, true, true, false, false, ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab12), true, true, true, true, true, false, false, ""));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab13, new ArrayList<>(list2), ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab14), false, false, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab15), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab16), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab17), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab18), false, false, true, true, true, false, false, ""));


            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_1), true, true, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_3), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_5), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_7), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_9), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_11), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_13), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_15), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_18), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_20), true, true, true, true, true, false, false, ""));


            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_2), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_4), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_6), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_8), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_10), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_12), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_14), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_16), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_19), false, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_21), false, true, true, true, true, false, false, ""));


            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_17), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_22), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_23), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_24), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_25), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_26), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_27), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_28), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_29), false, false, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_30), false, false, true, true, true, false, false, ""));

            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_1), true, true, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_2), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_3), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_4), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_5), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_6), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_7), true, true, true, true, true, false, false, ""));


            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_1), true, true, true, true, true, true, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_2), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_3), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_4), true, true, true, true, true, false, false, ""));
            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_5), true, true, true, true, true, false, false, ""));



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


//    private void getNewData() {
//        Retrofit retrofit = ApiClient.getClient();
//        var api = retrofit.create(ApiInterface.class);
//        api.getGradeData("grade1").enqueue(new retrofit2.Callback<>() {
//            private Call<GradeModel> call;
//            private Response<GradeModel> response;
//
//            @Override
//            public void onResponse(@NonNull retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, @NonNull retrofit2.Response<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> response) {
//                this.call = call;
//                this.response = response;
//                if (response.body() != null) {
//                    Log.d(TAG, "onResponse: " + response.code());
//                    Log.d(TAG, "onResponse: " + response.body().getEnglish().toString());
//                    var list = response.body().getEnglish();
//                    mapToGradeModel(list);
//                } else {
//                    Toast.makeText(LoginSignupActivity.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
//            }
//        });
//    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
            gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });
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