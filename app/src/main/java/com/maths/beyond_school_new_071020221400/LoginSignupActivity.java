package com.maths.beyond_school_new_071020221400;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradesDao;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.databinding.ActivityLoginSignupBinding;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.Chapter;
import com.maths.beyond_school_new_071020221400.retrofit.model.ResponseSubjects;
import com.maths.beyond_school_new_071020221400.signin_methods.GoogleSignInActivity;
import com.maths.beyond_school_new_071020221400.signin_methods.PhoneNumberLogin;
import com.maths.beyond_school_new_071020221400.utils.GradeConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "LoginSignUp";

    private GradesDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        insertData();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.googleSignIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
        });

        binding.phoneNumberSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PhoneNumberLogin.class);
            startActivity(intent);
            finish();
        });

        checkCalenderPermission();

        if (mUser != null) {
            checkUserAlreadyAvailable();
        }

        setUpRemoteConfig();
        var gradeDatabase = GradeDatabase.getDbInstance(this);
        dao = gradeDatabase.gradesDao();
        getData();

    }

    private void setUpRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        var value = PrefConfig.readIntInPref(this,
                getResources().getString(R.string.KEY_VALUE_SAVE)
                , 0);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            int val = (int) mFirebaseRemoteConfig.getLong("data_updated_value");
                            if (value != val) {
                                PrefConfig.writeIntInPref(
                                        LoginSignupActivity.this,
                                        val,
                                        getResources().getString(R.string.KEY_VALUE_SAVE)
                                );
                                getData();
                            }

                            Log.d(TAG, "Config params updated: " + updated + ", val:" + val);
                            return;
                        }
                        Toast.makeText(LoginSignupActivity.this, "Fetch failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });


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
                checkCalenderPermission();
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


    private void getData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getTodos().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSubjects> call,
                                   @NonNull Response<ResponseSubjects> response) {
                var res = response.body();
                if (res != null) {
                    var english = res.getEnglish();
                    var maths = res.getMaths();
                    convertDataEnglish(english, maths);
                    Log.d(TAG, "onResponse: Data Fetched" + response.code());
                } else {
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSubjects> call, @NonNull Throwable t) {
                //TODO: DISPLAY ERROR
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void convertDataEnglish(List<Chapter> english, List<Chapter> maths) {
        english.forEach(
                chapter -> {
                    var chapters = chapter.getChapters();
                    var converter = new GradeConverter();
                    var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
                    dao.insetAll(listOfGradeData);
                }
        );
        maths.forEach(chapter -> {
            var chapters = chapter.getChapters();
            var converter = new GradeConverter();
            var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
            dao.insetAll(listOfGradeData);
        });
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

//            //database
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add1), true, true, true, true, true, true, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add2), true, true, true, true, true, false, false, "https://youtu.be/RKL0TX8ogmw", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub1), true, true, true, true, true, true, false, "https://youtu.be/ShCq1BVVbQ0", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub2), true, true, true, true, true, false, false, "https://youtu.be/sBJp_Toqlhw", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul1), true, true, true, true, true, true, false, "https://youtu.be/fZFwHpiAVE0", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div1), true, true, true, true, true, true, false, "https://youtu.be/5VaqKu0ENlY", false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add3), false, true, true, true, true, false, false, "https://youtu.be/TBzsG75tvhw", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub3), false, true, true, true, true, false, false, "https://youtu.be/f0HPkXpzKf0", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul2), false, true, true, true, true, false, false, "https://youtu.be/Yo_6G5TrNqo", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div2), false, true, true, true, true, false, false, "https://youtu.be/2muobEZUalE", false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add4), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.add5), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub4), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.sub5), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.mul3), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.math), getResources().getString(R.string.div3), false, false, true, true, true, false, false, "https://youtu.be/1RaL_2okktE", false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab1), true, true, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab1_1), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab2), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab2_1), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab3), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab3_1), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab4), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab4_1), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab5), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab5_1), true, false, false, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab6), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab6_1), true, true, true, true, true, false, false, "", false));
////            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab7, new ArrayList<>(list1), "",false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab8), false, true, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab8_1), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab9), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab9_1), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab10), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab10_1), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab11), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab11_1), false, true, true, true, true, false, false, "", false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab12), true, true, true, true, true, false, false, "", false));
////            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), R.string.vocab13, new ArrayList<>(list2), "",false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab14), false, false, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab15), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab16), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab17), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.vocab18), false, false, true, true, true, false, false, "", false));
//
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_1), true, true, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_3), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_5), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_7), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_9), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_11), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_13), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_15), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_18), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_20), true, true, true, true, true, false, false, "", false));
//
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_2), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_4), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_6), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_8), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_10), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_12), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_14), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_16), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_19), false, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_21), false, true, true, true, true, false, false, "", false));
//
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_17), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_22), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_23), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_24), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_25), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_26), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_27), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_28), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_29), false, false, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_30), false, false, true, true, true, false, false, "", false));
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_1), true, true, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_2), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_3), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_4), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_5), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_6), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.spelling_common_words_7), true, true, true, true, true, false, false, "", false));
//
//
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_1), true, true, true, true, true, true, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_2), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_3), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_4), true, true, true, true, true, false, false, "", false));
//            database.gradesDao().insertNotes(new Grades_data(getResources().getString(R.string.english), getResources().getString(R.string.grammar_5), true, true, true, true, true, false, false, "", false));

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