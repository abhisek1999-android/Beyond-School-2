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


            database.gradesDao().insertNotes(new Grades_data("mul2", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[0], getResources().getString(R.string.math), true, false));
            database.gradesDao().insertNotes(new Grades_data("mul3", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[1], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul4", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[2], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul5", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[3], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul6", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[4], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul7", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[5], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul8", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[6], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul9", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[7], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul10", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[8], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul11", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[9], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul12", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[10], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul13", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[11], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul14", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[12], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul15", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[13], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul16", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[14], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul17", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[15], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul18", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[16], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul19", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[17], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("mul20", getResources().getString(R.string.mul), getResources().getStringArray(R.array.table_name)[18], getResources().getString(R.string.math), false, false));
            database.gradesDao().insertNotes(new Grades_data("div1", getResources().getString(R.string.div), getResources().getString(R.string.div1), getResources().getString(R.string.math), false, false));


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